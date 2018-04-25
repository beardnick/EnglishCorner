package com.example.qianz.englishcorner.util;

import android.text.SpannableString;
import android.text.Spanned;
import android.util.Log;
import android.view.View;

import com.example.qianz.englishcorner.custom.RoundBackgroundColorSpan;
import com.example.qianz.englishcorner.custom.SuggestionSpan;
import com.example.qianz.englishcorner.model.Message;
import com.example.qianz.englishcorner.model.Suggestion;
import com.stfalcon.chatkit.messages.MessageHolders;

import java.util.ArrayList;

/**
 * Created by qianz on 2018/4/21.
 */

/**
 * 发送信息的处理器，用于在消息列表中显示消息
 */
public class MyOutComingHolder extends MessageHolders.OutcomingTextMessageViewHolder<Message>{
    private static final String TAG = "MyOutComingHolder";
    private LanguageToolUtil checker;
    public MyOutComingHolder(View itemView) {
        super(itemView);
        checker = new LanguageToolUtil();

    }

    /**
     * 将消息和UI组件绑定
     * @param message 发送的消息
     */
    @Override
    public void onBind(final Message message) {
        super.onBind(message);
        if(message.getText().length() > 0){
            Log.i(TAG, "onBind: " + message.getText());
            checker.checkMessage(message.getText() , new LanguageToolUtil.OnCheckMessageListener() {
                @Override
                public void onSuccess(final ArrayList<Suggestion> suggestions) {
                    message.getContext().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SpannableString span = new SpannableString(message.getText());
                            for (Suggestion s:suggestions
                                    ) {
                                span.setSpan(new RoundBackgroundColorSpan() ,
                                        s.getBeg() , s.getEnd() ,
                                        Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                                span.setSpan(new SuggestionSpan(message.getContext() , s) , s.getBeg() , s.getEnd() , Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                            }
                            text.setText(span);
                        }
                    });
                }
            });
        }
    }
}
