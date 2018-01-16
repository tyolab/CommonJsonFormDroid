package au.com.tyo.json.android.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import au.com.tyo.json.android.R;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 15/1/18.
 */

public class OptionalButton extends RelativeLayout {

    private View imageClear;

    private View imageGo;

    public OptionalButton(@NonNull Context context) {
        super(context);
    }

    public OptionalButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public OptionalButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public OptionalButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        imageClear = findViewById(R.id.btn_clear);
        imageGo = findViewById(R.id.btn_go);
    }

    public void showClearButton() {
        imageClear.setVisibility(VISIBLE);
        imageGo.setVisibility(GONE);
    }

    public void hideClearButton() {
        imageGo.setVisibility(VISIBLE);
        imageClear.setVisibility(GONE);
    }

    public View getClearButton() {
        return imageClear;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}
