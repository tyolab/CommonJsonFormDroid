package au.com.tyo.json.android.interactors;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.tyo.json.android.R;
import au.com.tyo.json.android.constants.JsonFormConstants;
import au.com.tyo.json.android.interfaces.CommonListener;
import au.com.tyo.json.android.interfaces.FormWidgetFactory;
import au.com.tyo.json.android.interfaces.JsonApi;
import au.com.tyo.json.android.interfaces.MetaDataWatcher;
import au.com.tyo.json.android.widgets.CheckBoxFactory;
import au.com.tyo.json.android.widgets.EditTextFactory;
import au.com.tyo.json.android.widgets.GapFactory;
import au.com.tyo.json.android.widgets.GroupTitleFactory;
import au.com.tyo.json.android.widgets.ImagePickerFactory;
import au.com.tyo.json.android.widgets.LabelFactory;
import au.com.tyo.json.android.widgets.RadioButtonFactory;
import au.com.tyo.json.android.widgets.SpinnerFactory;

/**
 * Created by vijay on 5/19/15.
 */
public class JsonFormInteractor {

    private static final String                             TAG               = "JsonFormInteractor";
    private static final Map<String, FormWidgetFactory>    map                = new HashMap<>();
    private static final JsonFormInteractor                INSTANCE           = new JsonFormInteractor();

    private JsonFormInteractor() {
        registerWidgets();
    }

    private void registerWidgets() {
        registerWidget(new EditTextFactory(JsonFormConstants.EDIT_TEXT));
        registerWidget(new LabelFactory(JsonFormConstants.LABEL));
        registerWidget(new CheckBoxFactory(JsonFormConstants.CHECK_BOX));
        registerWidget(new RadioButtonFactory(JsonFormConstants.RADIO_BUTTON));
        registerWidget(new ImagePickerFactory(JsonFormConstants.CHOOSE_IMAGE));
        registerWidget(new SpinnerFactory(JsonFormConstants.SPINNER));
        registerWidget(new GapFactory(JsonFormConstants.GAP));
    }

    public static void registerWidget(String key, FormWidgetFactory factory) {
        map.put(key, factory);
    }

    public static void registerWidget(FormWidgetFactory factory) {
        map.put(factory.getWidgetKey(), factory);
    }

    /**
     * Using factory class name as key
     *
     * @param factory
     * @param <T>
     */
    public static <T extends FormWidgetFactory> void registerWidget(Class<T> factory) {
        try {
            Constructor ctor = factory.getConstructor(String.class);
            FormWidgetFactory instance = (FormWidgetFactory) ctor.newInstance(new Object[]{factory.getSimpleName()});

            registerWidget(instance);
        } catch (InstantiationException e) {
            Log.e(TAG, "Failed to create widget factory:" + factory.getSimpleName(), e);
        } catch (IllegalAccessException e) {
            Log.e(TAG, "Failed to create widget factory:" + factory.getSimpleName(), e);
        } catch (NoSuchMethodException e) {
            Log.e(TAG, "Failed to create widget factory:" + factory.getSimpleName(), e);
        } catch (InvocationTargetException e) {
            Log.e(TAG, "Failed to create widget factory:" + factory.getSimpleName(), e);
        }
    }

    public List<View> fetchFormElements(JsonApi jsonApi, String stepName, Context context, JSONObject parentJson, CommonListener listener, boolean editable, MetaDataWatcher metaDataWatcher) {
        Log.d(TAG, "fetchFormElements called");
        List<View> viewsFromJson = new ArrayList<>(5);

        LayoutInflater factory = LayoutInflater.from(context);

        View view;
        view = createHeaderView(factory, parentJson);

        if (null != view)
            viewsFromJson.add(view);

        viewsFromJson.addAll(createFieldViews(jsonApi, factory, stepName, context, parentJson, listener, editable, metaDataWatcher));
        viewsFromJson.addAll(createGroupViews(jsonApi, factory, stepName, context, parentJson, listener, editable, metaDataWatcher));

        view = createFooterView(factory, parentJson);
        if (null != view)
            viewsFromJson.add(view);

        return viewsFromJson;
    }

    private View createFooterView(LayoutInflater factory, JSONObject parentJson) {
        return createStaticView("footer", factory, parentJson);
    }

    private View createHeaderView(LayoutInflater factory, JSONObject parentJson) {
        return createStaticView("header", factory, parentJson);
    }

    private View createStaticView(String name, LayoutInflater factory, JSONObject parentJson) {
        int value = -1;
        View view = null;
        try {
            value = parentJson.getInt(name);

            //if (jsonObject.has("value"))
            if (value > 0)
                view = factory.inflate(value, null); //.getInt("value"), null);

        } catch (JSONException e) {
            Log.e(TAG, "failed to get json object header / footer", e);
        }
        return view;
    }

    private List<? extends View> createGroupViews(JsonApi jsonApi, LayoutInflater factory, String stepName, Context context, JSONObject parentJson, CommonListener listener, boolean editable, MetaDataWatcher metaDataWatcher) {
        List<View> viewsFromJson = new ArrayList<>();
        try {
            JSONArray groups = null;

            try {
                groups = parentJson.getJSONArray("groups");
            }
            catch (Exception ex) {}

            if (null != groups) {
                for (int i = 0; i < groups.length(); i++) {

                    if (i > 0) {
                        View gapView = factory.inflate(R.layout.form_group_divider, null);
                        viewsFromJson.add(gapView);
                    }

                    ViewGroup viewGroup = (ViewGroup) factory.inflate(R.layout.form_group, null);
                    JSONObject childJson = groups.getJSONObject(i);
                    try {
                        List<View> views = createFieldViews(jsonApi, factory, stepName, context, childJson, listener, editable, metaDataWatcher);
                        for (View view : views)
                            viewGroup.addView(view);

                    } catch (Exception e) {
                        Log.e(TAG,
                                "Exception occurred in making group view at index : " + i + " : Exception is : "
                                        + e.getMessage());
                    }
                    viewsFromJson.add(viewGroup);
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json exception occurred : " + e.getMessage());
        }
        return  viewsFromJson;
    }

    private List<View> createFieldViews(JsonApi jsonApi, LayoutInflater factory, String stepName, Context context, JSONObject parentJson, CommonListener listener, boolean editable, MetaDataWatcher metaDataWatcher) {
        List<View> viewsFromJson = new ArrayList<>();
        try {
            JSONArray fields = null;

            // LayoutInflater factory = LayoutInflater.from(context);

            try {
                fields = parentJson.getJSONArray("fields");
            }
            catch (Exception ex) {}

            if (null != fields) {
                int count = 0;
                for (int i = 0; i < fields.length(); i++) {
                    JSONObject childJson = fields.getJSONObject(i);
                    try {
                        FormWidgetFactory widgetFactory = map.get(childJson.getString("type"));

                        if (count > 0) {
                            View separator = factory.inflate(R.layout.form_separator, null);
                            viewsFromJson.add(separator);
                        }

                        List<View> views = widgetFactory.getViewsFromJson(jsonApi, stepName, context, childJson, listener, editable, metaDataWatcher);
                        if (!widgetFactory.getWidgetKey().equals(GroupTitleFactory.class.getSimpleName()))
                            ++count;

                        if (views.size() > 0) {
                            viewsFromJson.addAll(views);
                        }

                    } catch (Exception e) {
                        Log.e(TAG,
                                "Exception occurred in making child view at index : " + i + " : Exception is : "
                                        + e.getMessage());
                    }
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
