package com.aimacademyla.api.excel.format.impl;

import com.aimacademyla.api.excel.calculator.AttendanceStatisticsCalculator;
import com.aimacademyla.api.excel.format.AbstractSheetFormat;
import com.aimacademyla.api.excel.format.SheetFormat;
import com.aimacademyla.model.Charge;
import com.aimacademyla.model.ChargeLine;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.Member;
import com.aimacademyla.model.dto.PeriodSummaryDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DefaultSummaryReportSheetFormat extends AbstractSheetFormat{

    public DefaultSummaryReportSheetFormat(Sheet outputSheet){
        super(outputSheet);
    }

    /**
     * Formats and enters information from periodSummaryDTO onto outputSheet
     * @return outputSheet with proper, formatted information
     */
    @Override
    public Sheet formatOutput() {
        //Initialize Sheet Title
        Cell titleCell = outputSheet.getRow(0).getCell(0);
        titleCell.setCellValue("Summary: " + cyclePeriodString);


        //Format and fill in sheet information
        int currentRow = 2;
        int currentStartRow;
        int currentEndRow = currentRow;
        int index = 1;

        for (PeriodSummaryDTO.MemberSummary memberSummary : periodSummaryDTO.getMemberSummaryHashMap().values()){
            currentStartRow = currentRow;
            Cell indexCell = outputSheet.getRow(currentRow).getCell(ExcelRowColumnWrapper.INDEX_CELL_START.getColumn(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

            Member member = memberSummary.getMember();
            Cell memberNameCell = outputSheet.getRow(currentRow).getCell(ExcelRowColumnWrapper.MEMBER_NAME_CELL_START.getColumn(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

            Cell memberAttendanceCell = outputSheet.getRow(currentRow).getCell(ExcelRowColumnWrapper.MEMBERSHIP_ATTENDANCE_CELL_START.getColumn(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
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

                    Cell courseDescriptionCell = outputSheet.getRow(currentRow).getCell(ExcelRowColumnWrapper.COURSE_CELL_START.getColumn(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    Cell dateChargedCell = outputSheet.getRow(currentRow).getCell(ExcelRowColumnWrapper.DATE_CELL_START.getColumn(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    Cell numHoursCell = outputSheet.getRow(currentRow).getCell(ExcelRowColumnWrapper.HOURS_CELL_START.getColumn(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    Cell subtotalCell = outputSheet.getRow(currentRow).getCell(ExcelRowColumnWrapper.SUBTOTAL_CELL_START.getColumn(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

                    outputSheetFormatMiddleColumns(currentRow);
                    outputSheetFormatUnmergedCells(currentRow);

                    courseDescriptionCell.setCellValue(courseDescription);
                    dateChargedCell.setCellValue(dateCharged);
                    numHoursCell.setCellValue(hoursCharged + " " + charge.getBillableUnitType() + "(s)");
                    subtotalCell.setCellValue(subTotal);

                    currentRow++;
                }
                currentEndRow = currentRow;
            }
            Cell totalCell = outputSheet.getRow(currentStartRow).getCell(ExcelRowColumnWrapper.TOTAL_CELL_START.getColumn(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            String totalChargeString = totalCharge.toString();
            outputSheetFormatMergedColumns(currentStartRow, currentEndRow - 1);

            indexCell.setCellValue(index);
            memberNameCell.setCellValue(member.getMemberFirstName() + " " + member.getMemberLastName());
            memberAttendanceCell.setCellValue(numOpenStudyAttendance + "/" + periodSummaryDTO.getCourseSessionListHashMap().get(Course.OPEN_STUDY_ID).size() + " days");
            totalCell.setCellValue(totalChargeString);

            index++;
        }

        return outputSheet;
    }

    public Sheet getOutputSheet() {
        return outputSheet;
    }

    public void setOutputSheet(Sheet outputSheet) {
        this.outputSheet= outputSheet;
    }

    private void outputSheetFormatMiddleColumns(int currentRow){
        List<CellRangeAddress> cellRangeAddressList = generateCellRangeAddresses(currentRow);
        formatCellRangeAddressList(cellRangeAddressList);
    }

    private void outputSheetFormatUnmergedCells(int currentRow){
        outputSheet.getRow(currentRow).getCell(ExcelRowColumnWrapper.SUBTOTAL_CELL_START.getColumn(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellStyle(borderStyle);
        outputSheet.getRow(currentRow).getCell(ExcelRowColumnWrapper.HOURS_CELL_START.getColumn(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellStyle(borderStyle);
    }

    private void outputSheetFormatMergedColumns(int currentStartRow, int currentEndRow){
        List<CellRangeAddress> cellRangeAddressList = generateMergedCellRangeAddresses(currentStartRow, currentEndRow);
        for(CellRangeAddress cellRangeAddress : cellRangeAddressList) {
            outputSheetAddBorders(cellRangeAddress);
            outputSheetCenterAlign(cellRangeAddress);
        }
    }

    private void formatCellRangeAddressList(List<CellRangeAddress> cellRangeAddressList){
        for(CellRangeAddress cellRangeAddress : cellRangeAddressList){
            outputSheetAddBorders(cellRangeAddress);
            outputSheetCenterAlign(cellRangeAddress);
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

    private void outputSheetAddBorders(CellRangeAddress cellRangeAddress){
        if(cellRangeAddress.getNumberOfCells() > 1)
            outputSheet.addMergedRegion(cellRangeAddress);

        BorderStyle borderStyle = BorderStyle.THIN;
        RegionUtil.setBorderBottom(borderStyle, cellRangeAddress, outputSheet);
        RegionUtil.setBorderTop(borderStyle, cellRangeAddress, outputSheet);
        RegionUtil.setBorderLeft(borderStyle, cellRangeAddress, outputSheet);
        RegionUtil.setBorderRight(borderStyle, cellRangeAddress, outputSheet);
    }

    private void outputSheetCenterAlign(CellRangeAddress cellRangeAddress){
        Row firstRow = outputSheet.getRow(cellRangeAddress.getFirstRow());
        int startColumnIndex = cellRangeAddress.getFirstColumn();
        Cell cell = CellUtil.createCell(firstRow, startColumnIndex, "");
        CellUtil.setVerticalAlignment(cell, VerticalAlignment.CENTER);
        CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
    }
}
