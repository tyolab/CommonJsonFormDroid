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
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import au.com.tyo.json.jsonform.JsonFormFieldButton;
import au.com.tyo.json.jsonform.JsonFormGroup;
import au.com.tyo.json.android.interfaces.FormWidgetFactory;
import au.com.tyo.json.android.widgets.CommonItemFactory;
import au.com.tyo.json.android.widgets.GroupTitleFactory;
import au.com.tyo.json.android.widgets.TitledClickableLabelFactory;
import au.com.tyo.json.form.DataFormEx;
import au.com.tyo.json.form.DataJson;
import au.com.tyo.json.form.FormBasicItem;
import au.com.tyo.json.form.FormObject;
import au.com.tyo.json.jsonform.JsonForm;
import au.com.tyo.json.jsonform.JsonFormField;
import au.com.tyo.json.jsonform.JsonFormFieldDatePicker;
import au.com.tyo.json.jsonform.JsonFormFieldEditText;
import au.com.tyo.json.jsonform.JsonFormFieldSwitch;
import au.com.tyo.json.jsonform.JsonFormFieldTitledLabel;
import au.com.tyo.json.jsonform.JsonFormFieldWithTitleAndHint;
import au.com.tyo.json.jsonform.JsonFormStep;
import au.com.tyo.json.android.widgets.TitledEditTextFactory;
import au.com.tyo.json.android.widgets.TitledLabelFactory;
import au.com.tyo.json.android.widgets.TitledSwitchButtonFactory;
import au.com.tyo.json.form.FormField;
import au.com.tyo.json.form.FormGroup;
import au.com.tyo.json.util.TitleKeyConverter;

import static au.com.tyo.json.jsonform.JsonFormFieldButton.PICK_DATE;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 21/12/17.
 */

public class FormHelper {

    private static final String TAG = "FormHelper";

    public static final String WIDGET_TYPE_TITLED_CLICKABLE_LABEL = TitledClickableLabelFactory.class.getSimpleName();
    public static final String WIDGET_TYPE_TITLED_LABEL = TitledLabelFactory.class.getSimpleName();
    public static final String WIDGET_TYPE_TITLED_BUTTON = TitledClickableLabelFactory.class.getSimpleName();
    public static final String WIDGET_TYPE_TITLED_SWITCH_BUTTON = TitledSwitchButtonFactory.class.getSimpleName();

    public static class GeneralTitleKeyConverter implements TitleKeyConverter {

        @Override
        public String toKey(String title) {
            return FormField.toKey(title);
        }

        @Override
        public String toTitle(String key) {
            return FormField.toTitle(key);
        }
    }

    /**
     * The default title/key converter
     */
    private static TitleKeyConverter generalTitleConverter = new GeneralTitleKeyConverter();

    public static TitleKeyConverter getGeneralTitleKeyConverter() {
        return generalTitleConverter;
    }

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

    public static String getWidgetName(CommonItemFactory factory) {
        return factory.getClass().getCanonicalName();
    }

    public static JsonFormFieldEditText createTitledEditTextField(String key, String title, String text) {
        JsonFormFieldEditText editText = new JsonFormFieldEditText(key, title, "");
        editText.type = TitledEditTextFactory.NAME;
        editText.value = text;
        return editText;
    }

    public static JsonFormFieldTitledLabel createTitledLabelField(String key, String title, String text) {
        JsonFormFieldTitledLabel label = new JsonFormFieldTitledLabel(key, title, "");

        // make sure we use the right widget
        label.type = TitledLabelFactory.class.getSimpleName();
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
        JsonFormFieldSwitch switchButton = new JsonFormFieldSwitch(key, title);
        switchButton.type = WIDGET_TYPE_TITLED_SWITCH_BUTTON;
        switchButton.value = String.valueOf(initValue);
        return switchButton;
    }

    public static JsonFormFieldButton createButton(String key, String title, String text) {
        JsonFormFieldButton fieldButton = new JsonFormFieldButton(key, title);
        fieldButton.value = text;
        fieldButton.type = WIDGET_TYPE_TITLED_CLICKABLE_LABEL; // TitledButtonFactory.class.getSimpleName();
        return fieldButton;
    }

    public static JsonFormFieldTitledLabel createLabelButton(String key, String title, String text) {
        JsonFormFieldTitledLabel fieldButton = new JsonFormFieldTitledLabel(key, title);
        fieldButton.value = text;
        fieldButton.type = WIDGET_TYPE_TITLED_LABEL;
        fieldButton.clickable = 1;
        return fieldButton;
    }

    public static <T extends FormWidgetFactory> JsonFormField createFieldWidget(String key, Class<T> factoryClass, Object value) {
        return createFieldWidget(key, factoryClass.getSimpleName(), value);
    }

    /**
     * Create a field with the provide widget type, and user doesn't have to provide value
     *
     * @param key
     * @param type
     * @param value
     * @return
     */
    public static JsonFormField createFieldWidget(String key, String type, Object value) {
        return createFieldWidget(key, type, value, false);
    }

    public static JsonFormField createFieldWidget(String key, String type, Object value, boolean required) {
        JsonFormField formField = new JsonFormField(key, type, required);
        formField.value = value;
        return formField;
    }

    public static JsonForm createForm(FormBasicItem data, boolean sortFormNeeded) {
        return createForm(data, null, sortFormNeeded);
    }

    /**
     *
     * @param data
     * @param keyConverter
     * @param sortFormNeeded
     * @return
     */
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
                 *
                 * the field value can be null
                 */

                addField(step, key, null, value, data.isEditable(), false, keyConverter, metaMap);
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
        return createForm(map, true, keyConverter, null, sortForm);
    }

    /**
     *
     * @param map
     * @param keyConverter
     * @param metaMap
     * @param sortForm
     * @return
     */
    public static JsonForm createForm(Map map, boolean editable, TitleKeyConverter keyConverter, Map metaMap, boolean sortForm) {
        JsonForm form = new JsonForm();
        JsonFormStep step = form.createNewStep();

        if (keyConverter == null)
            keyConverter = generalTitleConverter;

        if (map instanceof DataFormEx) {
            DataFormEx formMap = (DataFormEx) map;
            final boolean isFormLocked = formMap.isLocked();
            boolean isFormEditable;
            if (formMap.isEditableValueSet())
                isFormEditable = formMap.isEditable();
            else
                isFormEditable = editable;

            form.title = formMap.getTitle();

            step.header = formMap.getHeader();
            step.footer = formMap.getFooter();

            List groups = formMap.getGroups();

            Map formMetaMap = formMap.getMetaMap();
            if (null == formMetaMap)
                formMetaMap = metaMap;

            for (int i = 0; i < groups.size(); ++i) {
                Map groupMap = (Map) groups.get(i);

                JsonFormGroup jsonFormGroup = createGroup();
                if (groupMap instanceof FormGroup) {
                    FormGroup formGroup = (FormGroup) groupMap;
                    if (formGroup.hasKey())
                        jsonFormGroup.key = formGroup.getKey();

                    if (formGroup.isShowingTitle() && null != formGroup.getTitle()) {
                        JsonFormField titleField = createFieldWidget(keyConverter.toKey(formGroup.getTitle()), GroupTitleFactory.class, formGroup.getTitle());
                        jsonFormGroup.addField(titleField);
                        if (formGroup.hasTitleLayout())
                            titleField.layout = formGroup.getTitleLayout();
                    }

                    for (int j = 0; j < formGroup.size(); ++j) {
                        FormField value = (FormField) formGroup.getValue(j); // all value are stored as String during form creation
                        JsonFormField field;
                        // if (value.getValue() instanceof JsonFormField) {
                        //     field = (JsonFormField) value.getValue();
                        //     jsonFormGroup.addField(field);
                        // }
                        // else
                        field = addField(jsonFormGroup, value.getKey(), value.getTitle(), value.getValue(), isFormEditable, isFormLocked, keyConverter,
                                formMetaMap);

                        field.clickable = value.isClickable();
                        field.enabled = value.isEnabled();

                        /**
                         * make it optional
                         */
                        if (value.getLayout() > -1)
                            field.layout = value.getLayout();

                        if (null != value.getType())
                            field.type = value.getType();

                        field.separator_under = value.hasSeparator();
                    }
                }
                else
                    mapToField(jsonFormGroup, groupMap, editable, keyConverter, formMetaMap);

                step.addGroup(jsonFormGroup);
            }
        }
        else {
            Set<Map.Entry<String, Object>> list = map.entrySet();

            for (Map.Entry<String, Object> entry : list) {
                String key = entry.getKey();
                Object value = entry.getValue();

                /**
                 * Form Category
                 */
                if (value instanceof Map) {
                    mapToField(step, (Map) value, editable, keyConverter, metaMap);
                }
                else
                    addField(step, key, null, value, editable, false, keyConverter, metaMap);
            }

            if (sortForm)
                form.sort();
        }

        return form;
    }

    /**
     *
     * @param jsonFormGroup
     * @param map
     * @param editable
     * @param keyConverter
     * @param metaMap
     */
    private static void mapToField(JsonFormGroup jsonFormGroup, Map map, boolean editable, TitleKeyConverter keyConverter, Map metaMap) {
        Set<Map.Entry> set = ((Map) map).entrySet();
        for (Map.Entry entry1 : set) {
            String subkey = (String) entry1.getKey();
            Object subvalue = entry1.getValue();

            addField(jsonFormGroup, subkey, null, subvalue, editable, false, keyConverter, metaMap);
        }
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
     */
    private static void addGroup(JsonFormStep step) {
        JsonFormGroup group = createGroup();

        if (null != group)
            step.addGroup(group);
    }

    /**
     *
     * @return
     */
    private static JsonFormGroup createGroup() {
        return new JsonFormGroup();
    }

    /**
     *  @param jsonFormGroup
     * @param key
     * @param value
     * @param isFormLocked
     * @param keyConverter
     * @param metaMap
     */
    private static JsonFormField addField(JsonFormGroup jsonFormGroup, String key, String title, Object value, boolean editable, boolean isFormLocked, TitleKeyConverter keyConverter, Map metaMap) {
        JsonFormField field = createField(key, title, value, editable, isFormLocked, keyConverter, metaMap);

        if (null != field)
            jsonFormGroup.addField(field);
        return field;
    }

    /**
     *
     * @param key
     * @param value
     * @param locked, the form is not changeable,
     * @param keyConverter
     * @param metaMap
     * @return
     */
    public static JsonFormField createField(String key, String title, Object value, boolean editable, boolean locked, TitleKeyConverter keyConverter, Map metaMap) {

        /**
         * Other data
         */
        JsonFormField field = null;
        String fieldKey = null;
        if (null == key)
            fieldKey = null != keyConverter ? keyConverter.toKey(title) : title;
        else
            fieldKey = key;

        Map subMetaMap = null;
        String newTitle = title;

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
        else if (null == newTitle){
            newTitle = null != keyConverter ? keyConverter.toTitle(key) : key;
        }

        if (null != subMetaMap && subMetaMap.containsKey(JsonForm.FORM_META_KEY_WIDGET)) {
            field = new JsonFormFieldWithTitleAndHint(fieldKey,
                    newTitle,
                    subMetaMap.containsKey(JsonForm.FORM_META_KEY_HINT) ? (String) subMetaMap.get(JsonForm.FORM_META_KEY_HINT) : "");
            field.type = (String) subMetaMap.get(JsonForm.FORM_META_KEY_WIDGET);

            if (field.type == null)
                throw new IllegalStateException("Widget field (" + field.key + ")'s type is not specified");

            field.value = value != null ? value.toString() : "";
        }
        else {
            if (value instanceof JsonFormField)
                field = (JsonFormField) value;
            else if (value instanceof Boolean)
                field = createSwitchButton(fieldKey, newTitle, Boolean.parseBoolean(String.valueOf(value)));
            else {

                JsonFormField labelField;
                if (!locked && editable)
                    // for anything else it is just edit text, and it is not editable by default
                    labelField = createTitledEditTextField(fieldKey, newTitle, value != null ? value.toString() : "");
                else
                    labelField = (createTitledLabelField(key, newTitle, value != null ? value.toString() : ""));
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
