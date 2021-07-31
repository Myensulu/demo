package com.demo.tests;

import com.demo.pages.HomePage;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;

public class DemoTest extends Base{
    private HomePage homePage;

    @BeforeClass
    public void setUpAndLogin() throws IOException, InterruptedException {
        loginAndVerifySMSCode();
        homePage = new HomePage(driver);


    }

    @Test
    public void testLoginAndVerifyWithSMSCode(){
        System.out.println("Your test case starts from here");
        //homePage.getTitle();
    }

    @AfterClass
    public void closeBrowser(){
        driver.quit();
    }
}
