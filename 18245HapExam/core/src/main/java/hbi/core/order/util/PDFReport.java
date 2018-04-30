package hbi.core.order.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;


/**
 *PDF报表
 */
public class PDFReport {

    public static BaseFont bfChinese;
    private  Document document = new Document();// 建立一个Document对象
    public static Font headfont;// 设置字体大小
    public static Font keyfont;// 设置字体大小
    public static Font textfont;// 设置字体大小
    public static Font topicfont;//设置字体大小



    static {//初始化字体
        BaseFont bfChinese;
        try {
            // bfChinese =
            // BaseFont.createFont("STSong-Light","UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
            bfChinese = BaseFont.createFont("C:/WINDOWS/Fonts/simsunb.ttf", BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);
            topicfont = new Font(bfChinese, 20, Font.BOLD);// 设置字体大小
            headfont = new Font(bfChinese, 15, Font.BOLD);// 设置字体大小
            keyfont = new Font(bfChinese, 10, Font.BOLD);// 设置字体大小
            textfont = new Font(bfChinese, 10, Font.NORMAL);// 设置字体大小
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    int maxWidth = 520;
    //创建单元

    public PdfPCell createCell(String value, Font font,
                               int align) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setPhrase(new Phrase(value, font));
        return cell;
    }

    public PdfPCell createCell(String value, Font font) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPhrase(new Phrase(value, font));
        return cell;
    }

    public PdfPCell createCell(String value, Font font,
                               int align, int colspan) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setColspan(colspan);

        cell.setPhrase(new Phrase(value, font));
        return cell;
    }

    public PdfPCell createCell(String value,Font font,
                               int align, int colspan, boolean boderFlag) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setColspan(colspan);
        cell.setPhrase(new Phrase(value, font));
        cell.setPadding(3.0f);

        if (!boderFlag) {
            cell.setBorder(0);
            cell.setPaddingTop(5.0f);
            cell.setPaddingBottom(5.0f);
        }
        return cell;
    }

    //列数
    public PdfPTable createTable(int colNumber) {
        PdfPTable table = new PdfPTable(colNumber);
        try {
            table.setTotalWidth(maxWidth);
            table.setLockedWidth(true);
            table.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.getDefaultCell().setBorder(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return table;
    }
    //表宽度

    public PdfPTable createTable(float[] widths) {
        PdfPTable table = new PdfPTable(widths);
        try {
            table.setTotalWidth(maxWidth);
            table.setLockedWidth(true);
            table.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.getDefaultCell().setBorder(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return table;
    }
    // 添加空行

    public PdfPTable createBlankTable() {
        PdfPTable table = new PdfPTable(1);
        table.getDefaultCell().setBorder(0);
        table.addCell(createCell("", keyfont));
        table.setSpacingAfter(20.0f);
        table.setSpacingBefore(20.0f);
        return table;
    }

}
