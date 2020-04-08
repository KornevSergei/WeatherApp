package com.example.weatherapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Thread thread = new Thread() {
            //запускаем поток
            @Override
            public void run() {
                try {
                    sleep(3000);//время показа заставки
                } catch (Exception e) {
                    e.printStackTrace();
                } finally { //запускаем активити после заставки
                    startActivity(new Intent(
                            StartActivity.this,
                            MainActivity.class));
                }
            }
        };
        //после страта переключаемся в новую активти
        thread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
