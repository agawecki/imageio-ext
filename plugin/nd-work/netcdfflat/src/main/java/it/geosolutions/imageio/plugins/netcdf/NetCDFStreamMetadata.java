package it.geosolutions.imageio.plugins.netcdf;

import it.geosolutions.imageio.ndplugin.BaseImageReader;
import it.geosolutions.imageio.ndplugin.util.Utilities;

import java.io.IOException;

import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;

import org.w3c.dom.Node;

public class NetCDFStreamMetadata extends IIOMetadata {
    /**
     * The name of the native metadata format for this object.
     */
    public static final String nativeMetadataFormatName = "it_geosolutions_imageio_plugins_netcdf_streamMetadata_1.0";

    private BaseImageReader reader;

    public static final String GLOBAL_ATTRIBUTES = "GlobalAttributes";

    public NetCDFStreamMetadata(final BaseImageReader reader) {
        this.reader = reader;

    }

    /**
     * Returns the XML DOM <code>Node</code> object that represents the root
     * of a tree of metadata contained within this object on its native format.
     * 
     * @return a root node containing common metadata exposed on its native
     *         format.
     */
    protected Node createCommonNativeTree() {
        // Create root node
        final IIOMetadataNode root = new IIOMetadataNode(
                nativeMetadataFormatName);

        // ////////////////////////////////////////////////////////////////////
        //
        // GlobalAttributes
        //
        // ////////////////////////////////////////////////////////////////////
        IIOMetadataNode node = new IIOMetadataNode(GLOBAL_ATTRIBUTES);
        if (reader instanceof NetCDFImageReader) {
            NetCDFImageReader flatReader = (NetCDFImageReader) reader;
            final int numAttributes = flatReader.getNumGlobalAttributes();
            try {
                for (int i = 0; i < numAttributes; i++) {
                    String attributePair= flatReader.getGlobalAttributeAsString(i);
                    final int separatorIndex = attributePair
                            .indexOf(NetCDFImageReader.SEPARATOR);
                    String attributeName = attributePair.substring(0,
                            separatorIndex);
                    final String attributeValue = attributePair.substring(
                            separatorIndex
                                    + NetCDFImageReader.SEPARATOR.length(),
                            attributePair.length());
                    // //
                    // Note: IIOMetadata doesn't allow to set attribute name
                    // containing "\\". Therefore we replace that char
                    // //
                    if (attributeName.contains("\\"))
                        attributeName = Utilities
                                .adjustAttributeName(attributeName);
                    node.setAttribute(attributeName, attributeValue);
                }
            } catch (IOException e) {
                throw new IllegalArgumentException("Unable to parse attribute",
                        e);
            }

            root.appendChild(node);
        }
        return root;
    }

    /**
     * Returns an XML DOM <code>Node</code> object that represents the root of
     * a tree of common stream metadata contained within this object according
     * to the conventions defined by a given metadata format name.
     * 
     */
    public Node getAsTree(String formatName) {
        if (nativeMetadataFormatName.equalsIgnoreCase(formatName))
            return createCommonNativeTree();
        throw new IllegalArgumentException(formatName
                + " is not a supported format name");
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }

    @Override
    public void mergeTree(String formatName, Node root)
            throws IIOInvalidTreeException {
        // TODO: add message
        throw new UnsupportedOperationException();
    }

    @Override
    public void reset() {
    }

}