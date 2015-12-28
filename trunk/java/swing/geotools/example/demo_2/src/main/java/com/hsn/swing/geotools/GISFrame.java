package com.hsn.swing.geotools;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import org.geotools.data.ows.CRSEnvelope;
import org.geotools.data.ows.Layer;
import org.geotools.data.ows.WMSCapabilities;
import org.geotools.data.wms.WebMapServer;
import org.geotools.data.wms.request.GetMapRequest;
import org.geotools.data.wms.response.GetMapResponse;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapContext;
import org.geotools.ows.ServiceException;
import org.geotools.referencing.crs.DefaultGeographicCRS;

public class GISFrame extends JFrame {
	
//  Status bar for warnings
	protected JLabel statusBar = new JLabel("Status Bar");

	public GISFrame() throws HeadlessException {
		super();
//		createGUI();
		addMap();	
	}

	private void addMap() {

		// The cPane JPanel will be set as the JFrames contentPane.				
		JPanel cPane = new JPanel();
		cPane.setOpaque(true);
		cPane.setBackground(Color.BLUE);
		cPane.setSize(500, 400);

		JToolBar cToolBar = createToolbar();
		
		cPane.add(cToolBar, BorderLayout.NORTH);
		cPane.add(statusBar, BorderLayout.SOUTH);
		
		ImagePan ipan = new ImagePan();
		try {
			Image img = getWMSLayers();
			ipan.setImage(img);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		cPane.add(ipan);
		
//		final JMapPane mapPane = new JMapPane(new StreamingRenderer(), map);
//		
//		
//		
//
	//	Finally, add the cPane to the JFrame;
		setContentPane(cPane);
		
	}
	
	public class ImagePan extends JPanel {
		protected Image ci = null;

		public ImagePan() {
		}

		public void setImage(Image si) {
			this.ci = si;
			validate();
			repaint();

		}

		public Image getDisplayedImage() {
			return this.ci;
		}

		public void update(Graphics g) {
			if (ci != null) {
				g.drawImage(ci, 0, 0, this.getSize().width, this.getSize().height, this);
			} else {
				super.update();
			}
		}

		public void paint(Graphics g) {
			update(g);
		}
	}

	private Image getWMSLayers() throws ServiceException, IOException {
		
		URL url = new URL("http://localhost:8080/geoserver/wms?REQUEST=GetCapabilities");
		WebMapServer wms = new WebMapServer(url);

		WMSCapabilities caps = wms.getCapabilities();

		List<Layer> layers = new ArrayList<Layer>();
		for (Layer test : caps.getLayerList()) {
			if( test.getName() != null && test.getName().length() != 0 ){
				MapContext context = new DefaultMapContext( DefaultGeographicCRS.WGS84 );
				layers.add(test);
			}
		}
		
		Layer layer = layers.get(3);
		GetMapRequest mapRequest = wms.createGetMapRequest();		
		mapRequest.addLayer(layer);
		
		mapRequest.setDimensions("400", "400");
		mapRequest.setFormat("application/openlayers");
		
		CRSEnvelope bbox = new CRSEnvelope("EPSG:4326",-100.0, -70, 25, 40 );
		mapRequest.setBBox(bbox);

		System.out.println(mapRequest.getFinalURL());
		
		GetMapResponse response = wms.issueRequest( mapRequest );
		
		// TODO Auto-generated method stub
		BufferedImage map = ImageIO.read(response.getInputStream());
		return map;
	}

	private JToolBar createToolbar() {
		//		Define buttons based on Action internal (word?) classes defined below;	
				/*
				JButton rstBut = new JButton (new ResetAction()); rstBut.setText("");
				JButton upBut = new JButton(new UpAction()); upBut.setText("");
				JButton downBut = new JButton(new DownAction()); downBut.setText("");
				JButton leftBut = new JButton (new LeftAction()); leftBut.setText("");
				JButton rightBut = new JButton(new RightAction()); rightBut.setText("");
				JToggleButton getBut = new JToggleButton (new GetFeatureAction()); getBut.setText("");
				JToggleButton zmInBut = new JToggleButton (new ZoomInAction()); zmInBut.setText("");
				JToggleButton zmOutBut = new JToggleButton (new ZoomOutAction()); zmOutBut.setText("");
				*/
		JButton rstBut = new JButton ("Reset");
		JButton upBut = new JButton("Up"); upBut.setText("");
		JButton downBut = new JButton("Down");
		JButton leftBut = new JButton ("Left"); leftBut.setText("");
		JButton rightBut = new JButton("Right"); rightBut.setText("");
		JToggleButton getBut = new JToggleButton ("Get Feature");
		JToggleButton zmInBut = new JToggleButton ("Zoom In");
		JToggleButton zmOutBut = new JToggleButton ("Zoom Out");
		
			//	Define the JToolBar, add buttons, and add it to the cPane;
				JToolBar cToolBar = new JToolBar();
				cToolBar.setPreferredSize(new Dimension(400,30));
				
				cToolBar.add(rstBut);
				cToolBar.add(leftBut);
				cToolBar.add(upBut);
				cToolBar.add(downBut);
				cToolBar.add(rightBut);
				cToolBar.addSeparator();
				
				ButtonGroup group = new ButtonGroup();		
				group.add(zmInBut);
				group.add(zmOutBut);		
				group.add(getBut);
				
				cToolBar.add(zmInBut);
				cToolBar.add(zmOutBut);
				cToolBar.add(getBut);
		return cToolBar;
	}

	private void createGUI() {
		
	}

}
