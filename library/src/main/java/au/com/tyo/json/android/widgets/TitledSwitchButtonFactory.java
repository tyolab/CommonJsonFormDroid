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

import com.rey.material.widget.Switch;

import org.json.JSONException;
import org.json.JSONObject;

import au.com.tyo.json.android.R;
import au.com.tyo.json.android.interfaces.CommonListener;
import au.com.tyo.json.android.interfaces.JsonApi;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 26/7/17.
 */

public class TitledSwitchButtonFactory extends TitledItemFactory {

    public static final String KEY = TitledSwitchButtonFactory.class.getSimpleName();

    @Override
    protected View createUserInputView(final JsonApi jsonApi, LayoutInflater factory, ViewGroup parent, final String stepName, JSONObject jsonObject, CommonListener listener, boolean editable, int gravity) throws JSONException {
        String value = jsonObject.getString("value");
        final String key = jsonObject.getString("key");
        boolean checked = Boolean.parseBoolean(value);

        View v = factory.inflate(R.layout.item_switch_button, null);

        Switch switchButton = (Switch) v.findViewById(R.id.user_input);

        if (!editable) {
            switchButton.setEnabled(false);
            switchButton.setClickable(false);
        }

        switchButton.setChecked(checked);
        switchButton.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean checked) {
                try {
                    jsonApi.writeValue(stepName, key, String.valueOf(checked));
                } catch (JSONException e) {

                }
            }
        });

        setViewTags(switchButton, jsonObject);
        return v;
    }

}
