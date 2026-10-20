package com.SflLegendAssembly.services.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.SflLegendAssembly.dtos.LiveMachineStatus;
import com.SflLegendAssembly.queries.MachineReportQueries;
import com.SflLegendAssembly.services.MachineReportService;

@Service
public class MachineReportServiceImpl implements MachineReportService {

  private static final RowMapper<LiveMachineStatus> REPORT_ROW_MAPPER =
      LiveDataServiceImpl::mapLiveMachineStatus;

  private final JdbcTemplate jdbcTemplate;

  public MachineReportServiceImpl(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public List<LiveMachineStatus> findAll(String machineCode) {
    return jdbcTemplate.query(
        MachineReportQueries.findAll(machineCode),
        REPORT_ROW_MAPPER);
  }

  @Override
  public List<LiveMachineStatus> findByPartId(String machineCode, String partId) {
    return jdbcTemplate.query(
        MachineReportQueries.findByPartId(machineCode),
        REPORT_ROW_MAPPER,
        partId);
  }

  @Override
  public List<LiveMachineStatus> findByDateRange(
      String machineCode,
      LocalDate startDate,
      LocalDate endDate) {
    return jdbcTemplate.query(
        MachineReportQueries.findByDateRange(machineCode),
        REPORT_ROW_MAPPER,
        startDate.atStartOfDay(),
        endDate.plusDays(1).atStartOfDay());
  }

}
