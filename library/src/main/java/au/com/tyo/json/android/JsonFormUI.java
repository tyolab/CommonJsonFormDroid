/*
 * Copyright (c) 2018 TYONLINE TECHNOLOGY PTY. LTD. (TYO Lab)
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

package au.com.tyo.json.android;

import java.util.HashMap;
import java.util.Map;

import au.com.tyo.app.Constants;
import au.com.tyo.app.Controller;
import au.com.tyo.app.ui.UIBase;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 2/1/18.
 */

public class JsonFormUI<ControllerType extends Controller> extends UIBase<ControllerType> implements FormUI {

    public JsonFormUI(ControllerType controller) {
        super(controller);
    }

    @Override
    public void editForm(Class activityClass, Object data, boolean editable) {
        Map map;

        map = new HashMap();
        map.put(Constants.DATA, data);
        map.put(Constants.EXTRA_KEY_EDITABLE, editable);

        gotoPageWithData(activityClass, map);
    }

    @Override
    public void showForm(Class activityClass, Object data) {
        editForm(activityClass, data, false);
    }

    @Override
    public void editForm(Class activityClass, Object data) {
        editForm(activityClass, data, true);
    }
}
