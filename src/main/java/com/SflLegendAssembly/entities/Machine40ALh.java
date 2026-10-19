package com.SflLegendAssembly.entities;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
@Entity 
@Table(name = "machine40alh")
public class Machine40ALh {
  @Id 
  @GeneratedValue (strategy = GenerationType.IDENTITY)
  private Long id;
  @Column (name = "barcode", length = 25, unique = true, nullable = false)
  private String barcode;
  @Column (name = "operator_name", length = 16)
  private String operatorName;
  private Integer shift;
  @Column (name = "cycle_start_time")
  private LocalDateTime cycleStartTime;
  @Column (name = "air_gauge1_min")  
  private Float airGauge1Min;
  @Column (name = "air_gauge1_actual")
  private Float airGauge1Actual; 
  @Column (name = "air_gauge1_max")
  private Float airGauge1Max;
  @Column (name = "air_gauge1_status")
  private Integer airGauge1Status;
  @Column (name = "air_gauge2_min")
  private Float airGauge2Min;
  @Column (name = "air_gauge2_actual")
  private Float airGauge2Actual;
  @Column (name = "air_gauge2_max")
  private Float airGauge2Max;
  @Column (name = "air_gauge2_status")
  private Integer airGauge2Status;
  @Column (name = "outer_dia_min")
  private Float outerDiaMin;
  @Column (name = "outer_dia_actual")
  private Float outerDiaActual;
  @Column (name = "outer_dia_max")
  private Float outerDiaMax;
  @Column (name = "outer_dia_status")
  private Integer outerDiaStatus;
  @Column (name = "inner_dia_min")
  private Float innerDiaMin;
  @Column (name = "inner_dia_actual")
  private Float innerDiaActual;
  @Column (name = "inner_dia_max")
  private Float innerDiaMax;
  @Column (name = "inner_dia_status")
  private Integer innerDiaStatus;
  @Column (name = "final_status")
  private Integer finalStatus;
  @Column(name = "ok_count")
  private Integer okCount;
  @Column(name = "not_ok_count")
  private Integer notOkCount;
  @Column(name = "total_part_count")
  private Integer totalPartCount;
  @Column(name = "cycle_time")
  private Integer cycleTime;
  @Column(name = "cycle_end_time")
  private LocalDateTime cycleEndTime;  
}
