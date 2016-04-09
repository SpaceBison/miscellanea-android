package org.spacebison.progressviewcontroller;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

/**
 *
 * Created by cmb on 09.04.16.
 */
class ProgressBarAdapter implements ProgressView {
    private static final String TAG = "ProgressBarAdapter";
    private final ProgressBar mProgressBar;

    public ProgressBarAdapter(ProgressBar progressBar) {
        mProgressBar = progressBar;
    }

    @Override
    public void setIndeterminate(final boolean indeterminate) {
        Log.d(TAG, "set indeterminate: " + indeterminate);
        mProgressBar.setIndeterminate(indeterminate);
    }

    @Override
    public void setProgress(int progress) {
        mProgressBar.setProgress(progress);
    }

    @Override
    public int getProgress() {
        return mProgressBar.getProgress();
    }

    @Override
    public void setMaxProgress(int maxProgress) {
        mProgressBar.setMax(maxProgress);
    }

    @Override
    public int getMaxProgress() {
        return mProgressBar.getMax();
    }

    @Override
    public void setVisible(final boolean visible) {
        Log.d(TAG, "set visible: " + visible);
        mProgressBar.post(new Runnable() {
            @Override
            public void run() {
                mProgressBar.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
            }
        });
    }
}
