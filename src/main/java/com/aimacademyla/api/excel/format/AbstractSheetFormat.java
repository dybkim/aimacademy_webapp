package com.aimacademyla.api.excel.format;

import com.aimacademyla.api.excel.impl.PeriodSummaryExcelWriter;
import com.aimacademyla.model.dto.PeriodSummaryDTO;
import com.aimacademyla.model.temporal.CyclePeriod;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.time.LocalDate;

public abstract class AbstractSheetFormat implements SheetFormat{

    protected PeriodSummaryDTO periodSummaryDTO;
    protected XSSFCellStyle borderStyle;
    protected String cyclePeriodString;
    protected Sheet outputSheet;

    public AbstractSheetFormat(Sheet outputSheet){
        this.outputSheet = outputSheet;
        borderStyle = ((XSSFWorkbook) outputSheet.getWorkbook()).createCellStyle();
        borderStyle.setBorderRight(BorderStyle.THIN);
        borderStyle.setBorderLeft(BorderStyle.THIN);
        borderStyle.setBorderTop(BorderStyle.THIN);
        borderStyle.setBorderBottom(BorderStyle.THIN);
        borderStyle.setAlignment(HorizontalAlignment.CENTER);
        borderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
    }

    public void setPeriodSummaryDTO(PeriodSummaryDTO periodSummaryDTO){
        if(periodSummaryDTO == null)
            return;

        this.periodSummaryDTO = periodSummaryDTO;
        CyclePeriod cyclePeriod = periodSummaryDTO.getCyclePeriod();
        LocalDate cycleStartDate = cyclePeriod.getCycleStartDate();
        LocalDate cycleEndDate = cyclePeriod.getCycleEndDate();

        if(cycleStartDate.equals(cycleEndDate)) {
            cyclePeriodString = cycleStartDate.getMonth().toString().substring(0,1) + cycleStartDate.getMonth().toString().substring(1).toLowerCase() + " " + cycleStartDate.getYear();
            return;
        }

        cyclePeriodString = cycleStartDate.getMonth().toString().substring(0,1) + cycleStartDate.getMonth().toString().substring(1).toLowerCase()
                + " " + cycleStartDate.getDayOfMonth() + ", " + cycleStartDate.getYear() + " - " +
                cycleEndDate.getMonth().toString().substring(0,1) + cycleEndDate.getMonth().toString().substring(1).toLowerCase()
                + " " + cycleEndDate.getDayOfMonth() + ", " + cycleEndDate.getYear();
    }

    @Override
    public PeriodSummaryDTO getPeriodSummaryDTO() {
        return periodSummaryDTO;
    }

    @Override
    public String getCyclePeriodString(){return cyclePeriodString;}

    public enum ExcelRowColumnWrapper{
        //The following are references are for summarySheet
        INDEX_CELL_START(2,0),
        MEMBER_NAME_CELL_START(2,1),
        MEMBERSHIP_ATTENDANCE_CELL_START(2,2),
        COURSE_CELL_START(2,4),
        DATE_CELL_START(2,7),
        HOURS_CELL_START(2,9),
        SUBTOTAL_CELL_START(2,10),
        TOTAL_CELL_START(2,11),

        //The following are references for reportSheet
        COURSETYPE_CELL_START(4,0),
        STATISTICS_TYPE_CELL_START(5,1),
        STATISTICS_DATA_CELL_START(5,2),

        //The following are references for invoicesSheet
        DESCRIPTION_CELL_START(19,0),
        QUANTITY_CELL_START(19,3),
        PRICE_CELL_START(19,4),
        DISCOUNT_CELL_START(19,5),
        INVOICE_SUBTOTAL_CELL_START(19,6),
        RECIPIENT_CELL(9,1),
        INVOICE_NUMBER_CELL(4,6),
        DATE_CELL(5,6),
        STUDENT_ID_CELL(6,6),
        TOTAL_CELL(39,6);


        /*
         * NOTE: Excel cells are 0-indexed
         */
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
