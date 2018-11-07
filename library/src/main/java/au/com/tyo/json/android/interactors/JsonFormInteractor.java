package au.com.tyo.json.android.interactors;

import android.content.Context;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.tyo.json.android.constants.JsonFormConstants;
import au.com.tyo.json.android.interfaces.CommonListener;
import au.com.tyo.json.android.interfaces.FormWidgetFactory;
import au.com.tyo.json.android.interfaces.JsonApi;
import au.com.tyo.json.android.widgets.CheckBoxFactory;
import au.com.tyo.json.android.widgets.EditTextFactory;
import au.com.tyo.json.android.widgets.GapFactory;
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
        map.put(JsonFormConstants.GAP, new GapFactory());
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
        viewsFromJson.addAll(createFieldViews(jsonApi, stepName, context, parentJson, listener, editable));
        viewsFromJson.addAll(createGroupViews(jsonApi, stepName, context, parentJson, listener, editable));

        return viewsFromJson;
    }

    private Collection<? extends View> createGroupViews(JsonApi jsonApi, String stepName, Context context, JSONObject parentJson, CommonListener listener, boolean editable) {
        List<View> viewsFromJson = new ArrayList<>(5);
        try {
            JSONArray groups = null;

            try {
                groups = parentJson.getJSONArray("groups");
            }
            catch (Exception ex) {}

            if (null != groups)
                for (int i = 0; i < groups.length(); i++) {
                    JSONObject childJson = groups.getJSONObject(i);
                    try {
                        createFieldViews(jsonApi, stepName, context, childJson, listener, editable);
                    } catch (Exception e) {
                        Log.e(TAG,
                                "Exception occurred in making group view at index : " + i + " : Exception is : "
                                        + e.getMessage());
                    }
                }
        } catch (JSONException e) {
            Log.e(TAG, "Json exception occurred : " + e.getMessage());
        }
        return  viewsFromJson;
    }

    private Collection<? extends View> createFieldViews(JsonApi jsonApi, String stepName, Context context, JSONObject parentJson, CommonListener listener, boolean editable) {
        List<View> viewsFromJson = new ArrayList<>(5);
        try {
            JSONArray fields = null;

            try {
                fields = parentJson.getJSONArray("fields");
            }
            catch (Exception ex) {}

            if (null != fields)
                for (int i = 0; i < fields.length(); i++) {
                    JSONObject childJson = fields.getJSONObject(i);
                    try {
                        List<View> views =  map.get(childJson.getString("type")).getViewsFromJson(jsonApi, stepName, context, childJson, listener, editable);
                        if (views.size() > 0) {
                            viewsFromJson.addAll(views);
                        }
                    } catch (Exception e) {
                        Log.e(TAG,
                                "Exception occurred in making child view at index : " + i + " : Exception is : "
                                        + e.getMessage());
                    }
                }
        } catch (JSONException e) {
            Log.e(TAG, "Json exception occurred : " + e.getMessage());
        }
        return  viewsFromJson;
    }

    public static JsonFormInteractor getInstance() {
        return INSTANCE;
    }
}
