package com.SflLegendAssembly.services;

import java.time.LocalDate;
import java.util.List;

import com.SflLegendAssembly.dtos.ProductionSummaryRow;

public interface ProductionSummaryService {
  List<ProductionSummaryRow> findLatestByDateAndShift(
      String side,
      LocalDate startDate,
      LocalDate endDate);
}
