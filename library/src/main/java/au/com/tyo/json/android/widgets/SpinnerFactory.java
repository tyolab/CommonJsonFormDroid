package au.com.tyo.json.android.widgets;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONObject;

import au.com.tyo.android.AndroidUtils;
import au.com.tyo.json.android.R;
import au.com.tyo.json.android.interfaces.CommonListener;
import au.com.tyo.json.android.interfaces.JsonApi;
import au.com.tyo.json.android.interfaces.MetaDataWatcher;
import au.com.tyo.json.android.utils.JsonMetadata;
import au.com.tyo.json.android.utils.ValidationStatus;

/**
 * Created by nipun on 30/05/15.
 */
public class SpinnerFactory extends CommonItemFactory {

    public SpinnerFactory(String widgetKey) {
        super(widgetKey);
    }

    public SpinnerFactory() {

    }

    @Override
    public View getViewFromJson(JsonApi jsonApi, String stepName, Context context, JSONObject jsonObject, JsonMetadata metadata, CommonListener listener, boolean editable, MetaDataWatcher metaDataWatcher) throws Exception {

        Spinner spinner = (Spinner) LayoutInflater.from(context).inflate(R.layout.item_spinner, null);

        // String hint = jsonObject.optString("hint");
        // if (!TextUtils.isEmpty(hint)) {
        //     spinner.setHint(jsonObject.getString("hint"));
        //     spinner.setFloatingLabelText(jsonObject.getString("hint"));
        // }

        spinner.setId(AndroidUtils.generateViewId());

        spinner.setTag(R.id.key, jsonObject.getString("key"));
        spinner.setTag(R.id.type, jsonObject.getString("type"));

        JSONObject requiredObject = jsonObject.optJSONObject("v_required");
        if (requiredObject != null) {
            String requiredValue = requiredObject.getString("value");
            if (!TextUtils.isEmpty(requiredValue)) {
                spinner.setTag(R.id.v_required, requiredValue);
                spinner.setTag(R.id.error, requiredObject.optString("err"));
            }
        }

        String valueToSelect = "";
        int indexToSelect = -1;
        if (!TextUtils.isEmpty(jsonObject.optString("value"))) {
            valueToSelect = jsonObject.optString("value");
        }

        JSONArray valuesJson = jsonObject.optJSONArray("values");
        String[] values = null;
        if (valuesJson != null && valuesJson.length() > 0) {
            values = new String[valuesJson.length()];
            for (int i = 0; i < valuesJson.length(); i++) {
                values[i] = valuesJson.optString(i);
                if (valueToSelect.equals(values[i])) {
                    indexToSelect = i;
                }
            }
        }

        if (values != null) {
            spinner.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, values));
            spinner.setSelection(indexToSelect + 1, true);
            spinner.setOnItemSelectedListener(listener);
        }
        return (spinner);
    }

    public static ValidationStatus validate(Spinner spinner) {
        if (!(spinner.getTag(R.id.v_required) instanceof String) || !(spinner.getTag(R.id.error) instanceof String)) {
            return new ValidationStatus(true, null);
        }
        Boolean isRequired = Boolean.valueOf((String) spinner.getTag(R.id.v_required));
        if (!isRequired) {
            return new ValidationStatus(true, null);
        }
        int selectedItemPosition = spinner.getSelectedItemPosition();
        if(selectedItemPosition > 0) {
            return new ValidationStatus(true, null);
        }
        return new ValidationStatus(false, (String) spinner.getTag(R.id.error));
    }
}
