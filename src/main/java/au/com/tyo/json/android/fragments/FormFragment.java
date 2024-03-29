package au.com.tyo.json.android.fragments;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import au.com.tyo.json.android.interfaces.FormWidgetFactory;
import au.com.tyo.json.android.R;
import au.com.tyo.json.android.constants.JsonFormConstants;
import au.com.tyo.json.android.customviews.RadioButton;
import au.com.tyo.json.android.interfaces.MetaDataWatcher;
import au.com.tyo.json.android.presenters.JsonFormExtensionPresenter;
import au.com.tyo.json.android.presenters.JsonFormFragmentPresenter;
import au.com.tyo.json.android.views.ButtonContainer;
import au.com.tyo.json.android.views.OptionalButton;
import au.com.tyo.json.validator.Validator;

import static au.com.tyo.json.jsonform.JsonFormField.VALUE_REQUIRED;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 19/7/17.
 */

public class FormFragment extends JsonFormFragment implements MetaDataWatcher {

    public static final String          FRAGMENT_JSON_FORM_TAG = "FormFragment";

    private static final String         TAG = FRAGMENT_JSON_FORM_TAG;

    private LinearLayout                mainView;

    private View                        dummyView;

    private boolean                     editable;

    private int                         grayColor;
    private ColorStateList              fieldTextColors;

    private Object form;

    private JsonFormExtensionPresenter  formPresenter;
    private boolean                     darkThemeInUse;

    public Object getForm() {
        return form;
    }

    public void setForm(Object form) {
        this.form = form;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public void setGrayColor(int grayColor) {
        this.grayColor = grayColor;
    }

    @Override
    protected JsonFormFragmentPresenter createPresenter() {
        if (null == formPresenter)
            formPresenter = new JsonFormExtensionPresenter();
        return formPresenter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * The context is themed
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            grayColor = getActivity().getColor(R.color.grey);
            fieldTextColors = getActivity().getColorStateList(R.color.field_text_colors);
        }
        else {
            grayColor = getActivity().getResources().getColor(R.color.grey);
            fieldTextColors = getActivity().getResources().getColorStateList(R.color.field_text_colors);
        }

        setMetaDataWatcher(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        mainView = (LinearLayout) rootView.findViewById(R.id.main_layout);
        dummyView = rootView.findViewById(R.id.dummy_view);
        return rootView;
    }

    public String getCurrentKey() {
        return formPresenter.getCurrentKey();
    }

    @Override
    public LinearLayout getMainView() {
        return mainView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false; // let the parent page to deal with ith
    }

    @Override
    public boolean onUserInputFieldClick(View view, String key, String text) {
        super.onUserInputFieldClick(view, key, text);

        if (view instanceof OptionalButton) {
            OptionalButton optionalButton = (OptionalButton) view;
            if (optionalButton.isOpClear()) {
                updateFormField(key, null);
                getJsonApi().onFieldValueClear(key);
                optionalButton.hideClearButton();
                return true;
            }
        }
        return false;
    }

    public void updateForm(String targetKey, Object result) {
        updateFormField(targetKey, result);
    }

    protected void updateFormField(String targetKey, Object value) {
        onValueChange(targetKey, null, value);

        View view = getViewByKey(targetKey);

        if (null != view) {
            String type = (String) view.getTag(R.id.type);

            FormWidgetFactory factory = FormWidgetFactory.getWidgetFactory(type);

            factory.updateView(getJsonApi(), view, targetKey, true, value, fieldTextColors);
        }
        else
            Log.w(TAG, "The view is not found, for key: " + targetKey);

    }

    @Override
    public void addFormElements(List<View> views) {
        for (int i = 0; i < views.size(); ++i) {
            View view = views.get(i);
            mainView.addView(view);
        }
    }

    /**
     *
     * We only keep the metadata info for the user input / or updatable
     * @param key
     * @param inputView
     * @param enabled
     * @param required
     */
    @Override
    public void setKeyInputView(String key, View inputView, boolean editable, boolean enabled, int required) {
        FieldMetadata metadata = null;

        metadata = getFieldMetaData(key);
        metadata.required = required;
        metadata.inputView = inputView;
        setFormRowEditableOrEnabled(metadata.inputView, editable, enabled);
    }

    @Override
    public void setKeyWidgetView(String key, View v) {
        FieldMetadata metadata = getFieldMetaData(key);
        metadata.view = v;
    }

    public void changeFormEditableState(boolean editable) {
        onFormEditableStateChanged(editable);
    }

    protected void setViewAlpha(View childView, boolean editable) {
        if (!editable)
            childView.setAlpha(0.6f);
        else
            childView.setAlpha(1.0f);
    }

    private void setInputViewTextColor(TextView inputView, boolean editable) {
        if (editable)
            inputView.setTextColor(fieldTextColors);
        else
            inputView.setTextColor(grayColor);
    }

    /**
     * editable and enabled should be treated separately
     *
     * A uneditable form doesn't mean the button / clickable view is not enabled.
     *
     * Still a bit mess
     *
     * TODO
     *  seperated editable and enabled
     *
     *  @param view
     * @param editable
     * @param enabled
     */
    private void setFormRowEditableOrEnabled(View view, boolean editable, boolean enabled) {
        /**
         * Even the dark theme in use, still
         *
         */
        if (null == view/* || isDarkThemeInUse()*/)
            return;

        View inputView = view.findViewById(R.id.user_input);

        if (null != inputView) {
            if (inputView instanceof ButtonContainer) {
                inputView.setClickable(editable);

                View v = view.findViewById(android.R.id.text1);

                if (v instanceof android.widget.TextView || v instanceof TextView) {
                    // setInputViewTextColor((android.widget.TextView) v, editable);
                // }
                // else if (v instanceof TextView) {
                    // setInputViewTextColor((TextView) v, editable);
                    inputView = v;
                }
            }

            if (inputView instanceof Button) {
                inputView.setClickable(enabled);
            }
            else if (inputView instanceof EditText) {
                inputView.setEnabled(editable);
            }
            else {
                /**
                 * Text View include button
                 */
                if (inputView instanceof android.widget.TextView) {
                    setInputViewTextColor((android.widget.TextView) inputView, enabled);
                }
                else if (inputView instanceof TextView) {
                    setInputViewTextColor((TextView) inputView, enabled);
                }
                else if (inputView instanceof ViewGroup) {
                    for (int i = 0; i < ((ViewGroup) inputView).getChildCount(); ++i) {
                        View childView = ((ViewGroup) inputView).getChildAt(i);
                        if (childView instanceof CompoundButton) {
                            // childView.setEnabled(editable);
                            childView.setClickable(editable);
                            setViewAlpha(childView, editable);
                            // the below doesn't look good
                        }
                    }
                }
                else
                    inputView.setEnabled(enabled);
            }
            // else if (inputView instanceof com.rey.material.widget.Switch)
            //     setViewAlpha(inputView, editable);
        }
        else if (view instanceof EditText)
            view.setEnabled(editable);
        else
            view.setEnabled(enabled);
    }


    public boolean validateForm() {
        boolean result = true;

        // check the basic first, the required must be filled
        Set<Map.Entry<String, FieldMetadata>> keyValues = metadataMap.entrySet();
        for (Map.Entry<String, FieldMetadata> entry : keyValues) {
            String key = entry.getKey();
            FieldMetadata md = entry.getValue();
            String errorMessage = validateField(md, md.value);
            //
            // if (VALUE_REQUIRED != md.required || !md.visible)
            //     continue;
            // else
            //     errorMessage = getContext().getResources().getString(R.string.requires_value);
            //
            // if (md.value instanceof String) {
            //     if (!TextUtils.isEmpty((CharSequence) md.value))
            //         continue;
            //
            //     errorMessage = getContext().getResources().getString(R.string.must_not_empty);
            // }
            // else if (md.value instanceof Set) {
            //     Set set = (Set) md.value;
            //     if (null != set && set.size() > 0)
            //         continue;
            //
            //     errorMessage = getContext().getResources().getString(R.string.must_select_one);
            // }
            // else if (null != md.getValidators()){
            //     List<Validator> validators = md.getValidators();
            //     boolean allPassed = true;
            //
            //     for (Validator validator : validators) {
            //         if (!validator.isValid(md.value)) {
            //             allPassed = false;
            //             errorMessage = validator.getErrorMessage();
            //             break;
            //         }
            //     }
            //
            //     if (allPassed)
            //         continue;
            // }
            // else if (md.value != null)
            //     continue;

            if (null != errorMessage) {
                // OK, the validation failed
                onValidateRequiredFormFieldFailed(key, errorMessage);

                result = false;
                break;
            }
        }
        return result;
    }

    public String validateField(String keyStr, String text) {
        return validateField(metadataMap.get(keyStr), text);
    }

    public String validateField(FieldMetadata md, Object value) {
        if (null == md) {
            Log.w(TAG, "null metadata");
            return null;
        }

        if (VALUE_REQUIRED == md.required && !md.visible) {
            if (value instanceof String) {
                if (TextUtils.isEmpty((CharSequence) value))
                    return getContext().getResources().getString(R.string.must_not_empty);
            }

            if (value == null)
                return getContext().getResources().getString(R.string.requires_value);
        }

        String errorMessage = null;
        if (value instanceof Set) {
            Set set = (Set) value;
            if (null == set || set.size() == 0)
                errorMessage = getContext().getResources().getString(R.string.must_select_one);
        }
        else if (null != md.getValidators()){
            List<Validator> validators = md.getValidators();

            for (Validator validator : validators) {
                if (!validator.isValid(value)) {
                    errorMessage = validator.getErrorMessage();
                    break;
                }
            }
        }

        return errorMessage;
    }

    protected void onValidateRequiredFormFieldFailed(String key, String errorMessage) {
        getJsonApi().onValidateRequiredFormFieldFailed(key, errorMessage);
    }

    @Nullable
    public View getViewByKey(String key) {
        return getWidgetViewByKey(key);
    }

    @Nullable
    public View getInputViewByKey(String key) {
        return getFieldMetaData(key).inputView;
    }

    @Nullable
    public View getWidgetViewByKey(String key) {
        return getFieldMetaData(key).view;
    }

    public void requestFocusForField(String key) {
        View view = getViewByKey(key);

        if (view != null) {
            View inputView = view.findViewById(R.id.user_input);
            if (null != inputView)
                inputView.requestFocus();
            else
                view.requestFocus();
        }
    }

    @Override
    public void updateVisibilityOfNextAndSave(boolean next, boolean save) {
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public void unCheckAllExcept(String parentKey, String childKey) {
        View rowView = (ViewGroup) getViewByKey(parentKey);
        if (null == rowView)
            return;

        ViewGroup parent = rowView.findViewById(R.id.user_input);

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            if (view instanceof RadioButton) {
                RadioButton radio = (RadioButton) view;
                String parentKeyAtIndex = (String) radio.getTag(R.id.key);
                String childKeyAtIndex = (String) radio.getTag(R.id.childKey);
                if (parentKeyAtIndex.equals(parentKey) && !childKeyAtIndex.equals(childKey)) {
                    radio.setChecked(false);
                }
            }
        }
    }

    public FieldMetadata addUserInputValueToMetadata(String key, String childKey, java.lang.Object value) {
        FieldMetadata metaData = getFieldMetaData(key);

        return addUserInputValueToMetadata(metaData, childKey, value);
    }

    public FieldMetadata addUserInputValueToMetadata(FieldMetadata metaData, String childKey, java.lang.Object value) {
        if (childKey == null) {
            metaData.value = value;
        }
        // this is checkbox / radio button value
        else {
            Set set = null;
            if (metaData.value == null)
                metaData.value = new HashSet();

            if (metaData.value instanceof Set)
                set = (Set) metaData.value;
            else {
                java.lang.Object v = metaData.value;
                set = new HashSet();
                metaData.value = set;
                set.add(v);
            }

            boolean b = false;
            if (value instanceof String) {
                try {
                    b = Boolean.parseBoolean((String) value);
                }
                catch (Exception ex) {
                    b = false;
                }
            }

            if (b)
                set.add(childKey);
            else
                set.remove(childKey);
        }

        return metaData;
    }

    public Object getValue(String key, String childKey) {
        FieldMetadata metaData = getFieldMetaData(key);
        return metaData.value;
    }

    @Override
    public void onValueChange(String parentKey, String childKey, java.lang.Object value) {
        addUserInputValueToMetadata(parentKey, childKey, value);
    }

    @Override
    public void onInitialValueSet(String parentKey, String childKey, Object value) {
        super.onInitialValueSet(parentKey, childKey, value);
        addUserInputValueToMetadata(parentKey, childKey, value);
    }

    public void updateFormFieldVisibility(String targetKey, boolean visible) {
        View v = getViewByKey(targetKey);
        if (null != v)
            v.setVisibility(visible ? View.VISIBLE : View.GONE);
        else
            Log.w(TAG, "Trying to update visibility of null field: " + targetKey);
        updateFormFieldVisibilityInMetadata(targetKey, visible);
    }

    public void updateGroupVisibility(String groupKey, boolean visible) {
        View groupView = getChildViewByKey(groupKey);
        if (null != groupView)
            groupView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    /**
     *
     * @param key
     * @param childKey
     * @param visible
     */
    @Override
    public void onVisibilityChange(String key, String childKey, boolean visible) {
        // ? TODO should it be updateFormFieldVisibility
        updateFormFieldVisibilityInMetadata(key, visible);
    }

    private void updateFormFieldVisibilityInMetadata(String key, boolean visible) {
        FieldMetadata metaData = getFieldMetaData(key);
        metaData.visible = visible;
    }

    /**
     * So far we need to enable/disable for the inputview (id/input_view)
     *
     * @param key
     * @param enabled
     */
    public void enableField(String key, boolean enabled) {
        getInputViewByKey(key).setEnabled(enabled);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

        requestFocusForDummyView();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        super.onCheckedChanged(buttonView, isChecked);

        requestFocusForDummyView();
    }

    protected void requestFocusForDummyView() {
        // hide the keyboard
        hideKeyBoard();

        if (null != dummyView && !dummyView.hasFocus())
            dummyView.requestFocus();
    }

    public void onDateWidgetClick(View v) {
        requestFocusForDummyView();
    }

    public static JsonFormFragment getFormFragment(String stepName) {
        FormFragment jsonFormFragment = new FormFragment();
        Bundle bundle = new Bundle();
        bundle.putString("stepName", stepName);
        jsonFormFragment.setArguments(bundle);
        return jsonFormFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // onFormEditableStateChanged(editable);
    }

    private void onFormEditableStateChanged(boolean editable) {
        if (isDarkThemeInUse())
            return;

        Collection<FieldMetadata> values = metadataMap.values();
        Iterator<FieldMetadata> it = values.iterator();
        while (it.hasNext()) {
            FieldMetadata metadata = it.next();
            setFormRowEditableOrEnabled(metadata.view, editable, editable);
        }
    }

    private boolean isDarkThemeInUse() {
        return darkThemeInUse;
    }

    public void setDarkThemeInUse(boolean darkThemeInUse) {
        this.darkThemeInUse = darkThemeInUse;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (null != data && requestCode == JsonFormConstants.REQUEST_FORM_FILLING) {
            /**
             * @TODO
             * to be finished
             */
            Object result = data.getParcelableExtra(JsonFormConstants.RESULT_FORM_FILLING);
            String keyStr = getCurrentKey();
            updateForm(keyStr, result);
        }
        else
            super.onActivityResult(requestCode, resultCode, data);
    }

    public void updateFieldTitle(String keyStr, CharSequence title) {
        View view = getViewByKey(keyStr);
        if (null != view) {
            TextView tvTitle = view.findViewById(android.R.id.text1);
            if (null != tvTitle)
                tvTitle.setText(title);
        }
    }

    public void updateFieldTitle(String keyStr, int titleResId) {
        View view = getViewByKey(keyStr);
        if (null != view) {
            TextView tvTitle = view.findViewById(android.R.id.text1);
            if (null != tvTitle)
                tvTitle.setText(titleResId);
        }
    }
}
