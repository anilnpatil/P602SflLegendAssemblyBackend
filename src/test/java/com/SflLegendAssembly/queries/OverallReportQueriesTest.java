package com.SflLegendAssembly.queries;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class OverallReportQueriesTest {

  @Test
  void combinedReportUsesAllMachinesAndOnlyCompletedBarcodes() {
    var prepared = OverallReportQueries.findCompletedComponents(
        "BOTH",
        LocalDate.of(2026, 10, 1),
        LocalDate.of(2026, 10, 20),
        "P05552127",
        2);
    String query = prepared.sql();

    assertThat(query)
        .contains("FROM machine50lh")
        .contains("FROM machine50rh")
        .contains("FROM machine30alh")
        .contains("FROM machine30arh")
        .contains("FROM machine30b")
        .contains("FROM machine40alh")
        .contains("FROM machine40arh")
        .contains("FROM machine40b")
        .contains("INNER JOIN completed_parts")
        .contains("WHERE cycle_start_time >= ? AND cycle_start_time < ?")
        .contains("UPPER(?)")
        .contains("AND shift = ?")
        .contains("ROW_NUMBER() OVER")
        .contains("PARTITION BY part_barcode")
        .contains("p.component_side = 'LH'")
        .contains("air_gauge1_actual AS air_gauge1_actual")
        .contains("actual_displacement AS actual_displacement");
    assertThat(prepared.parameters()).hasSize(8);
  }

  @Test
  void sideReportsIncludeCommonStationsAndExcludeOppositeSideStations() {
    var left = OverallReportQueries.findCompletedComponents(
        "LH", LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 20), null, null);
    var right = OverallReportQueries.findCompletedComponents(
        "RH", LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 20), null, null);
    String leftQuery = left.sql();
    String rightQuery = right.sql();

    assertThat(leftQuery)
        .contains("FROM machine30alh")
        .contains("FROM machine40alh")
        .contains("FROM machine50lh")
        .contains("FROM machine30b")
        .contains("FROM machine40b")
        .doesNotContain("SELECT '30A-RH'")
        .doesNotContain("SELECT '40A-RH'")
        .doesNotContain("SELECT '50-RH'");
    assertThat(rightQuery)
        .contains("FROM machine30arh")
        .contains("FROM machine40arh")
        .contains("FROM machine50rh")
        .contains("FROM machine30b")
        .contains("FROM machine40b")
        .doesNotContain("SELECT '30A-LH'")
        .doesNotContain("SELECT '40A-LH'")
        .doesNotContain("SELECT '50-LH'");
    assertThat(left.parameters()).hasSize(2);
    assertThat(right.parameters()).hasSize(2);
  }

  @Test
  void unsupportedSidesAreRejected() {
    assertThat(OverallReportQueries.supportsSide("LH")).isTrue();
    assertThat(OverallReportQueries.supportsSide("RH")).isTrue();
    assertThat(OverallReportQueries.supportsSide("BOTH")).isTrue();
    assertThatIllegalArgumentException()
        .isThrownBy(() -> OverallReportQueries.findCompletedComponents(
            "LH; DROP TABLE machine50lh",
            LocalDate.of(2026, 10, 1),
            LocalDate.of(2026, 10, 20),
            null,
            null));
  }
}
