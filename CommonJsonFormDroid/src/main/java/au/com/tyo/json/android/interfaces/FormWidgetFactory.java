package au.com.tyo.json.android.interfaces;

import android.content.Context;
import android.os.Build;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import au.com.tyo.android.AndroidUtils;
import au.com.tyo.json.android.R;
import au.com.tyo.json.android.utils.JsonMetadata;

/**
 * Created by vijay on 24-05-2015.
 */
public abstract class FormWidgetFactory {

    /**
     * Widget Key, for identifying the which Widget Factory to use
     */
    private String widgetKey;

    public static void setUserInputViewTags(View v, JSONObject jsonObject) throws JSONException {
        setUserInputViewTags(v, new JsonMetadata(jsonObject));
    }

    public static void setViewTagKey(View v, String key) {
        v.setTag(R.id.key, key);
    }

    public static void setViewTagKey(View v, String key, String type) {
        v.setTag(R.id.key, key);
        v.setTag(R.id.type, type);
    }

    public static void setFieldTags(View v, JsonMetadata metadata) {
        v.setTag(R.id.key, metadata.key);
        v.setTag(R.id.required, metadata.required);
    }

    public static void setUserInputViewTags(View v, JsonMetadata metadata) {
        v.setTag(R.id.key, metadata.key);
        v.setTag(R.id.type, metadata.type);
        v.setTag(R.id.required, metadata.required);
    }

    public FormWidgetFactory(String widgetKey) {
        this.widgetKey = widgetKey;
    }

    public FormWidgetFactory() {
        this.widgetKey = this.getClass().getSimpleName();
    }

    public String getWidgetKey() {
        return widgetKey;
    }

    public abstract View getViewFromJson(JsonApi jsonApi, String stepName, Context context, JSONObject jsonObject, JsonMetadata metadata, CommonListener listener, boolean editable, MetaDataWatcher metaDataWatcher) throws Exception;

}
