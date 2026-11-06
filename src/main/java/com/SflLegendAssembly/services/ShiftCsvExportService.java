package com.SflLegendAssembly.services;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.SflLegendAssembly.dtos.LiveMachineStatus;

@Service
public class ShiftCsvExportService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ShiftCsvExportService.class);
  private static final List<String> LH_MACHINES = List.of("30A-LH", "40A-LH", "50-LH");
  private static final List<String> RH_MACHINES = List.of("30A-RH", "40A-RH", "50-RH");
  private static final Comparator<LiveMachineStatus> REPORT_ORDER =
      Comparator.comparing(
              LiveMachineStatus::cycleStartTime,
              Comparator.nullsLast(Comparator.reverseOrder()))
          .thenComparing(
              LiveMachineStatus::id,
              Comparator.nullsLast(Comparator.reverseOrder()));

  private final MachineReportService machineReportService;
  private final Path reportRoot;
  private final ZoneId reportZone;

  public ShiftCsvExportService(
      MachineReportService machineReportService,
      @Value("${reports.shift-export.root-directory:C:/LegendAssemblyReports}") String reportRoot,
      @Value("${reports.shift-export.zone:Asia/Kolkata}") String reportZone) {
    this.machineReportService = machineReportService;
    this.reportRoot = Path.of(reportRoot);
    this.reportZone = ZoneId.of(reportZone);
  }

  @Scheduled(
      cron = "${reports.shift-export.cron:0 5 6,14,22 * * *}",
      zone = "${reports.shift-export.zone:Asia/Kolkata}")
  public void exportCompletedShift() {
    LocalDateTime completionTime = LocalDateTime.now(reportZone);
    ShiftWindow shiftWindow = ShiftWindow.completedAt(completionTime);
    LOGGER.info("Generating CSV reports for completed shift {} dated {}",
        shiftWindow.shiftNumber(), shiftWindow.reportDate());

    RuntimeException exportFailure = null;
    for (ReportSide side : ReportSide.values()) {
      try {
        exportSide(side, shiftWindow);
      } catch (IOException | RuntimeException exception) {
        LOGGER.error("Failed to generate {} report for shift {} dated {}",
            side.directoryName(), shiftWindow.shiftNumber(), shiftWindow.reportDate(), exception);
        if (exportFailure == null) {
          exportFailure = new IllegalStateException("One or more shift CSV exports failed.");
        }
        exportFailure.addSuppressed(exception);
      }
    }
    if (exportFailure != null) {
      throw exportFailure;
    }
  }

  private void exportSide(ReportSide side, ShiftWindow shiftWindow) throws IOException {
    List<LiveMachineStatus> rows = new ArrayList<>();
    for (String machineCode : side.machineCodes()) {
      rows.addAll(machineReportService.findByShiftAndDateRange(
          machineCode,
          shiftWindow.shiftNumber(),
          shiftWindow.startDateTime(),
          shiftWindow.endDateTime()));
    }
    rows.sort(REPORT_ORDER);

    Path reportFile = reportRoot
        .resolve("shift" + shiftWindow.shiftNumber())
        .resolve(side.directoryName())
        .resolve(shiftWindow.reportDate() + ".csv");
    Files.createDirectories(reportFile.getParent());
    Files.writeString(
        reportFile,
        MachineStatusCsvWriter.write(rows),
        StandardCharsets.UTF_8,
        StandardOpenOption.CREATE,
        StandardOpenOption.TRUNCATE_EXISTING,
        StandardOpenOption.WRITE);
    LOGGER.info("Generated {} rows in {}", rows.size(), reportFile);
  }

  private enum ReportSide {
    LH("LhReports", LH_MACHINES),
    RH("RhReports", RH_MACHINES);

    private final String directoryName;
    private final List<String> machineCodes;

    ReportSide(String directoryName, List<String> machineCodes) {
      this.directoryName = directoryName;
      this.machineCodes = machineCodes;
    }

    String directoryName() {
      return directoryName;
    }

    List<String> machineCodes() {
      return machineCodes;
    }
  }

  record ShiftWindow(
      int shiftNumber,
      LocalDate reportDate,
      LocalDateTime startDateTime,
      LocalDateTime endDateTime) {

    static ShiftWindow completedAt(LocalDateTime completionTime) {
      LocalDate reportDate = completionTime.toLocalDate();
      return switch (completionTime.getHour()) {
        case 6 -> new ShiftWindow(
            3,
            reportDate,
            reportDate.minusDays(1).atTime(22, 0),
            reportDate.atTime(6, 0));
        case 14 -> new ShiftWindow(
            1,
            reportDate,
            reportDate.atTime(6, 0),
            reportDate.atTime(14, 0));
        case 22 -> new ShiftWindow(
            2,
            reportDate,
            reportDate.atTime(14, 0),
            reportDate.atTime(22, 0));
        default -> throw new IllegalStateException(
            "Shift CSV export ran outside a configured shift completion time: "
                + completionTime);
      };
    }
  }
}
