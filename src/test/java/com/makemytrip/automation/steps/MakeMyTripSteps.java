package com.makemytrip.automation.steps;

import Locators.MakeMyTripLocators;
import com.gemini.gemjar.enums.Status;
import com.gemini.gemjar.exception.GemException;
import com.gemini.gemjar.reporting.GemTestReporter;
import com.gemini.gemjar.utils.ui.DriverAction;
import com.gemini.gemjar.utils.ui.DriverManager;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;

public class MakeMyTripSteps {

    @Given("^User navigates to the MakeMyTrip website$")
    public void navigateToWebsite() {
        try {
            DriverAction.maximizeBrowser();
            closePopups();
            GemTestReporter.addTestStep("Navigation", "Successfully navigated to MakeMyTrip homepage", Status.PASS, DriverAction.takeSnapShot());
        } catch (Exception e) {
            GemTestReporter.addTestStep("Navigation", "Failed to navigate to MakeMyTrip: " + e.getMessage(), Status.FAIL, DriverAction.takeSnapShot());
        }
    }

    private void closePopups() {
        try {
            if (DriverAction.isExist(MakeMyTripLocators.loginPopupCloseButton)) {
                DriverAction.click(MakeMyTripLocators.loginPopupCloseButton);
                GemTestReporter.addTestStep("Handle Popup", "Closed login popup.", Status.INFO);
            }
            if (DriverAction.isExist(MakeMyTripLocators.travelCardPopupCloseButton)) {
                DriverAction.click(MakeMyTripLocators.travelCardPopupCloseButton);
                GemTestReporter.addTestStep("Handle Popup", "Closed travel card popup.", Status.INFO);
            }
        } catch (Exception e) {
            GemTestReporter.addTestStep("Handle Popups", "No popups appeared or an issue occurred: " + e.getMessage(), Status.INFO, DriverAction.takeSnapShot());
        }
    }

    @When("^User selects a \"([^\"]*)\" trip$")
    public void selectTripType(String type) {
        try {
            if (type.equalsIgnoreCase("One Way")) {
                DriverAction.click(MakeMyTripLocators.oneWayTripType);
                GemTestReporter.addTestStep("Select Trip Type", "Selected one-way trip successfully.", Status.PASS, DriverAction.takeSnapShot());
            } else {
                GemTestReporter.addTestStep("Select Trip Type", "Trip type '" + type + "' not supported in this test.", Status.FAIL);
            }
        } catch (Exception e) {
            GemTestReporter.addTestStep("Select Trip Type", "Failed to select trip type: " + e.getMessage(), Status.FAIL, DriverAction.takeSnapShot());
        }
    }

    @And("^User enters source city as \"([^\"]*)\"$")
    public void enterSource(String src) {
        try {
            DriverAction.click(MakeMyTripLocators.fromCityLabel);
            DriverAction.typeText(MakeMyTripLocators.fromCityInput, src);
            By sourceOption = By.xpath("//li[@role='option']//span[text()='" + src + "']");
            DriverAction.waitUntilElementAppear(sourceOption, 10);
            DriverAction.click(sourceOption);
            GemTestReporter.addTestStep("Enter Source City", "Entered source city: " + src, Status.PASS, DriverAction.takeSnapShot());
        } catch (Exception e) {
            GemTestReporter.addTestStep("Enter Source City", "Failed to enter source city: " + e.getMessage(), Status.FAIL, DriverAction.takeSnapShot());
        }
    }

    @And("^User enters destination city as \"([^\"]*)\"$")
    public void enterDestination(String dest) {
        try {
            DriverAction.click(MakeMyTripLocators.toCityLabel);
            DriverAction.typeText(MakeMyTripLocators.toCityInput, dest);
            By destinationOption = By.xpath("//li[@role='option']//span[text()='" + dest + "']");
            DriverAction.waitUntilElementAppear(destinationOption, 10);
            DriverAction.click(destinationOption);
            GemTestReporter.addTestStep("Enter Destination City", "Entered destination city: " + dest, Status.PASS, DriverAction.takeSnapShot());
        } catch (Exception e) {
            GemTestReporter.addTestStep("Enter Destination City", "Failed to enter destination city: " + e.getMessage(), Status.FAIL, DriverAction.takeSnapShot());
        }
    }

    @And("^User selects departure date as day \"([^\"]*)\", month \"([^\"]*)\" and year \"([^\"]*)\"$")
    public void selectDepartureDate(String day, String month, String year) {
        try {
            closePopups();
            String targetMonthYear = month + " " + year;

            // Loop until the desired month is visible
            while (true) {
                DriverAction.waitUntilElementAppear(MakeMyTripLocators.dayPickerCaption, 10);
                String currentMonth = DriverAction.getElementText(MakeMyTripLocators.dayPickerCaption).trim();
                if (currentMonth.equalsIgnoreCase(targetMonthYear)) {
                    break;
                }
                DriverAction.click(MakeMyTripLocators.nextMonthButton);
                DriverAction.waitUntilElementAppear(MakeMyTripLocators.dayPickerCaption, 5);
            }
            By dateElement = MakeMyTripLocators.getDateElement(day, targetMonthYear);
            DriverAction.pageScroll(0, 250);
            DriverAction.waitUntilElementClickable(dateElement, 5);
            DriverAction.click(dateElement);
            GemTestReporter.addTestStep("Select Departure Date", "Selected departure date: " + day + " " + month + ", " + year, Status.PASS, DriverAction.takeSnapShot());
        } catch (Exception e) {
            GemTestReporter.addTestStep("Select Departure Date", "Failed to select departure date: " + e.getMessage(), Status.FAIL, DriverAction.takeSnapShot());
        }
    }

    @And("^User clicks the search button$")
    public void clickSearch() throws GemException {
        try {
            closePopups();
            DriverAction.click(MakeMyTripLocators.searchButton);
            GemTestReporter.addTestStep("Click Search", "Clicked on the search button.", Status.PASS, DriverAction.takeSnapShot());
            DriverAction.waitSec(10);

            // Check if the page is blank or blocked
            String pageSource = DriverManager.getWebDriver().getPageSource();
            if (pageSource.trim().isEmpty() || !pageSource.contains("Flights")) {
                GemTestReporter.addTestStep(
                        "Bot Detection",
                        "Blank page or blocked by MMT detected. Marking test as FAILED due to bot detection.",
                        Status.FAIL,
                        DriverAction.takeSnapShot()
                );
                // Throw an exception to fail the test case, but don't close the driver here.
                throw new GemException("Test failed: Bot detection triggered.");
            }
        } catch (Exception e) {
            GemTestReporter.addTestStep("Click Search", "Failed to click search button: " + e.getMessage(), Status.FAIL, DriverAction.takeSnapShot());
            throw e; // Re-throw the exception to fail the test case
        }
    }

    @And("^User sorts the flights by \"([^\"]*)\" from low to high$")
    public void sortByPriceLowToHigh(String sortBy) {
        try {
            if (sortBy.equalsIgnoreCase("Price")) {
                DriverAction.waitUntilElementAppear(MakeMyTripLocators.priceSortButton, 20);
                DriverAction.click(MakeMyTripLocators.priceSortButton);
                GemTestReporter.addTestStep("Sort Flights", "Sorted flights by price (low to high).", Status.PASS, DriverAction.takeSnapShot());
            }
        } catch (Exception e) {
            GemTestReporter.addTestStep("Sort Flights", "Failed to sort flights: " + e.getMessage(), Status.FAIL, DriverAction.takeSnapShot());
        }
    }

    @And("^User selects the cheapest flight$")
    public void selectCheapestFlight() {
        try {
            DriverAction.waitUntilElementAppear(MakeMyTripLocators.flightList, 20);
            // The first 'Book Now' button after sorting by price should be the cheapest.
            DriverAction.click(MakeMyTripLocators.cheapestFlightBookButton);
            GemTestReporter.addTestStep("Select Cheapest Flight", "Selected the cheapest flight from the list.", Status.PASS, DriverAction.takeSnapShot());
        } catch (Exception e) {
            GemTestReporter.addTestStep("Select Cheapest Flight", "Failed to select the cheapest flight: " + e.getMessage(), Status.FAIL, DriverAction.takeSnapShot());
        }
    }

    @Then("^User verifies the booking details for from city \"([^\"]*)\", to city \"([^\"]*)\", and date \"([^\"]*)\" are correct on the review page$")
    public void verifyReviewPage(String expectedFromCity, String expectedToCity, String expectedDate) {
        try {
            String currentUrl = DriverAction.getCurrentURL();

            //Verify URL
            boolean urlCheck = currentUrl.contains("review");

            //Verify booking details on the review page
            String actualFromCity = DriverAction.getElementText(MakeMyTripLocators.reviewFromCity).trim();
            String actualToCity = DriverAction.getElementText(MakeMyTripLocators.reviewToCity).trim();
            String actualDate = DriverAction.getElementText(MakeMyTripLocators.reviewDate).trim();

            boolean detailsMatch = actualFromCity.contains(expectedFromCity) &&
                    actualToCity.contains(expectedToCity) &&
                    actualDate.contains(expectedDate);

            // Combined result
            if (urlCheck && detailsMatch) {
                GemTestReporter.addTestStep(
                        "Verify Review Page",
                        "Successfully landed on review page and booking details match. " +
                                "URL: " + currentUrl +
                                " | From: " + actualFromCity +
                                " | To: " + actualToCity +
                                " | Date: " + actualDate,
                        Status.PASS,
                        DriverAction.takeSnapShot()
                );
            } else {
                GemTestReporter.addTestStep(
                        "Verify Review Page",
                        "Verification failed. " +
                                "URL Check: " + urlCheck +
                                " | Details Match: " + detailsMatch +
                                " | Expected - From: " + expectedFromCity +
                                ", To: " + expectedToCity +
                                ", Date: " + expectedDate +
                                " | Actual - From: " + actualFromCity +
                                ", To: " + actualToCity +
                                ", Date: " + actualDate,
                        Status.FAIL,
                        DriverAction.takeSnapShot()
                );
            }

        } catch (Exception e) {
            GemTestReporter.addTestStep(
                    "Verify Review Page",
                    "Verification failed due to exception: " + e.getMessage(),
                    Status.FAIL,
                    DriverAction.takeSnapShot()
            );
        }
    }
}