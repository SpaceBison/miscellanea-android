package com.spacebison.mediaplayertest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.spacebison.recyclerviewlistadapter.BindableViewHolder;
import org.spacebison.recyclerviewlistadapter.RecyclerViewListAdapter;
import org.spacebison.statefulmediaplayer.MediaError;
import org.spacebison.statefulmediaplayer.StatefulMediaPlayer;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "cmb.MainActivity";

    private StatefulMediaPlayer mMediaPlayer;
    private EventAdapter mAdapter = new EventAdapter();
    private TextView mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter);

        mText = (TextView) findViewById(R.id.text);

        final ImageView button = (ImageView) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (mMediaPlayer.getState()) {
                    case PREPARED:
                    case PLAYBACK_COMPLETED:
                    case PAUSED:
                        mMediaPlayer.start();
                        break;

                    case STARTED:
                        mMediaPlayer.pause();
                        break;

                    case INITIALIZED:
                    case STOPPED:
                        mMediaPlayer.prepareAsync();
                        break;

                    case ERROR:
                        break;
                    case RELEASED:
                        break;
                    case IDLE:
                        break;
                    case PREPARING:
                        break;
                }
            }
        });

        mMediaPlayer = new StatefulMediaPlayer();
        mMediaPlayer.setOnErrorListener(new StatefulMediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(StatefulMediaPlayer mp, MediaError error, int extra) {
                Log.e(TAG, "Error: " + error);
                mAdapter.add(0, new MediaEvent(error, extra));
                recyclerView.smoothScrollToPosition(0);
                return false;
            }
        });
        mMediaPlayer.setOnStateChangedListener(new StatefulMediaPlayer.OnStateChangedListener() {
            @Override
            public void onStateChanged(StatefulMediaPlayer mp, StatefulMediaPlayer.State state) {
                Log.e(TAG, "State: " + state);

                mAdapter.add(0, new MediaEvent(state));
                recyclerView.smoothScrollToPosition(0);

                if (state == StatefulMediaPlayer.State.STARTED) {
                    button.setImageResource(R.drawable.ic_pause_black_24dp);
                } else {
                    button.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                }
            }
        });

        if (checkCallingOrSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.READ_EXTERNAL_STORAGE }, 0xEEEE);
        } else {
            initPlayer();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (checkCallingOrSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.READ_EXTERNAL_STORAGE }, 0xEEEE);
        } else {
            initPlayer();
        }
    }

    private void initPlayer() {
        Uri data = getIntent().getData();
        Log.d(TAG, "Data: " + data);

        if (data != null) {
            mText.setText(data.toString());

            try {
                mMediaPlayer.setDataSource(data.toString());
                mMediaPlayer.prepare();
            } catch (IOException e) {
                Log.e(TAG, "Could not open " + data + ": " + e);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaPlayer.release();
    }

    public static class EventAdapter extends RecyclerViewListAdapter<MediaEvent, EventViewHolder> {
        @Override
        public EventViewHolder onCreateBindableViewHolder(ViewGroup parent, int viewType) {
            return new EventViewHolder(parent);
        }
    }

    public static class MediaEvent {
        private final StatefulMediaPlayer.State mState;
        private final MediaError mMediaError;
        private final int mExtra;

        public MediaEvent(StatefulMediaPlayer.State state) {
            this(state, null, 0);
        }

        public MediaEvent(MediaError error, int extra) {
            this(StatefulMediaPlayer.State.ERROR, error, extra);
        }

        private MediaEvent(StatefulMediaPlayer.State state, MediaError mediaError, int extra) {
            mState = state;
            mMediaError = mediaError;
            mExtra = extra;
        }
    }

    public static class EventViewHolder extends BindableViewHolder<MediaEvent> {
        private TextView mText;

        /**
         * Constructor.
         *
         * @param parent The ViewGroup into which the new View will be added after it is bound to an
         *               adapter position.
         */
        public EventViewHolder(@NonNull ViewGroup parent) {
            super(parent, R.layout.item_state);
            mText = (TextView) itemView;
        }

        @Override
        public void bind(MediaEvent data) {
            if (data.mMediaError == null) {
                mText.setText(data.mState.toString());
            } else {
                mText.setText(data.mState.toString() + " " + data.mMediaError + " " + data.mExtra);
            }
        }
    }
}
