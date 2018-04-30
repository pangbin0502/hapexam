package hbi.core.order.service.impl;

import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import hbi.core.order.dto.*;
import hbi.core.order.mapper.*;
import hbi.core.order.util.ExportExcel;
import hbi.core.order.util.PDFReport;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import hbi.core.order.service.IOmOrderHeadersService;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class OmOrderHeadersServiceImpl extends BaseServiceImpl<OmOrderHeaders> implements IOmOrderHeadersService{

    @Autowired
    OmOrderHeadersMapper omOrderHeadersMapper;
    @Autowired
    OmOrderLinesMapper omOrderLinesMapper;
    @Autowired
    ArCustomersMapper arCustomersMapper;
    @Autowired
    IOmOrderHeadersService service;
    @Autowired
    InvInventoryItemsMapper invInventoryItemsMapper;
    @Autowired
    OrgCompanysMapper orgCompanysMapper;

    @Override
    public void printPDF(Long headerId,HttpServletResponse response) {
        OmOrderHeaders headers = new OmOrderHeaders();
        headers.setHeaderId(headerId);
        OmOrderLines lines = new OmOrderLines();
        lines.setHeaderId(headerId);

        headers = omOrderHeadersMapper.selectByPrimaryKey(headers);

        OrgCompanys companys = new OrgCompanys();
        InvInventoryItems items = new InvInventoryItems();
        ArCustomers customers = new ArCustomers();

        companys.setCompanyId(headers.getCompanyId());
        companys = orgCompanysMapper.selectByPrimaryKey(companys);

        customers.setCustomerId(headers.getCustomerId());
        customers = arCustomersMapper.selectByPrimaryKey(customers);


        List<OmOrderLines> linesList = omOrderLinesMapper.select(lines);

        long amount = 0;
        for (int i = 0; i < linesList.size(); i++) {
            amount = amount + linesList.get(i).getOrderdQuantity() * linesList.get(i).getUnitSellingPrice();
        }

        HashMap<String, String> stauts = new HashMap<>();
        stauts.put("SUBMITED", "已提交");
        stauts.put("APPROVED", "已审批");
        stauts.put("REJECTED", "已拒绝");
        stauts.put("NEW", "新建");


        Document pdfDocument = new Document();
        String orderNumber = headers.getOrderNumber();
        String companyName = companys.getCompanyName();
        String customerName = customers.getCustomerName();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String orderDate = formatter.format(headers.getOrderDate());

        String orderStatus = stauts.get(headers.getOrderStatus());
        String orderAmount = String.valueOf(amount);

        try {
            //构建一个PDF文档输出流程
            //OutputStream pdfFileOutputStream = new FileOutputStream(new File("test.pdf"));
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PdfWriter.getInstance(pdfDocument, bos);
            //设置中文字体和字体样式
            BaseFont bfChinese = BaseFont.createFont("E:/Program Files/SIMYOU.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            Font f8 = new Font(bfChinese, 8, Font.NORMAL);
            Font f16 = new Font(bfChinese, 16, Font.NORMAL);

            //打开PDF文件流
            pdfDocument.open();

            //创建一个8列的表格控件
            PdfPTable pdfTabletop = new PdfPTable(9);
            PdfPTable pdfTablebase = new PdfPTable(6);

            //设置表格占PDF文档95%宽度
            pdfTabletop.setWidthPercentage(100);
            pdfTablebase.setWidthPercentage(100);
            //水平方向表格控件左对齐
            pdfTabletop.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
            //创建一个表格的表头单元格
            PdfPCell pdfTableHeaderCell = new PdfPCell();

            pdfTableHeaderCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);

            //创建一个表格的正文内容单元格
            PdfPCell pdfTableContentCell = new PdfPCell();
            pdfTableContentCell.setBorder(0);
            pdfTableContentCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            pdfTableContentCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);

            for (int i = 0; i < 9; i++) {
                if (i == 0) {
                    pdfTableContentCell.setPhrase(new Paragraph("·PDF打印", f16));
                    pdfTabletop.addCell(pdfTableContentCell);
                } else {
                    pdfTableContentCell.setPhrase(new Paragraph("", f16));
                    pdfTabletop.addCell(pdfTableContentCell);
                }
            }

            //添加标题列 f8中文字体
            pdfTableContentCell.setPhrase(new Paragraph("订单编号", f8));
            pdfTabletop.addCell(pdfTableContentCell);

            pdfTableContentCell.setPhrase(new Paragraph(orderNumber, f8));
            pdfTabletop.addCell(pdfTableContentCell);
            pdfTableContentCell.setPhrase(new Paragraph("", f8));
            pdfTabletop.addCell(pdfTableContentCell);

            pdfTableContentCell.setPhrase(new Paragraph("公司名称", f8));
            pdfTabletop.addCell(pdfTableContentCell);

            pdfTableContentCell.setPhrase(new Paragraph(companyName, f8));
            pdfTabletop.addCell(pdfTableContentCell);

            pdfTableContentCell.setPhrase(new Paragraph("", f8));
            pdfTabletop.addCell(pdfTableContentCell);
            pdfTableContentCell.setPhrase(new Paragraph("客户名称", f8));
            pdfTabletop.addCell(pdfTableContentCell);

            pdfTableContentCell.setPhrase(new Paragraph(customerName, f8));
            pdfTabletop.addCell(pdfTableContentCell);

            pdfTableContentCell.setPhrase(new Paragraph("", f8));
            pdfTabletop.addCell(pdfTableContentCell);
            pdfTableContentCell.setPhrase(new Paragraph("订单日期", f8));
            pdfTabletop.addCell(pdfTableContentCell);
            pdfTableContentCell.setPhrase(new Paragraph(orderDate, f8));
            pdfTabletop.addCell(pdfTableContentCell);

            pdfTableContentCell.setPhrase(new Paragraph("", f8));
            pdfTabletop.addCell(pdfTableContentCell);
            pdfTableContentCell.setPhrase(new Paragraph("订单总金额", f8));
            pdfTabletop.addCell(pdfTableContentCell);

            pdfTableContentCell.setPhrase(new Paragraph(orderAmount, f8));
            pdfTabletop.addCell(pdfTableContentCell);

            pdfTableContentCell.setPhrase(new Paragraph("", f8));
            pdfTabletop.addCell(pdfTableContentCell);
            pdfTableContentCell.setPhrase(new Paragraph("订单状态", f8));
            pdfTabletop.addCell(pdfTableContentCell);

            pdfTableContentCell.setPhrase(new Paragraph(orderStatus, f8));
            pdfTabletop.addCell(pdfTableContentCell);

            pdfTableContentCell.setPhrase(new Paragraph("", f8));
            pdfTabletop.addCell(pdfTableContentCell);

            for (int i = 0; i < 6; i++) {
                pdfTableContentCell.setPhrase(new Paragraph("", f16));
                pdfTablebase.addCell(pdfTableContentCell);
            }
            for (int i = 0; i < 6; i++) {
                pdfTableContentCell.setPhrase(new Paragraph("", f16));
                pdfTablebase.addCell(pdfTableContentCell);
            }



            for (int i = 0; i < 6; i++) {
                if (i == 0) {
                    pdfTableContentCell.setPhrase(new Paragraph("主要", f16));
                    pdfTablebase.addCell(pdfTableContentCell);
                } else {
                    pdfTableContentCell.setPhrase(new Paragraph("", f16));
                    pdfTablebase.addCell(pdfTableContentCell);
                }
            }
            pdfTableContentCell.setBorder(1);
            pdfTableContentCell.setPhrase(new Paragraph("物料编码", f8));
            pdfTablebase.addCell(pdfTableContentCell);
            pdfTableContentCell.setPhrase(new Paragraph("物料描述", f8));
            pdfTablebase.addCell(pdfTableContentCell);
            pdfTableContentCell.setPhrase(new Paragraph("产品单位", f8));
            pdfTablebase.addCell(pdfTableContentCell);
            pdfTableContentCell.setPhrase(new Paragraph("数量", f8));
            pdfTablebase.addCell(pdfTableContentCell);
            pdfTableContentCell.setPhrase(new Paragraph("销售单价", f8));
            pdfTablebase.addCell(pdfTableContentCell);
            pdfTableContentCell.setPhrase(new Paragraph("金额", f8));
            pdfTablebase.addCell(pdfTableContentCell);

            //表格内容行数的填充
            for (int i = 0; i < linesList.size(); i++) {

                lines = linesList.get(i);

                items.setInventoryItemId(lines.getInventoryItemId());
                items = invInventoryItemsMapper.selectByPrimaryKey(items);

                //获取每一个string字段
                String itemNum = items.getItemCode();
                String itemName = items.getItemDescription();
                String quantityUom = lines.getOrderQuantityUom();
                String quantity = String.valueOf(lines.getOrderdQuantity());
                String price = String.valueOf(lines.getUnitSellingPrice());
                String amountp = String.valueOf(lines.getOrderdQuantity() * lines.getUnitSellingPrice());

                //字段插入
                pdfTableContentCell.setPhrase(new Paragraph(itemNum));
                pdfTablebase.addCell(pdfTableContentCell);
                pdfTableContentCell.setPhrase(new Paragraph(itemName, f8));
                pdfTablebase.addCell(pdfTableContentCell);
                pdfTableContentCell.setPhrase(new Paragraph(quantityUom, f8));
                pdfTablebase.addCell(pdfTableContentCell);
                pdfTableContentCell.setPhrase(new Paragraph(quantity));
                pdfTablebase.addCell(pdfTableContentCell);
                pdfTableContentCell.setPhrase(new Paragraph(price));
                pdfTablebase.addCell(pdfTableContentCell);
                pdfTableContentCell.setPhrase(new Paragraph(amountp));
                pdfTablebase.addCell(pdfTableContentCell);
            }
            //把表格添加到pdf文件中
            pdfDocument.add(pdfTabletop);
            pdfDocument.add(pdfTablebase);
            //关闭文件
            pdfDocument.close();

            //响应设置文件名 文件名要加上后缀
            response.setHeader("Content-Disposition", "attachment; filename=test.pdf");
            //文件类型
            response.setContentType("application/pdf");
            response.setContentLength(bos.size());
            //定义输出流
            OutputStream out1 = response.getOutputStream();
            //按字节流下载
            bos.writeTo(out1);
            out1.flush();

        } catch (DocumentException de) {
            de.printStackTrace();
            System.err.println("document: " + de.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //关闭PDF文档流，OutputStream文件输出流也将在PDF文档流关闭方法内部关闭
            if (pdfDocument != null) {
                pdfDocument.close();
            }
        }

    }

    @Override
    public void deleteById(List<OmOrderHeaders> omOrderHeaders) {
        Iterator iterator = omOrderHeaders.iterator();

        OmOrderLines omOrderLines=new OmOrderLines();
        OmOrderHeaders orderHeader=new OmOrderHeaders();
        while (iterator.hasNext()) {
            orderHeader = (OmOrderHeaders) iterator.next();
            omOrderHeadersMapper.deleteByPrimaryKey(orderHeader);
            omOrderLines.setHeaderId(orderHeader.getHeaderId());
            omOrderLinesMapper.deleteByHeaderId(omOrderLines);
            //omOrderLinesMapper.deleteByPrimaryKey(omOrderLines);
        }

    }

    @Override
    public void exportExcel(IRequest requestContext, OmOrderHeaders dto, int page, int pageSize,HttpServletResponse response) {
        ExportExcel exportExcel =new ExportExcel();
        List<OmOrderHeaders> list = service.select(requestContext, dto, page, pageSize);
        for (int i = 0; i < list.size() ; i++) {
            OmOrderLines omOrderLines = new OmOrderLines();
            omOrderLines.setHeaderId(list.get(i).getHeaderId());
            List<OmOrderLines> omOrderLiness = omOrderLinesMapper.select(omOrderLines);
            list.get(i).setOmOrderLiness(omOrderLiness);
        }
        List<OmOrderHeaders> omOrderHeaderss = list;
        try {
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
                        items = invInventoryItemsMapper.selectByPrimaryKey(items);
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
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public List<OmOrderHeaders> select2(IRequest request, OmOrderHeaders omOrderHeaders, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<OmOrderHeaders> list=omOrderHeadersMapper.select(omOrderHeaders);
        String customerName,companyName;

        for(OmOrderHeaders k:list){
            customerName=arCustomersMapper.selectCustomerName(k.getCustomerId()).getCustomerName();
            companyName=orgCompanysMapper.selectCompanyName(k.getCompanyId()).getCompanyName();
            k.setCompanyName(companyName);
            k.setCustomerName(customerName);
        }
        return list;
    }

}