package com.example.michaelg.myapplication.Trivia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.michaelg.myapplication.R;
import com.example.michaelg.myapplication.User;

public class MainActivity extends AppCompatActivity {
    User myuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.trivia_activity_main);

        /*Intent intent = getIntent();
        //userType = intent.getIntExtra("Type",0);
        // myuser =(User) intent.getSerializableExtra("user");
         myuser = new User();
        myuser.setuserid("dror@d");
       ImageButton bt= (ImageButton) findViewById(R.id.imageButton2);*/

       /* bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutTrivia();
            }
        });*/

    }
/*
    public void aboutTrivia(){
        Intent intent= new Intent(getApplicationContext(), About.class);
        startActivity(intent);
    }

    public void start(View view) {

        //Intent intent = new Intent(getApplicationContext(), StartQuiz.class);
        Intent intent = new Intent(getApplicationContext(), triviaListview.class);
        startActivity(intent);
    }

    public void create(View view) {
        Intent intent = new Intent(getApplicationContext(), AddQuestion.class);
        startActivity(intent);
    }

    public void leaderboard(View view ) {
        //Toast.makeText(getApplicationContext(), "score:"  , Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), Leader.class);
        intent.putExtra("user",myuser);
        startActivity(intent);
    }*/
}

