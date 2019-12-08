package au.com.tyo.json.android.widgets;

import android.view.LayoutInflater;
import android.view.View;

import google.json.JSONException;
import google.json.JSONObject;

import au.com.tyo.json.android.interfaces.CommonListener;
import au.com.tyo.json.android.interfaces.JsonApi;
import au.com.tyo.json.android.interfaces.MetaDataWatcher;
import au.com.tyo.json.android.utils.JsonMetadata;

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
    protected View createView(JsonApi jsonApi, JSONObject jsonObject, LayoutInflater factory, JsonMetadata metadata, CommonListener listener, boolean editable, MetaDataWatcher metaDataWatcher) throws JSONException {
        /**
         * The value is for user input
         */
        int resId = jsonObject.optInt(ATTRIBUTE_NAME_LAYOUT, -1);

        if (resId == -1)
            throw new IllegalStateException("User provided view resource id can not be empty");

        View userProvided = inflateViewForField(jsonObject, factory, resId);

        bindDataAndAction(userProvided, jsonApi, jsonObject, editable, listener, metaDataWatcher);

        return userProvided;
    }

}
