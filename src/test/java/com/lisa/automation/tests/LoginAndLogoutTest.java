package com.lisa.automation.tests;

import com.lisa.automation.pages.HomePage;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.io.IOException;

public class LoginAndLogoutTest extends Base{

    private HomePage homePage;

    @BeforeClass
    public void setUpAndLogin() throws IOException, InterruptedException {
        loginAndVerifySMSCode();
        homePage = new HomePage(driver);
    }

    @Test
    public void testLoginAndVerifyWithSMSCode() throws InterruptedException {
        System.out.println("Logging in and logging out");
        homePage.getTitle();
        loginPage.logOut();
    }

    @AfterClass
    public void closeBrowser(){
        driver.quit();
    }
}
