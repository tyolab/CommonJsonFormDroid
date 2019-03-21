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

import au.com.tyo.json.Config;
import au.com.tyo.json.android.R;
import au.com.tyo.json.android.interfaces.CommonListener;
import au.com.tyo.json.android.interfaces.FormWidgetFactory;
import au.com.tyo.json.android.interfaces.JsonApi;
import au.com.tyo.json.android.interfaces.MetaDataWatcher;
import au.com.tyo.json.android.utils.JsonMetadata;
import au.com.tyo.json.android.widgets.ButtonFactory;
import au.com.tyo.json.android.widgets.CheckBoxFactory;
import au.com.tyo.json.android.widgets.EditTextFactory;
import au.com.tyo.json.android.widgets.GapFactory;
import au.com.tyo.json.android.widgets.GroupTitleFactory;
import au.com.tyo.json.android.widgets.ImageFactory;
import au.com.tyo.json.android.widgets.ImagePickerFactory;
import au.com.tyo.json.android.widgets.LabelFactory;
import au.com.tyo.json.android.widgets.RadioButtonFactory;
import au.com.tyo.json.android.widgets.SpinnerFactory;
import au.com.tyo.json.jsonform.JsonFormField;

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
        registerWidget(new EditTextFactory());
        registerWidget(new LabelFactory());
        registerWidget(new ButtonFactory());
        registerWidget(new ImageFactory());
        registerWidget(new CheckBoxFactory());
        registerWidget(new RadioButtonFactory());
        registerWidget(new ImagePickerFactory());
        registerWidget(new SpinnerFactory());
        registerWidget(new GapFactory());
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
        int value;
        View view = null;
        try {
            value = parentJson.optInt(name, -1);

            //if (jsonObject.has("value"))
            if (value > 0)
                view = factory.inflate(value, null); //.getInt("value"), null);

        } catch (Exception e) {
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

                    ViewGroup groupContainer = (ViewGroup) factory.inflate(R.layout.form_group_container, null);

                    JSONObject childJson = groups.getJSONObject(i);
                    try {
                        List<View> views = createFieldViews(jsonApi, factory, stepName, context, childJson, listener, editable, metaDataWatcher);
                        if (views.size() > 0) {
                            ViewGroup viewGroup = (ViewGroup) factory.inflate(R.layout.form_group, null);
                            for (View view : views)
                                viewGroup.addView(view);
                            groupContainer.addView(viewGroup);
                        }

                        if (groups.length() > 1 && i < (groups.length() - 1)) {
                            View gapView = factory.inflate(R.layout.form_group_divider, null);
                            groupContainer.addView(gapView);
                        }

                        /**
                         * Set Group view key tag
                         */
                        FormWidgetFactory.setViewTags(groupContainer, childJson);

                        viewsFromJson.add(groupContainer);
                    } catch (Exception e) {
                        Log.e(TAG,
                                "Exception occurred in making group view at index : " + i + "", e);
                    }
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json exception occurred", e);
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
                        String widgetType = childJson.getString("type");
                        FormWidgetFactory widgetFactory = map.get(widgetType);

                        if (null == widgetFactory)
                            throw new IllegalStateException("Unknown widget type: " + widgetType);


                        ++count; // count each field including group title and others

                        /**
                         * ### Add widget
                         * // a ViewGroups with lots of children
                         */
                        JsonMetadata metadata = new JsonMetadata(childJson);
                        View views = widgetFactory.getViewFromJson(jsonApi, stepName, context, childJson, metadata, listener, editable, metaDataWatcher);
                        FormWidgetFactory.setFieldTags(views, metadata);
                        metaDataWatcher.addFieldView(metadata.key, views);

                        viewsFromJson.add(views);

                        /**
                         * ## Add separator
                         *
                         * Group separator is not considered, if it is really necessary,
                         * then override the group title layout and add the separator there
                         */
                        if (!widgetType.equals(GroupTitleFactory.class.getSimpleName())) {
                            boolean needSeparator = childJson.optBoolean(JsonFormField.ATTRIBUTE_NAME_SEPARATOR_UNDER, Config.formWithSeparator);
                            if (needSeparator && count < fields.length()) {
                                View separator = factory.inflate(R.layout.form_separator, null);
                                viewsFromJson.add(separator);
                            }
                        }

                    } catch (Exception e) {
                        Log.e(TAG,
                                "Exception occurred in making child view at index : " + i + "", e);
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