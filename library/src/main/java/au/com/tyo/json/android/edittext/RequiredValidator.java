package au.com.tyo.json.android.edittext;


import android.widget.AutoCompleteTextView;

/**
 * Created by vijay.rawat01 on 7/21/15.
 */
public class RequiredValidator implements AutoCompleteTextView.Validator {

    public RequiredValidator(String errorMessage) {
    }

    public boolean isValid(CharSequence charSequence, boolean isEmpty) {
        return !isEmpty;
    }

    @Override
    public boolean isValid(CharSequence text) {
        return isValid(text, false);
    }

    @Override
    public CharSequence fixText(CharSequence invalidText) {
        return null;
    }
}
