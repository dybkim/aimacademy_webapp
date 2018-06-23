package com.aimacademyla.api.excel.impl;

import com.aimacademyla.api.excel.AbstractExcelWriter;
import com.aimacademyla.api.excel.calculator.AttendanceStatisticsCalculator;
import com.aimacademyla.api.excel.format.SheetFormat;
import com.aimacademyla.api.excel.format.impl.DefaultPeriodReportSheetFormat;
import com.aimacademyla.api.excel.format.impl.DefaultSummaryReportSheetFormat;
import com.aimacademyla.model.Charge;
import com.aimacademyla.model.ChargeLine;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.dto.OutstandingChargesPaymentDTO;
import com.aimacademyla.model.dto.PeriodSummaryDTO;
import com.aimacademyla.model.temporal.CyclePeriod;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PeriodSummaryExcelWriter extends AbstractExcelWriter{

    public static final String TEMPLATE_FILE_NAME = "Summaries_Template.xlsx";

    private SheetFormat defaultPeriodReportSheetFormat;
    private SheetFormat defaultSummaryReportSheetFormat;

    public PeriodSummaryExcelWriter(Workbook workbook){
        super(workbook);
        Sheet summarySheet = workbook.getSheetAt(0);
        Sheet reportSheet = workbook.getSheetAt(1);

        defaultSummaryReportSheetFormat = new DefaultSummaryReportSheetFormat(summarySheet);
        defaultPeriodReportSheetFormat = new DefaultPeriodReportSheetFormat(reportSheet);
    }

    @Override
    public void writeToSheets(){
        defaultSummaryReportSheetFormat.formatOutput();
        defaultPeriodReportSheetFormat.formatOutput();
    }

    @Override
    public void setPeriodSummaryDTO(PeriodSummaryDTO periodSummaryDTO){
        this.periodSummaryDTO = periodSummaryDTO;

        defaultSummaryReportSheetFormat.setPeriodSummaryDTO(periodSummaryDTO);
        defaultPeriodReportSheetFormat.setPeriodSummaryDTO(periodSummaryDTO);
    }

}
