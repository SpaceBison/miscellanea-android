package org.spacebison.miscellanea;

import android.support.v4.view.ViewCompat;

import com.github.rahatarmanahmed.cpv.CircularProgressView;

import org.spacebison.progressviewcontroller.ProgressView;

/**
 * Created by cmb on 09.04.16.
 */
public class CircularProgressViewAdapter implements ProgressView {
    private CircularProgressView mProgressView;

    public CircularProgressViewAdapter(CircularProgressView progressView) {
        mProgressView = progressView;
    }

    @Override
    public void setIndeterminate(boolean indeterminate) {
        mProgressView.setIndeterminate(indeterminate);
        mProgressView.post(new Runnable() {
            @Override
            public void run() {
                mProgressView.startAnimation();
            }
        });
    }

    @Override
    public boolean isIndeterminate() {
        return mProgressView.isIndeterminate();
    }

    @Override
    public void setProgress(final int progress) {
        mProgressView.post(new Runnable() {
            @Override
            public void run() {
                mProgressView.setProgress(progress);
            }
        });
    }

    @Override
    public int getProgress() {
        return (int) mProgressView.getProgress();
    }

    @Override
    public void setMaxProgress(final int maxProgress) {
        mProgressView.post(new Runnable() {
            @Override
            public void run() {
                mProgressView.setMaxProgress(maxProgress);
            }
        });
    }

    @Override
    public int getMaxProgress() {
        return (int) mProgressView.getMaxProgress();
    }

    @Override
    public void setVisible(final boolean visible) {
        mProgressView.post(new Runnable() {
            @Override
            public void run() {
                if (visible) {
                    if (mProgressView.getAlpha() < 0.5) {
                        mProgressView.startAnimation();
                        ViewCompat.animate(mProgressView).alpha(1);
                    }
                } else {
                    if (mProgressView.getAlpha() >= 0.5) {
                        ViewCompat.animate(mProgressView).alpha(0).withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                mProgressView.stopAnimation();
                            }
                        });
                    }
                }
            }
        });
    }
}
