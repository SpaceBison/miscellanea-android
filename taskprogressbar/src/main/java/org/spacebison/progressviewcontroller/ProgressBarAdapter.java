package org.spacebison.progressviewcontroller;

import android.support.v4.view.ViewCompat;
import android.widget.ProgressBar;

/**
 *
 * Created by cmb on 09.04.16.
 */
class ProgressBarAdapter implements ProgressView {
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
    public void setVisible(boolean visible) {
        ViewCompat.animate(mProgressBar).alpha(visible ? 1 : 0);
        //mProgressBar.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public boolean isVisible() {
        return ViewCompat.getAlpha(mProgressBar) > 0.5;
        //return mProgressBar.getVisibility() == View.VISIBLE;
    }

    @Override
    public void post(Runnable runnable) {
        mProgressBar.post(runnable);
    }
}
