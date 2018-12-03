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
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class NonFunctionalTests {
	
	final static Logger logger = Logger.getLogger(CalculatorTestClass.class);
	
	public String baseUrl = "http://output.jsbin.com/hudape/1";
	String driverPath = "C:\\geckodriver.exe";
	Random random = new Random();
	
	@Test( threadPoolSize = 3, invocationCount = 10,
			description="Verify home page load time by starting a thread pool, which contains 3 threads, and running the test method 10 times")
	  public void homePageLoadTest() {

		System.out.printf("%n[START] Thread Id : %s is started!", Thread.currentThread().getId());
			
		System.setProperty("webdriver.gecko.driver", driverPath);
		WebDriver driver = new FirefoxDriver();
		driver.get(baseUrl);
			
		System.out.printf("%n[END] Thread Id : %s", Thread.currentThread().getId());
			
		driver.close();

	  }
	
	
	@Test(description="Verify if user can delete a digits one by one using backspace key")
	  public void BackSpaceTest() {
			
		System.setProperty("webdriver.gecko.driver", driverPath);
		WebDriver driver = new FirefoxDriver();
		driver.get(baseUrl);
		
		logger.debug("Click on 8");
		driver.findElement(By.cssSelector("body > div > div.inputs > button:nth-child(8)")).click();
		logger.debug("Click on DIV");
		driver.findElement(By.cssSelector("body > div > div.inputs > button:nth-child(16)")).click();
		logger.debug("Click on 0");
		driver.findElement(By.cssSelector("body > div > div.inputs > button:nth-child(10)")).click();
		logger.debug("Click on DEL");
		driver.findElement(By.cssSelector("body > div > div.inputs > button:nth-child(11)")).click();
		logger.debug("Click on 2");
		driver.findElement(By.cssSelector("body > div > div.inputs > button:nth-child(2)")).click();
		logger.debug("Click on EQ");
		driver.findElement(By.cssSelector("body > div > div.inputs > button:nth-child(12)")).click();
		
		String actualResult = driver.findElement(By.cssSelector("#output")).getText();
		String expectedResult ="4";
		logger.debug("actualResult is:" + actualResult);
		Assert.assertEquals(actualResult, expectedResult);
		
		driver.close();

	  }
	
	
	@Test(dataProvider="TwoOperatorTC",
		description="Verify that on pressing same operator one after the other, the latest one will override the previous operator or ERR displayed")
	  public void TwoOperatorTest(String value1, String operator,String value2, String expectedResult1, String expectedResult2) {
			
		System.setProperty("webdriver.gecko.driver", driverPath);
		WebDriver driver = new FirefoxDriver();
		driver.get(baseUrl);
		
		operator = getOpInd(operator);
		
		logger.debug("Click on value1");
		driver.findElement(By.cssSelector("body > div > div.inputs > button:nth-child("+value1+")")).click();
		logger.debug("first Click on operator");
		driver.findElement(By.cssSelector("body > div > div.inputs > button:nth-child("+operator+")")).click();
		logger.debug("second Click on same operator");
		driver.findElement(By.cssSelector("body > div > div.inputs > button:nth-child("+operator+")")).click();
		logger.debug("Click on value2");
		driver.findElement(By.cssSelector("body > div > div.inputs > button:nth-child("+value2+")")).click();
		logger.debug("Click on EQ");
		driver.findElement(By.cssSelector("body > div > div.inputs > button:nth-child(12)")).click();
		
		String actualResult = driver.findElement(By.cssSelector("#output")).getText();
		
		logger.debug("actualResult is:" + actualResult);
		Assert.assertTrue(actualResult.equals(expectedResult1)||actualResult.equals(expectedResult2), "Expected "+expectedResult1+" or "+expectedResult2+ " but found "+actualResult);
		
		driver.close();

	  }
	
	@DataProvider(name = "TwoOperatorTC")
	public Object[][] TwoOperatorTC() {
		Object[][] arrayObject = getExcelData(
				"CalculatorTest/src/main/java/LogMeInCalcTest/CalcDataProvider.xls","TwoOperatorTC");
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
}
