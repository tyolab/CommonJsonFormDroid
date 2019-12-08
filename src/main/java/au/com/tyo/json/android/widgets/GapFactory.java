package au.com.tyo.json.android.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import google.json.JSONObject;

import au.com.tyo.json.android.R;
import au.com.tyo.json.android.interfaces.CommonListener;
import au.com.tyo.json.android.interfaces.JsonApi;
import au.com.tyo.json.android.interfaces.MetaDataWatcher;
import au.com.tyo.json.android.utils.JsonMetadata;

public class GapFactory extends CommonItemFactory {

    public static final String NAME = GapFactory.class.getSimpleName();

    public GapFactory(String widgetKey) {
        super(widgetKey);
    }

    public GapFactory() {
        super(NAME);
    }

    @Override
    public View getViewFromJson(JsonApi jsonApi, String stepName, Context context, JSONObject jsonObject, JsonMetadata metadata, CommonListener listener, boolean editable, MetaDataWatcher metaDataWatcher) throws Exception {
        LayoutInflater factory = LayoutInflater.from(context);
        return (factory.inflate(R.layout.form_group_divider, null));
    }
}
