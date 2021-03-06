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

public class GroupTitleFactory extends CommonItemFactory {

    public static final String NAME = GroupTitleFactory.class.getSimpleName();

    public GroupTitleFactory(String widgetKey) {
        super(widgetKey);
    }

    public GroupTitleFactory() {
        super(NAME);
    }

    @Override
    public View getViewFromJson(JsonApi jsonApi, String stepName, Context context, JSONObject jsonObject, JsonMetadata metadata, CommonListener listener, boolean editable, MetaDataWatcher metaDataWatcher) throws Exception {

        LayoutInflater factory = LayoutInflater.from(context);

        View v = inflateViewForField(jsonObject, factory, R.layout.item_group_title);

        bindTitle(v, jsonObject, "value", android.R.id.text1);

        return v;
    }
}
