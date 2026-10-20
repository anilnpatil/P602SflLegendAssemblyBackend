package com.SflLegendAssembly.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.SflLegendAssembly.dtos.LiveMachineStatus;
import com.SflLegendAssembly.queries.MachineReportQueries;
import com.SflLegendAssembly.services.MachineReportService;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

  private final MachineReportService machineReportService;

  public ReportController(MachineReportService machineReportService) {
    this.machineReportService = machineReportService;
  }

  @GetMapping("/history")
  public List<LiveMachineStatus> findAll(@RequestParam String machineCode) {
    validateMachine(machineCode);
    LocalDate today = LocalDate.now();
    return machineReportService.findByDateRange(machineCode, today, today);
  }

  @GetMapping("/part")
  public List<LiveMachineStatus> findByPartId(
      @RequestParam String machineCode,
      @RequestParam String partId) {
    validateMachine(machineCode);
    if (partId.isBlank()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Part ID is required.");
    }
    return machineReportService.findByPartId(machineCode, partId.trim());
  }

  @GetMapping("/date-range")
  public List<LiveMachineStatus> findByDateRange(
      @RequestParam String machineCode,
      @RequestParam LocalDate startDate,
      @RequestParam LocalDate endDate) {
    validateMachine(machineCode);
    if (startDate.isAfter(endDate)) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Start date must be on or before end date.");
    }
    return machineReportService.findByDateRange(machineCode, startDate, endDate);
  }

  private static void validateMachine(String machineCode) {
    if (!MachineReportQueries.supportsMachine(machineCode)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported machine code.");
    }
  }
}
