/*
 * Copyright (c) 2019. TYONLINE TECHNOLOGY PTY. LTD. (TYOLAB)
 *
 */

package au.com.tyo.json.android.validators;

import java.util.regex.Pattern;

import au.com.tyo.json.validator.Validator;

public class RegexpValidator extends Validator {

    private Pattern pattern;

    public RegexpValidator(String err, String patternStr) {
        super(err);

        pattern = Pattern.compile(patternStr);
    }

    @Override
    public boolean isValid(Object object) {
        if (object instanceof CharSequence) {
            CharSequence text = (CharSequence) object;
            return pattern.matcher(text).matches();
        }
        return false;
    }

}
