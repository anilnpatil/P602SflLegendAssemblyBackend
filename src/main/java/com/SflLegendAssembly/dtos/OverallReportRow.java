package com.SflLegendAssembly.dtos;

import java.time.LocalDateTime;
import java.util.List;

public record OverallReportRow(
    String barcode,
    String componentSide,
    Integer shift,
    LocalDateTime completionTime,
    Integer finalStatus,
    List<LiveMachineStatus> machines) {
}
