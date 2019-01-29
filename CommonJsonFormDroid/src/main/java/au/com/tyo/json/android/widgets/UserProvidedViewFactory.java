package au.com.tyo.json.android.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import org.json.JSONObject;

import au.com.tyo.json.android.interfaces.CommonListener;
import au.com.tyo.json.android.interfaces.JsonApi;
import au.com.tyo.json.android.interfaces.MetaDataWatcher;
import au.com.tyo.json.android.utils.JsonMetadata;

public class UserProvidedViewFactory extends CommonItemFactory {

    public UserProvidedViewFactory(String widgetKey) {
        super(widgetKey);
    }

    public UserProvidedViewFactory() {
        super(UserProvidedViewFactory.class.getSimpleName());
    }

    @Override
    public View getViewFromJson(JsonApi jsonApi, String stepName, Context context, JSONObject jsonObject, JsonMetadata metadata, CommonListener listener, boolean editable, MetaDataWatcher metaDataWatcher) throws Exception {
        int resId = jsonObject.getInt("value");
        LayoutInflater factory = LayoutInflater.from(context);

        int clickable = jsonObject.optInt("clickable", 0);

        View v = factory.inflate(resId, null);

        bindUserInput(jsonApi, v, jsonObject, -1, listener, editable, clickable, metaDataWatcher);

        return (v);
    }

}
