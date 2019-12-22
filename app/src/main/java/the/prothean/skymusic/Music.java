package the.prothean.skymusic;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Music implements Serializable {

    String songName;
    Bitmap cover;

    public Music(String songName, Bitmap  cover) {
        this.songName = songName;
        this.cover = cover;
    }


}
