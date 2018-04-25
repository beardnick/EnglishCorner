package com.example.qianz.englishcorner.util;

import android.util.Log;

import com.example.qianz.englishcorner.model.Suggestion;

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

/**
 * 语法查错类
 */
public class LanguageToolUtil {

    public interface OnCheckMessageListener{
        public void onSuccess(ArrayList<Suggestion> suggestions);
    }

    public static final String SEVER_URL = "https://languagetool.org/api/v2/check";
    public static final String ENGLISH = "en-US";
    private static final String TAG = "LanguageToolUtil";
    HttpURLConnection connection;
    private boolean isConnected = false;

    /**
     * 构建发送请求的URL
     * @param language 需要查语法的语言
     * @param text 需要查语法的句子
     * @return 请求的URL
     */
    private String buildURL(String language , String text){
        StringBuilder sb = new StringBuilder("");
        sb.append(buildParamenter("&","language" , language));
        sb.append(buildParamenter("&","text" , text));
        Log.i(TAG, "buildURL: " + sb.toString());
        return sb.toString();
    }

    /**
     * 构建URL中的参数部分
     * @param septator 参数分隔符
     * @param key 参数名
     * @param value 参数值
     * @return 构建好的参数部分
     */
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

    /**
     * 发送查错请求
     * @param message 需要查错的消息
     * @param listener 查错完成后相应操作的回调方法
     */
        public void checkMessage(final String message , final OnCheckMessageListener listener){
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
                        listener.onSuccess(getSuggestions(response.toString()));
                        Log.i(TAG, "Response:" + response);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

    /**
     * 解析返回的Json数据为需要的改错建议对象
     * @param jsonString 服务器返回的Json数据
     * @return 解析出的改错建议对象的ArrayList
     */
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
