package au.com.tyo.json.android.edittext;

import au.com.tyo.json.android.widgets.EditTextFactory;

/**
 * Created by vijay.rawat01 on 7/21/15.
 */
public class MinLengthValidator extends LengthValidator {

    public MinLengthValidator(String errorMessage, int minLength) {
        super(errorMessage, minLength, EditTextFactory.MAX_LENGTH);
    }
}
