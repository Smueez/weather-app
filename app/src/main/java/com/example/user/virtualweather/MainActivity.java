package com.example.user.virtualweather;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    public class BrowserAsync extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... params) {

            String result = "";
            URL url;
            HttpURLConnection http = null;
            try {
                url = new URL(params[0]);
                http = (HttpURLConnection) url.openConnection();
                InputStream input = http.getInputStream();
                InputStreamReader reader = new InputStreamReader(input);
                int data = reader.read();
                while (data != -1) {
                    char ch = (char) data;
                    result += ch;
                    data = reader.read();
                }
                return result;

            } catch (Exception e) {
                e.printStackTrace();
                return "Failed";
            }


        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            TextView ans = (TextView) findViewById(R.id.textView2);
            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            Log.d("s string", result);
            try {
                String mssg = null;
                JSONObject jsonObject = new JSONObject(result);

                String weatherInfo = jsonObject.getString("weather");

                Log.i("Weather content", weatherInfo);

                JSONArray arr = new JSONArray(weatherInfo);

                for (int i = 0; i < arr.length(); i++) {

                    JSONObject jsonPart = arr.getJSONObject(i);
                    String main= "";
                    String description="";

                    main = jsonPart.getString("main");
                    description = jsonPart.getString("description");

                    Log.i("main", jsonPart.getString("main"));
                    Log.i("description", jsonPart.getString("description"));
                    if(main!=" "  && description!=" "){
                        mssg += main + ":  "+description+"\r\n";
                    }

                }
                if(mssg!=""){
                    ans.setText(mssg);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


    public void click(View view) throws ExecutionException, InterruptedException {
        EditText text = (EditText) findViewById(R.id.editText);
        Log.d("Button", "done");
        BrowserAsync task = new BrowserAsync();
        task.execute("api.openweathermap.org/data/2.5/weather?q="+text.getText().toString()).get();



    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
