package com.aimacademyla.api.excel;

import org.apache.poi.ss.usermodel.Workbook;

public interface ExcelWriter {
    Workbook writeToWorkbook();
}
