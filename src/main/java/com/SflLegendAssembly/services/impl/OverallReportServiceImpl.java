package com.SflLegendAssembly.services.impl;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.SflLegendAssembly.dtos.LiveMachineStatus;
import com.SflLegendAssembly.dtos.OverallReportRow;
import com.SflLegendAssembly.queries.OverallReportQueries;
import com.SflLegendAssembly.services.OverallReportService;

@Service
public class OverallReportServiceImpl implements OverallReportService {

  private final JdbcTemplate jdbcTemplate;

  public OverallReportServiceImpl(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public List<OverallReportRow> findCompletedComponents(
      String side,
      String barcode,
      LocalDate startDate,
      LocalDate endDate,
      Integer shift) {
    OverallReportQueries.PreparedQuery query = OverallReportQueries.findCompletedComponents(
        side,
        startDate,
        endDate,
        barcode,
        shift);
    List<LiveMachineStatus> machineRows = jdbcTemplate.query(
        query.sql(),
        LiveDataServiceImpl::mapLiveMachineStatus,
        query.parameters().toArray());
    Map<String, ReportAccumulator> reports = new LinkedHashMap<>();
    for (LiveMachineStatus machine : machineRows) {
      String barcodeKey = normalizeBarcode(machine.barcode());
      if (barcodeKey.isEmpty()) {
        continue;
      }
      ReportAccumulator report = reports.computeIfAbsent(
          barcodeKey,
          ignored -> new ReportAccumulator(machine.barcode()));
      report.machineRows.putIfAbsent(machine.machineCode(), machine);
      if ("50-LH".equals(machine.machineCode()) || "50-RH".equals(machine.machineCode())) {
        report.componentSide = machine.machineCode().endsWith("LH") ? "LH" : "RH";
        report.shift = machine.shift();
        report.completionTime = machine.cycleStartTime();
        report.finalStatus = machine.finalStatus();
      }
    }
    return reports.values().stream()
        .map(ReportAccumulator::toReportRow)
        .toList();
  }

  private static String normalizeBarcode(String barcode) {
    return barcode == null ? "" : barcode.strip().toUpperCase(Locale.ROOT);
  }

  private static final class ReportAccumulator {
    private final String barcode;
    private final Map<String, LiveMachineStatus> machineRows = new LinkedHashMap<>();
    private String componentSide;
    private Integer shift;
    private java.time.LocalDateTime completionTime;
    private Integer finalStatus;

    private ReportAccumulator(String barcode) {
      this.barcode = barcode;
    }

    private OverallReportRow toReportRow() {
      return new OverallReportRow(
          barcode,
          componentSide,
          shift,
          completionTime,
          finalStatus,
          List.copyOf(machineRows.values()));
    }
  }
}
