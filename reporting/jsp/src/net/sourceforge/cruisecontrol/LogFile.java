/********************************************************************************
 * CruiseControl, a Continuous Integration Toolkit
 * Copyright (c) 2001, ThoughtWorks, Inc.
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

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;


/**
 * Contains information about a XML log file.
 *
 * @author <a href="mailto:hak@2mba.dk">Hack Kampbjorn</a>
 */
public class LogFile {
    private static final String XML_SUFFIX = ".xml";
    private File xmlFile;

    /**
     * Creates a new instance of LogFile
     * @param logDir directory with the XML log file
     * @param logName name of the XML log file
     */
    public LogFile(File logDir, String logName) {
        this.xmlFile = new File(logDir, logName + BuildInfo.LOG_SUFFIX);
        if (!xmlFile.exists()) {
            xmlFile = new File(logDir, logName + BuildInfo.LOG_SUFFIX + BuildInfo.LOG_COMPRESSED_SUFFIX);
        }
    }

    /**
     * Creates a new instance of LogFile
     * @param xmlFile the XML log file
     */
    public LogFile(File xmlFile) {
        this.xmlFile = xmlFile;
    }

    /**
     * Gets the file object.
     * @return the log file
     */
    public File getFile() {
        return xmlFile;
    }

    /**
     * Wether the log file is compressed or not.
     * @return <code>true</code> if the file is compressed
     */
    public boolean isCompressed() {
        return getName().endsWith(BuildInfo.LOG_COMPRESSED_SUFFIX);
    }

    /**
     * Gets the name of the log file.
     * @return the name of the log file
     */
    public String getName() {
        return getFile().getName();
    }

    /**
     * Gets the log file's directory.
     * @return the parent directory of the log file
     */
    public File getLogDirectory() {
        return getFile().getParentFile();
    }

    /**
     * Gets ...
     * @throws java.io.IOException if there is an error reading the file
     * @return the file content as a stream
     */
    public InputStream getInputStream() throws IOException {
        InputStream in = null;
        if (isCompressed()) {
            in = new GZIPInputStream(new FileInputStream(xmlFile));
        } else {
            in = new FileInputStream(xmlFile);
        }
        return in;
    }
}