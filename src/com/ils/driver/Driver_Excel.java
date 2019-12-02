package com.ils.driver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.ils.genericmethods.Generic_Methods;
import com.opera.core.systems.scope.protos.SelftestProtos.SelftestResult.Result;

public class Driver_Excel {
	static boolean result;
	public static void main(String[] args) throws InvalidFormatException, IOException {

		String current_path = System.getProperty("user.dir");
		File Excel_File = new File(current_path + "\\Driver_Excel.xlsx");
		FileInputStream fis = new FileInputStream(Excel_File);
		Workbook excelbook = WorkbookFactory.create(fis);
		Sheet sheet = excelbook.getSheet("Sheet1");
		int lst_row = sheet.getLastRowNum();

		for (int i = 1; i <= lst_row; i++) {
			String flag = sheet.getRow(i).getCell(0).getStringCellValue();
			if (flag.equalsIgnoreCase("Y")) {
				String EXL_NAME = sheet.getRow(i).getCell(1).getStringCellValue();

				System.out.println("Now we are going to work on Excel ----->>>>" + EXL_NAME);
				result=false;
				
				try {
					Test_Cases.Web_call(EXL_NAME);
					
					//result=Test_Cases.status_flag;
					result =Test_Cases.status_flag;
					
					System.out.println("---result---- " + result);
					
				} catch (InterruptedException e) {
					sheet.getRow(i).createCell(2).setCellValue("FAILED");
					sheet.getRow(i).getCell(2).setCellStyle(Generic_Methods.failobj(excelbook));
					e.printStackTrace();
					
				}
				
			
				if (result==true) {
					sheet.getRow(i).createCell(2).setCellValue("Failed");
					sheet.getRow(i).getCell(2).setCellStyle(Generic_Methods.failobj(excelbook));
						
				}
				else {
					sheet.getRow(i).createCell(2).setCellValue("Passed");
					sheet.getRow(i).getCell(2).setCellStyle(Generic_Methods.passobj(excelbook));
					
				}
				FileOutputStream fos = new FileOutputStream(Excel_File);
				excelbook.write(fos);

			}
			
			else {
				sheet.getRow(i).createCell(2).setCellValue("SKIPPED");
				sheet.getRow(i).getCell(2).setCellStyle(Generic_Methods.skipobj(excelbook));
				FileOutputStream fos = new FileOutputStream(Excel_File);
				excelbook.write(fos);
			}
			
		}

	}
	
	

}
