package org.spacebison.progressviewcontroller;

/**
 * Created by cmb on 09.04.16.
 */
public interface ProgressView {
    void setIndeterminate(boolean indeterminate);
    boolean isIndeterminate();
    void setProgress(int progress);
    int getProgress();
    void setMaxProgress(int maxProgress);
    int getMaxProgress();
    void setVisible(boolean visible);
}
