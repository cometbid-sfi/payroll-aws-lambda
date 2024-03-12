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
package org.cometbid.kubeforce.payroll.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.cometbid.kubeforce.payroll.gson.util.Exclude;
import org.cometbid.kubeforce.payroll.common.util.ArtifactForFramework;
import org.springframework.data.annotation.Version;

/**
 *
 * @author samueladebowale
 * @param <T>
 */
@MappedSuperclass
@Accessors(chain = true)
@ToString(callSuper = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class AbstractEntity<T extends EntityId> implements Entity<T>, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 6425982031170127365L;

    @Id
    @JsonIgnore
    @Exclude
    protected T id;

    @Version
    @JsonIgnore
    @Exclude
    protected int version;

    @ArtifactForFramework
    protected AbstractEntity() {
    }

    protected AbstractEntity(T id) {
        this.id = id;
    }

    @Override
    public T getId() {
        return id;
    }

    protected abstract void setId();

}
