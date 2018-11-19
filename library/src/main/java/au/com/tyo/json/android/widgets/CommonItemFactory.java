package au.com.tyo.json.android.widgets;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import au.com.tyo.json.android.R;
import au.com.tyo.json.android.interfaces.CommonListener;
import au.com.tyo.json.android.interfaces.FormWidgetFactory;
import au.com.tyo.json.android.interfaces.MetaDataWatcher;
import au.com.tyo.json.android.utils.JsonMetadata;

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

    protected void setViewTags(View v, JSONObject jsonObject) throws JSONException {
        setViewTags(v, new JsonMetadata(jsonObject));
    }

    protected void setViewTags(View v, JsonMetadata metadata) {
        v.setTag(R.id.key, metadata.key);
        v.setTag(R.id.type, metadata.type);
        v.setTag(R.id.required, metadata.required);
    }

    protected ViewGroup createViewContainer(LayoutInflater factory) {
        return (ViewGroup) factory.inflate(R.layout.form_row, null);
    }

    protected View createTitleView(LayoutInflater factory, JSONObject jsonObject, String titleKey) throws JSONException {
        View v = factory.inflate(R.layout.form_title, null);
        bindTitle(v, jsonObject, titleKey);
        return v;
    }

    protected void bindTitle(View parent, JSONObject jsonObject, String titleKey) throws JSONException {
        TextView titletext = (TextView) parent.findViewById(android.R.id.text1);
        // 1st Column
        if (jsonObject.has(titleKey))
            titletext.setText(jsonObject.getString(titleKey));
        else if (jsonObject.has("value"))
            titletext.setText(jsonObject.getString("value"));
        else
            titletext.setVisibility(View.GONE);
    }

    protected void bindUserInput(View parent, JSONObject jsonObject, int gravity, CommonListener listener, boolean editable, MetaDataWatcher metaDataWatcher) throws JSONException {
        View userInputView = parent.findViewById(R.id.user_input);
        String value = jsonObject.getString("value");

        if (userInputView instanceof android.widget.TextView) {
            android.widget.TextView inputTextView = (android.widget.TextView) userInputView;

            if (jsonObject.has("textStyle") && jsonObject.getString("textStyle").equalsIgnoreCase("html"))
                inputTextView.setText(Html.fromHtml(value));
            else
                inputTextView.setText(value);

            inputTextView.setGravity(gravity);
        }
        else if (userInputView instanceof EditText) {
            EditText inputTextView = (EditText) userInputView;
            inputTextView.setText(value);
        }
        else
            throw new IllegalStateException("Unknown user input view type");

        if (null != userInputView) {
            metaDataWatcher.setUserInputView(jsonObject.getString("key"), userInputView, editable, -1);

            if (jsonObject.has("clickable") && jsonObject.getBoolean("clickable")) {
                userInputView.setClickable(true);
                userInputView.setOnClickListener(listener);
            }
        }
    }
}
