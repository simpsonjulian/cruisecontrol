package net.sourceforge.cruisecontrol.listeners;

import junit.framework.TestCase;
import net.sourceforge.cruisecontrol.CruiseControlException;
import net.sourceforge.cruisecontrol.ProjectState;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * .
 * User: jfredrick
 * Date: Sep 6, 2004
 * Time: 10:58:41 PM
 */
public class CurrentBuildStatusListenerTest extends TestCase {
    private static final String TEST_DIR = "tmp";
    private final List filesToClear = new ArrayList();
    private CurrentBuildStatusListener listener;

    protected void setUp() throws Exception {
        listener = new CurrentBuildStatusListener();
    }

    protected void tearDown() {
        listener = null;
        for (Iterator iterator = filesToClear.iterator(); iterator.hasNext();) {
            File file = (File) iterator.next();
            if (file.exists()) {
                file.delete();
            }
        }
        filesToClear.clear();
    }

    public void testValidate() throws CruiseControlException {
        try {
            listener.validate();
            fail("'file' should be a required attribute");
        } catch (CruiseControlException cce) {
        }

        listener.setFile("somefile");
        listener.validate();

        listener.setFile("holycowbatman" + File.separator + "filename");
        try {
            listener.validate();
            fail("validate should fail if parent directory doesn't exist");
        } catch (CruiseControlException cce) {
        }
    }

    public void testWritingStatus() throws CruiseControlException, IOException {
        final String fileName = TEST_DIR + File.separator + "_testCurrentBuildStatus.txt";
        listener.setFile(fileName);
        filesToClear.add(new File(fileName));

        checkResultForState(fileName, ProjectState.WAITING);
        checkResultForState(fileName, ProjectState.IDLE);
        checkResultForState(fileName, ProjectState.QUEUED);
        checkResultForState(fileName, ProjectState.BOOTSTRAPPING);
        checkResultForState(fileName, ProjectState.MODIFICATIONSET);
        checkResultForState(fileName, ProjectState.BUILDING);
        checkResultForState(fileName, ProjectState.MERGING_LOGS);
        checkResultForState(fileName, ProjectState.PUBLISHING);
        checkResultForState(fileName, ProjectState.PAUSED);
        checkResultForState(fileName, ProjectState.STOPPED);
    }

    private void checkResultForState(final String fileName, ProjectState state)
            throws CruiseControlException, IOException {
        // This should be equivalent to the date used in listener at seconds precision
        Date date = new Date();
        listener.handleEvent(new ProjectStateChangedEvent("projName", state));
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        final String dateString = formatter.format(date);
        final String description = state.getDescription();
        String expected = "<span class=\"link\">" + description + " as of:<br>" + dateString + "</span>";
        assertEquals(expected, readFileToString(fileName));
    }

    private String readFileToString(String fileName) throws IOException {
        StringBuffer contents = new StringBuffer();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(fileName));
            String line = br.readLine();
            while (line != null) {
                contents.append(line);
                line = br.readLine();
            }
            return contents.toString();
        } finally {
            if (br != null) {
                br.close();
            }
        }
    }

}