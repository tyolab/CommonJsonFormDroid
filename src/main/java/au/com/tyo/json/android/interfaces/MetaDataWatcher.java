package au.com.tyo.json.android.interfaces;

import android.view.View;

public interface MetaDataWatcher {

    void setKeyInputView(String key, View inputView, boolean editable, boolean enabled, int required);

    void setKeyWidgetView(String key, View v);
}
