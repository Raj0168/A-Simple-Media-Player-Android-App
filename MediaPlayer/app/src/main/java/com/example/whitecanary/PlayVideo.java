package com.example.whitecanary;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;

public class PlayVideo extends AppCompatActivity {


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateSeek.interrupt();
    }

    String textContent;
    int position;
    SeekBar seekBar;
    Thread updateSeek;
    ArrayList<File> videos;
    ImageView videotog, videoprev, videonext;
    MediaPlayer mediaPlayer;
    SurfaceView surfaceView;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        surfaceView = findViewById(R.id.surfaceView);
        seekBar = findViewById(R.id.vidseek);
        surfaceView.setKeepScreenOn(true);
        textView = findViewById(R.id.video_name);
        videotog = findViewById(R.id.videotog);
        videoprev = findViewById(R.id.videoprev);
        videonext = findViewById(R.id.videonext);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        videos = (ArrayList)bundle.getParcelableArrayList("videoList");
        textContent = intent.getStringExtra("currentVideo");
        textView.setText(textContent);
        textView.setSelected(true);

        position = intent.getIntExtra("position", 0);
        Uri uri = Uri.parse(videos.get(position).toString());
        mediaPlayer = MediaPlayer.create(this, uri);
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());


        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                mediaPlayer.setDisplay(surfaceHolder);

                /*
                int videoWidth = mediaPlayer.getVideoWidth();
                int videoHeight = mediaPlayer.getVideoHeight();
                float videoProportion = (float) videoWidth / (float) videoHeight;

                // Get the width of the screen
                DisplayMetrics metrics = getResources().getDisplayMetrics();
                int screenWidth = metrics.widthPixels;
                int screenHeight = metrics.heightPixels;
                float screenProportion = (float) screenWidth / (float) screenHeight;

                // Get the SurfaceView layout parameters
                android.view.ViewGroup.LayoutParams lp = surfaceView.getLayoutParams();
                if (videoProportion > screenProportion) {
                    lp.width = screenWidth;
                    lp.height = (int) ((float) screenWidth / videoProportion);
                } else {
                    lp.width = (int) (videoProportion * (float) screenHeight);
                    lp.height = screenHeight;
                }
                // Commit the layout parameters
                surfaceView.setLayoutParams(lp);
                */
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

            }
        });

        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        updateSeek = new Thread() {
            @Override
            public void run() {
                int currentPosition = 0;
                try {
                    while(currentPosition<mediaPlayer.getDuration()) {
                        currentPosition = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                        sleep(200);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        updateSeek.start();

        videotog.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                textContent = videos.get(position).getName().toString();
                textView.setText(textContent);

                if(mediaPlayer.isPlaying()) {
                    videotog.setImageResource(R.drawable.play2);
                    mediaPlayer.pause();
                }
                else {
                    videotog.setImageResource(R.drawable.pause2);
                    mediaPlayer.start();
                }
                ;
            }
        });

        videoprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(position!=0)
                    position=position-1;
                else
                    position=videos.size()-1;
                Uri uri = Uri.parse(videos.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();
                videotog.setImageResource(R.drawable.pause2);
                seekBar.setMax(mediaPlayer.getDuration());

                textContent = videos.get(position).getName().toString();
                textView.setText(textContent);
                seekBar.setProgress(0);
                mediaPlayer.setDisplay(surfaceHolder);
            }
        });

        videonext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(position!=videos.size()-1)
                    position=position+1;
                else
                    position=0;
                Uri uri = Uri.parse(videos.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();
                videotog.setImageResource(R.drawable.pause2);
                seekBar.setMax(mediaPlayer.getDuration());

                textContent = videos.get(position).getName().toString();
                textView.setText(textContent);
                seekBar.setProgress(0);
                mediaPlayer.setDisplay(surfaceHolder);
            }
        });


    }
}