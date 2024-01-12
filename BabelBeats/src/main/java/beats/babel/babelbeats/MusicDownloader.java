package beats.babel.babelbeats;
import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder;
import java.util.List;
import java.util.ArrayList;

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
                    "--output", "src/main/resources/audio/" + name.replace(" ", "_") + ".mp3",
                    url);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            exitCode = process.waitFor();
            }
        catch (Exception e) {
            e.printStackTrace();

        }
//        exitCode = separate(name);
        return exitCode;
    }

//    public static int separate(String name) {
//        int exitCode = 0;
//        ProcessBuilder processBuilder = new ProcessBuilder(
//                "python",
//                "-m",
//                "demucs",
//                "--mp3",
//                "--two-stems=vocals",
//                "'src/main/resources/audio/Travis_Scott_FE!N_(feat._Playboi_Carti).mp3'",
//                "-o",
//                "src/main/resources/audio/"
//        );
//
//        try {
//            Process process = processBuilder.start();
//            exitCode = process.waitFor();
//            System.out.println("Exit Code: " + exitCode);
//
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        return exitCode;
//    }
}
