package au.com.tyo.json.android.validators;

import au.com.tyo.json.android.widgets.EditTextFactory;

/**
 * Created by vijay.rawat01 on 7/21/15.
 */
public class MaxValidator extends LengthValidator {

    public MaxValidator(String errorMessage, int maxLength) {
        super(errorMessage, 0, maxLength);
    }
}
