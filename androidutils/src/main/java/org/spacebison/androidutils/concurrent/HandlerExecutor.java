package org.spacebison.androidutils.concurrent;

import android.os.Handler;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

/**
 * Created by cmb on 03.03.17.
 */

public class HandlerExecutor implements Executor {
    private final Handler mHandler;

    public HandlerExecutor(@NonNull Handler handler) {
        mHandler = handler;
    }

    @Override
    public void execute(@NonNull Runnable runnable) {
        mHandler.post(runnable);
    }
}
