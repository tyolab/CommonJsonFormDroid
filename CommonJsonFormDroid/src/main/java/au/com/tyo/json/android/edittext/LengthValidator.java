package au.com.tyo.json.android.edittext;

import android.widget.AutoCompleteTextView;

/**
 * Created by vijay.rawat01 on 7/21/15.
 */
public class LengthValidator implements AutoCompleteTextView.Validator {

    int minLength = 0;
    int maxLength = Integer.MAX_VALUE;

    public LengthValidator(String errorMessage, int minLength, int maxLength) {
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    public boolean isValid(CharSequence charSequence, boolean isEmpty) {
        if(!isEmpty) {
            if(charSequence.length() >= minLength && charSequence.length() <= maxLength) {
                return true;
            }
        }
        return false;
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
