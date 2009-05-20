/*
 *    JImageIO-extension - OpenSource Java Image translation Library
 *    http://www.geo-solutions.it/
 *    (C) 2007, GeoSolutions
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package it.geosolutions.imageio.plugins.jhdf.aps;

import it.geosolutions.imageio.ndplugin.BaseImageMetadata;
import it.geosolutions.imageio.plugins.jhdf.JHDFTestCase;
import it.geosolutions.imageio.utilities.ImageIOUtilities;
import it.geosolutions.resources.TestData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;

import junit.framework.Test;
import junit.framework.TestSuite;

public class APSHDFReadTest extends JHDFTestCase {

    private static final Logger LOGGER = Logger
            .getLogger("it.geosolutions.imageio.plugins.jhdf.aps");

    public APSHDFReadTest(String name) {
        super(name);
    }

    private void warningMessage() {
        StringBuffer sb = new StringBuffer(
                "Test file not available. Test are skipped");
        LOGGER.info(sb.toString());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();

        // Test reading of a simple image
        suite.addTest(new APSHDFReadTest("testRead"));

        return suite;
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public void testRead() throws IOException {
        File file;
        try {
            file = TestData
                    .file(this, "MODPM2008242115741.L3_000_EAST_MED_1");
        } catch (FileNotFoundException fnfe) {
            warningMessage();
            return;
        }
        ImageReader reader = new HDFAPSImageReaderSpi().createReaderInstance();
        reader.setInput(file);
        final int index = 0;
        if (TestData.isInteractiveTest()) {
//            ImageIOUtilities.visualize(reader.read(0), "sst",  true);
            ImageIOUtilities.visualize(reader.read(3), "true_color",  false);
            
        } else
            assertNotNull(reader.read(index));

        IIOMetadata metadata = reader.getImageMetadata(index);
        ImageIOUtilities.displayImageIOMetadata(metadata
                .getAsTree(BaseImageMetadata.nativeMetadataFormatName));
        ImageIOUtilities.displayImageIOMetadata(metadata
                .getAsTree(HDFAPSImageMetadata.nativeMetadataFormatName));
        
        IIOMetadata streamMetadata = reader.getStreamMetadata();
        ImageIOUtilities.displayImageIOMetadata(streamMetadata
                .getAsTree(HDFAPSStreamMetadata.nativeMetadataFormatName));
        reader.dispose();
    }
}
