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
import au.com.tyo.json.android.customviews.RadioButton;
import au.com.tyo.json.android.interfaces.CommonListener;
import au.com.tyo.json.jsonform.JsonFormField;

/**
 * Created by vijay on 24-05-2015.
 */
public class RadioButtonFactory extends CompoundItemFactory {

    public static final String NAME = ;

    public RadioButtonFactory(String widgetKey) {
        super(widgetKey);
    }

    public RadioButtonFactory() {

    }

    /**
     *
     *
     * @param factory
     * @param parent
     * @param stepName
     * @param jsonObject
     * @param listener
     * @param editable
     * @throws JSONException
     */
    @Override
    protected void createCompoundView(LayoutInflater factory, ViewGroup parent, String stepName, JSONObject jsonObject, CommonListener listener, boolean editable) throws JSONException {

        View titleView = createTitleView(factory, jsonObject, "label", R.layout.item_compound_title);
        parent.addView(titleView);

        String parentKey = jsonObject.getString("key");
        Object value = jsonObject.opt("value");

        JSONArray options = jsonObject.getJSONArray(JsonFormConstants.OPTIONS_FIELD_NAME);
        for (int i = 0; i < options.length(); i++) {
            JSONObject item = options.getJSONObject(i);
            View view = inflateViewForField(jsonObject, factory, R.layout.item_radiobutton); //factory.inflate(R.layout.item_radiobutton,null);

            String childKey = item.getString("key");
            Object childValue = item.opt("value");
            if (null == childValue)
                childValue = childKey;

            RadioButton radioButton = (RadioButton) view; // (RadioButton) view.findViewById(R.id.user_input);
            radioButton.setText(item.getString("text"));
            radioButton.setTag(R.id.key, parentKey);
            radioButton.setTag(R.id.type, jsonObject.getString("type"));
            radioButton.setTag(R.id.childKey, childKey);
            radioButton.setGravity(Gravity.CENTER_VERTICAL);
            // radioButton.setTextSize(16);
            /// radioButton.setTypeface(Typeface.createFromAsset(context.getAssets(), Resources.FONT_DEFAULT_PATH));
            radioButton.setOnCheckedChangeListener(listener);

            boolean enabled = item.optBoolean(JsonFormField.ATTRIBUTE_NAME_ENABLED, true);
            radioButton.setEnabled(enabled);

            /**
             * For recording the value of radio button to be used when it gets changed
             */
            listener.onInitialValueSet(childKey, null, childValue);

            if (null != childValue && null != value && childValue.equals(value)) {
                // radio button only keep one value
                listener.onInitialValueSet(parentKey, null, childValue);

                radioButton.setChecked(true);
            }

            parent.addView(view);
        }
    }

    /**
     * Extra setup
     */
    /*
           if (i == options.length() - 1) {
           radioButton.setLayoutParams(JsonFormUtils.getRelativeLayoutParams(MATCH_PARENT, WRAP_CONTENT, 0, 0, 0, (int) context
                   .getResources().getDimension(R.dimen.extra_bottom_margin)));
       }
     */
}
