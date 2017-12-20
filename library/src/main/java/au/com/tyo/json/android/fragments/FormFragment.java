package au.com.tyo.json.android.fragments;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.rey.material.widget.Button;
import com.rey.material.widget.TextView;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import au.com.tyo.json.FormItem;
import au.com.tyo.json.android.R;
import au.com.tyo.json.android.customviews.RadioButton;
import au.com.tyo.json.android.presenters.JsonFormExtensionPresenter;
import au.com.tyo.json.android.presenters.JsonFormFragmentPresenter;
import au.com.tyo.json.android.views.ButtonContainer;
import au.com.tyo.utils.StringUtils;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 19/7/17.
 */

public class FormFragment extends JsonFormFragment {

    public static final String          FRAGMENT_JSON_FORM_TAG = "FormFragment";

    private static final String TAG = FRAGMENT_JSON_FORM_TAG;

    private LinearLayout                mainView;

    private View                        dummyView;

    private boolean                     editable;

    private int                         grayColor;
    private ColorStateList              fieldTextColors;

    private FormItem                    form;

    private JsonFormExtensionPresenter formPresenter;

    public static class FieldMetadata {
        public int index;
        public boolean required;
        public Object value;
        public boolean visible;

        public FieldMetadata(int i, boolean required) {
            this();
            this.index = i;
            this.required = required;
        }

        public FieldMetadata() {
            visible = true;
            index = -1;
        }
    }

    public FormItem getForm() {
        return form;
    }

    public void setForm(FormItem form) {
        this.form = form;
    }

    public Map<String, FieldMetadata> metadataMap;

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

        if (null == metadataMap)
            metadataMap = new HashMap<>();

        grayColor = getActivity().getResources().getColor(R.color.grey);
        fieldTextColors = getActivity().getResources().getColorStateList(R.color.field_text_colors);
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

    public void updateForm(String targetKey, Object result) {
        String text = null;
        if (result instanceof String)
            text = (String) result;
        else if (result instanceof Object)
            text = result.toString();

        if (null == text)
            return;

        updateFormFieldText(targetKey, text);
    }

    protected void updateFormFieldText(String targetKey, String text) {
        onValueChange(targetKey, null, text);

        View view = getViewByKey(targetKey);
        if (null != view) {
            View v = view.findViewById(R.id.button_text);

            if (null == v)
                v = view.findViewById(R.id.user_input);

            if (v instanceof TextView) {
                TextView button = (TextView) v;
                button.setText(text);
                button.setTextColor(fieldTextColors);
            }
        }
    }

    @Override
    public void addFormElements(List<View> views) {
        for (int i = 0; i < views.size(); ++i) {
            View view = views.get(i);
            mainView.addView(view);

            String key = (String) view.getTag(R.id.key);

            FieldMetadata metadata = getFieldMetaData(key);
            boolean required = (boolean) view.getTag(R.id.required);
            metadata.required = required;
            metadata.index = i;

            if (!isEditable())
                setFormRowEditable(view, false);
        }
    }

    private FieldMetadata getFieldMetaData(String key) {
        FieldMetadata fieldMetadata = metadataMap.get(key);
        if (fieldMetadata == null) {
            fieldMetadata = new FieldMetadata();
            metadataMap.put(key, fieldMetadata);
        }
        return fieldMetadata;
    }

    public void setFormEditable(boolean editable) {
        setEditable(editable);

        for (int i = 0; i < mainView.getChildCount(); ++i) {
            View view = mainView.getChildAt(i);
            setFormRowEditable(view, editable);
        }
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

    private void setInputViewTextColor(android.widget.TextView inputView, boolean editable) {
        if (editable)
            inputView.setTextColor(fieldTextColors);
        else
            inputView.setTextColor(grayColor);
    }

    private void setFormRowEditable(View view, boolean editable) {
        View inputView = view.findViewById(R.id.user_input);

        if (null != inputView) {
            inputView.setEnabled(editable);

            if (inputView instanceof ButtonContainer) {
                inputView.setClickable(editable);

                View v = view.findViewById(R.id.button_text);

                if (v instanceof android.widget.TextView) {
                    setInputViewTextColor((android.widget.TextView) v, editable);
                }
                else if (v instanceof TextView) {
                    setInputViewTextColor((TextView) v, editable);
                }
            }
            else if (inputView instanceof android.widget.TextView) {
                setInputViewTextColor((android.widget.TextView) inputView, editable);
            }
            else if (inputView instanceof TextView) {
                setInputViewTextColor((TextView) inputView, editable);
            }
            else if (inputView instanceof Button) {
                inputView.setClickable(editable);
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
            else if (inputView instanceof com.rey.material.widget.Switch)
                setViewAlpha(inputView, editable);
        }
        else if (view instanceof EditText)
            view.setEnabled(false);
    }


    public boolean validateForm() {
        boolean result = true;

        // check the basic first, the required must be filled
        Set<Map.Entry<String, FieldMetadata>> keyValues = metadataMap.entrySet();
        for (Map.Entry<String, FieldMetadata> entry : keyValues) {
            String key = entry.getKey();
            FieldMetadata md = entry.getValue();

            if (!md.required || !md.visible)
                continue;

            if (md.value instanceof String ) {
                if (!TextUtils.isEmpty((CharSequence) md.value))
                    continue;
            }
            else if (md.value instanceof Set) {
                Set set = (Set) md.value;
                if (null != set && set.size() > 0)
                    continue;
            }
            else if (md.value != null)
                continue;

            // OK, the validation failed
            onValidateRequiredFormFieldFailed(key);

            result = false;
        }
        return result;
    }

    protected void onValidateRequiredFormFieldFailed(String key) {
        // no ops
    }

    public View getViewByKey(String key) {
        View view = null;
        FieldMetadata metaData = getFieldMetaData(key);
        int index = metaData.index;
        try {
            view = mainView.getChildAt(index);
        }
        catch (Exception e) {
            Log.e(TAG, StringUtils.exceptionStackTraceToString(e));
        }

        if (view == null)
            for (int i = 0; i < mainView.getChildCount(); i++) {
                View childView = mainView.getChildAt(i);

                String aKey = (String) view.getTag(R.id.key);
                if (aKey.equals(key)) {
                    view= childView;
                    metaData.index = i;
                    break;
                }
            }

        return view;
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

    public void addUserInputValueToMetadata(String key, String childKey, Object value) {
        FieldMetadata metaData = getFieldMetaData(key);

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
                Object v = metaData.value;
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
    }

    @Override
    public void onValueChange(String parentKey, String childKey, Object value) {
        addUserInputValueToMetadata(parentKey, childKey, value);
    }

    @Override
    public void onInitialValueSet(String parentKey, String childKey, String value) {
        super.onInitialValueSet(parentKey, childKey, value);
        addUserInputValueToMetadata(parentKey, childKey, value);
    }

    public void updateFormFieldVisibility(String targetKey, boolean visible) {
        getViewByKey(targetKey).setVisibility(visible ? View.VISIBLE : View.GONE);
        updateFormFieldVisibilityInMetadata(targetKey, visible);
    }

    @Override
    public void onVisibilityChange(String key, String childKey, boolean b) {
        updateFormFieldVisibilityInMetadata(key, b);
    }

    private void updateFormFieldVisibilityInMetadata(String key, boolean visible) {
        FieldMetadata metaData = getFieldMetaData(key);
        metaData.visible = visible;
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
}
