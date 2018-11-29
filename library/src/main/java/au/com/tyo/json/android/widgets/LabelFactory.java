package au.com.tyo.json.android.widgets;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import au.com.tyo.json.android.R;
import au.com.tyo.json.android.interfaces.CommonListener;
import au.com.tyo.json.android.interfaces.JsonApi;
import au.com.tyo.json.android.interfaces.MetaDataWatcher;
import au.com.tyo.json.android.utils.JsonMetadata;

import org.json.JSONObject;

import static au.com.tyo.json.android.utils.FormUtils.FONT_BOLD_PATH;
import static au.com.tyo.json.android.utils.FormUtils.WRAP_CONTENT;
import static au.com.tyo.json.android.utils.FormUtils.getLayoutParams;
import static au.com.tyo.json.android.utils.FormUtils.getTextViewWith;

/**
 * Created by vijay on 24-05-2015.
 */
public class LabelFactory extends CommonItemFactory {

    public LabelFactory(String widgetKey) {
        super(widgetKey);
    }

    public LabelFactory() {

    }

    @Override
    public View getViewFromJson(JsonApi jsonApi, String stepName, Context context, JSONObject jsonObject, JsonMetadata metadata, CommonListener listener, boolean editable, MetaDataWatcher metaDataWatcher) throws Exception {

        LinearLayout.LayoutParams layoutParams = getLayoutParams(WRAP_CONTENT, WRAP_CONTENT, 0, 0, 0, (int) context
                .getResources().getDimension(R.dimen.form_default_bottom_margin));
        return (getTextViewWith(context, 16, jsonObject.getString("text"), jsonObject.getString("key"),
                jsonObject.getString("type"), layoutParams, FONT_BOLD_PATH));
    }

}
