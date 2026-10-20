package com.SflLegendAssembly.services;

import java.time.LocalDate;
import java.util.List;

import com.SflLegendAssembly.dtos.LiveMachineStatus;

public interface MachineReportService {
  List<LiveMachineStatus> findAll(String machineCode);

  List<LiveMachineStatus> findByPartId(String machineCode, String partId);

  List<LiveMachineStatus> findByDateRange(String machineCode, LocalDate startDate, LocalDate endDate);
}
