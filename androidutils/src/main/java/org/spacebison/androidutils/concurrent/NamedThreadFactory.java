package org.spacebison.androidutils.concurrent;

import android.support.annotation.NonNull;

import java.util.Locale;
import java.util.concurrent.ThreadFactory;

public class NamedThreadFactory implements ThreadFactory {
    private final String mNamePattern;
    private int mThreads = 0;

    public NamedThreadFactory(String namePattern) {
        mNamePattern = namePattern;
    }

    @Override
    public Thread newThread(@NonNull Runnable runnable) {
        return new Thread(runnable, String.format(Locale.US, mNamePattern, ++mThreads));
    }
}
