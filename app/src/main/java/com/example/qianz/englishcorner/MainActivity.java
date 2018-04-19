package com.example.qianz.englishcorner;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    private TextView text;
    private EditText myText;
    private Button ok;

    public static final String SEVER_URL = "https://languagetool.org/api/v2/check";
    public static final String ENGLISH = "en-US";
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onInitView();
        onInitEvent();
    }

    public void onInitView(){
        text = (TextView) findViewById(R.id.text);
        myText = (EditText) findViewById(R.id.my_text);
        ok = (Button) findViewById(R.id.ok);

    }

    private void onInitEvent(){
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Request request = new Request(text , myText);
                request.execute();
            }
        });
    }
    private String buildURL(String language , String text){
       StringBuilder sb = new StringBuilder("");
       sb.append(buildParamenter("&","language" , language));
       sb.append(buildParamenter("&","text" , text));
       return sb.toString();
    }

    private String buildParamenter(String septator , String key , String value){
        StringBuilder sb = new StringBuilder("");
        sb.append(septator);
        sb.append(key);
        sb.append("=");
        try {
            sb.append(URLEncoder.encode(value , "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

     class Request extends AsyncTask {

         private TextView text;
         private EditText myText;
         String checkText;

         public Request(TextView text, EditText mytext) {
             this.text = text;
             this.myText = mytext;
         }

         @Override
         protected void onPreExecute() {
             if (myText.getText().length() > 0) {
                 text.setText(myText.getText());
                 checkText = String.valueOf(myText.getText());
                 myText.setText("");
             }
         }



         @Override
         protected Object doInBackground(Object[] objects) {
             HttpURLConnection connection = null;
             try {
                 StringBuilder urlBuilder = new StringBuilder("");
                 urlBuilder.append(SEVER_URL);
                 urlBuilder.append(buildParamenter("?" , "sessionID" , "12352"));
                 URL url = new URL(urlBuilder.toString());
                 connection = (HttpURLConnection) url.openConnection();
                 connection.setRequestMethod("POST");
                 connection.setConnectTimeout(8000);
                 DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                 outputStream.writeBytes(buildURL("en", String.valueOf(text.getText())));
                 outputStream.flush();
                 outputStream.close();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(
                         connection.getInputStream()
                 ));
                 StringBuilder response = new StringBuilder("");
                 String line;
                 while ((line = reader.readLine()) != null) {
                     response.append(line);
                 }
                 Log.i(TAG, "Response:" + response);
             } catch (MalformedURLException e) {
                 e.printStackTrace();
             } catch (IOException e) {
                 e.printStackTrace();
             }
             return true;
         }
     }
}
