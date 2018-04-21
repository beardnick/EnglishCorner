package com.example.qianz.englishcorner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.qianz.englishcorner.custom.MyIncomingHolder;
import com.example.qianz.englishcorner.custom.MyOutComingHolder;
import com.example.qianz.englishcorner.model.Author;
import com.example.qianz.englishcorner.model.Message;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.messages.MessageHolders;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

public class MainActivity extends AppCompatActivity implements MessageInput.InputListener {

    private static final String TAG = "MainActivity";
    private MessagesList messageList;
    private MessageInput input;
    private MessagesListAdapter<Message> adapter;
    private ImageLoader loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onInitView();
        initAdapter();
        onInitEvent();
    }

    public void onInitView(){
        messageList = (MessagesList) findViewById(R.id.message_list);
        input = (MessageInput) findViewById(R.id.input);
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
        adapter = new MessagesListAdapter<>("1", holders, loader);
//        adapter = new MessagesListAdapter<>("1", loader);
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
                        new Author("0" , "Nick" , "http://i.imgur.com/R3Jm1CL.png") , input.toString() , MainActivity.this
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
