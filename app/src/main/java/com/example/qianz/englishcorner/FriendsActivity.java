package com.example.qianz.englishcorner;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qianz.englishcorner.model.MyDialog;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

import java.lang.reflect.Type;

import cn.bmob.v3.BmobUser;

public class FriendsActivity extends AppCompatActivity {

    DialogsList dialogsList;
    DialogsListAdapter<MyDialog> dialogAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        onBindView();
        initAdapter();
    }

    public void onBindView(){
        dialogsList = (DialogsList) findViewById(R.id.dialog_list);


    }

    public void initAdapter(){
        dialogAdapter = new DialogsListAdapter<MyDialog>(new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url) {
                Picasso.with(FriendsActivity.this).load(url).into(imageView);
            }
        });
        dialogsList.setAdapter(dialogAdapter);
    }
}
