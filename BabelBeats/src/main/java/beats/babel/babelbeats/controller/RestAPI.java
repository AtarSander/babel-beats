package beats.babel.babelbeats.controller;

import beats.babel.babelbeats.SpotifyHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RestAPI {
    private final SpotifyHandler spotifyHandler;

    @Autowired
    public RestAPI(SpotifyHandler sh){
        this.spotifyHandler = sh;
    }
    @GetMapping("/resume")
    public void resumePlayback(){
        spotifyHandler.startPlayback();
    }

    @GetMapping("/pause")
    public void pausePlayback(){
        spotifyHandler.pausePlayback();
    }
}
