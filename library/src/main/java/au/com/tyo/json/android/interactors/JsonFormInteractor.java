package au.com.tyo.json.android.interactors;

import android.content.Context;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.tyo.json.android.constants.JsonFormConstants;
import au.com.tyo.json.android.interfaces.CommonListener;
import au.com.tyo.json.android.interfaces.FormWidgetFactory;
import au.com.tyo.json.android.interfaces.JsonApi;
import au.com.tyo.json.android.widgets.CheckBoxFactory;
import au.com.tyo.json.android.widgets.EditTextFactory;
import au.com.tyo.json.android.widgets.ImagePickerFactory;
import au.com.tyo.json.android.widgets.LabelFactory;
import au.com.tyo.json.android.widgets.RadioButtonFactory;
import au.com.tyo.json.android.widgets.SpinnerFactory;

/**
 * Created by vijay on 5/19/15.
 */
public class JsonFormInteractor {

    private static final String                     TAG               = "JsonFormInteractor";
    private static final Map<String, FormWidgetFactory>    map = new HashMap<>();
    private static final JsonFormInteractor         INSTANCE          = new JsonFormInteractor();

    private JsonFormInteractor() {
        registerWidgets();
    }

    private void registerWidgets() {
        map.put(JsonFormConstants.EDIT_TEXT, new EditTextFactory());
        map.put(JsonFormConstants.LABEL, new LabelFactory());
        map.put(JsonFormConstants.CHECK_BOX, new CheckBoxFactory());
        map.put(JsonFormConstants.RADIO_BUTTON, new RadioButtonFactory());
        map.put(JsonFormConstants.CHOOSE_IMAGE, new ImagePickerFactory());
        map.put(JsonFormConstants.SPINNER, new SpinnerFactory());
    }

    public static void registerWidget(String key, FormWidgetFactory factory) {
        map.put(key, factory);
    }

    public static void registerWidget(FormWidgetFactory factory) {
        map.put(factory.getClass().getSimpleName(), factory);
    }

    public List<View> fetchFormElements(JsonApi jsonApi, String stepName, Context context, JSONObject parentJson, CommonListener listener, boolean editable) {
        Log.d(TAG, "fetchFormElements called");
        List<View> viewsFromJson = new ArrayList<>(5);
        try {
            JSONArray fields = parentJson.getJSONArray("fields");
            for (int i = 0; i < fields.length(); i++) {
                JSONObject childJson = fields.getJSONObject(i);
                try {
                    List<View> views =  map.get(childJson.getString("type")).getViewsFromJson(jsonApi, stepName, context, childJson, listener, editable);
                    if (views.size() > 0) {
                        viewsFromJson.addAll(views);
                    }
                } catch (Exception e) {
                    Log.d(TAG,
                            "Exception occurred in making child view at index : " + i + " : Exception is : "
                                    + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            Log.d(TAG, "Json exception occurred : " + e.getMessage());
            e.printStackTrace();
        }
        return viewsFromJson;
    }

    public static JsonFormInteractor getInstance() {
        return INSTANCE;
    }
}
