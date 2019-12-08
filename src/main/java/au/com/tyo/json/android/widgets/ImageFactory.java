/*
 * Copyright (c) 2019. TYONLINE TECHNOLOGY PTY. LTD. (TYOLAB)
 *
 */

package au.com.tyo.json.android.widgets;

import android.content.Context;
import android.view.View;

import google.json.JSONException;
import google.json.JSONObject;

import au.com.tyo.json.android.R;
import au.com.tyo.json.android.interfaces.CommonListener;
import au.com.tyo.json.android.interfaces.JsonApi;
import au.com.tyo.json.android.interfaces.MetaDataWatcher;
import au.com.tyo.json.android.utils.JsonMetadata;

import static au.com.tyo.json.jsonform.JsonFormField.CLICKABLE_ROW;

public class ImageFactory extends CommonItemFactory {

    public static final String NAME = ImageFactory.class.getSimpleName();

    public ImageFactory(String widgetKey) {
        super(widgetKey);
        setLayoutResourceId(R.layout.item_imageview);
    }

    public ImageFactory() {
        setLayoutResourceId(R.layout.item_imageview);
    }

    @Override
    public View getViewFromJson(JsonApi jsonApi, String stepName, Context context, JSONObject jsonObject, JsonMetadata metadata, CommonListener listener, boolean editable, MetaDataWatcher metaDataWatcher) throws Exception {
        return super.getViewFromJson(jsonApi, stepName, context, jsonObject, metadata, listener, editable, metaDataWatcher);
    }

    @Override
    protected void bindDataAndAction(View parent, JsonApi jsonApi, JSONObject jsonObject, boolean editable, CommonListener listener, MetaDataWatcher metaDataWatcher) throws JSONException {
        bindUserInput(jsonApi, parent, jsonObject, 0, listener, editable, CLICKABLE_ROW, false, metaDataWatcher);
    }

}
