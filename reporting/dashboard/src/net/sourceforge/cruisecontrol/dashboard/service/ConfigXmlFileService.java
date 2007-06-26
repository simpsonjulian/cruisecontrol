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
package net.sourceforge.cruisecontrol.dashboard.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.cruisecontrol.CruiseControlException;
import net.sourceforge.cruisecontrol.dashboard.Projects;
import net.sourceforge.cruisecontrol.dashboard.exception.ConfigurationException;
import net.sourceforge.cruisecontrol.dashboard.sourcecontrols.VCS;
import net.sourceforge.cruisecontrol.dashboard.utils.DashboardXMLManager;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

public class ConfigXmlFileService {

    private EnvironmentService envService;

    private final TemplateRenderService renderService;

    // this is only here for unit tests. should refactor those.
    public ConfigXmlFileService(EnvironmentService envSerivce) {
        this(envSerivce, getDefaultRenderService());
    }

    private static TemplateRenderService getDefaultRenderService() {
        TemplateRenderService renderService = new TemplateRenderService();
        try {
            renderService.loadTemplates();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return renderService;
    }

    public ConfigXmlFileService(EnvironmentService envService, TemplateRenderService renderService) {
        this.envService = envService;
        this.renderService = renderService;
    }

    public String readContentFromConfigFile(String pathToConfigFile) {
        File config = new File(StringUtils.defaultString(pathToConfigFile));
        if (!config.exists()) {
            return "";
        }
        try {
            return FileUtils.readFileToString(config, null);
        } catch (IOException e) {
            return "";
        }
    }

    public void writeContentToConfigXml(String pathToConfigFile, String configFileContent)
            throws IOException {
        FileUtils.writeStringToFile(new File(pathToConfigFile), configFileContent, null);
    }

    public File getConfigXmlFile(File cruiseConfigFile) {
        if (isConfigFileValid(cruiseConfigFile)) {
            return cruiseConfigFile;
        }
        if (isConfigFileValid(envService.getConfigXml())) {
            return envService.getConfigXml();
        }
        return null;
    }

    public boolean isConfigFileValid(File cruiseConfigFile) {
        return cruiseConfigFile != null && cruiseConfigFile.exists()
                && cruiseConfigFile.getName().endsWith(".xml");
    }

    public Projects getProjects(File cruiseConfigFile) throws CruiseControlException,
            ConfigurationException {
        if (cruiseConfigFile == null) {
            return null;
        } else {
            DashboardXMLManager manager = new DashboardXMLManager(cruiseConfigFile);
            return new Projects(envService.getProjectsDir(), envService.getLogDir(), envService
                    .getArtifactsDir(), manager.getCruiseControlConfig());
        }
    }

    public void addProject(File cruiseConfigFile, String projectName, VCS vcs) {
        Map map = new HashMap();
        map.put("$projectname", projectName);
        map.put("$bootstrapper", vcs.getBootStrapper());
        map.put("$repository", vcs.getRepository());
        String xml = renderService.renderTemplate("project_xml.template", map);
        String content;
        try {
            content = FileUtils.readFileToString(cruiseConfigFile, null);
        } catch (IOException e) {
            content = "";
        }
        content = StringUtils.replace(content, "</cruisecontrol>", "");
        try {
            this.writeContentToConfigXml(cruiseConfigFile.getAbsolutePath(), content + xml
                    + "\n</cruisecontrol>");
        } catch (IOException e) {
            throw new RuntimeException("");
        }
    }

    public boolean isConfigFileEditable() {
        return this.envService.isConfigFileEditable();
    }
}