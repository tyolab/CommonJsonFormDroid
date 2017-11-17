package au.com.tyo.json.android.interfaces;

import android.content.Context;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Spinner;

import com.rey.material.widget.Switch;

/**
 * Created by vijay on 5/17/15.
 *
 */
public interface CommonListener extends View.OnClickListener,
        CompoundButton.OnCheckedChangeListener,
        Spinner.OnItemSelectedListener,
        Switch.OnCheckedChangeListener, OnFieldStateChangeListener, View.OnFocusChangeListener {

    void onUserInputFieldClick(Context context, String key, String text);
}
