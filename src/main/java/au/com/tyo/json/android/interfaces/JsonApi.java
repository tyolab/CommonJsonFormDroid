package au.com.tyo.json.android.interfaces;

import android.view.View;
import android.widget.ImageView;

import au.com.tyo.json.util.OrderedDataMap;
import google.json.JSONException;
import google.json.JSONObject;

import java.util.Date;
import java.util.List;

import au.com.tyo.json.android.fragments.FormFragment;
import au.com.tyo.json.validator.Validator;

/**
 * Created by vijay on 5/16/15.
 */
public interface JsonApi {

    JSONObject getStep(String stepName);

    boolean writeValue(String stepName, String key, String value) throws JSONException;

    void writeValue(String stepName, String prentKey, String childObjectKey, String childKey, String value)
            throws JSONException;

    String currentJsonState();

    String getCount();

    boolean isEditable();

    String formatDateTime(String key, Date date);

    void updateForm(String keyStr);

    void updateForm(String keyStr, Object value);

    void updateGroupVisibility(String keyStr, boolean visible);

    void updateFieldVisibility(String keyStr, boolean visible);

    void enableField(String keyStr, boolean enabled);

    void setFieldEditable(String keyStr, boolean editable);

    FormFragment getJsonFormFragment();

    String getPredefinedValue(String stepName, String key);

    String getPredefinedValueMax(String stepName, String key);

    String getPredefinedValueMin(String stepName, String key);

    void onFieldClick(String key, String type);

    void onFieldValueClear(String key);

    CommonListener getFormOnClickListenerByName(String listenerMethodStr);

    CommonListener getFormOnClickListenerByKey(String key, String text);

    Object getNullValueReplacement(String keyStr);

    boolean onValidateRequiredFormFieldFailed(String key, String errorMessage);

    void loadFormFieldImage(String keyStr, ImageView imageView);

    void installValidator(String keyStr, Validator validator);

    boolean validate(String key, String text);

    void setupFormHeader(View view);

    void setupFormFooter(View view);

    void updateFieldTitle(String keyStr, int titleResId);

    OrderedDataMap getOrderedDataMap();

    List getGroups();

    List getFields();
}
