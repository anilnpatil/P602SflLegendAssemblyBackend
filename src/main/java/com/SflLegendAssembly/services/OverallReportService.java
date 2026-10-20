package com.SflLegendAssembly.services;

import java.time.LocalDate;
import java.util.List;

import com.SflLegendAssembly.dtos.OverallReportRow;

public interface OverallReportService {
  List<OverallReportRow> findCompletedComponents(
      String side,
      String barcode,
      LocalDate startDate,
      LocalDate endDate,
      Integer shift);
}
