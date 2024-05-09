package com.example.demo.exception;

import static io.swagger.v3.core.util.Constants.COMMA;
import static org.springframework.util.CollectionUtils.isEmpty;

import com.example.demo.exception.model.ApiError;
import com.example.demo.exception.model.ApiErrorEntry;
import com.example.demo.exception.model.DomainCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  public static final char DOT_SEPARATOR = '.';
  private static final int INDEX_CODE = 0;
  private static final int INDEX_VALUE = 1;

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ApiError handleMethodArgumentNotValidException(
      HttpServletRequest request,
      MethodArgumentNotValidException e) {

    List<ApiErrorEntry> apiErrorEntries = e
        .getBindingResult()
        .getFieldErrors()
        .stream()
        .map(this::useDetailErrorOrDefault)
        .collect(Collectors.toList());

    if (e.hasErrors() && isEmpty(apiErrorEntries)) {
      apiErrorEntries = List.of(createApiErrorEntry(DomainCode.INVALID_INPUT,
          String.format(DomainCode.INVALID_INPUT.getMessage()), null));
    }

    return new ApiError(apiErrorEntries);
  }

  private ApiErrorEntry useDetailErrorOrDefault(FieldError fieldError) {
    boolean isCustomerMessage = true;
    String defaultMessage = ObjectUtils.nullSafeToString(fieldError.getDefaultMessage());
    String[] errorInfo = defaultMessage.split(COMMA);
    DomainCode code = DomainCode.get(errorInfo[INDEX_CODE]);
    if (ObjectUtils.isEmpty(code)) {
      code = DomainCode.INVALID_INPUT_FIELD;
      isCustomerMessage = false;
    }
    return createApiErrorEntry(code,
        String.format(code.getMessage(),
            errorInfo.length > INDEX_VALUE && isCustomerMessage ? errorInfo[INDEX_VALUE]
                : fieldError.getField()),
        null);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(BindException.class)
  public ApiError handleBindException(
      HttpServletRequest request,
      BindException e) {

    List<ApiErrorEntry> apiErrorEntries = e
        .getFieldErrors()
        .stream()
        .map(fieldError -> createApiErrorEntry(DomainCode.INVALID_INPUT_FIELD,
            String.format(DomainCode.INVALID_INPUT_FIELD.getMessage(), fieldError.getField()),
            null))
        .collect(Collectors.toList());

    return new ApiError(apiErrorEntries);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(ConstraintViolationException.class)
  public ApiError handleConstraintViolationException(
      HttpServletRequest request,
      ConstraintViolationException e) {
    String value = e.getConstraintViolations().iterator().next().getMessage();
    DomainCode domainCode =
        DomainCode.get(value) == null ? DomainCode.INVALID_INPUT_FIELD : DomainCode.get(value);
    List<ApiErrorEntry> apiErrorEntries = e
        .getConstraintViolations()
        .stream()
        .map(fieldError -> {
              final String path = fieldError.getPropertyPath().toString();
              final String fieldName = StringUtils.substring(path, path.lastIndexOf(DOT_SEPARATOR) + 1);
              return createApiErrorEntry(
                  domainCode,
                  String.format(DomainCode.INVALID_INPUT_FIELD.getMessage(), fieldName), null);
            }
        )
        .collect(Collectors.toList());
    return new ApiError(apiErrorEntries);
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Exception.class)
  public ApiError handleException(
      HttpServletRequest request,
      Exception e) {
    log.error(e.getMessage(), e);

    return new ApiError(
        createApiErrorEntry(DomainCode.UNKNOWN_ERROR, DomainCode.UNKNOWN_ERROR.getMessage(), null));
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(DomainException.class)
  public ApiError handleDomainException(
      HttpServletRequest request,
      DomainException e) {
    return new ApiError(createApiErrorEntry(e.getDomainCode(), e.getMessage(), e.getMetadata()));
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MissingRequestHeaderException.class)
  public ApiError handleMissingRequestHeaderException(
      HttpServletRequest request,
      MissingRequestHeaderException e) {
    return new ApiError(
        createApiErrorEntry(DomainCode.MISSING_REQUEST_HEADER, e.getMessage(), null));
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ApiError handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
    return new ApiError(
        createApiErrorEntry(DomainCode.INVALID_INPUT_FIELD, e.getMessage(), null));
  }

  private ApiErrorEntry createApiErrorEntry(DomainCode domainCode, String message,
      Object metadata) {
    return ApiErrorEntry
        .builder()
        .errorCode(domainCode.toUniversalCode())
        .errorMessage(message)
        .metadata(metadata).build();
  }

  private ApiErrorEntry createApiErrorEntry(String errorCode, String message,
      Object metadata) {
    return ApiErrorEntry
        .builder()
        .errorCode(errorCode)
        .errorMessage(message)
        .metadata(metadata).build();
  }
}
