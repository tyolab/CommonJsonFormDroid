package au.com.tyo.json.android.validators;

import au.com.tyo.json.android.widgets.EditTextFactory;

/**
 * Created by vijay.rawat01 on 7/21/15.
 */
public class MinValidator extends LengthValidator {

    public MinValidator(String errorMessage, int minLength) {
        super(errorMessage, minLength, Integer.MAX_VALUE);
    }
}
