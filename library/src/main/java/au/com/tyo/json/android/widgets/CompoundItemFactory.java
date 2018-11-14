package au.com.tyo.json.android.widgets;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import au.com.tyo.json.android.R;
import au.com.tyo.json.android.interfaces.CommonListener;
import au.com.tyo.json.android.interfaces.JsonApi;
import au.com.tyo.json.android.interfaces.MetaDataWatcher;


/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 21/9/17.
 */

public abstract class CompoundItemFactory extends TitledItemFactory {

    @Override
    protected View createUserInputView(JsonApi jsonApi, LayoutInflater factory, ViewGroup parent, String stepName, JSONObject jsonObject, CommonListener listener, boolean editable, int gravity, MetaDataWatcher metaDataWatcher) throws JSONException {
        ViewGroup v = (ViewGroup) factory.inflate(R.layout.item_compound, null);

        final String key = jsonObject.getString("key");
        createCompoundView(factory, v, stepName, jsonObject, listener, editable);
        metaDataWatcher.setUserInputView(key, (View) v);

        return v;
    }

    protected abstract void createCompoundView(LayoutInflater factory, ViewGroup parent, String stepName, JSONObject jsonObject, CommonListener listener, boolean editable) throws JSONException;
}
