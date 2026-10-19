package com.SflLegendAssembly.queries;

public final class LiveDataQueries {

  private LiveDataQueries() {
  }

  public static final String FIND_LATEST_MACHINE_STATUSES = """
      WITH machine_logs AS (
          SELECT '30A-LH' AS machine_code, 'Machine30ALh' AS entity_name,
                 id, final_status, barcode, operator_name,
                 shift, ok_count, not_ok_count, total_part_count, cycle_time,
                 cycle_start_time, cycle_end_time,
                 air_gauge1_min, air_gauge1_actual, air_gauge1_max, air_gauge1_status,
                 air_gauge2_min, air_gauge2_actual, air_gauge2_max, air_gauge2_status,
                 NULL::real AS outer_dia_min, NULL::real AS outer_dia_actual,
                 NULL::real AS outer_dia_max, NULL::integer AS outer_dia_status,
                 NULL::real AS inner_dia_min, NULL::real AS inner_dia_actual,
                 NULL::real AS inner_dia_max, NULL::integer AS inner_dia_status,
                 NULL::real AS mini_load, NULL::real AS actual_load,
                 NULL::real AS max_load, NULL::integer AS load_status,
                 NULL::real AS min_displacement, NULL::real AS actual_displacement,
                 NULL::real AS max_displacement, NULL::integer AS displacement_status
          FROM machine30alh
          UNION ALL
          SELECT '30A-RH' AS machine_code, 'Machine30ARh' AS entity_name,
                 id, final_status, barcode, operator_name,
                 shift, ok_count, not_ok_count, total_part_count, cycle_time,
                 cycle_start_time, cycle_end_time,
                 air_gauge1_min, air_gauge1_actual, air_gauge1_max, air_gauge1_status,
                 air_gauge2_min, air_gauge2_actual, air_gauge2_max, air_gauge2_status,
                 NULL::real AS outer_dia_min, NULL::real AS outer_dia_actual,
                 NULL::real AS outer_dia_max, NULL::integer AS outer_dia_status,
                 NULL::real AS inner_dia_min, NULL::real AS inner_dia_actual,
                 NULL::real AS inner_dia_max, NULL::integer AS inner_dia_status,
                 NULL::real AS mini_load, NULL::real AS actual_load,
                 NULL::real AS max_load, NULL::integer AS load_status,
                 NULL::real AS min_displacement, NULL::real AS actual_displacement,
                 NULL::real AS max_displacement, NULL::integer AS displacement_status
          FROM machine30arh
          UNION ALL
          SELECT '30B' AS machine_code, 'Machine30B' AS entity_name,
                 id, final_status, barcode, operator_name,
                 shift, ok_count, not_ok_count, total_part_count, cycle_time,
                 cycle_start_time, cycle_end_time,
                 NULL::real AS air_gauge1_min, NULL::real AS air_gauge1_actual,
                 NULL::real AS air_gauge1_max, NULL::integer AS air_gauge1_status,
                 NULL::real AS air_gauge2_min, NULL::real AS air_gauge2_actual,
                 NULL::real AS air_gauge2_max, NULL::integer AS air_gauge2_status,
                 NULL::real AS outer_dia_min, NULL::real AS outer_dia_actual,
                 NULL::real AS outer_dia_max, NULL::integer AS outer_dia_status,
                 NULL::real AS inner_dia_min, NULL::real AS inner_dia_actual,
                 NULL::real AS inner_dia_max, NULL::integer AS inner_dia_status,
                 mini_load, actual_load, max_load, load_status,
                 min_displacement, actual_displacement, max_displacement, displacement_status
          FROM machine30b
          UNION ALL
          SELECT '40A-LH' AS machine_code, 'Machine40ALh' AS entity_name,
                 id, final_status, barcode, operator_name,
                 shift, ok_count, not_ok_count, total_part_count, cycle_time,
                 cycle_start_time, cycle_end_time,
                 air_gauge1_min, air_gauge1_actual, air_gauge1_max, air_gauge1_status,
                 air_gauge2_min, air_gauge2_actual, air_gauge2_max, air_gauge2_status,
                 outer_dia_min, outer_dia_actual, outer_dia_max, outer_dia_status,
                 inner_dia_min, inner_dia_actual, inner_dia_max, inner_dia_status,
                 NULL::real AS mini_load, NULL::real AS actual_load,
                 NULL::real AS max_load, NULL::integer AS load_status,
                 NULL::real AS min_displacement, NULL::real AS actual_displacement,
                 NULL::real AS max_displacement, NULL::integer AS displacement_status
          FROM machine40alh
          UNION ALL
          SELECT '40A-RH' AS machine_code, 'Machine40ARh' AS entity_name,
                 id, final_status, barcode, operator_name,
                 shift, ok_count, not_ok_count, total_part_count, cycle_time,
                 cycle_start_time, cycle_end_time,
                 air_gauge1_min, air_gauge1_actual, air_gauge1_max, air_gauge1_status,
                 air_gauge2_min, air_gauge2_actual, air_gauge2_max, air_gauge2_status,
                 outer_dia_min, outer_dia_actual, outer_dia_max, outer_dia_status,
                 inner_dia_min, inner_dia_actual, inner_dia_max, inner_dia_status,
                 NULL::real AS mini_load, NULL::real AS actual_load,
                 NULL::real AS max_load, NULL::integer AS load_status,
                 NULL::real AS min_displacement, NULL::real AS actual_displacement,
                 NULL::real AS max_displacement, NULL::integer AS displacement_status
          FROM machine40arh
          UNION ALL
          SELECT '40B' AS machine_code, 'Machine40B' AS entity_name,
                 id, final_status, barcode, operator_name,
                 shift, ok_count, not_ok_count, total_part_count, cycle_time,
                 cycle_start_time, cycle_end_time,
                 NULL::real AS air_gauge1_min, NULL::real AS air_gauge1_actual,
                 NULL::real AS air_gauge1_max, NULL::integer AS air_gauge1_status,
                 NULL::real AS air_gauge2_min, NULL::real AS air_gauge2_actual,
                 NULL::real AS air_gauge2_max, NULL::integer AS air_gauge2_status,
                 NULL::real AS outer_dia_min, NULL::real AS outer_dia_actual,
                 NULL::real AS outer_dia_max, NULL::integer AS outer_dia_status,
                 NULL::real AS inner_dia_min, NULL::real AS inner_dia_actual,
                 NULL::real AS inner_dia_max, NULL::integer AS inner_dia_status,
                 mini_load, actual_load, max_load, load_status,
                 min_displacement, actual_displacement, max_displacement, displacement_status
          FROM machine40b
          UNION ALL
          SELECT '50-LH' AS machine_code, 'Machine50Lh' AS entity_name,
                 id, final_status, barcode, operator_name,
                 shift, ok_count, not_ok_count, total_part_count, cycle_time,
                 cycle_start_time, cycle_end_time,
                 air_gauge1_min, air_gauge1_actual, air_gauge1_max, air_gauge1_status,
                 air_gauge2_min, air_gauge2_actual, air_gauge2_max, air_gauge2_status,
                 NULL::real AS outer_dia_min, NULL::real AS outer_dia_actual,
                 NULL::real AS outer_dia_max, NULL::integer AS outer_dia_status,
                 NULL::real AS inner_dia_min, NULL::real AS inner_dia_actual,
                 NULL::real AS inner_dia_max, NULL::integer AS inner_dia_status,
                 NULL::real AS mini_load, NULL::real AS actual_load,
                 NULL::real AS max_load, NULL::integer AS load_status,
                 NULL::real AS min_displacement, NULL::real AS actual_displacement,
                 NULL::real AS max_displacement, NULL::integer AS displacement_status
          FROM machine50lh
          UNION ALL
          SELECT '50-RH' AS machine_code, 'Machine50Rh' AS entity_name,
                 id, final_status, barcode, operator_name,
                 shift, ok_count, not_ok_count, total_part_count, cycle_time,
                 cycle_start_time, cycle_end_time,
                 air_gauge1_min, air_gauge1_actual, air_gauge1_max, air_gauge1_status,
                 air_gauge2_min, air_gauge2_actual, air_gauge2_max, air_gauge2_status,
                 NULL::real AS outer_dia_min, NULL::real AS outer_dia_actual,
                 NULL::real AS outer_dia_max, NULL::integer AS outer_dia_status,
                 NULL::real AS inner_dia_min, NULL::real AS inner_dia_actual,
                 NULL::real AS inner_dia_max, NULL::integer AS inner_dia_status,
                 NULL::real AS mini_load, NULL::real AS actual_load,
                 NULL::real AS max_load, NULL::integer AS load_status,
                 NULL::real AS min_displacement, NULL::real AS actual_displacement,
                 NULL::real AS max_displacement, NULL::integer AS displacement_status
          FROM machine50rh
      ),
      ranked_machine_logs AS (
          SELECT machine_logs.*,
                 ROW_NUMBER() OVER (PARTITION BY machine_code ORDER BY id DESC) AS row_number
          FROM machine_logs
      )
      SELECT machine_code, entity_name, id, final_status, barcode, operator_name, shift,
             ok_count, not_ok_count, total_part_count, cycle_time,
             cycle_start_time, cycle_end_time,
             air_gauge1_min, air_gauge1_actual, air_gauge1_max, air_gauge1_status,
             air_gauge2_min, air_gauge2_actual, air_gauge2_max, air_gauge2_status,
             outer_dia_min, outer_dia_actual, outer_dia_max, outer_dia_status,
             inner_dia_min, inner_dia_actual, inner_dia_max, inner_dia_status,
             mini_load, actual_load, max_load, load_status,
             min_displacement, actual_displacement, max_displacement, displacement_status
      FROM ranked_machine_logs
      WHERE row_number = 1
      ORDER BY machine_code
      """;
}
