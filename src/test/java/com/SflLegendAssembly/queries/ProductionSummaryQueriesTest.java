package com.SflLegendAssembly.queries;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class ProductionSummaryQueriesTest {

  @Test
  void productionSummaryUsesTheCorrectSideTableAndReturnsLatestShiftRows() {
    var left = ProductionSummaryQueries.findLatestByDateAndShift(
        "LH", LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 31));
    var right = ProductionSummaryQueries.findLatestByDateAndShift(
        "RH", LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 31));

    assertThat(left.sql())
        .contains("FROM machine50lh")
        .contains("PARTITION BY cycle_start_time::date, shift")
        .contains("ORDER BY cycle_start_time DESC, id DESC")
        .contains("WHERE cycle_start_time >= ? AND cycle_start_time < ?")
        .contains("ok_count, not_ok_count, total_part_count")
        .doesNotContain("machine50rh");
    assertThat(right.sql()).contains("FROM machine50rh").doesNotContain("machine50lh");
    assertThat(left.parameters())
        .containsExactly(
            LocalDate.of(2026, 10, 1).atStartOfDay(),
            LocalDate.of(2026, 11, 1).atStartOfDay());
  }

  @Test
  void unsupportedSidesAreRejectedRatherThanInterpolatedIntoSql() {
    assertThat(ProductionSummaryQueries.supportsSide("LH")).isTrue();
    assertThat(ProductionSummaryQueries.supportsSide("RH")).isTrue();
    assertThat(ProductionSummaryQueries.supportsSide("LH; DROP TABLE machine50lh")).isFalse();
    assertThatIllegalArgumentException()
        .isThrownBy(() -> ProductionSummaryQueries.findLatestByDateAndShift(
            "LH; DROP TABLE machine50lh",
            LocalDate.of(2026, 10, 1),
            LocalDate.of(2026, 10, 31)));
  }
}
