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

import android.view.Gravity;
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

public abstract class TitledItemFactory extends UserInputItemFactory {

    public TitledItemFactory(String widgetKey) {
        super(widgetKey);
    }

    protected abstract View createUserInputView(JsonApi jsonApi, LayoutInflater factory, ViewGroup parent, String stepName, JSONObject jsonObject, CommonListener listener, boolean editable, int gravity, MetaDataWatcher metaDataWatcher) throws JSONException;

    @Override
    protected View createView(JsonApi jsonApi, LayoutInflater factory, ViewGroup parent, String stepName, JSONObject jsonObject, CommonListener listener, boolean editable, MetaDataWatcher metaDataWatcher) throws JSONException {
        ViewGroup v;

        boolean vertical = false;
        if (this instanceof CompoundItemFactory ||
            jsonObject.has("orientation") && jsonObject.getString("orientation").equals("vertical")) {
            v = (ViewGroup) factory.inflate(R.layout.form_item_two_rows, parent, false);
            vertical = true;
        }
        else
            v = (ViewGroup) factory.inflate(R.layout.form_item_two_cols, parent, false);

        // 1st Column / Row
        bindTitle(v, jsonObject, "title");

        // 2nd Column / Row
        // if it is aligned vertically, we adjust form to the "left"
        ViewGroup container = (ViewGroup) v.findViewById(R.id.frame2);
        View child = createUserInputView(jsonApi, factory, v, stepName, jsonObject, listener, editable, vertical ? Gravity.LEFT : Gravity.RIGHT, metaDataWatcher);

        View userInputView = child.findViewById(R.id.user_input);
        final String keyStr = jsonObject.getString("key");
        if (null != userInputView)
            metaDataWatcher.setUserInputView(keyStr, userInputView);
        else
            metaDataWatcher.setUserInputView(keyStr, child);

        container.addView(child);

        return v;
    }
}
