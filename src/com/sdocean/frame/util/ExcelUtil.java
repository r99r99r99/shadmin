package com.sdocean.frame.util;

import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExcelUtil {
	
	
	
	public static void fillSheet(WritableSheet sheet, ExcelBean excelBean,Integer[][] mergerCell,int beginRow) throws Exception{
		Label label;
		String[] title = excelBean.getTitle();
		List<String[]> list = excelBean.getData();
		int localBeginRow=beginRow;
		for(int x=0; x<title.length; x++){   
			label = new Label(x, localBeginRow, title[x]);
			WritableFont font = new WritableFont(WritableFont.ARIAL, 11, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
		    WritableCellFormat cFormat = new WritableCellFormat(font);
		    cFormat.setAlignment(Alignment.CENTRE);
		    cFormat.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
		    
		    if (excelBean.getColumnWidth() != null){
		    	sheet.setColumnView(x, excelBean.getColumnWidth()[x]);
		    }
		    
			label.setCellFormat(cFormat);
        	sheet.addCell(label); 
		}
		if(mergerCell!=null){
			for (Integer[] rowNum : mergerCell) {
				sheet.mergeCells(rowNum[0], localBeginRow, rowNum[1], localBeginRow);
			}
		}
		
		for(int y=0; y<list.size(); y++){
			++localBeginRow;
			for(int x=0; x<title.length; x++){ 
				label = new Label(x, localBeginRow, list.get(y)[x]);
				WritableCellFormat cFormatOnlyCenter = new WritableCellFormat();
				cFormatOnlyCenter.setAlignment(Alignment.CENTRE);
				cFormatOnlyCenter.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
				label.setCellFormat(cFormatOnlyCenter);
            	sheet.addCell(label); 
			}
			if(mergerCell!=null){
				for (Integer[] rowNum : mergerCell) {
					sheet.mergeCells(rowNum[0], localBeginRow, rowNum[1], localBeginRow);
				}
			}
			
		}
	}
	/**
	 * 导出Excel
	 * 
	 * @param response
	 * @param sheetName
	 * @param fileName
	 * @param title
	 * @param list
	 * @throws Exception
	 */
	public static void expordExcel(HttpServletResponse response, ExcelBean excelBean) throws Exception {
		
		OutputStream os = response.getOutputStream();// 取得输出流
		response.setHeader("Content-disposition", "attachment;filename=" + excelBean.getFileName() + ".xls");// 设定输出文件头
		response.setContentType("application/msexcel");// 定义输出类型
		
		WritableWorkbook wbook = Workbook.createWorkbook(os); // 建立excel文件
		WritableSheet sheet = wbook.createSheet(excelBean.getSheetName(), 0); // sheet名称
		
		Label label;
		
		String[] title = excelBean.getTitle();
		List<String[]> list = excelBean.getData();
		
		for(int x=0; x<title.length; x++){   
			label = new Label(x, 0, title[x]);
			WritableFont font = new WritableFont(WritableFont.ARIAL, 11, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
		    WritableCellFormat cFormat = new WritableCellFormat(font);
		    
		    if (excelBean.getColumnWidth() != null){
		    	System.out.println( excelBean.getColumnWidth()[x]+"--------------------");
		    	sheet.setColumnView(x, excelBean.getColumnWidth()[x]);
		    }
		    
			label.setCellFormat(cFormat);
        	// 将定义好的单元格添加到工作表中   
        	sheet.addCell(label); 
		}
		
		for(int y=0; y<list.size(); y++){   
			for(int x=0; x<title.length; x++){ 
				label = new Label(x, y+1, list.get(y)[x]);
            	// 将定义好的单元格添加到工作表中   
            	sheet.addCell(label); 
			}
		}
	
		wbook.write(); // 写入文件
		wbook.close();
		os.close();
	}
}
