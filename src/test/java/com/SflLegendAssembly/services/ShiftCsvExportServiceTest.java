package com.SflLegendAssembly.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class ShiftCsvExportServiceTest {

  @Test
  void determinesTheCompletedShiftWindowAndUsesItsCompletionDate() {
    LocalDateTime shiftThreeCompleted = LocalDateTime.of(2026, 10, 23, 6, 5);
    ShiftCsvExportService.ShiftWindow shiftThree =
        ShiftCsvExportService.ShiftWindow.completedAt(shiftThreeCompleted);
    assertThat(shiftThree.shiftNumber()).isEqualTo(3);
    assertThat(shiftThree.reportDate()).isEqualTo(LocalDate.of(2026, 10, 23));
    assertThat(shiftThree.startDateTime()).isEqualTo(LocalDateTime.of(2026, 10, 22, 22, 0));
    assertThat(shiftThree.endDateTime()).isEqualTo(LocalDateTime.of(2026, 10, 23, 6, 0));

    ShiftCsvExportService.ShiftWindow shiftOne =
        ShiftCsvExportService.ShiftWindow.completedAt(LocalDateTime.of(2026, 10, 23, 14, 5));
    assertThat(shiftOne.shiftNumber()).isEqualTo(1);
    assertThat(shiftOne.startDateTime()).isEqualTo(LocalDateTime.of(2026, 10, 23, 6, 0));
    assertThat(shiftOne.endDateTime()).isEqualTo(LocalDateTime.of(2026, 10, 23, 14, 0));

    ShiftCsvExportService.ShiftWindow shiftTwo =
        ShiftCsvExportService.ShiftWindow.completedAt(LocalDateTime.of(2026, 10, 23, 22, 5));
    assertThat(shiftTwo.shiftNumber()).isEqualTo(2);
    assertThat(shiftTwo.startDateTime()).isEqualTo(LocalDateTime.of(2026, 10, 23, 14, 0));
    assertThat(shiftTwo.endDateTime()).isEqualTo(LocalDateTime.of(2026, 10, 23, 22, 0));
  }
}
