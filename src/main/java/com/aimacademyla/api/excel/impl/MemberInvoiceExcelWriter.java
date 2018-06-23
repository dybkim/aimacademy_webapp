package com.aimacademyla.api.excel.impl;

import com.aimacademyla.api.excel.AbstractExcelWriter;
import com.aimacademyla.api.excel.format.impl.DefaultMemberInvoiceSheetFormat;
import com.aimacademyla.model.dto.PeriodSummaryDTO;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class MemberInvoiceExcelWriter extends AbstractExcelWriter {

    public static final String TEMPLATE_FILE_NAME = "Invoice_Template.xlsx";

    private DefaultMemberInvoiceSheetFormat defaultMemberInvoiceSheetFormat;

    public MemberInvoiceExcelWriter(Workbook workbook){
        super(workbook);
        Sheet invoiceSheet = workbook.getSheetAt(0);
        defaultMemberInvoiceSheetFormat = new DefaultMemberInvoiceSheetFormat(invoiceSheet);
    }

    @Override
    public void writeToSheets(){
        defaultMemberInvoiceSheetFormat.formatOutput();
    }

    @Override
    public void setPeriodSummaryDTO(PeriodSummaryDTO periodSummaryDTO){
        this.periodSummaryDTO = periodSummaryDTO;
        defaultMemberInvoiceSheetFormat.setPeriodSummaryDTO(periodSummaryDTO);
    }

    public MemberInvoiceExcelWriter setMemberSummary(PeriodSummaryDTO.MemberSummary memberSummary){
        defaultMemberInvoiceSheetFormat.setMemberSummary(memberSummary);
        return this;
    }
}
