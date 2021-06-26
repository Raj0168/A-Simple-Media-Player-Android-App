package com.example.whitecanary;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ListView listView, listView2;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        listView = findViewById(R.id.listView);
        listView2 = findViewById(R.id.listView2);
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

//                        Toast.makeText(MainActivity.this, "Runtime permission given", Toast.LENGTH_SHORT).show();

/*                        ArrayList<File> myMedia = fetchSongs(Environment.getExternalStorageDirectory());

                        int noaudio=0, novideo=0;

                        for(int i=0; i<myMedia.size(); i++) {
                            if(myMedia.get(i).getName().endsWith(".mp3")) {
                                noaudio++;
                            }
                            else {
                                novideo++;
                            }
                        }

                        String [] audios = new String[noaudio];
                        String [] videos = new String[novideo];
                        int aa=0, vv=0;
                        for(int i=0; i<myMedia.size(); i++) {
                            if(myMedia.get(i).getName().endsWith(".mp3")) {
                                audios[aa] = myMedia.get(i).getName().replace(".mp3", "");
                                aa++;
                            }
                            else {
                                videos[vv] = myMedia.get(i).getName().replace(".mp4", "");
                                vv++;
                            }
                        }

 */

                        ArrayList<File> mySongs = fetchSongs(Environment.getExternalStorageDirectory());
                        String [] audios = new String[mySongs.size()];
                        for(int i=0;i<mySongs.size();i++){
                            audios[i] = mySongs.get(i).getName().replace(".mp3", "");
                        }

                        ArrayList<File> myVideos = fetchVideos(Environment.getExternalStorageDirectory());
                        String [] videos = new String[myVideos.size()];
                        for(int i=0;i<myVideos.size();i++){
                            videos[i] = myVideos.get(i).getName().replace(".mp4", "");
                        }

                        ArrayAdapter<String> audioadapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, audios);
                        listView.setAdapter(audioadapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(MainActivity.this, PlaySong.class);
                                String currentSong = listView.getItemAtPosition(position).toString();
                                intent.putExtra("songList", mySongs);
                                intent.putExtra("currentSong", currentSong);
                                intent.putExtra("position", position);
                                startActivity(intent);
                            }
                        });

                        ArrayAdapter<String> videoadapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, videos);
                        listView2.setAdapter(videoadapter);
                        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(MainActivity.this, PlayVideo.class);
                                String currentVideo = listView2.getItemAtPosition(position).toString();
                                intent.putExtra("videoList", myVideos);
                                intent.putExtra("currentVideo", currentVideo);
                                intent.putExtra("position", position);
                                startActivity(intent);
                            }
                        });




/*
                        ArrayAdapter<String> videoadapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, videos);
                        listView.setAdapter(videoadapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(MainActivity.this, PlaySong.class);
                                String currentSong = listView.getItemAtPosition(position).toString();
                                intent.putExtra("songList", myMedia);
                                intent.putExtra("currentSong", currentSong);
                                intent.putExtra("position", position);
                                startActivity(intent);
                            }
                        });

                        ArrayAdapter<String> audioadapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, audios);
                        listView.setAdapter(audioadapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(MainActivity.this, PlaySong.class);
                                String currentSong = listView.getItemAtPosition(position).toString();
                                intent.putExtra("songList", myMedia);
                                intent.putExtra("currentSong", currentSong);
                                intent.putExtra("position", position);
                                startActivity(intent);
                            }
                        });
 */

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(MainActivity.this, "Storage Permission is required", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                })
                .check();
    }

    public ArrayList<File> fetchSongs(File file){
        ArrayList<File> arrayList = new ArrayList<>();
        File [] songs = file.listFiles();
        if(songs !=null){
            for(File myFile: songs){
                if(!myFile.isHidden() && myFile.isDirectory()){
                    arrayList.addAll(fetchSongs(myFile));
                }
                else{
                    if(myFile.getName().endsWith(".mp3")  && !myFile.getName().startsWith(".")) {
                        arrayList.add(myFile);
                    }
                }
            }
        }
        return arrayList;
    }

    public ArrayList<File> fetchVideos(File file){
        ArrayList<File> arrayList = new ArrayList<>();
        File [] videos = file.listFiles();
        if(videos !=null){
            for(File myFile: videos){
                if(!myFile.isHidden() && myFile.isDirectory()){
                    arrayList.addAll(fetchVideos(myFile));
                }
                else{
                    if(myFile.getName().endsWith(".mp4")  && !myFile.getName().startsWith(".")) {
                        arrayList.add(myFile);
                    }
                }
            }
        }
        return arrayList;
    }
}