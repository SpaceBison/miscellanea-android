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
        mProgressView.startAnimation();
    }

    @Override
    public boolean isIndeterminate() {
        return mProgressView.isIndeterminate();
    }

    @Override
    public void setProgress(int progress) {
        mProgressView.setProgress(progress);
    }

    @Override
    public int getProgress() {
        return (int) mProgressView.getProgress();
    }

    @Override
    public void setMaxProgress(int maxProgress) {
        mProgressView.setMaxProgress(maxProgress);
    }

    @Override
    public int getMaxProgress() {
        return (int) mProgressView.getMaxProgress();
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            mProgressView.startAnimation();
            ViewCompat.animate(mProgressView).alpha(1);
        } else {
            ViewCompat.animate(mProgressView).alpha(0).withEndAction(new Runnable() {
                @Override
                public void run() {
                    mProgressView.stopAnimation();
                }
            });
        }
    }

    @Override
    public boolean isVisible() {
        return ViewCompat.getAlpha(mProgressView) > 0.5;
    }

    @Override
    public void post(Runnable runnable) {
        mProgressView.post(runnable);
    }
}
