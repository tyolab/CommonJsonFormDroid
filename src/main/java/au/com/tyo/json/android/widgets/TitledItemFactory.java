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

import android.content.res.ColorStateList;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import google.json.JSONException;
import google.json.JSONObject;

import au.com.tyo.json.android.R;
import au.com.tyo.json.android.interfaces.CommonListener;
import au.com.tyo.json.android.interfaces.JsonApi;
import au.com.tyo.json.android.interfaces.MetaDataWatcher;
import au.com.tyo.json.android.utils.JsonMetadata;
import au.com.tyo.json.android.views.OptionalButton;
import au.com.tyo.json.jsonform.JsonFormField;

import static au.com.tyo.json.jsonform.JsonFormField.VALUE_REQUIRED;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 21/9/17.
 */

/**
 * This class is for creating a titled row
 */
public class TitledItemFactory extends UserInputItemFactory {

    public static final String NAME = TitledItemFactory.class.getSimpleName();

    public TitledItemFactory(String widgetKey) {
        super(widgetKey);
    }

    public TitledItemFactory() {
        super(NAME);
    }

    protected View createUserInputView(JsonApi jsonApi, LayoutInflater factory, ViewGroup parent, String stepName, JSONObject jsonObject, JsonMetadata metadata, CommonListener listener, boolean editable, int clickable, int gravity, MetaDataWatcher metaDataWatcher) throws JSONException {
        return null;
    }

    @Override
    protected View createView(JsonApi jsonApi, LayoutInflater factory, ViewGroup parent, String stepName, JSONObject jsonObject, JsonMetadata metadata, CommonListener listener, boolean editable, int clickable, MetaDataWatcher metaDataWatcher) throws JSONException {
        ViewGroup v;
        ViewGroup container;

        boolean vertical = false;
        boolean needTitle = true;

        boolean enabled;
        if (editable) {
            enabled = jsonObject.optBoolean(JsonFormField.ATTRIBUTE_NAME_ENABLED, editable);
        }
        else
            enabled = editable;

        if (this instanceof CompoundItemFactory) {
            vertical = true;
            needTitle = false;
        }
        else
            vertical = jsonObject.optString(JsonFormField.ATTRIBUTE_NAME_ORIENTATION, "horizontal").equals("vertical");

        if (needTitle) {
            if (vertical)
                v = (ViewGroup) factory.inflate(R.layout.form_item_two_rows, parent, false);
            else
                v = (ViewGroup) factory.inflate(R.layout.form_item_two_cols, parent, false);

            // 1st Column / Row
            bindTitle(v, jsonObject, JsonFormField.ATTRIBUTE_NAME_TITLE, android.R.id.text1);
            bindTitle(v, jsonObject, JsonFormField.ATTRIBUTE_NAME_SUB_TITLE, android.R.id.text2);

            container = v.findViewById(R.id.frame2);
        }
        else {
            container = parent;
            v = null;
        }
        // 2nd Column / Row
        // if it is aligned vertically, we adjust form to the "left"

        View child = createUserInputView(jsonApi, factory, v, stepName, jsonObject, metadata, listener, editable, clickable, vertical ? Gravity.LEFT : Gravity.RIGHT, metaDataWatcher);

        View userInputView = null;
        if (null != child) {

            if (!vertical) {
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.gravity = Gravity.RIGHT;
                child.setLayoutParams(layoutParams);
            }

            container.addView(child);

            userInputView = child.findViewById(R.id.user_input);

            if (null != userInputView) {
                setViewTags(userInputView, metadata);

                userInputView.setEnabled(enabled);
            }
        }

        if (null != metaDataWatcher)
            metaDataWatcher.setKeyInputView(metadata.key, userInputView, enabled, editable, metadata.required);

        return v;
    }

    /**
     * Important:
     *
     * Clear button has
     *
     * @param metadata
     * @param view
     * @param text
     * @param listener
     */
    public static void setupOptionalButton(JsonMetadata metadata, View view, String text, CommonListener listener) {
        OptionalButton optionalButton;
        optionalButton = view.findViewById(R.id.btn_clearable);
        setViewTagKey(optionalButton, metadata.key);
        showHideOptionalClearButton(optionalButton, text, metadata.required);

        if (null != listener)
            optionalButton.setOnClickListener(listener);
    }

    public static void showHideOptionalClearButton(View view, String text, int required) {
        if (required != VALUE_REQUIRED) {
            OptionalButton optionalButton;
            optionalButton = view.findViewById(R.id.btn_clearable);

            showHideOptionalClearButton(optionalButton, text, required);
        }
    }

    public static void showHideOptionalClearButton(OptionalButton optionalButton, String text, int required) {
        if (required != VALUE_REQUIRED) {
            if (null != optionalButton) {

                if (TextUtils.isEmpty(text)) {
                    optionalButton.hideClearButton();
                }
                else {
                    optionalButton.showClearButton();
                }
            }
        }
    }

    @Override
    public void updateView(JsonApi jsonApi, View view, String targetKey, Object value, ColorStateList fieldTextColors) {
        super.updateView(jsonApi, view, targetKey, value, fieldTextColors);

        if (null != view) {
            int required = (int) view.getTag(R.id.required);

            showHideOptionalClearButton(view, null != value ? value.toString() : "", required);
        }
    }
}
