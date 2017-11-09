package au.com.tyo.json.android.widgets;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vijay.jsonwizard.R;
import au.com.tyo.json.android.interfaces.CommonListener;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 21/9/17.
 */

public abstract class CompoundItemFactory extends UserInputItemFactory {

    @Override
    protected View createView(LayoutInflater factory, ViewGroup parent, String stepName, JSONObject jsonObject, CommonListener listener, boolean editable) throws JSONException {
        ViewGroup v = (ViewGroup) factory.inflate(R.layout.item_compound, null);
        createCompoundView(factory, v, stepName, jsonObject, listener, editable);
        return v;
    }

    protected abstract void createCompoundView(LayoutInflater factory, ViewGroup v, String stepName, JSONObject jsonObject, CommonListener listener, boolean editable) throws JSONException;
}
