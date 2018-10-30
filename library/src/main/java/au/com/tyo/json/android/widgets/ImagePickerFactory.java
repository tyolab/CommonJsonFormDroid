package au.com.tyo.json.android.widgets;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import au.com.tyo.json.android.R;
import au.com.tyo.json.android.interfaces.CommonListener;
import au.com.tyo.json.android.utils.ImageUtils;
import au.com.tyo.json.android.utils.ValidationStatus;

import static au.com.tyo.json.android.utils.FormUtils.MATCH_PARENT;
import static au.com.tyo.json.android.utils.FormUtils.WRAP_CONTENT;
import static au.com.tyo.json.android.utils.FormUtils.dpToPixels;
import static au.com.tyo.json.android.utils.FormUtils.getLayoutParams;

/**
 * Created by vijay on 24-05-2015.
 */
public class ImagePickerFactory extends CompoundItemFactory {

    @Override
    protected void createCompoundView(LayoutInflater factory, ViewGroup parent, String stepName, JSONObject jsonObject, CommonListener listener, boolean editable) throws JSONException {
        Context context = parent.getContext();

        List<View> views = new ArrayList<>(1);
        ImageView imageView = new ImageView(context);
        imageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.grey_bg));
        imageView.setTag(R.id.key, jsonObject.getString("key"));
        imageView.setTag(R.id.type, jsonObject.getString("type"));

        JSONObject requiredObject = jsonObject.optJSONObject("v_required");
        if (requiredObject != null) {
            String requiredValue = requiredObject.getString("value");
            if (!TextUtils.isEmpty(requiredValue)) {
                imageView.setTag(R.id.v_required, requiredValue);
                imageView.setTag(R.id.error, requiredObject.optString("err"));
            }
        }

        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setLayoutParams(getLayoutParams(MATCH_PARENT, dpToPixels(context, 200), 0, 0, 0, (int) context
                .getResources().getDimension(R.dimen.form_default_bottom_margin)));
        String imagePath = jsonObject.optString("value");
        if (!TextUtils.isEmpty(imagePath)) {
            imageView.setTag(R.id.imagePath, imagePath);
            imageView.setImageBitmap(ImageUtils.loadBitmapFromFile(imagePath, ImageUtils.getDeviceWidth(context), dpToPixels(context, 200)));
        }
        views.add(imageView);
        Button uploadButton = new Button(context);
        uploadButton.setText(jsonObject.getString("uploadButtonText"));
        uploadButton.setLayoutParams(getLayoutParams(WRAP_CONTENT, WRAP_CONTENT, 0, 0, 0, (int) context
                .getResources().getDimension(R.dimen.form_default_bottom_margin)));
        uploadButton.setOnClickListener(listener);
        uploadButton.setTag(R.id.key, jsonObject.getString("key"));
        uploadButton.setTag(R.id.type, jsonObject.getString("type"));
        views.add(uploadButton);

        for (int i = 0; i < views.size(); ++i) {
            parent.addView(views.get(i));
        }
    }

    public static ValidationStatus validate(ImageView imageView) {
        if (!(imageView.getTag(R.id.v_required) instanceof String) || !(imageView.getTag(R.id.error) instanceof String)) {
            return new ValidationStatus(true, null);
        }
        Boolean isRequired = Boolean.valueOf((String) imageView.getTag(R.id.v_required));
        if (!isRequired) {
            return new ValidationStatus(true, null);
        }
        Object path = imageView.getTag(R.id.imagePath);
        if (path instanceof String && !TextUtils.isEmpty((String) path)) {
            return new ValidationStatus(true, null);
        }
        return new ValidationStatus(false, (String) imageView.getTag(R.id.error));
    }

}
