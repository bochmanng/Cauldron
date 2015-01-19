package com.geromeb.cauldron;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class BalanceMathFactory {

    public int calcTransactionMean(List<String> transactionHistory) {
        if (transactionHistory.size() > 0) {
            int sum = 0;

            for (String e : transactionHistory) {
                sum = sum + Integer.valueOf(e);
            }

            return sum/transactionHistory.size();
        } else {
            return 0;
        }
    }

    public int addToBalance(int balance, String transaction, int modifier) {
        return balance + (Integer.valueOf(transaction) * modifier);
    }

    public int dateToDays(String targetDate) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
            formatter.setLenient(false);

        Date today = new Date();
        long todayMs = today.getTime();
        Date target = formatter.parse(targetDate);
        long targetMs = target.getTime();

        // Convert milliseconds to number of days using Java's BigDecimal Class
        BigDecimal bd = new BigDecimal(targetMs - todayMs);
//        bd.setScale(0, BigDecimal.ROUND_HALF_UP); // don't keep the decimals and round up if decimal >= .5
        bd = bd.divide(new BigDecimal(1000 * 60 * 60 * 24),0,BigDecimal.ROUND_HALF_UP); // perform conversion


        return bd.intValue();
    }

/*    public int simpleMeanBudgetPerDay(String numDays, int balance){
        int d  = Integer.valueOf(numDays);

        return balance/d;
    }*/


    public String intToCurrency(int n) {
        BigDecimal decimal = new BigDecimal(n);
        decimal = decimal.divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP);

        return decimal.toString() + "€";

    }

    public String getCentString(String transaction) {

        if (transaction.equals("")) {return "0";}

        if (transaction.contains(".")) {
            transaction = transaction.replace(".", "");
        } else {
            transaction = Integer.toString(Integer.parseInt(transaction) * 100);
        }
        return transaction;
    }

    public int calcWeeklyBudget(int daysLeft, int balance) {
        BigDecimal bd = new BigDecimal(balance);

        bd = bd.divide(BigDecimal.valueOf(daysLeft), 0, BigDecimal.ROUND_HALF_UP);
        bd = bd.multiply(BigDecimal.valueOf(7));


        return bd.intValue();
    }
}
