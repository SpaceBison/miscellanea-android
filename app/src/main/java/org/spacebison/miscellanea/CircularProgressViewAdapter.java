package org.spacebison.miscellanea;

import android.view.View;

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
            mProgressView.setVisibility(View.VISIBLE);
            mProgressView.startAnimation();
        } else {
            mProgressView.setVisibility(View.INVISIBLE);
            mProgressView.stopAnimation();
        }

    }

    @Override
    public boolean isVisible() {
        return mProgressView.getVisibility() == View.VISIBLE;
    }

    @Override
    public void post(Runnable runnable) {
        mProgressView.post(runnable);
    }
}
