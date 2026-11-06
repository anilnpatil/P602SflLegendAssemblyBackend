package com.SflLegendAssembly.queries;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public final class ProductionSummaryQueries {

  private static final Map<String, String> TABLES = Map.of(
      "LH", "machine50lh",
      "RH", "machine50rh");

  private ProductionSummaryQueries() {
  }

  public static boolean supportsSide(String side) {
    return TABLES.containsKey(side);
  }

  public static PreparedQuery findLatestByDateAndShift(
      String side,
      LocalDate startDate,
      LocalDate endDate) {
    String table = TABLES.get(side);
    if (table == null) {
      throw new IllegalArgumentException("Unsupported component side.");
    }

    String sql = """
        SELECT production_date, shift, ok_count, not_ok_count, total_part_count
        FROM (
            SELECT cycle_start_time::date AS production_date, shift, ok_count, not_ok_count,
                   total_part_count,
                   ROW_NUMBER() OVER (
                       PARTITION BY cycle_start_time::date, shift
                       ORDER BY cycle_start_time DESC, id DESC
                   ) AS row_rank
            FROM %s
            WHERE cycle_start_time >= ? AND cycle_start_time < ?
        ) latest_rows
        WHERE row_rank = 1
        ORDER BY production_date DESC, shift ASC
        """.formatted(table);

    return new PreparedQuery(
        sql,
        List.of(startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay()));
  }

  public record PreparedQuery(String sql, List<Object> parameters) {
  }
}
