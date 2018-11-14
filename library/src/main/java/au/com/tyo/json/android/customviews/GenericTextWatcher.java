package au.com.tyo.json.android.customviews;

import android.support.v7.internal.widget.TintContextWrapper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import au.com.tyo.json.android.R;

import org.json.JSONException;

import au.com.tyo.json.android.interfaces.JsonApi;

public class GenericTextWatcher implements TextWatcher {

    private static final String TAG = GenericTextWatcher.class.getSimpleName();
    private View   mView;
    private String mStepName;

    protected JsonApi api;

    public GenericTextWatcher(JsonApi jsonApi, String stepName, View view) {
        mView = view;
        mStepName = stepName;
        api = jsonApi;
    }

    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    public void afterTextChanged(Editable editable) {
        try {
            String text = editable.toString().trim();
            String key = (String) mView.getTag(R.id.key);

            // don't it, we need to catch the empty text too
            // if (text.length() > 0) {
                if (null == api) {

                    if (mView.getContext() instanceof JsonApi) {
                        api = (JsonApi) mView.getContext();
                    } else if (mView.getContext() instanceof TintContextWrapper) {
                        TintContextWrapper tintContextWrapper = (TintContextWrapper) mView.getContext();
                        api = (JsonApi) tintContextWrapper.getBaseContext();
                    } else {
                        throw new RuntimeException("Could not get json api interface");
                    }
                }

                try {
                    api.writeValue(mStepName, key, text);
                } catch (JSONException e) {
                    // TODO- handle
                    Log.e(TAG, "Write form value error", e);
                }
            // }
        } catch (Exception e) {
            Log.e("GenericTextWatcher", "Error in catching text edit value");
        }

    }
}