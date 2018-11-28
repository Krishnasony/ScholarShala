package com.appteq.ad.appteq;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class TestActivity extends AppCompatActivity {
    CountDownTimer countDownTimer;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        button = findViewById(R.id.button_timer);
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                button.setText("Test Starts In :"+ String.valueOf((int) millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                button.setText("No Test Availabe");
                Toast.makeText(TestActivity.this, "Timer Finish", Toast.LENGTH_SHORT).show();
            }
        }.start();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TestActivity.this,HomeActivity.class));
            }
        });
    }
}
