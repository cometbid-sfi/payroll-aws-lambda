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
package org.cometbid.kubeforce.payroll.config;

import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 *
 * @author samueladebowale
 */
@Builder
@Getter
@Setter
@Configuration
@AllArgsConstructor
@NoArgsConstructor
@PropertySource(value = "classpath:application.yml", factory = YamlPropertySourceFactory.class)
public class AppResponseMetadata {

    @JsonProperty("apiVersion")
    @Value("${api.common.version}")
    private String apiVersion;

    @JsonProperty("reportTo")
    @Value("${api.common.reportError}")
    private String sendReportUri;

    @JsonProperty("moreInfo")
    @Value("${api.common.help}")
    private String moreInfoUrl;

    @JsonProperty("documentation")
    @Value("${api.common.documentation}")
    private String apiDocumentation;

    @JsonProperty("technical")
    @Value("${api.common.technicalReport}")
    private String technical;

}
