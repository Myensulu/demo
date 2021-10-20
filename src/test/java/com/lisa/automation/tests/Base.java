package com.lisa.automation.tests;

import com.lisa.automation.common.utils.AppProperties;
import com.lisa.automation.config.DriverConfig;
import com.lisa.automation.pages.LoginPage;
import io.qameta.allure.Step;
import java.io.IOException;

import static com.lisa.automation.common.constants.PropertyNames.COMM_URL;

abstract public class Base extends DriverConfig {

    @Step("Login to application with General Credentials")
    public void loginAndVerifySMSCode() throws InterruptedException {
        setup();
        loginPage = new LoginPage(getUser(),getPwd(), getUrl(), getDriver());
        loginPage.loginStep();
        String smsCode = loginPage.getAuthenticationCode();
        loginPage.enterConfirmationCode(smsCode);
    }

    @Step("Setup Driver")
    public void setupDriver() {
        setup();
    }

    @Step("Open Common URL")
    public void openCommonURL(){
        driver.get(AppProperties.getValueFor(COMM_URL));
    }
}
