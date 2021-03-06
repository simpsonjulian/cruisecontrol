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
package net.sourceforge.cruisecontrol.dashboard;

import java.io.File;

import net.sourceforge.cruisecontrol.dashboard.utils.functors.CCProjectFolderFilter;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Projects {
    private File logRoot;

    private File artifacts;

    private String[] projectNames;

    public Projects(File logRoot, File artifacts, String[] projectNames) {
        this.projectNames = projectNames;
        this.logRoot = logRoot;
        this.artifacts = artifacts;
    }

    public File getArtifactRoot(String projectName) {
        return new File(artifacts, projectName);
    }

    public File getLogRoot(String projectName) {
        return new File(logRoot, projectName);
    }

    public File getLogRoot() {
        return logRoot;
    }

    public File[] getProjectsFromFileSystem() {
        return getLogRoot().listFiles(new CCProjectFolderFilter());
    }

    public boolean hasProject(String projectName) {
        return ArrayUtils.indexOf(projectNames, projectName) > -1;
    }

    public File[] getProjectsRegistedInBuildLoop() {
        File[] projects = new File[projectNames.length];
        for (int j = 0; j < projectNames.length; j++) {
            projects[j] = getLogRoot(projectNames[j]);
        }
        return projects;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
