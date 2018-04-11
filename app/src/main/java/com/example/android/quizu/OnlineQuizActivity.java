package com.example.android.quizu;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class OnlineQuizActivity extends AppCompatActivity {
    public static ArrayList<JSONObject> quesList = new ArrayList<>();
    int TotalQuesNumber = 10;
    int score = 0;
    String username;
    int ques_limit = 0;
    Button button0;
    int quesNumber = 0;
    int locationOfCorrectAnswer = 0;
    String[] answers = new String[4];
    Button button1;
    String correct;
    Button button2;
    Button button3;
    TextView quesView;
    TextView app_name;
    TextView score_textview_online;
    LinearLayout quesSetView;

    public void checkAnswer(View v) {
        if (ques_limit < TotalQuesNumber) {
            if (v.getTag().toString().equals(String.valueOf(locationOfCorrectAnswer))) {
                Snackbar.make(findViewById(R.id.quesSet), "correct", Snackbar.LENGTH_SHORT).show();
                score++;
                updateScore();
            } else {
                Snackbar.make(findViewById(R.id.quesSet), "incorrect! Correct answer was " + correct, Snackbar.LENGTH_SHORT).show();
//                    Toast.makeText(this, "incorrect! Correct answer was " + correct, Toast.LENGTH_SHORT).show();
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    createQuestion();
                }
            }, 1000);
        } else {
            Intent intent = new Intent(this, ResultActivity.class);
            intent.putExtra("SCORE", score);
            intent.putExtra("TOTAL_QUES", TotalQuesNumber);
            intent.putExtra("username", username);
            startActivity(intent);
            finish();
        }

    }

    public void createQuestion() {


        ques_limit++;
        Random rand = new Random();
        quesNumber = rand.nextInt(quesList.size());
        locationOfCorrectAnswer = rand.nextInt(4);
        JSONObject ques = quesList.get(quesNumber);
        try {
            JSONArray json_incorrect_ans = ques.getJSONArray("incorrect_answers");
            String[] incorrect_ans = new String[json_incorrect_ans.length()];
            for (int i = 0; i < incorrect_ans.length; i++) {
                incorrect_ans[i] = json_incorrect_ans.getString(i);
            }
            button0.setText(incorrect_ans[0]);
            int p = 0;
            for (int i = 0; i < 4; i++) {
                if (i == locationOfCorrectAnswer) {
                    correct = answers[i] = ques.getString("correct_answer");

                } else {
                    answers[i] = incorrect_ans[p];
                    p++;
                }

            }
            String questionString = ques.getString("question");
            questionString = questionString.replaceAll("&ldquo;", "”");
            questionString = questionString.replaceAll("&rdquo;", "“");
            questionString = questionString.replaceAll("&amp;", "&");
            questionString = questionString.replaceAll("&lt;", "<");
            questionString = questionString.replaceAll("&gt;", ">");
            questionString = questionString.replaceAll("&#039;", "'");
            questionString = questionString.replaceAll("&quot;", "\"");
            quesView.setText(questionString);
            for (int i = 0; i < 4; i++) {
                answers[i] = answers[i].replaceAll("&ldquo;", "”");
                answers[i] = answers[i].replaceAll("&rdquo;", "“");
                answers[i] = answers[i].replaceAll("&amp;", "&");
                answers[i] = answers[i].replaceAll("&lt;", "<");
                answers[i] = answers[i].replaceAll("&gt;", ">");
                answers[i] = answers[i].replaceAll("&#039;", "'");
                answers[i] = answers[i].replaceAll("&quot;", "\"");


            }
            button0.setText(answers[0]);
            button1.setText(answers[1]);
            button2.setText(answers[2]);
            button3.setText(answers[3]);
        } catch (Exception e) {
            Log.d("createQues", "error");
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_online);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
        button0 = findViewById(R.id.button0);
        username = getIntent().getStringExtra("name");
        TotalQuesNumber = getIntent().getIntExtra("user_ques", 10);
        //                progress = findViewById(R.id.progress);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        quesView = findViewById(R.id.ques);
        score_textview_online = findViewById(R.id.score_textview_online);
        //                app_name = findViewById(R.id.app_name);
        //                loading = findViewById(R.id.loading);
        quesSetView = findViewById(R.id.quesSet);
        quesSetView.animate().alphaBy(1).setDuration(2000);
        createQuestion();
        updateScore();

    }

    private void updateScore() {
        score_textview_online.setText(String.valueOf(score) + "/" + String.valueOf(TotalQuesNumber));
    }

}
