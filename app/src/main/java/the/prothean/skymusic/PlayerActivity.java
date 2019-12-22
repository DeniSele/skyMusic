package the.prothean.skymusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import the.prothean.entryactivity.R;

public class PlayerActivity extends AppCompatActivity {

    static MediaPlayer mp;
    int position;
    SeekBar sb;
    ArrayList<File> mySongsFiles;
    ArrayList<Music> mySongs = new ArrayList<>();
    Thread updateSeekBar;
    Button pause, next, previous;
    TextView songNameText;
    ImageView coverIV;

    String sname;

    public Bitmap getCover(Uri uri) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        byte[] rawArt;
        Bitmap art;
        BitmapFactory.Options bfo = new BitmapFactory.Options();

        mmr.setDataSource(getApplicationContext(), uri);
        rawArt = mmr.getEmbeddedPicture();
        if (null != rawArt)
            art = BitmapFactory.decodeByteArray(rawArt, 0, rawArt.length, bfo);
        else
            art = BitmapFactory.decodeResource(getResources(), R.drawable.cover_art);
        return art;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        songNameText = findViewById(R.id.txtSongLabel);
        coverIV = findViewById(R.id.album_art);

        pause = findViewById(R.id.pause);

        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);

        sb = findViewById(R.id.seekBar);

        updateSeekBar = new Thread() {
            @Override
            public void run() {
                int totalDuration = mp.getDuration();
                int currentPosition = 0;
                while (currentPosition < totalDuration) {
                    try {
                        sleep(500);
                        currentPosition = mp.getCurrentPosition();
                        sb.setProgress(currentPosition);
                    } catch (InterruptedException e) {

                    }
                }
                return;
            }
        };

        if (mp != null) {
            mp.stop();
            mp.release();
        }
        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        mySongsFiles = (ArrayList) b.getParcelableArrayList("songsfile");

        for (int i = 0; i < mySongsFiles.size(); i++) {
            mySongs.add(new Music(mySongsFiles.get(i).getName()
                    .replace(".mp3", "")
                    .replace(".wav", ""),
                    getCover(Uri.fromFile(mySongsFiles.get(i)))));
        }

        position = b.getInt("pos", 0);
        Log.d("INFO",""+position);
        Uri u = Uri.parse(mySongsFiles.get(position).toString());
        coverIV.setImageBitmap(mySongs.get(position).cover);
        mp = MediaPlayer.create(getApplicationContext(), u);
        sname = mySongs.get(position).songName;

        songNameText.setText(mySongs.get(position).songName);
        songNameText.setSelected(true);
        mp.start();
        sb.setMax(mp.getDuration());
        updateSeekBar.start();

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i,
                                          boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mp.seekTo(seekBar.getProgress());

            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sb.setMax(mp.getDuration());
                if (mp.isPlaying()) {
                    pause.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);
                    mp.pause();

                } else {
                    pause.setBackgroundResource(R.drawable.pause);
                    mp.start();
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
                mp.release();
                position = ((position + 1) % mySongs.size());
                Uri u = Uri.parse(mySongsFiles.get(position).toString());
                mp = MediaPlayer.create(getApplicationContext(), u);

                sname = mySongs.get(position).songName;
                songNameText.setText(sname);
                coverIV.setImageBitmap(mySongs.get(position).cover);
                try {
                    mp.start();
                } catch (Exception e) {
                }
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //songNameText.setText(getSongName);
                mp.stop();
                mp.release();

                position = ((position - 1) < 0) ? (mySongs.size() - 1) : (position - 1);
                Uri u = Uri.parse(mySongsFiles.get(position).toString());
                mp = MediaPlayer.create(getApplicationContext(), u);
                sname = mySongsFiles.get(position).getName();
                songNameText.setText(sname);
                coverIV.setImageBitmap(mySongs.get(position).cover);
                mp.start();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

}