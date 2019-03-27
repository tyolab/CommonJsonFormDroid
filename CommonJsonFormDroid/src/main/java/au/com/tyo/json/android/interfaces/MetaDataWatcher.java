package au.com.tyo.json.android.interfaces;

import android.view.View;

public interface MetaDataWatcher {

    void setKeyMappingView(String key, View widgetView, View inputView, boolean editable, boolean enabled, int required);

    void addFieldView(String key, View v);
}
