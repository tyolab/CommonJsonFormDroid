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
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import au.com.tyo.common.ui.CardBox;
import au.com.tyo.json.android.R;
import au.com.tyo.json.android.interfaces.CommonListener;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 22/12/17.
 */

public class ImageBoxFactory extends CompoundItemFactory {

    @Override
    protected void createCompoundView(LayoutInflater factory, ViewGroup v, String stepName, JSONObject jsonObject, CommonListener listener, boolean editable) throws JSONException {
        String imageUrl = null;

        CardBox cardBox = (CardBox) factory.inflate(R.layout.item_card_box, null);

        Object value = jsonObject.get("value");

        if (value instanceof JSONArray) {
            JSONArray array = (JSONArray) value;

            for (int i = 0; i < array.length(); ++i) {
                imageUrl = array.getString(i);
                cardBox.addPreviewItem(imageUrl);
            }
        }
        else if (value instanceof JSONObject) {
            imageUrl = value.toString();
            cardBox.addPreviewItem(imageUrl);
        }

        v.addView(cardBox);
    }

}
