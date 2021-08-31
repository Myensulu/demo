package com.demo.tests;

import com.demo.pages.FakeEmailPage;
import com.demo.pages.HomePage;
import com.demo.pages.LoginPage;
import com.demo.pages.RegistrationPage;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;

public class DemoTest extends Base{
    private HomePage homePage;
    private FakeEmailPage fakeEmailPage;
    private RegistrationPage registrationPage;
    private LoginPage loginPage;

    @BeforeClass
    public void setUpAndLogin() throws IOException, InterruptedException {
        setupDriver();
        homePage = new HomePage(driver);
        fakeEmailPage = new FakeEmailPage(driver);
        registrationPage = new RegistrationPage(driver);
    }

    @Test
    public void testLoginAndVerifyWithSMSCode() throws Exception {
        String lastName = "";
        String birthday = "";
        String socialSecurity = "";
        String password = "";
        String confPassword = "";
        String phoneNumber = "";

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
