package the.prothean.skymusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

import the.prothean.entryactivity.R;

public class MusicListActivity extends AppCompatActivity implements RecyclerAdapter.OnUserClickListener{

    ArrayList<Music> mySongs = new  ArrayList<Music>();
    ArrayList<File> mySongsFiles = new  ArrayList<File>();
    private RecyclerAdapter tableAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_music_list);
        RecyclerView recyclerView_main = findViewById(R.id.recyclerView_main_musics);
        recyclerView_main.setLayoutManager(new LinearLayoutManager(this));
        tableAdapter = new RecyclerAdapter(this);
        recyclerView_main.setAdapter(tableAdapter);

        runtimePermission();
    }

    public void runtimePermission(){
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        display();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    public ArrayList<File> findSong(File file){
        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = file.listFiles();

        for(File singleFile: files){

            if(singleFile.isDirectory() && !singleFile.isHidden()){
                arrayList.addAll(findSong(singleFile));
            }
            else {
                if(singleFile.getName().endsWith(".mp3") ||
                        singleFile.getName().endsWith(".wav")){
                    arrayList.add(singleFile);
                }
            }
        }

        return arrayList;
    }

    public Bitmap getCover(Uri uri){
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        byte[] rawArt;
        Bitmap art;
        BitmapFactory.Options bfo=new BitmapFactory.Options();

        mmr.setDataSource(getApplicationContext(), uri);
        rawArt = mmr.getEmbeddedPicture();
        if (null != rawArt)
            art = BitmapFactory.decodeByteArray(rawArt, 0, rawArt.length, bfo);
        else
            art = BitmapFactory.decodeResource(getResources(), R.drawable.cover_art);
        return  art;
    }

    void display(){
        mySongsFiles = findSong(Environment.getExternalStorageDirectory());
        for(int i = 0; i < mySongsFiles.size(); i++) {
            mySongs.add(new Music(mySongsFiles.get(i).getName()
                    .replace(".mp3", "")
                    .replace(".wav",""),
                    getCover(Uri.fromFile(mySongsFiles.get(i)))));
        }

        tableAdapter.updateElements(mySongs);
    }

    @Override
    public void onUserClick(int position) {
        Intent intent = new Intent(this, PlayerActivity.class);
        intent.putExtra("songsfile",  mySongsFiles)
                .putExtra("pos", position);
        startActivity(intent);
    }
}
