package com.SflLegendAssembly.services;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.SflLegendAssembly.dtos.LiveMachineStatus;

final class MachineStatusCsvWriter {

  private static final List<String> HEADERS = List.of(
      "machineCode", "entityName", "id", "finalStatus", "barcode", "operatorName", "shift",
      "airGauge1Min", "airGauge1Actual", "airGauge1Max", "airGauge1Status",
      "airGauge2Min", "airGauge2Actual", "airGauge2Max", "airGauge2Status",
      "outerDiaMin", "outerDiaActual", "outerDiaMax", "outerDiaStatus",
      "innerDiaMin", "innerDiaActual", "innerDiaMax", "innerDiaStatus",
      "miniLoad", "actualLoad", "maxLoad", "loadStatus",
      "minDisplacement", "actualDisplacement", "maxDisplacement", "displacementStatus",
      "okCount", "notOkCount", "totalPartCount", "cycleTime", "cycleStartTime", "cycleEndTime");

  private MachineStatusCsvWriter() {
  }

  static String write(List<LiveMachineStatus> rows) {
    StringBuilder csv = new StringBuilder(csvLine(HEADERS)).append("\r\n");
    for (LiveMachineStatus row : rows) {
      csv.append(csvLine(Arrays.asList(
          row.machineCode(), row.entityName(), row.id(), row.finalStatus(), row.barcode(),
          row.operatorName(), row.shift(),
          row.airGauge1Min(), row.airGauge1Actual(), row.airGauge1Max(), row.airGauge1Status(),
          row.airGauge2Min(), row.airGauge2Actual(), row.airGauge2Max(), row.airGauge2Status(),
          row.outerDiaMin(), row.outerDiaActual(), row.outerDiaMax(), row.outerDiaStatus(),
          row.innerDiaMin(), row.innerDiaActual(), row.innerDiaMax(), row.innerDiaStatus(),
          row.miniLoad(), row.actualLoad(), row.maxLoad(), row.loadStatus(),
          row.minDisplacement(), row.actualDisplacement(), row.maxDisplacement(),
          row.displacementStatus(), row.okCount(), row.notOkCount(), row.totalPartCount(),
          row.cycleTime(), row.cycleStartTime(), row.cycleEndTime())));
      csv.append("\r\n");
    }
    return csv.toString();
  }

  private static String csvLine(List<?> values) {
    return values.stream()
        .map(MachineStatusCsvWriter::escape)
        .collect(Collectors.joining(","));
  }

  private static String escape(Object value) {
    if (value == null) {
      return "";
    }
    String text = value.toString();
    if (text.indexOf(',') >= 0 || text.indexOf('"') >= 0
        || text.indexOf('\r') >= 0 || text.indexOf('\n') >= 0) {
      return "\"" + text.replace("\"", "\"\"") + "\"";
    }
    return text;
  }
}
