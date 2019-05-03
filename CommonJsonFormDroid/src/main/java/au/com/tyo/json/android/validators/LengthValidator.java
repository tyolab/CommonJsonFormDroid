package au.com.tyo.json.android.validators;


import java.util.Collection;

import au.com.tyo.json.validator.Validator;

public class LengthValidator extends Validator {

    int minLength = 0;
    int maxLength = Integer.MAX_VALUE;

    public LengthValidator(String errorMessage, int minLength, int maxLength) {
        super(errorMessage);
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    @Override
    public boolean isValid(Object text) {
        int length = 0;
        if (text instanceof CharSequence) {
            CharSequence charSequence = (CharSequence) text;
            if (Character.isDigit(charSequence.charAt(0))) {
                length = Integer.valueOf(charSequence.toString());
            }
            else
                length = charSequence.length();
        }
        else if (text instanceof Collection)
            length = ((Collection) text).size();
        else if (text instanceof Object[])
            length = ((Object[]) text).length;
        else if (text instanceof Long)
            length = (int) text;
        else if (text instanceof Integer)
            length = (int) text;

        return length >= minLength && length <= maxLength;
    }

}
