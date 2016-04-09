package org.spacebison.progressviewcontroller;

/**
 * Created by cmb on 09.04.16.
 */
public interface ProgressView {
    void setIndeterminate(boolean indeterminate);
    void setProgress(int progress);
    int getProgress();
    void setMaxProgress(int maxProgress);
    int getMaxProgress();
    void setVisible(boolean visible);
}
