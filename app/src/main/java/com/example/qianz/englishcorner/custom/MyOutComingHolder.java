package com.example.qianz.englishcorner.custom;

import android.text.SpannableString;
import android.util.Log;
import android.view.View;

import com.example.qianz.englishcorner.model.Message;
import com.example.qianz.englishcorner.util.LanguageToolUtil;
import com.stfalcon.chatkit.messages.MessageHolders;

/**
 * Created by qianz on 2018/4/21.
 */

public class MyOutComingHolder extends MessageHolders.OutcomingTextMessageViewHolder<Message>{

    private static final String TAG = "MyOutComingHolder";
    public MyOutComingHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onBind(Message message) {
        super.onBind(message);
        if(message.getText().length() > 0){
            Log.i(TAG, "onBind: " + message.getText());
            LanguageToolUtil checker = new LanguageToolUtil(message.getText() , new LanguageToolUtil.OnCheckMessageListener() {
                @Override
                public void onSuccess(SpannableString span) {
                    text.setText(span);
                }
            });
            checker.checkMessage();
        }
    }
}
