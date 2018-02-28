package com.aimacademyla.api.excel.impl;

import com.aimacademyla.api.excel.AbstractExcelWriter;
import com.aimacademyla.api.excel.calculator.AttendanceStatisticsCalculator;
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

public class PeriodSummaryExcelWriter extends AbstractExcelWriter {

    public static final String TEMPLATE_FILE_NAME = "Summaries_Template.xlsx";

    private PeriodSummaryDTO periodSummaryDTO;
    private Sheet summarySheet;
    private Sheet reportSheet;
    private String cyclePeriodString;
    private XSSFCellStyle borderStyle;

    public PeriodSummaryExcelWriter(Workbook workbook){
        super(workbook);
        summarySheet = workbook.getSheetAt(0);
        reportSheet = workbook.getSheetAt(1);

        borderStyle = ((XSSFWorkbook) workbook).createCellStyle();
        borderStyle.setBorderRight(BorderStyle.THIN);
        borderStyle.setBorderLeft(BorderStyle.THIN);
        borderStyle.setBorderTop(BorderStyle.THIN);
        borderStyle.setBorderBottom(BorderStyle.THIN);
        borderStyle.setAlignment(HorizontalAlignment.CENTER);
        borderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
    }

    @Override
    public Workbook writeToWorkbook() {
        writeMemberPeriodSummary();
        writeReportSummary();
        return workbook;
    }

    public PeriodSummaryExcelWriter setPeriodSummaryDTO(PeriodSummaryDTO periodSummaryDTO){
        this.periodSummaryDTO = periodSummaryDTO;
        CyclePeriod cyclePeriod = periodSummaryDTO.getCyclePeriod();
        LocalDate cycleStartDate = cyclePeriod.getCycleStartDate();
        LocalDate cycleEndDate = cyclePeriod.getCycleEndDate();

        if(cycleStartDate.equals(cycleEndDate)) {
            cyclePeriodString = cycleStartDate.getMonth().toString().substring(0,1) + cycleStartDate.getMonth().toString().substring(1).toLowerCase() + " " + cycleStartDate.getYear();
            return this;
        }

        cyclePeriodString = cycleStartDate.getMonth().toString().substring(0,1) + cycleStartDate.getMonth().toString().substring(1).toLowerCase()
                + " " + cycleStartDate.getDayOfMonth() + ", " + cycleStartDate.getYear() + " - " +
                cycleEndDate.getMonth().toString().substring(0,1) + cycleEndDate.getMonth().toString().substring(1).toLowerCase()
                + " " + cycleEndDate.getDayOfMonth() + ", " + cycleEndDate.getYear();
        return this;
    }

    private void writeMemberPeriodSummary() {
        Cell titleCell = summarySheet.getRow(0).getCell(0);
        titleCell.setCellValue("Summary: " + cyclePeriodString);

        int currentRow = 2;
        int currentStartRow;
        int currentEndRow = currentRow;
        int index = 1;

        for (PeriodSummaryDTO.MemberSummary memberSummary : periodSummaryDTO.getMemberSummaryHashMap().values()){
            currentStartRow = currentRow;
            Cell indexCell = summarySheet.getRow(currentRow).getCell(ExcelRowColumnWrapper.INDEX_CELL_START.getColumn(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

            Member member = memberSummary.getMember();
            Cell memberNameCell = summarySheet.getRow(currentRow).getCell(ExcelRowColumnWrapper.MEMBER_NAME_CELL_START.getColumn(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

            Cell memberAttendanceCell = summarySheet.getRow(currentRow).getCell(ExcelRowColumnWrapper.MEMBERSHIP_ATTENDANCE_CELL_START.getColumn(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            int numOpenStudyAttendance = memberSummary.getCourseAttendanceListHashMap().get(Course.OPEN_STUDY_ID).size();

            List<Charge> chargeList = memberSummary.getChargeList();
            BigDecimal totalCharge = BigDecimal.ZERO;

            for (Charge charge : chargeList) {
                List<ChargeLine> chargeLineList = new ArrayList<>(charge.getChargeLineSet());
                String courseDescription = charge.getDescription();

                for (ChargeLine chargeLine : chargeLineList) {
                    totalCharge = totalCharge.add(chargeLine.getChargeAmount());
                    String dateCharged = chargeLine.getDateCharged().getMonth().toString().substring(0, 1) + chargeLine.getDateCharged().getMonth().toString().substring(1).toLowerCase() + " "
                            + chargeLine.getDateCharged().getDayOfMonth() + ", " + chargeLine.getDateCharged().getYear();
                    String hoursCharged = chargeLine.getBillableUnitsBilled().toString();
                    String subTotal = chargeLine.getChargeAmount().toString();

                    Cell courseDescriptionCell = summarySheet.getRow(currentRow).getCell(ExcelRowColumnWrapper.COURSE_CELL_START.getColumn(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    Cell dateChargedCell = summarySheet.getRow(currentRow).getCell(ExcelRowColumnWrapper.DATE_CELL_START.getColumn(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    Cell numHoursCell = summarySheet.getRow(currentRow).getCell(ExcelRowColumnWrapper.HOURS_CELL_START.getColumn(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    Cell subtotalCell = summarySheet.getRow(currentRow).getCell(ExcelRowColumnWrapper.SUBTOTAL_CELL_START.getColumn(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

                    summarySheetFormatMiddleColumns(currentRow);
                    summarySheetFormatUnmergedCells(currentRow);

                    courseDescriptionCell.setCellValue(courseDescription);
                    dateChargedCell.setCellValue(dateCharged);
                    numHoursCell.setCellValue(hoursCharged + " " + charge.getBillableUnitType() + "(s)");
                    subtotalCell.setCellValue(subTotal);

                    currentRow++;
                }
                currentEndRow = currentRow;
            }
            Cell totalCell = summarySheet.getRow(currentStartRow).getCell(ExcelRowColumnWrapper.TOTAL_CELL_START.getColumn(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            String totalChargeString = totalCharge.toString();
            summarySheetFormatMergedColumns(currentStartRow, currentEndRow - 1);

            indexCell.setCellValue(index);
            memberNameCell.setCellValue(member.getMemberFirstName() + " " + member.getMemberLastName());
            memberAttendanceCell.setCellValue(numOpenStudyAttendance + "/" + periodSummaryDTO.getCourseSessionListHashMap().get(Course.OPEN_STUDY_ID).size() + " days");
            totalCell.setCellValue(totalChargeString);

            index++;
        }
    }

    private void summarySheetFormatMiddleColumns(int currentRow){
        List<CellRangeAddress> cellRangeAddressList = generateCellRangeAddresses(currentRow);
        formatCellRangeAddressList(cellRangeAddressList);
    }

    private void summarySheetFormatUnmergedCells(int currentRow){
        summarySheet.getRow(currentRow).getCell(ExcelRowColumnWrapper.SUBTOTAL_CELL_START.getColumn(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellStyle(borderStyle);
        summarySheet.getRow(currentRow).getCell(ExcelRowColumnWrapper.HOURS_CELL_START.getColumn(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellStyle(borderStyle);
    }

    private void summarySheetFormatMergedColumns(int currentStartRow, int currentEndRow){
        List<CellRangeAddress> cellRangeAddressList = generateMergedCellRangeAddresses(currentStartRow, currentEndRow);
        for(CellRangeAddress cellRangeAddress : cellRangeAddressList) {
            summarySheetAddBorders(cellRangeAddress);
            summarySheetCenterAlign(cellRangeAddress);
        }
    }

    private void formatCellRangeAddressList(List<CellRangeAddress> cellRangeAddressList){
        for(CellRangeAddress cellRangeAddress : cellRangeAddressList){
            summarySheetAddBorders(cellRangeAddress);
            summarySheetCenterAlign(cellRangeAddress);
        }
    }

    private List<CellRangeAddress> generateCellRangeAddresses(int currentRow){
        List<CellRangeAddress> cellRangeAddressList = new ArrayList<>();
        cellRangeAddressList.add(new CellRangeAddress(currentRow, currentRow, ExcelRowColumnWrapper.COURSE_CELL_START.getColumn(), ExcelRowColumnWrapper.COURSE_CELL_START.getColumn() + 2));
        cellRangeAddressList.add(new CellRangeAddress(currentRow, currentRow, ExcelRowColumnWrapper.DATE_CELL_START.getColumn(), ExcelRowColumnWrapper.DATE_CELL_START.getColumn() + 1));
        return cellRangeAddressList;
    }

    private List<CellRangeAddress> generateMergedCellRangeAddresses(int currentStartRow, int currentEndRow){
        List<CellRangeAddress> cellRangeAddressList = new ArrayList<>();
        cellRangeAddressList.add(new CellRangeAddress(currentStartRow, currentEndRow, ExcelRowColumnWrapper.INDEX_CELL_START.getColumn(), ExcelRowColumnWrapper.INDEX_CELL_START.getColumn()));
        cellRangeAddressList.add(new CellRangeAddress(currentStartRow, currentEndRow, ExcelRowColumnWrapper.MEMBER_NAME_CELL_START.getColumn(), ExcelRowColumnWrapper.MEMBER_NAME_CELL_START.getColumn()));
        cellRangeAddressList.add(new CellRangeAddress(currentStartRow, currentEndRow, ExcelRowColumnWrapper.MEMBERSHIP_ATTENDANCE_CELL_START.getColumn(), ExcelRowColumnWrapper.MEMBERSHIP_ATTENDANCE_CELL_START.getColumn() + 1));
        cellRangeAddressList.add(new CellRangeAddress(currentStartRow, currentEndRow, ExcelRowColumnWrapper.TOTAL_CELL_START.getColumn(), ExcelRowColumnWrapper.TOTAL_CELL_START.getColumn()));
        return cellRangeAddressList;
    }

    private void summarySheetAddBorders(CellRangeAddress cellRangeAddress){
        if(cellRangeAddress.getNumberOfCells() > 1)
            summarySheet.addMergedRegion(cellRangeAddress);

        BorderStyle borderStyle = BorderStyle.THIN;
        RegionUtil.setBorderBottom(borderStyle, cellRangeAddress, summarySheet);
        RegionUtil.setBorderTop(borderStyle, cellRangeAddress, summarySheet);
        RegionUtil.setBorderLeft(borderStyle, cellRangeAddress, summarySheet);
        RegionUtil.setBorderRight(borderStyle, cellRangeAddress, summarySheet);
    }

    private void summarySheetCenterAlign(CellRangeAddress cellRangeAddress){
        Row firstRow = summarySheet.getRow(cellRangeAddress.getFirstRow());
        int startColumnIndex = cellRangeAddress.getFirstColumn();
        Cell cell = CellUtil.createCell(firstRow, startColumnIndex, "");
        CellUtil.setVerticalAlignment(cell, VerticalAlignment.CENTER);
        CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
    }

    private void writeReportSummary(){
        Cell periodCell = reportSheet.getRow(1).getCell(0);
        periodCell.setCellValue(cyclePeriodString);
        HashMap<String, AttendanceStatisticsCalculator.CourseStatistics> statisticsHashMap = AttendanceStatisticsCalculator.generateStatistics(periodSummaryDTO);

        int currentCourseTypeRow = ExcelRowColumnWrapper.COURSETYPE_CELL_START.getRow();

        int currentStatisticsRow = ExcelRowColumnWrapper.STATISTICS_TYPE_CELL_START.getRow();

        for(String courseType : statisticsHashMap.keySet()){
            AttendanceStatisticsCalculator.CourseStatistics courseStatistics = statisticsHashMap.get(courseType);
            Cell courseTitleCell = reportSheet.getRow(currentCourseTypeRow).getCell(ExcelRowColumnWrapper.COURSETYPE_CELL_START.getColumn(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            courseTitleCell.setCellValue(courseType);

            populateReportSheetStatisticsLabelCells(currentStatisticsRow);
            populateReportSheetStatisticsCells(currentStatisticsRow, courseStatistics);

            currentCourseTypeRow += 8;
            currentStatisticsRow += 8;
        }
    }

    private void populateReportSheetStatisticsLabelCells(int currentRow){
        Cell medianLabelCell = reportSheet.getRow(currentRow).getCell(ExcelRowColumnWrapper.STATISTICS_TYPE_CELL_START.getColumn(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        Cell ninetyFifthPercentileLabelCell = reportSheet.getRow(currentRow + 1).getCell(ExcelRowColumnWrapper.STATISTICS_TYPE_CELL_START.getColumn(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        Cell averageLabelCell = reportSheet.getRow(currentRow + 2).getCell(ExcelRowColumnWrapper.STATISTICS_TYPE_CELL_START.getColumn(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        Cell minLabelCell = reportSheet.getRow(currentRow + 3).getCell(ExcelRowColumnWrapper.STATISTICS_TYPE_CELL_START.getColumn(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        Cell maxLabelCell = reportSheet.getRow(currentRow + 4).getCell(ExcelRowColumnWrapper.STATISTICS_TYPE_CELL_START.getColumn(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

        medianLabelCell.setCellValue("Median");
        ninetyFifthPercentileLabelCell.setCellValue("95th Percentile");
        averageLabelCell.setCellValue("Average");
        minLabelCell.setCellValue("Min");
        maxLabelCell.setCellValue("Max");
    }

    private void populateReportSheetStatisticsCells(int currentRow, AttendanceStatisticsCalculator.CourseStatistics courseStatistics) {
        Cell medianCell = reportSheet.getRow(currentRow).getCell(ExcelRowColumnWrapper.STATISTICS_DATA_CELL_START.getColumn(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        Cell ninetyFifthPercentileCell = reportSheet.getRow(currentRow + 1).getCell(ExcelRowColumnWrapper.STATISTICS_DATA_CELL_START.getColumn(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        Cell averageCell = reportSheet.getRow(currentRow + 2).getCell(ExcelRowColumnWrapper.STATISTICS_DATA_CELL_START.getColumn(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        Cell minCell = reportSheet.getRow(currentRow + 3).getCell(ExcelRowColumnWrapper.STATISTICS_DATA_CELL_START.getColumn(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        Cell maxCell = reportSheet.getRow(currentRow + 4).getCell(ExcelRowColumnWrapper.STATISTICS_DATA_CELL_START.getColumn(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

        medianCell.setCellValue(courseStatistics.getMedian().toString() + "/" + periodSummaryDTO.getCourseSessionListHashMap().get(Course.OPEN_STUDY_ID).size() + " days");
        ninetyFifthPercentileCell.setCellValue(courseStatistics.getNinetyFifthPercentile().toString() + "/" + periodSummaryDTO.getCourseSessionListHashMap().get(Course.OPEN_STUDY_ID).size() + " days");
        averageCell.setCellValue(courseStatistics.getAverage().toString() + "/" + periodSummaryDTO.getCourseSessionListHashMap().get(Course.OPEN_STUDY_ID).size() + " days");
        minCell.setCellValue(courseStatistics.getMin().toString() + "/" + periodSummaryDTO.getCourseSessionListHashMap().get(Course.OPEN_STUDY_ID).size() + " days");
        maxCell.setCellValue(courseStatistics.getMax().toString() + "/" + periodSummaryDTO.getCourseSessionListHashMap().get(Course.OPEN_STUDY_ID).size() + " days");
    }
    private enum ExcelRowColumnWrapper{
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
        STATISTICS_DATA_CELL_START(5,2);

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
