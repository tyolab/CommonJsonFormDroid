package au.com.tyo.json.android.interfaces;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;


/**
 * Created by vijay on 5/17/15.
 *
 */
public interface CommonListener extends View.OnClickListener,
        Spinner.OnItemSelectedListener,
        CompoundButton.OnCheckedChangeListener, OnFieldStateChangeListener, View.OnFocusChangeListener {

    void onCheckedChanged(Switch view, boolean checked);

    boolean onUserInputFieldClick(View context, String key, String text);
}
