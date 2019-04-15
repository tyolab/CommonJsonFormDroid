package au.com.tyo.json.android.widgets;

import android.content.Context;
import android.content.res.ColorStateList;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import au.com.tyo.json.android.R;
import au.com.tyo.json.android.interfaces.CommonListener;
import au.com.tyo.json.android.interfaces.FormWidgetFactory;
import au.com.tyo.json.android.interfaces.JsonApi;
import au.com.tyo.json.android.interfaces.MetaDataWatcher;
import au.com.tyo.json.android.utils.JsonMetadata;
import au.com.tyo.json.jsonform.JsonFormField;

import static au.com.tyo.json.jsonform.JsonFormField.CLICKABLE_FIELD;
import static au.com.tyo.json.jsonform.JsonFormField.CLICKABLE_NONE;
import static au.com.tyo.json.jsonform.JsonFormField.CLICKABLE_ROW;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 26/7/17.
 */

public abstract class CommonItemFactory extends FormWidgetFactory {
    
    private int layoutResourceId = -1;

    public CommonItemFactory(String widgetKey) {
        super(widgetKey);
    }

    public CommonItemFactory() {
        super();
    }

    public int getLayoutResourceId() {
        return layoutResourceId;
    }

    /**
     *
     * @param layoutResourceId
     */
    public void setLayoutResourceId(int layoutResourceId) {
        this.layoutResourceId = layoutResourceId;
    }

    /**
     *
     * @param jsonObject
     * @param factory
     * @param layoutFallback
     * @return
     */
    protected static View inflateViewForField(JSONObject jsonObject, LayoutInflater factory, int layoutFallback) {
        return inflateViewForField(jsonObject, factory, layoutFallback, false);
    }

    /**
     *
     * @param jsonObject
     * @param factory
     * @param layoutFallback
     * @param mustHaveUserInputId
     * @return
     */
    protected static View inflateViewForField(JSONObject jsonObject, LayoutInflater factory, int layoutFallback, boolean mustHaveUserInputId) {
        int layout = jsonObject.optInt(JsonFormField.ATTRIBUTE_NAME_LAYOUT, layoutFallback);

        if (layout <= 0)
            layout = layoutFallback;

        View v = factory.inflate(layout, null);

        if (mustHaveUserInputId) {
            if (null == v.findViewById(R.id.user_input))
                throw new IllegalArgumentException("The layout must contain a view with id: user_input");
        }

        return v;
    }

    /**
     *
     * @param jsonObject
     * @return
     */
    public static String getJsonStringValue(JSONObject jsonObject) {
        String text = null;
        try {
            if (jsonObject.has(JsonFormField.ATTRIBUTE_NAME_VALUE))
                text = jsonObject.getString(JsonFormField.ATTRIBUTE_NAME_VALUE);
        }
        catch (Exception ex) {}
        return text;
    }

    /**
     *
     * @param jsonApi
     * @param parent
     * @param jsonObject
     * @param gravity
     * @param listener
     * @param editable
     * @param clickable
     * @param scrollable
     * @param metaDataWatcher
     * @throws JSONException
     */
    public static void bindUserInput(JsonApi jsonApi, View parent, JSONObject jsonObject, int gravity, CommonListener listener, boolean editable, int clickable, boolean scrollable, MetaDataWatcher metaDataWatcher) throws JSONException {
        View userInputView = parent.findViewById(R.id.user_input);
        final String value = getJsonStringValue(jsonObject);
        final String keyStr = jsonObject.getString(JsonFormField.ATTRIBUTE_NAME_KEY);

        bindUserInput(jsonApi, userInputView, keyStr, value, jsonObject.has("textStyle") && jsonObject.getString("textStyle").equalsIgnoreCase("html"), scrollable);

        // inputTextView.setGravity(gravity);
        if (null != userInputView) {
            if (clickable == CLICKABLE_FIELD) {
                userInputView.setClickable(true);
                userInputView.setOnClickListener(listener);

                setViewTagKey(userInputView, keyStr);
            }
        }

        if (null != metaDataWatcher)
            metaDataWatcher.setKeyInputView(keyStr, userInputView, editable, editable, -1);
    }

    /**
     *  @param jsonApi
     * @param userInputView
     * @param keyStr
     * @param value
     * @param styled
     * @param scrollable
     */
    public static void bindUserInput(JsonApi jsonApi, View userInputView, String keyStr, String value, boolean styled, boolean scrollable) {
        if (userInputView instanceof android.widget.TextView) {
            android.widget.TextView inputTextView = (android.widget.TextView) userInputView;

            if (null == value) {
                Object replacement = jsonApi.getNullValueReplacement(keyStr);
                value = null != replacement ? replacement.toString() : "";
            }

            if (styled)
                inputTextView.setText(Html.fromHtml(value));
            else
                inputTextView.setText(value);

            if (scrollable)
                inputTextView.setMovementMethod(new ScrollingMovementMethod());

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

    /**
     *
     * @param factory
     * @return
     */
    protected static ViewGroup createViewContainer(LayoutInflater factory) {
        return (ViewGroup) factory.inflate(R.layout.form_row, null);
    }

    /**
     *
     * @param factory
     * @param jsonObject
     * @param titleKey
     * @return
     * @throws JSONException
     */
    protected static View createTitleView(LayoutInflater factory, JSONObject jsonObject, String titleKey) throws JSONException {
        return createTitleView(factory, jsonObject, titleKey, R.layout.form_title);
    }

    /**
     *
     * @param factory
     * @param jsonObject
     * @param titleKey
     * @param titleLayoutResId
     * @return
     * @throws JSONException
     */
    protected static View createTitleView(LayoutInflater factory, JSONObject jsonObject, String titleKey, int titleLayoutResId) throws JSONException {
        View v = factory.inflate(titleLayoutResId, null);
        bindTitle(v, jsonObject, titleKey);
        return v;
    }

    /**
     *
     * @param parent
     * @param jsonObject
     * @param titleKey
     * @throws JSONException
     */
    protected static void bindTitle(View parent, JSONObject jsonObject, String titleKey) throws JSONException {
        TextView titletext = (TextView) parent.findViewById(android.R.id.text1);
        // 1st Column
        if (jsonObject.has(titleKey))
            titletext.setText(jsonObject.getString(titleKey));
        else if (jsonObject.has(JsonFormField.ATTRIBUTE_NAME_VALUE))
            titletext.setText(jsonObject.getString(JsonFormField.ATTRIBUTE_NAME_VALUE));
        else
            titletext.setVisibility(View.GONE);
    }

    /**
     *
     * @param jsonApi
     * @param stepName
     * @param context
     * @param jsonObject
     * @param metadata
     * @param listener
     * @param editable
     * @param metaDataWatcher
     * @return
     * @throws Exception
     */
    @Override
    public View getViewFromJson(JsonApi jsonApi, String stepName, Context context, JSONObject jsonObject, JsonMetadata metadata, CommonListener listener, boolean editable, MetaDataWatcher metaDataWatcher) throws Exception {

        LayoutInflater factory = LayoutInflater.from(context);

        View v = createView(jsonObject, factory);

        bindDataAndAction(v, jsonApi, jsonObject, editable, listener, metaDataWatcher);

        if (v.isClickable())
            v.setOnClickListener(listener);

        return v;
    }

    protected View createView(JSONObject jsonObject, LayoutInflater factory) {
        return inflateViewForField(jsonObject, factory, layoutResourceId);
    }

    protected void bindDataAndAction(View parent, JsonApi jsonApi, JSONObject jsonObject, boolean editable, CommonListener listener, MetaDataWatcher metaDataWatcher) throws JSONException {
        final String keyStr = jsonObject.optString(JsonFormField.ATTRIBUTE_NAME_KEY);
        final int clickable = jsonObject.optInt(JsonFormField.ATTRIBUTE_NAME_CLICKABLE, CLICKABLE_NONE);
        final String value = jsonObject.optString(JsonFormField.ATTRIBUTE_NAME_VALUE);
        final boolean enabled = jsonObject.optBoolean(JsonFormField.ATTRIBUTE_NAME_ENABLED, true);
        final boolean scrollable = jsonObject.optBoolean(JsonFormField.ATTRIBUTE_NAME_SCROLLABLE, false);

        setViewTagKey(parent, keyStr);

        // do nothing for now, not all widgets need binding data
        TextView tv = parent.findViewById(android.R.id.text1);
        if (null != tv) {
            String text = jsonObject.optString(JsonFormField.ATTRIBUTE_NAME_TITLE);
            tv.setText(text);
        }

        /**
         * Check if there is a view with the id of "user_input"
         */
        View userInputView = parent.findViewById(R.id.user_input);

        if (null != userInputView) {

            bindUserInput(jsonApi, userInputView, keyStr, value, false, scrollable);

            if (clickable == CLICKABLE_FIELD) {
                userInputView.setClickable(true);
                userInputView.setOnClickListener(listener);

                /**
                 * Set the input view with key too
                 */
                setViewTagKey(userInputView, keyStr);
            }

            /**
             * Register the view to the form metadata map
             */
            listener.onInitialValueSet(keyStr, null, value);
        }

        if (null != metaDataWatcher)
            metaDataWatcher.setKeyInputView(keyStr, userInputView, editable, enabled, -1);


        if (clickable == CLICKABLE_ROW) {
            parent.setClickable(true);
            parent.setOnClickListener(listener);
        }
    }

    @Override
    public void updateView(JsonApi jsonApi, View view, String targetKey, Object value, ColorStateList fieldTextColors) {
        // no ops yet
        // as most views won't be changed due the lifespan
    }
}
