package com.lisa.automation.pages.requestTimeOff;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CreateNewRTOPage extends RequestTimeOffPage {

    private final By FROM_DATE_ELE = By.id("start-date");
    private final By TO_DATE_ELE = By.id("end-date");
    private final By VACATION_TYPE_ELE = By.id("types");
    private final By TOGGLE_ELE = By.xpath("//span//input[@type='checkbox']");
    private final By COMMENT_AREA_ELE = By.cssSelector("");
    private final By SUBMIT_REQ_ELE = By.cssSelector("");

    private Logger logger = LoggerFactory.getLogger(CreateNewRTOPage.class);

    public CreateNewRTOPage(WebDriver webDriver, int... time) {
        super(webDriver, time);
    }

    @Step("Select From Date")
    public void enterFromDate(String fromDate){
        logger.info("Enter From Date: " + fromDate);
        clickOnElement(FROM_DATE_ELE);
        selectDayFromCalendar(fromDate);
    }

    @Step("Select To Date")
    public void enterToDate(String toDate){
        logger.info("Enter To Date: " + toDate);
        clickOnElement(TO_DATE_ELE);
        selectDayFromCalendar(toDate);
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
                return;
            }
        }
        throw new Exception("Unable to select vacation from drop down list");
    }

    @Step("Click on Toggle button")
    public void clickOnToggleButton(){
        logger.info("Clicking on toggle button");
        clickOnElement(TOGGLE_ELE);
    }

    @Step("Select Mixed Vacation Types")
    public void selectMixedVacationTypes(List<String> vacationTypes) throws Exception {
        logger.info("Selecting mixed vacation types");
        List<WebElement> trElements = getWebElements(By.tagName("tr"));
        for(int i=0; i<vacationTypes.size(); i++){
            trElements.get(i).findElement(By.id("type")).click();
            waitInSeconds(2);
            List<WebElement> elementList = getWebElements(By.cssSelector("ul li"));
            boolean found = false;
            for (WebElement next : elementList) {
                String data = next.getAttribute("data-value").trim();
                if (data.equalsIgnoreCase(vacationTypes.get(i))) {
                    clickOnElement(next);
                    found = true;
                    break;
                }
            }
            if(!found)
                throw new Exception("Unable to select vacation from drop down list");
        }
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
}
