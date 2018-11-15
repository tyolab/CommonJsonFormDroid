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
public class TitledLabelFactory extends TitledItemFactory {

    public static final String KEY = TitledLabelFactory.class.getSimpleName();

    private static final String TAG = TitledLabelFactory.class.getSimpleName();

    public TitledLabelFactory(String widgetKey) {
        super(widgetKey);
    }

    public TitledLabelFactory() {
        super(TitledLabelFactory.class.getSimpleName());
    }

    @Override
    protected View createUserInputView(JsonApi jsonApi, LayoutInflater factory, ViewGroup parent, String stepName, JSONObject jsonObject, CommonListener listener, boolean editable, int gravity, MetaDataWatcher metaDataWatcher) throws JSONException {

        View v = factory.inflate(R.layout.item_label, null);

        bindUserInput(v, jsonObject, gravity);

        return v;
    }

}
