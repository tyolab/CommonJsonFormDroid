package au.com.tyo.json.android.validators;


import android.text.TextUtils;

import au.com.tyo.json.validator.Validator;

/**
 * Created by vijay.rawat01 on 7/21/15.
 */
public class RequiredValidator extends Validator {

    public RequiredValidator(String errorMessage) {
    }

    public boolean isValid(Object  charSequence, boolean isEmpty) {
        return !isEmpty;
    }

    @Override
    public boolean isValid(Object text) {
        if (text instanceof String)
            return !TextUtils.isEmpty((CharSequence) text);
        return null != text;
    }

}
