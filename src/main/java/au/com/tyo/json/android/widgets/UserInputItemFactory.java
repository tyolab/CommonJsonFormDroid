package au.com.tyo.json.android.widgets;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import google.json.JSONArray;
import google.json.JSONException;
import google.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

import au.com.tyo.android.AndroidUtils;
import au.com.tyo.json.android.validators.MaxValidator;
import au.com.tyo.json.android.validators.MinValidator;
import au.com.tyo.json.android.validators.RegexpValidator;
import au.com.tyo.json.android.validators.RequiredValidator;
import au.com.tyo.json.form.FieldValue;
import au.com.tyo.json.jsonform.JsonFormField;
import au.com.tyo.json.jsonform.JsonFormFieldFilter;
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

    public UserInputItemFactory(String widgetKey) {
        super(widgetKey);
    }

    protected abstract View createView(JsonApi jsonApi, LayoutInflater factory, ViewGroup parent, String stepName, JSONObject jsonObject, JsonMetadata metadata, CommonListener listener, boolean editable, int clickable, MetaDataWatcher metaDataWatcher) throws JSONException;

    @Override
    public View getViewFromJson(JsonApi jsonApi, String stepName, Context context, JSONObject jsonObject, JsonMetadata metadata, CommonListener listener, boolean editable, MetaDataWatcher metaDataWatcher) throws Exception {

        LayoutInflater factory = LayoutInflater.from(context);

        ViewGroup v = createViewContainer(factory);
        RelativeLayout.LayoutParams layoutParams = (new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

        int clickable = jsonObject.optInt(JsonFormField.ATTRIBUTE_NAME_CLICKABLE, 0);
        View child = createView(jsonApi, factory, v, stepName, jsonObject, metadata, listener, editable, clickable, metaDataWatcher);

        if (null != child) {
            child.setLayoutParams(layoutParams);
            v.addView(child);
        }

        adjustViewVisibility(v, jsonObject, listener);

        return v;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint("ResourceType")
    protected View createEditText(JsonApi jsonApi, LayoutInflater factory, ViewGroup parent, int resId, String stepName, JSONObject jsonObject, int minLength, int maxLength, final CommonListener listener) throws JSONException {
        final String keyStr = jsonObject.getString("key");

        View v = inflateViewForField(jsonObject, factory, resId); //factory.inflate(resId, parent, false);
        EditText editText = (EditText) v.findViewById(R.id.user_input);

        if (jsonObject.has("editable")) {
            editText.setEnabled(jsonObject.getBoolean("editable"));
        }

        if (jsonObject.has("hint"))
            editText.setHint(jsonObject.getString("hint"));
        //editText.setFloatingLabelText(jsonObject.getString("hint"));

        // no no, we don't need it, the id should be "id/user_input"
        if (editText.getId() < 0)
            editText.setId(AndroidUtils.generateViewId());
        // JsonMetadata metadata = new JsonMetadata(jsonObject);
        // setViewTags(editText, metadata);

        String value = jsonObject.optString("value");
        if (!TextUtils.isEmpty(value)) {
            editText.setText(value);
            listener.onInitialValueSet(keyStr, null, value);
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
                           jsonApi.installValidator(keyStr, new RequiredValidator(requiredObject.getString("err")));
                       }
                   }
               }
               else if (validatorName.equals("v_min_length")) {
                   String minLengthValue = requiredObject.optString("value");
                   if (!TextUtils.isEmpty(minLengthValue)) {
                       minLength = Integer.parseInt(minLengthValue);
                       jsonApi.installValidator(keyStr,  new MinValidator(requiredObject.getString("err"), minLength));
                   }
               }

               else if (validatorName.equals("v_max_length")) {
                   String maxLengthValue = requiredObject.optString("value");
                   if (!TextUtils.isEmpty(maxLengthValue)) {
                       maxLength = Integer.parseInt(maxLengthValue);
                       jsonApi.installValidator(keyStr,  new MaxValidator(requiredObject.getString("err"), maxLength));
                   }
               }

               else if (validatorName.equals("v_regex")) {
                   String regexValue = requiredObject.optString("value");
                   if (!TextUtils.isEmpty(regexValue)) {
                       jsonApi.installValidator(keyStr,  new RegexpValidator(requiredObject.getString("err"), regexValue));
                   }
               }

               else if (validatorName.equals("v_email")) {
                   String emailValue = requiredObject.optString("value");
                   if (!TextUtils.isEmpty(emailValue)) {
                       if (Boolean.TRUE.toString().equalsIgnoreCase(emailValue)) {
                           jsonApi.installValidator(keyStr,  new RegexpValidator(requiredObject.getString("err"), Patterns.EMAIL_ADDRESS.toString()));
                       }
                   }
               }

               else if (validatorName.equals("v_url")) {
                   String urlValue = requiredObject.optString("value");
                   if (!TextUtils.isEmpty(urlValue)) {
                       if (Boolean.TRUE.toString().equalsIgnoreCase(urlValue)) {
                           jsonApi.installValidator(keyStr,  new RegexpValidator(requiredObject.getString("err"), Patterns.WEB_URL.toString()));
                       }
                   }
               }

               else if (validatorName.equals("v_numeric")) {
                   String numericValue = requiredObject.optString("value");
                   if (!TextUtils.isEmpty(numericValue)) {
                       if (Boolean.TRUE.toString().equalsIgnoreCase(numericValue)) {
                           jsonApi.installValidator(keyStr,  new RegexpValidator(requiredObject.getString("err"), "[0-9]+"));
                       }
                   }
               }
           }
       }

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

    @Override
    public void updateView(JsonApi jsonApi, View view, String targetKey, boolean enabled, Object value, ColorStateList fieldTextColors) {
        View userInputView = view.findViewById(R.id.user_input);
        userInputView.setEnabled(enabled);

        updateUserInput(jsonApi, userInputView, targetKey, value, fieldTextColors);
    }

    protected void updateUserInput(JsonApi jsonApi, View userInputView, String targetKey, Object result, ColorStateList fieldTextColors) {

        String text = null;
        if (null != result) {
            if (result instanceof String)
                text = (String) result;
            else if (result instanceof FieldValue)
                text = ((FieldValue) result).getStringValue();
            else
                text = result.toString();
        }

        /** it can be null
         if (null == text)
         return;
         */

        // update the text
        if (null != userInputView) {
            UserInputItemFactory.bindUserInput(jsonApi, userInputView, targetKey, text, false, false);

            if (userInputView instanceof TextView) {
                TextView button = (TextView) userInputView;
                // button.setText(text);
                button.setTextColor(fieldTextColors);
            }
        }
    }
}
