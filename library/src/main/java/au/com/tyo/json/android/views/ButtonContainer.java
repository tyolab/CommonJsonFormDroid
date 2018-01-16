package au.com.tyo.json.android.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import au.com.tyo.json.android.R;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 7/12/17.
 */

public class ButtonContainer extends FrameLayout {

    private OptionalButton btnClearable;

    public ButtonContainer(@NonNull Context context) {
        super(context);
    }

    public ButtonContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ButtonContainer(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ButtonContainer(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        btnClearable = findViewById(R.id.btn_clearable);
    }

    public OptionalButton getClearableButton() {
        return btnClearable;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}
