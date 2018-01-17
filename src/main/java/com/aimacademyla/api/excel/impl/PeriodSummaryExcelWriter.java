package com.aimacademyla.api.excel.impl;

import com.aimacademyla.api.excel.MSExcelWriter;
import org.apache.poi.ss.usermodel.Workbook;

public class PeriodSummaryExcelWriter extends MSExcelWriter{

    public PeriodSummaryExcelWriter(Workbook workbook){
        super(workbook);
    }

    @Override
    public Workbook writeToWorkbook() {
        return null;
    }
}
