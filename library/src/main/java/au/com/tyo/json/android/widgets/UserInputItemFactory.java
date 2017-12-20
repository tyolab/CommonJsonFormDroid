package au.com.tyo.json.android.widgets;

import android.content.Context;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.RegexpValidator;
import com.rey.material.util.ViewUtil;
import com.rey.material.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import au.com.tyo.json.JsonFormFieldFilter;
import au.com.tyo.json.android.R;
import au.com.tyo.json.android.customviews.GenericTextWatcher;
import au.com.tyo.json.android.edittext.MaxLengthValidator;
import au.com.tyo.json.android.edittext.MinLengthValidator;
import au.com.tyo.json.android.edittext.RequiredValidator;
import au.com.tyo.json.android.interfaces.CommonListener;
import au.com.tyo.json.android.interfaces.FormWidgetFactory;
import au.com.tyo.json.android.utils.JsonMetadata;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 26/7/17.
 */

public abstract class UserInputItemFactory extends CommonItemFactory implements FormWidgetFactory {

    @Override
    public List<View> getViewsFromJson(String stepName, Context context, JSONObject jsonObject, CommonListener listener, boolean editable) throws Exception {

        List<View> views = new ArrayList<>(1);
        LayoutInflater factory = LayoutInflater.from(context);
        ViewGroup v = createViewContainer(factory);

        JsonMetadata metadata = new JsonMetadata(jsonObject);
        setViewTags(v, metadata);

        View child = createView(factory, v, stepName, jsonObject, listener, editable);
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

    protected abstract View createView(LayoutInflater factory, ViewGroup parent, String stepName, JSONObject jsonObject, CommonListener listener, boolean editable) throws JSONException;

    protected View createTitleView(LayoutInflater factory, JSONObject jsonObject, String titleKey) throws JSONException {
        View v = factory.inflate(R.layout.form_title, null);
        bindTitle(v, jsonObject, titleKey);
        return v;
    }

    protected void bindTitle(View parent, JSONObject jsonObject, String titleKey) throws JSONException {
        // 1st Column
        TextView titletext = (TextView) parent.findViewById(android.R.id.text1);
        titletext.setText(jsonObject.getString(titleKey));
    }

    protected View createEditText(LayoutInflater factory, ViewGroup parent, int resId, String stepName, JSONObject jsonObject, int minLength, int maxLength, final CommonListener listener) throws JSONException {
        View v = factory.inflate(
                resId, parent, false);
        MaterialEditText editText = (MaterialEditText) v.findViewById(R.id.user_input);
        editText.setHint(jsonObject.getString("hint"));
        //editText.setFloatingLabelText(jsonObject.getString("hint"));

        // no no, we don't need it, the id should be "id/user_input"
        if (editText.getId() < 0)
            editText.setId(ViewUtil.generateViewId());
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
        JSONArray validatorArray = jsonObject.optJSONArray("validators");
        if (validatorArray != null) {
            for (int i = 0; i < validatorArray.length(); ++ i) {
                JSONObject requiredObject = validatorArray.getJSONObject(i);
                String validatorName = requiredObject.getString("name");
                if (validatorName.equals("v_required")) {
                    String requiredValue = requiredObject.getString("value");
                    if (!TextUtils.isEmpty(requiredValue)) {
                        if (Boolean.TRUE.toString().equalsIgnoreCase(requiredValue)) {
                            editText.addValidator(new RequiredValidator(requiredObject.getString("err")));
                        }
                    }
                }
                else if (validatorName.equals("v_min_length")) {
                    String minLengthValue = requiredObject.optString("value");
                    if (!TextUtils.isEmpty(minLengthValue)) {
                        minLength = Integer.parseInt(minLengthValue);
                        editText.addValidator(new MinLengthValidator(requiredObject.getString("err"), Integer.parseInt(minLengthValue)));
                    }
                }

                else if (validatorName.equals("v_max_length")) {
                    String maxLengthValue = requiredObject.optString("value");
                    if (!TextUtils.isEmpty(maxLengthValue)) {
                        maxLength = Integer.parseInt(maxLengthValue);
                        editText.addValidator(new MaxLengthValidator(requiredObject.getString("err"), Integer.parseInt(maxLengthValue)));
                    }
                }

                else if (validatorName.equals("v_regex")) {
                    String regexValue = requiredObject.optString("value");
                    if (!TextUtils.isEmpty(regexValue)) {
                        editText.addValidator(new RegexpValidator(requiredObject.getString("err"), regexValue));
                    }
                }

                else if (validatorName.equals("v_email")) {
                    String emailValue = requiredObject.optString("value");
                    if (!TextUtils.isEmpty(emailValue)) {
                        if (Boolean.TRUE.toString().equalsIgnoreCase(emailValue)) {
                            editText.addValidator(new RegexpValidator(requiredObject.getString("err"), Patterns.EMAIL_ADDRESS.toString()));
                        }
                    }
                }

                else if (validatorName.equals("v_url")) {
                    String urlValue = requiredObject.optString("value");
                    if (!TextUtils.isEmpty(urlValue)) {
                        if (Boolean.TRUE.toString().equalsIgnoreCase(urlValue)) {
                            editText.addValidator(new RegexpValidator(requiredObject.getString("err"), Patterns.WEB_URL.toString()));
                        }
                    }
                }

                else if (validatorName.equals("v_numeric")) {
                    String numericValue = requiredObject.optString("value");
                    if (!TextUtils.isEmpty(numericValue)) {
                        if (Boolean.TRUE.toString().equalsIgnoreCase(numericValue)) {
                            editText.addValidator(new RegexpValidator(requiredObject.getString("err"), "[0-9]+"));
                        }
                    }
                }
            }
        }

        editText.setMaxCharacters(maxLength);
        editText.setMinCharacters(minLength);

        // add filters
        InputFilter[] editFilters = editText.getFilters();
        Set<InputFilter> newFilters = new HashSet<InputFilter>();
        JSONArray filterArray = jsonObject.optJSONArray("filters");

        for (InputFilter filter : editFilters)
            newFilters.add(filter);

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
        editText.addTextChangedListener(new GenericTextWatcher(stepName, editText));

        editText.setOnFocusChangeListener(listener);
        return v;
    }
}
