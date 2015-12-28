package com.hsn.swing.geotools;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import org.geotools.data.ows.Layer;
import org.geotools.data.ows.WMSCapabilities;
import org.geotools.data.wms.WebMapServer;
import org.geotools.ows.ServiceException;

public class GISViewer {

	public static void main(String[] args) {
		
		/*
		try {
			printLayers();
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		GISFrame frame = new GISFrame();
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			}
		});
		frame.setVisible(true);
	}

	private static void printLayers() throws ServiceException, IOException {
		URL url = new URL("http://localhost:8080/geoserver/wms?REQUEST=GetCapabilities");
		WebMapServer wms = new WebMapServer(url);

		WMSCapabilities caps = wms.getCapabilities();

		Layer layer = null;
		for( Iterator i = caps.getLayerList().iterator(); i.hasNext();){
			Layer test = (Layer) i.next();

			System.out.println("Layer: " + test.getName() + " : " + test.getClass());

//			if( test.getName() != null && test.getName().length() != 0 ){
//				layer = test;
//				break;
//			}
		}
	}

}
