package com.example.qianz.englishcorner;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMTextMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.newim.listener.MessageListHandler;
import cn.bmob.newim.listener.MessagesQueryListener;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class MainActivity extends AppCompatActivity implements MessageInput.InputListener , MessageListHandler {

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
        user = BmobUser.getCurrentUser(Author.class);
        Intent intent = getIntent();
        BmobQuery<Author> query = new BmobQuery<>();
        Log.i(TAG, "friend id : " + intent.getStringExtra("objectid"));
        String objectid = intent.getStringExtra("objectid");
        query.getObject(objectid, new QueryListener<Author>() {
            @Override
            public void done(Author author, BmobException e) {
                if(e == null){
                    friend = author;
//                    adapter.addToStart(new Message(friend , "hello" , MainActivity.this) , true);
                    bmobIM.getUserInfo(friend.getObjectId());
                    BmobIMConversation conversation = bmobIM.startPrivateConversation(
                            BmobIM.getInstance().getUserInfo(friend.getObjectId()),
                            false,
                            new ConversationListener() {
                        @Override
                        public void done(BmobIMConversation bmobIMConversation, BmobException e) {
                            if(e == null){
                                Log.i(TAG, "会话连接成功");
                            }else {
                                Log.i(TAG, "会话连接失败" + e.getMessage());
                            }
                        }
                    });
                    manager = BmobIMConversation.obtain(BmobIMClient.getInstance() , conversation);
                    manager.queryMessages(null, 10, new MessagesQueryListener() {
                        @Override
                        public void done(List<BmobIMMessage> list, BmobException e) {
                            if(e == null){
                                ArrayList<Message> msgs = new ArrayList<>();
                                for (BmobIMMessage msg: list
                                     ) {
                                    if(msg.getBmobIMUserInfo().getUserId().equals(friend.getObjectId())){
                                        msgs.add(new Message(friend , msg.getContent() , new Date(msg.getCreateTime()) ,  MainActivity.this));
                                    }else {
                                        msgs.add(new Message(user , msg.getContent() , new Date(msg.getCreateTime()) ,  MainActivity.this));
                                    }
                                }
                                adapter.addToEnd(msgs , true);
                            }else {
                                Log.i(TAG, "query to build message conversation : " + e.getMessage());
                            }
                        }
                    });
                }else {
                    Log.i(TAG, "query for friend : " + e.getMessage());
                }
            }
        });
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
    public boolean onSubmit(final CharSequence input) {
        Log.i(TAG, "onSubmit: " + input.toString());
        //判断不能含有中文
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(input.toString());
        if(! m.find()) {
            adapter.addToStart(
                    new Message(
                            user, input.toString(), MainActivity.this
                    ),
                    true
            );
            new Thread(new Runnable() {
                @Override
                public void run() {
                    BmobIMTextMessage imTextMessage = new BmobIMTextMessage();
                    imTextMessage.setContent(input.toString());
                    manager.sendMessage(imTextMessage);
                }
            }).start();
        }else{
            Toast.makeText(MainActivity.this, "Chinese is not allowed", Toast.LENGTH_SHORT).show();
        }
        return true;
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        bmobIM.addMessageListHandler(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        bmobIM.removeMessageListHandler(this);
    }

    @Override
    public void onMessageReceive(List<MessageEvent> list) {
        for (MessageEvent event: list
             ) {
            adapter.addToStart(
                    new Message(
                    friend , event.getMessage().getContent()
                            , MainActivity.this)
            , true);
        }
    }

//    @Override
//    public void onLoadMore(int page, int totalItemsCount) {
//        Log.i(TAG, "onLoadMore: use this methond");
//    }
}
