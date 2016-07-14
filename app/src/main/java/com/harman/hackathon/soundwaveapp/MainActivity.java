package com.harman.hackathon.soundwaveapp;

import android.media.AudioFormat;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.harman.hackathon.soundwaveapp.recording.HQRecorder;
import com.harman.hackathon.soundwaveapp.recording.QualityAnalyzer;

import java.io.InputStream;


public class MainActivity extends AppCompatActivity {

    static final String TEST_FILE_NAME = Environment.getExternalStorageDirectory().getPath() + "/" + "testFile.wav";

    private void playSound(){
        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.sample);
        mp.start();
    }

    private void recordSound() {

        HQRecorder hqr = new HQRecorder(true, MediaRecorder.AudioSource.MIC,44100, AudioFormat.CHANNEL_IN_MONO,AudioFormat.ENCODING_PCM_16BIT);
        hqr.setOutputFile(TEST_FILE_NAME);
        hqr.prepare();
        hqr.start();

        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        hqr.stop();
        hqr.release();
    }

    private int scoreSound() {
        InputStream x = getResources().openRawResource(R.raw.sample);
        InputStream fis = x;

        return new QualityAnalyzer().analyzeFile(fis,TEST_FILE_NAME);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //String sampleFileName = Uri.parse("android.resource://com.com.harman.hackathon.soundwaveapp/" + R.raw.sample).getPath();

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
                p.setAnchorId(View.NO_ID);
                fab.setLayoutParams(p);
                fab.setVisibility(View.GONE);

                playSound();
                recordSound();
                int score = scoreSound();

                p.setAnchorId(View.NO_ID);
                fab.setLayoutParams(p);
                fab.setVisibility(View.VISIBLE);

                Snackbar.make(view, "Your Score is "+ score, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
