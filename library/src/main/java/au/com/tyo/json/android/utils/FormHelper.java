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

package au.com.tyo.json.android.utils;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import au.com.tyo.json.JsonForm;
import au.com.tyo.json.JsonFormField;
import au.com.tyo.json.JsonFormFieldDatePicker;
import au.com.tyo.json.JsonFormFieldEditText;
import au.com.tyo.json.JsonFormFieldSwitch;
import au.com.tyo.json.JsonFormStep;
import au.com.tyo.json.android.interactors.JsonFormInteractor;
import au.com.tyo.json.android.widgets.TitledEditTextFactory;
import au.com.tyo.json.android.widgets.TitledSwitchButtonFactory;

import static au.com.tyo.json.JsonFormFieldButton.PICK_DATE;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 21/12/17.
 */

public class FormHelper {

    private static final TitledEditTextFactory titledTextFactory = new TitledEditTextFactory();
    private static final TitledSwitchButtonFactory titledSwitchButtonFactory = new TitledSwitchButtonFactory();

    public interface TitleToKey {
        String toKey(String title);
    }

    static {
        registerWidgetFactories();
    }

    public static void registerWidgetFactories() {
        JsonFormInteractor.registerWidget(titledTextFactory);
        JsonFormInteractor.registerWidget(titledSwitchButtonFactory);
    }

    public static JsonFormFieldEditText createTitledEditTextField(String key, String title, String text) {
        JsonFormFieldEditText editText = new JsonFormFieldEditText(key, titledTextFactory.getClass().getSimpleName(), title, "");
        return editText;
    }

    public static JsonFormFieldDatePicker createDatePicker(JsonFormStep step, String key, String title, Date date, String hint) {
        return createDatePicker(step, key, title, date, PICK_DATE, hint);
    }

    public static JsonFormFieldDatePicker createDatePicker(JsonFormStep step, String key, String title, Date date, int pickType, String hint) {
        JsonFormFieldDatePicker jsonFormFieldDatePicker = (JsonFormFieldDatePicker) step.addField(new JsonFormFieldDatePicker(key, title, pickType, hint));
        return jsonFormFieldDatePicker;
    }

    public static JsonFormFieldSwitch createSwitchButton(String key, String title, boolean initValue) {
        JsonFormFieldSwitch switchButton = new JsonFormFieldSwitch(key, titledSwitchButtonFactory.getClass().getSimpleName(), title);
        switchButton.value = String.valueOf(initValue);
        return switchButton;
    }

    public static JsonForm createForm(Map data) {
        return createForm(data, null);
    }

    public static JsonForm createForm(Map data, TitleToKey keyConverter) {
        JsonForm form = new JsonForm();
        JsonFormStep step = form.createNewStep();

        Set<Map.Entry<String, Object>> list = data.entrySet();

        for (Map.Entry<String, Object> entry : list) {
            String key = entry.getKey();
            Object value = entry.getValue();

            JsonFormField field = createField(key, value, keyConverter);

            if (null != field)
                step.addField(field);
        }

        return form;
    }

    public static JsonFormField createField(String title, Object value, TitleToKey keyConvertor) {
        JsonFormField field = null;
        String key = null != keyConvertor ? keyConvertor.toKey(title) : title;
        if (value instanceof Boolean)
            field = createSwitchButton(key, title, Boolean.parseBoolean(String.valueOf(value)));
        else // for anything else it is just edit text
            field = createTitledEditTextField(key, title, value != null ? value.toString() : "");
        return field;
    }
}
