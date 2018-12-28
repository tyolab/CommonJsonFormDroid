package au.com.tyo.json.android.widgets;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import au.com.tyo.json.android.R;
import au.com.tyo.json.android.interfaces.CommonListener;
import au.com.tyo.json.android.interfaces.FormWidgetFactory;
import au.com.tyo.json.android.interfaces.JsonApi;
import au.com.tyo.json.android.interfaces.MetaDataWatcher;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 26/7/17.
 */

public abstract class CommonItemFactory extends FormWidgetFactory {

    public CommonItemFactory(String widgetKey) {
        super(widgetKey);
    }

    public CommonItemFactory() {
        super();
    }

    public static String getJsonStringValue(JSONObject jsonObject) {
        String text = null;
        try {
            if (jsonObject.has("value"))
                text = jsonObject.getString("value");
        }
        catch (Exception ex) {}
        return text;
    }

    protected static ViewGroup createViewContainer(LayoutInflater factory) {
        return (ViewGroup) factory.inflate(R.layout.form_row, null);
    }

    protected static View createTitleView(LayoutInflater factory, JSONObject jsonObject, String titleKey) throws JSONException {
        View v = factory.inflate(R.layout.form_title, null);
        bindTitle(v, jsonObject, titleKey);
        return v;
    }

    protected static void bindTitle(View parent, JSONObject jsonObject, String titleKey) throws JSONException {
        TextView titletext = (TextView) parent.findViewById(android.R.id.text1);
        // 1st Column
        if (jsonObject.has(titleKey))
            titletext.setText(jsonObject.getString(titleKey));
        else if (jsonObject.has("value"))
            titletext.setText(jsonObject.getString("value"));
        else
            titletext.setVisibility(View.GONE);
    }

    public static void bindUserInput(JsonApi jsonApi, View userInputView, String keyStr, String value, boolean styled) {

        if (null == value)
            value = jsonApi.getNullValueReplacement(keyStr).toString();

        if (userInputView instanceof android.widget.TextView) {
            android.widget.TextView inputTextView = (android.widget.TextView) userInputView;

            if (styled)
                inputTextView.setText(Html.fromHtml(value));
            else
                inputTextView.setText(value);

        }
        else if (userInputView instanceof EditText) {
            EditText inputTextView = (EditText) userInputView;
            inputTextView.setText(value);
        }
        else if (userInputView instanceof ImageView) {
            ImageView imageView = (ImageView) userInputView;
            jsonApi.loadImage(keyStr, imageView);
        }
        else
            throw new IllegalStateException("Unknown user input view type");

    }

    public static void bindUserInput(JsonApi jsonApi, View parent, JSONObject jsonObject, int gravity, CommonListener listener, boolean editable, MetaDataWatcher metaDataWatcher) throws JSONException {
        View userInputView = parent.findViewById(R.id.user_input);
        final String value = getJsonStringValue(jsonObject);
        final String keyStr = jsonObject.getString("key");

        bindUserInput(jsonApi, userInputView, keyStr, value, jsonObject.has("textStyle") && jsonObject.getString("textStyle").equalsIgnoreCase("html"));

        // inputTextView.setGravity(gravity);
        if (null != userInputView) {
            metaDataWatcher.setUserInputView(keyStr, userInputView, editable, -1);

            if (jsonObject.has("clickable") && jsonObject.getBoolean("clickable")) {
                userInputView.setClickable(true);
                userInputView.setOnClickListener(listener);
            }
        }
    }
}
