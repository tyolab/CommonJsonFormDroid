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

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import au.com.tyo.json.jsonform.JsonFormFieldButton;
import au.com.tyo.json.android.R;
import au.com.tyo.json.android.interfaces.CommonListener;
import au.com.tyo.json.android.interfaces.JsonApi;
import au.com.tyo.json.android.interfaces.MetaDataWatcher;
import au.com.tyo.json.android.utils.FormUtils;
import au.com.tyo.json.android.utils.JsonMetadata;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 18/7/17.
 */

public class TitledButtonFactory extends TitledItemFactory {

    public static final String NAME = TitledButtonFactory.class.getSimpleName();

    public static final String KEY = NAME;

    private static final String TAG = NAME;

    static {
        JsonFormFieldButton.setWidgetType(NAME);
    }

    public TitledButtonFactory(String widgetKey) {
        super(widgetKey);
    }

    public TitledButtonFactory() {
        super(NAME);
    }

    protected void setupOnClickListener(final CommonListener listener, final View button) {
        // don't need to get the listener this way
        // if (listenerMethodStr != null)
        //     button.setOnClickListener(new View.OnClickListener() {
        //         @Override
        //         public void onFieldClick(View v) {
        //             if (null != listenerMethodStr)
        //                 try {
        //                     Method listenerMethod = CommonApp.getInstance().getClass().getDeclaredMethod(listenerMethodStr, String.class, String.class);
        //                     listenerMethod.setAccessible(true);
        //                     Object invoke = listenerMethod.invoke(CommonApp.getInstance(), key, text);
        //                     listenerMethod.setAccessible(false);
        //                 } catch (NoSuchMethodException e) {
        //                     Log.e(TAG, "incorrect listener: \n" + StringUtils.exceptionStackTraceToString(e));
        //                 } catch (InvocationTargetException e) {
        //                     Log.e(TAG, "exception in calling the listener method: \n" + StringUtils.exceptionStackTraceToString(e));
        //                 } catch (IllegalAccessException e) {
        //                     Log.e(TAG, "listener method not accessable: \n" + StringUtils.exceptionStackTraceToString(e));
        //                 }
        //             else {
        //                 listener.onUserInputFieldClick(button.getContext(), key, text);
        //             }
        //         }
        //     });
        // else
            button.setOnClickListener(listener);

    }

    @Override
    protected View createUserInputView(JsonApi jsonApi, LayoutInflater factory, ViewGroup parent, String stepName, JSONObject jsonObject, JsonMetadata metadata, CommonListener listener, boolean editable, int clickable, int gravity, MetaDataWatcher metaDataWatcher) throws JSONException {
        final String key = jsonObject.getString("key");

        String text = null;

        if (jsonObject.has("value"))
            text = jsonObject.getString("value");
        else if (jsonObject.has("text"))
            text = jsonObject.getString("text");

        View v = inflateViewForField(jsonObject, factory, R.layout.item_button); //factory.inflate(R.layout.item_button, parent, false);

        v.setClickable(editable);

        // set tag info
        FormUtils.formatView(v, key,
                jsonObject.getString("type") );
        v.setTag(R.id.pick, jsonObject.optString("pick"));

        CommonListener aListener = listener;

        if (null == aListener) {
            String listenerMethodStr = null;

            // the listener in form has the highest priority
            if (jsonObject.has("listener"))
                listenerMethodStr = jsonObject.getString("listener");

            if (null != listenerMethodStr)
                aListener = jsonApi.getFormOnClickListenerByName(listenerMethodStr);

            if (null == aListener)
                aListener = jsonApi.getFormOnClickListenerByKey(key, text);
        }

        setupOnClickListener(aListener, v);

        View userInputView = v.findViewById(R.id.user_input);
        if (null != userInputView && userInputView instanceof TextView) {
            TextView buttonText = (TextView) userInputView;
            if (null != text && text.length() > 0) {
                buttonText.setText(text);
                listener.onInitialValueSet(key, null, text);
            } else if (jsonObject.has("hint")) {
                buttonText.setHint(jsonObject.getString("hint"));
                buttonText.setHintTextColor(Color.GRAY);
                buttonText.setTextColor(Color.GRAY);
            }
        }
        return v;
    }
}
