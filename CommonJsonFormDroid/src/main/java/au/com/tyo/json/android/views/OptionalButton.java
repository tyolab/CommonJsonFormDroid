package au.com.tyo.json.android.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import au.com.tyo.json.android.R;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 15/1/18.
 */

public class OptionalButton extends RelativeLayout {

    public static final int OPEATION_CLEAR = 0;

    public static final int OPEATION_GO = 1;

    private View imageClear;

    private int op;

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
    }

    public int getOp() {
        return op;
    }

    public void setOp(int op) {
        this.op = op;
    }

    public void showClearButton() {
        setOp(OPEATION_CLEAR);
        imageClear.setVisibility(VISIBLE);
    }

    public void hideClearButton() {
        setOp(OPEATION_GO);
        imageClear.setVisibility(GONE);
    }

    public View getClearButton() {
        return imageClear;
    }

    public boolean isOpClear() {
        return op == OPEATION_CLEAR;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}
