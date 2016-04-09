package org.spacebison.miscellanea;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import com.github.rahatarmanahmed.cpv.CircularProgressView;

import org.spacebison.progressviewcontroller.ProgressViewController;

import java.util.NoSuchElementException;

public class TaskProgressBarTestActivity extends AppCompatActivity {

    private ProgressViewController mCircleViewController;
    private ProgressViewController mProgressViewController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_progress_bar_test);
        mProgressViewController = new ProgressViewController((ProgressBar) findViewById(R.id.progress_bar));
        mCircleViewController = new ProgressViewController(new CircularProgressViewAdapter((CircularProgressView) findViewById(R.id.circular_progress_view)));

        SwitchCompat indeterminate1 = (SwitchCompat) findViewById(R.id.switch1);
        indeterminate1.setOnCheckedChangeListener(new IndeterminateSwitchListener());
        SwitchCompat indeterminate2 = (SwitchCompat) findViewById(R.id.switch2);
        indeterminate2.setOnCheckedChangeListener(new IndeterminateSwitchListener());
        SwitchCompat indeterminate3 = (SwitchCompat) findViewById(R.id.switch3);
        indeterminate3.setOnCheckedChangeListener(new IndeterminateSwitchListener());

        SeekBar seek1 = (SeekBar) findViewById(R.id.task_seek_bar1);
        seek1.setOnSeekBarChangeListener(new SeekBarChangeListener("task1"));
        SeekBar seek2 = (SeekBar) findViewById(R.id.task_seek_bar2);
        seek2.setOnSeekBarChangeListener(new SeekBarChangeListener("task2"));
        SeekBar seek3 = (SeekBar) findViewById(R.id.task_seek_bar3);
        seek3.setOnSeekBarChangeListener(new SeekBarChangeListener("task3"));

        SwitchCompat task1 = (SwitchCompat) findViewById(R.id.task_switch1);
        task1.setOnCheckedChangeListener(new TaskSwitchListener("task1", seek1.getMax()));
        SwitchCompat task2 = (SwitchCompat) findViewById(R.id.task_switch2);
        task2.setOnCheckedChangeListener(new TaskSwitchListener("task2", seek2.getMax()));
        SwitchCompat task3 = (SwitchCompat) findViewById(R.id.task_switch3);
        task3.setOnCheckedChangeListener(new TaskSwitchListener("task3", seek3.getMax()));
    }

    private class IndeterminateSwitchListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                mProgressViewController.notifyIndeterminateTaskStarted();
                mCircleViewController.notifyIndeterminateTaskStarted();
            } else {
                mProgressViewController.notifyIndeterminateTaskFinished();
                mCircleViewController.notifyIndeterminateTaskFinished();
            }
        }
    }

    private class TaskSwitchListener implements CompoundButton.OnCheckedChangeListener {
        private String mId;
        private int mMaxProgress;

        public TaskSwitchListener(String id, int maxProgress) {
            mId = id;
            mMaxProgress = maxProgress;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                mProgressViewController.notifyTaskStarted(mId, mMaxProgress);
                mCircleViewController.notifyTaskStarted(mId, mMaxProgress);
            } else {
                mProgressViewController.notifyTaskFinished(mId);
                mCircleViewController.notifyTaskFinished(mId);
            }
        }
    }

    private class SeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
        private String mId;

        public SeekBarChangeListener(String id) {
            mId = id;
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            try {
                mProgressViewController.notifyTaskProgressChanged(mId, progress);
                mCircleViewController.notifyTaskProgressChanged(mId, progress);
            } catch (NoSuchElementException ignored) {
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }
}

