package com.SflLegendAssembly.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.SflLegendAssembly.dtos.LiveMachineStatus;

class MachineStatusCsvWriterTest {

  @Test
  void writesAllReportColumnsAndEscapesCsvValues() {
    LiveMachineStatus row = new LiveMachineStatus(
        "30A-LH", null, null, null, "part, \"A\"", null, 2,
        null, null, null, null, null, null, null, null,
        null, null, null, null, null, null, null, null,
        null, null, null, null, null, null, null, null,
        null, null, null, null, null, null);

    String csv = MachineStatusCsvWriter.write(List.of(row));

    assertThat(csv)
        .startsWith("machineCode,entityName,id,finalStatus,barcode,operatorName,shift,")
        .contains("\"part, \"\"A\"\"\"")
        .endsWith("\r\n");
  }
}
