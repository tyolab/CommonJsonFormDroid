/*
 * Copyright (c) 2019. TYONLINE TECHNOLOGY PTY. LTD. (TYOLAB)
 *
 */

package au.com.tyo.json.android.widgets;

import au.com.tyo.json.android.R;

public class ImageFactory extends CommonItemFactory {

    public ImageFactory(String widgetKey) {
        super(widgetKey);
        setLayoutResourceId(R.layout.item_imageview);
    }

    public ImageFactory() {
        setLayoutResourceId(R.layout.item_imageview);
    }

}
