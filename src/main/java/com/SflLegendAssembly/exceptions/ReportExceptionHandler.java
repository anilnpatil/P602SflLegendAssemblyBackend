package com.SflLegendAssembly.exceptions;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.SflLegendAssembly.controllers.ReportController;
import com.SflLegendAssembly.controllers.OverallReportController;

@RestControllerAdvice(assignableTypes = {ReportController.class, OverallReportController.class})
public class ReportExceptionHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(ReportExceptionHandler.class);

  @ExceptionHandler(DataAccessException.class)
  public ProblemDetail handleDataAccessException(DataAccessException exception) {
    LOGGER.error("Unable to retrieve machine report data from the database", exception);
    ProblemDetail problem = ProblemDetail.forStatusAndDetail(
        HttpStatus.SERVICE_UNAVAILABLE,
        "Machine report data is temporarily unavailable.");
    problem.setTitle("Report data unavailable");
    problem.setProperty("timestamp", Instant.now());
    return problem;
  }
}
