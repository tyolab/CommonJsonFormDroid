package au.com.tyo.json.android.customviews;

import android.support.v7.widget.TintContextWrapper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import au.com.tyo.json.android.R;

import org.json.JSONException;

import au.com.tyo.app.CommonApp;
import au.com.tyo.app.Controller;
import au.com.tyo.json.android.interfaces.JsonApi;

public class GenericTextWatcher implements TextWatcher {

    private View   mView;
    private String mStepName;

    public GenericTextWatcher(String stepName, View view) {
        mView = view;
        mStepName = stepName;
    }

    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    public void afterTextChanged(Editable editable) {
        try {
            String text = editable.toString().trim();

            // never do it, we need to catch the empty text too
            // if (text.length() > 0) {
                JsonApi api = null;

                String key = (String) mView.getTag(R.id.key);

                Controller controller = (Controller) CommonApp.getInstance();
                if (controller.getUi().getCurrentPage() instanceof JsonApi) {
                    api = (JsonApi) controller.getUi().getCurrentPage();
                }
                else if (mView.getContext() instanceof JsonApi) {
                    api = (JsonApi) mView.getContext();
                }
                else if (mView.getContext() instanceof TintContextWrapper) {
                    TintContextWrapper tintContextWrapper = (TintContextWrapper) mView.getContext();
                    api = (JsonApi) tintContextWrapper.getBaseContext();
                }
                else {
                    throw new RuntimeException("Could not fetch context");
                }

                try {
                    api.writeValue(mStepName, key, text);
                } catch (JSONException e) {
                    // TODO- handle
                    e.printStackTrace();
                }
            // }
        } catch (Exception e) {
            Log.e("GenericTextWatcher", "Error in catching text edit value");
        }

    }
}