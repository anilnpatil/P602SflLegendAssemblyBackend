package com.SflLegendAssembly.queries;

import java.util.Map;

public final class MachineReportQueries {

  private static final Map<String, MachineTable> MACHINES = Map.of(
      "30A-LH", new MachineTable("30A-LH", "machine30alh", "Machine30ALh", false, true, false),
      "30A-RH", new MachineTable("30A-RH", "machine30arh", "Machine30ARh", false, true, false),
      "30B", new MachineTable("30B", "machine30b", "Machine30B", false, false, true),
      "40A-LH", new MachineTable("40A-LH", "machine40alh", "Machine40ALh", true, true, false),
      "40A-RH", new MachineTable("40A-RH", "machine40arh", "Machine40ARh", true, true, false),
      "40B", new MachineTable("40B", "machine40b", "Machine40B", false, false, true),
      "50-LH", new MachineTable("50-LH", "machine50lh", "Machine50Lh", false, true, false),
      "50-RH", new MachineTable("50-RH", "machine50rh", "Machine50Rh", false, true, false));

  private MachineReportQueries() {
  }

  public static boolean supportsMachine(String machineCode) {
    return MACHINES.containsKey(machineCode);
  }

  public static String findByPartId(String machineCode) {
    MachineTable machine = requireMachine(machineCode);
    return select(machine)
        + " WHERE UPPER(REGEXP_REPLACE(barcode, '^[[:space:]]+|[[:space:]]+$', '', 'g')) = UPPER(?)"
        + " ORDER BY cycle_start_time DESC, id DESC";
  }

  public static String findAll(String machineCode) {
    MachineTable machine = requireMachine(machineCode);
    return select(machine) + " ORDER BY cycle_start_time DESC, id DESC";
  }

  public static String findByDateRange(String machineCode) {
    MachineTable machine = requireMachine(machineCode);
    return select(machine)
        + " WHERE cycle_start_time >= ? AND cycle_start_time < ?"
        + " ORDER BY cycle_start_time DESC, id DESC";
  }

  private static MachineTable requireMachine(String machineCode) {
    MachineTable machine = MACHINES.get(machineCode);
    if (machine == null) {
      throw new IllegalArgumentException("Unsupported machine code.");
    }
    return machine;
  }

  private static String select(MachineTable machine) {
    String airGauge1 = operationValues(machine.hasAirGauges(),
        "air_gauge1_min", "air_gauge1_actual", "air_gauge1_max", "air_gauge1_status");
    String airGauge2 = operationValues(machine.hasAirGauges(),
        "air_gauge2_min", "air_gauge2_actual", "air_gauge2_max", "air_gauge2_status");
    String outerDiameter = operationValues(machine.hasDiameterChecks(),
        "outer_dia_min", "outer_dia_actual", "outer_dia_max", "outer_dia_status");
    String innerDiameter = operationValues(machine.hasDiameterChecks(),
        "inner_dia_min", "inner_dia_actual", "inner_dia_max", "inner_dia_status");
    String load = operationValues(machine.hasLoadChecks(),
        "mini_load", "actual_load", "max_load", "load_status");
    String displacement = operationValues(machine.hasLoadChecks(),
        "min_displacement", "actual_displacement", "max_displacement", "displacement_status");

    return """
        SELECT '%s' AS machine_code, '%s' AS entity_name, id, final_status, barcode, operator_name,
               shift, %s, %s, %s, %s, %s, %s,
               ok_count, not_ok_count, total_part_count, cycle_time, cycle_start_time, cycle_end_time
        FROM %s
        """.formatted(machine.machineCode(), machine.entityName(), airGauge1, airGauge2,
        outerDiameter, innerDiameter, load, displacement, machine.tableName());
  }

  private static String operationValues(boolean present, String... columnNames) {
    return java.util.Arrays.stream(columnNames)
        .map(columnName -> {
          String type = columnName.endsWith("_status") ? "integer" : "real";
          String expression = present ? columnName : "NULL::" + type;
          return expression + " AS " + columnName;
        })
        .collect(java.util.stream.Collectors.joining(", "));
  }

  private record MachineTable(
      String machineCode,
      String tableName,
      String entityName,
      boolean hasDiameterChecks,
      boolean hasAirGauges,
      boolean hasLoadChecks) {
  }
}
