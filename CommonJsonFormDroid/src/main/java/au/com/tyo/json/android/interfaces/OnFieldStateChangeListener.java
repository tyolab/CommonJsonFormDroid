package au.com.tyo.json.android.interfaces;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 20/10/17.
 */

public interface OnFieldStateChangeListener {

    void onInitialValueSet(String parentKey, String childKey, String value);
    void onValueChange(String parentKey, String childKey, Object value);
    void onVisibilityChange(String key, String o, boolean b);

}
