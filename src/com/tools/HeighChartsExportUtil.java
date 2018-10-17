package com.tools;

import java.io.IOException;
import java.io.StringReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.fop.svg.PDFTranscoder;

public class HeighChartsExportUtil  extends HttpServlet{
	private static final long serialVersionUID = 2144151124530961067L;
	public HeighChartsExportUtil() {
	      super();
	} 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	      doPost(request,response);
	   }    
	  
	   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		   Map map = new HashMap();
	      request.setCharacterEncoding("utf-8");//注意编码
	      String type = request.getParameter("type");
	      String svg = request.getParameter("svg");
	      String fileName = "数据报表";
	      String filename = request.getParameter("filename");
	      response.setCharacterEncoding("utf-8");
	      ServletOutputStream out = response.getOutputStream();
	      if(filename!=null&&filename.length()>0){
	    	  fileName = filename;
	      }
	      
	      if (null != type && null != svg){
	      svg = svg.replaceAll(":rect", "rect");
	      String ext = "";
	      Transcoder t = null;
	     if (type.equals("image/png")) {
	         ext = "png";
	         t = new PNGTranscoder();    
	      } else if (type.equals("image/jpeg")) {
	         ext = "jpg";
	          t = new JPEGTranscoder();
	      } else if (type.equals("application/pdf")) {
	         ext = "pdf";
	         t = new PDFTranscoder();
	      } else if (type.equals("image/svg+xml")) {
	         ext = "svg";  
	      }
	     
	      response.addHeader("Content-Disposition", "attachment; filename="+new String((fileName).getBytes("GB2312"),"iso8859-1")+"."+ext);
	      response.addHeader("Content-Type", type);
	       if (null != t){
	            TranscoderInput input = new TranscoderInput(new StringReader(svg));
	            TranscoderOutput output = new TranscoderOutput(out);
	            try {
	               t.transcode(input,output);
	            } catch (TranscoderException e){
	               out.print("编码流错误.");
	               e.printStackTrace();
	            }
	           } else if (ext == "svg"){
	                     svg =  svg.replace("http://www.w3.org/2000/svg", "http://www.w3.org/TR/SVG11/");
	            out.print(svg);
	         } else {
	            out.print("Invalid type: " + type);
	         }
	      } else {
	         response.addHeader("Content-Type", "text/html");
	      }
	      out.flush();
	      out.close();  
	   }
}
