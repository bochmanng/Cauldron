package com.geromeb.cauldron;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class BalanceScreen extends ActionBarActivity {

    private int balance;
    private List<String> transactionHistory = new ArrayList<>();
    private BalanceMathFactory bMF = new BalanceMathFactory();
    private String payDate;


    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);

        readBalance();
        readTransactionHistory();
        readPayDate();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_payDate) {

            DefaultDatePicker datePicker = new DefaultDatePicker();
            datePicker.setBalanceScreen(this);
            datePicker.show(getFragmentManager(), "datePicker");
            return true;
        }

/*        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    protected void refresh() {
        updateEverything(1);

    }


    /*The methods add_amount() and sub_amount() are called by the respective buttons in the main screen.
    Their only purpose is to set the modifier before any calculations are done.*/
    public void add_amount(View view){
        int modifier = 1;
        updateEverything(modifier);

    }


    public void sub_amount(View view) {
        int modifier = -1;
        updateEverything(modifier);
    }


    private void updateEverything(int modifier) {
        // Get the numbers from views
        TextView balanceView = (TextView) findViewById(R.id.txt_balance);
        TextView transMeanView = (TextView) findViewById(R.id.txt_transMean);
        TextView transactionView = (EditText) findViewById(R.id.edt_income);
        TextView daysView = (TextView) findViewById(R.id.txt_daysLeft);
        TextView weeklyView = (TextView) findViewById(R.id.txt_budgetPerWeek);


        // Determine how many days are left until new money is paid in
        try {
            daysView.setText(String.valueOf(bMF.dateToDays(payDate)));
        } catch (ParseException e) {
            e.printStackTrace();
        }



        // Determine the current transaction value and transform it to cents
        String transaction = transactionView.getText().toString();
        transaction = bMF.getCentString(transaction);

        // Add transaction to balance, update transaction history, transaction mean and weekly budget
        balance = bMF.addToBalance(balance, transaction, modifier);
        transactionHistory.add(transaction);
        int transactionMean = bMF.calcTransactionMean(transactionHistory);
        int weeklyBudget = 0;
        try {
            weeklyBudget = bMF.calcWeeklyBudget(bMF.dateToDays(payDate), balance);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        // Set result in text views and reset input field
        balanceView.setText(bMF.intToCurrency(balance));
        transMeanView.setText(bMF.intToCurrency(transactionMean));
        transactionView.setText("");
        weeklyView.setText(bMF.intToCurrency(weeklyBudget));

        // Store the current numbers
        writeBalance();
        writeTransactionhistory();
    }


    protected void writePayDate() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(getString(R.string.payDate), payDate);
        editor.apply();
    }


    private void readPayDate() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        payDate = sharedPref.getString(getString(R.string.payDate), "0");

        TextView balanceView = (TextView) findViewById(R.id.txt_daysLeft);

        try {
            if (payDate.equals("0")){
                balanceView.setText("0");
            } else {
                balanceView.setText(Integer.toString(bMF.dateToDays(payDate)));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    private void writeBalance() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putInt(getString(R.string.balance), balance);
        editor.apply();
    }


    private void readBalance() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        balance = sharedPref.getInt(getString(R.string.balance), 0);

        TextView balanceView = (TextView) findViewById(R.id.txt_balance);
        balanceView.setText(bMF.intToCurrency(balance));
    }


    private void writeTransactionhistory() {
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        String historyString = "";

        for (String s : transactionHistory){
            historyString  = historyString + s + ";";
        }

        editor.putString(getString(R.string.transactionHistory), historyString);
        editor.apply();

    }

    private void readTransactionHistory() {
        String historyString;

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        historyString = sharedPref.getString(getString(R.string.transactionHistory), null);


        if (historyString != null) {
            transactionHistory = new ArrayList<>(Arrays.asList(historyString.split(";")));
        } else {
            transactionHistory = new ArrayList<>();
        }

        TextView transMeanView = (TextView) findViewById(R.id.txt_transMean);
        int outInt = bMF.calcTransactionMean(transactionHistory);

        transMeanView.setText(String.valueOf(outInt));

    }
}
