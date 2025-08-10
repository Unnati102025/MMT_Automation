package Locators;

import org.openqa.selenium.By;

public class MakeMyTripLocators {
    // Pop-ups
    public static By loginPopupCloseButton = By.cssSelector("span[data-cy='closeModal']");
    public static By travelCardPopupCloseButton = By.cssSelector("span[data-cy='travel-card-close']");

    // Trip Type
    public static By oneWayTripType = By.xpath("//li[@data-cy='oneWayTrip']");

    // Source and Destination
    public static By fromCityLabel = By.xpath("//label[@for='fromCity']");
    public static By toCityLabel = By.xpath("//label[@for='toCity']");
    public static By fromCityInput = By.xpath("//input[@placeholder='From']");
    public static By toCityInput = By.xpath("//input[@placeholder='To']");

    // Date Picker
    public static By dayPickerCaption = By.xpath("//div[@class='DayPicker-Caption']/div");
    public static By nextMonthButton = By.xpath("//span[@aria-label='Next Month']");
    // Search
    public static By searchButton = By.xpath("//a[text()='Search']");
    // Search Results
    public static By priceSortButton = By.xpath("//span[text()='Price']");
    public static By flightList = By.cssSelector("div.fli-list");
    public static By cheapestFlightBookButton = By.xpath("(//button[contains(text(),'Book Now')])[1]");
    // Review Page Locators
    public static By reviewFromCity = By.xpath("//div[@class='review-header']//span[contains(@class,'from-city')]");
    public static By reviewToCity = By.xpath("//div[@class='review-header']//span[contains(@class,'to-city')]");
    public static By reviewDate = By.xpath("//div[@class='review-header']//span[contains(@class,'travel-date')]");

    // Dynamic locator for selecting the date
    public static By getDateElement(String day, String monthYear) {
        return By.xpath("//div[@class='DayPicker-Month'][.//div[@class='DayPicker-Caption']/div[text()='" + monthYear + "']]"
                + "//div[contains(@class,'DayPicker-Day') and not(contains(@class,'disabled'))]"
                + "//p[text()='" + day + "']/parent::div");
    }
}
