package beats.babel.babelbeats;
import java.io.IOException;
import java.lang.ProcessBuilder;

public class MusicDownloader {

    public static int download(String url, String name)
    {
        int exitCode = 0;
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
            exitCode = process.waitFor();
            }
        catch (Exception e) {
            e.printStackTrace();

        }
        return exitCode;
    }
}
