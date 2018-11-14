package au.com.tyo.json.android.interfaces;

import android.content.Context;
import android.view.View;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by vijay on 24-05-2015.
 */
public interface FormWidgetFactory {

    List<View> getViewsFromJson(JsonApi jsonApi, String stepName, Context context, JSONObject jsonObject, CommonListener listener, boolean editable, MetaDataWatcher metaDataWatcher) throws Exception;

}
