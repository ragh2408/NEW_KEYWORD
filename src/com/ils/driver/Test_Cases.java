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
	static boolean status_flag =false;
	
	
	public Test_Cases() throws IOException {
		super();

	}
	
		
	public static void Web_call(String Exl_Name) throws InvalidFormatException, IOException, InterruptedException
	{
		status_flag =false;
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
					
	
					
				default:
					System.out.println("Invalid Action!");
					break;

				}

				System.out.println(">>>>>>>>>>>>>>>>>>>>> Line number -> " + i + " is completed");
				sheet.getRow(i).createCell(5).setCellValue("Passed");
				sheet.getRow(i).getCell(5).setCellStyle(Generic_Methods.passobj(excelbook));
				//status_flag="Passed";
				
			}
			
			catch (Exception e)
			{
				System.out.println(" Now we are in Exception block.....for FAILED for Line number.." + i);
				sheet.getRow(i).createCell(5).setCellValue("Failed");
				status_flag=true;
				
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
	
	public static String status_check (String flag)
	{
		return flag;
	}

}
