package com.example.android.quizu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {
    TextView resultView;
    Button btn;
    String score_text;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        resultView=findViewById(R.id.final_result);
        Intent intent=getIntent();
        int score=intent.getIntExtra("SCORE",0);
        int ques=intent.getIntExtra("TOTAL_QUES",0);
        username=intent.getStringExtra("username");
        score_text = score + "/" + ques;
        if(score<ques+4 && ques>8)
        resultView.setText("Hi "+username+"\n Your score : \n "+score_text+"\n"+"Try Again!");
        else
        resultView.setText("Cheers! "+username+"\n"+"Your score : \n "+score_text);
    }
    public void shareScore(View v){
        btn=(Button)v;
        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT,"QuizU");
        intent.putExtra(Intent.EXTRA_TEXT,"Hey!\n"+username+" scored "+score_text+ " in QuizU\nJoin QuizU to compete with "+username);
        startActivity(Intent.createChooser(intent,"Share Using"));
    }
}