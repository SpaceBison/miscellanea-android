package org.spacebison.statefulmediaplayer;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.MediaDataSource;
import android.media.MediaPlayer;
import android.media.TimedMetaData;
import android.media.TimedText;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.EnumSet;
import java.util.Map;

import static org.spacebison.statefulmediaplayer.StatefulMediaPlayer.State.ERROR;
import static org.spacebison.statefulmediaplayer.StatefulMediaPlayer.State.IDLE;
import static org.spacebison.statefulmediaplayer.StatefulMediaPlayer.State.INITIALIZED;
import static org.spacebison.statefulmediaplayer.StatefulMediaPlayer.State.PAUSED;
import static org.spacebison.statefulmediaplayer.StatefulMediaPlayer.State.PLAYBACK_COMPLETED;
import static org.spacebison.statefulmediaplayer.StatefulMediaPlayer.State.PREPARED;
import static org.spacebison.statefulmediaplayer.StatefulMediaPlayer.State.PREPARING;
import static org.spacebison.statefulmediaplayer.StatefulMediaPlayer.State.RELEASED;
import static org.spacebison.statefulmediaplayer.StatefulMediaPlayer.State.STARTED;
import static org.spacebison.statefulmediaplayer.StatefulMediaPlayer.State.STOPPED;

/**
 * A {@link MediaPlayer} extension that keeps track of its state.
 * <p>
 *     {@link StatefulMediaPlayer} wraps all of {@link MediaPlayer}'s state-dependant methods, so
 *     that they are never called in an invalid state. The current state can also be checked
 *     using {@link StatefulMediaPlayer#getState()}.
 * </p>
 * <p>
 *     Replacements for listener setters are provided.
 *     listener calls pass a {@link StatefulMediaPlayer} reference for convenience. In case of
 *     {@link OnInfoListener} and {@link OnErrorListener}, error codes and info codes are translated
 *     to {@link MediaError} and {@link MediaInfo} enums. Note that the methods marked
 * </p>
 * <p>
 *     This class was inspired by <a href=https://gist.github.com/danielhawkes/1029568>danielhawkes' gist</a>
 * </p>
 */
@SuppressWarnings("unused")
public class StatefulMediaPlayer extends MediaPlayer {
    private static final String TAG = "StatefulMediaPlayer";
    private State mState;
    private OnStateChangedListener mOnStateChangedListener;
    private OnBufferingUpdateListener mOnBufferingUpdateListener;
    private OnCompletionListener mOnCompletionListener;
    private OnErrorListener mOnErrorListener;
    private OnInfoListener mOnInfoListener;
    private OnPreparedListener mOnPreparedListener;
    private OnSeekCompleteListener mOnSeekCompleteListener;
    private OnTimedMetaDataAvailableListener mOnTimedMetaDataAvailableListener;
    private OnTimedTextListener mOnTimedTextListener;
    private OnVideoSizeChangedListener mOnVideoSizeChangedListener;

    @SuppressWarnings("deprecation")
    public StatefulMediaPlayer() {
        final MediaPlayerListener listener = new MediaPlayerListener();
        setOnBufferingUpdateListener(listener);
        setOnCompletionListener(listener);
        setOnErrorListener(listener);
        setOnPreparedListener(listener);
        setOnSeekCompleteListener(listener);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            MediaPlayerCupcakeListener cupcakeListener = new MediaPlayerCupcakeListener();
            setOnInfoListener(cupcakeListener);
            setOnVideoSizeChangedListener(cupcakeListener);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                MediaPlayerJellyBeanListener jellyBeanListener = new MediaPlayerJellyBeanListener();
                setOnTimedTextListener(jellyBeanListener);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    MediaPlayerMarshmallowListener marshmallowListener = new MediaPlayerMarshmallowListener();
                    setOnTimedMetaDataAvailableListener(marshmallowListener);
                }
            }
        }

        setState(IDLE);
    }

    public State getState() {
        return mState;
    }

    /**
     * See {@link MediaPlayer#create(Context, Uri, SurfaceHolder, AudioAttributes, int)}
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static StatefulMediaPlayer create(Context context, Uri uri, SurfaceHolder holder, AudioAttributes audioAttributes, int audioSessionId) {
        try {
            StatefulMediaPlayer mp = new StatefulMediaPlayer();
            final AudioAttributes aa = audioAttributes != null ? audioAttributes :
                    new AudioAttributes.Builder().build();
            mp.setAudioAttributes(aa);
            mp.setAudioSessionId(audioSessionId);
            mp.setDataSource(context, uri);
            if (holder != null) {
                mp.setDisplay(holder);
            }
            mp.prepare();
            return mp;
        } catch (IOException | IllegalArgumentException | SecurityException ex) {
            Log.d(TAG, "create failed:", ex);
            // fall through
        }
        return null;
    }

    /**
     * See {@link MediaPlayer#create(Context, Uri, SurfaceHolder)}
     */
    public static StatefulMediaPlayer create(Context context, Uri uri, SurfaceHolder holder) {
        try {
            StatefulMediaPlayer mp = new StatefulMediaPlayer();
            mp.setDataSource(context, uri);
            if (holder != null) {
                mp.setDisplay(holder);
            }
            mp.prepare();
            return mp;
        } catch (IOException | IllegalArgumentException | SecurityException ex) {
            Log.d(TAG, "create failed:", ex);
            // fall through
        }
        return null;
    }

    /**
     * See {@link MediaPlayer#create(Context, int, AudioAttributes, int)}
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static StatefulMediaPlayer create(Context context, int resid, AudioAttributes audioAttributes, int audioSessionId) {
        try {
            AssetFileDescriptor afd = context.getResources().openRawResourceFd(resid);
            if (afd == null) return null;
            StatefulMediaPlayer mp = new StatefulMediaPlayer();
            final AudioAttributes aa = audioAttributes != null ? audioAttributes :
                    new AudioAttributes.Builder().build();
            mp.setAudioAttributes(aa);
            mp.setAudioSessionId(audioSessionId);
            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            mp.prepare();
            return mp;
        } catch (IOException | IllegalArgumentException | SecurityException ex) {
            Log.d(TAG, "create failed:", ex);
            // fall through
        }
        return null;
    }

    /**
     * See {@link MediaPlayer#create(Context, int)}
     */
    public static StatefulMediaPlayer create(Context context, int resid) {
        try {
            AssetFileDescriptor afd = context.getResources().openRawResourceFd(resid);
            if (afd == null) return null;
            StatefulMediaPlayer mp = new StatefulMediaPlayer();
            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            mp.prepare();
            return mp;
        } catch (IOException | SecurityException | IllegalArgumentException ex) {
            Log.d(TAG, "create failed:", ex);
            // fall through
        }
        return null;
    }

    /**
     * See {@link MediaPlayer#create(Context, Uri)}
     */
    public static StatefulMediaPlayer create(Context context, Uri uri) {
        return create(context, uri, null);
    }

    @Override
    public synchronized void setVideoScalingMode(int mode) {
        if (MethodStates.SET_VIDEO_SCALING_MODE.contains(mState)) {
            super.setVideoScalingMode(mode);
        } else {
            throw new IllegalStateException(getIllegalStateExceptionDetailMessage("setVideoScalingMode()", mState, MethodStates.SET_VIDEO_SCALING_MODE));
        }
    }

    @Override
    public synchronized void setDataSource(Context context, Uri uri) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        if (MethodStates.SET_DATA_SOURCE.contains(mState)) {
            super.setDataSource(context, uri);
            setState(INITIALIZED);
        } else {
            throw new IllegalStateException(getIllegalStateExceptionDetailMessage("setDataSource()", mState, MethodStates.SET_DATA_SOURCE));
        }
    }

    @Override
    public synchronized void setDataSource(Context context, Uri uri, Map<String, String> headers) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        if (MethodStates.SET_DATA_SOURCE.contains(mState)) {
            super.setDataSource(context, uri, headers);
            setState(INITIALIZED);
        } else {
            throw new IllegalStateException(getIllegalStateExceptionDetailMessage("setDataSource()", mState, MethodStates.SET_DATA_SOURCE));
        }
    }

    @Override
    public synchronized void setDataSource(String path) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        if (MethodStates.SET_DATA_SOURCE.contains(mState)) {
            super.setDataSource(path);
            setState(INITIALIZED);
        } else {
            throw new IllegalStateException(getIllegalStateExceptionDetailMessage("setDataSource()", mState, MethodStates.SET_DATA_SOURCE));
        }
    }

    @Override
    public synchronized void setDataSource(FileDescriptor fd) throws IOException, IllegalArgumentException, IllegalStateException {
        if (MethodStates.SET_DATA_SOURCE.contains(mState)) {
            super.setDataSource(fd);
            setState(INITIALIZED);
        } else {
            throw new IllegalStateException(getIllegalStateExceptionDetailMessage("setDataSource()", mState, MethodStates.SET_DATA_SOURCE));
        }
    }

    @Override
    public synchronized void setDataSource(FileDescriptor fd, long offset, long length) throws IOException, IllegalArgumentException, IllegalStateException {
        if (MethodStates.SET_DATA_SOURCE.contains(mState)) {
            super.setDataSource(fd, offset, length);
            setState(INITIALIZED);
        } else {
            throw new IllegalStateException(getIllegalStateExceptionDetailMessage("setDataSource()", mState, MethodStates.SET_DATA_SOURCE));
        }
    }

    @Override
    public synchronized void setDataSource(MediaDataSource dataSource) throws IllegalArgumentException, IllegalStateException {
        if (MethodStates.SET_DATA_SOURCE.contains(mState)) {
            super.setDataSource(dataSource);
            setState(INITIALIZED);
        } else {
            throw new IllegalStateException(getIllegalStateExceptionDetailMessage("setDataSource()", mState, MethodStates.SET_DATA_SOURCE));
        }
    }

    private String getIllegalStateExceptionDetailMessage(String method, State state, MethodStates validStates) {
        return method + " called in invalid state: " + state + "; valid states: " + validStates.mStates;
    }

    @Override
    public synchronized void prepare() throws IOException, IllegalStateException {
        if (MethodStates.PREPARE.contains(mState)) {
            super.prepare();
            setState(PREPARED);
        } else {
            throw new IllegalStateException(getIllegalStateExceptionDetailMessage("prepare()", mState, MethodStates.PREPARE));
        }
    }

    @Override
    public synchronized void prepareAsync() throws IllegalStateException {
        if (MethodStates.PREPARE_ASYNC.contains(mState)) {
            super.prepareAsync();
            setState(PREPARING);
        } else {
            throw new IllegalStateException(getIllegalStateExceptionDetailMessage("prepareAsync()", mState, MethodStates.PREPARE_ASYNC));
        }
    }

    @Override
    public synchronized void start() throws IllegalStateException {
        if (MethodStates.START.contains(mState)) {
            super.start();
            setState(STARTED);
        } else {
            throw new IllegalStateException(getIllegalStateExceptionDetailMessage("start()", mState, MethodStates.START));
        }
    }

    @Override
    public synchronized void stop() throws IllegalStateException {
        if (MethodStates.STOP.contains(mState)) {
            super.stop();
            setState(STOPPED);
        } else {
            throw new IllegalStateException(getIllegalStateExceptionDetailMessage("stop()", mState, MethodStates.STOP));
        }
    }

    @Override
    public synchronized void pause() throws IllegalStateException {
        if (MethodStates.PAUSE.contains(mState)) {
            super.pause();
            setState(PAUSED);
        } else {
            throw new IllegalStateException(getIllegalStateExceptionDetailMessage("pause()", mState, MethodStates.PAUSE));
        }
    }

    @Override
    public int getVideoWidth() {
        if (MethodStates.GET_VIDEO_WIDTH.contains(mState)) {
            return super.getVideoWidth();
        } else {
            throw new IllegalStateException(getIllegalStateExceptionDetailMessage("getVideoWidth()", mState, MethodStates.GET_VIDEO_WIDTH));
        }
    }

    @Override
    public int getVideoHeight() {
        if (MethodStates.GET_VIDEO_HEIGHT.contains(mState)) {
            return super.getVideoHeight();
        } else {
            throw new IllegalStateException(getIllegalStateExceptionDetailMessage("getVideoHeight()", mState, MethodStates.GET_VIDEO_HEIGHT));
        }
    }

    @Override
    public boolean isPlaying() {
        if (MethodStates.IS_PLAYING.contains(mState)) {
            return super.isPlaying();
        } else {
            throw new IllegalStateException(getIllegalStateExceptionDetailMessage("isPlaying()", mState, MethodStates.IS_PLAYING));
        }
    }

    @Override
    public synchronized void seekTo(int msec) throws IllegalStateException {
        if (MethodStates.SEEK_TO.contains(mState)) {
            super.seekTo(msec);
        } else {
            throw new IllegalStateException(getIllegalStateExceptionDetailMessage("seekTo()", mState, MethodStates.SEEK_TO));
        }
    }

    @Override
    public int getCurrentPosition() {
        if (MethodStates.GET_CURRENT_POSITION.contains(mState)) {
            return super.getCurrentPosition();
        } else {
            throw new IllegalStateException(getIllegalStateExceptionDetailMessage("getCurrentPosition()", mState, MethodStates.GET_CURRENT_POSITION));
        }
    }

    @Override
    public int getDuration() {
        if (MethodStates.GET_DURATION.contains(mState)) {
            return super.getDuration();
        } else {
            throw new IllegalStateException(getIllegalStateExceptionDetailMessage("getDuration()", mState, MethodStates.GET_DURATION));
        }
    }

    /**
     * @deprecated Use {@link StatefulMediaPlayer#setNextMediaPlayer(StatefulMediaPlayer)} instead.
     */
    @Override
    @Deprecated
    public synchronized void setNextMediaPlayer(MediaPlayer next) {
        super.setNextMediaPlayer(next);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public synchronized void setNextMediaPlayer(StatefulMediaPlayer next) {
        super.setNextMediaPlayer(next);
    }

    @Override
    public synchronized void release() {
        super.release();
        setState(RELEASED);
    }

    @Override
    public synchronized void reset() {
        if (MethodStates.RESET.contains(mState)) {
            super.reset();
            setState(IDLE);
        } else {
            throw new IllegalStateException(getIllegalStateExceptionDetailMessage("reset()", mState, MethodStates.RESET));
        }
    }

    @Override
    public synchronized void setAudioStreamType(int streamtype) {
        if (MethodStates.SET_AUDIO_STREAM_TYPE.contains(mState)) {
            super.setAudioStreamType(streamtype);
        } else {
            throw new IllegalStateException(getIllegalStateExceptionDetailMessage("setAudioStreamType()", mState, MethodStates.SET_AUDIO_STREAM_TYPE));
        }
    }

    @Override
    public synchronized void setAudioAttributes(AudioAttributes attributes) throws IllegalArgumentException {
        if (MethodStates.SET_AUDIO_ATTRIBUTES.contains(mState)) {
            super.setAudioAttributes(attributes);
        } else {
            throw new IllegalStateException(getIllegalStateExceptionDetailMessage("setAudioAttributes()", mState, MethodStates.SET_AUDIO_ATTRIBUTES));
        }
    }

    @Override
    public synchronized void setLooping(boolean looping) {
        if (MethodStates.SET_LOOPING.contains(mState)) {
            super.setLooping(looping);
        } else {
            throw new IllegalStateException(getIllegalStateExceptionDetailMessage("setLooping()", mState, MethodStates.SET_LOOPING));
        }
    }

    @Override
    public synchronized void setVolume(float leftVolume, float rightVolume) {
        if (MethodStates.SET_VOLUME.contains(mState)) {
            super.setVolume(leftVolume, rightVolume);
        } else {
            throw new IllegalStateException(getIllegalStateExceptionDetailMessage("setVolume()", mState, MethodStates.SET_VOLUME));
        }
    }

    @Override
    public synchronized void setAudioSessionId(int sessionId) throws IllegalArgumentException, IllegalStateException {
        if (MethodStates.SET_AUDIO_SESSION_ID.contains(mState)) {
            super.setAudioSessionId(sessionId);
        } else {
            throw new IllegalStateException(getIllegalStateExceptionDetailMessage("setAudioSessionId()", mState, MethodStates.SET_AUDIO_SESSION_ID));
        }
    }

    @Override
    public synchronized void attachAuxEffect(int effectId) {
        if (MethodStates.ATTACH_AUX_EFFECT.contains(mState)) {
            super.attachAuxEffect(effectId);
        } else {
            throw new IllegalStateException(getIllegalStateExceptionDetailMessage("attachAuxEffect()", mState, MethodStates.ATTACH_AUX_EFFECT));
        }
    }

    @Override
    public TrackInfo[] getTrackInfo() throws IllegalStateException {
        if (MethodStates.GET_TRACK_INFO.contains(mState)) {
            return super.getTrackInfo();
        } else {
            throw new IllegalStateException(getIllegalStateExceptionDetailMessage("getTrackInfo()", mState, MethodStates.GET_TRACK_INFO));
        }
    }

    @Override
    public synchronized void addTimedTextSource(String path, String mimeType) throws IOException, IllegalArgumentException, IllegalStateException {
        if (MethodStates.ADD_TIMED_TEXT_SOURCE.contains(mState)) {
            super.addTimedTextSource(path, mimeType);
        } else {
            throw new IllegalStateException(getIllegalStateExceptionDetailMessage("addTimedTextSource()", mState, MethodStates.ADD_TIMED_TEXT_SOURCE));
        }
    }

    @Override
    public synchronized void addTimedTextSource(Context context, Uri uri, String mimeType) throws IOException, IllegalArgumentException, IllegalStateException {
        if (MethodStates.ADD_TIMED_TEXT_SOURCE.contains(mState)) {
            super.addTimedTextSource(context, uri, mimeType);
        } else {
            throw new IllegalStateException(getIllegalStateExceptionDetailMessage("addTimedTextSource()", mState, MethodStates.ADD_TIMED_TEXT_SOURCE));
        }
    }

    @Override
    public synchronized void addTimedTextSource(FileDescriptor fd, String mimeType) throws IllegalArgumentException, IllegalStateException {
        if (MethodStates.ADD_TIMED_TEXT_SOURCE.contains(mState)) {
            super.addTimedTextSource(fd, mimeType);
        } else {
            throw new IllegalStateException(getIllegalStateExceptionDetailMessage("addTimedTextSource()", mState, MethodStates.ADD_TIMED_TEXT_SOURCE));
        }
    }

    @Override
    public synchronized void addTimedTextSource(FileDescriptor fd, long offset, long length, String mime) throws IllegalArgumentException, IllegalStateException {
        if (MethodStates.ADD_TIMED_TEXT_SOURCE.contains(mState)) {
            super.addTimedTextSource(fd, offset, length, mime);
        } else {
            throw new IllegalStateException(getIllegalStateExceptionDetailMessage("addTimedTextSource()", mState, MethodStates.ADD_TIMED_TEXT_SOURCE));
        }
    }

    @Override
    public synchronized void selectTrack(int index) throws IllegalStateException {
        if (MethodStates.SELECT_TRACK.contains(mState)) {
            super.selectTrack(index);
        } else {
            throw new IllegalStateException(getIllegalStateExceptionDetailMessage("selectTrack()", mState, MethodStates.SELECT_TRACK));
        }
    }

    @Override
    public synchronized void deselectTrack(int index) throws IllegalStateException {
        if (MethodStates.DESELECT_TRACK.contains(mState)) {
            super.deselectTrack(index);
        } else {
            throw new IllegalStateException(getIllegalStateExceptionDetailMessage("deselectTrack()", mState, MethodStates.DESELECT_TRACK));
        }
    }

    /**
     * @deprecated Use {@link StatefulMediaPlayer#setOnPreparedListener(OnPreparedListener)} instead.
     */
    @Override
    @Deprecated
    public void setOnPreparedListener(MediaPlayer.OnPreparedListener listener) {
        super.setOnPreparedListener(listener);
    }

    /**
     * @deprecated Use {@link StatefulMediaPlayer#setOnCompletionListener(OnCompletionListener)} instead;
     */
    @Override
    @Deprecated
    public void setOnCompletionListener(MediaPlayer.OnCompletionListener listener) {
        super.setOnCompletionListener(listener);
    }

    /**
     * @deprecated Use {@link StatefulMediaPlayer#setOnPreparedListener(OnPreparedListener)} instead.
     */
    @Override
    @Deprecated
    public void setOnBufferingUpdateListener(MediaPlayer.OnBufferingUpdateListener listener) {
        super.setOnBufferingUpdateListener(listener);
    }

    /**
     * @deprecated Use {@link StatefulMediaPlayer#setOnSeekCompleteListener(OnSeekCompleteListener)} instead.
     */
    @Override
    @Deprecated
    public void setOnSeekCompleteListener(MediaPlayer.OnSeekCompleteListener listener) {
        super.setOnSeekCompleteListener(listener);
    }

    /**
     * @deprecated Use {@link StatefulMediaPlayer#setOnVideoSizeChangedListener(OnVideoSizeChangedListener)} instead.
     */
    @Override
    @Deprecated
    public void setOnVideoSizeChangedListener(MediaPlayer.OnVideoSizeChangedListener listener) {
        super.setOnVideoSizeChangedListener(listener);
    }

    /**
     * @deprecated Use {@link StatefulMediaPlayer#setOnTimedTextListener(OnTimedTextListener)} instead.
     */
    @Override
    @Deprecated
    public void setOnTimedTextListener(MediaPlayer.OnTimedTextListener listener) {
        super.setOnTimedTextListener(listener);
    }

    /**
     * @deprecated Use {@link StatefulMediaPlayer#setOnTimedMetaDataAvailableListener(OnTimedMetaDataAvailableListener)}} instead.
     */
    @Override
    @Deprecated
    public void setOnTimedMetaDataAvailableListener(MediaPlayer.OnTimedMetaDataAvailableListener listener) {
        super.setOnTimedMetaDataAvailableListener(listener);
    }

    /**
     * @deprecated Use {@link StatefulMediaPlayer#setOnErrorListener(OnErrorListener)} instead.
     */
    @Override
    @Deprecated
    public void setOnErrorListener(MediaPlayer.OnErrorListener listener) {
        super.setOnErrorListener(listener);
    }

    /**
     * @deprecated Use {@link StatefulMediaPlayer#setOnInfoListener(OnInfoListener)} instead.
     */
    @Override
    @Deprecated
    public void setOnInfoListener(MediaPlayer.OnInfoListener listener) {
        super.setOnInfoListener(listener);
    }


    public void setOnStateChangedListener(OnStateChangedListener onStateChangedListener) {
        mOnStateChangedListener = onStateChangedListener;
    }

    public void setOnBufferingUpdateListener(OnBufferingUpdateListener onBufferingUpdateListener) {
        mOnBufferingUpdateListener = onBufferingUpdateListener;
    }

    public void setOnCompletionListener(OnCompletionListener onCompletionListener) {
        mOnCompletionListener = onCompletionListener;
    }

    public void setOnErrorListener(OnErrorListener onErrorListener) {
        mOnErrorListener = onErrorListener;
    }

    public void setOnInfoListener(OnInfoListener onInfoListener) {
        mOnInfoListener = onInfoListener;
    }

    public void setOnPreparedListener(OnPreparedListener onPreparedListener) {
        mOnPreparedListener = onPreparedListener;
    }

    public void setOnSeekCompleteListener(OnSeekCompleteListener onSeekCompleteListener) {
        mOnSeekCompleteListener = onSeekCompleteListener;
    }

    public void setOnTimedMetaDataAvailableListener(OnTimedMetaDataAvailableListener onTimedMetaDataAvailableListener) {
        mOnTimedMetaDataAvailableListener = onTimedMetaDataAvailableListener;
    }

    public void setOnTimedTextListener(OnTimedTextListener onTimedTextListener) {
        mOnTimedTextListener = onTimedTextListener;
    }

    public void setOnVideoSizeChangedListener(OnVideoSizeChangedListener onVideoSizeChangedListener) {
        mOnVideoSizeChangedListener = onVideoSizeChangedListener;
    }

    private synchronized void setState(State state) {
        mState = state;
        Log.v(TAG, "State: " + mState);
        notifyOnStateChangedListener();
    }

    private synchronized void notifyOnStateChangedListener() {
        if (mOnStateChangedListener != null) {
            mOnStateChangedListener.onStateChanged(this, mState);
        }
    }

    /**
     * MediaPlayer states.
     * <p>
     *    For details, check out the
     *    <a href=http://developer.android.com/reference/android/media/MediaPlayer.html#StateDiagram>MediaPlayer state diagram</a>.
     * </p>
     */
    public enum State {
        IDLE,
        INITIALIZED,
        PREPARING,
        PREPARED,
        STARTED,
        STOPPED,
        PLAYBACK_COMPLETED,
        PAUSED,
        ERROR,
        RELEASED
    }

    /**
     * Valid state sets for each of MediaPlayer's methods.
     * <p/>
     * <p>
     * For details, see the
     * <a href=http://developer.android.com/reference/android/media/MediaPlayer.html#Valid_and_Invalid_States>valid states table</a>
     * </p>
     */
    private enum MethodStates {
        ATTACH_AUX_EFFECT(EnumSet.of(INITIALIZED, PREPARED, STARTED, PAUSED, STOPPED, PLAYBACK_COMPLETED)),
        GET_CURRENT_POSITION(EnumSet.of(IDLE, INITIALIZED, PREPARED, STARTED, PAUSED, STOPPED, PLAYBACK_COMPLETED)),
        GET_DURATION(EnumSet.of(PREPARED, STARTED, PAUSED, STOPPED, PLAYBACK_COMPLETED)),
        GET_VIDEO_HEIGHT(EnumSet.of(IDLE, INITIALIZED, PREPARED, STARTED, PAUSED, STOPPED, PLAYBACK_COMPLETED)),
        GET_VIDEO_WIDTH(EnumSet.of(IDLE, INITIALIZED, PREPARED, STARTED, PAUSED, STOPPED, PLAYBACK_COMPLETED)),
        IS_PLAYING(EnumSet.of(IDLE, INITIALIZED, PREPARED, STARTED, PAUSED, STOPPED, PLAYBACK_COMPLETED)),
        PAUSE(EnumSet.of(STARTED, PAUSED, PLAYBACK_COMPLETED)),
        PREPARE(EnumSet.of(INITIALIZED, STOPPED)),
        PREPARE_ASYNC(EnumSet.of(INITIALIZED, STOPPED)),
        RESET(EnumSet.of(IDLE, INITIALIZED, PREPARED, STARTED, PAUSED, STOPPED, PLAYBACK_COMPLETED, ERROR)),
        SEEK_TO(EnumSet.of(PREPARED, STARTED, PAUSED, PLAYBACK_COMPLETED)),
        SET_AUDIO_ATTRIBUTES(EnumSet.of(IDLE, INITIALIZED, STOPPED, PREPARED, STARTED, PAUSED, PLAYBACK_COMPLETED)),
        SET_AUDIO_SESSION_ID(EnumSet.of(IDLE)),
        SET_AUDIO_STREAM_TYPE(EnumSet.of(IDLE, INITIALIZED, STOPPED, PREPARED, STARTED, PAUSED, PLAYBACK_COMPLETED)),
        SET_DATA_SOURCE(EnumSet.of(IDLE)),
        SET_VIDEO_SCALING_MODE(EnumSet.of(INITIALIZED, PREPARED, STARTED, PAUSED, STOPPED, PLAYBACK_COMPLETED)),
        SET_LOOPING(EnumSet.of(IDLE, INITIALIZED, STOPPED, PREPARED, STARTED, PAUSED, PLAYBACK_COMPLETED)),
        SET_VOLUME(EnumSet.of(IDLE, INITIALIZED, STOPPED, PREPARED, STARTED, PAUSED, PLAYBACK_COMPLETED)),
        START(EnumSet.of(PREPARED, STARTED, PAUSED, PLAYBACK_COMPLETED)),
        STOP(EnumSet.of(PREPARED, STARTED, STOPPED, PAUSED, PLAYBACK_COMPLETED)),
        GET_TRACK_INFO(EnumSet.of(PREPARED, STARTED, STOPPED, PAUSED, PLAYBACK_COMPLETED)),
        ADD_TIMED_TEXT_SOURCE(EnumSet.of(PREPARED, STARTED, STOPPED, PAUSED, PLAYBACK_COMPLETED)),
        SELECT_TRACK(EnumSet.of(PREPARED, STARTED, STOPPED, PAUSED, PLAYBACK_COMPLETED)),
        DESELECT_TRACK(EnumSet.of(PREPARED, STARTED, STOPPED, PAUSED, PLAYBACK_COMPLETED));

        private final EnumSet<State> mStates;

        MethodStates(EnumSet<State> states) {
            mStates = states;
        }

        public EnumSet<State> getStates() {
            return mStates;
        }

        public boolean contains(State state) {
            return mStates.contains(state);
        }
    }

    public interface OnStateChangedListener {
        void onStateChanged(StatefulMediaPlayer mp, State state);
    }

    /**
     * See {@link android.media.MediaPlayer.OnBufferingUpdateListener}
     */
    public interface OnBufferingUpdateListener {
        /**
         * See {@link android.media.MediaPlayer.OnBufferingUpdateListener#onBufferingUpdate(MediaPlayer, int)}
         */
        void onBufferingUpdate(StatefulMediaPlayer mp, int percent);
    }

    /**
     * See {@link android.media.MediaPlayer.OnCompletionListener}
     */
    public interface OnCompletionListener {
        /**
         * See {@link android.media.MediaPlayer.OnCompletionListener#onCompletion(MediaPlayer)}
         */
        void onCompletion(StatefulMediaPlayer mp);
    }

    /**
     * See {@link android.media.MediaPlayer.OnErrorListener}
     */
    public interface OnErrorListener {
        /**
         * See {@link android.media.MediaPlayer.OnCompletionListener#onCompletion(MediaPlayer)}
         */
        boolean onError(StatefulMediaPlayer mp, MediaError error, int extra);
    }

    /**
     * See {@link android.media.MediaPlayer.OnInfoListener}
     */
    public interface OnInfoListener {
        /**
         * See {@link android.media.MediaPlayer.OnInfoListener#onInfo(MediaPlayer, int, int)}
         */
        boolean onInfo(StatefulMediaPlayer mp, MediaInfo info, int extra);
    }

    /**
     * See {@link android.media.MediaPlayer.OnPreparedListener}
     */
    public interface OnPreparedListener {
        /**
         * See {@link android.media.MediaPlayer.OnPreparedListener#onPrepared(MediaPlayer)}
         */
        void onPrepared(StatefulMediaPlayer mp);
    }

    /**
     * See {@link android.media.MediaPlayer.OnSeekCompleteListener}
     */
    public interface OnSeekCompleteListener {
        /**
         * See {@link android.media.MediaPlayer.OnSeekCompleteListener#onSeekComplete(MediaPlayer)}
         */
        void onSeekComplete(StatefulMediaPlayer mp);
    }

    /**
     * See {@link android.media.MediaPlayer.OnTimedMetaDataAvailableListener}
     */
    public interface OnTimedMetaDataAvailableListener {
        /**
         * See {@link android.media.MediaPlayer.OnTimedMetaDataAvailableListener#onTimedMetaDataAvailable(MediaPlayer, TimedMetaData)}
         */
        void onTimedMetaDataAvailable(StatefulMediaPlayer mp, TimedMetaData data);
    }

    /**
     * See {@link android.media.MediaPlayer.OnTimedTextListener}
     */
    public interface OnTimedTextListener {
        /**
         * See {@link android.media.MediaPlayer.OnTimedTextListener#onTimedText(MediaPlayer, TimedText)}
         */
        void onTimedText(StatefulMediaPlayer mp, TimedText text);
    }

    /**
     * See {@link android.media.MediaPlayer.OnVideoSizeChangedListener}
     */
    public interface OnVideoSizeChangedListener {
        /**
         * See {@link android.media.MediaPlayer.OnVideoSizeChangedListener#onVideoSizeChanged(MediaPlayer, int, int)}
         */
        void onVideoSizeChanged(StatefulMediaPlayer mp, int width, int height);
    }

    /**
     * Internal listener.
     */
    private class MediaPlayerListener implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnSeekCompleteListener {

        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            Log.v(TAG, "Buffering update: " + percent + '%');

            if (mOnBufferingUpdateListener != null) {
                mOnBufferingUpdateListener.onBufferingUpdate(StatefulMediaPlayer.this, percent);
            }
        }

        @Override
        public void onCompletion(MediaPlayer mp) {
            Log.v(TAG, "Playback completed");

            if (mState != ERROR) {
                if (isLooping()) {
                    setState(STARTED);
                } else {
                    setState(PLAYBACK_COMPLETED);
                }

                if (mOnCompletionListener != null) {
                    mOnCompletionListener.onCompletion(StatefulMediaPlayer.this);
                }
            }
        }

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            final MediaError mediaError = MediaError.fromCode(what);
            Log.v(TAG, "Error: " + mediaError + "; extra: " + extra + " (0x" + Integer.toHexString(extra) + ')');
            setState(ERROR);

            if (mOnErrorListener != null) {
                mOnErrorListener.onError(StatefulMediaPlayer.this, mediaError, extra);
            }
            return false;
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            Log.v(TAG, "Prepared");
            setState(PREPARED);

            if (mOnPreparedListener != null) {
                mOnPreparedListener.onPrepared(StatefulMediaPlayer.this);
            }
        }

        @Override
        public void onSeekComplete(MediaPlayer mp) {
            Log.v(TAG, "Seek completed");

            if (mOnSeekCompleteListener != null) {
                mOnSeekCompleteListener.onSeekComplete(StatefulMediaPlayer.this);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    private class MediaPlayerCupcakeListener implements MediaPlayer.OnInfoListener, MediaPlayer.OnVideoSizeChangedListener {

        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            final MediaInfo mediaInfo = MediaInfo.fromCode(what);
            Log.v(TAG, "Info: " + mediaInfo + "; extra: " + extra + " (0x" + Integer.toHexString(extra) + ')');

            return mOnInfoListener != null && mOnInfoListener.onInfo(StatefulMediaPlayer.this, mediaInfo, extra);
        }

        @Override
        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
            Log.v(TAG, "Video size changed: " + width + 'x' + height);

            if (mOnVideoSizeChangedListener != null) {
                mOnVideoSizeChangedListener.onVideoSizeChanged(StatefulMediaPlayer.this, width, height);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private class MediaPlayerJellyBeanListener implements MediaPlayer.OnTimedTextListener {

        @Override
        public void onTimedText(MediaPlayer mp, TimedText text) {
            Log.v(TAG, "Timed text: " + text);

            if (mOnTimedTextListener != null) {
                mOnTimedTextListener.onTimedText(StatefulMediaPlayer.this, text);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private class MediaPlayerMarshmallowListener implements MediaPlayer.OnTimedMetaDataAvailableListener {

        @Override
        public void onTimedMetaDataAvailable(MediaPlayer mp, TimedMetaData data) {
            Log.v(TAG, "Timed meta data: " + data);

            if (mOnTimedMetaDataAvailableListener != null) {
                mOnTimedMetaDataAvailableListener.onTimedMetaDataAvailable(StatefulMediaPlayer.this, data);
            }
        }
    }
}