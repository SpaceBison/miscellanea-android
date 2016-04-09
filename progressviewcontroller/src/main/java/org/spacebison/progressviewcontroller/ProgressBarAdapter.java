package org.spacebison.progressviewcontroller;

import android.os.Build;
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
    public void setIndeterminate(boolean indeterminate) {
        mProgressBar.setMax(100);
        mProgressBar.setIndeterminate(indeterminate);
    }

    @Override
    public boolean isIndeterminate() {
        return mProgressBar.isIndeterminate();
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            mProgressBar.animate().alpha(visible ? 1 : 0);
        } else {
            mProgressBar.post(new Runnable() {
                @Override
                public void run() {
                    mProgressBar.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
                }
            });
        }
    }
}
