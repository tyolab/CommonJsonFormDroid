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

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 22/12/17.
 */

public abstract class ImageBoxFactory extends CompoundItemFactory {

    /**
     * For those who need cardbox widget, just comment off the following
     * and include both CommandUiLib and CommonFormDroid libraries in the gradle file
     */
        /*
    @Override
    protected void createCompoundView(LayoutInflater factory, ViewGroup v, String stepName, JSONObject jsonObject, CommonListener listener, boolean editable) throws JSONException {
        String imageUrl = null;

        View container = factory.inflate(R.layout.item_image_box, null);


        CardBox cardBox = (CardBox) container.findViewById(R.id.user_input);
        cardBox.setClickable(true);
        cardBox.setOnClickListener(listener);

        Object value = jsonObject.get("value");

        if (value instanceof JSONArray) {
            JSONArray array = (JSONArray) value;

            for (int i = 0; i < array.length(); ++i) {
                imageUrl = array.getString(i);
                cardBox.addPreviewItem(imageUrl);
            }
        }
        else {
            imageUrl = value.toString();
            cardBox.addPreviewItem(imageUrl);
        }

        v.addView(container);
    }
    */
}
