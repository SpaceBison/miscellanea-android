package org.spacebison.progressviewcontroller;

import android.util.Log;
import android.widget.ProgressBar;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by wmatuszewski on 4/8/16.
 */
public class ProgressViewController {
    private static final String TAG = "ProgressViewController";
    private final AtomicInteger mIndeterminateTasks = new AtomicInteger(0);
    private final HashMap<String, Task> mTasks = new HashMap<>();
    private final Task mSumTask = new Task(0,0);
    private final ProgressView mProgressView;

    public ProgressViewController(ProgressView progressView) {
        mProgressView = progressView;
        updateVisibility();
    }

    public ProgressViewController(ProgressBar progressBar) {
        this(new ProgressBarAdapter(progressBar));
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
            updateProgress();
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
            updateProgress();

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
            updateProgress();
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
            updateProgress();

            mTasks.remove(id);
            if (mTasks.isEmpty()) {
                updateVisibility();
            }
        }
    }

    private void updateProgress() {
        if (mProgressView.getMaxProgress() != mSumTask.maxProgress) {
            mProgressView.setMaxProgress(mSumTask.maxProgress);
        }

        if (mProgressView.getProgress() != mSumTask.progress) {
            mProgressView.setProgress(mSumTask.progress);
        }
    }

    private void updateVisibility() {
        boolean noTasks;
        synchronized (mTasks) {
            noTasks = mTasks.isEmpty();
        }
        final boolean noIndeterminateTasks = mIndeterminateTasks.get() <= 0;

        setIndeterminate(noTasks);

        final boolean visible = !(noTasks && noIndeterminateTasks);
        Log.d(TAG, "Setting visibile: " + visible);
        mProgressView.setVisible(visible);
    }

    private void setIndeterminate(boolean indeterminate) {
        if (mProgressView.isIndeterminate() != indeterminate) {
            mProgressView.setIndeterminate(indeterminate);
        }
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
