package au.com.tyo.json.android.interfaces;

import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import au.com.tyo.json.android.fragments.FormFragment;

/**
 * Created by vijay on 5/16/15.
 */
public interface JsonApi {
    JSONObject getStep(String stepName);

    void writeValue(String stepName, String key, String value) throws JSONException;

    void writeValue(String stepName, String prentKey, String childObjectKey, String childKey, String value)
            throws JSONException;

    String currentJsonState();

    String getCount();

    boolean isEditable();

    String formatDateTime(String key, Date date);

    FormFragment getJsonFormFragment();

    String getPredefinedValue(String stepName, String key);

    String getPredefinedValueMax(String stepName, String key);

    String getPredefinedValueMin(String stepName, String key);

    void onFieldClick(View v);
}
