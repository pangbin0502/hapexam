package hbi.core.order.util;

import hbi.core.order.dto.OmOrderHeaders;
import hbi.core.order.dto.OmOrderLines;
import hbi.core.order.dto.ArCustomers;
import hbi.core.order.dto.InvInventoryItems;
import hbi.core.order.dto.OrgCompanys;
import hbi.core.order.mapper.ArCustomersMapper;
import hbi.core.order.mapper.InvInventoryItemsMapper;
import hbi.core.order.mapper.OrgCompanysMapper;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;


public class ExportExcel {

    @Autowired
    ArCustomersMapper arCustomersMapper;
    @Autowired
    OrgCompanysMapper orgCompanysMapper;
    @Autowired
    InvInventoryItemsMapper invInventoryItemsMapper;


    public void exportexcel(List<OmOrderHeaders> omOrderHeaderss,HttpServletResponse response) throws IOException {
        //创建HSSFWorkbook对象(excel的文档对象)
        HSSFWorkbook wb = new HSSFWorkbook();
        //建立新的sheet对象（excel的表单）
        HSSFSheet sheet = wb.createSheet("订单信息");
        
        //在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
        //行数
        HSSFRow row1 = sheet.createRow(0);
        
        //创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
        //列数

        HSSFCell cell = row1.createCell(0);
        //设置单元格内容
        //合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
        //sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
        //创建单元格并设置单元格内容
        //销售订单号	公司名称	客户名称	订单日期	订单状态	物料编码	物料描述	数量	销售单价	金额

        row1.createCell(0).setCellValue("销售订单号");
        row1.createCell(1).setCellValue("公司名称");
        row1.createCell(2).setCellValue("客户名称");
        row1.createCell(3).setCellValue("订单日期");
        row1.createCell(4).setCellValue("订单状态");
        row1.createCell(5).setCellValue("物料编码");
        row1.createCell(6).setCellValue("物料描述");
        row1.createCell(7).setCellValue("数量");
        row1.createCell(8).setCellValue("销售单价");
        row1.createCell(9).setCellValue("金额");


        int rowNum = 1;
        for (int i = 0; i < omOrderHeaderss.size(); i++) {

            OmOrderHeaders omOrderHeaders = omOrderHeaderss.get(i);
            String orderNumber = omOrderHeaders.getOrderNumber();

            ArCustomers customers = new ArCustomers();
            customers.setCustomerId(omOrderHeaders.getCustomerId());
             ArCustomers newCustomers = arCustomersMapper.select(customers).get(0);
            String customerName = newCustomers.getCustomerName();

            OrgCompanys companys = new OrgCompanys();
            companys.setCompanyId(omOrderHeaders.getCompanyId());
            companys = orgCompanysMapper.selectByPrimaryKey(companys);
            String companyName = companys.getCompanyName();



            String orderDate = String.valueOf(omOrderHeaders.getOrderDate());
            String orderStatus = omOrderHeaders.getOrderStatus();

            HSSFRow newRow = sheet.createRow(rowNum);
            if(omOrderHeaders.getOmOrderLiness() != null) {
                for (int j = 0; j < omOrderHeaders.getOmOrderLiness().size(); j++) {
                    OmOrderLines omOrderLines = omOrderHeaderss.get(i).getOmOrderLiness().get(j);


                    InvInventoryItems items = new InvInventoryItems();
                    items.setInventoryItemId(omOrderLines.getInventoryItemId());
                    items = invInventoryItemsMapper.selectOptionsByPrimaryKey(items);
                    String itemNum = String.valueOf(items.getItemCode());
                    String itemDescription = items.getItemDescription();




                    String orderdQuantity = String.valueOf(omOrderLines.getOrderdQuantity());
                    String price = String.valueOf(omOrderLines.getUnitSellingPrice());
                    String amount = String.valueOf(omOrderLines.getOrderdQuantity() * omOrderLines.getUnitSellingPrice());
                    if (j == 0) {
                        newRow.createCell(0).setCellValue(orderNumber);
                        newRow.createCell(1).setCellValue(companyName);
                        newRow.createCell(2).setCellValue(customerName);
                        newRow.createCell(3).setCellValue(orderDate);
                        newRow.createCell(4).setCellValue(orderStatus);
                        newRow.createCell(5).setCellValue(itemNum);
                        newRow.createCell(6).setCellValue(itemDescription);
                        newRow.createCell(7).setCellValue(orderdQuantity);
                        newRow.createCell(8).setCellValue(price);
                        newRow.createCell(9).setCellValue(amount);
                        rowNum++;
                    }else {
                        newRow = sheet.createRow(rowNum);
                        newRow.createCell(5).setCellValue(itemNum);
                        newRow.createCell(6).setCellValue(itemDescription);
                        newRow.createCell(7).setCellValue(orderdQuantity);
                        newRow.createCell(8).setCellValue(price);
                        newRow.createCell(9).setCellValue(amount);
                        rowNum++;
                    }
                }
            }else{
                newRow.createCell(0).setCellValue(orderNumber);
                newRow.createCell(1).setCellValue(companyName);
                newRow.createCell(2).setCellValue(customerName);
                newRow.createCell(3).setCellValue(orderDate);
                newRow.createCell(4).setCellValue(orderStatus);
                rowNum++;
            }
        }




        //输出Excel文件
        OutputStream output = response.getOutputStream();
        response.reset();
        response.setHeader("Content-disposition", "attachment; filename=details.xls");
        response.setContentType("application/xls");
        wb.write(output);
        output.close();
    }
}
