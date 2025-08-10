package com.makemytrip.automation.steps;

import com.gemini.gemjar.exception.GemException;
import com.gemini.gemjar.utils.ui.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;

public class Hook {

    @Before
    public void start() throws GemException {
        DriverManager.setUpBrowser();
    }

    @After
    public void tearDown() {
        try {
            DriverManager.closeDriver();
        } catch (Exception e) {
            if (e instanceof org.openqa.selenium.NoSuchSessionException) {
                System.out.println("Driver session already closed.");
            } else {
                System.out.println("Error during teardown: " + e.getMessage());
            }
        }
    }
}