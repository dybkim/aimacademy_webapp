package com.aimacademyla.api.excel.impl;

import com.aimacademyla.model.Charge;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.wrapper.MemberChargesFinancesWrapper;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ExcelInvoiceView extends AbstractXlsxView {

    @Override
    protected void buildExcelDocument(Map<String, Object> map, Workbook workbook, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        String invoiceTemplatePath = getClass().getClassLoader().getResource("Invoice_Template.xlsx").getFile();
        File invoiceTemplateFile = new File(invoiceTemplatePath);
        FileInputStream inputStream = new FileInputStream(invoiceTemplateFile);
        OPCPackage opcPackage = OPCPackage.open(inputStream);

        workbook = WorkbookFactory.create(opcPackage);
        Sheet invoiceSheet = workbook.getSheetAt(0);

        MemberChargesFinancesWrapper memberChargesFinancesWrapper = (MemberChargesFinancesWrapper) map.get("memberChargesFinancesWrapper");

        Member member = memberChargesFinancesWrapper.getMember();
        LocalDate selectedDate = memberChargesFinancesWrapper.getCycleStartDate();
        List<Charge> chargeList = new ArrayList<>(memberChargesFinancesWrapper.getChargeHashMap().values());
        HashMap<Integer, BigDecimal> hoursBilledHashMap = memberChargesFinancesWrapper.getHoursBilledHashMap();

        String selectedMonthString = selectedDate.getMonth().toString().substring(0,1) + selectedDate.getMonth().toString().substring(1).toLowerCase();
        String fileName = member.getMemberID() + "_" + selectedMonthString + selectedDate.getYear() + "_INVOICE.xlsx";

        httpServletResponse.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        httpServletResponse.setHeader("Content-Disposition", "attachment;filename=" + fileName);

        FileOutputStream outputStream = new FileOutputStream("/Users/davidkim/IdeaProjects/webportal/target/webportal/WEB-INF/classes/new.xlsx");

        String dateTodayString = LocalDate.now().getMonth().toString().substring(0,1) + LocalDate.now().getMonth().toString().substring(1).toLowerCase()
                                    + " " + LocalDate.now().getDayOfMonth() + ", " + LocalDate.now().getYear();
        String invoiceNumberString = Integer.toString(LocalDate.now().getMonthValue()) + Integer.toString(LocalDate.now().getDayOfMonth()) + Integer.toString(LocalDate.now().getYear());

        Cell recipientCell = invoiceSheet.getRow(ExcelRowColumnWrapper.RECIPIENT_CELL.getRow()).getCell(ExcelRowColumnWrapper.RECIPIENT_CELL.getColumn());
        Cell dateCell = invoiceSheet.getRow(ExcelRowColumnWrapper.DATE_CELL.getRow()).getCell(ExcelRowColumnWrapper.DATE_CELL.getColumn());
        Cell studentIDCell = invoiceSheet.getRow(ExcelRowColumnWrapper.STUDENT_ID_CELL.getRow()).getCell(ExcelRowColumnWrapper.STUDENT_ID_CELL.getColumn());
        Cell invoiceNumberCell = invoiceSheet.getRow(ExcelRowColumnWrapper.INVOICE_NUMBER_CELL.getRow()).getCell(ExcelRowColumnWrapper.INVOICE_NUMBER_CELL.getColumn());

        recipientCell.setCellValue(member.getMemberFirstName() + " " + member.getMemberLastName());
        dateCell.setCellValue(dateTodayString);
        studentIDCell.setCellValue(member.getMemberID());
        invoiceNumberCell.setCellValue(invoiceNumberString);

        for(int count = 0; count < chargeList.size(); count++){
            Charge charge = chargeList.get(count);
            String chargeDescription = charge.getDescription();
            BigDecimal chargeAmount = charge.getChargeAmount();
            BigDecimal discountAmount = charge.getDiscountAmount();
            BigDecimal hoursBilled = hoursBilledHashMap.get(charge.getChargeID());
            BigDecimal balanceAmount = chargeAmount.subtract(discountAmount);

            System.out.println(chargeDescription);

            Cell descriptionCell = invoiceSheet.getRow(ExcelRowColumnWrapper.DESCRIPTION_CELL_START.getRow() + count).getCell(ExcelRowColumnWrapper.DESCRIPTION_CELL_START.getColumn());
            Cell quantityCell = invoiceSheet.getRow(ExcelRowColumnWrapper.QUANTITY_CELL_START.getRow() + count).getCell(ExcelRowColumnWrapper.QUANTITY_CELL_START.getColumn());
            Cell priceCell = invoiceSheet.getRow(ExcelRowColumnWrapper.PRICE_CELL_START.getRow() + count).getCell(ExcelRowColumnWrapper.PRICE_CELL_START.getColumn());
            Cell discountCell = invoiceSheet.getRow(ExcelRowColumnWrapper.DISCOUNT_CELL_START.getRow() + count).getCell(ExcelRowColumnWrapper.DISCOUNT_CELL_START.getColumn());
            Cell totalCell = invoiceSheet.getRow(ExcelRowColumnWrapper.TOTAL_CELL_START.getRow() + count).getCell(ExcelRowColumnWrapper.TOTAL_CELL_START.getColumn());

            descriptionCell.setCellValue(chargeDescription);
            quantityCell.setCellValue(hoursBilled.toString());
            priceCell.setCellValue(chargeAmount.toString());
            discountCell.setCellValue(discountAmount.toString());
            totalCell.setCellValue(balanceAmount.toString());
        }

        workbook.write(outputStream);
        inputStream.close();
        outputStream.close();
        opcPackage.close();
    }

    private enum ExcelRowColumnWrapper{
        /**
         * NOTE: Excel cell index starts from 0
         */
        DESCRIPTION_CELL_START(19,0),
        QUANTITY_CELL_START(19,3),
        PRICE_CELL_START(19,4),
        DISCOUNT_CELL_START(19,5),
        TOTAL_CELL_START(19,6),
        RECIPIENT_CELL(9,1),
        INVOICE_NUMBER_CELL(4,6),
        DATE_CELL(5,6),
        STUDENT_ID_CELL(6,6);


        private int column;
        private int row;

        ExcelRowColumnWrapper(int row, int column){
            this.row = row;
            this.column = column;
        }

        public int getColumn() {
            return column;
        }

        public void setColumn(int column) {
            this.column = column;
        }

        public int getRow() {
            return row;
        }

        public void setRow(int row) {
            this.row = row;
        }
    }
}
