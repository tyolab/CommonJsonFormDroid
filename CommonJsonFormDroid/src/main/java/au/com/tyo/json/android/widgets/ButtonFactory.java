/*
 * Copyright (c) 2019. TYONLINE TECHNOLOGY PTY. LTD. (TYOLAB)
 *
 */

package au.com.tyo.json.android.widgets;

import au.com.tyo.json.android.R;

public class ButtonFactory extends CommonItemFactory {

    public static final String NAME =

    public ButtonFactory(String widgetKey) {
        super(widgetKey);
        setLayoutResourceId(R.layout.item_button);
    }

    public ButtonFactory() {
        this()
        setLayoutResourceId(R.layout.item_button);
    }

}
