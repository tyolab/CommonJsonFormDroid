package au.com.tyo.json.android.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import google.json.JSONException;
import google.json.JSONObject;

import au.com.tyo.android.AndroidUtils;
import au.com.tyo.json.android.R;

/**
 * Created by vijay on 24-05-2015.
 */
public class FormUtils {
    public static final String FONT_BOLD_PATH = "font/Roboto-Bold.ttf";
    public static final String FONT_REGULAR_PATH = "font/Roboto-Regular.ttf";
    public static final int MATCH_PARENT = -1;
    public static final int WRAP_CONTENT = -2;

    public static LinearLayout.LayoutParams getLayoutParams(int width, int height, int left, int top, int right, int bottom) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
        layoutParams.setMargins(left, top, right, bottom);
        return layoutParams;
    }

    public static TextView getTextViewWith(Context context, int textSizeInSp, String text, String key, String type,
                                           LinearLayout.LayoutParams layoutParams, String fontPath) {
        TextView textView = new TextView(context);
        textView.setText(text);
        textView.setTag(R.id.key, key);
        textView.setTag(R.id.type, type);
        textView.setId(AndroidUtils.generateViewId());
        textView.setTextSize(textSizeInSp);
        textView.setLayoutParams(layoutParams);
        textView.setTypeface(Typeface.createFromAsset(context.getAssets(), fontPath));
        return textView;
    }

    public static int dpToPixels(Context context, float dps) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dps * scale + 0.5f);
    }

    public static void setKeyTypeTags(View v, JSONObject jsonObject) throws JSONException {
        v.setTag(R.id.key, jsonObject.getString("key"));
        v.setTag(R.id.type, jsonObject.getString("type"));
        v.setTag(R.id.required, jsonObject.getInt("required"));
    }

    public static void formatView(View v, String key, String type) {
        v.setTag(R.id.key, key);
        v.setTag(R.id.type, type);
    }
}
