package com.example.android.quizu;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Splash extends AppCompatActivity {
    int i = 0;
    boolean isConnectivityAvailable = false;
    FrameLayout splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splash = findViewById(R.id.splash_page);
        final ProgressBar bar = findViewById(R.id.splashprogress);
        bar.setIndeterminate(true);

        bar.setProgress(i);
        final MediaPlayer player = MediaPlayer.create(this, R.raw.entryclip);
        player.start();
        checkForConnection();
        File file = new File(this.getFilesDir(), "QuizQues.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
                Log.e("create new file", "create new file");


            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("file length", String.valueOf(file.length()));
        }
//        SQLiteDatabase db=this.openOrCreateDatabase("quesdb",MODE_PRIVATE,null);
//        db.execSQL("create table mixed_questions(ques varchar,)");
        DownloadQuestionSet set = new DownloadQuestionSet();
        set.setFile(file);
        final DownloadQuestionSet.DownloadQuestionTask task = set.new DownloadQuestionTask();

        if (isConnectivityAvailable || file.length() > 0) {
            task.execute("https://opentdb.com/api.php?amount=50&difficulty=medium&type=multiple");
            final TextView loading = findViewById(R.id.loading_textview);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    int count = 0;
                    while (task.getStatus() != AsyncTask.Status.FINISHED) {
                        count = count % 24000;
                        if (count == 0) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    loading.setText("Updating.");
                                }
                            });

                            //count++;
                        }
                        if (count == 8000) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    loading.setText("Updating..");
                                }
                            });
                            //count++;
                        }
                        if (count == 12000) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    loading.setText("Updating...");
                                }
                            });
                            //count++;
                        }
                        count++;
                    }
                    startActivity(new Intent(Splash.this, StartPage.class));
                    Splash.this.finish();
                }
            }).start();
        } else {
            Snackbar.make(splash, "Please Connect to Internet", Snackbar.LENGTH_INDEFINITE).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                    return;
                }
            }, 4000);
        }
        player.release();
    }

    public void checkForConnection() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://www.google.com");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(1000);
                    connection.connect();
                    isConnectivityAvailable = connection.getResponseCode() == 200;
                    Log.e("hello", String.valueOf(isConnectivityAvailable));
                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try {
            t.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}