package com.lisa.automation.pages;

import com.lisa.automation.common.utils.AppProperties;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import static com.lisa.automation.common.constants.PropertyNames.FAKE_EMAIL_URL;

public class FakeEmailPage extends BasePage {

    //  *************** Fake email url locators **********************************
    private final By DELETE_BTN = By.xpath("//div[@class='links']//a[@href='/delete']");
    private final By COPY_EMAIL_BTN = By.cssSelector("body > div:nth-child(2) > div.row.info > div.col-xs-0.col-md-2.hidden-mobile > div > a");

    private Logger logger = LoggerFactory.getLogger(FakeEmailPage.class);
    private WebDriver driver;

    public FakeEmailPage(WebDriver webDriver, int... time) {
        super(webDriver, time);
        this.driver = webDriver;
    }

    @Step("Get Fake Email")
    public String getFakeEmail() throws IOException, UnsupportedFlavorException {
        String mainWindow = getWindowName();
        String fakeEmail = null;
        try {
            executeScript("window.open('');");
            Set<String> allWindowHandles = driver.getWindowHandles();
            Iterator<String> iterator = allWindowHandles.iterator();
            while (iterator.hasNext()) {
                String childWindow = iterator.next();
                if (!mainWindow.equalsIgnoreCase(childWindow)) {
                    switchToWindow(childWindow);
                    driver.get(AppProperties.getValueFor(FAKE_EMAIL_URL));
                    waitForElementToAppear(DELETE_BTN, 60);
                    clickOnElement(DELETE_BTN);
                    waitInSeconds(2);
                    driver.navigate().refresh();
                    clickOnElement(COPY_EMAIL_BTN);
                    fakeEmail = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null).getTransferData(DataFlavor.stringFlavor);
                    driver.close();
                }
            }
        }finally {
            switchToWindow(mainWindow);
        }
        return fakeEmail;
    }
}

