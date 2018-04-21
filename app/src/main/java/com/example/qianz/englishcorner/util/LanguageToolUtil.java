package com.example.qianz.englishcorner.util;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.SubscriptSpan;
import android.util.Log;

import com.example.qianz.englishcorner.model.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

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
    HttpURLConnection connection;
    private boolean isConnected = false;


    public LanguageToolUtil() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i(TAG, "try to connect languagetool");
                    StringBuilder urlBuilder = new StringBuilder("");
                    urlBuilder.append(SEVER_URL);
                    urlBuilder.append(buildParamenter("?", "sessionID", "12352"));
                    URL url = new URL(urlBuilder.toString());
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(8000);
                }catch (MalformedURLException e){
                    isConnected = false;
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                    isConnected = false;
                }
                isConnected = true;
            }
        }).start();
    }

    private String buildURL(String language , String text){
        StringBuilder sb = new StringBuilder("");
        sb.append(buildParamenter("&","language" , language));
        sb.append(buildParamenter("&","text" , text));
        Log.i(TAG, "buildURL: " + sb.toString());
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

        public void checkMessage(final String message , final OnCheckMessageListener listener){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while(! isConnected){
                        }
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
                        ArrayList<Suggestion> suggestions = getSuggestions(response.toString());
                        SpannableString span = new SpannableString(message);
                        for (Suggestion s:suggestions
                             ) {
                            span.setSpan(new RoundBackgroundColorSpan() ,
                                    s.getBeg() , s.getEnd() ,
                                    Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                        }
                        listener.onSuccess(span);
                        Log.i(TAG, "Response:" + response);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        public ArrayList<Suggestion> getSuggestions(String jsonString){
            Log.i(TAG, "jsonString: " + jsonString);
            ArrayList<Suggestion> suggestions = new ArrayList<>();
            JSONObject json = null;
            JSONArray matches = null;
            try {
                json = new JSONObject(jsonString);
                matches = json.getJSONArray("matches");
                for (int i = 0; i < matches.length(); i++) {
                    JSONObject matche = matches.getJSONObject(i);
                    Suggestion s = new Suggestion(
                            matche.getInt("offset"),
                            matche.getInt("length") + matche.getInt("offset"),
                            matche.getString("message")
                    );
                    JSONArray replacement = matche.getJSONArray("replacements");
                    for (int r = 0; r < replacement.length(); r++) {
                        s.addReplacement(replacement.getJSONObject(r).getString("value"));
                    }
                    suggestions.add(s);
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
            for (Suggestion s: suggestions
                 ) {
                Log.i(TAG, "getSuggestions:" +
                " start : " + s.getBeg() +
                " end : "  + s.getEnd() +
                " message : " + s.getErro());
            }
            return suggestions;
        }
}
