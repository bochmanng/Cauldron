package com.geromeb.cauldron;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;



public class DefaultDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private BalanceScreen balanceScreen;
    //    int mNum;

    public void setBalanceScreen(BalanceScreen balanceScreen) {
        this.balanceScreen = balanceScreen;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        String returnDate = String.valueOf(dayOfMonth) +
                "." +
                String.valueOf(monthOfYear + 1) +
                "." +
                String.valueOf(year);// +
//                ", 00:00";

        balanceScreen.setPayDate(returnDate);
        balanceScreen.writePayDate();
        balanceScreen.refresh();


    }
}