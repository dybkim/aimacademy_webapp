package com.aimacademyla.api.excel;

import org.apache.poi.ss.usermodel.Workbook;

public abstract class MSExcelWriter {

    protected Workbook workbook;

    public MSExcelWriter(Workbook workbook){
        this.workbook = workbook;
    }

    public abstract Workbook writeToWorkbook();
}
