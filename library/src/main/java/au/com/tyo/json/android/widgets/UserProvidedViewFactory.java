package au.com.tyo.json.android.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import au.com.tyo.json.android.R;
import au.com.tyo.json.android.interfaces.CommonListener;
import au.com.tyo.json.android.interfaces.JsonApi;
import au.com.tyo.json.android.interfaces.MetaDataWatcher;

public class UserProvidedViewFactory extends CommonItemFactory {

    public UserProvidedViewFactory(String widgetKey) {
        super(widgetKey);
    }

    public UserProvidedViewFactory() {
        super(UserProvidedViewFactory.class.getSimpleName());
    }

    @Override
    public List<View> getViewsFromJson(JsonApi jsonApi, String stepName, Context context, JSONObject jsonObject, CommonListener listener, boolean editable, MetaDataWatcher metaDataWatcher) throws Exception {
        List<View> views = new ArrayList<>(1);

        int resId = jsonObject.getInt("value");
        LayoutInflater factory = LayoutInflater.from(context);

        View v = factory.inflate(resId, null);

        View userInputView = v.findViewById(R.id.user_input);
        if (null != userInputView)
            metaDataWatcher.setUserInputView(jsonObject.getString("key"), userInputView, editable, -1);
        views.add(v);

        return views;
    }

}
