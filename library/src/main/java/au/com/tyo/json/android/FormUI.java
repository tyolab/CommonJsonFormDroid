package au.com.tyo.json.android;

import au.com.tyo.app.ui.UI;

public interface FormUI extends UI {

    void editForm(Class activityClass, Object data, boolean editable);

    void showForm(Class activityClass, Object data);
    void editForm(Class activityClass, Object data);
}
