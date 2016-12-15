package org.spacebison.progressviewcontroller;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

/**
 *
 * Created by cmb on 09.04.16.
 */
public class ProgressBarAdapter implements ProgressView {
    private static final String TAG = "ProgressBarAdapter";
    private final ProgressBar mProgressBar;

    public ProgressBarAdapter(ProgressBar progressBar) {
        mProgressBar = progressBar;
    }

    @Override
    public void setIndeterminate(final boolean indeterminate) {
        Log.d(TAG, "set indeterminate: " + indeterminate);

        mProgressBar.post(new Runnable() {
            @Override
            public void run() {
                if (indeterminate) {
            /* this is a workaround for a vanishing progressbar */
                    mProgressBar.setMax(100);
                    mProgressBar.setProgress(100);
                    mProgressBar.setIndeterminate(true);
                    mProgressBar.setMax(1);
                    mProgressBar.setProgress(0);
                } else {
                    mProgressBar.setIndeterminate(false);
                    mProgressBar.setProgress(0);
                }
            }
        });
    }

    @Override
    public void setProgress( int progress) {
        Log.d(TAG, "set progress: " + progress);
        mProgressBar.setProgress(progress);
    }

    @Override
    public int getProgress() {
        return mProgressBar.getProgress();
    }

    @Override
    public void setMaxProgress(final int maxProgress) {
        Log.d(TAG, "set max progress: " + maxProgress);
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
            mProgressBar.post(new Runnable() {
                @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
                @Override
                public void run() {
                    mProgressBar.animate().alpha(visible ? 1 : 0);
                }
            });
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
