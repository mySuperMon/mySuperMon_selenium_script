# mysupermon_selenium_script
Implementation of mySupermon-Selenium Integration
In the Selenium-TestNG Automation framework-
First, set up the test environment using 
```ruby
@BeforeSuite
	public void setup() {
System.setProperty("webdriver.chrome.driver", "H:\\SupermonData\\mySuperMon_Automation\\chromedriver\\chromedriver.exe");
		System.out.print("Inside setUp....... ");
		String browserName = readApplicationProperties("browserForTestRun");
		initializeDriver(browserName);
		driver.manage().window().maximize();
		loadApplicationByURL();
    }
 ```
In @BeforeSuite initialize the browser and load your application on which you want to automate.

Prepared the test case of your application-
```ruby
@Test(priority = 1)
	public void home() throws InterruptedException {
		waitForUserInterfaceToLoad(AppConstants.DEFAULT_WAIT_TIME);
driver.navigate().refresh();
		EventsUtil.clickWebElementr(By.xpath(readLocatorsProperties("Home_Page")));
		waitForUserInterfaceToLoad(AppConstants.DEFAULT_WAIT_TIME);
	}
 ```
In the above code, the TestNG @Test annotation method is used for writing a test case to which assign the test priority and written the simple test case to click the Home_page of my application.
But here I want to analyze the performance of the database after execution of the test case home. So here implemented the start/stop recording functionality of the mySupermon application.

Before the test case of application is execute implement mySupermon start recording REST API
```ruby
@Parameters({ "TestName" })
	@BeforeTest
	public void callStartRecording(String testName) throws InterruptedException {
Response res = RestAssured.given().header("Authorization", "Bearer " + accessToken)
				.header("applicationIdentifier", readApplicationProperties("applicationIdentifier"))
				.get(readApplicationProperties("Supermon_url") + "devaten/data/startRecording?usecaseIdentifier="
						+ testName);
//			System.out.println(res.getStatusCode());
		Thread.sleep(2500);
	}
  ```
In the above code @Parameters({TestName}) is the Name of the test which you can give in your testng.xml file this way
```ruby 
<test name="HomeTest">
		<parameter name="TestName" value="Home_Click" /> 
		<classes>
			<class name="com.trionxt.webdriver.scenarios.DemoTest">
				<methods>
				<include name="home" />
				</methods>
			</class>
		</classes>
	</test>
  ```
And pass the method name i.e. test case written namely home 
```ruby
<methods>
<include name="home" />
</methods>
```
In this way implemented the start recording API mySupermon
```ruby
Response res = RestAssured.given().header("Authorization", "Bearer " + accessToken)
				.header("applicationIdentifier", readApplicationProperties("applicationIdentifier"))
				.get(readApplicationProperties("Supermon_url") + "devaten/data/startRecording?usecaseIdentifier="
						+ testName);
 ```
In the above code, the start recording API of mySupermon accepts applicationIdentifier which passes through the properties file by declaring in that, and the second parameter is usecaseIdentifier=”testName” declared in testNG.xml file.After start recording implemented the stop recording using
```ruby
@Parameters({ "TestName" })
	@AfterTest
public void callStopRecording(String testName) throws InterruptedException {
Response res = RestAssured.given().header("Authorization", "Bearer " + accessToken)
				.header("applicationIdentifier", readApplicationProperties("applicationIdentifier"))
				.get(readApplicationProperties("Supermon_url") + "devaten/data/stopRecording?usecaseIdentifier="
						+ testName + "&inputSource=batFile");

//			System.out.println(res.getStatusCode());
		Thread.sleep(2500);
	}
  ```
Here Passed the same parameter of which pass in the start recording. And use @AfterTest annotation so that it can stop the recording after executing the test.  

Get a report after executing the use cases using 
```ruby
@AfterSuite
	public void close() throws InterruptedException {
		Response report = RestAssured.given().header("Authorization", "Bearer " + accessToken)
				.header("applicationIdentifier", readApplicationProperties("applicationIdentifier"))
				.get(readApplicationProperties("Supermon_url") + "devaten/data/generateReport");

//			System.out.println(report.getBody().asString());
		try (FileWriter file = new FileWriter("report/response.json")) {
			// File Writer creates a file in write mode at the given location
			file.write(report.getBody().asString());
			// write function is use to write in file,
			// here we write the Json object in the file
			file.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Thread.sleep(2000);
		// waitForUserInterfaceToLoad(Constant.DEFAULT_WAIT_TIME);
		driver.close();
	}
  ```
For getting a report used the mySupermon generate report API which is implemented in the @AfterSuite annotation method. The report is stored in the response.json file. 

You can view these executed use cases on your mySuperMon dashboard.

