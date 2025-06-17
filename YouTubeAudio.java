package com.tido.music;

import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.runtime.EventDispatcher;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.services.youtube.YoutubeService;
import org.schabi.newpipe.extractor.stream.StreamExtractor;
import org.schabi.newpipe.extractor.stream.AudioStream;
import org.schabi.newpipe.downloader.Downloader;

import java.util.List;

public class YouTubeAudioExtractor extends AndroidNonvisibleComponent {

    public YouTubeAudioExtractor(ComponentContainer container) {
        super(container.$form());
        // Initialize the NewPipe Extractor library
        NewPipe.init(Downloader.getInstance());
    }

    @SimpleFunction(description = "Extracts the audio URL from a YouTube video link.")
    public void GetAudioUrl(String videoUrl) {
        // Network operations must be done in the background, not on the main UI thread.
        new Thread(() -> {
            try {
                YoutubeService service = new YoutubeService(0);
                StreamExtractor extractor = service.getStreamExtractor(videoUrl);
                extractor.fetchPage(); // This fetches the video details

                // Get the best quality audio stream
                List<AudioStream> audioStreams = extractor.getAudioStreams();
                if (audioStreams.isEmpty()) {
                    ExtractionFailed("No audio streams found for this video.");
                    return;
                }

                // For simplicity, we'll pick the first one which is often the best.
                String resultUrl = audioStreams.get(0).getUrl();

                // Trigger the 'GotAudioUrl' event with the result
                GotAudioUrl(resultUrl);

            } catch (Exception e) {
                // If anything goes wrong, trigger the 'ExtractionFailed' event
                ExtractionFailed(e.getMessage());
            }
        }).start();
    }

    @SimpleEvent(description = "Event triggered when the audio URL has been successfully extracted.")
    public void GotAudioUrl(String url) {
        // This dispatches the event to the Kodular blocks
        EventDispatcher.dispatchEvent(this, "GotAudioUrl", url);
    }

    @SimpleEvent(description = "Event triggered if the extraction fails.")
    public void ExtractionFailed(String reason) {
        EventDispatcher.dispatchEvent(this, "ExtractionFailed", reason);
    }
}
