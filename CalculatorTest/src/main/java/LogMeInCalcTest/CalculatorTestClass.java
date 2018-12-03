package LogMeInCalcTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


public class CalculatorTestClass {

	final static Logger logger = Logger.getLogger(CalculatorTestClass.class);

	public String baseUrl = "http://output.jsbin.com/hudape/1";
	String driverPath = "C:\\geckodriver.exe";
	public WebDriver driver;
	Random random = new Random();

	@BeforeMethod
	public void launchBrowser() {
		System.out.println("launching browser");
		System.setProperty("webdriver.gecko.driver", driverPath);
		DesiredCapabilities capabilities = DesiredCapabilities.firefox();
		capabilities.setCapability("marionette", true);
		driver = new FirefoxDriver(capabilities);
		driver.get(baseUrl);
	}

	@Test(dataProvider = "TwoValueOperation",
		description="Verify the arithmetic operations are working fine for 2 values")
	public void verifyTwoValueOperation(String value1, String operator,String value2, String expectedResult) throws InterruptedException {
		logger.debug("first value is:" + value1);
		logger.debug("Operator value is:" + operator);
		operator = getOpInd(operator);
		logger.debug("second value is:" + value2);
		
		for (int i = 0; i < value1.length(); i++) {

			logger.debug("Click on :" + value1.charAt(i));

			int valueInd = getValueInd(value1.charAt(i));
			
			driver.findElement(By.cssSelector("body > div > div.inputs > button:nth-child("+ valueInd + ")")).click();
		}

		logger.debug("Click on operator");
		driver.findElement(By.cssSelector("body > div > div.inputs > button:nth-child("+ operator + ")")).click();

		for (int i = 0; i < value2.length(); i++) {

			logger.debug("Click on :" + value2.charAt(i));

			int valueInd = getValueInd(value2.charAt(i));
			
			driver.findElement(By.cssSelector("body > div > div.inputs > button:nth-child("+ valueInd + ")")).click();
		}

		logger.debug("click on EQ button");
		driver.findElement(By.cssSelector("body > div > div.inputs > button:nth-child(12)")).click();

		logger.debug("expectedResult is:" + expectedResult);
		String actualResult = driver.findElement(By.cssSelector("#output")).getText();
		logger.debug("actualResult is:" + actualResult);
		
		Assert.assertEquals(actualResult, expectedResult);
	}

	 @Test(dataProvider="ThreeValueOperation",
		description="Verify the arithmetic operations are working fine and respecting order for 3 values")
	 public void verifyThreeValueOperation(String value1,String operator1,String value2,String operator2,String value3,String expectedResult) throws InterruptedException {
		 logger.debug("first value is:"+value1);
		 operator1=getOpInd(operator1);
		 logger.debug("first operator is:"+operator1);
		 logger.debug("second value is:"+value2);
		 operator2=getOpInd(operator2);
		 logger.debug("second operator is:"+operator2);
		 logger.debug("third value is:"+value3);
		
		 for (int i = 0; i < value1.length(); i++) {
		
			 logger.debug("Click on :"+value1.charAt(i));
			
			 int valueInd=getValueInd(value1.charAt(i));
			 driver.findElement(By.cssSelector("body > div > div.inputs > button:nth-child("+valueInd+")")).click();
		 }
		
		 logger.debug("Click on operator 1");
		 driver.findElement(By.cssSelector("body > div > div.inputs > button:nth-child("+operator1+")")).click();
		
		 for (int i = 0; i < value2.length(); i++) {
		
			 logger.debug("Click on :"+value2.charAt(i));
			
			 int valueInd=getValueInd(value2.charAt(i));
			 driver.findElement(By.cssSelector("body > div > div.inputs > button:nth-child("+valueInd+")")).click();
		 }
		
		 logger.debug("Click on operator 2");
		 driver.findElement(By.cssSelector("body > div > div.inputs > button:nth-child("+operator2+")")).click();
		
		 for (int i = 0; i < value3.length(); i++) {
		
			 logger.debug("Click on :"+value3.charAt(i));
			
			 int valueInd=getValueInd(value3.charAt(i));
			 driver.findElement(By.cssSelector("body > div > div.inputs > button:nth-child("+valueInd+")")).click();
		 }
		 
		 logger.debug("click on EQ button");
		 driver.findElement(By.cssSelector("body > div > div.inputs > button:nth-child(12)")).click();
		
		 logger.debug("expectedResult is:"+expectedResult);
		 String actualResult = driver.findElement(By.cssSelector("#output")).getText();
		 logger.debug("actualResult is:"+actualResult);
		 Assert.assertEquals(actualResult, expectedResult);
	 }
	
	 
	 @Test(invocationCount=10, description="Verify the arithmetic operations of display ERR when second number is not provided")
			public void verifyTwoValueOperation() throws InterruptedException {
				String value1 = String.valueOf(random.nextInt(20)+1);
		 		logger.debug("first value is:" + value1);
		 		int operator = random.nextInt(3)+13;
				logger.debug("Operator value is:" + operator);
				
				for (int i = 0; i < value1.length(); i++) {

					logger.debug("Click on :" + value1.charAt(i));

					int valueInd = getValueInd(value1.charAt(i));
					
					driver.findElement(By.cssSelector("body > div > div.inputs > button:nth-child("+ valueInd + ")")).click();
				}

				logger.debug("Click on operator");
				driver.findElement(By.cssSelector("body > div > div.inputs > button:nth-child("+ operator + ")")).click();


				logger.debug("click on EQ button");
				driver.findElement(By.cssSelector("body > div > div.inputs > button:nth-child(12)")).click();

				logger.debug("expectedResult is: ERR");
				String actualResult = driver.findElement(By.cssSelector("#output")).getText();
				logger.debug("actualResult is:" + actualResult);
				
				Assert.assertEquals(actualResult, "ERR");
			}
	
	
	@AfterMethod
	public void terminateBrowser() {
		driver.close();
	}

	
	@DataProvider(name = "TwoValueOperation")
	public Object[][] TwoValueOperation() {
		Object[][] arrayObject = getExcelData(
				"CalculatorTest/src/main/java/LogMeInCalcTest/CalcDataProvider.xls",
				"TwoValueFunctionalTC");
		return arrayObject;
	}

	@DataProvider(name = "ThreeValueOperation")
	public Object[][] ThreeValueOperation() {
		Object[][] arrayObject = getExcelData(
				"CalculatorTest/src/main/java/LogMeInCalcTest/CalcDataProvider.xls",
				"ThreeValueFunctionalTC");
		return arrayObject;
	}

	/**
	 * @param File
	 *            Name
	 * @param Sheet
	 *            Name
	 * @return
	 */
	public String[][] getExcelData(String fileName, String sheetName) {
		String[][] arrayExcelData = null;
		try {
			FileInputStream fs = new FileInputStream(fileName);
			Workbook wb = Workbook.getWorkbook(fs);
			Sheet sh = wb.getSheet(sheetName);

			int totalNoOfCols = sh.getColumns();
			int totalNoOfRows = sh.getRows();

			arrayExcelData = new String[totalNoOfRows - 1][totalNoOfCols];

			for (int i = 1; i < totalNoOfRows; i++) {

				for (int j = 0; j < totalNoOfCols; j++) {
					arrayExcelData[i - 1][j] = sh.getCell(j, i).getContents();
				}

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			e.printStackTrace();
		} catch (BiffException e) {
			e.printStackTrace();
		}
		return arrayExcelData;
	}

	public String getOpInd(String op) {
		switch (op) {
		case "+":
			logger.debug("operator is addition ");
			op = "13";
			break;
		case "-":
			logger.debug("operator is subtraction ");
			op = "14";
			break;
		case "*":
			logger.debug("operator is multiplication ");
			op = "15";
			break;
		case "/":
			logger.debug("operator is division ");
			op = "16";
			break;
		}
		return op;
	}

	public int getValueInd(char value) {
		int valueInd = Character.getNumericValue(value);

		switch (value) {
		case '0':
			valueInd = 10;
			break;
		case '+':
			valueInd = 13;
			break;
		case '-':
			valueInd = 14;
			break;
		case '*':
			valueInd = 15;
			break;
		case '/':
			valueInd = 16;
			break;
		}
		return valueInd;
	}
}
