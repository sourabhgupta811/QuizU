package com.example.android.quizu;


import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    TextView ques_textview;
    TextView timer_textview;
    Bundle bundle;
    TextView last_message;
    TextView result_textview;
    TextView score_textveiw;
    Button button0;
    Button button1;
    Button button2;
    Button button3;
    Button play_again_button;
    CardView card;
    CardView playAgainCard;
    CardView score_card;
    CardView shareCard;
    int quesNumber;
    int time_remaining;
    int maxProgress;
    int score;
    int locationOfAnswer;
    String[] operator = new String[]{"+", "-", "/", "*"};
    String username;
    CountDownTimer timer;
    ArrayList<String> answers = new ArrayList<String>();
    GridLayout mainLayout;
    LinearLayout mainLinearLayout;
    RelativeLayout mainRelativeLayout;
    RelativeLayout progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));

        ProgressBar pb = findViewById(R.id.progressBarToday);
        bundle = savedInstanceState;
        mainRelativeLayout = findViewById(R.id.main_relative_layout);
        mainLinearLayout = findViewById(R.id.main_linear_layout);
        mainLayout = findViewById(R.id.main_grid_layout);
        playAgainCard = findViewById(R.id.play_again_card);
        shareCard = findViewById(R.id.share_card);
        ques_textview = findViewById(R.id.questions_textview);
        button0 = findViewById(R.id.button0);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        username = getIntent().getStringExtra("name");
        time_remaining = getIntent().getIntExtra("time", 1);
        time_remaining = time_remaining * 60;
        maxProgress = time_remaining * 1000;
        timer_textview = findViewById(R.id.timer);
        score_textveiw = findViewById(R.id.score_textview);
        last_message = findViewById(R.id.last_message);
        play_again_button = findViewById(R.id.play_again);
        result_textview = findViewById(R.id.result_textview);
        score_card = findViewById(R.id.score_card);
        progress_bar = findViewById(R.id.progressbar);
        ((ProgressBar) findViewById(R.id.progressBarToday)).setMax(time_remaining - 1);
        ObjectAnimator animation = ObjectAnimator.ofInt(progress_bar, "custom", 0, time_remaining); // see this max value coming back here, we animate towards that value
        animation.setDuration(time_remaining * 1000); //in milliseconds
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
        mainLinearLayout.setTranslationY(1000f);
        progress_bar.setTranslationX(-1000f);
        score_card.setTranslationX(1000f);
        mainLinearLayout.animate().translationYBy(-1000f).setDuration(1000);
        progress_bar.animate().translationXBy(1000f).setDuration(800);
        score_card.animate().translationXBy(-1000f).setDuration(900);

        if (savedInstanceState == null)
            generateQuestions();
        else {
            locationOfAnswer = savedInstanceState.getInt("locationOfAnswer");
            button0.setText(savedInstanceState.getString("button0"));
            ques_textview.setText(savedInstanceState.getString("ques"));
            button1.setText(savedInstanceState.getString("button1"));
            button2.setText(savedInstanceState.getString("button2"));
            button3.setText(savedInstanceState.getString("button3"));
            timer_textview.setText(savedInstanceState.getString("timerTime"));
            score_textveiw.setText(savedInstanceState.getString("scoreView"));
            last_message.setText(savedInstanceState.getString("lastMsg"));
            score = savedInstanceState.getInt("score");
            quesNumber = savedInstanceState.getInt("quesNumber");
            try {
                time_remaining = Integer.parseInt(savedInstanceState.getString("timerTime"));
            } catch (NumberFormatException e) {
                time_remaining = 0;
            }

        }
        startTimer(time_remaining);
    }

    @Override
    public void onSaveInstanceState(Bundle save) {
        save.putInt("locationOfAnswer", locationOfAnswer);
        save.putString("button0", button0.getText().toString());
        save.putString("button1", button1.getText().toString());
        save.putString("button2", button2.getText().toString());
        save.putString("button3", button3.getText().toString());
        save.putString("ques", ques_textview.getText().toString());
        save.putString("timerTime", timer_textview.getText().toString());
        save.putString("scoreView", score_textveiw.getText().toString());
        save.putString("lastMsg", last_message.getText().toString());
        save.putInt("quesNumber", quesNumber);
        save.putInt("score", score);

        super.onSaveInstanceState(save);
    }


    private void generateQuestions() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                answers.clear();
                Random rand = new Random();
                int a = rand.nextInt(20);
                int b = rand.nextInt(20);
                while (b == 0) {
                    b = rand.nextInt(20);
                }
                String operate = operator[rand.nextInt(4)];
                int solution = 0;
                if (operate.equals("+")) {
                    solution = a + b;
                } else if (operate.equals("-")) {
                    solution = a - b;
                }
                if (operate.equals("/")) {
                    while ((a % b != 0)) {
                        a = rand.nextInt(30);
                        b = rand.nextInt(30);
                        while (b == 0) {
                            b = rand.nextInt(20);
                        }
                    }
                    solution = a / b;
                }
                if (operate.equals("*")) {
                    solution = a * b;
                }
                final String ques = a + " " + operate + " " + b;
                locationOfAnswer = rand.nextInt(4);
                for (int i = 0; i < 4; i++) {
                    if (i == locationOfAnswer) {
                        answers.add(String.valueOf(solution));
                        //answers.set(i,String.valueOf(solution));
                    } else {
                        int x = rand.nextInt(70);
                        while (x == solution) {
                            x = rand.nextInt(70);
                        }
                        answers.add(String.valueOf(x));
                    }
                }
                ques_textview.setText(ques);
                button0.setText(answers.get(0).toString());
                button1.setText(answers.get(1).toString());
                button2.setText(answers.get(2).toString());
                button3.setText(answers.get(3).toString());
            }
        }, 500);
    }

    public void checkAnswer(View v) {
        quesNumber++;
        final int check;
        card = (CardView) v.getParent();
        if (v.getTag().toString().equals(String.valueOf(locationOfAnswer))) {
            result_textview.setText("correct");
            score++;
            check = 0;
        } else {
            result_textview.setText("incorrect");
            check = 1;
        }
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (check == 0) {
//                    card.setCardBackgroundColor(getResources().getColor(R.color.right));
                } else {
                    card.setCardBackgroundColor(getResources().getColor(R.color.wrong));
                }
                for (int i = 0; i < mainLayout.getChildCount(); i++) {
                    CardView child = (CardView) mainLayout.getChildAt(i);
                    View baby = child.getChildAt(0);
                    if (baby.getTag().toString().equals(String.valueOf(locationOfAnswer)))
                        child.setCardBackgroundColor(getResources().getColor(R.color.right));
                    baby.setClickable(false);
                }
            }
        });
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int temp = locationOfAnswer;
                card.setCardBackgroundColor(getResources().getColor(R.color.unclicked));
                for (int i = 0; i < mainLayout.getChildCount(); i++) {
                    CardView child = (CardView) mainLayout.getChildAt(i);
                    View baby = child.getChildAt(0);
                    if (baby.getTag().toString().equals(String.valueOf(temp)))
                        child.setCardBackgroundColor(getResources().getColor(R.color.unclicked));
                    baby.setClickable(true);
                }
            }
        }, 500);
        generateQuestions();
        updateScore();
//            timer.cancel();
//            startTimer();
    }

    private void updateScore() {
        score_textveiw.setText(String.valueOf(score) + "/" + String.valueOf(quesNumber));
    }

    private void startTimer(int time_remaining) {
        this.time_remaining = time_remaining;
        maxProgress = this.time_remaining * 1000;
        timer = new CountDownTimer(maxProgress + 100, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timer_textview.setText(String.valueOf(MainActivity.this.time_remaining));
                ((ProgressBar) findViewById(R.id.progressBarToday)).setProgress(MainActivity.this.time_remaining);
                MainActivity.this.time_remaining--;
            }

            @Override
            public void onFinish() {
                ((ProgressBar) findViewById(R.id.progressBarToday)).setProgress(MainActivity.this.time_remaining);
                timer_textview.setText(String.valueOf("Time Up!"));
                enableButtons(false);
                last_message.setVisibility(View.VISIBLE);
                setScoreOnView();
                playAgainCard.setVisibility(View.VISIBLE);
                shareCard.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    public void playAgain(View v) {
        playAgainCard.setVisibility(View.INVISIBLE);
        shareCard.setVisibility(View.INVISIBLE);
        enableButtons(true);
        quesNumber = 0;
        score_textveiw.setText("0/0");
        last_message.setText("");
        result_textview.setText("");
        ques_textview.setText("");
        button0.setText("");
        button1.setText("");
        button2.setText("");
        button3.setText("");
        score = 0;
        generateQuestions();
        startTimer(getIntent().getIntExtra("time", 1) * 60);
    }

    private void enableButtons(boolean choice) {
        for (
                int i = 0; i < mainLayout.getChildCount(); i++) {
            CardView child = (CardView) mainLayout.getChildAt(i);
            View baby = child.getChildAt(0);
            baby.setClickable(choice);
        }
    }

    private void setScoreOnView() {
        //last_message.setAllCaps(true);
        if (score >= quesNumber / 2) {
            last_message.setTextColor(getResources().getColor(R.color.rightAnswer));
            if (quesNumber == 0)
                last_message.setText("Start playing " + username + "!");
            else if (quesNumber == score)
                last_message.setText("Woahh " + username + "!\nYOUR SCORE: " + String.valueOf(score) + " / " + String.valueOf(quesNumber));
            else
                last_message.setText("nice " + username + "!\nYOUR SCORE: " + String.valueOf(score) + " / " + String.valueOf(quesNumber));

        } else {
            last_message.setTextColor(getResources().getColor(R.color.wrongAnswer));
            last_message.setText("Try again " + username + "!\nYOUR SCORE: " + String.valueOf(score) + " / " + String.valueOf(quesNumber));

        }
    }

    public void shareScore(View v) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String score_text = score + "/" + quesNumber;
        intent.putExtra(Intent.EXTRA_SUBJECT, "QuizU");
        intent.putExtra(Intent.EXTRA_TEXT, "Hey!\n" + username + " scored " + score_text + " in QuizU\nJoin QuizU to compete with " + username);
        startActivity(Intent.createChooser(intent, "Share Using"));
        finish();
    }

}


