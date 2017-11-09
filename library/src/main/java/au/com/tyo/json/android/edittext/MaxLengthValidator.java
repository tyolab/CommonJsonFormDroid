package au.com.tyo.json.android.edittext;

import au.com.tyo.json.android.widgets.EditTextFactory;

/**
 * Created by vijay.rawat01 on 7/21/15.
 */
public class MaxLengthValidator extends LengthValidator {

    public MaxLengthValidator(String errorMessage, int maxLength) {
        super(errorMessage, EditTextFactory.MIN_LENGTH, maxLength);
    }
}
