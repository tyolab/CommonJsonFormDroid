package au.com.tyo.json.android.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import org.json.JSONObject;

import au.com.tyo.json.android.interfaces.CommonListener;
import au.com.tyo.json.android.interfaces.JsonApi;
import au.com.tyo.json.android.interfaces.MetaDataWatcher;
import au.com.tyo.json.android.utils.JsonMetadata;
import au.com.tyo.json.jsonform.JsonFormField;

import static au.com.tyo.json.jsonform.JsonFormField.ATTRIBUTE_NAME_LAYOUT;

public class UserProvidedViewFactory extends CommonItemFactory {

    public static final String NAME = UserProvidedViewFactory.class.getSimpleName();

    public UserProvidedViewFactory(String widgetKey) {
        super(widgetKey);
    }

    public UserProvidedViewFactory() {
        super(NAME);
    }

    @Override
    public View getViewFromJson(JsonApi jsonApi, String stepName, Context context, JSONObject jsonObject, JsonMetadata metadata, CommonListener listener, boolean editable, MetaDataWatcher metaDataWatcher) throws Exception {
        Object value = jsonObject.opt("value");
        int resId = -1;
        if (value instanceof Integer)
            resId = (int) value;
        else if (value instanceof String) {
            try {
                resId = Integer.parseInt((String) value);
            }
            catch (Exception ex) {}
        }

        if (resId == -1)
            resId = jsonObject.optInt(ATTRIBUTE_NAME_LAYOUT, -1);

        if (resId == -1)
            throw new IllegalStateException("User provided view resource id can not be empty");

        LayoutInflater factory = LayoutInflater.from(context);

        int clickable = jsonObject.optInt(JsonFormField.ATTRIBUTE_NAME_CLICKABLE, 0);

        View v = factory.inflate(resId, null);

        bindUserInput(jsonApi, v, jsonObject, -1, listener, editable, clickable, metaDataWatcher);

        return (v);
    }

}
