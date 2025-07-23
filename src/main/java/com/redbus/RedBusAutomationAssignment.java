package com.redbus;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class RedBusAutomationAssignment {

    public static void main(String args[]) throws InterruptedException {
        ChromeOptions ch = new ChromeOptions();
        ch.addArguments("--start-maximized");
        WebDriver wd = new ChromeDriver(ch);
        WebDriverWait wait = new WebDriverWait(wd, Duration.ofSeconds(30));
        wd.get("https://www.redbus.in");
        wd.manage().deleteAllCookies();

        By fromPlace = By.xpath("(//div[contains(@class, 'srcDestWrapper') and @role='button'])[1]");
        WebElement sourceButton = wd.findElement(fromPlace);
        sourceButton.click();
        selectLocation(wd, wait, "mumbai");
        selectLocation(wd, wait, "pune");

        By searchButtonLocator = By.xpath("//button[contains(@class,'searchButtonWrapper')]");
        WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(searchButtonLocator));
        searchButton.click();

        By primoButtonLocator = By.xpath("//div[contains(text(), 'Primo')]");
        WebElement primoButton = wait.until(ExpectedConditions.elementToBeClickable(primoButtonLocator));
        primoButton.click();
//        With Non AC Filter data loading was only 2 so commented out this filter
//        By nonACLocator = By.xpath("//div[contains(text(), 'NONAC')]");
//        WebElement nonAC = wait.until(ExpectedConditions.elementToBeClickable(nonACLocator));
//        nonAC.click();

        By subtitleLocator = By.xpath("//span[contains(@class, 'subtitle')]");
        WebElement subTitle = null;
        if (wait.until(ExpectedConditions.textToBePresentInElementLocated(subtitleLocator, "buses"))) {
           subTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(subtitleLocator));
        }
        System.out.println(subTitle.getText());

        By tuppleWrapperLocator = By.xpath("//li[contains(@class, 'tupleWrapper')]");
        By busNameLocator = By.xpath(".//div[contains(@class, 'travelsName')]");
        List<WebElement> busList = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(tuppleWrapperLocator));
        JavascriptExecutor js = (JavascriptExecutor)wd;

        while(true)
        {
            busList = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(tuppleWrapperLocator));
            List<WebElement> endOfList = wd.findElements(By.xpath("//span[contains(text(),'End of list')]"));
            if(!endOfList.isEmpty()) {
                break;
            }
            js.executeScript("arguments[0].scrollIntoView({behavior:'smooth'})" , busList.get(busList.size()-3));

        }
        List<WebElement> newBusList = wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(tuppleWrapperLocator, busList.size()));
        for(WebElement bus : newBusList) {
            System.out.println(bus.findElement(busNameLocator).getText());
        }



    }

    public static void selectLocation(WebDriver wd, WebDriverWait wait, String locationData) throws InterruptedException {
        Thread.sleep(4000);
        WebElement searchElement = wd.switchTo().activeElement();
//        searchElement.sendKeys("Mumbai");

        By searchSuggestionSection = By.xpath("//div[contains(@class,'searchSuggestionWrapper')]");
        WebElement searchSuggestionSections = wait.until(ExpectedConditions.visibilityOfElementLocated(searchSuggestionSection));
        WebElement serachTextBoxElement = wd.switchTo().activeElement();
        searchElement.sendKeys(locationData);

        By searchCategoryLocator = By.xpath("//div[contains(@class, 'searchCategory')]");
        List<WebElement> searchList = wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(searchCategoryLocator, 2));

        System.out.println(searchList.size());

        List<WebElement> locationList = searchList.getFirst().findElements(By.xpath(".//div[contains(@class, 'listHeader')]"));

        System.out.println(locationList.size());

        for (WebElement location : locationList) {
            String lname = location.getText().trim();
            if (lname.equalsIgnoreCase(locationData)) {
                location.click();
                break;
            }

        }
    }


}
