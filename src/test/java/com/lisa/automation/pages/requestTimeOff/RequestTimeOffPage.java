package com.lisa.automation.pages.requestTimeOff;

import com.lisa.automation.common.utils.Utilities;
import com.lisa.automation.pages.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestTimeOffPage extends BasePage {

    private final By NEW_REQ_BTN = By.cssSelector("");
    private final By REQ_DATA_CONTAINER_ELE = By.cssSelector("div[class*='MuiContainer-root'] div[class*='MuiTableContainer-root'] > div[class*='MuiBox-root']");

    //date fields
    private final String CAL_DAY_ELE = "//button[contains(@class, '-day')]//p[text()='DAY_NUM']";
    private final By CAL_MONTH_NAME_ELE = By.cssSelector("div[class*='CalendarHeader-switchHeader'] p");
    private final By CAL_RIGHT_ARROW_ELE = By.cssSelector("div[class*='CalendarHeader-switchHeader'] button:nth-child(3)");

    private Logger logger = LoggerFactory.getLogger(RequestTimeOffPage.class);

    public RequestTimeOffPage(WebDriver webDriver, int... time) {
        super(webDriver, time);
    }

    @Step("Click On New Request Button")
    public void clickOnNewRequest() {
        logger.info("Clicking on New Request Button");
        clickOnElement(NEW_REQ_BTN);
    }

    @Step("Get Requested Data")
    public Map<Integer, List<String>> getRequestData() {
        Map<Integer, List<String>> containerData = new HashMap<>();
        List<WebElement> containerElements = getWebElements(REQ_DATA_CONTAINER_ELE);
        int count = 0;
        for (WebElement containerElement : containerElements) {
            List<String> dataList = new ArrayList<>();
            List<WebElement> dataElements = containerElement.findElements(By.cssSelector("div[class*='MuiGrid-grid-xs'] p"));
            for (WebElement dataElement : dataElements) {
                dataList.add(dataElement.getText());
            }
            containerData.put(count, dataList);
        }
        return containerData;
    }

    protected void selectDayFromCalendar(String date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate localDate = LocalDate.parse(date, dateTimeFormatter);
        int day = localDate.getDayOfMonth();
        int month = localDate.getMonthValue();
        int year = localDate.getYear();
        waitInSeconds(1);
        String []calendarMonthYear = getText(CAL_MONTH_NAME_ELE).trim().split(" ");
        String actualCalMonthName = calendarMonthYear[0];
        String actualCalYearName = calendarMonthYear[1];
        logger.info("Actual Calendar month name is: " + actualCalMonthName);
        String expectedCalMonthName = Utilities.getMonthName(month);
        if ((actualCalMonthName.equalsIgnoreCase(expectedCalMonthName)) && (actualCalYearName.equals(String.valueOf(year)))) {
            String dayNum = CAL_DAY_ELE.replace("DAY_NUM", String.valueOf(day));
            clickOnElement(By.xpath(dayNum));
            waitInSeconds(2);
        } else {
            clickOnElement(CAL_RIGHT_ARROW_ELE);
            waitInSeconds(2);
            logger.info("Actual and Expected dates are not matched moving to next month");
            selectDayFromCalendar(date);
        }
    }

}
