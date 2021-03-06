package au.com.tyo.json.android.widgets;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import google.json.JSONException;
import google.json.JSONObject;

import au.com.tyo.json.android.interfaces.CommonListener;
import au.com.tyo.json.android.interfaces.JsonApi;
import au.com.tyo.json.android.interfaces.MetaDataWatcher;
import au.com.tyo.json.android.utils.JsonMetadata;

public class TitledUserProvidedViewFactory extends TitledItemFactory {

    public static final String NAME = TitledUserProvidedViewFactory.class.getSimpleName();

    public TitledUserProvidedViewFactory(String widgetKey) {
        super(widgetKey);
    }

    public TitledUserProvidedViewFactory() {
        super(NAME);
    }

    @Override
    protected View createUserInputView(JsonApi jsonApi, LayoutInflater factory, ViewGroup parent, String stepName, JSONObject jsonObject, JsonMetadata metadata, CommonListener listener, boolean editable, int clickable, int gravity, MetaDataWatcher metaDataWatcher) throws JSONException {

        int resId = jsonObject.getInt("value");

        View v = factory.inflate(resId, null);

        bindUserInput(jsonApi, v, jsonObject, -1, listener, editable, clickable, false, metaDataWatcher);

        return v;
    }

}
