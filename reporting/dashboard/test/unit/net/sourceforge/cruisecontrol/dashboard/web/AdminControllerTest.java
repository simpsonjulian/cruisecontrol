/********************************************************************************
 * CruiseControl, a Continuous Integration Toolkit
 * Copyright (c) 2007, ThoughtWorks, Inc.
 * 200 E. Randolph, 25th Floor
 * Chicago, IL 60601 USA
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
package net.sourceforge.cruisecontrol.dashboard.web;

import net.sourceforge.cruisecontrol.dashboard.Configuration;
import net.sourceforge.cruisecontrol.dashboard.service.ConfigXmlFileService;
import net.sourceforge.cruisecontrol.dashboard.service.EnvironmentService;
import net.sourceforge.cruisecontrol.dashboard.service.SystemService;
import net.sourceforge.cruisecontrol.dashboard.web.command.ConfigurationCommand;

import org.jmock.Mock;
import org.jmock.cglib.MockObjectTestCase;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class AdminControllerTest extends MockObjectTestCase {
    private MockHttpServletRequest request;

    private MockHttpServletResponse response;

    private Mock configurationMock;

    private Configuration configuration;

    private AdminController controller;

    protected void setUp() throws Exception {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        configurationMock =
                mock(Configuration.class, new Class[] {ConfigXmlFileService.class},
                        new Object[] {null});
        configuration = (Configuration) configurationMock.proxy();
        controller =
                new AdminController(configuration, new EnvironmentService(new SystemService()));
    }

    public void testViewNameShouldBeAdminWhenGetIt() throws Exception {
        configurationMock.expects(once()).method("getCruiseConfigLocation");
        ModelAndView mov = controller.handleRequest(request, response);
        assertEquals("page_admin", mov.getViewName());
    }

    public void testShouldShowExistingConfigFileLocationAndContentIfItHasBeenSet() throws Exception {
        String expectedPath = "test/data/config.xml";
        configurationMock.expects(once()).method("getCruiseConfigLocation").will(
                returnValue(expectedPath));
        ModelAndView mov = controller.handleRequest(request, response);
        ConfigurationCommand command = (ConfigurationCommand) mov.getModel().get("command");
        assertEquals(expectedPath, command.getConfigFileLocation());

    }

    public void testShouldPutIsConfigurationEditableInputModel() throws Exception {
        configurationMock.expects(once()).method("getCruiseConfigLocation");
        ModelAndView mov = controller.handleRequest(request, response);
        assertNotNull(mov.getModel().get("isConfigFileEditable"));
    }
}