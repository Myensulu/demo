package com.lisa.automation.pages;

import com.lisa.automation.common.utils.AppProperties;
import com.lisa.automation.common.utils.DbManager;
import com.lisa.automation.common.utils.Utilities;
import com.lisa.automation.common.utils.Wrappers;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.Set;
import static com.lisa.automation.common.constants.PropertyNames.FAKE_EMAIL_URL;
import static com.lisa.automation.common.constants.PropertyNames.PHONE_VERIFICATION_URL2;

public class BasePage extends Wrappers {

    private final By SMS_CODE_VERIFICATION_ELE = By.xpath("//td[contains(text(), 'Your verification code is')]");
    private final By SMS_CODE_ELE = By.xpath("//td[contains(text(), 'authentication code')]");
    private final By LATEST_EMAIL_ELE = By.xpath("//*[@id='schranka']/tr[1]");
    private final By EMAIL_VERIFICATION_ELE = By.xpath("//*[contains(text(), 'Your verification code is')]");
    private final By DASHBOARD_ELE = By.cssSelector("#root > div > div:nth-child(1) > header > div > div > div.MuiGrid-root.MuiGrid-item.MuiGrid-grid-xs-true > div > img");

    private Logger logger = LoggerFactory.getLogger(BasePage.class);

    protected WebDriver driver;
    public BasePage(WebDriver webDriver, int... time) {
        super(webDriver, time);
        this.driver = webDriver;
    }

    private String getSmsCode(String codeType, String ...args){
        By elementFor;
        switch (codeType){
            case "VERIFICATION":
                elementFor = SMS_CODE_VERIFICATION_ELE;
                break;
            case "AUTHENTICATION":
                elementFor = SMS_CODE_ELE;
                break;
            default:
                return null;
        }
        String mainWindow = getWindowName();
        executeScript("window.open('');");
        Set<String> allWindowHandles = driver.getWindowHandles();
        Iterator<String> iterator = allWindowHandles.iterator();
        String code = null;
        while (iterator.hasNext()) {
            String childWindow = iterator.next();
            if (!mainWindow.equalsIgnoreCase(childWindow)) {
                switchToWindow(childWindow);
                String phoneURL = PHONE_VERIFICATION_URL2;
                if(args.length > 0)
                    phoneURL = args[0];
                driver.get(AppProperties.getValueFor(phoneURL));
                waitInSeconds(30);
                driver.navigate().refresh();
                code = getWebElements(elementFor).get(0).getText();
                driver.close();
                switchToWindow(mainWindow);
            }
        }
        if(code != null) {
            String[] split = code.split("is");
            String actualCode = split[1];
            actualCode = actualCode.replaceAll("[-+.^:,]", "");
            return actualCode.trim();
        }
        return code;
    }

    @Step("Get Verification Code")
    public String getVerificationCode(){ return getSmsCode("VERIFICATION"); }

    @Step("Get Authentication Code")
    public String getAuthenticationCode(){
        return getSmsCode("AUTHENTICATION");
    }

    @Step("Change Phone Number Code")
    public String getChangePhoneNumberCode(String phoneNumber){
        return getSmsCode("VERIFICATION", Utilities.getChangePhoneNumberURL(phoneNumber));
    }

    @Step("Email Confirmation Code")
    public String getEmailConfirmationCode() {
        String mainWindow = getWindowName();
        executeScript("window.open('');");
        Set<String> allWindowHandles = driver.getWindowHandles();
        Iterator<String> iterator = allWindowHandles.iterator();
        String code = null;
        while (iterator.hasNext()) {
            String childWindow = iterator.next();
            if (!mainWindow.equalsIgnoreCase(childWindow)) {
                switchToWindow(childWindow);
                driver.get(AppProperties.getValueFor(FAKE_EMAIL_URL));
                waitInSeconds(10);
                clickOnElement(LATEST_EMAIL_ELE);
                waitInSeconds(10);
                driver.switchTo().frame(getWebElement(By.id("iframeMail")));
                code = getWebElements(EMAIL_VERIFICATION_ELE).get(0).getText();
                driver.switchTo().defaultContent();
                driver.close();
                switchToWindow(mainWindow);
            }
        }
        if(code != null) {
            String[] split = code.split("is");
            String actualCode = split[1];
            actualCode = actualCode.replaceAll("[-+.^:,]", "");
            return actualCode.trim();
        }
        return code;
    }

    public String getCustomRegHash() throws SQLException, ClassNotFoundException {
        String query = "select * from public.fn_setup_custom_reg_hash('9189108','000000','19540910');";
        return new DbManager().runQuery(query).getString(0);
    }

    @Step("Navigate to Home/Root/Dashboard Page")
    public void navigateToDashboard(){
        logger.info("Navigate to Dashboard Page");
        clickOnElement(DASHBOARD_ELE);
    }
}

