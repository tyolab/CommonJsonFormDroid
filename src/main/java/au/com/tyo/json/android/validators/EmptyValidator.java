/*
 * Copyright (c) 2019. TYONLINE TECHNOLOGY PTY. LTD. (TYOLAB)
 *
 */

package au.com.tyo.json.android.validators;

import au.com.tyo.json.validator.Validator;

public class EmptyValidator extends Validator {

    public EmptyValidator(String err) {
        super(err);
    }

    public boolean isValid(Object object) {
        return true;
    }

}
