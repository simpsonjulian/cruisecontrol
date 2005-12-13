/********************************************************************************
 * CruiseControl, a Continuous Integration Toolkit
 * Copyright (c) 2005, ThoughtWorks, Inc.
 * 651 W Washington Ave. Suite 600
 * Chicago, IL 60661 USA
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *     + Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *
 *     + Redistributions in binary form must reproduce the above
 *       copyright notice, this list of conditions and the following
 *       disclaimer in the documentation and/or other materials provided
 *       with the distribution.
 *
 *     + Neither the name of ThoughtWorks, Inc., CruiseControl, nor the
 *       names of its contributors may be used to endorse or promote
 *       products derived from this software without specific prior
 *       written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE REGENTS OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ********************************************************************************/
package net.sourceforge.cruisecontrol;

import junit.framework.TestCase;

public class ProjectConfigTest extends TestCase {

    private ProjectConfig config;

    protected void setUp() {
        config = new ProjectConfig();
    }

    protected void tearDown() {
        config = null;
    }

    public void testValidate_ScheduleRequired() throws CruiseControlException {
        try {
            config.validate();
            fail("a schedule should have been required by ProjectConfig");
        } catch (CruiseControlException expected) {
            assertEquals("project requires a schedule", expected.getMessage());
        }

        config.add(new MockSchedule());
        config.validate();
    }

    public void testValidateCallsSubelementValidates() throws CruiseControlException {
        MockSchedule schedule = new MockSchedule();
        config.add(schedule);
        MockBootstrappers bootstrappers = new MockBootstrappers();
        config.add(bootstrappers);
        MockModificationSet modificationSet = new MockModificationSet();
        config.add(modificationSet);
        MockListeners listeners = new MockListeners();
        config.add(listeners);
        MockPublishers publishers = new MockPublishers();
        config.add(publishers);
        MockLog log = new MockLog();
        config.add(log);

        config.validate();

        assertTrue(schedule.validateWasCalled());
        assertTrue(bootstrappers.validateWasCalled());
        assertTrue(modificationSet.validateWasCalled());
        assertTrue(listeners.validateWasCalled());
        assertTrue(publishers.validateWasCalled());
        assertTrue(log.validateWasCalled());
    }

    private static class MockBootstrappers extends ProjectConfig.Bootstrappers {
        private boolean validateWasCalled = false;

        public void validate() throws CruiseControlException {
            validateWasCalled = true;
        }

        public boolean validateWasCalled() {
            return validateWasCalled;
        }
    }

    private static class MockModificationSet extends ModificationSet {
        private boolean validateWasCalled = false;

        public void validate() throws CruiseControlException {
            validateWasCalled = true;
        }

        public boolean validateWasCalled() {
            return validateWasCalled;
        }
    }

    private static class MockListeners extends ProjectConfig.Listeners {
        private boolean validateWasCalled = false;

        public void validate() throws CruiseControlException {
            validateWasCalled = true;
        }

        public boolean validateWasCalled() {
            return validateWasCalled;
        }
    }

    private static class MockPublishers extends ProjectConfig.Publishers {
        private boolean validateWasCalled = false;

        public void validate() throws CruiseControlException {
            validateWasCalled = true;
        }

        public boolean validateWasCalled() {
            return validateWasCalled;
        }
    }

    private static class MockLog extends Log {
        private boolean validateWasCalled = false;

        public void validate() throws CruiseControlException {
            validateWasCalled = true;
        }

        public boolean validateWasCalled() {
            return validateWasCalled;
        }
    }
}