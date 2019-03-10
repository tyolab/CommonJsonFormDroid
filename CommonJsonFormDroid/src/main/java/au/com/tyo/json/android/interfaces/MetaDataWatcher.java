package au.com.tyo.json.android.interfaces;

import android.view.View;

public interface MetaDataWatcher {

    void setUserInputView(String key, View v, boolean editable, boolean enabled, int required);

    void addFieldView(String key, View v);
}
