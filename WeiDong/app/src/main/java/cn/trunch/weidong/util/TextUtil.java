package cn.trunch.weidong.util;

import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.widget.TextView;

public class TextUtil {
    public static void setBold(@NonNull TextView textView) {
        TextPaint paint = textView.getPaint();
        paint.setFakeBoldText(true);
    }
}
