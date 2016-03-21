package org.spacebison.statefulmediaplayer;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;

import java.util.HashMap;
import java.util.Map;

/**
 * MediaPlayer error codes.
 */
@SuppressLint("InlinedApi")
public enum MediaError {
    /**
     * {@link MediaPlayer#MEDIA_ERROR_IO}
     */
    IO(MediaPlayer.MEDIA_ERROR_IO),
    /**
     * {@link MediaPlayer#MEDIA_ERROR_MALFORMED}
     */
    MALFORMED(MediaPlayer.MEDIA_ERROR_MALFORMED),
    /**
     * {@link MediaPlayer#MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK
     **/
    NOT_VALID_FOR_PROGRESSIVE_PLAYBACK(MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK),
    /**
     * {@link MediaPlayer#MEDIA_ERROR_SERVER_DIED}
     */
    SERVER_DIED(MediaPlayer.MEDIA_ERROR_SERVER_DIED),
    /**
     * {@link MediaPlayer#MEDIA_ERROR_TIMED_OUT}
     */
    TIMED_OUT(MediaPlayer.MEDIA_ERROR_TIMED_OUT),
    /**
     * {@link MediaPlayer#MEDIA_ERROR_UNKNOWN}
     */
    UNKNOWN(MediaPlayer.MEDIA_ERROR_UNKNOWN),
    /**
     * {@link MediaPlayer#MEDIA_ERROR_UNSUPPORTED}
     */
    UNSUPPORTED(MediaPlayer.MEDIA_ERROR_UNSUPPORTED),
    PVMFFailure(-1),
    /**
     Error due to cancellation
     */
    PVMFErrCancelled(-2),
    /**
     Error due to no memory being available
     */
    PVMFErrNoMemory(-3),
    /**
     Error due to request not being supported
     */
    PVMFErrNotSupported(-4),
    /**
     Error due to invalid argument
     */
    PVMFErrArgument(-5),
    /**
     Error due to invalid resource handle being specified
     */
    PVMFErrBadHandle(-6),
    /**
     Error due to resource already exists and another one cannot be created
     */
    PVMFErrAlreadyExists(-7),
    /**
     Error due to resource being busy and request cannot be handled
     */
    PVMFErrBusy(-8),
    /**
     Error due to resource not ready to accept request
     */
    PVMFErrNotReady(-9),
    /**
     Error due to data corruption being detected
     */
    PVMFErrCorrupt(-10),
    /**
     Error due to request timing out
     */
    PVMFErrTimeout(-11),
    /**
     Error due to general overflow
     */
    PVMFErrOverflow(-12),
    /**
     Error due to general underflow
     */
    PVMFErrUnderflow(-13),
    /**
     Error due to resource being in wrong state to handle request
     */
    PVMFErrInvalidState(-14),
    /**
     Error due to resource not being available
     */
    PVMFErrNoResources(-15),
    /**
     Error due to invalid configuration of resource
     */
    PVMFErrResourceConfiguration(-16),
    /**
     Error due to general error in underlying resource
     */
    PVMFErrResource(-17),
    /**
     Error due to general data processing
     */
    PVMFErrProcessing(-18),
    /**
     Error due to general port processing
     */
    PVMFErrPortProcessing(-19),
    /**
     Error due to lack of authorization to access a resource.
     */
    PVMFErrAccessDenied(-20),
    /**
     Error due to the lack of a valid license for the content
     */
    PVMFErrLicenseRequired(-21),
    /**
     Error due to the lack of a valid license for the content.  However
     a preview is available.
     */
    PVMFErrLicenseRequiredPreviewAvailable(-22),
    /**
     Error due to the download content length larger than the maximum request size
     */
    PVMFErrContentTooLarge(-23),
    /**
     Error due to a maximum number of objects in use
     */
    PVMFErrMaxReached(-24),
    /**
     Return code for low disk space
     */
    PVMFLowDiskSpace(-25),
    /**
     Error due to the requirement of user-id and password input from app for HTTP basic/digest authentication
     */
    PVMFErrHTTPAuthenticationRequired(-26),
    /**
     PVMFMediaClock specific error. Callback has become invalid due to change in direction of NPT clock.
     */
    PVMFErrCallbackHasBecomeInvalid(-27),
    /**
     PVMFMediaClock specific error. Callback is called as clock has stopped.
     */
    PVMFErrCallbackClockStopped(-28),
    /**
     Error due to missing call for ReleaseMatadataValue() API
     */
    PVMFErrReleaseMetadataValueNotDone(-29),
    /**
     Error due to the redirect error
     */
    PVMFErrRedirect(-30),
    /**
     Error if a given method or API is not implemented. This is NOT the same as PVMFErrNotSupported.
     */
    PVMFErrNotImplemented(-31),
    /**
     Error: the video container is not valid for progressive playback.
     */
    PVMFErrContentInvalidForProgressivePlayback(-32);

    private static final Map<Integer, MediaError> mCodeMap = new HashMap<>(values().length);

    static {
        final MediaError[] values = values();
        for (MediaError me : values) {
            mCodeMap.put(me.mErrorCode, me);
        }
    }

    private final int mErrorCode;

    MediaError(int errorCode) {
        mErrorCode = errorCode;
    }

    public static MediaError fromCode(int errorCode) {
        if (mCodeMap.containsKey(errorCode)) {
            return mCodeMap.get(errorCode);
        } else {
            return UNKNOWN;
        }
    }

    public int getErrorCode() {
        return mErrorCode;
    }
}
