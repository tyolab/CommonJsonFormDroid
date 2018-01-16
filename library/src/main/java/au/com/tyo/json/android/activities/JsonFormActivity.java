package au.com.tyo.json.android.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import au.com.tyo.json.android.R;
import au.com.tyo.json.android.constants.JsonFormConstants;
import au.com.tyo.json.android.fragments.FormFragment;
import au.com.tyo.json.android.fragments.JsonFormFragment;
import au.com.tyo.json.android.interfaces.JsonApi;

/**
 * Please be aware, using this activity for json form is not fully implemented, particular when getting the form fragment
 */

public class JsonFormActivity extends AppCompatActivity implements JsonApi {

    private static final String TAG = "JsonFormActivity";

    private Toolbar             mToolbar;

    private JSONObject          mJSONObject;

    public void init(String json) {
        try {
            mJSONObject = new JSONObject(json);
        } catch (JSONException e) {
            Log.d(TAG, "Initialization error. Json passed is invalid : " + e.getMessage());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();
        createFragments(savedInstanceState);
    }

    protected void initialize() {
        setContentView(R.layout.activity_json_form);
        mToolbar = (Toolbar) findViewById(R.id.tb_top);
        setSupportActionBar(mToolbar);
    }

    protected void createFragments(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            init(getIntent().getStringExtra("json"));
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, JsonFormFragment.getFormFragment(JsonFormConstants.FIRST_STEP_NAME)).commit();
        } else {
            init(savedInstanceState.getString("jsonState"));
        }
    }

    public Toolbar getToolbar() {
        return mToolbar;
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
    public void writeValue(String stepName, String key, String value) throws JSONException {
        synchronized (mJSONObject) {
            JSONObject jsonObject = mJSONObject.getJSONObject(stepName);
            JSONArray fields = jsonObject.getJSONArray("fields");
            for (int i = 0; i < fields.length(); i++) {
                JSONObject item = fields.getJSONObject(i);
                String keyAtIndex = item.getString("key");
                if (key.equals(keyAtIndex)) {
                    item.put("value", value);
                    return;
                }
            }
        }
    }

    @Override
    public void writeValue(String stepName, String parentKey, String childObjectKey, String childKey, String value)
            throws JSONException {
        synchronized (mJSONObject) {
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
                            innerItem.put("value", value);
                            return;
                        }
                    }
                }
            }
        }
    }

    @Override
    public String currentJsonState() {
        synchronized (mJSONObject) {
            return mJSONObject.toString();
        }
    }

    @Override
    public String getCount() {
        synchronized (mJSONObject) {
            return mJSONObject.optString("count");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("jsonState", mJSONObject.toString());
    }

    @Override
    public boolean isEditable() {
        return true;
    }

    @Override
    public String formatDateTime(String key, Date date) {
        return null;
    }

    @Override
    public FormFragment getJsonFormFragment() {
        return null;
    }

    @Override
    public String getPredefinedValue(String stepName, String key) {
        return null;
    }

    @Override
    public String getPredefinedValueMax(String stepName, String key) {
        return null;
    }

    @Override
    public String getPredefinedValueMin(String stepName, String key) {
        return null;
    }

    @Override
    public void onFieldClick(View v) {
        // no ops
    }

    @Override
    public void onFieldValueClear(String key) {
        // no ops
    }
}
