package com.lisa.automation.common.utils;

import java.text.DateFormatSymbols;

import static com.lisa.automation.common.constants.PropertyNames.*;

public class Utilities {

    public static String getChangePhoneNumberURL(String phoneNumber){
        return AppProperties.getValueFor(CHANGE_PHONE_NUMBER_URL) + phoneNumber;
    }

    public static String getPhoneNumberURL(String phoneNumber){
        return AppProperties.getValueFor(PHONE_VERIFICATION_URL) + phoneNumber;
    }

    public static String getMonthName(int month) {
        return new DateFormatSymbols().getMonths()[month-1];
    }
}
