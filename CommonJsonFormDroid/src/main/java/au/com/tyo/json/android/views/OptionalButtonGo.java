package au.com.tyo.json.android.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import au.com.tyo.json.android.R;

public class OptionalButtonGo extends OptionalButton {

    private View imageGo;

    public OptionalButtonGo(@NonNull Context context) {
        super(context);
    }

    public OptionalButtonGo(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public OptionalButtonGo(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public OptionalButtonGo(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        imageGo = findViewById(R.id.btn_go);
    }

    public void showClearButton() {
        super.showClearButton();

        imageGo.setVisibility(GONE);
    }

    public void hideClearButton() {
        super.hideClearButton();

        imageGo.setVisibility(VISIBLE);
    }
}
