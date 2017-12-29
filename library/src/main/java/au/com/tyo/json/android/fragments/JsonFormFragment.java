package au.com.tyo.json.android.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.rey.material.widget.EditText;
import com.rey.material.widget.Switch;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import au.com.tyo.json.android.R;
import au.com.tyo.json.android.activities.JsonFormActivity;
import au.com.tyo.json.android.customviews.RadioButton;
import au.com.tyo.json.android.interfaces.CommonListener;
import au.com.tyo.json.android.interfaces.JsonApi;
import au.com.tyo.json.android.mvp.MvpFragment;
import au.com.tyo.json.android.presenters.JsonFormFragmentPresenter;
import au.com.tyo.json.android.views.JsonFormFragmentView;
import au.com.tyo.json.android.viewstates.JsonFormFragmentViewState;

/**
 * Created by vijay on 5/7/15.
 */
public class JsonFormFragment extends MvpFragment<JsonFormFragmentPresenter, JsonFormFragmentViewState> implements
        CommonListener, JsonFormFragmentView<JsonFormFragmentViewState> {
    private static final String TAG = "JsonFormFragment";
    private LinearLayout        mMainView;
    private Menu                mMenu;
    private JsonApi             mJsonApi;
    private boolean             keyboardHidden = true;

    public void setJsonApi(JsonApi jsonApi) {
        this.mJsonApi = jsonApi;
    }

    public JsonApi getJsonApi() {
        return mJsonApi;
    }

    public boolean isKeyboardHidden() {
        return keyboardHidden;
    }

    public void setKeyboardHidden(boolean keyboardHidden) {
        this.keyboardHidden = keyboardHidden;
    }

    @Override
    public void onAttach(Activity activity) {
        if (activity instanceof JsonApi)
            mJsonApi = (JsonApi) activity;
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_json_wizard, null);
        mMainView = (LinearLayout) rootView.findViewById(R.id.main_layout);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.addFormElements(mJsonApi, mJsonApi.isEditable());
    }

    @Override
    protected JsonFormFragmentViewState createViewState() {
        return new JsonFormFragmentViewState();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        if (getActivity() instanceof JsonFormActivity) {
            mMenu = menu;
            menu.clear();
            inflater.inflate(R.menu.menu_toolbar, menu);
            presenter.setUpToolBar();
        }
    }

    @Override
    public void setActionBarTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar)
            actionBar.setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            presenter.onBackClick();
            return true;
        } else if (item.getItemId() == R.id.action_next) {
            presenter.onNextClick(mMainView);
            return true;
        } else if (item.getItemId() == R.id.action_save) {
            presenter.onSaveClick(mMainView);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        mJsonApi.onFieldClick(v);

        presenter.onClick(v);

        hideKeyBoard();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDetach() {
        mJsonApi = null;
        super.onDetach();
    }

    @Override
    public void updateRelevantImageView(Bitmap bitmap, String imagePath, String currentKey) {
        int childCount = mMainView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = mMainView.getChildAt(i);
            if (view instanceof ImageView) {
                ImageView imageView = (ImageView) view;
                String key = (String) imageView.getTag(R.id.key);
                if (key.equals(currentKey)) {
                    imageView.setImageBitmap(bitmap);
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setTag(R.id.imagePath, imagePath);
                }
            }
        }
    }

    @Override
    public void writeValue(String stepName, String key, String s) {
        try {
            mJsonApi.writeValue(stepName, key, s);
        } catch (JSONException e) {
            // TODO - handle
            e.printStackTrace();
        }
    }

    @Override
    public void writeValue(String stepName, String prentKey, String childObjectKey, String childKey, String value) {
        try {
            mJsonApi.writeValue(stepName, prentKey, childObjectKey, childKey, value);
        } catch (JSONException e) {
            // TODO - handle
            e.printStackTrace();
        }
    }

    @Override
    public JSONObject getStep(String stepName) {
        return mJsonApi.getStep(stepName);
    }

    @Override
    public String getCurrentJsonState() {
        return mJsonApi.currentJsonState();
    }

    @Override
    protected JsonFormFragmentPresenter createPresenter() {
        return new JsonFormFragmentPresenter();
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public CommonListener getCommonListener() {
        return this;
    }

    @Override
    public void addFormElements(List<View> views) {
        for (View view : views) {
            mMainView.addView(view);
        }
    }

    @Override
    public ActionBar getSupportActionBar() {
        if (getActivity() instanceof JsonFormActivity)
            return ((JsonFormActivity) getActivity()).getSupportActionBar();
        return null;
    }

    @Override
    public Toolbar getToolbar() {
        return ((JsonFormActivity) getActivity()).getToolbar();
    }

    @Override
    public void setToolbarTitleColor(int colorId) {
        getToolbar().setTitleTextColor(getContext().getResources().getColor(colorId));
    }

    @Override
    public void updateVisibilityOfNextAndSave(boolean next, boolean save) {
        mMenu.findItem(R.id.action_next).setVisible(next);
        mMenu.findItem(R.id.action_save).setVisible(save);
    }

    @Override
    public void hideKeyBoard() {
        if (!isKeyboardHidden()) {
            super.hideSoftKeyboard();
            setKeyboardHidden(true);
        }
    }

    @Override
    public void backClick() {
        getActivity().onBackPressed();
    }

    @Override
    public void unCheckAllExcept(String parentKey, String childKey) {
        int childCount = mMainView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = mMainView.getChildAt(i);
            if (view instanceof RadioButton) {
                RadioButton radio = (RadioButton) view;
                String parentKeyAtIndex = (String) radio.getTag(R.id.key);
                String childKeyAtIndex = (String) radio.getTag(R.id.childKey);
                if (parentKeyAtIndex.equals(parentKey) && !childKeyAtIndex.equals(childKey)) {
                    radio.setChecked(false);
                }
            }
        }
    }

    @Override
    public String getCount() {
        return mJsonApi.getCount();
    }

    @Override
    public void finishWithResult(Intent returnIntent) {
        getActivity().setResult(Activity.RESULT_OK, returnIntent);
        getActivity().finish();
    }

    @Override
    public void setUpBackButton() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void transactThis(JsonFormFragment next) {
        getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left,
                        R.anim.exit_to_right).replace(R.id.container, next)
                .addToBackStack(next.getClass().getSimpleName()).commit();
    }

    public static JsonFormFragment getFormFragment(String stepName) {
        JsonFormFragment jsonFormFragment = new JsonFormFragment();
        Bundle bundle = new Bundle();
        bundle.putString("stepName", stepName);
        jsonFormFragment.setArguments(bundle);
        return jsonFormFragment;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        presenter.onCheckedChanged(buttonView, isChecked);

        buttonView.requestFocus();
        if (!isKeyboardHidden())
            hideKeyBoard();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        presenter.onItemSelected(parent, view, position, id);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onCheckedChanged(Switch view, boolean checked) {
        presenter.onSwitchOnOrOff(view, checked);
    }

    public LinearLayout getMainView() {
        return mMainView;
    }

    @Override
    public void onInitialValueSet(String parentKey, String childKey, String value) {
        // no ops
    }

    @Override
    public void onValueChange(String parentKey, String childKey, Object value) {
        // no ops
    }

    @Override
    public void onVisibilityChange(String key, String o, boolean b) {
        // no ops
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (((v instanceof EditText) || (v instanceof android.widget.EditText))) {
            if (hasFocus) {
                setKeyboardHidden(false);
            }
            else {
                hideKeyBoard();
            }
        }
    }

    @Override
    public void onUserInputFieldClick(Context context, String key, String text) {
        // no ops
    }
}
