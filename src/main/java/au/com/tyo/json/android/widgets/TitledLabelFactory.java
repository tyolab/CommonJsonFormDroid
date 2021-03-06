/*
 * Copyright (c) 2017 TYONLINE TECHNOLOGY PTY. LTD. (TYO Lab)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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
import au.com.tyo.json.jsonform.JsonFormField;

/**
 * Created by vijay on 24-05-2015.
 */
public class TitledLabelFactory extends TitledItemFactory {

    public static final String NAME = TitledLabelFactory.class.getSimpleName();

    public static final String KEY = NAME;

    private static final String TAG = NAME;

    public TitledLabelFactory(String widgetKey) {
        super(widgetKey);
    }

    public TitledLabelFactory() {
        super(NAME);
    }

    @Override
    protected View createUserInputView(JsonApi jsonApi, LayoutInflater factory, ViewGroup parent, String stepName, JSONObject jsonObject, JsonMetadata metadata, CommonListener listener, boolean editable, int clickable, int gravity, MetaDataWatcher metaDataWatcher) throws JSONException {

        View v = inflateViewForField(jsonObject, factory, R.layout.item_label, editable);

        boolean scrollable = jsonObject.optBoolean(JsonFormField.ATTRIBUTE_NAME_SCROLLABLE, false);

        bindUserInput(jsonApi, v, jsonObject, gravity, listener, editable, clickable, scrollable, metaDataWatcher);

        adjustViewClickable(v, jsonObject, listener, JsonFormField.CLICKABLE_FIELD);

        return v;
    }

}
