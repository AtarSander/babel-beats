package beats.babel.babelbeats;
import java.io.IOException;
import java.lang.ProcessBuilder;

public class MusicDownloader {

    public static void download(String url, String name)
    {
        try {
            // Build the command
            ProcessBuilder processBuilder = new ProcessBuilder("yt-dlp",
                    "--extract-audio",
                    "--audio-format", "mp3",
                    "--audio-quality", "0",
                    "--output", "src/main/resources/audio/" + name + ".mp3",
                    url);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            }
        catch (IOException e) {
            e.printStackTrace();

        }
    }
}
