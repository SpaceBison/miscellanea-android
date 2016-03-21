package org.spacebison.miscellanea;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.spacebison.statefulmediaplayer.MediaError;
import org.spacebison.statefulmediaplayer.MediaInfo;
import org.spacebison.statefulmediaplayer.StatefulMediaPlayer;

/**
 * Created by cmb on 20.03.16.
 */
public class StatefulMediaPlayerTestActivity extends AppCompatActivity implements StatefulMediaPlayer.OnStateChangedListener, StatefulMediaPlayer.OnPreparedListener, StatefulMediaPlayer.OnErrorListener, StatefulMediaPlayer.OnInfoListener {
    private static final String TAG = "SMPTestActivity";
    private StatefulMediaPlayer mPlayer;
    private TextView mText;
    private ViewGroup mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stateful_mp);

        mContent = (ViewGroup) findViewById(android.R.id.content);
        mText = (TextView) findViewById(R.id.text);

        mPlayer = StatefulMediaPlayer.create(this, R.raw.jam5);
        mPlayer.setOnStateChangedListener(this);
        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnErrorListener(this);
        mPlayer.setOnInfoListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayer.release();
    }

    @Override
    public void onStateChanged(StatefulMediaPlayer mp, StatefulMediaPlayer.State state) {
        final String text = "State " + state;
        Log.d(TAG, text);
        mText.setText(text);
    }

    public void onPlayClick(View view) {
        try {
            mPlayer.start();
        } catch (IllegalStateException e) {
            Toast.makeText(this, "Illegal state: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    public void onPauseClick(View view) {
        try {
            mPlayer.pause();
        } catch (IllegalStateException e) {
            Toast.makeText(this, "Illegal state: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    public void onStopClick(View view) {
        try {
            mPlayer.stop();
        } catch (IllegalStateException e) {
            Toast.makeText(this, "Illegal state: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    public void onResetClick(View view) {
        try {
            mPlayer.reset();
        } catch (IllegalStateException e) {
            Toast.makeText(this, "Illegal state: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPrepared(StatefulMediaPlayer mp) {
        Log.d(TAG, "Prepared");
        Toast.makeText(this, "Prepared", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onError(StatefulMediaPlayer mp, MediaError error, int extra) {
        Log.d(TAG, "Error: " + error);
        Toast.makeText(this, "Error: " + error, Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public boolean onInfo(StatefulMediaPlayer mp, MediaInfo info, int extra) {
        Log.d(TAG, "Info: " + info);
        return false;
    }
}
