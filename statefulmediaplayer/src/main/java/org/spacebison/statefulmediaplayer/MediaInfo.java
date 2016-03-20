package org.spacebison.statefulmediaplayer;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;

/**
 * MediaPlayer info codes;
 */
@SuppressLint("InlinedApi")
public enum MediaInfo {
    /**
     * {@link MediaPlayer#MEDIA_INFO_BAD_INTERLEAVING}
     **/
    BAD_INTERLEAVING(MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING),
    /**
     * {@link MediaPlayer#MEDIA_INFO_BUFFERING_END}
     **/
    BUFFERING_END(MediaPlayer.MEDIA_INFO_BUFFERING_END),
    /**
     * {@link MediaPlayer#MEDIA_INFO_BUFFERING_START}
     **/
    BUFFERING_START(MediaPlayer.MEDIA_INFO_BUFFERING_START),
    /**
     * {@link MediaPlayer#MEDIA_INFO_METADATA_UPDATE}
     **/
    METADATA_UPDATE(MediaPlayer.MEDIA_INFO_METADATA_UPDATE),
    /**
     * {@link MediaPlayer#MEDIA_INFO_NOT_SEEKABLE}
     **/
    NOT_SEEKABLE(MediaPlayer.MEDIA_INFO_NOT_SEEKABLE),
    /**
     * {@link MediaPlayer#MEDIA_INFO_SUBTITLE_TIMED_OUT}
     **/
    SUBTITLE_TIMED_OUT(MediaPlayer.MEDIA_INFO_SUBTITLE_TIMED_OUT),
    /**
     * {@link MediaPlayer#MEDIA_INFO_UNKNOWN}
     **/
    UNKNOWN(MediaPlayer.MEDIA_INFO_UNKNOWN),
    /**
     * {@link MediaPlayer#MEDIA_INFO_UNSUPPORTED_SUBTITLE}
     **/
    UNSUPPORTED_SUBTITLE(MediaPlayer.MEDIA_INFO_UNSUPPORTED_SUBTITLE),
    /**
     * {@link MediaPlayer#MEDIA_INFO_VIDEO_RENDERING_START}
     **/
    VIDEO_RENDERING_START(MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START),
    /**
     * {@link MediaPlayer#MEDIA_INFO_VIDEO_TRACK_LAGGING}
     **/
    VIDEO_TRACK_LAGGING(MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING),
    /**
     Notification that a port was created
     */
    PVMFInfoPortCreated(10),
    /**
     Notification that a port was deleted
     */
    PVMFInfoPortDeleted(11),
    /**
     Notification that a port was connected
     */
    PVMFInfoPortConnected(12),
    /**
     Notification that a port was disconnected
     */
    PVMFInfoPortDisconnected(13),
    /**
     Notification that an overflow occurred (not fatal error)
     */
    PVMFInfoOverflow(14),
    /**
     Notification that an underflow occurred (not fatal error)
     */
    PVMFInfoUnderflow(15),
    /**
     Notification that a processing failure occurred (not fatal error)
     */
    PVMFInfoProcessingFailure(16),
    /**
     Notification that end of data stream has been reached
     */
    PVMFInfoEndOfData(17),
    /**
     Notification that a data buffer has been created
     */
    PVMFInfoBufferCreated(18),
    /**
     Notification that buffering of data has started
     */
    PVMFInfoBufferingStart(19),
    /**
     Notification for data buffering level status
     */
    PVMFInfoBufferingStatus(20),
    /**
     Notification that data buffering has completed
     */
    PVMFInfoBufferingComplete(21),
    /**
     Notification that data is ready for use
     */
    PVMFInfoDataReady(22),
    /**
     Notification for position status
     */
    PVMFInfoPositionStatus(23),
    /**
     Notification for node state change
     */
    PVMFInfoStateChanged(24),
    /**
     Notification that data was discarded during synchronization.
     */
    PVMFInfoDataDiscarded(25),
    /**
     Notification that error handling has started
     */
    PVMFInfoErrorHandlingStart(26),
    /**
     Notification that error handling has completed
     */
    PVMFInfoErrorHandlingComplete(27),
    /**
     Notification from a remote source
     */
    PVMFInfoRemoteSourceNotification(28),
    /**
     Notification that license acquisition has started.
     */
    PVMFInfoLicenseAcquisitionStarted(29),
    /**
     Notification that download content length is available
     */
    PVMFInfoContentLength(30),
    /**
     Notification that downloaded content reaches the maximum request size, and will
     be truncated, especially for the case of unavailable content length
     */
    PVMFInfoContentTruncated(31),
    /**
     Notification that source format is not supported, typically sent
     during protocol rollover
     */
    PVMFInfoSourceFormatNotSupported(32),
    /**
     Notification that a clip transition has occurred while playing a playlist
     */
    PVMFInfoPlayListClipTransition(33),
    /**
     Notification that content type for download or HTTP streaming is available
     */
    PVMFInfoContentType(34),
    /**
     Notification that paticular track is disable. This one is on a per track basis.
     */
    PVMFInfoTrackDisable(35),
    /**
     Notification that unexpected data has been obtained, especially for download,
     when client receives from server more data than requested in content-length header
     */
    PVMFInfoUnexpectedData(36),
    /**
     Notification that server discnnect happens after download is complete
     */
    PVMFInfoSessionDisconnect(37),
    /**
     Notification that new meadi stream has been started
     */
    PVMFInfoStartOfData(38),
    /**
     Notification that node has processed a command with ReportObserver marker info
     */
    PVMFInfoReportObserverRecieved(39),
    /**
     Notification that meta data is available with source node
     */
    PVMFInfoMetadataAvailable(40),
    /**
     Notification that duration is available with source node
     */
    PVMFInfoDurationAvailable(41),
    /**
     Notification that Change Position request not supported
     */
    PVMFInfoChangePlaybackPositionNotSupported(42),
    /**
     Notification that the content is poorly inter-leaved
     */
    PVMFInfoPoorlyInterleavedContent(43),
    /**
     Notification for actual playback position after repositioning
     */
    PVMFInfoActualPlaybackPosition(44),
    /**
     Notification that the live buffer is empty
     */
    PVMFInfoLiveBufferEmpty(45),
    /**
     Notification that a server has responded with 200 OK to a Playlist play request
     */
    PVMFInfoPlayListSwitch(46),
    /**
     Notification of configuration complete
     */
    PVMFMIOConfigurationComplete(47),
    /**
     Notification that the video track is falling behind
     */
    PVMFInfoVideoTrackFallingBehind(48),
    /**
     Notification that memory is not available for new RTP packets
     */
    PVMFInfoSourceOverflow(49),
    /**
     Notification for Media data length in shoutcast session
     */
    PVMFInfoShoutcastMediaDataLength(50),
    /**
     Notification for clip bitrate in shoutcast session
     */
    PVMFInfoShoutcastClipBitrate(51),
    /**
     Notification for shoutcast session
     */
    PVMFInfoIsShoutcastSesssion(52);

    private final int mInfoCode;

    MediaInfo(int infoCode) {
        mInfoCode = infoCode;
    }

    public static MediaInfo fromCode(int infoCode) {
        for (MediaInfo mi : values()) {
            if (mi.mInfoCode == infoCode) {
                return mi;
            }
        }
        return UNKNOWN;
    }

    public int getInfoCode() {
        return mInfoCode;
    }
}
