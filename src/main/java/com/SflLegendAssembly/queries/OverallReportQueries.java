package com.SflLegendAssembly.queries;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class OverallReportQueries {

  private static final Map<String, MachineTable> MACHINES = Map.of(
      "30A-LH", new MachineTable("30A-LH", "machine30alh", "Machine30ALh", true, false, false),
      "30A-RH", new MachineTable("30A-RH", "machine30arh", "Machine30ARh", true, false, false),
      "30B", new MachineTable("30B", "machine30b", "Machine30B", false, false, true),
      "40A-LH", new MachineTable("40A-LH", "machine40alh", "Machine40ALh", true, true, false),
      "40A-RH", new MachineTable("40A-RH", "machine40arh", "Machine40ARh", true, true, false),
      "40B", new MachineTable("40B", "machine40b", "Machine40B", false, false, true),
      "50-LH", new MachineTable("50-LH", "machine50lh", "Machine50Lh", true, false, false),
      "50-RH", new MachineTable("50-RH", "machine50rh", "Machine50Rh", true, false, false));

  private static final List<String> MACHINE_ORDER = List.of(
      "30A-LH", "30A-RH", "30B", "40A-LH", "40A-RH", "40B", "50-LH", "50-RH");

  private OverallReportQueries() {
  }

  public static boolean supportsSide(String side) {
    return "LH".equals(side) || "RH".equals(side) || "BOTH".equals(side);
  }

  public static PreparedQuery findCompletedComponents(
      String side,
      LocalDate startDate,
      LocalDate endDate,
      String barcode,
      Integer shift) {
    if (!supportsSide(side)) {
      throw new IllegalArgumentException("Unsupported component side.");
    }
    boolean filterBarcode = barcode != null && !barcode.isBlank();
    boolean filterShift = shift != null;
    List<Object> parameters = new ArrayList<>();

    List<String> completionSides = switch (side) {
      case "LH" -> List.of("LH");
      case "RH" -> List.of("RH");
      default -> List.of("LH", "RH");
    };
    List<String> completionQueries = new ArrayList<>();
    for (String completionSide : completionSides) {
      String table = completionSide.equals("LH") ? "machine50lh" : "machine50rh";
      completionQueries.add(completionSelect(table, completionSide, filterBarcode, filterShift));
      addFilterParameters(parameters, startDate, endDate, barcode, shift, filterBarcode, filterShift);
    }

    List<String> machineQueries = new ArrayList<>();
    for (String machineCode : MACHINE_ORDER) {
      if (!includesMachine(side, machineCode)) {
        continue;
      }
      MachineTable machine = MACHINES.get(machineCode);
      machineQueries.add(selectMachine(machine));
    }

    String query = """
        WITH completion_rows AS (
        %s
        ),
        ranked_completions AS (
            SELECT part_barcode, source_barcode, component_side, shift, cycle_start_time, final_status,
                   ROW_NUMBER() OVER (
                       PARTITION BY part_barcode
                       ORDER BY cycle_start_time DESC, component_side ASC
                   ) AS completion_rank
            FROM completion_rows
        ),
        completed_parts AS (
            SELECT part_barcode, source_barcode, component_side, shift, cycle_start_time, final_status
            FROM ranked_completions
            WHERE completion_rank = 1
        ),
        component_rows AS (
        %s
        )
        SELECT c.machine_code, c.entity_name, c.id, c.final_status, c.barcode, c.operator_name,
               c.shift, c.air_gauge1_min, c.air_gauge1_actual, c.air_gauge1_max, c.air_gauge1_status,
               c.air_gauge2_min, c.air_gauge2_actual, c.air_gauge2_max, c.air_gauge2_status,
               c.outer_dia_min, c.outer_dia_actual, c.outer_dia_max, c.outer_dia_status,
               c.inner_dia_min, c.inner_dia_actual, c.inner_dia_max, c.inner_dia_status,
               c.mini_load, c.actual_load, c.max_load, c.load_status,
               c.min_displacement, c.actual_displacement, c.max_displacement, c.displacement_status,
               c.ok_count, c.not_ok_count, c.total_part_count, c.cycle_time,
               c.cycle_start_time, c.cycle_end_time
        FROM component_rows c
        ORDER BY c.barcode, c.machine_code, c.cycle_start_time DESC, c.id DESC
        """.formatted(String.join("\nUNION ALL\n", completionQueries),
        String.join("\nUNION ALL\n", machineQueries));
    return new PreparedQuery(query, List.copyOf(parameters));
  }

  private static String completionSelect(
      String tableName,
      String side,
      boolean filterBarcode,
      boolean filterShift) {
    StringBuilder query = new StringBuilder("""
        (
        SELECT DISTINCT ON (UPPER(REGEXP_REPLACE(barcode, '^[[:space:]]+|[[:space:]]+$', '', 'g')))
               UPPER(REGEXP_REPLACE(barcode, '^[[:space:]]+|[[:space:]]+$', '', 'g')) AS part_barcode,
               barcode AS source_barcode, '%s' AS component_side, shift, cycle_start_time, final_status
        FROM %s
        WHERE cycle_start_time >= ? AND cycle_start_time < ?
        """.formatted(side, tableName));
    appendOptionalFilters(query, filterBarcode, filterShift);
    query.append("""
        ORDER BY UPPER(REGEXP_REPLACE(barcode, '^[[:space:]]+|[[:space:]]+$', '', 'g')),
                 cycle_start_time DESC, id DESC
        )
        """);
    return query.toString();
  }

  private static String selectMachine(MachineTable machine) {
    return """
        SELECT '%s' AS machine_code, '%s' AS entity_name, m.id, m.final_status, m.barcode, m.operator_name,
               m.shift, %s, %s, %s, %s, %s, %s,
               m.ok_count, m.not_ok_count, m.total_part_count, m.cycle_time, m.cycle_start_time, m.cycle_end_time
        FROM %s m
        INNER JOIN completed_parts p
          ON p.source_barcode = m.barcode
         AND (%s)
        """.formatted(
        machine.machineCode(),
        machine.entityName(),
        operationValues(machine.hasAirGauges(),
            "air_gauge1_min", "air_gauge1_actual", "air_gauge1_max", "air_gauge1_status"),
        operationValues(machine.hasAirGauges(),
            "air_gauge2_min", "air_gauge2_actual", "air_gauge2_max", "air_gauge2_status"),
        operationValues(machine.hasDiameterChecks(),
            "outer_dia_min", "outer_dia_actual", "outer_dia_max", "outer_dia_status"),
        operationValues(machine.hasDiameterChecks(),
            "inner_dia_min", "inner_dia_actual", "inner_dia_max", "inner_dia_status"),
        operationValues(machine.hasLoadChecks(),
            "mini_load", "actual_load", "max_load", "load_status"),
        operationValues(machine.hasLoadChecks(),
            "min_displacement", "actual_displacement", "max_displacement", "displacement_status"),
        machine.tableName(),
        isCommonMachine(machine.machineCode())
            ? "TRUE"
            : "p.component_side = '" + machine.machineCode().substring(machine.machineCode().length() - 2) + "'");
  }

  private static void appendOptionalFilters(
      StringBuilder query,
      boolean filterBarcode,
      boolean filterShift) {
    if (filterBarcode) {
      query.append("""
             AND UPPER(REGEXP_REPLACE(barcode, '^[[:space:]]+|[[:space:]]+$', '', 'g')) = UPPER(?)
            """);
    }
    if (filterShift) {
      query.append("   AND shift = ?\n");
    }
  }

  private static void addFilterParameters(
      List<Object> parameters,
      LocalDate startDate,
      LocalDate endDate,
      String barcode,
      Integer shift,
      boolean filterBarcode,
      boolean filterShift) {
    parameters.add(startDate.atStartOfDay());
    parameters.add(endDate.plusDays(1).atStartOfDay());
    if (filterBarcode) {
      parameters.add(barcode.trim());
    }
    if (filterShift) {
      parameters.add(shift);
    }
  }

  private static boolean includesMachine(String side, String machineCode) {
    return "BOTH".equals(side) || isCommonMachine(machineCode)
        || machineCode.endsWith("-" + side);
  }

  private static boolean isCommonMachine(String machineCode) {
    return "30B".equals(machineCode) || "40B".equals(machineCode);
  }

  private static String operationValues(boolean present, String... columnNames) {
    return java.util.Arrays.stream(columnNames)
        .map(columnName -> {
          String type = columnName.endsWith("_status") ? "integer" : "real";
          String expression = present ? "m." + columnName : "NULL::" + type;
          return expression + " AS " + columnName;
        })
        .collect(java.util.stream.Collectors.joining(", "));
  }

  private record MachineTable(
      String machineCode,
      String tableName,
      String entityName,
      boolean hasAirGauges,
      boolean hasDiameterChecks,
      boolean hasLoadChecks) {
  }

  public record PreparedQuery(String sql, List<Object> parameters) {
  }
}
