/*
 * Copyright (c) 2017 TYONLINE TECHNOLOGY PTY. LTD. (TYO Lab)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package au.com.tyo.json.android.widgets;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import au.com.tyo.json.android.R;
import au.com.tyo.android.utils.SimpleDateUtils;
import au.com.tyo.json.android.interfaces.CommonListener;
import au.com.tyo.json.android.interfaces.JsonApi;
import au.com.tyo.utils.StringUtils;

import static au.com.tyo.json.JsonFormFieldButton.PICK_DATE;
import static au.com.tyo.json.JsonFormFieldButton.PICK_TIME;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 14/8/17.
 */

public class TitledDatePickerFactory extends TitledButtonFactory {

    public static final String KEY = TitledDatePickerFactory.class.getSimpleName();

    private static final String TAG = "TitledDatePickerFactory";

    private void setDatePickerMinMax(DatePicker datePicker, long finalMax, long finalMin) {

        if (finalMax > -1)
            datePicker.setMaxDate(finalMax);

        if (finalMin > -1)
            datePicker.setMinDate(finalMin);
    }

    @Override
    protected View getUserInputView(final JsonApi jsonApi, LayoutInflater factory, ViewGroup parent, final String stepName, final JSONObject jsonObject, final CommonListener listener, boolean editable) throws JSONException {
        View view = super.getUserInputView(jsonApi, factory, parent, stepName, jsonObject, listener, editable);
        String dateString = jsonObject.getString("text");
        final String key = jsonObject.getString("key");
        final String title = jsonObject.getString("title");
        final int pick = jsonObject.getInt("pick");

        Date date;
        try {
            date = SimpleDateUtils.parseSlashDelimAussieDate(dateString);
        } catch (ParseException e) {
            date = Calendar.getInstance(TimeZone.getDefault()).getTime();
        }

        final TextView buttonText = (TextView) view.findViewById(R.id.button_text);

        final int y, m, day;
        Calendar cal = null;
        cal = Calendar.getInstance(TimeZone.getDefault());
        cal.setTime(date);
        y = cal.get(Calendar.YEAR);
        m = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
        long max = -1, min = -1;
        if (jsonObject.has("max"))
            max = jsonObject.getLong("max");
        if (jsonObject.has("min"))
            min = jsonObject.getLong("min");

        view.setClickable(true);
        //view.setEnabled(false);
        final long finalMax = max;
        final long finalMin = min;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonApi.getJsonFormFragment().onDateWidgetClick(v);

                String jsonDateString = jsonApi.getPredefinedValue(stepName, key);
                String jsonDateStringMax = jsonApi.getPredefinedValueMax(stepName, key);
                String jsonDateStringMin = jsonApi.getPredefinedValueMin(stepName, key);

                Date minDate = null, maxDate = null;
                long tmax = -1, tmin = -1, targetMax = -1, targetMin = -1;
                if (jsonDateStringMin != null) {
                    minDate = SimpleDateUtils.fromJSONDate(jsonDateStringMin);
                    tmin = minDate.getTime();
                }
                targetMin = Math.max(finalMin, tmin);

                if (jsonDateStringMax != null) {
                    maxDate = SimpleDateUtils.fromJSONDate(jsonDateStringMax);
                    tmax = maxDate.getTime();
                }
                targetMax = Math.min(finalMax > -1 ? finalMax : tmax, tmax > -1 ? tmax : finalMax);

                Date defaultDate = null;
                if (jsonDateString != null)
                    defaultDate = SimpleDateUtils.fromJSONDate(jsonDateString);

                if (pick == PICK_DATE) {
                    final DatePickerDialog dialog;
                    dialog = new DatePickerDialog(v.getContext(), R.style.Theme_AppCompat_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            Calendar cal = Calendar.getInstance(TimeZone.getDefault());
                            cal.set(year, month, day, 0, 0);
                            String str = SimpleDateUtils.toSlashDelimAussieDate(cal.getTime());
                            String jsonDate = SimpleDateUtils.toJSONDateString(cal.getTime());
                            buttonText.setText(str);
                            listener.onValueChange(key, null, str);
                            try {
                                jsonApi.writeValue(stepName, key, jsonDate);
                            } catch (JSONException e) {
                                Log.e(TAG, StringUtils.exceptionStackTraceToString(e));
                            }
                        }
                    }, y, m, day); //, myDateListener, year, month, day).show();

                    setDatePickerMinMax(dialog.getDatePicker(), targetMax, targetMin);

                    dialog.setTitle(title);
                    dialog.show();
                }
                else if (pick == PICK_TIME){
                    final Calendar cal = Calendar.getInstance(TimeZone.getDefault());
                    int hour = cal.get(Calendar.HOUR_OF_DAY);
                    int minute = cal.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            cal.set(Calendar.HOUR_OF_DAY, selectedHour);
                            cal.set(Calendar.MINUTE, selectedMinute);

                            String jsonDate = SimpleDateUtils.toJSONDateString(cal.getTime());
                            String value = selectedHour + ":" + selectedMinute;
                            buttonText.setText(value);
                            listener.onValueChange(key, null, value);
                            try {
                                jsonApi.writeValue(stepName, key, jsonDate);
                            } catch (JSONException e) {
                                Log.e(TAG, StringUtils.exceptionStackTraceToString(e));
                            }
                        }
                    }, hour, minute, true); //Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                }
                else {
                    final View dialogView = View.inflate(v.getContext(), R.layout.date_time_picker, null);
                    final AlertDialog alertDialog = new AlertDialog.Builder(v.getContext(), R.style.Theme_AppCompat_Light_Dialog).create();

                    Calendar defaultCal = Calendar.getInstance(TimeZone.getDefault());

                    if (null!= defaultDate)
                        defaultCal.setTime(defaultDate);

                    final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
                    final TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);

                    datePicker.updateDate(defaultCal.get(Calendar.YEAR), defaultCal.get(Calendar.MONTH), defaultCal.get(Calendar.DAY_OF_MONTH));
                    setDatePickerMinMax(datePicker, targetMax, targetMin);
                    timePicker.setCurrentHour(defaultCal.get(Calendar.HOUR_OF_DAY));
                    timePicker.setCurrentMinute(defaultCal.get(Calendar.MINUTE));

                    dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Calendar cal = new GregorianCalendar(datePicker.getYear(),
                                    datePicker.getMonth(),
                                    datePicker.getDayOfMonth(),
                                    timePicker.getCurrentHour(),
                                    timePicker.getCurrentMinute());

                            Date date = cal.getTime();
                            String str = jsonApi.formatDateTime(key, date);
                            String jsonDate = SimpleDateUtils.toJSONDateString(date);
                            buttonText.setText(str);
                            listener.onValueChange(key, null, str);
                            try {
                                jsonApi.writeValue(stepName, key, jsonDate);
                            } catch (JSONException e) {
                                Log.e(TAG, StringUtils.exceptionStackTraceToString(e));
                            }
                            alertDialog.dismiss();
                        }});
                    alertDialog.setView(dialogView);
                    alertDialog.show();
                }
            }
        });
        return view;
    }
}
