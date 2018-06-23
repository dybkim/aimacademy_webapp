package com.aimacademyla.api.excel;

import com.aimacademyla.model.dto.PeriodSummaryDTO;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public abstract class AbstractExcelWriter implements ExcelWriter{

    protected Workbook workbook;
    protected PeriodSummaryDTO periodSummaryDTO;

    public AbstractExcelWriter(Workbook workbook){
        this.workbook = workbook;
    }

    @Override
    public Workbook writeToWorkbook() {
        writeToSheets();
        return workbook;
    }

    public abstract void writeToSheets();

    public PeriodSummaryDTO getPeriodSummaryDTO() {
        return periodSummaryDTO;
    }
}
