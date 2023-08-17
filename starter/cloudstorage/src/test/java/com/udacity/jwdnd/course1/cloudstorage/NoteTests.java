package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.mockito.MockitoAnnotations;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NoteTests {


    @LocalServerPort
    private int port;

    private WebDriver driver;

    private WebDriverWait wait;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }



    @Test
    public void testNoteCreationAndDisplay() throws InterruptedException {
        //Creating a test account
        driver.get("http://localhost:" + port + "/signup");

        //Filling in signup form
        driver.findElement(By.id("inputFirstName")).sendKeys("Koen");
        driver.findElement(By.id("inputLastName")).sendKeys("Hein");
        driver.findElement(By.id("inputUsername")).sendKeys("koen");
        driver.findElement(By.id("inputPassword")).sendKeys("password10");
        driver.findElement(By.id("buttonSignUp")).click();
        doLogIn("koen", "password10");

        WebElement NoteButton = driver.findElement(By.id("nav-notes-tab"));
        NoteButton.click();

        //Waiting for the "Notes" button to become clickable
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement addNoteButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("new-Note")));

        //Clicking the button
        addNoteButton.click();

        //Waiting for the modal to become visible
        WebElement noteModal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteModal")));

        //Locating and interacting with the form fields
        WebElement noteTitleInput = noteModal.findElement(By.id("notes-title"));
        WebElement noteDescriptionInput = noteModal.findElement(By.id("notes-description"));
        WebElement submitButton = noteModal.findElement(By.id("SubmitNote"));

        //Filling in the form fields
        noteTitleInput.sendKeys("Hello");
        noteDescriptionInput.sendKeys("Hello dear");

        //Clicking the submit button
        submitButton.click();

        //Clicking the success button
        WebElement successButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("continue")));
        successButton.click();

        //Clicking note button
        WebElement reClickNoteButton = driver.findElement(By.id("nav-notes-tab"));
        reClickNoteButton.click();

        //Verifying that the new note is displayed in the table
        WebElement titleCell = driver.findElement(By.id("titleNote"));
        WebElement descriptionCell = driver.findElement(By.id("descriptionNote"));

        Thread.sleep(5000);

        assertEquals("Hello", titleCell.getText());
        assertEquals("Hello dear", descriptionCell.getText());

    }

    @Test
    public void testNoteCreationEditAndDisplay() throws InterruptedException {

        //Creating a test account
        driver.get("http://localhost:" + port + "/signup");

        //Filling in signup form
        driver.findElement(By.id("inputFirstName")).sendKeys("Hans");
        driver.findElement(By.id("inputLastName")).sendKeys("Smith");
        driver.findElement(By.id("inputUsername")).sendKeys("hans");
        driver.findElement(By.id("inputPassword")).sendKeys("password20");
        driver.findElement(By.id("buttonSignUp")).click();
        doLogIn("hans", "password20");

        WebElement NoteButton = driver.findElement(By.id("nav-notes-tab"));
        NoteButton.click();

        //Waiting for the "Notes" button to become clickable
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement addNoteButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("new-Note")));

        //Clicking the button
        addNoteButton.click();

        //Waiting for the modal to become visible
        WebElement noteModal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteModal")));

        //Locating and interacting with the form fields
        WebElement noteTitleInput = noteModal.findElement(By.id("notes-title"));
        WebElement noteDescriptionInput = noteModal.findElement(By.id("notes-description"));
        WebElement submitButton = noteModal.findElement(By.id("SubmitNote"));

        //Filling in the form fields
        noteTitleInput.sendKeys("My first note");
        noteDescriptionInput.sendKeys("A lot to say");

        //Clicking the submit button
        submitButton.click();

        //Clicking the success button
        WebElement successButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("continue")));
        successButton.click();

        //Clicking note button
        WebElement reClickNoteButton = driver.findElement(By.id("nav-notes-tab"));
        reClickNoteButton.click();

        //Verifying that the new note is displayed in the table
        WebElement titleCell = driver.findElement(By.id("titleNote"));
        WebElement descriptionCell = driver.findElement(By.id("descriptionNote"));

        Thread.sleep(5000);

        assertEquals("My first note", titleCell.getText());
        assertEquals("A lot to say", descriptionCell.getText());

        //Waiting for the edit button to become visible
        wait = new WebDriverWait(driver, 10);
        WebElement noteEditButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("edit-notes")));
        noteEditButton.click();
        Thread.sleep(5000);

        //Waiting for the modal to become visible
        noteModal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteModal")));

        //Locating and interacting with the form fields
        noteTitleInput = noteModal.findElement(By.id("notes-title"));
        noteDescriptionInput = noteModal.findElement(By.id("notes-description"));
        submitButton = noteModal.findElement(By.id("SubmitNote"));

        //Filling in the form fields
        noteTitleInput.sendKeys(" indeed");
        noteDescriptionInput.sendKeys(" indeed");

        //Clicking the submit button
        submitButton.click();

        //Clicking the success button
        successButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("continue")));
        successButton.click();

        //Clicking note button
        reClickNoteButton = driver.findElement(By.id("nav-notes-tab"));
        reClickNoteButton.click();

        //Verifying that the new note is displayed in the table
        WebElement newTitleCell = driver.findElement(By.id("titleNote"));
        WebElement newDescriptionCell = driver.findElement(By.id("descriptionNote"));

        Thread.sleep(5000);

        //Verifying that edited values are displayed
        assertEquals("My first note indeed", newTitleCell.getText());
        assertEquals("A lot to say indeed", newDescriptionCell.getText());

    }

    @Test
    public void testNoteCreationAndDelete() throws InterruptedException {

        //Creating a test account
        driver.get("http://localhost:" + port + "/signup");

        //Filling in signup form
        driver.findElement(By.id("inputFirstName")).sendKeys("Finn");
        driver.findElement(By.id("inputLastName")).sendKeys("Mann");
        driver.findElement(By.id("inputUsername")).sendKeys("finn");
        driver.findElement(By.id("inputPassword")).sendKeys("password40");
        driver.findElement(By.id("buttonSignUp")).click();
        doLogIn("finn", "password40");

        WebElement NoteButton = driver.findElement(By.id("nav-notes-tab"));
        NoteButton.click();

        //Waiting for the "Notes" button to become clickable
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement addNoteButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("new-Note")));

        //Clicking the button
        addNoteButton.click();

        //Waiting for the modal to become visible
        WebElement noteModal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteModal")));

        //Locating and interacting with the form fields
        WebElement noteTitleInput = noteModal.findElement(By.id("notes-title"));
        WebElement noteDescriptionInput = noteModal.findElement(By.id("notes-description"));
        WebElement submitButton = noteModal.findElement(By.id("SubmitNote"));

        //Filling in the form fields
        noteTitleInput.sendKeys("My first note");
        noteDescriptionInput.sendKeys("A lot to say");

        //Clicking the submit button
        submitButton.click();

        //Clicking the success button
        WebElement successButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("continue")));
        successButton.click();

        //Clicking note button
        WebElement reClickNoteButton = driver.findElement(By.id("nav-notes-tab"));
        reClickNoteButton.click();

        //Verifying that the new note is displayed in the table
        WebElement titleCell = driver.findElement(By.id("titleNote"));
        WebElement descriptionCell = driver.findElement(By.id("descriptionNote"));

        Thread.sleep(5000);

        assertEquals("My first note", titleCell.getText());
        assertEquals("A lot to say", descriptionCell.getText());

        List<WebElement> credentialsList = driver.findElements(By.id("credentialTable"));
        int initialCount = credentialsList.size();

        //Waiting for the delete button to become visible
        wait = new WebDriverWait(driver, 10);
        WebElement deleteButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("delete-note")));
        deleteButton.click();

        successButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("continue")));
        successButton.click();

        wait = new WebDriverWait(driver, 10);
        //Clicking credential button
        WebElement reClickNoteButtonAgain = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
        reClickNoteButtonAgain.click();
        Thread.sleep(5000);

        List<WebElement> updatedCredentialsList = driver.findElements(By.cssSelector("#notesTable tbody tr"));
        int finalCount = updatedCredentialsList.size();

        // Assertion
        assertEquals(initialCount - 1, finalCount);

        driver.quit();

    }

    private void doLogIn(String userName, String password)
    {
        // Log in to our dummy account.
        driver.get("http://localhost:" + this.port + "/login");
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
        WebElement loginUserName = driver.findElement(By.id("inputUsername"));
        loginUserName.click();
        loginUserName.sendKeys(userName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
        WebElement loginPassword = driver.findElement(By.id("inputPassword"));
        loginPassword.click();
        loginPassword.sendKeys(password);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
        WebElement loginButton = driver.findElement(By.id("login-button"));
        loginButton.click();

        webDriverWait.until(ExpectedConditions.titleContains("Home"));

    }
}
