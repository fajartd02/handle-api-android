package com.example.handleapi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private TextView tvShow;
    private Button btnRefresh;

    private String apiUrl = "https://genshin-news-scraper.alviannn.repl.co/news";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initListener();
        getDataHome();
    }

    private void getDataHome() {
        getHomeAsyncTask getHomeAsyncTask = new getHomeAsyncTask();
        getHomeAsyncTask.execute();
    }

    private void initListener() {
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDataHome();
            }
        });
    }

    private void initView() {
        tvShow = findViewById(R.id.tv_show);
        btnRefresh = findViewById(R.id.btn_refresh);
    }

    public class getHomeAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please Waitt.....");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String current = "";
            try {
                URL url;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL(apiUrl);
                    urlConnection = (HttpURLConnection) url.openConnection();
//                    urlConnection.setRequestMethod("GET");

                    InputStream in = urlConnection.getInputStream();
                    Scanner scan = new Scanner(in);
//                    InputStreamReader isw = new InputStreamReader(in);

//                    int data = isw.read();
//                    while(data != -1) {
//                        current += (char) data;
//                        System.out.println(current);
//                        System.out.println("\n");
//                        data = isw.read();
//                    }

                    while (scan.hasNextLine()) {
                        current += scan.nextLine();
                        System.out.println(current + "\n");
                    }

                    return current;

                } catch (Exception e) {
                    e.printStackTrace();

                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                return "Exception : " + e.getMessage();
            }

            return current;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();

            try {
//                JSONArray jsonArray = new JSONArray(s);
//                JSONObject object = jsonArray.getJSONObject(0);
                JSONObject object = new JSONObject(s);
                JSONArray news = object.getJSONArray("news");
                int len = news.length();
                String showData = "";
                for (int i = 0; i < len; i++) {
                    showData += (i + 1) + " - Tanggal Genshin: " + news.getJSONObject(i).getString("date");
                    showData += "\n";
                }

                tvShow.setText(showData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}