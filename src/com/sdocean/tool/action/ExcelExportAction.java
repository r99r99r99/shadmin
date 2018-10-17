package com.sdocean.tool.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sdocean.log.service.OperationLogService;
import com.sdocean.station.service.StationService;

@Controller
public class ExcelExportAction {
	@Resource
	StationService stationService;
	@Resource
	OperationLogService logService;
	
	@RequestMapping("excelExportinfo.do")
	public void excelExportinfo(HttpServletRequest request,  
	        HttpServletResponse response)throws Exception{
		  request.setCharacterEncoding("utf-8");//注意编码
	      String fileName = request.getParameter("fileName");
	      String content = request.getParameter("content");
		  String[] array = content.split("[\\n]"); 
		 
		  SXSSFWorkbook wb = new SXSSFWorkbook(100); // keep 100 rows in memory,
		  Sheet sh = wb.createSheet();
		  int row = 0;
		  int h = 0;
		  for(int i=0;i<array.length;i++){
			  String rowString = array[i];
			  if(rowString!=null&&rowString.length()>0){
				  Row header = sh.createRow(row);
				  row++;
				  String[] cells = rowString.split(",");
				  for(int j=0;j<cells.length;j++){
					  Cell cell = header.createCell(j);
					  if(cells[j].contains("\"")){
						  cells[j] = cells[j].replaceAll("\"", "");
						  if(isDouble(cells[j].trim())||isNum(cells[j].trim())){
							  CellStyle styleFormat = wb.createCellStyle();
							  styleFormat.setFillForegroundColor(IndexedColors.TAN.getIndex());// #F2DEDE
								styleFormat.setFillPattern(CellStyle.SOLID_FOREGROUND);
							  cell.setCellValue(Double.parseDouble(cells[j]));
						  }else{
							  cell.setCellValue(cells[j]);
						  }
						  
					  }else{
						  if(isDouble(cells[j].trim())||isNum(cells[j].trim())){
							  CellStyle styleFormat = wb.createCellStyle();
							  styleFormat.setFillForegroundColor(IndexedColors.TAN.getIndex());// #F2DEDE
								styleFormat.setFillPattern(CellStyle.SOLID_FOREGROUND);
							  cell.setCellValue(Double.parseDouble(cells[j]));
						  }else{
							  cell.setCellValue(cells[j]);
						  }
					  }
					  
				  }
			  }
		  } 
		  
		  response.reset();
		  response.setContentType("application/x-msdownload");
		  String pName = fileName;
			try {
				response.setHeader("Content-Disposition", "attachment; filename="
						+ new String(pName.getBytes("gb2312"), "ISO-8859-1")
						+ ".xlsx");
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			ServletOutputStream outStream = null;

			try {
				outStream = response.getOutputStream();
				wb.write(outStream);
				wb.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					outStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	}
	
	public static boolean isDouble(String value) {
		  try {
		   Double.parseDouble(value);
		   if (value.contains("."))
		    return true;
		   return false;
		  } catch (NumberFormatException e) {
		   return false;
		  }
	}
	public static boolean isNum(String value){
		boolean result=value.matches("[0-9]+");
		return result;
	}
}
