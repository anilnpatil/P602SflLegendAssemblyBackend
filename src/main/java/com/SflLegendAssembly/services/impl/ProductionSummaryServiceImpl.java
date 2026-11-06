package com.SflLegendAssembly.services.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import com.SflLegendAssembly.dtos.ProductionSummaryRow;
import com.SflLegendAssembly.queries.ProductionSummaryQueries;
import com.SflLegendAssembly.services.ProductionSummaryService;

@Service
public class ProductionSummaryServiceImpl implements ProductionSummaryService {

  private static final RowMapper<ProductionSummaryRow> ROW_MAPPER =
      ProductionSummaryServiceImpl::mapRow;

  private final JdbcTemplate jdbcTemplate;

  public ProductionSummaryServiceImpl(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public List<ProductionSummaryRow> findLatestByDateAndShift(
      String side,
      LocalDate startDate,
      LocalDate endDate) {
    ProductionSummaryQueries.PreparedQuery query =
        ProductionSummaryQueries.findLatestByDateAndShift(side, startDate, endDate);
    return jdbcTemplate.query(query.sql(), ROW_MAPPER, query.parameters().toArray());
  }

  private static ProductionSummaryRow mapRow(ResultSet resultSet, int rowNumber)
      throws SQLException {
    return new ProductionSummaryRow(
        resultSet.getObject("production_date", LocalDate.class),
        resultSet.getObject("shift", Integer.class),
        resultSet.getObject("ok_count", Integer.class),
        resultSet.getObject("not_ok_count", Integer.class),
        resultSet.getObject("total_part_count", Integer.class));
  }
}
