package au.com.tyo.json.android.widgets;

import android.view.LayoutInflater;
import android.view.View;

import org.json.JSONObject;

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
    protected View createView(JSONObject jsonObject, LayoutInflater factory) {
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

        return inflateViewForField(jsonObject, factory, resId);
    }

    // @Override
    // public View getViewFromJson(JsonApi jsonApi, String stepName, Context context, JSONObject jsonObject, JsonMetadata metadata, CommonListener listener, boolean editable, MetaDataWatcher metaDataWatcher) throws Exception {
    //
    //
    //     LayoutInflater factory = LayoutInflater.from(context);
    //
    //     int clickable = jsonObject.optInt(JsonFormField.ATTRIBUTE_NAME_CLICKABLE, 0);
    //
    //     View v = factory.inflate(resId, null);
    //
    //
    //     bindUserInput(jsonApi, v, jsonObject, -1, listener, editable, clickable, metaDataWatcher);
    //
    //     return (v);
    // }

}
