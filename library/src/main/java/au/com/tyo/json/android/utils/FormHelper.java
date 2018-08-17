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

import android.text.TextUtils;

import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import au.com.tyo.json.DataJson;
import au.com.tyo.json.FormBasicItem;
import au.com.tyo.json.FormObject;
import au.com.tyo.json.JsonForm;
import au.com.tyo.json.JsonFormField;
import au.com.tyo.json.JsonFormFieldDatePicker;
import au.com.tyo.json.JsonFormFieldEditText;
import au.com.tyo.json.JsonFormFieldSwitch;
import au.com.tyo.json.JsonFormFieldTitledLabel;
import au.com.tyo.json.JsonFormFieldWithTitleAndHint;
import au.com.tyo.json.JsonFormStep;
import au.com.tyo.json.android.interactors.JsonFormInteractor;
import au.com.tyo.json.android.widgets.ImageBoxFactory;
import au.com.tyo.json.android.widgets.TitledEditTextFactory;
import au.com.tyo.json.android.widgets.TitledLabelFactory;
import au.com.tyo.json.android.widgets.TitledSwitchButtonFactory;

import static au.com.tyo.json.JsonFormFieldButton.PICK_DATE;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 21/12/17.
 */

public class FormHelper {

    private static final TitledEditTextFactory titledTextFactory = new TitledEditTextFactory();
    private static final TitledLabelFactory titledLabelFactory = new TitledLabelFactory();
    private static final TitledSwitchButtonFactory titledSwitchButtonFactory = new TitledSwitchButtonFactory();
    private static final ImageBoxFactory imageBoxFactory = new ImageBoxFactory();

    public interface TitleKeyConverter {
        String toKey(String title);
        String toTitle(String key);
    }

    public static class GeneralTitleKeyConverter implements TitleKeyConverter {

        @Override
        public String toKey(String title) {
            String[] tokens = title.split(" -_");
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < tokens.length; ++i) {
                if (TextUtils.isEmpty(tokens[i]))
                    continue;

                if (i > 0)
                    sb.append('-');

                sb.append(tokens[i].toLowerCase());
            }
            return sb.toString();
        }

        @Override
        public String toTitle(String key) {
            String[] tokens = key.split("-_");
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < tokens.length; ++i) {
                if (TextUtils.isEmpty(tokens[i]))
                    continue;

                if (i > 0)
                    sb.append(' ');

                sb.append(tokens[i].substring(0, 1).toUpperCase());
                sb.append(tokens[i].substring(1));
            }
            return sb.toString();
        }
    }

    /**
     * The default title/key converter
     */
    private static TitleKeyConverter generalTitleConverter = new GeneralTitleKeyConverter();

    public static class DefaultTitleKeyConverter implements TitleKeyConverter {

        @Override
        public String toKey(String title) {
            if (TextUtils.isEmpty(title))
                return null;

            if (title.length() == 1)
                return title.toLowerCase();

            String temp = title.replaceAll(" ", "");
            return Character.toLowerCase(temp.charAt(0)) + temp.substring(1);
        }

        @Override
        public String toTitle(String key) {
            StringBuffer buffer = new StringBuffer();

            if (key != null && key.length() > 0) {
                char pre = key.charAt(0);
                pre = Character.toUpperCase(pre);
                buffer.append(pre);

                for (int i = 1; i < key.length(); ) {
                    char cur = key.charAt(i);
                    if (Character.isUpperCase(cur))
                        buffer.append(" ");

                    buffer.append(cur);
                    ++i;
                }
            }
            return buffer.toString();
        }
    }

    static {
        registerWidgetFactories();
    }

    public static void registerWidgetFactories() {
        JsonFormInteractor.registerWidget(titledLabelFactory);
        JsonFormInteractor.registerWidget(titledTextFactory);
        JsonFormInteractor.registerWidget(titledSwitchButtonFactory);
        JsonFormInteractor.registerWidget(imageBoxFactory);
    }

    public static JsonFormFieldEditText createTitledEditTextField(String key, String title, String text) {
        JsonFormFieldEditText editText = new JsonFormFieldEditText(key, titledTextFactory.getClass().getSimpleName(), title, "");
        editText.value = text;
        return editText;
    }

    public static JsonFormFieldTitledLabel createTitledLabelField(String key, String title, String text) {
        JsonFormFieldTitledLabel label = new JsonFormFieldTitledLabel(key, titledLabelFactory.getClass().getSimpleName(), title, "");
        label.value = text;
        return label;
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

    public static JsonForm createForm(FormBasicItem data, boolean sortFormNeeded) {
        return createForm(data, null, sortFormNeeded);
    }

    public static JsonForm createForm(FormBasicItem data, TitleKeyConverter keyConverter, boolean sortFormNeeded) {
        JsonForm form;

        Map metaDataMap = data.getFormMetaDataMap();

        if (null != metaDataMap) {
            form = new JsonForm();
            JsonFormStep step = form.createNewStep();

            Set<Map.Entry<String, Object>> metaDataList = metaDataMap.entrySet();

            for (Map.Entry<String, Object> entry : metaDataList) {
                String key = entry.getKey();

                Map metaMap = (Map) entry.getValue();
                if (!((Boolean) metaMap.get(JsonForm.FORM_META_KEY_VISIBLE)))
                    continue;

                Object value = data.getValue(key);

                /**
                 * Not necessary to check null value
                 */
//                if (null == value)
//                    continue;

                addField(step, key, value, keyConverter, metaMap);
            }
        }
        else {
            Map map = data.getFormKeyValueMap();
            form = createForm(map, keyConverter, sortFormNeeded);
        }

        return form;
    }

    /**
     *
     * @param map
     * @return
     */
    public static JsonForm createForm(Map map) {
        return createForm(map, false);
    }

    /**
     *
     * @param map
     * @param sortForm
     * @return
     */
    public static JsonForm createForm(Map map, boolean sortForm) {
        return createForm(map, generalTitleConverter, sortForm);
    }

    /**
     *
     * @param map
     * @param keyConverter
     * @param sortForm
     * @return
     */
    public static JsonForm createForm(Map map, TitleKeyConverter keyConverter, boolean sortForm) {
        return createForm(map, keyConverter, null, sortForm);
    }

    /**
     *
     * @param map
     * @param keyConverter
     * @param metaMap
     * @param sortForm
     * @return
     */
    public static JsonForm createForm(Map map, TitleKeyConverter keyConverter, Map metaMap, boolean sortForm) {
        JsonForm form = new JsonForm();
        JsonFormStep step = form.createNewStep();

        Set<Map.Entry<String, Object>> list = map.entrySet();

        for (Map.Entry<String, Object> entry : list) {
            String key = entry.getKey();
            Object value = entry.getValue();

            /**
             * Form Category
             */
            if (value instanceof Map) {
                Set<Map.Entry> set = ((Map) value).entrySet();
                for (Map.Entry entry1 : set) {
                    String subkey = (String) entry1.getKey();
                    Object subvalue = entry1.getValue();

                    addField(step, subkey, subvalue, keyConverter, metaMap);
                }
            }
            else
                addField(step, key, value, keyConverter, metaMap);
        }

        if (sortForm)
            form.sort();

        return form;
    }

    /**
     *
     * @param data
     * @param keyConverter
     * @return
     */
    public static JsonForm createForm(FormObject data, TitleKeyConverter keyConverter) {
        JsonForm form = new JsonForm();
        JsonFormStep step = form.createNewStep();
        return form;
    }

    /**
     *
     * @param step
     * @param key
     * @param value
     * @param keyConverter
     * @param metaMap
     */
    private static void addField(JsonFormStep step, String key, Object value, TitleKeyConverter keyConverter, Map metaMap) {
        JsonFormField field = createField(key, value, keyConverter, metaMap);

        if (null != field)
            step.addField(field);
    }

    /**
     *
     * @param key
     * @param value
     * @param keyConverter
     * @param metaMap
     * @return
     */
    public static JsonFormField createField(String key, Object value, TitleKeyConverter keyConverter, Map metaMap) {
        JsonFormField field = null;
        String fieldKey = null != keyConverter ? keyConverter.toKey(key) : key;
        if (null == fieldKey)
            fieldKey = key;

        String newTitle;

        Map subMetaMap = null;

        if (null != metaMap)
            subMetaMap = (Map) metaMap.get(key);

        if (null != subMetaMap && subMetaMap.containsKey(JsonForm.FORM_META_KEY_I18N)) {
            /// TODO
            Map i18n = (Map) subMetaMap.get(JsonForm.FORM_META_KEY_I18N);
            newTitle = (String) i18n.get("en");

            String lang = Locale.getDefault().getLanguage();
            if (lang.length() > 1) {
                String chars = lang.substring(0, 2);
                String localTitle = (String) i18n.get(chars);
                if (!TextUtils.isEmpty(localTitle))
                    newTitle = localTitle;
            }
        }
        else {
            newTitle = null != keyConverter ? keyConverter.toTitle(key) : key;
            if (null == newTitle)
                newTitle = key;
        }

        if (null != subMetaMap && subMetaMap.containsKey(JsonForm.FORM_META_KEY_WIDGET)) {
            field = new JsonFormFieldWithTitleAndHint(fieldKey,
                    (String) subMetaMap.get(JsonForm.FORM_META_KEY_WIDGET),
                    newTitle,
                    subMetaMap.containsKey(JsonForm.FORM_META_KEY_HINT) ? (String) subMetaMap.get(JsonForm.FORM_META_KEY_HINT) : "");
            field.value = value != null ? value.toString() : "";
        }
        else {
            if (value instanceof Boolean)
                field = createSwitchButton(fieldKey, newTitle, Boolean.parseBoolean(String.valueOf(value)));
            else { // for anything else it is just edit text
                JsonFormFieldEditText labelField = createTitledEditTextField(fieldKey, newTitle, value != null ? value.toString() : "");

                // JsonFormFieldLabel labelField = (JsonFormFieldLabel) (field = createTitledLabelField(key, newTitle, value != null ? value.toString() : ""));
//                if (metaMap.containsKey(JsonForm.FORM_META_KEY_TEXT_STYLE))
//                    labelField.textStyle = (String) metaMap.get(JsonForm.FORM_META_KEY_TEXT_STYLE);
                field = labelField;
            }
        }

        if (null != subMetaMap) {
            if (subMetaMap.containsKey(JsonForm.FORM_META_KEY_ORIENTATION))
                field.orientation = (String) subMetaMap.get(JsonForm.FORM_META_KEY_ORIENTATION);

            if (subMetaMap.containsKey(JsonForm.FORM_META_KEY_VISIBLE))
                field.visible = String.valueOf(subMetaMap.get(JsonForm.FORM_META_KEY_VISIBLE));

            if (subMetaMap.containsKey(JsonForm.FORM_META_KEY_DATA_TYPE))
                field.type = (String) subMetaMap.get(JsonForm.FORM_META_KEY_DATA_TYPE);

            if (subMetaMap.containsKey(JsonForm.FORM_META_KEY_DISPLAY_ORDER))
                field.display_order = DataJson.getInt(subMetaMap, (JsonForm.FORM_META_KEY_DISPLAY_ORDER));
        }

        return field;
    }
}
