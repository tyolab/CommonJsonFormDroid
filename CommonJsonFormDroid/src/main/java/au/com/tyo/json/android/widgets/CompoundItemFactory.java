package au.com.tyo.json.android.widgets;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import google.json.JSONException;
import google.json.JSONObject;

import au.com.tyo.json.android.R;
import au.com.tyo.json.android.interfaces.CommonListener;
import au.com.tyo.json.android.interfaces.JsonApi;
import au.com.tyo.json.android.interfaces.MetaDataWatcher;
import au.com.tyo.json.android.utils.JsonMetadata;


/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 21/9/17.
 */

public abstract class CompoundItemFactory extends TitledItemFactory {

    public static final String NAME = CompoundItemFactory.class.getSimpleName();

    public CompoundItemFactory(String widgetKey) {
        super(widgetKey);
    }

    public CompoundItemFactory() {
        super(NAME);
    }

    @Override
    protected View createUserInputView(JsonApi jsonApi, LayoutInflater factory, ViewGroup parent, String stepName, JSONObject jsonObject, JsonMetadata metadata, CommonListener listener, boolean editable, int clickable, int gravity, MetaDataWatcher metaDataWatcher) throws JSONException {
        ViewGroup v = (ViewGroup) factory.inflate(R.layout.item_compound, null);

        createCompoundView(factory, v, stepName, jsonObject, listener, editable);

        return v;
    }

    protected abstract void createCompoundView(LayoutInflater factory, ViewGroup parent, String stepName, JSONObject jsonObject, CommonListener listener, boolean editable) throws JSONException;
}
