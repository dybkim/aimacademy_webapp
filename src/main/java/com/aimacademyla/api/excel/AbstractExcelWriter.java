package com.aimacademyla.api.excel;

import org.apache.poi.ss.usermodel.Workbook;

public abstract class AbstractExcelWriter implements ExcelWriter{

    protected Workbook workbook;

    public AbstractExcelWriter(Workbook workbook){
        this.workbook = workbook;
    }

    public abstract Workbook writeToWorkbook();
}
