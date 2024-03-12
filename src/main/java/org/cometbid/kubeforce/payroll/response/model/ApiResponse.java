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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.time.ZonedDateTime;
import org.apache.logging.log4j.ThreadContext;
import lombok.Getter;
import lombok.Setter;
import org.cometbid.kubeforce.payroll.common.util.TimeZonesConverter;
import org.cometbid.kubeforce.payroll.jackson.util.ZonedDateTimeDeserializer;
import org.cometbid.kubeforce.payroll.jackson.util.ZonedDateTimeSerializer;
import org.springframework.http.HttpStatus;

/**
 *
 * @author samueladebowale
 */
@Getter
@Setter
//@JsonRootName(value = "response")
public abstract class ApiResponse {

    /**
     * Url of request that produced the error.
     */
    @JsonProperty(value = "path")
    protected String path = "Not available";

    /**
     * Short, human-readable summary of the problem.
     */
    @JsonProperty(value = "message")
    protected String message;

    /**
     * Method of request that produced the error.
     */
    @JsonProperty(value = "method")
    protected String reqMethod = "Not available";

    @JsonProperty("status")
    protected String status;

    @JsonIgnore
    protected HttpStatus httpStatus;

    @JsonProperty("traceId")
    protected String traceId;

    @JsonProperty(value = "timestamp")
    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ssa z")
    protected ZonedDateTime timestamp;

    public ApiResponse() {
        this.traceId = ThreadContext.get("X-B3-TraceId");
        this.timestamp = TimeZonesConverter.getZonedDateTimeInUTC();

        this.message = null;
        this.status = null;
        this.httpStatus = null;
    }

    public ApiResponse(String message, String status, HttpStatus httpStatus) {
        this();
        this.message = message;
        this.status = status;
        this.httpStatus = httpStatus;
    }

}
