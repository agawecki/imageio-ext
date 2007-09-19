/*
 *    JImageIO-extension - OpenSource Java Image translation Library
 *    http://www.geo-solutions.it/
 *	  https://imageio-ext.dev.java.net/
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
package it.geosolutions.imageio.plugins.geotiff;

import it.geosolutions.imageio.gdalframework.GDALImageReaderSpi;
import it.geosolutions.imageio.gdalframework.GDALUtilities;

import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageReader;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.spi.ServiceRegistry;

/**
 * Service provider interface for the GeoTiff Image
 * 
 * @author Simone Giannecchini, GeoSolutions.
 * @author Daniele Romagnoli, GeoSolutions.
 * 
 */
public class GeoTiffImageReaderSpi extends GDALImageReaderSpi {

		private static final Logger logger = Logger
				.getLogger(GeoTiffImageReaderSpi.class.toString());

		static final String[] suffixes = { "GeoTiff", "tiff" };

		static final String[] formatNames = { "Tiff", "GeoTiff"};

		static final String[] MIMETypes = { "image/tiff" };

		static final String version = "1.0";

		static final String readerCN = "it.geosolutions.imageio.plugins.geotiff.GeoTiffImageReader";

		static final String vendorName = "GeoSolutions";

		// writerSpiNames
		static final String[] wSN = {/* "it.geosolutions.imageio.plugins.geotiff.GeoTiffImageReaderSpi" */null };

		// StreamMetadataFormatNames and StreamMetadataFormatClassNames
		static final boolean supportsStandardStreamMetadataFormat = false;

		static final String nativeStreamMetadataFormatName = null;

		static final String nativeStreamMetadataFormatClassName = null;

		static final String[] extraStreamMetadataFormatNames = { null };

		static final String[] extraStreamMetadataFormatClassNames = { null };

		// ImageMetadataFormatNames and ImageMetadataFormatClassNames
		static final boolean supportsStandardImageMetadataFormat = false;

		static final String nativeImageMetadataFormatName = null;

		static final String nativeImageMetadataFormatClassName = null;

		static final String[] extraImageMetadataFormatNames = { null };

		static final String[] extraImageMetadataFormatClassNames = { null };

		private boolean registered;

		public GeoTiffImageReaderSpi() {
			super(
					vendorName,
					version,
					formatNames,
					suffixes,
					MIMETypes,
					readerCN, // readerClassName
					STANDARD_INPUT_TYPE,
					wSN, // writer Spi Names
					supportsStandardStreamMetadataFormat,
					nativeStreamMetadataFormatName,
					nativeStreamMetadataFormatClassName,
					extraStreamMetadataFormatNames,
					extraStreamMetadataFormatClassNames,
					supportsStandardImageMetadataFormat,
					nativeImageMetadataFormatName,
					nativeImageMetadataFormatClassName,
					extraImageMetadataFormatNames,
					extraImageMetadataFormatClassNames);

			if (logger.isLoggable(Level.FINE))
				logger.fine("GeoTiffImageReaderSpi Constructor");
			needsTileTuning=true;

		}

		/**
		 * This method checks if the provided input can be decoded from this SPI
		 */
		public boolean canDecodeInput(Object input) throws IOException {
			return super.canDecodeInput(input);
		}

		/**
		 * Returns an instance of the GeoTiffImageReader
		 * 
		 * @see javax.imageio.spi.ImageReaderSpi#createReaderInstance(java.lang.Object)
		 */
		public ImageReader createReaderInstance(Object source) throws IOException {
			return new GeoTiffImageReader(this);
		}

		/**
		 * @see javax.imageio.spi.IIOServiceProvider#getDescription(java.util.Locale)
		 */
		public String getDescription(Locale locale) {
			return new StringBuffer("GeoTiff Image Reader, version ").append(version)
					.toString();
		}

		protected String getSupportedFormats() {
			return "GTiff";
		}

		/**
		 * Upon registration, this method ensures that this SPI is listed at the top
		 * of the ImageReaderSpi items, so that it will be invoked before the
		 * default ImageReaderSpi
		 * 
		 * @param registry
		 *            ServiceRegistry where this object has been registered.
		 * @param category
		 *            a Class object indicating the registry category under which
		 *            this object has been registered.
		 */
		public void onRegistration(ServiceRegistry registry, Class category) {
			 super.onRegistration(registry, category);
			if (registered) {
				return;
			}

			registered = true;

			Iterator readers = GDALUtilities.getJDKImageReaderWriterSPI(registry, "TIFF",
					true).iterator();

			ImageReaderSpi spi;
			while (readers.hasNext()) {
				spi = (ImageReaderSpi) readers.next();
				if(spi==this)
					continue;
				registry.deregisterServiceProvider(spi);
				registry.setOrdering(category, this, spi);

			}
		

		}
}
