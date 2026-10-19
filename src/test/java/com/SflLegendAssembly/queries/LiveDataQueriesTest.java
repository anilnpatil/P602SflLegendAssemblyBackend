package com.SflLegendAssembly.queries;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class LiveDataQueriesTest {

  @Test
  void latestMachineQueryIncludesEveryMachineAndSelectsOnlyItsNewestLog() {
    assertThat(LiveDataQueries.FIND_LATEST_MACHINE_STATUSES)
        .contains("FROM machine30alh")
        .contains("FROM machine30arh")
        .contains("FROM machine30b")
        .contains("FROM machine40alh")
        .contains("FROM machine40arh")
        .contains("FROM machine40b")
        .contains("FROM machine50lh")
        .contains("FROM machine50rh")
        .contains("'Machine30ALh' AS entity_name")
        .contains("'Machine30ARh' AS entity_name")
        .contains("'Machine30B' AS entity_name")
        .contains("'Machine40ALh' AS entity_name")
        .contains("'Machine40ARh' AS entity_name")
        .contains("'Machine40B' AS entity_name")
        .contains("'Machine50Lh' AS entity_name")
        .contains("'Machine50Rh' AS entity_name")
        .contains("SELECT machine_code, entity_name, id, final_status")
        .contains("air_gauge1_min, air_gauge1_actual, air_gauge1_max")
        .contains("air_gauge2_min, air_gauge2_actual, air_gauge2_max")
        .contains("outer_dia_min, outer_dia_actual, outer_dia_max")
        .contains("air_gauge1_status")
        .contains("air_gauge2_status")
        .contains("outer_dia_status")
        .contains("inner_dia_status")
        .contains("load_status")
        .contains("displacement_status")
        .contains("inner_dia_min, inner_dia_actual, inner_dia_max")
        .contains("mini_load, actual_load, max_load, load_status")
        .contains("min_displacement, actual_displacement, max_displacement, displacement_status")
        .contains("ROW_NUMBER() OVER (PARTITION BY machine_code ORDER BY id DESC)")
        .contains("WHERE row_number = 1");
  }
}
