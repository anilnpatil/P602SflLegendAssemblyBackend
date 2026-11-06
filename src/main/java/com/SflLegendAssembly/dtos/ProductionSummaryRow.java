package com.SflLegendAssembly.dtos;

import java.time.LocalDate;

public record ProductionSummaryRow(
    LocalDate productionDate,
    Integer shift,
    Integer okCount,
    Integer notOkCount,
    Integer totalPartCount) {
}
