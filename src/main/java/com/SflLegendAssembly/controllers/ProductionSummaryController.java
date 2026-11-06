package com.SflLegendAssembly.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.SflLegendAssembly.dtos.ProductionSummaryRow;
import com.SflLegendAssembly.queries.ProductionSummaryQueries;
import com.SflLegendAssembly.services.ProductionSummaryService;

@RestController
@RequestMapping("/api/production-summary")
public class ProductionSummaryController {

  private final ProductionSummaryService productionSummaryService;

  public ProductionSummaryController(ProductionSummaryService productionSummaryService) {
    this.productionSummaryService = productionSummaryService;
  }

  @GetMapping
  public List<ProductionSummaryRow> findLatestByDateAndShift(
      @RequestParam String side,
      @RequestParam LocalDate startDate,
      @RequestParam LocalDate endDate) {
    if (!ProductionSummaryQueries.supportsSide(side)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Side must be LH or RH.");
    }
    if (startDate.isAfter(endDate)) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Start date must be on or before end date.");
    }
    return productionSummaryService.findLatestByDateAndShift(side, startDate, endDate);
  }
}
