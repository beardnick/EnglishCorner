package com.example.qianz.englishcorner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.qianz.englishcorner.custom.MyIncomingHolder;
import com.example.qianz.englishcorner.custom.MyOutComingHolder;
import com.example.qianz.englishcorner.model.Author;
import com.example.qianz.englishcorner.model.Message;
import com.example.qianz.englishcorner.util.MessageHandler;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class MainActivity extends AppCompatActivity implements MessageInput.InputListener {

    private static final String TAG = "MainActivity";
    private MessagesList messageList;
    private MessageInput input;
    private MessagesListAdapter<Message> adapter;
    private ImageLoader loader;
    private Author user;
    private Author friend;
    private final BmobIM bmobIM = BmobIM.getInstance();
    private BmobIMConversation manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onInitView();
        oninitData();
        initAdapter();
        onInitEvent();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        BmobIM bmobIM = BmobIM.getInstance();
//        BmobIMConversation conversation = bmobIM.startPrivateConversation(bmobIM.getUserInfo(friend.getObjectId()) , false ,null);
//        BmobIMConversation manager = BmobIMConversation.obtain(BmobIMClient.getInstance() , conversation);
//        manager.getConversationIcon();

    }

    public void onInitView(){
        messageList = (MessagesList) findViewById(R.id.message_list);
        input = (MessageInput) findViewById(R.id.input);
    }

    public void oninitData(){
        Intent intent = getIntent();
        BmobQuery<Author> query = new BmobQuery<>();
        query.getObject(intent.getStringExtra("objectid"), new QueryListener<Author>() {
            @Override
            public void done(Author author, BmobException e) {
                if(e == null){
                    friend = author;
                    BmobIMConversation conversation = bmobIM.startPrivateConversation(bmobIM.getUserInfo(friend.getObjectId()) , false , null);
                    manager = BmobIMConversation.obtain(BmobIMClient.getInstance() , conversation);
                }else {
                    Log.i(TAG, "done: " + e.getMessage());
                }
            }
        });
        user = BmobUser.getCurrentUser(Author.class);
        if(user.getObjectId().length() > 0){
            BmobIM.connect(user.getObjectId(), new ConnectListener() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        Log.i(TAG, "done: 连接成功");
                    }else {
                        Log.i(TAG, "done: " + e.getMessage());
                    }
                }
            });
        }
    }

    public void initAdapter(){
        MessageHolders holders = new MessageHolders()
                .setIncomingTextHolder(MyIncomingHolder.class)
                .setOutcomingTextHolder(MyOutComingHolder.class);
        loader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url) {
                Picasso.with(MainActivity.this).load(url).into(imageView);
            }
        };
        adapter = new MessagesListAdapter<>(user.getId(), holders, loader);
        messageList.setAdapter(adapter);
    }
    private void onInitEvent(){
        input.setInputListener(this);
    }

    @Override
    public boolean onSubmit(CharSequence input) {
        Log.i(TAG, "onSubmit: " + input.toString());
        adapter.addToStart(
                new Message(
                        user , input.toString() , MainActivity.this
                ) ,
                true
        );
        return true;
    }

//    @Override
//    public void onLoadMore(int page, int totalItemsCount) {
//        Log.i(TAG, "onLoadMore: use this methond");
//    }
}
