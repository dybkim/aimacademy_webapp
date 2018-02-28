package com.aimacademyla.model.temporal;

import java.time.LocalDate;

public class CyclePeriod{

    private LocalDate cycleStartDate;
    private LocalDate cycleEndDate;

    public CyclePeriod(LocalDate cycleStartDate, LocalDate cycleEndDate){
        this.cycleStartDate = cycleStartDate;
        this.cycleEndDate = cycleEndDate;
    }

    public LocalDate getCycleStartDate() {
        return cycleStartDate;
    }

    public CyclePeriod setCycleStartDate(LocalDate cycleStartDate) {
        this.cycleStartDate = cycleStartDate;
        return this;
    }

    public LocalDate getCycleEndDate() {
        return cycleEndDate;
    }

    public CyclePeriod setCycleEndDate(LocalDate cycleEndDate) {
        this.cycleEndDate = cycleEndDate;
        return this;
    }
}
