package au.com.tyo.json.android.interfaces;

import android.content.Context;
import android.view.View;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import au.com.tyo.json.android.R;
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
import au.com.tyo.json.android.widgets.TitledButtonFactory;
import au.com.tyo.json.android.widgets.TitledClickableLabelFactory;
import au.com.tyo.json.android.widgets.TitledEditTextFactory;
import au.com.tyo.json.android.widgets.TitledImageFactory;
import au.com.tyo.json.android.widgets.TitledLabelFactory;
import au.com.tyo.json.android.widgets.TitledSwitchButtonFactory;
import au.com.tyo.json.android.widgets.UserProvidedViewFactory;
import au.com.tyo.json.jsonform.JsonFormField;

/**
 * Created by vijay on 24-05-2015.
 */
public abstract class FormWidgetFactory {

    private static final Map<String, FormWidgetFactory> map                     = new HashMap<>();

    private static final TitledEditTextFactory titledTextFactory                = new TitledEditTextFactory();
    private static final TitledLabelFactory titledLabelFactory                  = new TitledLabelFactory();
    private static final TitledSwitchButtonFactory titledSwitchButtonFactory    = new TitledSwitchButtonFactory();
    private static final UserProvidedViewFactory userProvidedViewFactory        = new UserProvidedViewFactory();

    /**
     * Widget Key, for identifying the which Widget Factory to use
     */
    private String widgetKey;

    public static Map<String, FormWidgetFactory> registerWidgets() {
        registerWidget(new EditTextFactory());
        registerWidget(new LabelFactory());
        registerWidget(new ButtonFactory());
        registerWidget(new ImageFactory());
        registerWidget(new CheckBoxFactory());
        registerWidget(new RadioButtonFactory());
        registerWidget(new ImagePickerFactory());
        registerWidget(new SpinnerFactory());
        registerWidget(new GapFactory());

        FormWidgetFactory.registerWidget(new TitledButtonFactory());
        FormWidgetFactory.registerWidget(new TitledClickableLabelFactory());

        FormWidgetFactory.registerWidget(new GroupTitleFactory());
        FormWidgetFactory.registerWidget(new TitledImageFactory());

        FormWidgetFactory.registerWidget(titledLabelFactory);
        FormWidgetFactory.registerWidget(titledTextFactory);
        FormWidgetFactory.registerWidget(titledSwitchButtonFactory);
        FormWidgetFactory.registerWidget(userProvidedViewFactory);
        return map;
    }

    public static <T extends FormWidgetFactory> void registerWidget(Class<T> tClass) throws InstantiationException, IllegalAccessException {
        registerWidget(tClass.newInstance());
    }

    public static void registerWidget(String key, FormWidgetFactory factory) {
        map.put(key, factory);
    }

    public static void registerWidget(FormWidgetFactory factory) {
        map.put(factory.getWidgetKey(), factory);
    }

    public static void setViewTags(View v, JSONObject jsonObject) {
        setViewTagKey(v, jsonObject.optString(JsonFormField.ATTRIBUTE_NAME_KEY), jsonObject.optString(JsonFormField.ATTRIBUTE_NAME_TYPE));
    }

    public static void setViewTagKey(View v, String key) {
        v.setTag(R.id.key, key);
    }

    public static void setViewTagKey(View v, String key, String type) {
        if (null != key)
            v.setTag(R.id.key, key);

        if (null != type)
            v.setTag(R.id.type, type);
    }

    public static void setFieldTags(View v, JsonMetadata metadata) {
        v.setTag(R.id.key, metadata.key);
        v.setTag(R.id.required, metadata.required);
    }

    public static void setViewTags(View v, JsonMetadata metadata) {
        v.setTag(R.id.key, metadata.key);
        v.setTag(R.id.type, metadata.type);
        v.setTag(R.id.required, metadata.required);
    }

    public FormWidgetFactory(String widgetKey) {
        this.widgetKey = widgetKey;
    }

    public FormWidgetFactory() {
        this.widgetKey = this.getClass().getSimpleName();
    }

    public String getWidgetKey() {
        return widgetKey;
    }

    public abstract View getViewFromJson(JsonApi jsonApi, String stepName, Context context, JSONObject jsonObject, JsonMetadata metadata, CommonListener listener, boolean editable, MetaDataWatcher metaDataWatcher) throws Exception;

}
