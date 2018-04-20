package com.example.qianz.englishcorner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.qianz.englishcorner.customer.MyIncomingHolder;
import com.example.qianz.englishcorner.model.Message;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";

    private MessagesList messageList;
    private MessageInput input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onInitView();
        initAdapter();
    }

    public void onInitView(){
        messageList = (MessagesList) findViewById(R.id.message_list);
        input = (MessageInput) findViewById(R.id.input);
    }

    public void initAdapter(){
//        MessageHolders holders = new MessageHolders().setIncomingTextConfig(MyIncomingHolder.class , )
//        MessagesListAdapter<Message> adapter = new MessagesListAdapter<Message>(0 , )
    }

    private void onInitEvent(){

    }

}
