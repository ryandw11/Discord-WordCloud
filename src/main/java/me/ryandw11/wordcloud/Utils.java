package main.java.me.ryandw11.wordcloud;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Paths;

public class Utils {
    public static String getLocalPath(){
        File location = new File(Paths.get("").toAbsolutePath().toString() + File.separator + "wordcloud");
        if(!location.exists())
            location.mkdir();
        return location.toURI().getPath();
    }
}
