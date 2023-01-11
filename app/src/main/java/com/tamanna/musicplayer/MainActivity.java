package com.tamanna.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    //Widgets
    TextView song_title,start_time;
    Button back_btn,play_btn,pause_btn,forward_btn;
    SeekBar seekbar;

    //Media Player
    MediaPlayer mediaPlayer;

    //Handler

    Handler handler = new Handler();

    //Variables
    double startTime = 0;
    double endTime=0;
    int forward= 10000;
    int backward= 10000;
    static int oneTimeOnly = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play_btn = findViewById(R.id.play_btn);
        pause_btn = findViewById(R.id.pause_btn);
        forward_btn = findViewById(R.id.forward_btn);
        back_btn = findViewById(R.id.back_btn);

        song_title = findViewById(R.id.song_title);
        start_time = findViewById(R.id.time_left_text);

        seekbar = findViewById(R.id.seekBar);

        // media player
        mediaPlayer = MediaPlayer.create(
                this,
                R.raw.astronaut
        );


        song_title.setText(getResources().getIdentifier(
                "astronaut",
                "raw",
                getPackageName()
        ) );
        seekbar.setClickable(true);

        // Adding Functionalities for the buttons
        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayMusic();
            }
        });

        pause_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.pause();

            }
        });


        forward_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp = (int) startTime;
                if ((temp + forward) <= endTime){
                    startTime = startTime + forward;
                    mediaPlayer.seekTo((int) startTime);

                }else{
                    Toast.makeText(MainActivity.this,
                            "Can't Jump Forward!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp = (int) startTime;

                if ((temp - backward) > 0){
                    startTime = startTime - backward;
                    mediaPlayer.seekTo((int) startTime);
                }else{
                    Toast.makeText(MainActivity.this,
                            "Can't Go Back!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void PlayMusic() {
        mediaPlayer.start();

        endTime = mediaPlayer.getDuration();
        startTime = mediaPlayer.getCurrentPosition();

        if (oneTimeOnly == 0){
            seekbar.setMax((int) endTime);
            oneTimeOnly = 1;
        }

        start_time.setText(String.format(
                "%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes((long) endTime),
                TimeUnit.MILLISECONDS.toSeconds((long) endTime)-
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes
                                ((long) endTime))
        ));

        seekbar.setProgress((int) startTime);

       // Handlers allows you to send and process message and runnable object  associated
        // with the  message queue. Each handler instance is associate with a single thread
        // and that with the threads message queue.

        handler.postDelayed(UpdateSongTime, 100);
    }

    // Creating the Runnable
    private Runnable UpdateSongTime = new Runnable() {
        @Override
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            start_time.setText(String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime)
                            - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime))
            ));
            seekbar.setProgress((int)startTime);
            handler.postDelayed(this, 100);
        }
    };
}