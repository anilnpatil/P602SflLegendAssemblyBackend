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
@Table(name = "machine30b")
public class Machine30B {
  @Id 
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "barcode", length = 25, unique = true, nullable = false)
  private String barcode;
  @Column (name = "operator_name", length = 16)
  private String operatorName;
  private Integer shift;
  @Column (name = "cycle_start_time")
  private LocalDateTime cycleStartTime;  
  @Column (name = "mini_load")
  private Float miniLoad;
  @Column (name = "actual_load")
  private Float actualLoad;
  @Column (name = "max_load")
  private Float maxLoad;
  @Column (name = "load_status")
  private Integer loadStatus;
 @Column (name = "min_displacement")
  private Float minDisplacement;
  @Column (name = "actual_displacement")
  private Float actualDisplacement;
  @Column (name = "max_displacement")
  private Float maxDisplacement;
  @Column (name = "displacement_status")
  private Integer displacementStatus; 
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
