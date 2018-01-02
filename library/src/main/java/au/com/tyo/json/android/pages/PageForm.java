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

package au.com.tyo.json.android.pages;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import au.com.tyo.app.Constants;
import au.com.tyo.app.Controller;
import au.com.tyo.app.ui.page.Page;
import au.com.tyo.json.FormItem;
import au.com.tyo.json.FormState;
import au.com.tyo.json.JsonForm;
import au.com.tyo.json.android.R;
import au.com.tyo.json.android.constants.JsonFormConstants;
import au.com.tyo.json.android.fragments.FormFragment;
import au.com.tyo.json.android.interfaces.JsonApi;
import au.com.tyo.json.android.utils.FormHelper;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 20/12/17.
 */

public abstract class PageForm<T extends Controller> extends Page<T>  implements JsonApi {

    private static final    String              TAG = "PageForm";

    protected               String              json;

    private                 JSONObject          mJSONObject;

    private                 int                 formContainerId;

    private                 boolean             editable;

    private                 boolean             dirty;

    private                 boolean             exitAfterSaveAction = true;
    private                 boolean             errorHandled = false;

    private                 Object              form;
    private                 JsonForm            jsonForm;

    /**
     *
     * @param controller
     * @param activity
     */
    public PageForm(T controller, Activity activity) {
        super(controller, activity);

        setFormContainerId(R.id.content_view);
        this.form = null;
    }

    public Object getForm() {
        return form;
    }

    public void setForm(Object form) {
        this.form = form;
    }

    /**
     *
     */
    protected void setContentViewResource() {
        setContentViewResId(R.layout.form_frame);
    }

    public void setFormContainerId(int formContainerId) {
        this.formContainerId = formContainerId;
    }

    public boolean exitAfterSaveAction() {
        return exitAfterSaveAction;
    }

    public void setExitAfterSaveAction(boolean exitAfterSaveAction) {
        this.exitAfterSaveAction = exitAfterSaveAction;
    }

    public boolean isErrorHandled() {
        return errorHandled;
    }

    public void setErrorHandled(boolean errorHandled) {
        this.errorHandled = errorHandled;
    }

    @Override
    public synchronized JSONObject getStep(String name) {
        synchronized (mJSONObject) {
            try {
                return mJSONObject.getJSONObject(name);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public String currentJsonState() {
        synchronized (mJSONObject) {
            return mJSONObject.toString();
        }
    }

    @Override
    public String getCount() {
        return "1";
    }

    @Override
    public void writeValue(String stepName, String key, String value) throws JSONException {
        synchronized (mJSONObject) {
            try {
                JSONObject jsonObject = mJSONObject.getJSONObject(stepName);
                JSONArray fields = jsonObject.getJSONArray("fields");
                for (int i = 0; i < fields.length(); i++) {
                    JSONObject item = fields.getJSONObject(i);
                    String keyAtIndex = item.getString("key");
                    if (key.equals(keyAtIndex)) {
                        getJsonFormFragment().addUserInputValueToMetadata(key, null, value);

                        String oldValue = null;

                        if (item.has("value"))
                            oldValue = item.getString("value");

                        if (null == oldValue || !oldValue.equals(value)) {
                            item.put("value", value);
                            onFieldDataDirty(key, null, value);
                        }
                        return;
                    }
                }
            }
            catch (Exception e) {}
        }
    }

    protected void onFieldDataDirty(String key, String childKey, java.lang.Object value) {
        setDirty(true);
        getJsonFormFragment().onValueChange(key, childKey, value);
    }

    @Override
    public void writeValue(String stepName, String parentKey, String childObjectKey, String childKey, String value)
            throws JSONException {
        synchronized (mJSONObject) {
            try {
                JSONObject jsonObject = mJSONObject.getJSONObject(stepName);
                JSONArray fields = jsonObject.getJSONArray("fields");
                for (int i = 0; i < fields.length(); i++) {
                    JSONObject item = fields.getJSONObject(i);
                    String keyAtIndex = item.getString("key");
                    if (parentKey.equals(keyAtIndex)) {
                        JSONArray jsonArray = item.getJSONArray(childObjectKey);
                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONObject innerItem = jsonArray.getJSONObject(j);
                            String anotherKeyAtIndex = innerItem.getString("key");
                            if (childKey.equals(anotherKeyAtIndex)) {
                                // add value to the metadata
                                getJsonFormFragment().addUserInputValueToMetadata(parentKey, childKey, value);

                                String oldValue = null;

                                if (innerItem.has("value"))
                                    oldValue = innerItem.getString("value");

                                if (null == oldValue || !oldValue.equals(value)) {
                                    innerItem.put("value", value);
                                    onFieldDataDirty(parentKey, childKey, value);
                                }
                                return;
                            }
                        }
                    }
                }
            }
            catch (Exception e) {}
        }
    }


    protected void loadFormData(Intent intent) {
        json = intent.getStringExtra(Constants.EXTRA_KEY_JSON);
    }

    protected void processData(Intent intent) {
        if (null != getForm()) {
            if (form != null) {
                if (form instanceof FormItem)
                    jsonForm = ((FormItem) form).toJsonForm();
                else if (form instanceof Map)
                    jsonForm = FormHelper.createForm((Map) form);
                else
                    throw new IllegalStateException("Form data must be derived from a Map class or implemented FormItem interface");
            }

            intent.putExtra(Constants.EXTRA_KEY_JSON, jsonForm.toString());
        }
    }

    @Override
    public void bindData() {
        super.bindData();

        if (getController().getParcel() != null && getForm() == null) {
            if (getController().getParcel() instanceof Map) {
                Map map = (Map) getController().getParcel();
                if (map.containsKey(Constants.DATA)) {
                    setForm(map.get(Constants.DATA));

                    if (map.containsKey(Constants.EXTRA_KEY_EDITABLE))
                        editable = (boolean) map.get(Constants.EXTRA_KEY_EDITABLE);
                }
                else
                    setForm(map);
            }
            else if (getController().getParcel() instanceof FormItem)
                setForm((FormItem) getController().getParcel());
        }
    }

    @Override
    public void bindData(Intent intent) {
        super.bindData(intent);

        if (null == json) {
            processData(intent);

            loadFormData(intent);
        }

        editable = intent.getBooleanExtra(Constants.EXTRA_KEY_EDITABLE, true);
    }

    @Override
    public void onDataBound() {
        super.onDataBound();

        if (null != json) {
            load(json);
            FormFragment jsonFormFragment = createFragmentJsonForm();

            if (editable && getJsonForm().getFormState() == FormState.State.NONE) {
                getJsonForm().setFormState(FormState.State.NEW);
            }

            jsonFormFragment.setEditable(editable);

            // make sure we use the same data pointer when we load the data or save the data
            jsonFormFragment.setForm(getForm());
            jsonFormFragment.setJsonApi(this);

            replaceFragment(formContainerId, jsonFormFragment, FormFragment.FRAGMENT_JSON_FORM_TAG);
        }
    }

    public FormFragment getJsonFormFragment() {
        if (getFragmentCount() == 0)
            createFragmentJsonForm();

        return (FormFragment) getFragment(0);
    }

    private FormFragment createFragmentJsonForm() {
        if (getFragmentCount() > 0)
            removeFragments();

        FormFragment jsonFormFragment = (FormFragment) FormFragment.getFormFragment(JsonFormConstants.FIRST_STEP_NAME);
        addFragmentToList(jsonFormFragment);
        return jsonFormFragment;
    }

    public void load(String json) {
        try {
            mJSONObject = new JSONObject(json);
        } catch (JSONException e) {
            Log.d(TAG, "Initialization error. Json passed is invalid : " + e.getMessage());
        }
    }

    public String getCurrentKey() {
        return getJsonFormFragment().getCurrentKey();
    }

    protected boolean checkBeforeSave() {
        return getJsonFormFragment().validateForm() && checkOthers();
    }

    protected boolean checkOthers() {
        return true;
    }

    protected void goBack() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(MenuInflater menuInflater, Menu menu) {
        createMenuItemEditSave(menuInflater, menu);
        return super.onCreateOptionsMenu(menuInflater, menu);
    }

    private void createMenuItemEditSave(MenuInflater menuInflater, Menu menu) {
        // TODO
        // implement this function
    }

    @Override
    protected boolean onMenuCreated(Menu menu) {
        super.onMenuCreated(menu);

        setEditOrSave();

        return true;
    }

    protected void setEditOrSave() {
        // if (editable)
            // setMenuItemSave();
        // else
            // setMenuItemNumberTwo();
    }

    protected boolean isNewForm() {
        return isNewForm(getJsonForm());
    }

    protected static boolean isNewForm(JsonForm form) {
        return form.getFormState() == FormState.State.NEW
                || form.getFormState() == FormState.State.AUTO_FILLED;
    }

    public void setFormEditable(boolean editable) {
        this.editable = !editable;
        getJsonFormFragment().setFormEditable(this.editable);
    }

    protected void onFormEditStateChange(boolean editable) {
        if (editable) {
            if (!saveAndFinish())
                return; // if we fail the saving, we are not gonna change the editing state
        }
        else
            getJsonForm().setFormState(FormState.State.UPDATING);

        //
        setFormEditable(editable);

    }

    /**
     * can exit the page without filling the data
     *
     * @return true/false
     */
    protected boolean isExitable() {
        return !isNewForm() && !isDirty();
    }

    protected boolean saveAndFinish() {
        if (isExitable()) {
            if (exitAfterSaveAction())
                finish();
            return true;
        }

        // form is dirty
        if (checkBeforeSave()) {
            // getToolBarMenu().setMenuItemOneVisible(false);
            save();

            onFormSaved();

            if (exitAfterSaveAction())
                finish();

            return true;
        }

        onFormCheckFailed();
        return false;
    }

    protected void onFormSaved() {
        // no ops
    }

    protected abstract void onFormCheckFailed();

    protected void save() {
        if (!isNewForm())
            jsonForm.setFormState(FormState.State.UPDATED);

        // need an explanation here
        if (exitAfterSaveAction())
            setResult(getForm());

        saveFormData(getForm());

        setDirty(false);
    }

    protected abstract void saveFormData(Object form);

    @Override
    public boolean isEditable() {
        return editable;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public String getPredefinedValue(String stepName, String key) {
        return null;
    }

    public String getPredefinedValueMax(String stepName, String key) {
        return null;
    }

    public String getPredefinedValueMin(String stepName, String key) {
        return null;
    }

    public abstract void onFormClick(Context context, String key, String text);

    public JsonForm getJsonForm() {
        return jsonForm;
    }

}
