package com.aimacademyla.api.excel.format.impl;

import com.aimacademyla.api.excel.calculator.AttendanceStatisticsCalculator;
import com.aimacademyla.api.excel.format.AbstractSheetFormat;
import com.aimacademyla.api.excel.format.SheetFormat;
import com.aimacademyla.model.Course;
import com.aimacademyla.model.dto.PeriodSummaryDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.HashMap;

public class DefaultPeriodReportSheetFormat extends AbstractSheetFormat{

    public DefaultPeriodReportSheetFormat(Sheet outputSheet){
        super(outputSheet);
    }

    /**
     * Formats and enters information from periodSummaryDTO and
     * @return
     */
    @Override
    public Sheet formatOutput() {
        //Initialize sheet title
        Cell periodCell = outputSheet.getRow(1).getCell(0);
        periodCell.setCellValue(cyclePeriodString);

        //Format and fill in sheet information
        HashMap<String, AttendanceStatisticsCalculator.CourseStatistics> statisticsHashMap = AttendanceStatisticsCalculator.generateStatistics(periodSummaryDTO);

        int currentCourseTypeRow = ExcelRowColumnWrapper.COURSETYPE_CELL_START.getRow();
        int currentStatisticsRow = ExcelRowColumnWrapper.STATISTICS_TYPE_CELL_START.getRow();

        for(String courseType : statisticsHashMap.keySet()){
            AttendanceStatisticsCalculator.CourseStatistics courseStatistics = statisticsHashMap.get(courseType);
            Cell courseTitleCell = outputSheet.getRow(currentCourseTypeRow).getCell(ExcelRowColumnWrapper.COURSETYPE_CELL_START.getColumn(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            courseTitleCell.setCellValue(courseType);

            populateReportSheetStatisticsLabelCells(currentStatisticsRow);
            populateReportSheetStatisticsCells(currentStatisticsRow, courseStatistics);

            currentCourseTypeRow += 8;
            currentStatisticsRow += 8;
        }
        return outputSheet;
    }

    private void populateReportSheetStatisticsLabelCells(int currentRow){
        Cell medianLabelCell = outputSheet.getRow(currentRow).getCell(ExcelRowColumnWrapper.STATISTICS_TYPE_CELL_START.getColumn(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        Cell ninetyFifthPercentileLabelCell = outputSheet.getRow(currentRow + 1).getCell(ExcelRowColumnWrapper.STATISTICS_TYPE_CELL_START.getColumn(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        Cell averageLabelCell = outputSheet.getRow(currentRow + 2).getCell(ExcelRowColumnWrapper.STATISTICS_TYPE_CELL_START.getColumn(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        Cell minLabelCell = outputSheet.getRow(currentRow + 3).getCell(ExcelRowColumnWrapper.STATISTICS_TYPE_CELL_START.getColumn(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        Cell maxLabelCell = outputSheet.getRow(currentRow + 4).getCell(ExcelRowColumnWrapper.STATISTICS_TYPE_CELL_START.getColumn(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

        medianLabelCell.setCellValue("Median");
        ninetyFifthPercentileLabelCell.setCellValue("95th Percentile");
        averageLabelCell.setCellValue("Average");
        minLabelCell.setCellValue("Min");
        maxLabelCell.setCellValue("Max");
    }

    private void populateReportSheetStatisticsCells(int currentRow, AttendanceStatisticsCalculator.CourseStatistics courseStatistics) {
        Cell medianCell = outputSheet.getRow(currentRow).getCell(ExcelRowColumnWrapper.STATISTICS_DATA_CELL_START.getColumn(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        Cell ninetyFifthPercentileCell = outputSheet.getRow(currentRow + 1).getCell(ExcelRowColumnWrapper.STATISTICS_DATA_CELL_START.getColumn(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        Cell averageCell = outputSheet.getRow(currentRow + 2).getCell(ExcelRowColumnWrapper.STATISTICS_DATA_CELL_START.getColumn(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        Cell minCell = outputSheet.getRow(currentRow + 3).getCell(ExcelRowColumnWrapper.STATISTICS_DATA_CELL_START.getColumn(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        Cell maxCell = outputSheet.getRow(currentRow + 4).getCell(ExcelRowColumnWrapper.STATISTICS_DATA_CELL_START.getColumn(), Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

        medianCell.setCellValue(courseStatistics.getMedian().toString() + "/" + periodSummaryDTO.getCourseSessionListHashMap().get(Course.OPEN_STUDY_ID).size() + " days");
        ninetyFifthPercentileCell.setCellValue(courseStatistics.getNinetyFifthPercentile().toString() + "/" + periodSummaryDTO.getCourseSessionListHashMap().get(Course.OPEN_STUDY_ID).size() + " days");
        averageCell.setCellValue(courseStatistics.getAverage().toString() + "/" + periodSummaryDTO.getCourseSessionListHashMap().get(Course.OPEN_STUDY_ID).size() + " days");
        minCell.setCellValue(courseStatistics.getMin().toString() + "/" + periodSummaryDTO.getCourseSessionListHashMap().get(Course.OPEN_STUDY_ID).size() + " days");
        maxCell.setCellValue(courseStatistics.getMax().toString() + "/" + periodSummaryDTO.getCourseSessionListHashMap().get(Course.OPEN_STUDY_ID).size() + " days");
    }
}
