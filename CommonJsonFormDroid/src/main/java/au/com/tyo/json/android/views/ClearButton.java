package au.com.tyo.json.android.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 16/1/18.
 */

public class ClearButton extends RelativeLayout {

    public ClearButton(Context context) {
        super(context);
    }

    public ClearButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClearButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ClearButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

}
