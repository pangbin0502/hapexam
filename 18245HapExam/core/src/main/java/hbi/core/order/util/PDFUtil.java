/*
package hbi.core.order.util;

*/
/**
 * Created by PB on 2018/4/20.
 *//*

import java.awt.*;
import java.io.*;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import hbi.core.order.dto.OmOrderHeaders;

import javax.servlet.http.HttpServletResponse;
public class PDFUtil {
    public boolean importPDF(List<> handtests,HttpServletResponse response) throws DocumentException {
        Document pdfDocument = new Document();
        try {
            //构建一个PDF文档输出流程
            //OutputStream pdfFileOutputStream = new FileOutputStream(new File("test.pdf"));
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PdfWriter.getInstance(pdfDocument, bos);
            //设置中文字体和字体样式
            BaseFont bfChinese = BaseFont.createFont("C:/WINDOWS/Fonts/SIMYOU.TTF", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            Font f8 = new Font(bfChinese, 8, Font.NORMAL);
            //打开PDF文件流
            pdfDocument.open();
            //创建一个8列的表格控件
            PdfPTable pdfTable = new PdfPTable(5);
            //设置表格占PDF文档100%宽度
            pdfTable.setWidthPercentage(95);
            //水平方向表格控件左对齐
            pdfTable.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
            //创建一个表格的表头单元格
            PdfPCell pdfTableHeaderCell = new PdfPCell();

            pdfTableHeaderCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);

            //创建一个表格的正文内容单元格
            PdfPCell pdfTableContentCell = new PdfPCell();
            pdfTableContentCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            pdfTableContentCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);

            pdfTableContentCell.setPhrase(new Paragraph("教师序号",f8));
            pdfTable.addCell(pdfTableContentCell);
            pdfTableContentCell.setPhrase(new Paragraph("教师姓名",f8));
            pdfTable.addCell(pdfTableContentCell);
            pdfTableContentCell.setPhrase(new Paragraph("年龄",f8));
            pdfTable.addCell(pdfTableContentCell);
            pdfTableContentCell.setPhrase(new Paragraph("是否在职",f8));
            pdfTable.addCell(pdfTableContentCell);
            pdfTableContentCell.setPhrase(new Paragraph("职称",f8));
            pdfTable.addCell(pdfTableContentCell);

            //表格内容行数的填充
            for (int i = 0; i < handtests.size(); i++) {
                String hid = String.valueOf(handtests.get(i).getTid());
                String hcord = handtests.get(i).getName();
                String isok = String.valueOf(handtests.get(i).getAge());
                String stime = handtests.get(i).getStatus();
                String etime = handtests.get(i).getZhicheng();

                pdfTableContentCell.setPhrase(new Paragraph(hid));
                pdfTable.addCell(pdfTableContentCell);
                pdfTableContentCell.setPhrase(new Paragraph(hcord,f8));
                pdfTable.addCell(pdfTableContentCell);
                pdfTableContentCell.setPhrase(new Paragraph(isok));
                pdfTable.addCell(pdfTableContentCell);
                pdfTableContentCell.setPhrase(new Paragraph(stime,f8));
                pdfTable.addCell(pdfTableContentCell);
                pdfTableContentCell.setPhrase(new Paragraph(etime,f8));
                pdfTable.addCell(pdfTableContentCell);

            }
            pdfDocument.add(pdfTable);
            System.out.println("1234543456543");
            pdfDocument.close();

            response.setHeader("Content-Disposition", "attachment; filename=teacher.pdf");
            response.setContentType("application/pdf");
            response.setContentLength(bos.size());
            OutputStream out1 = response.getOutputStream();

            bos.writeTo(out1);
            out1.flush();

            return true;
        } catch (DocumentException de) {
            de.printStackTrace();
            System.err.println("document: " + de.getMessage());
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            //关闭PDF文档流，OutputStream文件输出流也将在PDF文档流关闭方法内部关闭
            if (pdfDocument != null) {
                pdfDocument.close();
            }
        }


//        OutputStream out1=response.getOutputStream();
//
//        bos.writeTo(out1);
//        out1.flush();
    }
}
*/
