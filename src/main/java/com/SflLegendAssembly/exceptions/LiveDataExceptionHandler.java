package com.SflLegendAssembly.exceptions;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.SflLegendAssembly.controllers.LiveDataController;

@RestControllerAdvice(assignableTypes = LiveDataController.class)
public class LiveDataExceptionHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(LiveDataExceptionHandler.class);

  @ExceptionHandler(DataAccessException.class)
  public ProblemDetail handleDataAccessException(DataAccessException exception) {
    LOGGER.error("Unable to retrieve live machine status from the database", exception);
    ProblemDetail problem = ProblemDetail.forStatusAndDetail(
        HttpStatus.SERVICE_UNAVAILABLE,
        "Live machine status is temporarily unavailable.");
    problem.setTitle("Live data unavailable");
    problem.setProperty("timestamp", Instant.now());
    return problem;
  }
}
