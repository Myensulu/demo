package com.lisa.automation.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class RequestTimeOffPage extends BasePage {

    private final By NEW_REQ_BTN = By.cssSelector("");
    private final By FROM_DATE_ELE = By.id("start-date");
    private final By TO_DATE_ELE = By.id("end-date");
    private final By VACATION_TYPE_ELE = By.id("types");
    private final By COMMENT_AREA_ELE = By.cssSelector("");
    private final By SUBMIT_REQ_ELE = By.cssSelector("");
    private final By REQ_DATA_CONTAINER_ELE = By.cssSelector("div[class*='MuiContainer-root'] div[class*='MuiTableContainer-root'] > div[class*='MuiBox-root']");

    private Logger logger = LoggerFactory.getLogger(RequestTimeOffPage.class);

    public RequestTimeOffPage(WebDriver webDriver, int... time) {
        super(webDriver, time);
    }

    @Step("Click On New Request Button")
    public void clickOnNewRequest(){
        logger.info("Clicking on New Request Button");
        clickOnElement(NEW_REQ_BTN);
    }

    @Step("Select From Date")
    public void enterFromDate(String fromDate){
        logger.info("Enter From Date: " + fromDate);
        clearAndEnterText(FROM_DATE_ELE, fromDate);
    }

    @Step("Select To Date")
    public void enterToDate(String toDate){
        logger.info("Enter To Date: " + toDate);
        clearAndEnterText(TO_DATE_ELE, toDate);
    }

    @Step("Select Vacation Type")
    public void selectVacationType(String vacationType) throws Exception {
        logger.info("Select Vacation type");
        clickOnElement(VACATION_TYPE_ELE);
        waitInSeconds(2);
        List<WebElement> elementList = getWebElements(By.cssSelector("ul li"));
        for (WebElement next : elementList) {
            String data = next.getAttribute("data-value").trim();
            if (data.equalsIgnoreCase(vacationType)) {
                clickOnElement(next);
                break;
            }
        }
        throw new Exception("Unable to select vacation from drop down list");
    }

    @Step("Enter Comments")
    public void enterComments(String comment){
        logger.info("Enter Comments");
        clearAndEnterText(COMMENT_AREA_ELE, comment);
    }

    @Step("Click Submit Request")
    public void clickSubmitRequest(){
        logger.info("Click Submit Request");
        moveAndClick(SUBMIT_REQ_ELE);
        waitInSeconds(60);
    }

    public Map<Integer, List<String>> getRequestData(){
        Map<Integer, List<String>> containerData = new HashMap<>();
        List<WebElement> containerElements = getWebElements(REQ_DATA_CONTAINER_ELE);
        int count = 0;
        for(WebElement containerElement: containerElements){
            List<String> dataList = new ArrayList<>();
            List<WebElement> dataElements = containerElement.findElements(By.cssSelector("div[class*='MuiGrid-grid-xs'] p"));
            for(WebElement dataElement: dataElements){
                dataList.add(dataElement.getText());
            }
            containerData.put(count, dataList);
        }
        return containerData;
    }



}
