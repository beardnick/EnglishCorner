package com.example.qianz.englishcorner.util;

import android.text.SpannableString;
import android.util.Log;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by qianz on 2018/4/20.
 */

public class LanguageToolUtil {

    public interface OnCheckMessageListener{
        public void onSuccess(SpannableString span);
    }

    public static final String SEVER_URL = "https://languagetool.org/api/v2/check";
    public static final String ENGLISH = "en-US";
    private static final String TAG = "LanguageToolUtil";
    private OnCheckMessageListener listener;
    private String message;

    public LanguageToolUtil(String message , OnCheckMessageListener listener) {
        this.listener = listener;
        this.message = message;
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

        public void checkMessage(){
            new Thread(new Runnable() {
                @Override
                public void run() {
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
                        outputStream.writeBytes(buildURL("en", String.valueOf(message)));
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
                        SpannableString span = new SpannableString(message);
                        listener.onSuccess(span);
                        Log.i(TAG, "Response:" + response);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
}
