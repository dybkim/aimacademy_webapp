package com.aimacademyla.api.excel.format;

import com.aimacademyla.model.dto.PeriodSummaryDTO;
import org.apache.poi.ss.usermodel.Sheet;

public interface SheetFormat {
    Sheet formatOutput();
    void setPeriodSummaryDTO(PeriodSummaryDTO periodSummaryDTO);
    PeriodSummaryDTO getPeriodSummaryDTO();
    String getCyclePeriodString();
}
