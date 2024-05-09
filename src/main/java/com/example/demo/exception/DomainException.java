package com.example.demo.exception;

import com.example.demo.exception.model.DomainCode;

public class DomainException extends RuntimeException {

  private final DomainCode domainCode;

  private transient Object metadata;

  public DomainException(DomainCode domainCode, Object... args) {
    super(String.format(domainCode.getMessage(), args));
    this.domainCode = domainCode;
  }

  public DomainException(DomainCode domainCode, Object metadata, Object... args) {
    super(String.format(domainCode.getMessage(), args));
    this.domainCode = domainCode;
    this.metadata = metadata;
  }

  public DomainCode getDomainCode() {
    return domainCode;
  }

  public Object getMetadata() {
    return metadata;
  }
}
