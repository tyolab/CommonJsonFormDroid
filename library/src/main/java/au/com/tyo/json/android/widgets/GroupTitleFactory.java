package au.com.tyo.json.android.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import au.com.tyo.json.android.R;
import au.com.tyo.json.android.interfaces.CommonListener;
import au.com.tyo.json.android.interfaces.JsonApi;
import au.com.tyo.json.android.interfaces.MetaDataWatcher;

public class GroupTitleFactory extends CommonItemFactory {
    @Override
    public List<View> getViewsFromJson(JsonApi jsonApi, String stepName, Context context, JSONObject jsonObject, CommonListener listener, boolean editable, MetaDataWatcher metaDataWatcher) throws Exception {
        List<View> views = new ArrayList<>(1);

        LayoutInflater factory = LayoutInflater.from(context);
        views.add(factory.inflate(R.layout.item_group_title, null));

        return views;
    }
}
