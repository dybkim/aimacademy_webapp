package com.aimacademyla.api.excel.format.impl;

import com.aimacademyla.api.excel.format.AbstractSheetFormat;
import com.aimacademyla.model.Charge;
import com.aimacademyla.model.ChargeLine;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.dto.PeriodSummaryDTO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DefaultMemberInvoiceSheetFormat extends AbstractSheetFormat{

    private PeriodSummaryDTO.MemberSummary memberSummary;

    public DefaultMemberInvoiceSheetFormat(Sheet outputSheet){
        super(outputSheet);
    }

    public void setMemberSummary(PeriodSummaryDTO.MemberSummary memberSummary){
        this.memberSummary = memberSummary;
    }

    @Override
    public Sheet formatOutput() {
        //Initialize Sheet
        Member member = memberSummary.getMember();
        LocalDate cycleStartDate = memberSummary.getCyclePeriod().getCycleStartDate();
        LocalDate cycleEndDate = memberSummary.getCyclePeriod().getCycleEndDate();
        String dateString = cycleStartDate.getMonth().toString().substring(0,1) + cycleStartDate.getMonth().toString().substring(1).toLowerCase()
                + " " + cycleStartDate.getDayOfMonth() + ", " + cycleStartDate.getYear() + " - " + cycleEndDate.getMonth().toString().substring(0,1) + cycleEndDate.getMonth().toString().substring(1).toLowerCase()
                + " " + cycleEndDate.getDayOfMonth() + ", " + cycleEndDate.getYear();

        String invoiceNumberString = Integer.toString(LocalDate.now().getMonthValue()) + Integer.toString(LocalDate.now().getDayOfMonth()) + Integer.toString(LocalDate.now().getYear());

        Cell recipientCell = outputSheet.getRow(AbstractSheetFormat.ExcelRowColumnWrapper.RECIPIENT_CELL.getRow()).getCell(AbstractSheetFormat.ExcelRowColumnWrapper.RECIPIENT_CELL.getColumn());
        Cell dateCell = outputSheet.getRow(AbstractSheetFormat.ExcelRowColumnWrapper.DATE_CELL.getRow()).getCell(AbstractSheetFormat.ExcelRowColumnWrapper.DATE_CELL.getColumn());
        Cell studentIDCell = outputSheet.getRow(AbstractSheetFormat.ExcelRowColumnWrapper.STUDENT_ID_CELL.getRow()).getCell(AbstractSheetFormat.ExcelRowColumnWrapper.STUDENT_ID_CELL.getColumn());
        Cell invoiceNumberCell = outputSheet.getRow(AbstractSheetFormat.ExcelRowColumnWrapper.INVOICE_NUMBER_CELL.getRow()).getCell(AbstractSheetFormat.ExcelRowColumnWrapper.INVOICE_NUMBER_CELL.getColumn());

        recipientCell.setCellValue(member.getMemberFirstName() + " " + member.getMemberLastName());
        dateCell.setCellValue(dateString);
        studentIDCell.setCellValue(member.getMemberID());
        invoiceNumberCell.setCellValue(invoiceNumberString);

        //Format and fill in sheet information
        BigDecimal totalBalance = BigDecimal.ZERO;
        List<Charge> chargeList = new ArrayList<>(memberSummary.getChargeList());

        for(int count = 0; count < chargeList.size(); count++){
            Charge charge = chargeList.get(count);

            String chargeDescription = generateChargeDescription(charge);

            BigDecimal chargeAmount = charge.getChargeAmount();
            BigDecimal discountAmount = charge.getDiscountAmount();
            BigDecimal hoursBilled = charge.getBillableUnitsBilled();
            BigDecimal balanceAmount = chargeAmount.subtract(discountAmount);
            totalBalance = totalBalance.add(balanceAmount);

            Cell descriptionCell = outputSheet.getRow(AbstractSheetFormat.ExcelRowColumnWrapper.DESCRIPTION_CELL_START.getRow() + count).getCell(AbstractSheetFormat.ExcelRowColumnWrapper.DESCRIPTION_CELL_START.getColumn());
            Cell quantityCell = outputSheet.getRow(AbstractSheetFormat.ExcelRowColumnWrapper.QUANTITY_CELL_START.getRow() + count).getCell(AbstractSheetFormat.ExcelRowColumnWrapper.QUANTITY_CELL_START.getColumn());
            Cell priceCell = outputSheet.getRow(AbstractSheetFormat.ExcelRowColumnWrapper.PRICE_CELL_START.getRow() + count).getCell(AbstractSheetFormat.ExcelRowColumnWrapper.PRICE_CELL_START.getColumn());
            Cell discountCell = outputSheet.getRow(AbstractSheetFormat.ExcelRowColumnWrapper.DISCOUNT_CELL_START.getRow() + count).getCell(AbstractSheetFormat.ExcelRowColumnWrapper.DISCOUNT_CELL_START.getColumn());
            Cell subTotalCell = outputSheet.getRow(AbstractSheetFormat.ExcelRowColumnWrapper.SUBTOTAL_CELL_START.getRow() + count).getCell(AbstractSheetFormat.ExcelRowColumnWrapper.SUBTOTAL_CELL_START.getColumn());

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

        Cell totalCell = outputSheet.getRow(AbstractSheetFormat.ExcelRowColumnWrapper.TOTAL_CELL.getRow()).getCell(AbstractSheetFormat.ExcelRowColumnWrapper.TOTAL_CELL.getColumn());
        totalCell.setCellValue(totalBalance.toString());
        CellUtil.setAlignment(totalCell, HorizontalAlignment.CENTER);

        return outputSheet;
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

}
