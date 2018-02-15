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

package de.kaiserpfalzedv.billing.api.base.impl;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import de.kaiserpfalzedv.billing.api.base.BaseMeteredBillingRecord;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-09
 */
public abstract class BaseMeteredBillingRecordImpl extends BaseBillingRecordImpl implements BaseMeteredBillingRecord {
    private static final long serialVersionUID = -6656305165085168621L;


    /**
     * The value metered (for some product lines this may be zero, since the duration is the metered value).
     */
    private final BigDecimal meteredValue;

    public BaseMeteredBillingRecordImpl(
            final UUID id,
            final String meteringId,
            final OffsetDateTime recordedDate,
            final OffsetDateTime importedDate,
            final OffsetDateTime valueDate,
            final BigDecimal meteredValue
    ) {
        super(id, meteringId, recordedDate, importedDate, valueDate);

        this.meteredValue = meteredValue;
    }

    public BigDecimal getMeteredValue() {
        return meteredValue;
    }

    @Override
    public int compareTo(@NotNull BaseMeteredBillingRecord o) {
        return getValueDate().compareTo(o.getValueDate());
    }
}