package au.com.tyo.json.android.widgets;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rey.material.util.ViewUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import au.com.tyo.android.AndroidUtils;
import au.com.tyo.json.JsonFormFieldFilter;
import au.com.tyo.json.android.R;
import au.com.tyo.json.android.customviews.GenericTextWatcher;
import au.com.tyo.json.android.interfaces.CommonListener;
import au.com.tyo.json.android.interfaces.JsonApi;
import au.com.tyo.json.android.interfaces.MetaDataWatcher;
import au.com.tyo.json.android.utils.JsonMetadata;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 26/7/17.
 */

public abstract class UserInputItemFactory extends CommonItemFactory {

    protected abstract View createView(JsonApi jsonApi, LayoutInflater factory, ViewGroup parent, String stepName, JSONObject jsonObject, CommonListener listener, boolean editable, MetaDataWatcher metaDataWatcher) throws JSONException;

    @Override
    public List<View> getViewsFromJson(JsonApi jsonApi, String stepName, Context context, JSONObject jsonObject, CommonListener listener, boolean editable, MetaDataWatcher metaDataWatcher) throws Exception {

        List<View> views = new ArrayList<>(1);
        LayoutInflater factory = LayoutInflater.from(context);

        ViewGroup v = createViewContainer(factory);
        RelativeLayout.LayoutParams layoutParams = (new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

        JsonMetadata metadata = new JsonMetadata(jsonObject);
        setViewTags(v, metadata);

        View child = createView(jsonApi, factory, v, stepName, jsonObject, listener, editable, metaDataWatcher);
        child.setLayoutParams(layoutParams);
        v.addView(child);

        views.add(v);

        if (jsonObject.has("visible")) {
            boolean visible = Boolean.parseBoolean(jsonObject.getString("visible"));
            if (!visible) {
                v.setVisibility(View.GONE);
                listener.onVisibilityChange(metadata.key, null, false);
            }
        }
        return views;
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
        else
            titletext.setVisibility(View.GONE);
    }

    protected void bindUserInput(View parent, JSONObject jsonObject, int gravity) throws JSONException {
        View view = parent.findViewById(R.id.user_input);
        String value = jsonObject.getString("value");
        if (view instanceof android.widget.TextView) {
            android.widget.TextView inputTextView = (android.widget.TextView) view;

            if (jsonObject.has("textStyle") && jsonObject.getString("textStyle").equalsIgnoreCase("html"))
                inputTextView.setText(Html.fromHtml(value));
            else
                inputTextView.setText(value);

            inputTextView.setGravity(gravity);
        }
        else if (view instanceof EditText) {
            EditText inputTextView = (EditText) view;
            inputTextView.setText(value);
        }
        else
            throw new IllegalStateException("Unknown user input view type");
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint("ResourceType")
    protected View createEditText(JsonApi jsonApi, LayoutInflater factory, ViewGroup parent, int resId, String stepName, JSONObject jsonObject, int minLength, int maxLength, final CommonListener listener) throws JSONException {
        View v = factory.inflate(
                resId, parent, false);
        EditText editText = (EditText) v.findViewById(R.id.user_input);

        if (jsonObject.has("editable")) {
            editText.setEnabled(jsonObject.getBoolean("editable"));
        }

        if (jsonObject.has("hint"))
            editText.setHint(jsonObject.getString("hint"));
        //editText.setFloatingLabelText(jsonObject.getString("hint"));

        // no no, we don't need it, the id should be "id/user_input"
        if (editText.getId() < 0) {
            if (AndroidUtils.getAndroidVersion() >= 17 )
                editText.setId(View.generateViewId());
            else
                editText.setId(ViewUtil.generateViewId());
        }
        JsonMetadata metadata = new JsonMetadata(jsonObject);
        setViewTags(editText, metadata);

        String value = jsonObject.optString("value");
        if (!TextUtils.isEmpty(value)) {
            editText.setText(value);
            listener.onInitialValueSet(metadata.key, null, value);
        }
        else
            editText.setText("");

        //add validators
//        JSONArray validatorArray = jsonObject.optJSONArray("validators");
//        if (validatorArray != null) {
//            for (int i = 0; i < validatorArray.length(); ++ i) {
//                JSONObject requiredObject = validatorArray.getJSONObject(i);
//                String validatorName = requiredObject.getString("name");
//                if (validatorName.equals("v_required")) {
//                    String requiredValue = requiredObject.getString("value");
//                    if (!TextUtils.isEmpty(requiredValue)) {
//                        if (Boolean.TRUE.toString().equalsIgnoreCase(requiredValue)) {
//                            editText.addValidator(new RequiredValidator(requiredObject.getString("err")));
//                        }
//                    }
//                }
//                else if (validatorName.equals("v_min_length")) {
//                    String minLengthValue = requiredObject.optString("value");
//                    if (!TextUtils.isEmpty(minLengthValue)) {
//                        minLength = Integer.parseInt(minLengthValue);
//                        editText.addValidator(new MinLengthValidator(requiredObject.getString("err"), Integer.parseInt(minLengthValue)));
//                    }
//                }
//
//                else if (validatorName.equals("v_max_length")) {
//                    String maxLengthValue = requiredObject.optString("value");
//                    if (!TextUtils.isEmpty(maxLengthValue)) {
//                        maxLength = Integer.parseInt(maxLengthValue);
//                        editText.addValidator(new MaxLengthValidator(requiredObject.getString("err"), Integer.parseInt(maxLengthValue)));
//                    }
//                }
//
//                else if (validatorName.equals("v_regex")) {
//                    String regexValue = requiredObject.optString("value");
//                    if (!TextUtils.isEmpty(regexValue)) {
//                        editText.addValidator(new RegexpValidator(requiredObject.getString("err"), regexValue));
//                    }
//                }
//
//                else if (validatorName.equals("v_email")) {
//                    String emailValue = requiredObject.optString("value");
//                    if (!TextUtils.isEmpty(emailValue)) {
//                        if (Boolean.TRUE.toString().equalsIgnoreCase(emailValue)) {
//                            editText.addValidator(new RegexpValidator(requiredObject.getString("err"), Patterns.EMAIL_ADDRESS.toString()));
//                        }
//                    }
//                }
//
//                else if (validatorName.equals("v_url")) {
//                    String urlValue = requiredObject.optString("value");
//                    if (!TextUtils.isEmpty(urlValue)) {
//                        if (Boolean.TRUE.toString().equalsIgnoreCase(urlValue)) {
//                            editText.addValidator(new RegexpValidator(requiredObject.getString("err"), Patterns.WEB_URL.toString()));
//                        }
//                    }
//                }
//
//                else if (validatorName.equals("v_numeric")) {
//                    String numericValue = requiredObject.optString("value");
//                    if (!TextUtils.isEmpty(numericValue)) {
//                        if (Boolean.TRUE.toString().equalsIgnoreCase(numericValue)) {
//                            editText.addValidator(new RegexpValidator(requiredObject.getString("err"), "[0-9]+"));
//                        }
//                    }
//                }
//            }
//        }

        // add filters
        InputFilter[] editFilters = editText.getFilters();
        Set<InputFilter> newFilters = new HashSet<InputFilter>();
        JSONArray filterArray = jsonObject.optJSONArray("filters");

        for (InputFilter filter : editFilters)
            newFilters.add(filter);

//        if (maxLength > 0) {
//            editText.setMaxCharacters(maxLength);
//            newFilters.add(new InputFilter.LengthFilter(maxLength));
//        }
//        editText.setMinCharacters(minLength);

        Set<Integer> inputTypes = new HashSet<>();

        if (filterArray != null)
            for (int i = 0; i < filterArray.length(); ++ i) {
                JSONObject requiredObject = filterArray.getJSONObject(i);
                String filterName = requiredObject.getString("name");
//                    editText.setAllCaps(true);

                if (filterName.equals(JsonFormFieldFilter.FILTER_NUMBERIC)) {
                    String requiredValue = requiredObject.getString("value");
                    if (!TextUtils.isEmpty(requiredValue)) {
                        if (Boolean.TRUE.toString().equalsIgnoreCase(requiredValue)) {
                            inputTypes.add(InputType.TYPE_CLASS_NUMBER);
                        }
                    }
                }
                else if (filterName.equals(JsonFormFieldFilter.FILTER_DECIMAL)) {
                    String requiredValue = requiredObject.getString("value");
                    if (!TextUtils.isEmpty(requiredValue)) {
                        if (Boolean.TRUE.toString().equalsIgnoreCase(requiredValue)) {
                            inputTypes.add(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                        }
                    }
                }
                else if (filterName.equals(JsonFormFieldFilter.FILTER_TEXT)) {
                    String requiredValue = requiredObject.getString("value");
                    if (!TextUtils.isEmpty(requiredValue)) {
                        if (Boolean.TRUE.toString().equalsIgnoreCase(requiredValue)) {
                            inputTypes.add(InputType.TYPE_CLASS_TEXT);
                        }
                    }
                }
                else if (filterName.equals(JsonFormFieldFilter.FILTER_CAP_SENTENCES)) {
                    String requiredValue = requiredObject.getString("value");
                    if (!TextUtils.isEmpty(requiredValue)) {
                        if (Boolean.TRUE.toString().equalsIgnoreCase(requiredValue)) {
                            inputTypes.add(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                        }
                    }
                }
                else if (filterName.equals(JsonFormFieldFilter.FILTER_CAP_WORDS)) {
                    String requiredValue = requiredObject.getString("value");
                    if (!TextUtils.isEmpty(requiredValue)) {
                        if (Boolean.TRUE.toString().equalsIgnoreCase(requiredValue)) {
                            inputTypes.add(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                        }
                    }
                }
                else if (filterName.equals(JsonFormFieldFilter.FILTER_CAP_CHARACTERS)) {
                    String requiredValue = requiredObject.getString("value");
                    if (!TextUtils.isEmpty(requiredValue)) {
                        if (Boolean.TRUE.toString().equalsIgnoreCase(requiredValue)) {
                            inputTypes.add(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
                        }
                    }
                }
                else if (filterName.equals(JsonFormFieldFilter.FILTER_ALLCAPS)) {
                    newFilters.add(new InputFilter.AllCaps());
                }
            }

        InputFilter[] filters = new InputFilter[newFilters.size()];
        Object[] objects = newFilters.toArray();
        for (int i = 0; i < filters.length; ++i) {
            InputFilter filter = (InputFilter) objects[i];
            filters[i] = filter;
        }
        editText.setFilters(filters);

        if (inputTypes.size() > 0) {
            int types = 0;
            for (Integer type : inputTypes) {
                types |= type;
            }
            // be careful, setRawInputType doesn't work on the CAP_SETENNCES and CAP_WORDS
            editText.setInputType(types);
        }
        editText.addTextChangedListener(new GenericTextWatcher(jsonApi, stepName, editText));

        editText.setOnFocusChangeListener(listener);
        return v;
    }
}
