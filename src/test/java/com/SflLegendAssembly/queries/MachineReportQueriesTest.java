package com.SflLegendAssembly.queries;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.Test;

class MachineReportQueriesTest {

  @Test
  void reportQueriesUseTheRequestedMachineTableAndParameterizedFilters() {
    assertThat(MachineReportQueries.findAll("30A-LH"))
        .contains("FROM machine30alh")
        .contains("ORDER BY cycle_start_time DESC, id DESC")
        .contains("air_gauge1_actual AS air_gauge1_actual")
        .contains("air_gauge2_actual AS air_gauge2_actual");

    assertThat(MachineReportQueries.findByPartId("40A-LH"))
        .contains("FROM machine40alh")
        .contains("'40A-LH' AS machine_code")
        .contains("WHERE UPPER(REGEXP_REPLACE(barcode, '^[[:space:]]+|[[:space:]]+$', '', 'g')) = UPPER(?)")
        .doesNotContain("cycle_start_time >=")
        .contains("outer_dia_min AS outer_dia_min")
        .contains("outer_dia_status AS outer_dia_status")
        .contains("inner_dia_min AS inner_dia_min")
        .contains("inner_dia_status AS inner_dia_status");

    assertThat(MachineReportQueries.findByDateRange("30B"))
        .contains("FROM machine30b")
        .contains("WHERE cycle_start_time >= ? AND cycle_start_time < ?")
        .contains("mini_load AS mini_load")
        .contains("actual_load AS actual_load")
        .contains("max_load AS max_load")
        .contains("load_status AS load_status")
        .contains("min_displacement AS min_displacement")
        .contains("actual_displacement AS actual_displacement")
        .contains("max_displacement AS max_displacement")
        .contains("displacement_status AS displacement_status")
        .contains("NULL::real AS air_gauge1_min");
  }

  @Test
  void reportsRejectUnknownMachineCodesRatherThanInterpolatingThemIntoSql() {
    assertThat(MachineReportQueries.supportsMachine("30A-LH")).isTrue();
    assertThat(MachineReportQueries.supportsMachine("machine30alh; DROP TABLE machine30alh"))
        .isFalse();
    assertThatIllegalArgumentException()
        .isThrownBy(() -> MachineReportQueries.findByPartId("machine30alh; DROP TABLE machine30alh"));
  }
}
