package au.com.tyo.json.android.widgets;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import au.com.tyo.json.android.interfaces.CommonListener;
import au.com.tyo.json.android.interfaces.JsonApi;
import au.com.tyo.json.android.interfaces.MetaDataWatcher;
import au.com.tyo.json.android.utils.JsonMetadata;

public class TitledUserProvidedViewFactory extends TitledItemFactory {

    public TitledUserProvidedViewFactory(String widgetKey) {
        super(widgetKey);
    }

    public TitledUserProvidedViewFactory() {
        super(TitledUserProvidedViewFactory.class.getSimpleName());
    }

    @Override
    protected View createUserInputView(JsonApi jsonApi, LayoutInflater factory, ViewGroup parent, String stepName, JSONObject jsonObject, JsonMetadata metadata, CommonListener listener, boolean editable, int gravity, MetaDataWatcher metaDataWatcher) throws JSONException {

        int resId = jsonObject.getInt("value");

        View v = factory.inflate(resId, null);

        bindUserInput(v, jsonObject, -1, listener, editable, metaDataWatcher);

        return v;
    }

}
