package com.aimacademyla.api.excel.impl;

import com.aimacademyla.api.excel.AbstractExcelWriter;
import com.aimacademyla.model.Charge;
import com.aimacademyla.model.ChargeLine;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.dto.MemberChargesFinancesDTO;
import com.aimacademyla.model.dto.PeriodSummaryDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MemberInvoiceExcelWriter extends AbstractExcelWriter {

    public static final String TEMPLATE_FILE_NAME = "Invoice_Template.xlsx";

    private PeriodSummaryDTO.MemberSummary memberSummary;

    public MemberInvoiceExcelWriter(Workbook workbook){
        super(workbook);
    }

    @Override
    public Workbook writeToWorkbook() {
        Sheet invoiceSheet = workbook.getSheetAt(0);
        Member member = memberSummary.getMember();
        List<Charge> chargeList = new ArrayList<>(memberSummary.getChargeList());

        LocalDate cycleStartDate = memberSummary.getCyclePeriod().getCycleStartDate();
        LocalDate cycleEndDate = memberSummary.getCyclePeriod().getCycleEndDate();
        String dateString = cycleStartDate.getMonth().toString().substring(0,1) + cycleStartDate.getMonth().toString().substring(1).toLowerCase()
                + " " + cycleStartDate.getDayOfMonth() + ", " + cycleStartDate.getYear() + " - " + cycleEndDate.getMonth().toString().substring(0,1) + cycleEndDate.getMonth().toString().substring(1).toLowerCase()
                + " " + cycleEndDate.getDayOfMonth() + ", " + cycleEndDate.getYear();

        String invoiceNumberString = Integer.toString(LocalDate.now().getMonthValue()) + Integer.toString(LocalDate.now().getDayOfMonth()) + Integer.toString(LocalDate.now().getYear());

        Cell recipientCell = invoiceSheet.getRow(ExcelRowColumnWrapper.RECIPIENT_CELL.getRow()).getCell(ExcelRowColumnWrapper.RECIPIENT_CELL.getColumn());
        Cell dateCell = invoiceSheet.getRow(ExcelRowColumnWrapper.DATE_CELL.getRow()).getCell(ExcelRowColumnWrapper.DATE_CELL.getColumn());
        Cell studentIDCell = invoiceSheet.getRow(ExcelRowColumnWrapper.STUDENT_ID_CELL.getRow()).getCell(ExcelRowColumnWrapper.STUDENT_ID_CELL.getColumn());
        Cell invoiceNumberCell = invoiceSheet.getRow(ExcelRowColumnWrapper.INVOICE_NUMBER_CELL.getRow()).getCell(ExcelRowColumnWrapper.INVOICE_NUMBER_CELL.getColumn());

        recipientCell.setCellValue(member.getMemberFirstName() + " " + member.getMemberLastName());
        dateCell.setCellValue(dateString);
        studentIDCell.setCellValue(member.getMemberID());
        invoiceNumberCell.setCellValue(invoiceNumberString);

        BigDecimal totalBalance = BigDecimal.ZERO;

        for(int count = 0; count < chargeList.size(); count++){
            Charge charge = chargeList.get(count);

            String chargeDescription = generateChargeDescription(charge);

            BigDecimal chargeAmount = charge.getChargeAmount();
            BigDecimal discountAmount = charge.getDiscountAmount();
            BigDecimal hoursBilled = charge.getBillableUnitsBilled();
            BigDecimal balanceAmount = chargeAmount.subtract(discountAmount);
            totalBalance = totalBalance.add(balanceAmount);

            Cell descriptionCell = invoiceSheet.getRow(ExcelRowColumnWrapper.DESCRIPTION_CELL_START.getRow() + count).getCell(ExcelRowColumnWrapper.DESCRIPTION_CELL_START.getColumn());
            Cell quantityCell = invoiceSheet.getRow(ExcelRowColumnWrapper.QUANTITY_CELL_START.getRow() + count).getCell(ExcelRowColumnWrapper.QUANTITY_CELL_START.getColumn());
            Cell priceCell = invoiceSheet.getRow(ExcelRowColumnWrapper.PRICE_CELL_START.getRow() + count).getCell(ExcelRowColumnWrapper.PRICE_CELL_START.getColumn());
            Cell discountCell = invoiceSheet.getRow(ExcelRowColumnWrapper.DISCOUNT_CELL_START.getRow() + count).getCell(ExcelRowColumnWrapper.DISCOUNT_CELL_START.getColumn());
            Cell subTotalCell = invoiceSheet.getRow(ExcelRowColumnWrapper.SUBTOTAL_CELL_START.getRow() + count).getCell(ExcelRowColumnWrapper.SUBTOTAL_CELL_START.getColumn());

            descriptionCell.setCellValue(chargeDescription);
            quantityCell.setCellValue(hoursBilled.toString());
            priceCell.setCellValue(chargeAmount.toString());
            discountCell.setCellValue(discountAmount.toString());
            subTotalCell.setCellValue(balanceAmount.toString());

            CellUtil.setAlignment(descriptionCell, HorizontalAlignment.CENTER);
            CellUtil.setAlignment(quantityCell, HorizontalAlignment.CENTER);
            CellUtil.setAlignment(priceCell, HorizontalAlignment.CENTER);
            CellUtil.setAlignment(discountCell, HorizontalAlignment.CENTER);
            CellUtil.setAlignment(subTotalCell, HorizontalAlignment.CENTER);
        }

        Cell totalCell = invoiceSheet.getRow(ExcelRowColumnWrapper.TOTAL_CELL.getRow()).getCell(ExcelRowColumnWrapper.TOTAL_CELL.getColumn());
        totalCell.setCellValue(totalBalance.toString());
        CellUtil.setAlignment(totalCell, HorizontalAlignment.CENTER);

        return workbook;
    }

    private String generateChargeDescription(Charge charge){
        StringBuilder stringBuilder = new StringBuilder(charge.getDescription() + " (");
        List<ChargeLine> chargeLineList = new ArrayList<>(charge.getChargeLineSet());

        for(int i = 0; i < chargeLineList.size(); i++){
            ChargeLine chargeLine = chargeLineList.get(i);
            stringBuilder.append(chargeLine.getDateCharged().getMonthValue() + "/" + chargeLine.getDateCharged().getDayOfMonth() + "/" + chargeLine.getDateCharged().getYear());
            if(i < chargeLineList.size() - 1)
                stringBuilder.append(", ");
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    public MemberInvoiceExcelWriter setMemberSummary(PeriodSummaryDTO.MemberSummary memberSummary){
        this.memberSummary = memberSummary;
        return this;
    }

    private enum ExcelRowColumnWrapper{
        /*
         * NOTE: Excel cell index starts from 0
         */
        DESCRIPTION_CELL_START(19,0),
        QUANTITY_CELL_START(19,3),
        PRICE_CELL_START(19,4),
        DISCOUNT_CELL_START(19,5),
        SUBTOTAL_CELL_START(19,6),
        RECIPIENT_CELL(9,1),
        INVOICE_NUMBER_CELL(4,6),
        DATE_CELL(5,6),
        STUDENT_ID_CELL(6,6),
        TOTAL_CELL(39,6);

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
