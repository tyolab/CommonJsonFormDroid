package au.com.tyo.json.android.customviews;

import android.content.Context;
import android.util.AttributeSet;

import com.rey.material.widget.ImageButton;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 12/1/18.
 */

public class ImageOption extends ImageButton {

    public ImageOption(Context context) {
        super(context);
    }

    public ImageOption(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageOption(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ImageOption(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);

        if (selected) {

        }
        else {

        }
    }
}
