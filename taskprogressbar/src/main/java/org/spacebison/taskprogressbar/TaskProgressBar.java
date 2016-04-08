package org.spacebison.taskprogressbar;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ProgressBar;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by wmatuszewski on 4/8/16.
 */
public class TaskProgressBar extends ProgressBar {
    private static final String TAG = "TaskProgressBar";
    private final AtomicInteger mIndeterminateTasks = new AtomicInteger(0);
    private final HashMap<String, Task> mTasks = new HashMap<>();
    private final Task mSumTask = new Task(0,0);

    /**
     * Create a new progress bar with range 0...100 and initial progress of 0.
     *
     * @param context the application environment
     */
    public TaskProgressBar(Context context) {
        super(context);
    }

    public TaskProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TaskProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TaskProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void notifyIndeterminateTaskStarted() {
        if (mIndeterminateTasks.getAndIncrement() == 0) {
            updateVisibility();
        }
    }

    public void notifyIndeterminateTaskFinished() {
        if (mIndeterminateTasks.getAndDecrement() == 1) {
            updateVisibility();
        }
    }

    public void notifyTaskStarted(String id) {
        synchronized (mTasks) {
            final Task newTask = new Task();
            mTasks.put(id, newTask);
            mSumTask.progress += newTask.progress;
            mSumTask.maxProgress += newTask.maxProgress;
            setMax(mSumTask.maxProgress);
            setProgress(mSumTask.progress);
            updateVisibility();
        }
    }

    public void notifyTaskStarted(String id, int maxProgress) {
        synchronized (mTasks) {
            final boolean firstTask = mTasks.isEmpty();
            final Task newTask = new Task(0, maxProgress);
            mTasks.put(id, newTask);
            mSumTask.progress += newTask.progress;
            mSumTask.maxProgress += newTask.maxProgress;
            setMax(mSumTask.maxProgress);
            setProgress(mSumTask.progress);

            if (firstTask) {
                updateVisibility();
            }
        }
    }

    public void notifyTaskProgressChanged(String id, int progress) {
        synchronized (mTasks) {
            Task task = mTasks.get(id);

            if (task == null) {
                throw new NoSuchElementException("No task with id: " + id);
            }

            mSumTask.progress -= task.progress;
            mSumTask.progress += progress;
            setMax(mSumTask.maxProgress);
            setProgress(mSumTask.progress);
            task.progress = progress;
        }
    }

    public void notifyTaskFinished(String id) {
        synchronized (mTasks) {
            Task task = mTasks.get(id);

            if (task == null) {
                throw new NoSuchElementException("No task with id: " + id);
            }

            mSumTask.maxProgress -= task.maxProgress;
            mSumTask.progress -= task.progress;
            setMax(mSumTask.maxProgress);
            setProgress(mSumTask.progress);

            mTasks.remove(id);
            if (mTasks.isEmpty()) {
                updateVisibility();
            }
        }
    }

    private void updateVisibility() {
        post(new Runnable() {
            @Override
            public void run() {
                synchronized (mTasks) {
                    final boolean noTasks = mTasks.isEmpty();
                    final boolean noIndeterminateTasks = mIndeterminateTasks.get() <= 0;

                    Log.d(TAG, "Update visibility; no tasks? " + noTasks + "; no indeterminate? " + noIndeterminateTasks);

                    setIndeterminate(noTasks);
                    if (noTasks && noIndeterminateTasks) {
                        Log.d(TAG, "Hiding");
                        ViewCompat.animate(TaskProgressBar.this).alpha(0);
                    } else {
                        Log.d(TAG, "Showing");
                        ViewCompat.animate(TaskProgressBar.this).alpha(1);
                    }
                }
            }
        });
    }

    private static class Task {
        public int maxProgress = 100;
        public int progress = 0;

        public Task() {
        }

        public Task(int progress, int maxProgress) {
            this.maxProgress = maxProgress;
            this.progress = progress;
        }
    }
}
