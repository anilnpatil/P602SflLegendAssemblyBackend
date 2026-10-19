package com.SflLegendAssembly.services;

import java.util.List;

import com.SflLegendAssembly.dtos.LiveMachineStatus;

public interface LiveDataservice {
  List<LiveMachineStatus> getLatestMachineStatuses();
}
