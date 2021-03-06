/*
 *    Copyright 2018 Kaiserpfalz EDV-Service, Roland T. Lichti
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package de.kaiserpfalzedv.billing.ratio.api;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;

import de.kaiserpfalzedv.billing.api.rated.Tarif;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-10
 */
public class TarifImpl implements Tarif {
    private static final long serialVersionUID = -6618835601099917468L;


    private final UUID id;
    private final String tarifName;

    private final String unit;
    private final BigDecimal unitDivisor;
    private final MonetaryAmount rate;

    private final HashMap<String, String> tags = new HashMap<>();


    TarifImpl(
            final UUID id,
            final String tarifName,
            final String unit,
            final BigDecimal unitDivisor,
            final MonetaryAmount rate,
            final Map<String, String> tags
    ) {
        this.id = id;
        this.tarifName = tarifName;
        this.unit = unit;
        this.unitDivisor = unitDivisor;
        this.rate = rate;

        if (tags != null) {
            this.tags.putAll(tags);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tarif)) return false;

        Tarif tarif = (Tarif) o;

        return Objects.equals(getId(), tarif.getId());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("tarifName", tarifName)
                .append("unit", unit)
                .append("unitDivisor", unitDivisor)
                .append("rate", rate)
                .toString();
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public String getName() {
        return tarifName;
    }

    @Override
    public String getUnit() {
        return unit;
    }

    @Override
    public BigDecimal getUnitDivisor() {
        return unitDivisor;
    }

    @Override
    public MonetaryAmount getRate() {
        return rate;
    }

    @Override
    public CurrencyUnit getCurrency() {
        return rate.getCurrency();
    }

    @Override
    public Map<String, String> getTags() {
        return Collections.unmodifiableMap(tags);
    }
}