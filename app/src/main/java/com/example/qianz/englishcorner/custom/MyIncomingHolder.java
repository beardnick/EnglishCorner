package com.example.qianz.englishcorner.custom;

import android.support.annotation.UiThread;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;

import com.example.qianz.englishcorner.model.Message;
import com.example.qianz.englishcorner.util.LanguageToolUtil;
import com.stfalcon.chatkit.messages.MessageHolders;

/**
 * Created by qianz on 2018/4/20.
 */

public class MyIncomingHolder extends MessageHolders.IncomingTextMessageViewHolder<Message> {

    private static final String TAG = "MyIncomingHolder";
    private LanguageToolUtil checker = new LanguageToolUtil();
    public MyIncomingHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onBind(final Message message) {
        super.onBind(message);
        if(message.getText().length() > 0){
            Log.i(TAG, "onBind: " + message.getText());
             checker.checkMessage(message.getText() , new LanguageToolUtil.OnCheckMessageListener() {
                @Override
                public void onSuccess(final SpannableString span) {
                    message.getContext().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            text.setText(span);
                        }
                    });
                }
            });
        }
    }
}
