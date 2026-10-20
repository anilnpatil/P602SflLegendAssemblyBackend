package com.SflLegendAssembly.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.SflLegendAssembly.dtos.OverallReportRow;
import com.SflLegendAssembly.queries.OverallReportQueries;
import com.SflLegendAssembly.services.OverallReportService;

@RestController
@RequestMapping("/api/overall-reports")
public class OverallReportController {

  private final OverallReportService overallReportService;

  public OverallReportController(OverallReportService overallReportService) {
    this.overallReportService = overallReportService;
  }

  @GetMapping
  public List<OverallReportRow> findCompletedComponents(
      @RequestParam String side,
      @RequestParam LocalDate startDate,
      @RequestParam LocalDate endDate,
      @RequestParam(required = false) String barcode,
      @RequestParam(defaultValue = "ALL") String shift) {
    if (!OverallReportQueries.supportsSide(side)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Side must be LH, RH, or BOTH.");
    }
    if (startDate.isAfter(endDate)) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Start date must be on or before end date.");
    }
    Integer shiftNumber = parseShift(shift);
    return overallReportService.findCompletedComponents(
        side,
        barcode,
        startDate,
        endDate,
        shiftNumber);
  }

  private static Integer parseShift(String shift) {
    if ("ALL".equals(shift)) {
      return null;
    }
    if ("1".equals(shift) || "2".equals(shift) || "3".equals(shift)) {
      return Integer.valueOf(shift);
    }
    throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST,
        "Shift must be ALL, 1, 2, or 3.");
  }
}
