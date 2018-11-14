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

import org.json.JSONException;
import org.json.JSONObject;

import au.com.tyo.json.android.R;
import au.com.tyo.json.android.interfaces.CommonListener;
import au.com.tyo.json.android.interfaces.JsonApi;
import au.com.tyo.json.android.interfaces.MetaDataWatcher;

/**
 * Created by vijay on 24-05-2015.
 */
public class TitledEditTextFactory extends TitledItemFactory {

    public static int WIDGET_EDIT_TEXT_RESOURCE = R.layout.item_titled_edit_text;

    public static final String KEY = TitledEditTextFactory.class.getSimpleName();

    private static final String TAG = TitledEditTextFactory.class.getSimpleName();

    public static final int MIN_LENGTH = 0;
    public static final int MAX_LENGTH = 0;

    @Override
    protected View createUserInputView(JsonApi jsonApi, LayoutInflater factory, ViewGroup parent, String stepName, JSONObject jsonObject, CommonListener listener, boolean editable, int gravity, MetaDataWatcher metaDataWatcher) throws JSONException {
        int minLength = MIN_LENGTH;
        int maxLength = MAX_LENGTH;

        View v = createEditText(jsonApi, factory, parent, WIDGET_EDIT_TEXT_RESOURCE, stepName, jsonObject, minLength, maxLength, listener);

        return v;
    }

//    public static ValidationStatus validate(MaterialEditText editText) {
//        boolean validate = editText.validate();
//        if(!validate) {
//            return new ValidationStatus(false, editText.getError().toString());
//        }
//        return new ValidationStatus(true, null);
//    }

}
