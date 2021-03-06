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

package de.kaiserpfalzedv.billing.invectio.csv.test;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.UUID;

import de.kaiserpfalzedv.billing.api.imported.RawMeteredRecord;
import de.kaiserpfalzedv.billing.api.imported.RawTimedRecord;
import de.kaiserpfalzedv.billing.invectio.RawBillingRecordBuilder;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.slf4j.bridge.SLF4JBridgeHandler;

import static java.time.ZoneOffset.UTC;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2018-02-11
 */
public class RawTimedRecordTest {
    private static final Logger LOG = LoggerFactory.getLogger(RawTimedRecordTest.class);

    private static final UUID ID = UUID.randomUUID();
    private static final String METERING_ID = "metered-id";

    private static final OffsetDateTime METERED_START_DATE = OffsetDateTime.now(UTC).minusMinutes(2L);
    private static final Duration METERED_DURATION = Duration.ofMinutes(2L);

    private static final OffsetDateTime RECORDED_DATE = OffsetDateTime.now(UTC);
    private static final OffsetDateTime IMPORT_DATE = OffsetDateTime.now(UTC);
    private static final OffsetDateTime VALUE_DATE = OffsetDateTime.now(UTC);

    private static final RawTimedRecord RAW_TIMED_RECORD = new RawBillingRecordBuilder<RawTimedRecord>()
            .setId(ID)
            .setMeteringId(METERING_ID)
            .setRecordedDate(RECORDED_DATE)
            .setImportedDate(IMPORT_DATE)
            .setValueDate(VALUE_DATE)
            .setMeteredTimestamp(METERED_START_DATE)
            .setMeteredDuration(METERED_DURATION)
            .build();

    private RawBillingRecordBuilder<RawTimedRecord> service;

    @BeforeClass
    public static void setUpClass() {
        MDC.put("test", RawMeteredRecord.class.getSimpleName());

        if (!SLF4JBridgeHandler.isInstalled()) {
            SLF4JBridgeHandler.install();
        }
    }

    @AfterClass
    public static void tearDownClass() {
        MDC.remove("test");
        MDC.remove("id");
    }

    @Test
    public void generateSimpleTimedRecord() {
        logMethod("metered-record", "Testing a simple metered record");

        RawTimedRecord result = service
                .setMeteredDuration(METERED_DURATION)
                .build();
        LOG.debug("result: {}", result);

        assertNotNull("The id should default to a random UUID", result.getId());
        assertEquals("The metering-id does not match the id", result.getId().toString(), result.getMeteringId());
        assertEquals("The metered duration does not match", METERED_DURATION, result.getMeteredDuration());

        assertNotNull("The value date does not exist", result.getValueDate());
        assertEquals("The value date and recorded date does not match", result.getValueDate(), result.getRecordedDate());
        assertEquals("The value date and imported date does not match", result.getValueDate(), result.getImportedDate());
    }

    private void logMethod(final String method, final String message, final Object... paramater) {
        MDC.put("id", method);

        LOG.debug(message, paramater);
    }

    @Test
    public void copyTimedRecord() {
        logMethod("copy-metered-record", "Copying the metered record: {}", RAW_TIMED_RECORD);

        RawTimedRecord result = service.copy(RAW_TIMED_RECORD).build();
        LOG.debug("result: {}", result);

        assertEquals("ID does not match", ID, result.getId());
        assertEquals("The metering-id does not match", METERING_ID, result.getMeteringId());
        assertEquals("The metered start date does not match", METERED_START_DATE, result.getMeteredTimestamp());
        assertEquals("The metered duration does not match", METERED_DURATION, result.getMeteredDuration());

        assertEquals("The value date does not match", VALUE_DATE, result.getValueDate());
        assertEquals("The recorded date does not match", RECORDED_DATE, result.getRecordedDate());
        assertEquals("The imported date does not match", IMPORT_DATE, result.getImportedDate());
    }

    @Test(timeout = 80L)
    public void runtimeTest() {
        logMethod("runtime-verification", "checking the runtime of the tarif builder ...");

        for (int i = 0; i < 1000; i++) {
            service
                    .setMeteredDuration(METERED_DURATION)
                    .build();
        }
    }

    @Before
    public void setUp() throws Exception {
        service = new RawBillingRecordBuilder<>();
    }

    @After
    public void tearDown() throws Exception {
        MDC.remove("id");
    }

}