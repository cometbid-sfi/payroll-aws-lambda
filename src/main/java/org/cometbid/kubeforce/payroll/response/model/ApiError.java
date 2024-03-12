/*
 * The MIT License
 *
 * Copyright 2024 samueladebowale.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.cometbid.kubeforce.payroll.response.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import jakarta.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

/**
 *
 * @author samueladebowale
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = false)
public class ApiError extends ApiResponse implements Serializable {

    /**
     * Application error code, which is different from HTTP error code.
     */
    @JsonProperty("errorCode")
    private String errorCode;

    @JsonProperty("debugMessage")
    private String debugMessage;

    @JsonProperty("statusCode")
    private int statusCode;

    @JsonProperty("errorDetails")
    private List<ApiSubError> subErrors;

    public static ApiError create(String uri, String method,
            String errorCode, String status, int statusCode,
            String message, String detailMessage) {

        return ApiError.builder()
                .path(uri)
                .reqMethod(method)
                .errorCode(errorCode)
                .httpCode(statusCode)
                .status(status)
                .shortMessage(message)
                .detailMessage(detailMessage)
                .build();
    }

    @Builder
    public ApiError(String path, String reqMethod, String errorCode,
            String status, int httpCode, String shortMessage, String detailMessage) {

        this(shortMessage, status, HttpStatus.valueOf(httpCode));

        this.path = path;
        this.reqMethod = reqMethod;
        this.debugMessage = detailMessage;
        this.errorCode = errorCode;
        this.statusCode = httpCode;
    }

    public ApiError(String message, String status, HttpStatus httpStatus) {
        super(message, status, httpStatus);

        this.subErrors = new ArrayList<>();
    }

    public ApiError() {
        super();
        this.subErrors = new ArrayList<>();
    }

    private void addSubError(ApiSubError subError) {
        if (subErrors == null) {
            subErrors = new ArrayList<>();
        }
        subErrors.add(subError);
    }

    private void addValidationError(String object, String field, Object rejectedValue, String message) {
        addSubError(new ApiValidationError(object, field, rejectedValue, message));
    }

    private void addValidationError(String object, String message) {
        addSubError(new ApiValidationError(object, message));
    }

    public void addValidationError(FieldError fieldError) {
        this.addValidationError(fieldError.getObjectName(),
                fieldError.getField(),
                fieldError.getRejectedValue(),
                fieldError.getDefaultMessage());
    }

    public void addValidationErrors(List<FieldError> fieldErrors) {
        fieldErrors.forEach(this::addValidationError);
    }

    public void addValidationError(ObjectError objectError) {
        this.addValidationError(
                objectError.getObjectName(),
                objectError.getDefaultMessage());
    }

    public void addValidationError(List<ObjectError> globalErrors) {
        globalErrors.forEach(this::addValidationError);
    }

    /**
     * Utility method for adding error of ConstraintViolation. Usually when a
     *
     * @Validated validation fails.
     *
     * @param cv the ConstraintViolation
     */
    private void addValidationError(ConstraintViolation<?> cv) {
        this.addValidationError(cv.getRootBeanClass().getSimpleName(),
                cv.getPropertyPath() == null ? ""
                : ((PathImpl) cv.getPropertyPath()).getLeafNode().asString(),
                cv.getInvalidValue(), cv.getMessage());
    }

    public void addValidationErrors(Set<ConstraintViolation<?>> constraintViolations) {
        constraintViolations.forEach(this::addValidationError);
    }
}
