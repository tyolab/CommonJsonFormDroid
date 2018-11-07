package au.com.tyo.json.android.widgets;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import au.com.tyo.json.android.R;
import au.com.tyo.json.android.interfaces.CommonListener;
import au.com.tyo.json.android.interfaces.FormWidgetFactory;
import au.com.tyo.json.android.interfaces.JsonApi;
import au.com.tyo.json.android.utils.JsonMetadata;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 26/7/17.
 */

public abstract class CommonItemFactory implements FormWidgetFactory {

    protected void setViewTags(View v, JSONObject jsonObject) throws JSONException {
        setViewTags(v, new JsonMetadata(jsonObject));
    }

    protected void setViewTags(View v, JsonMetadata metadata) {
        v.setTag(R.id.key, metadata.key);
        v.setTag(R.id.type, metadata.type);
        v.setTag(R.id.required, metadata.required);
    }

}
