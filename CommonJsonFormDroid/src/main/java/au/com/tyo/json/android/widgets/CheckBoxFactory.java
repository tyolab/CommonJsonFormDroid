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

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import au.com.tyo.json.android.R;
import au.com.tyo.json.android.constants.JsonFormConstants;
import au.com.tyo.json.android.customviews.CheckBox;
import au.com.tyo.json.android.interfaces.CommonListener;

/**
 * Created by vijay on 24-05-2015.
 */
public class CheckBoxFactory extends CompoundItemFactory {

    public CheckBoxFactory(String widgetKey) {
        super(widgetKey);
    }

    public CheckBoxFactory() {
        super();
    }

    @Override
    protected void createCompoundView(LayoutInflater factory, ViewGroup parent, String stepName, JSONObject jsonObject, CommonListener listener, boolean editable) throws JSONException {

        View titleView = createTitleView(factory, jsonObject, "label");
        parent.addView(titleView);

        String parentKey = jsonObject.getString("key");

        JSONArray options = jsonObject.getJSONArray(JsonFormConstants.OPTIONS_FIELD_NAME);
        for (int i = 0; i < options.length(); i++) {
            JSONObject item = options.getJSONObject(i);
            View view = inflateViewForField(jsonObject, factory, R.layout.item_checkbox); //factory.inflate(R.layout.item_checkbox, null);
            CheckBox checkBox = (CheckBox) view; // (CheckBox) view.findViewById(R.id.user_input);

            String childKey = item.getString("key");
            String value = item.optString("value");

            checkBox.setText(item.getString("text"));
            checkBox.setTag(R.id.key, parentKey);
            checkBox.setTag(R.id.type, jsonObject.getString("type"));
            checkBox.setTag(R.id.childKey, childKey);
            checkBox.setGravity(Gravity.CENTER_VERTICAL);
            checkBox.setTextSize(16);
            // checkBox.setTypeface(Typeface.createFromAsset(context.getAssets(), Resources.FONT_DEFAULT_PATH));
            checkBox.setOnCheckedChangeListener(listener);
            if (!TextUtils.isEmpty(value)) {
                checkBox.setChecked(Boolean.valueOf(value));
                listener.onInitialValueSet(parentKey, childKey, value);
            }

            /**
             * TODO
             *
             * make it optional maybe
             */
//            if (i == options.length() - 1) {
//                checkBox.setLayoutParams(JsonFormUtils.getRelativeLayoutParams(MATCH_PARENT, WRAP_CONTENT, 0, 0, 0, (int) context
//                        .getResources().getDimension(R.dimen.extra_bottom_margin)));
//            }

            parent.addView(view);
        }
    }
}
