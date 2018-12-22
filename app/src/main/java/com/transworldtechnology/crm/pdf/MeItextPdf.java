package com.transworldtechnology.crm.pdf;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by melayer on 17/3/16.
 */
public class MeItextPdf implements MePdfCreatable {
    private BaseFont bfBold;
    private OutputStream output = null;
    private File myFileLedger, myFileInvoice;
    private Context context;
    private String concatenateText;
    private String[] setLabelsLedger = {"#", "Tr. Date", "Tr. ID", "Debit", "Credit", "Balance(o/s)", "Desc.", "Desc.ID", "Sign"};
    private String[] setLabelsInvoice = {"#", "Inv No.", "Inv Dt", "Total", "Gr Total", "Currency", "Group", "Status", "Company Name", "Sign"};
    private float[] columnWidthsLedger = {300f, 650f, 650f, 400f, 400f, 400f, 440f, 400f, 650f};
  //  private float[] columnWidthsInvoice = {300f, 1300f, 1200f, 750f, 750f, 800f, 1000f, 700f, 1000f, 850f};
  private float[] columnWidthsInvoice = {300f, 850f, 850f, 750f, 750f, 800f, 900f, 700f, 900f, 850f};
    private Document document;

    public MeItextPdf(String concatenateText) {
        this.concatenateText = concatenateText;
        Log.i("@Transworld", "concatenateText constructor " + concatenateText);
    }

    @Override
    public File initConfigLedger(List<Map<String, Object>> mapLedgerAccountDetails) {
        Double creditAmount, debitAmount, balance = 0.0, addOfCredit = 0.0, addOfDebit = 0.0, addOfBalance = 0.0;
        String mytime = java.text.DateFormat.getTimeInstance().format(Calendar.getInstance().getTime());
        Log.i("@Transworld", " mytime+_Ledger.pdf -" + mytime);
        String separate[] = concatenateText.split("\n");
        Log.i("@Transworld", " separate[1]+_Ledger.pdf" + separate[1]);
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                myFileLedger = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), separate[1] + mytime + "_Ledger.pdf");
                output = new FileOutputStream(myFileLedger);
            } else {
                myFileLedger = new File(context.getExternalFilesDir(Environment.DIRECTORY_DCIM), separate[1] + mytime + "_Ledger.pdf");
                output = new FileOutputStream(myFileLedger);
            }
            document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, output);
            initializeFonts();
            document.open();
            Paragraph paragraphTable1 = new Paragraph();
            paragraphTable1.add("\n");
            paragraphTable1.setSpacingAfter(50);
            document.add(paragraphTable1);
            document.add(Chunk.NEWLINE);
            document.add(Chunk.NEWLINE);
            PdfContentByte cb = writer.getDirectContent();
            cb.beginText();
            String ledger = separate[0];
            createHeadings(cb, 180, 740, ledger);
            cb.setFontAndSize(bfBold, 20);
            cb.showText(ledger);
            String companyName = separate[1];
            createHeadings(cb, 180, 720, companyName);
            cb.setFontAndSize(bfBold, 16);
            cb.showText(companyName);
            createHeadings(cb, 130, 700, separate[2]);
            cb.setFontAndSize(bfBold, 16);
            cb.showText(separate[2]);
            //create PDF table with the given widths
            cb.endText();
            document.add(Chunk.NEWLINE);
            PdfPTable table = new PdfPTable(columnWidthsLedger);
            // set table width a percentage of the page width
            table.setWidths(columnWidthsLedger);
            table.setWidthPercentage(100);
            Font font = new Font(Font.getFamily("TIMES_ROMAN"), 12, Font.BOLD);
            PdfPCell cell = new PdfPCell(new Phrase(setLabelsLedger[0], font));
            cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(setLabelsLedger[1], font));
            cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(setLabelsLedger[2], font));
            cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(setLabelsLedger[3], font));
            cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(setLabelsLedger[4], font));
            cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(setLabelsLedger[5], font));
            cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(setLabelsLedger[6], font));
            cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(setLabelsLedger[7], font));
            cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(setLabelsLedger[8], font));
            cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
            int i = 1;
            if (mapLedgerAccountDetails != null) {
                for (Map<String, Object> map : mapLedgerAccountDetails) {
                    // for (Integer i = 1; i <= mapLedgerAccountDetails.size(); i++) {
                    // Map<String, Object> map = mapLedgerAccountDetails.get(i);
                    cell = new PdfPCell(new Phrase("" + i));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                    String DateValue = "" + map.get("transactionDate");
                    cell = new PdfPCell(new Phrase("" + map.get("transactionDate")));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                    Log.i("@Transworld", "textTrDateValue in generatePDF()-" + DateValue);
                    Log.i("@Transwold", "value of i - " + i);
                    if (i == 1) {
                        cell = new PdfPCell(new Phrase("opening balance"));
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table.addCell(cell);
                    } else {
                        cell = new PdfPCell(new Phrase("" + map.get("transactionId")));
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table.addCell(cell);
                    }
                    debitAmount = (Double) map.get("debitAmount");
                    if (debitAmount != null) {
                        addOfDebit = addOfDebit + debitAmount;
                    }
                    cell = new PdfPCell(new Phrase(String.format("%.2f",map.get("debitAmount"))));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                    creditAmount = (Double) map.get("creditAmount");
                    if (creditAmount != null) {
                        addOfCredit = addOfCredit + creditAmount;
                    }
                    cell = new PdfPCell(new Phrase(String.format("%.2f", map.get("creditAmount"))));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                    balance = (Double) map.get("balance");
                    if (balance != null) {
                        addOfBalance = addOfBalance + balance;
                    }
                    Log.i("@Transworld", "addOfBalance -:" + addOfBalance);
                    cell = new PdfPCell(new Phrase(String.format("%.2f", map.get("balance"))));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("" + map.get("description")));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(""+ map.get("descriptionId")));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);
                    String sign = "" + map.get("sign");
                    cell = new PdfPCell(new Phrase("" + map.get("sign")));
                    Log.i("@Transworld", "textSignValue -" + sign);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);
                    i++;
                }
                cell = new PdfPCell(new Phrase("Total", font));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("-"));
                cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("-"));
                cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(String.format("%.2f", +addOfDebit)));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(String.format("%.2f", addOfCredit)));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase(String.format("%.2f", addOfBalance)));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("-"));
                cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("-"));
                cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell);
                cell = new PdfPCell(new Phrase("-"));
                cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell);
            }
            document.add(table);
            document.close();
            writer.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return myFileLedger;
    }

    @Override
    public File initConfigInvoice(List<Map<String, Object>> listInvoiceMap) {
        String mytime = java.text.DateFormat.getTimeInstance().format(Calendar.getInstance().getTime());
        Log.i("@Transworld", " mytime+_Ledger.pdf" + mytime);
        String separate[] = concatenateText.split("\n");
        Log.i("@Transworld", "separate[1] b4 name" + separate[1]);
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                myFileInvoice = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), separate[1] + mytime + "_Invoice.pdf");
                output = new FileOutputStream(myFileInvoice);
            } else {
                myFileInvoice = new File(context.getExternalFilesDir(Environment.DIRECTORY_DCIM), separate[1] + mytime + "_Invoice.pdf");
                output = new FileOutputStream(myFileInvoice);
            }
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, output);
            initializeFonts();
            document.open();
            Paragraph paragraphTable1 = new Paragraph();
            paragraphTable1.add("\n");
            paragraphTable1.setSpacingAfter(50);
            document.add(paragraphTable1);
            document.add(Chunk.NEWLINE);
            PdfContentByte cb = writer.getDirectContent();
            cb.beginText();
            Log.i("@Transworld", "concatenateText in init config" + concatenateText);
            Log.i("@Transworld", "separate[0]" + separate[0]);
            Log.i("@Transworld", "separate[1]" + separate[1]);
            createHeadings(cb, 180, 740, separate[0]);
            cb.setFontAndSize(bfBold, 20);
            cb.showText(separate[0]);
            createHeadings(cb, 180, 720, separate[1]);
            cb.setFontAndSize(bfBold, 16);
            cb.showText(separate[1]);
            Log.i("@Transworld", "separate[0]" + separate[0]);
            Log.i("@Transworld", "separate[1]" + separate[1]);
            cb.endText();
            //create PDF table with the given widths
            document.add(Chunk.NEWLINE);
            PdfPTable table = new PdfPTable(columnWidthsInvoice);
            table.setWidths(columnWidthsInvoice);
            table.setWidthPercentage(100);
            Font font = new Font(Font.getFamily("TIMES_ROMAN"), 10, Font.BOLD);
            PdfPCell cell = new PdfPCell(new Phrase(setLabelsInvoice[0], font));
            cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(setLabelsInvoice[1], font));
            cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(setLabelsInvoice[2], font));
            cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(setLabelsInvoice[3], font));
            cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(setLabelsInvoice[4], font));
            cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(setLabelsInvoice[5], font));
            cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(setLabelsInvoice[6], font));
            cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(setLabelsInvoice[7], font));
            cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(setLabelsInvoice[8], font));
            cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(setLabelsInvoice[9], font));
            cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
            int i = 1;
            if (listInvoiceMap != null) {
                for (Map<String, Object> map : listInvoiceMap) {
                    cell = new PdfPCell(new Phrase("" + i));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("" + map.get("invoiceRefNo")));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("" + map.get("invoiceDate")));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(String.format("%.2f", map.get("total"))));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase(String.format("%.2f",map.get("grandTotal"))));
                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("" + map.get("currType")));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("" + map.get("theGroup")));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("" + map.get("status")));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("" + map.get("companyName")));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);
                    cell = new PdfPCell(new Phrase("" + map.get("TWEmpName")));
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);
                    i++;
                }
            }
            document.add(table);
            document.close();
            writer.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return myFileInvoice;
    }

    private void initializeFonts() {
        try {
            bfBold = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createHeadings(PdfContentByte cb, float x, float y, String text) throws DocumentException {
        //cb.beginText();
        //cb.setFontAndSize(bfBold, 20);
        cb.setTextMatrix(x, y);
        //cb.showText(text);
//        document.add(Chunk.NEWLINE);
        //cb.endText();
    }
}
