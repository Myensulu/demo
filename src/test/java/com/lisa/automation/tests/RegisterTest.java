package com.lisa.automation.tests;

import com.lisa.automation.pages.FakeEmailPage;
import com.lisa.automation.pages.DashboardPage;
import com.lisa.automation.pages.LoginPage;
import com.lisa.automation.pages.RegistrationPage;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class RegisterTest extends Base{
    private DashboardPage dashboardPage;
    private FakeEmailPage fakeEmailPage;
    private RegistrationPage registrationPage;
    private LoginPage loginPage;

    @BeforeClass
    public void setUpAndLogin() {
        setupDriver();
        dashboardPage = new DashboardPage(driver);
        fakeEmailPage = new FakeEmailPage(driver);
        registrationPage = new RegistrationPage(driver);
    }

    @Test
    public void registerLoginAndVerify() throws Exception {
        String lastName = "Fouchard";
        String birthday = "09101954";
        String socialSecurity = "000000";
        String password = "Testers@1";
        String confPassword = "Testers@1";
        String phoneNumber = "7077190993";

        //get Fake Email
        String emailId = fakeEmailPage.getFakeEmail();

        //Open Registration Form and Enter details and click on Sign In page
        registrationPage.openRegistrationPage();
        String registrationForm = registrationPage.fillAndConfirmPortalRegistrationForm(
                lastName, birthday, socialSecurity, emailId, password, confPassword, phoneNumber);
        Assert.assertEquals(registrationForm, "");

        //Login or Sign in the page
        loginPage = new LoginPage(emailId, password, "", driver);
        loginPage.loginStep();

        //Enter SMS Code generated for mobile number
        String smsCode = loginPage.getAuthenticationCode();
        loginPage.enterConfirmationCode(smsCode);

        //Enter Email Code generated for email Address
        String emailConfirmationCode = loginPage.getEmailConfirmationCode();
        loginPage.enterConfirmationCode(emailConfirmationCode);

        //Close the link
        loginPage.clickClose();
    }

    @AfterClass
    public void closeBrowser(){
        driver.quit();
    }
}

