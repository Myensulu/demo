package com.demo.tests;

import com.demo.config.DriverConfig;
import com.demo.pages.LoginPage;
import io.qameta.allure.Step;

import java.io.IOException;

abstract public class Base extends DriverConfig {


    @Step("Login to application with General Credentials")
    public void loginAndVerifySMSCode() throws IOException, InterruptedException {
        setup();
        loginPage = new LoginPage(getUser(),getPwd(), getUrl(), getDriver());
        loginPage.loginStep();
        String smsCode = loginPage.getSmsCode();
        loginPage.enterConfirmationCode(smsCode);
    }

    @Step("Setup Driver")
    public void setupDriver() throws IOException, InterruptedException {
        setup();
    }




}
