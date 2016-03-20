package mediaplayer;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.media.TimedMetaData;
import android.media.TimedText;

import java.util.EnumSet;

import static mediaplayer.StatefulMediaPlayer.State.ERROR;
import static mediaplayer.StatefulMediaPlayer.State.INITIALIZED;
import static mediaplayer.StatefulMediaPlayer.State.PAUSED;
import static mediaplayer.StatefulMediaPlayer.State.PLAYBACK_COMPLETED;
import static mediaplayer.StatefulMediaPlayer.State.PREPARED;
import static mediaplayer.StatefulMediaPlayer.State.STARTED;
import static mediaplayer.StatefulMediaPlayer.State.STOPPED;

/**
 * A {@link MediaPlayer} subclass that keeps track of its state, which can be checked with {@link #getState()}.
 *
 * <p>
 *     This is partially based on <a href=https://gist.github.com/danielhawkes/1029568>danielhawkes' gist</a>
 * </p>
 */
public class StatefulMediaPlayer extends MediaPlayer {
    private static final String TAG = "StatefulMediaPlayer";
    private final MediaPlayerListener mListener = new MediaPlayerListener();
    private State mState;
    private OnBufferingUpdateListener mOnBufferingUpdateListener;
    private OnCompletionListener mOnCompletionListener;
    private OnErrorListener mOnErrorListener;
    private OnInfoListener mOnInfoListener;
    private OnPreparedListener mOnPreparedListener;
    private OnSeekCompleteListener mOnSeekCompleteListener;
    private OnTimedMetaDataAvailableListener mOnTimedMetaDataAvailableListener;
    private OnTimedTextListener mOnTimedTextListener;
    private OnVideoSizeChangedListener mOnVideoSizeChangedListener;

    public StatefulMediaPlayer() {
        setOnPreparedListener(mListener);
        setOnCompletionListener(mListener);
        setOnErrorListener(mListener);
    }

    public State getState() {
        return mState;
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
        END;
    }

    /**
     * MediaPlayer error codes.
     */
    @SuppressLint("InlinedApi")
    public enum MediaError {
        /** {@link MediaPlayer#MEDIA_ERROR_IO} */
        IO(MEDIA_ERROR_IO),
        /** {@link MediaPlayer#MEDIA_ERROR_MALFORMED} */
        MALFORMED(MEDIA_ERROR_MALFORMED),
        /** {@link MediaPlayer#MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK **/
        NOT_VALID_FOR_PROGRESSIVE_PLAYBACK(MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK),
        /** {@link MediaPlayer#MEDIA_ERROR_SERVER_DIED} */
        SERVER_DIED(MEDIA_ERROR_SERVER_DIED),
        /** {@link MediaPlayer#MEDIA_ERROR_TIMED_OUT} */
        TIMED_OUT(MEDIA_ERROR_TIMED_OUT),
        /** {@link MediaPlayer#MEDIA_ERROR_UNKNOWN} */
        UNKNOWN(MEDIA_ERROR_UNKNOWN),
        /** {@link MediaPlayer#MEDIA_ERROR_UNSUPPORTED} */
        UNSUPPORTED(MEDIA_ERROR_UNSUPPORTED);

        private final int mErrorCode;

        MediaError(int errorCode) {
            mErrorCode = errorCode;
        }

        public static MediaError fromCode(int errorCode) {
            switch (errorCode) {
                case MEDIA_ERROR_IO:
                    return IO;
                case MEDIA_ERROR_MALFORMED:
                    return MALFORMED;
                case MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                    return NOT_VALID_FOR_PROGRESSIVE_PLAYBACK;
                case MEDIA_ERROR_SERVER_DIED:
                    return SERVER_DIED;
                case MEDIA_ERROR_TIMED_OUT:
                    return TIMED_OUT;
                case MEDIA_ERROR_UNSUPPORTED:
                    return UNSUPPORTED;
                case MEDIA_ERROR_UNKNOWN:
                default:
                    return UNKNOWN;
            }
        }
    }

    /**
     * MediaPlayer info codes;
     */
    @SuppressLint("InlinedApi")
    public enum MediaInfo {
        /** {@link MediaPlayer#MEDIA_INFO_BAD_INTERLEAVING} **/
        BAD_INTERLEAVING(MEDIA_INFO_BAD_INTERLEAVING),
        /** {@link MediaPlayer#MEDIA_INFO_BUFFERING_END} **/
        BUFFERING_END(MEDIA_INFO_BUFFERING_END),
        /** {@link MediaPlayer#MEDIA_INFO_BUFFERING_START} **/
        BUFFERING_START(MEDIA_INFO_BUFFERING_START),
        /** {@link MediaPlayer#MEDIA_INFO_METADATA_UPDATE} **/
        METADATA_UPDATE(MEDIA_INFO_METADATA_UPDATE),
        /** {@link MediaPlayer#MEDIA_INFO_NOT_SEEKABLE} **/
        NOT_SEEKABLE(MEDIA_INFO_NOT_SEEKABLE),
        /** {@link MediaPlayer#MEDIA_INFO_SUBTITLE_TIMED_OUT} **/
        SUBTITLE_TIMED_OUT(MEDIA_INFO_SUBTITLE_TIMED_OUT),
        /** {@link MediaPlayer#MEDIA_INFO_UNKNOWN} **/
        UNKNOWN(MEDIA_INFO_UNKNOWN),
        /** {@link MediaPlayer#MEDIA_INFO_UNSUPPORTED_SUBTITLE} **/
        UNSUPPORTED_SUBTITLE(MEDIA_INFO_UNSUPPORTED_SUBTITLE),
        /** {@link MediaPlayer#MEDIA_INFO_VIDEO_RENDERING_START} **/
        VIDEO_RENDERING_START(MEDIA_INFO_VIDEO_RENDERING_START),
        /** {@link MediaPlayer#MEDIA_INFO_VIDEO_TRACK_LAGGING} **/
        VIDEO_TRACK_LAGGING(MEDIA_INFO_VIDEO_TRACK_LAGGING);

        private final int mInfoCode;

        MediaInfo(int infoCode) {
            mInfoCode = infoCode;
        }

        public static MediaInfo fromCode(int infoCode) {
            switch (infoCode) {
                case MEDIA_INFO_BAD_INTERLEAVING:
                    return BAD_INTERLEAVING;
                case MEDIA_INFO_BUFFERING_END:
                    return BUFFERING_END;
                case MEDIA_INFO_BUFFERING_START:
                    return BUFFERING_START;
                case MEDIA_INFO_METADATA_UPDATE:
                    return METADATA_UPDATE;
                case MEDIA_INFO_NOT_SEEKABLE:
                    return NOT_SEEKABLE;
                case MEDIA_INFO_SUBTITLE_TIMED_OUT:
                    return SUBTITLE_TIMED_OUT;
                case MEDIA_INFO_UNSUPPORTED_SUBTITLE:
                    return UNSUPPORTED_SUBTITLE;
                case MEDIA_INFO_VIDEO_RENDERING_START:
                    return VIDEO_RENDERING_START;
                case MEDIA_INFO_VIDEO_TRACK_LAGGING:
                    return VIDEO_TRACK_LAGGING;
                case MEDIA_INFO_UNKNOWN:
                default:
                    return UNKNOWN;
            }
        }
    }

    /** See {@link android.media.MediaPlayer.OnBufferingUpdateListener} */
    public interface OnBufferingUpdateListener {
        /** See {@link android.media.MediaPlayer.OnBufferingUpdateListener#onBufferingUpdate(MediaPlayer, int)} */
        void onBufferingUpdate(StatefulMediaPlayer mp, int percent);
    }

    /** See {@link android.media.MediaPlayer.OnCompletionListener} */
    public interface OnCompletionListener {
        /** See {@link android.media.MediaPlayer.OnCompletionListener#onCompletion(MediaPlayer)} */
        void onCompletion(StatefulMediaPlayer mp);
    }

    /** See {@link android.media.MediaPlayer.OnErrorListener} */
    public interface OnErrorListener {
        /** See {@link android.media.MediaPlayer.OnCompletionListener#onCompletion(MediaPlayer)} */
        boolean onError(StatefulMediaPlayer mp, MediaError error, int extra);
    }

    /** See {@link android.media.MediaPlayer.OnInfoListener} */
    public interface OnInfoListener {
        /** See {@link android.media.MediaPlayer.OnInfoListener#onInfo(MediaPlayer, int, int)} */
        boolean onInfo(StatefulMediaPlayer mp, MediaInfo info, int extra);
    }

    /** See {@link android.media.MediaPlayer.OnPreparedListener} */
    public interface OnPreparedListener {
        /** See {@link android.media.MediaPlayer.OnPreparedListener#onPrepared(MediaPlayer)} */
        void onPrepared(StatefulMediaPlayer mp);
    }

    /** See {@link android.media.MediaPlayer.OnSeekCompleteListener} */
    public interface OnSeekCompleteListener {
        /** See {@link android.media.MediaPlayer.OnSeekCompleteListener#onSeekComplete(MediaPlayer)} */
        void onSeekComplete(StatefulMediaPlayer mp);
    }

    /** See {@link android.media.MediaPlayer.OnTimedMetaDataAvailableListener} */
    public interface OnTimedMetaDataAvailableListener {
        /** See {@link android.media.MediaPlayer.OnTimedMetaDataAvailableListener#onTimedMetaDataAvailable(MediaPlayer, TimedMetaData)} */
        void onTimedMetaDataAvailable(StatefulMediaPlayer mp, TimedMetaData data);
    }

    /** See {@link android.media.MediaPlayer.OnTimedTextListener} */
    public interface OnTimedTextListener {
        /** See {@link android.media.MediaPlayer.OnTimedTextListener#onTimedText(MediaPlayer, TimedText)} */
        void onTimedText(StatefulMediaPlayer mp, TimedText text);
    }

    /** See {@link android.media.MediaPlayer.OnVideoSizeChangedListener} */
    public interface OnVideoSizeChangedListener {
        /** See {@link android.media.MediaPlayer.OnVideoSizeChangedListener#onVideoSizeChanged(MediaPlayer, int, int)} */
        void onVideoSizeChanged(StatefulMediaPlayer mp, int width, int height);
    }

    /**
     * Internal listener.
     */
    private class MediaPlayerListener implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnTimedMetaDataAvailableListener, MediaPlayer.OnTimedTextListener, MediaPlayer.OnVideoSizeChangedListener {

        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            if (mOnBufferingUpdateListener != null) {
                mOnBufferingUpdateListener.onBufferingUpdate(StatefulMediaPlayer.this, percent);
            }
        }

        @Override
        public void onCompletion(MediaPlayer mp) {
            if (mOnCompletionListener != null) {
                mOnCompletionListener.onCompletion(StatefulMediaPlayer.this);
            }
        }

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            if (mOnErrorListener != null) {
                mOnErrorListener.onError(StatefulMediaPlayer.this, MediaError.fromCode(what), extra);
            }
            return false;
        }

        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            return false;
        }

        @Override
        public void onPrepared(MediaPlayer mp) {

        }

        @Override
        public void onSeekComplete(MediaPlayer mp) {

        }

        @Override
        public void onTimedMetaDataAvailable(MediaPlayer mp, TimedMetaData data) {

        }

        @Override
        public void onTimedText(MediaPlayer mp, TimedText text) {

        }

        @Override
        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {

        }
    }

    /**
     * Valid state sets for each of MediaPlayer's methods.
     *
     * <p>
     *     For details, see the
     *     <a href=http://developer.android.com/reference/android/media/MediaPlayer.html#Valid_and_Invalid_States>valid states table</a>
     * </p>
     */
    private enum MethodStates {
        ATTACH_AUX_EFFECT(EnumSet.of(INITIALIZED, PREPARED, STARTED, PAUSED, STOPPED, PLAYBACK_COMPLETED)),
        GET_AUDIO_SESSION_ID(EnumSet.allOf(State.class)),
        GET_CURRENT_POSITION(EnumSet.of(STARTED, INITIALIZED, PREPARED, STARTED, PAUSED, STOPPED, PLAYBACK_COMPLETED)),
        GET_DURATION(EnumSet.of(PREPARED, STARTED, PAUSED, STOPPED, PLAYBACK_COMPLETED)),
        GET_VIDEO_HEIGHT(EnumSet.of(STARTED, INITIALIZED, PREPARED, STARTED, PAUSED, STOPPED, PLAYBACK_COMPLETED)),
        GET_VIDEO_WIDTH(EnumSet.of(STARTED, INITIALIZED, PREPARED, STARTED, PAUSED, STOPPED, PLAYBACK_COMPLETED)),
        IS_PLAYING(EnumSet.of(STARTED, INITIALIZED, PREPARED, STARTED, PAUSED, STOPPED, PLAYBACK_COMPLETED)),
        PAUSE(EnumSet.of(STARTED, PAUSED, PLAYBACK_COMPLETED)),
        PREPARE(EnumSet.of(INITIALIZED, STOPPED)),
        PREPARE_ASYNC(EnumSet.of(INITIALIZED, STOPPED)),
        RELEASE(EnumSet.allOf(State.class)),
        RESET(EnumSet.of(STARTED, INITIALIZED, PREPARED, STARTED, PAUSED, STOPPED, PLAYBACK_COMPLETED, ERROR)),
        SEEK_TO(EnumSet.of(PREPARED, STARTED, PAUSED, PLAYBACK_COMPLETED)),
        SET_AUDIO_ATTRIBUTES(EnumSet.of(STARTED, INITIALIZED, STOPPED, PREPARED, STARTED, PAUSED, PLAYBACK_COMPLETED)),
        SET_AUDIO_SESSION_ID(EnumSet.of(STARTED)),
        SET_AUDIO_STREAM_TYPE(EnumSet.of(STARTED, INITIALIZED, STOPPED, PREPARED, STARTED, PAUSED, PLAYBACK_COMPLETED)),
        SET_AUX_EFFECT_SEND_LEVEL(EnumSet.allOf(State.class)),
        SET_DATA_SOURCE(EnumSet.of(STARTED)),
        SET_DISPLAY(EnumSet.allOf(State.class)),
        SET_SURFACE(EnumSet.allOf(State.class)),
        SET_VIDEO_SCALING_MODE(EnumSet.of(INITIALIZED, PREPARED, STARTED, PAUSED, STOPPED, PLAYBACK_COMPLETED)),
        SET_LOOPING(EnumSet.of(STARTED, INITIALIZED, STOPPED, PREPARED, STARTED, PAUSED, PLAYBACK_COMPLETED)),
        IS_LOOPING(EnumSet.allOf(State.class)),
        SET_ON_BUFFERING_UPDATE_LISTENER(EnumSet.allOf(State.class)),
        SET_ON_COMPLETION_LISTENER(EnumSet.allOf(State.class)),
        SET_ON_ERROR_LISTENER(EnumSet.allOf(State.class)),
        SET_ON_PREPARED_LISTENER(EnumSet.allOf(State.class)),
        SET_ON_SEEK_COMPLETE_LISTENER(EnumSet.allOf(State.class)),
        SET_PLAYBACK_PARAMS(EnumSet.allOf(State.class)),
        SET_SCREEN_ON_WHILE_PLAYING(EnumSet.allOf(State.class)),
        SET_VOLUME(EnumSet.of(STARTED, INITIALIZED, STOPPED, PREPARED, STARTED, PAUSED, PLAYBACK_COMPLETED)),
        SET_WAKE_MODE(EnumSet.allOf(State.class)),
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
    }
}