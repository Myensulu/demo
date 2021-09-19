package com.lisa.automation.common.utils;

import static com.lisa.automation.common.constants.PropertyNames.CHANGE_PHONE_NUMBER_URL;

public class Utilities {

    public static String getChangePhoneNumberURL(String phoneNumber){
        return AppProperties.getValueFor(CHANGE_PHONE_NUMBER_URL) + phoneNumber;
    }
}
