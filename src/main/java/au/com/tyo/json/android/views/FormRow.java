package au.com.tyo.json.android.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 9/11/17.
 */

public class FormRow extends RelativeLayout {

    public FormRow(@NonNull Context context) {
        super(context);
    }

    public FormRow(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FormRow(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FormRow(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr, @StyleRes int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

}
