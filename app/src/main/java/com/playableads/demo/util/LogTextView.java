package com.playableads.demo.util;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;

/**
 * Description:
 * <p>
 * Created by lgd on 2018/9/6.
 */

public class LogTextView extends android.support.v7.widget.AppCompatTextView {

    public LogTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setTextColor(0x88ff0000);
    }

    public <T> void addLog(T message) {
        String msg = String.valueOf(message);
        String lastMsg = getText().toString();
        if (TextUtils.isEmpty(lastMsg)) {
            setText(msg);
        } else {
            setText(String.format("%s\n%s", lastMsg, msg));
        }
    }

    public void clearLog() {
        setText("");
    }
}
