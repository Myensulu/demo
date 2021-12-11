package com.lisa.automation.listeners;

import com.lisa.automation.tests.Base;
import io.qameta.allure.Allure;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

public class Listener implements ITestListener {
    private Logger logger = LoggerFactory.getLogger(Listener.class);

    private void takeAndAddScreenshotToReport(ITestResult result){
        Object currentClass = result.getInstance();
        WebDriver driver = ((Base) currentClass).getDriver();
        if(driver != null) {
            Allure.addAttachment("Screenshot", new ByteArrayInputStream(((TakesScreenshot)driver).getScreenshotAs(OutputType.BYTES)));
            TakesScreenshot ts = (TakesScreenshot) driver;
            File srcFile = ts.getScreenshotAs(OutputType.FILE);
            try {
                FileUtils.copyFile(srcFile, new File("target/ScreenShots/" + result.getName() + ".jpg"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onTestSuccess(ITestResult result) {
        logger.error("Test Passed at " +result.getName());
        takeAndAddScreenshotToReport(result);
    }
    /*
      Attached the screenshot with the Allure Report Failure
     */
    public void onTestFailure(ITestResult result) {
        logger.error("Test Failed at " +result.getName());
        takeAndAddScreenshotToReport(result);
    }
}
