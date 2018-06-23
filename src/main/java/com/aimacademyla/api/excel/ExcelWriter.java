package com.aimacademyla.api.excel;

import com.aimacademyla.model.dto.PeriodSummaryDTO;
import org.apache.poi.ss.usermodel.Workbook;

public interface ExcelWriter {
    Workbook writeToWorkbook();
    void setPeriodSummaryDTO(PeriodSummaryDTO periodSummaryDTO);
}
