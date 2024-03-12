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

import org.cometbid.kubeforce.payroll.config.AppResponseMetadata;
import java.util.Map;
import org.springframework.http.HttpStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author samueladebowale
 */
@Getter
@NoArgsConstructor
public class AppResponse {

    @Setter
    @JsonProperty("success")
    private boolean success;

    @Setter
    @JsonProperty("message")
    private String message;
    
    @Setter
    @JsonProperty("metadata")
    private AppResponseMetadata metadata;

    @JsonProperty("response")
    private ApiResponse response;

    @Builder
    private AppResponse(boolean success, String message, AppResponseMetadata metadata, ApiResponse data) {
        this.success = success;
        this.message = message;
        this.metadata = metadata;
        this.response = data;
    }

    public static AppResponse empty(AppResponseMetadata metadata) {
        return success(null, metadata);
    }

    public static AppResponse success(ApiResponse data, AppResponseMetadata metadata) {

        return AppResponse.builder()
                .message("SUCCESS!")
                .success(true)
                .metadata(metadata)
                .data(data)
                .build();
    }

    public static AppResponse error(ApiResponse data, AppResponseMetadata metadata) {

        return AppResponse.builder()
                .message("ERROR!")
                .success(false)
                .metadata(metadata)
                .data(data)
                .build();
    }

    /**
     *
     * @param apiVersion
     * @param httpMethod
     * @param errorCode
     * @param code
     * @param message
     * @param domain
     * @param reason
     * @param errorReportUri
     * @param moreInfoUrl
     */
    public AppResponse(final String apiVersion, String httpMethod, String errorCode, final int code,
            final String message, final String domain, final String reason, final String errorReportUri,
            final String moreInfoUrl) {

        String status = HttpStatus.valueOf(code).name();
        this.response = ApiError.create(domain, httpMethod, errorCode, status, code, reason, message);

        this.metadata = AppResponseMetadata.builder().apiVersion(apiVersion)
                .moreInfoUrl(moreInfoUrl).sendReportUri(errorReportUri + "?id=" + this.response.getTraceId())
                .build();
    }

    /**
     *
     * @param currentApiVersion
     * @param httpMethod
     * @param errorCode
     * @param httpStatus
     * @param message
     * @param path
     * @param sendReportUri
     * @param moreInfoUrl
     * @param ex
     */
    public AppResponse(String currentApiVersion, String httpMethod, String errorCode, HttpStatus httpStatus,
            String message, String path, String sendReportUri, String moreInfoUrl, Exception ex) {

        String status = httpStatus.name();
        this.response = ApiError.create(path, httpMethod, errorCode, status, httpStatus.value(),
                httpStatus.getReasonPhrase(), ex.getMessage());

        this.metadata = AppResponseMetadata.builder().apiVersion(currentApiVersion)
                .moreInfoUrl(moreInfoUrl).sendReportUri(sendReportUri + "?id=" + this.response.getTraceId())
                .build();
    }

    public static AppResponse fromDefaultAttributeMap(final String apiVersion, String httpMethod, String errorCode,
            final Map<String, Object> defaultErrorAttributes, final String sendReportBaseUri,
            final String moreInfoUrl) {

        // original attribute values are documented in
        // org.springframework.boot.web.servlet.error.DefaultErrorAttributes
        return new AppResponse(apiVersion, httpMethod, errorCode, ((Integer) defaultErrorAttributes.get("status")),
                (String) defaultErrorAttributes.getOrDefault("message", "no message available"),
                (String) defaultErrorAttributes.getOrDefault("path", "no domain available"),
                (String) defaultErrorAttributes.getOrDefault("error", "no reason available"),
                sendReportBaseUri, moreInfoUrl);
    }

    // utility method to return a map of serialized root attributes,
    // see the last part of the guide for more details
    public Map<String, Object> toAttributeMap() {
        return Map.of("meta", metadata, "response", response);
    }

    public ApiResponse getApiResponse() {
        // TODO Auto-generated method stub
        return this.response;
    }

}
