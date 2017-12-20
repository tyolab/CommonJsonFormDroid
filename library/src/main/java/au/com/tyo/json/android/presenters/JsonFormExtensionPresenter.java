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

package au.com.tyo.json.android.presenters;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import com.rengwuxian.materialedittext.MaterialEditText;

import au.com.tyo.app.ui.page.Page;
import au.com.tyo.json.android.R;
import au.com.tyo.json.android.fragments.FormFragment;
import au.com.tyo.json.android.utils.ValidationStatus;
import au.com.tyo.json.android.widgets.EditTextFactory;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 31/7/17.
 */

public class JsonFormExtensionPresenter extends JsonFormFragmentPresenter {

    @Override
    public void onClick(View v) {
        String key = (String) v.getTag(R.id.key);
        String type = (String) v.getTag(R.id.pick);
        setCurrentKey(key);

        ((FormFragment) getView()).onUserInputFieldClick(v.getContext(), key, type);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && null != data) {
            FormFragment fragmentJsonForm = (FormFragment) getView();
            Object result = Page.getActivityResult(data);
            fragmentJsonForm.updateForm(getCurrentKey(), result);
        }
    }

    @Override
    public void setUpToolBarTitleColor() {
        // no ops
    }

    public boolean validate(LinearLayout mainView) {
        ValidationStatus validationStatus = writeValuesAndValidate(mainView);
        return validationStatus.isValid();
    }

    public ValidationStatus writeValuesAndValidate(LinearLayout mainView) {
        int childCount = mainView.getChildCount();
        // String stepName = getStepName();
        for (int i = 0; i < childCount; i++) {
            View childAt = mainView.getChildAt(i);
            View view = childAt.findViewById(R.id.user_input);
            if (null == view)
                view = childAt;

            String key = (String) view.getTag(R.id.key);
            if (view instanceof MaterialEditText) {
                MaterialEditText editText = (MaterialEditText) view;
                ValidationStatus validationStatus = EditTextFactory.validate(editText);
                if (!validationStatus.isValid()) {
                    return validationStatus;
                }
            }
        }
        return new ValidationStatus(true, null);
    }
}
