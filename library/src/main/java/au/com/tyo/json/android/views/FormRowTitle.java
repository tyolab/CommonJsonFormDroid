package au.com.tyo.json.android.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 9/11/17.
 */

public class FormRowTitle extends FrameLayout {

    private TextView tvTitle;

    public FormRowTitle(@NonNull Context context) {
        super(context);
    }

    public FormRowTitle(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FormRowTitle(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FormRowTitle(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tvTitle = (TextView) findViewById(android.R.id.text1);
    }

    public void setTitleTextColor(ColorStateList colors) {
        tvTitle.setTextColor(colors);
    }

    public void setTitleTextColor(int color) {
        tvTitle.setTextColor(color);
    }
}
