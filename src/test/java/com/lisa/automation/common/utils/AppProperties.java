package com.lisa.automation.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static com.lisa.automation.common.constants.FilePaths.CONFIGURATION_FILE;

public class AppProperties {
    private static Properties properties;

    static {
        properties = new Properties();
        try {
            properties.load(new FileInputStream(new File(CONFIGURATION_FILE)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getValueFor(String propName) {
        String propVal = properties.getProperty(propName);
        if(propVal == null) throw new NullPointerException(String.format("'%s' does not exist.", propName));
        return propVal;
    }
}
