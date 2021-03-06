package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SearchActivity extends AppCompatActivity {

    //обьявляем для работы
    TextView cityName;
    Button searchButton;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

    }

    //адрес ЮРЛ и получение результата в Стринг
    class Weather extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... address) {
            try {
                URL url = new URL(address[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                //получаем данные
                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);

                //получаем и возвращаем в Стринг
                int data = isr.read();
                String content = "";
                char ch;
                while (data != -1) {
                    ch = (char) data;
                    content = content + ch;
                    data = isr.read();
                }
                return content;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    public void search(View view) {
        //связываем
        cityName = findViewById(R.id.cityName);
        searchButton = findViewById(R.id.searchButton);
        result = findViewById(R.id.result);

        //введеный город приводим к Стринг
        String cName = cityName.getText().toString();

        //присваимваем обьекту данные и делаем проверку на получение
        String content;
        Weather weather = new Weather();
        try {

            content = weather.execute("https://openweathermap.org/data/2.5/weather?q=" +
                    cName + //вставляем введеное название в ссылку
                    "&appid=b6907d289e10d714a6e88b30761fae22").get();
            Log.i("content", content);

            //JSON
            JSONObject jsonObject = new JSONObject(content);
            String weatherData = jsonObject.getString("weather");
            String mainTemperature = jsonObject.getString("main");
            double visibility;


            Log.i("weatherData", weatherData);
            JSONArray array = new JSONArray(weatherData);


            String main = "";
            String description = "";
            String temperature = "";

            for (int i = 0; i < array.length(); i++) {
                JSONObject weatherPart = array.getJSONObject(i);
                main = weatherPart.getString("main");
                description = weatherPart.getString("description");
            }

            JSONObject mainPart = new JSONObject(mainTemperature);
            temperature = mainPart.getString("temp");

            visibility = Double.parseDouble(jsonObject.getString("visibility"));
            int visibilityInKilometer = (int) visibility / 1000;

            Log.i("main", main);
            Log.i("description", description);
            Log.i("Temperature", temperature);


            //устанавливаем полученный текст
            String resultText = "Состояние : " + main +
                    "\nDescription : " + description +
                    "\nТемпература : " + temperature + "°C" +
                    "\nВидимость : " + visibilityInKilometer + "км";
            result.setText(resultText);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



