package xiongxing.reptile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import xiongxing.reptile.model.ClinicInfo;

public class JavaCaiPiaoTest2 {
	
	private static Integer pageTotal = 1;
	
	private static ArrayList<ClinicInfo> totalList = new ArrayList<ClinicInfo>();
	
	public void init(String inputHtml,Integer pageNo,String cityName) {
		
		Document doc = Jsoup.parse(inputHtml);
		Elements els = doc.select("[borderColorDark]");
		if(pageNo == 1){
			Elements floot = doc.select("[bgcolor=#c2def5]"); //bgcolor="#c2def5"
			Elements lastPage = floot.select("a:contains(最后页)");
			String hrefVal = lastPage.get(0).attr("href");
			String total = hrefVal.replace("javascript:jump(", "").replace(")", "");
			System.out.println("总页数："+total);
			pageTotal = Integer.parseInt(total);
		}
		System.out.println("第"+pageNo+"页,table几个：" + els.size());		
		System.out.println(cityName + "总的List个数：" + totalList.size());
	}

	public void createExcel(ArrayList<ClinicInfo> list , String fileName){
		String fname = "江苏省资源表";
		HSSFWorkbook workbook = null;
		workbook = new HSSFWorkbook();
	    
	    //获取List size作为excel行数
	    int rowCount = list.size();
	    HSSFSheet sheet = workbook.createSheet(fname);
	    //创建第一栏
	    HSSFRow headRow = sheet.createRow(0);
	    String[] titleArray = {"城市","单位名称", "机构类别", "级别", "等次", "地址 ", "邮编","电话","实际床位数","职工总数","卫技人数"};
	    //获取参数个数作为excel列数
	    int columeCount = titleArray.length;
	    for(int m=0;m<=columeCount-1;m++)
	    {
	        HSSFCell cell = headRow.createCell(m);
	        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
	        //sheet.setColumnWidth(m, 6000);
	        HSSFCellStyle style = workbook.createCellStyle();
	        HSSFFont font = workbook.createFont();
	        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	        short color = HSSFColor.RED.index;
	        font.setColor(color);
	        style.setFont(font);
	        //填写数据
	        cell.setCellStyle(style);
	        cell.setCellValue(titleArray[m]);
	    }
	    
	    int index = 0;
	    //写入数据
	    for(ClinicInfo entity : list)
	    {
	        //logger.info("写入一行");
	        HSSFRow row = sheet.createRow(index+1);
	        for(int n=0;n<=columeCount-1;n++){
	        	row.createCell(n);
	        }
	        row.getCell(0).setCellValue(entity.getCityName());
	        row.getCell(1).setCellValue(entity.getUnitName());
	        row.getCell(2).setCellValue(entity.getJgType());
	        row.getCell(3).setCellValue(entity.getLevel());
	        row.getCell(4).setCellValue(entity.getKind());
	        row.getCell(5).setCellValue(entity.getAddr());
	        row.getCell(6).setCellValue(entity.getZipCode());
	        row.getCell(7).setCellValue(entity.getTel());
	        row.getCell(8).setCellValue(entity.getBedNum());
	        row.getCell(9).setCellValue(entity.getStaffNum());
	        row.getCell(10).setCellValue(entity.getWorkers());
	        index++;
	    }
	    //写到磁盘上
	    try {
	        FileOutputStream fileOutputStream = new FileOutputStream(new File("/Users/bigbear/Desktop/"+fileName+".xlsx"));
	        workbook.write(fileOutputStream);
	        fileOutputStream.close();
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    
	}
	
	public static void main(String[] args) throws InterruptedException, ClientProtocolException, IOException {
		JavaCaiPiaoTest2 test = new JavaCaiPiaoTest2();
		Map<String, String> params = new HashMap<String, String>();
//		http://caipiao.163.com/award/cqssc/20170521.html
		
//		String inputHtml = JavaReptileTest.sendSSLPostRequest("http://caipiao.163.com/award/cqssc/20170512.html",params);
		String inputHtml = JavaReptileTest.sendGetRequest("http://www.cqcp.net/game/ssc/");
//		Document doc = Jsoup.parse(inputHtml);
		System.out.println(inputHtml);
		//test.createExcel(totalList, "江苏省资源表");
	}
}
