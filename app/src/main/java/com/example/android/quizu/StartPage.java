package com.example.android.quizu;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class StartPage extends AppCompatActivity {
    EditText nameView;
    EditText timeView;
    TextInputLayout timerLayout;
    EditText user_ques_view;
    TextInputLayout user_ques_layout;

    //    ListView options_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
//        options_list=findViewById(R.id.options_list);
        nameView = findViewById(R.id.name);
        timeView = findViewById(R.id.time);
        user_ques_layout = findViewById(R.id.user_ques_layout);
        timerLayout = findViewById(R.id.timer);
        user_ques_view = findViewById(R.id.user_ques);
        timerLayout.setErrorEnabled(true);
        timerLayout.setError("(in minutes)");
        user_ques_layout.setErrorEnabled(true);
        user_ques_layout.setError("less than 50");
        floodList();
    }

    private void floodList() {
        ArrayList<String> array = new ArrayList<>();
        array.add("Mixed Questions");
//        ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,array);
//        options_list.setAdapter(arrayAdapter);
//        options_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if(position==0){
//                    Intent intent=new Intent(StartPage.this,OnlineQuizActivity.class);
//                    startActivity(intent);
//                }
//
//
//            }
//        });

    }

    public void loadGeneralQuestion(View v) {
        EditText name_general_view = findViewById(R.id.name_general_ques);
        String name_general = name_general_view.getText().toString();
        if (name_general.equals("")) {
            name_general = "Guest";
        }
        Intent intent = new Intent(StartPage.this, OnlineQuizActivity.class);
        intent.putExtra("name", name_general);
        try {
            intent.putExtra("user_ques", Integer.parseInt(user_ques_view.getText().toString()));
        } catch (Exception e) {
            Snackbar.make(findViewById(R.id.activity_start_page_layout), "Enter a valid number", Snackbar.LENGTH_SHORT).show();
        }
        startActivity(intent);
    }

    public void startMainApp(View v) {
        String name;
        int time;
        if (nameView.getText().toString().equals("")) {
            name = "Guest";
        } else {
            name = nameView.getText().toString();
        }
        try {
            time = Integer.parseInt(timeView.getText().toString());
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("time", time);
            startActivity(intent);
        } catch (Exception e) {
            Snackbar.make(findViewById(R.id.activity_start_page_layout), "Enter a valid time", Snackbar.LENGTH_SHORT).show();
        }
    }
}