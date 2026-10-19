package com.SflLegendAssembly.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.SflLegendAssembly.dtos.LiveMachineStatus;
import com.SflLegendAssembly.services.LiveDataservice;

@RestController
@RequestMapping("/api/live-data")
public class LiveDataController {

  private final LiveDataservice liveDataService;

  public LiveDataController(LiveDataservice liveDataService) {
    this.liveDataService = liveDataService;
  }

  @GetMapping("/machines")
  public List<LiveMachineStatus> getLatestMachineStatuses() {
    return liveDataService.getLatestMachineStatuses();
  }
}
