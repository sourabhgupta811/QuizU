package com.example.android.quizu;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DownloadQuestionSet extends AppCompatActivity {
    File file;

    public void setFile(File file) {
        this.file = file;

    }


    public class DownloadQuestionTask extends AsyncTask<String, Void, String> {


        String result = "";
        ArrayList<JSONObject> quesList = new ArrayList<>();

        @Override
        protected String doInBackground(String... urls) {
            BufferedReader breader = null;
            BufferedWriter bwriter = null;
            String sample = "";
            try {
//                file=new File(getFilesDir(),"QuizQues.txt");
//                if(!file.exists()){
//                    try {
//                        file.createNewFile();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }

                FileInputStream in = new FileInputStream(file);
                InputStreamReader reader = new InputStreamReader(in);
                breader = new BufferedReader(reader);

                if ((sample = breader.readLine()) != null) {
                    result += sample;
                }
                if (sample == null) {
                    sample = "";
                }
            } catch (Exception e) {
                Log.e("error", "reached");
                e.printStackTrace();
            }
            //----------enter code for reading json-----------------

            if (result == "") {

                try {
                    URL url = new URL(urls[0]);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                    int c = 0;
                    Log.e("resultPrior", "hello" + result);
                    while ((c = reader.read()) != -1) {
                        result += (char) c;
                    }
                    Log.e("error", result);
                    if (sample.length() == 0) {
                        breader.close();
                        FileOutputStream out = new FileOutputStream(file);
                        OutputStreamWriter writer = new OutputStreamWriter(out);
                        bwriter = new BufferedWriter(writer);
                        bwriter.write(result, 0, result.length());

                    }
                    bwriter.close();
                    return result;


                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("error", "reached here in catch");
                }
            }


            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject data = new JSONObject(result);
                String specificData = data.getString("results");
                JSONArray array = new JSONArray(specificData);
                JSONObject obj = null;
                for (int i = 0; i < array.length(); i++) {
                    obj = array.getJSONObject(i);
                    quesList.add(obj);
                }
                OnlineQuizActivity.quesList = quesList;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}