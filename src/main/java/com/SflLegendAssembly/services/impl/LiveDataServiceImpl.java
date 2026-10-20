package com.SflLegendAssembly.services.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.SflLegendAssembly.dtos.LiveMachineStatus;
import com.SflLegendAssembly.queries.LiveDataQueries;
import com.SflLegendAssembly.services.LiveDataservice;

@Service
public class LiveDataServiceImpl implements LiveDataservice {

  private static final RowMapper<LiveMachineStatus> LIVE_MACHINE_STATUS_ROW_MAPPER =
      LiveDataServiceImpl::mapLiveMachineStatus;

  private final JdbcTemplate jdbcTemplate;

  public LiveDataServiceImpl(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public List<LiveMachineStatus> getLatestMachineStatuses() {
    return jdbcTemplate.query(
        LiveDataQueries.FIND_LATEST_MACHINE_STATUSES,
        LIVE_MACHINE_STATUS_ROW_MAPPER);
  }

  static LiveMachineStatus mapLiveMachineStatus(ResultSet resultSet, int rowNumber)
      throws SQLException {
    return new LiveMachineStatus(
        resultSet.getString("machine_code"),
        resultSet.getString("entity_name"),
        resultSet.getObject("id", Long.class),
        resultSet.getObject("final_status", Integer.class),
        resultSet.getString("barcode"),
        resultSet.getString("operator_name"),
        resultSet.getObject("shift", Integer.class),
        resultSet.getObject("air_gauge1_min", Float.class),
        resultSet.getObject("air_gauge1_actual", Float.class),
        resultSet.getObject("air_gauge1_max", Float.class),
        resultSet.getObject("air_gauge1_status", Integer.class),
        resultSet.getObject("air_gauge2_min", Float.class),
        resultSet.getObject("air_gauge2_actual", Float.class),
        resultSet.getObject("air_gauge2_max", Float.class),
        resultSet.getObject("air_gauge2_status", Integer.class),
        resultSet.getObject("outer_dia_min", Float.class),
        resultSet.getObject("outer_dia_actual", Float.class),
        resultSet.getObject("outer_dia_max", Float.class),
        resultSet.getObject("outer_dia_status", Integer.class),
        resultSet.getObject("inner_dia_min", Float.class),
        resultSet.getObject("inner_dia_actual", Float.class),
        resultSet.getObject("inner_dia_max", Float.class),
        resultSet.getObject("inner_dia_status", Integer.class),
        resultSet.getObject("mini_load", Float.class),
        resultSet.getObject("actual_load", Float.class),
        resultSet.getObject("max_load", Float.class),
        resultSet.getObject("load_status", Integer.class),
        resultSet.getObject("min_displacement", Float.class),
        resultSet.getObject("actual_displacement", Float.class),
        resultSet.getObject("max_displacement", Float.class),
        resultSet.getObject("displacement_status", Integer.class),
        resultSet.getObject("ok_count", Integer.class),
        resultSet.getObject("not_ok_count", Integer.class),
        resultSet.getObject("total_part_count", Integer.class),
        resultSet.getObject("cycle_time", Integer.class),
        resultSet.getObject("cycle_start_time", java.time.LocalDateTime.class),
        resultSet.getObject("cycle_end_time", java.time.LocalDateTime.class));
  }
}
