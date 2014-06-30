package org.openflexo.technologyadapter.freeplane;

import java.awt.Dimension;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.openflexo.rm.ResourceLocator;

/**
 * Simple main class to have some visual test of panels. True IHM tests will
 * come later.
 * 
 * @author eloubout
 * 
 */
public class FreeplaneMapEditorVisualMock {

    private static final Logger LOGGER = Logger.getLogger("Test FreeplaneMapEditorVisualMock:w");

    public static void main(final String[] args) throws NoSuchFieldException, SecurityException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                try {
                    final JFrame frame = new JFrame();
                    new File(ResourceLocator.locateResource("TestResourceCenter/FPTest.mm").getURI().split(":")[1]);

                    frame.setSize(new Dimension(550, 500));
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setVisible(true);
                } catch (final Exception e) {
                    // TODO Auto-generated catch block
                    final String msg = "";
                    LOGGER.log(Level.SEVERE, msg, e);
                }

            }
        });
    }
}
