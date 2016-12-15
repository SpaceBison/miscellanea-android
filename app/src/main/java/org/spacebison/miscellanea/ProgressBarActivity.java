package org.spacebison.miscellanea;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;

import org.spacebison.progressviewcontroller.ProgressBarAdapter;

public class ProgressBarActivity extends AppCompatActivity {

    private ProgressBar mProgressBar;
    private SeekBar mProgress;
    private SeekBar mMaxProgress;
    private SwitchCompat mIndeterminate;
    private SwitchCompat mVisible;

    private ProgressBarAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_bar);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mProgress = (SeekBar) findViewById(R.id.progress);
        mMaxProgress = (SeekBar) findViewById(R.id.max_progress);
        mIndeterminate = (SwitchCompat) findViewById(R.id.indeterminate);
        mVisible = (SwitchCompat) findViewById(R.id.visible);

        mAdapter = new ProgressBarAdapter(mProgressBar);

        mProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mAdapter.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mMaxProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mAdapter.setMaxProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mIndeterminate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mAdapter.setIndeterminate(isChecked);
            }
        });

        mVisible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mAdapter.setVisible(isChecked);
            }
        });
    }
}
