package com.ils.driver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.regex.Pattern;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.ils.genericmethods.Generic_Methods;

public class Test_Cases extends Generic_Methods {

	static String FCA = "https://ess-f0-f-1-alb-995328410.us-east-1.elb.amazonaws.com/fh/ilss/";
	static String FCB = "http://www.newtours.demoaut.com/";
	static String FCD = "http://www.newtours.demoaut.com/mercuryregister.php";
	static String Backend=" https://ess-external-alb-1667722264.us-gov-west-1.elb.amazonaws.com/ess-backend/";
	static String Frontend=" https://ess-external-alb-1667722264.us-gov-west-1.elb.amazonaws.com/ess/";

	static String F0B= "https://ess-f0-f-1-alb-995328410.us-east-1.elb.amazonaws.com/ess-backend/";
    static String F0F = " https://ess-f0-f-1-alb-995328410.us-east-1.elb.amazonaws.com/ess/";
	static String FA="https://ess-f0-f-1-alb-995328410.us-east-1.elb.amazonaws.com/fa/ess-backend/";
	static String FB= "https://ess-f0-f-1-alb-995328410.us-east-1.elb.amazonaws.com/fb/ess-backend/";
	static String FC= "https://ess-f0-f-1-alb-995328410.us-east-1.elb.amazonaws.com/fc/ess-backend/";
	static String FD= " https://ess-f0-f-1-alb-995328410.us-east-1.elb.amazonaws.com/fd/ess-backend/";
	static String FE= "https://ess-f0-f-1-alb-995328410.us-east-1.elb.amazonaws.com/fe/ess-backend/";
	static String FF= "https://ess-f0-f-1-alb-995328410.us-east-1.elb.amazonaws.com/ff/ess-backend/";
	static String FG = "https://ess-f0-f-1-alb-995328410.us-east-1.elb.amazonaws.com/fg/ess-backend/";
	static String FH = "https://ess-f0-f-1-alb-995328410.us-east-1.elb.amazonaws.com/fh/ilss/";
	static String FI= "https://ess-f0-f-1-alb-995328410.us-east-1.elb.amazonaws.com/fi/ess-backend/";
	static String fn_env = null;
	static String Browsername = "CH";
	
	public Test_Cases() throws IOException {
		super();

	}
	
		
	public static void Web_call(String Exl_Name) throws InvalidFormatException, IOException, InterruptedException
	{
		String[][] varArray = new String[200][2];
		String varEnv = null;
		WebElement elementVar = null;
		
		//Initialize varArray
		for (int i = 0; i < 200; i++) {
		    for (int j = 0; j < 2; j++) {
			
		        varArray[i][j] = "";
		    }
		}
		
		String current_path = System.getProperty("user.dir");
		File Excel_File = new File(current_path + "\\" + Exl_Name);
		FileInputStream fis = new FileInputStream(Excel_File);
		Workbook excelbook = WorkbookFactory.create(fis);
		Sheet sheet = excelbook.getSheet("Sheet1");
		int lst_row = sheet.getLastRowNum();
		int lst_Cell = sheet.getRow(0).getLastCellNum();
		for (int rw = 0; rw <= lst_row; rw++) {
			for (int cl = 0; cl < lst_Cell; cl++) {
				if (sheet.getRow(rw).getCell(cl) == null) {
					sheet.getRow(rw).createCell(cl);
				}
				sheet.getRow(rw).getCell(cl).setCellType(1);
			}
		}
		
		
		
		for (int i = 1; i <= lst_row; i++) {
			String flag = sheet.getRow(i).getCell(1).getStringCellValue();
			if (flag.equalsIgnoreCase("Y")) {

				String Action = sheet.getRow(i).getCell(2).getStringCellValue();
				String Locator = sheet.getRow(i).getCell(3).getStringCellValue();
				String Value = sheet.getRow(i).getCell(4).getStringCellValue();
				

				System.out.println("Action---" + Action);
				try {
				switch (Action) {

				case "Browser_Name":
					driver(Value);
					break;
				case "url":
					url(env(Value));
					
					//Set varEnv for use when looking enviroment variables 
					if (Value.length() > 2) {
						varEnv = Value.substring(0,1);
					} else {
						varEnv = Value;
					}
					break;
				case "inputvalue":
					getelement(Locator).sendKeys(Value);
					break;
				case "enter":
					getelement(Locator).sendKeys(Keys.ENTER);
					break;
				case "click":
					getelement(Locator).click();
					Thread.sleep(1000);
					break;
				case "submit":
					getelement(Locator).submit();
					Thread.sleep(1000);
					break;
				case "close":
					driver.quit();
					break;
				case "Alert":
					Thread.sleep(1000);
					Alert AR = driver.switchTo().alert();
					AR.accept();
					break;
				case "get":
					String getstr = getelement(Locator).getText();
					sheet.getRow(i).getCell(3).setCellValue(getstr);
					System.out.println("Get Text value is -" + getstr);
					break;
					
	//Raghav- I have commented out below code as code is not correct.				
				
/*				case "Join":
					used to create a variable that can store a string or concatenate strings  
					 * and other variables using the "|" as a delimiter (variable names will 
					 * be prefixed with ^ to indicate they are variables) creating variable value    
					 * 			  			  
					 * Spreadsheet format:
					 * Run	Action		Locator				Value
					 * y	Join		variable name	 	string or string|^variable...
					 * y	Join		codeRollRespVar		r|^codeRollDodaac
					 * Var	N			N					Y
					 
					String varValue = ""; 
					String varName = Locator;
					
					//Check for concatenation character "|" in inputString
					if(Value.indexOf("|")>-1) {
						//Steps concatenation
						//System.out.println("Concat Req'd");
						
						String workingStr = Value;
						
						//Using regex patternString escaped by "\\Q" for the regex meta "|" followed by "\\E"  
						String patternString = "\\Q|\\E";
						Pattern pattern = Pattern.compile(patternString);
						String[] strParts = pattern.split(workingStr);
						
						int m = strParts.length;
						i = 0;
						Value = "";
						
						//Work individual parts of the string
						while (i < m) {
							
							if (strParts[i].substring(0,1).contentEquals("^")) {
								//strPart is a variable (starts with "^") so lookup variable and append
							
								varName = strParts[i].substring(1, strParts[i].length());
								
								Value = Value + LookupVar(varName,varArray,varEnv);
							} else {
								//strPart is not a variable so append it
								Value = Value + strParts[i];
							}
							
							i = i + 1;
							
							varValue = Value;
						}
						
					} else {
					   //Steps for no concatenation
					   //System.out.println("No Concat Req'd");
						varValue = Value;
						
					}

					//Call SaveVar to store data
					SaveVar(varName,varValue, varArray);

					
					break;
					
				case "ReCheck":
					Used for regular expression check to validate information reflected on a web 
					 * element 
					 * 			  			  
					 * Spreadsheet format:
					 * Run	Action		Locator				Value
					 * y	ReCheck		attribute:value		String to use for Check
					 * y	ReCheck		html id:content		[0-9]{3}0001REJ INPUT COLUMNS WITH X BELOW ARE INVALID - INITIATOR                   SD: 01 DATE [0-9]{5} TIME [0-9]{4}    000000 TR NR [0 ]{5} NGV431
					 * Var	N			N:Y				 	Y
					 
					//insert  steps

					
					//Parse locator and value from searchName
					String[] LocatorArray  = Locator.split("#", 2);
					
					String locator = LocatorArray[0];
					String locatorVal = LocatorArray[1];
					
					//Lookup variable if first position is "^" for locatorVal
					if (locatorVal.substring(0,1).contentEquals("^")) {
						//strPart is a variable (starts with "^") so lookup variable and return
					
						varName = locatorVal.substring(1, locatorVal.length());
						
						locatorVal = LookupVar(varName,varArray,varEnv);
					}
					
					//Set page element based on locator and locatorVal
					elementVar = FindWebElement(driver, locator, locatorVal);
					
					String valText = elementVar.getText();
					
					//Replace line separators in valText
					valText = valText.replaceAll("\\r\\n|\\r|\\n", "");
					//System.out.println(valText);
					
					
					//Lookup variable if first position is "^" for inputString
					if (Value.substring(0,1).contentEquals("^")) {
						//strPart is a variable (starts with "^") so lookup variable and return
					
						varName = Value.substring(1, Value.length());
						
						Value = LookupVar(varName,varArray,varEnv);
					}
					
					//Add .* to front and back of regular expression because Java matches to entire string
					Value = ".*" + Value + ".*";
					
					//Regular Expression check
					if (Pattern.matches(Value,valText) == true) {
						System.out.println("PASS---'" + valText + "'");
						System.out.println("Matched as a Regular Expression to:");
						System.out.println("'" + Value + "'");		
					}else {
						System.out.println("Fail---'" + valText + "'");
						System.out.println("Did not contain Regular Expression:");
						System.out.println("'" + Value + "'");
					}		

					break;
	
				case "Check":
					Used to validate information reflected on a web element, checks two ways:     
					 * in string and exact match
					 * 			  			  
					 * Spreadsheet format:
					 * Run	Action		Location			Value
					 * y	Check		attribute:value		attribute:value
					 * y	Check		html id:content		Reject
					 * Var	N			N:Y				 	Y
					 
					//System.out.println("Check Steps");
					
					LocatorArray  = Locator.split("#", 2);
					
					//Parse locator and value from searchName
					locator  = LocatorArray[0];
					locatorVal = LocatorArray[1];
					
					//Lookup variable if first position is "^" for locatorVal
					if (locatorVal.substring(0,1).contentEquals("^")) {
						//strPart is a variable (starts with "^") so lookup variable and return
					
						varName = locatorVal.substring(1, locatorVal.length());
						
						locatorVal = LookupVar(varName,varArray,varEnv);
					}
					
					//Set page element based on locator and locatorVal
					elementVar = FindWebElement(driver, locator, locatorVal);
					
					valText = elementVar.getText();
					
					//Replace line separators in valText
					valText = valText.replaceAll("\\r\\n|\\r|\\n", "");
					//System.out.println(valText);
					
					
					//Lookup variable if first position is "^" for inputString
					if (Value.substring(0,1).contentEquals("^")) {
						//strPart is a variable (starts with "^") so lookup variable and return
					
						varName = Value.substring(1, Value.length());
						
						Value = LookupVar(varName,varArray,varEnv);
					}
					
					if (valText == Value) {
						System.out.println("PASS---'"+valText + "' was equal to '" + Value+"'");
					}else if (valText.contains(Value) == true) {
						System.out.println("PASS---'"+valText + "' contained the string '" + Value+"'");
					}
					*/
					
				default:
					System.out.println("Invalid Action!");
					break;

				}

				System.out.println(">>>>>>>>>>>>>>>>>>>>> Line number -> " + i + " is completed");
				sheet.getRow(i).createCell(5).setCellValue("Passed");
				sheet.getRow(i).getCell(5).setCellStyle(Generic_Methods.passobj(excelbook));
				
				
			}
			
			catch (Exception e)
			{
				System.out.println(" Now we are in Exception block.....for FAILED for Line number.." + i);
				sheet.getRow(i).createCell(5).setCellValue("Failed");
				sheet.getRow(i).getCell(5).setCellStyle(Generic_Methods.failobj(excelbook));
			}
			
		
		
			
			
			FileOutputStream fos = new FileOutputStream(Excel_File);
			excelbook.write(fos);
				
				
				

			} else {
				sheet.getRow(i).createCell(5).setCellValue("Skipped");
				sheet.getRow(i).getCell(5).setCellStyle(Generic_Methods.skipobj(excelbook));
			}
		}
		
		System.out.println("................................. TEST CASE END .......................................");

	}

	public static String env(String val) {
		if (val.equalsIgnoreCase("FCA")) {
			fn_env = FCA;
		} else if (val.equalsIgnoreCase("FCB")) {
			fn_env = FCB;
		} else if (val.equalsIgnoreCase("FCD")) {
			fn_env = FCD;
		} else if (val.equalsIgnoreCase("FH")) {
			fn_env = FH;
		}
		else {
			fn_env =val;
		}
			
		return fn_env;
	}

}
