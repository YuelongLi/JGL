package ui;

import java.util.List;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

import utils.Curve;
import utils.Dataset;
import utils.Function;
import utils.Particle;
import utils.Particles;
import utils.Chart;
import utils.Conversion;

/**
 * The datapane2d serves the function of presenting datas by working with the
 * abstract class Function1V. It is a flexible tool for graphics design,
 * function presentations and data presentations.
 * <p>
 * At the core of this class is a {@link} Locator that powers through all the
 * vector transformations.
 * </p>
 * 
 * @see Locator
 * @version 0.2
 * @author Yuelong Li
 *
 */
public class DataPane2D extends JPanel {

	/**
	 * Identification information
	 */
	private static final long serialVersionUID = -5602431922185503626L;

	protected DataPane2D base = this;

	public Color coordinateMinor = new Color(240, 240, 240);

	public Color coordinateMajor = Color.LIGHT_GRAY;

	public Color axis = Color.BLACK;

	public Color text = Color.gray;

	int FPS = 100;

	public static final double PLOT_GAP_2D_IN_PIXEL = 1;

	public boolean drawAxis = true;

	double plotGapP = PLOT_GAP_2D_IN_PIXEL;

	/**
	 * Once a function renew is scheduled, paintComponent() will notify
	 * renewFunctions at the end of its clause
	 */
	boolean renewScheduled = false;

	double t = 0;

	Timer timing = new Timer(1000 / FPS, (ActionEvent e) -> {
		repaint();
		t += 1.0 / FPS;
	});
	protected Locator locator = new Locator(this) {
		protected void extra() {
			renewDatasets();
		}
	};

	List<Dataset> functions = new ArrayList<Dataset>() {
		private static final long serialVersionUID = 1L;

		public boolean add(Dataset dataset) {
			boolean pas = super.add(dataset);
			repaint();
			return pas;
		}
	};

	/**
	 * Tells the graphing panel whether XY(Z) axis should be drawn
	 * 
	 * @param drawAxis
	 */
	public void setDrawAxis(boolean drawAxis) {
		this.drawAxis = drawAxis;
	}

	Stroke regular = new BasicStroke(1.6f);
	double size = functions.size();

	// For inherited classes to access super method
	protected void paintSuper(Graphics g) {
		super.paintComponent(g);
	}

	protected void paintComponent(Graphics g) {
		paintSuper(g);
		if (size != functions.size()) {
			renewDatasets();
			size = functions.size();
		}

		double[] I_xy = locator.getI_xyz();
		double gap1x = Math.pow(10, 1 - (int) Math.ceil(Math.log(locator.xScale) / Math.log(10)));
		double gap1y = Math.pow(10, 1 - (int) Math.ceil(Math.log(locator.yScale) / Math.log(10)));
		double gap2x = gap1x / 10;
		double gap2y = gap1y / 10;

		double left = (int) (locator.tox(0) / gap2x) * gap2x;
		double up = (int) (locator.toy(0) / gap2y) * gap2y;

		double point = left;

		// Minor coordinate lines
		g.setColor(coordinateMinor);
		while (point <= I_xy[0] + left) {
			g.drawLine(locator.toX(point), 0, locator.toX(point), locator.I_Y);
			point += gap2x;
		}

		point = up;
		while (point >= up - I_xy[1]) {
			g.drawLine(0, locator.toY(point), locator.I_X, locator.toY(point));
			point -= gap2y;
		}

		// Major coordinate lines
		left = (int) (locator.tox(0) / gap1x) * gap1x;
		up = (int) (locator.toy(0) / gap1y) * gap1y;

		point = left;
		g.setColor(coordinateMajor);
		while (point <= I_xy[0] + left) {
			g.drawLine(locator.toX(point), 0, locator.toX(point), locator.I_Y);
			point += gap1x;
		}

		point = up;
		while (point >= up - I_xy[1]) {
			g.drawLine(0, locator.toY(point), locator.I_X, locator.toY(point));
			point -= gap1y;
		}

		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setStroke(regular);

		// Coordinate axis
		if (drawAxis) {
			g2.setColor(axis);
			g2.drawLine(locator.toX(0), 0, locator.toX(0), locator.I_Y);
			g2.drawLine(0, locator.toY(0), locator.I_X, locator.toY(0));
		}

		for (int k = 0; k < functions.size(); k++) {// Modified to avoid concurrent modification exceptions
			Dataset function = functions.get(k);
			if(!function.visible)
				continue;
			if (function instanceof Function)
				function.initialize();

			g2.setColor(function.getColor());
			if (function instanceof Curve) {
				List<double[]> curve = ((Curve) function).getCurve();

				for (int i = 1; i < curve.size(); i++) {
					try {
						if (curve.get(i - 1) != null && curve.get(i) != null
								&& !(Double.isNaN(curve.get(i)[0]) || Double.isNaN(curve.get(i)[1])
										|| Double.isNaN(curve.get(i - 1)[0]) || Double.isNaN(curve.get(i - 1)[1]))) {
							int X1 = locator.toX(curve.get(i - 1));
							int Y1 = locator.toY(curve.get(i - 1));
							int X2 = locator.toX(curve.get(i));
							int Y2 = locator.toY(curve.get(i));
							g2.drawLine(X1, Y1, X2, Y2);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					;
				}
			}

			if (function instanceof Particles) {
				Particles particles = (Particles) function;
				List<double[]> points = particles.getParticles(t);
				double size = particles.radius;
				try {
					for (double[] pas : points) {
						g2.fillOval(locator.toX(pas) - (int) (locator.dpiSize * size / 2),
								locator.toY(pas) - (int) (locator.dpiSize * size / 2), (int) (locator.dpiSize * size),
								(int) (locator.dpiSize * size));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (function instanceof Particle) {
				Particle particle = (Particle) function;
				double[] position = particle.getParticle(t);
				double size = (particle.isSelected) ? particle.radius * 1.5 : particle.radius;
				try {
					g2.fillOval(locator.toX(position) - (int) (locator.dpiSize * size / 2),
							locator.toY(position) - (int) (locator.dpiSize * size / 2), (int) (locator.dpiSize * size),
							(int) (locator.dpiSize * size));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (function instanceof Chart) {
				if (((Chart) function).geometries.size() == 0)
					function.initialize();
				for (int i = 0; i < ((Chart) function).geometries.size(); i++) {
					ArrayList<double[]> geometry = ((Chart) function).geometries.get(i);
					int sides = geometry.size();
					int[] x = new int[sides];
					int[] y = new int[sides];
					for (int j = 0; j < sides; j++) {
						x[j] = locator.toX(geometry.get(j));
						y[j] = locator.toY(geometry.get(j));
					}
					g2.fillPolygon(x, y, sides);
				}
			}
		}

		g.setColor(text);
		g.setFont(new Font("Consolas", 0, 12));

		double gapx = gap2x * 2;
		double gapy = gap2y * 2;

		left = (int) (locator.tox(0) / gapx) * gapx;
		up = (int) (locator.toy(0) / gapy) * gapy;

		point = left;

		int lastpoint = 0;
		while (point < I_xy[0] + left) {// Put down lables for x
			String sci = getXLabel(point);
			int X = locator.toX(point);
			if (lastpoint < X) {
				g.drawString(sci, X, locator.I_Y);
				lastpoint = X + sci.length() * 7 + 10;
			}
			point += gapx;
		}
		point = up;
		lastpoint = 0;
		while (point > up - I_xy[1]) {// Put down lables for y
			String sci = getYLabel(point);
			int Y = locator.toY(point);
			if (Y > lastpoint) {
				g.drawString(sci, locator.I_X - sci.length() * 7, Y);
				lastpoint = Y + locator.dpiSize;
			}
			point -= gapy;
		}
	}

	/**
	 * This method specify what should be displayed in terms of x-axis
	 * 
	 * @param x the value of x
	 * @return the coordinate marker it corresponds to
	 */
	protected String getXLabel(double x) {
		return Conversion.getNumberLabel(x, 5);
	}

	/**
	 * This method specify what should be displayed in terms of y-axis
	 * 
	 * @param y the value of y
	 * @return the coordinate marker it corresponds to
	 */
	protected String getYLabel(double y) {
		return Conversion.getNumberLabel(y, 5);
	}

	/**
	 * This method specify what should be displayed in terms of z-axis
	 * 
	 * @param z the value of z
	 * @return the coordinate marker it corresponds to
	 */
	protected String getZLabel(double z) {
		return Conversion.getNumberLabel(z, 5);
	}

	/**
	 * @return plotting range estimation based on I_xy for curves
	 */
	protected double getPlottingGapC() {
		return locator.I_x() / locator.I_X * PLOT_GAP_2D_IN_PIXEL;
	}

	/**
	 * This method manages to redefine bounds of each function, redefine its
	 * plotting gap and reintialize it. Frame update is paused when this method is
	 * called.
	 */
	protected void renewDatasets() {
		for (Dataset function : functions) {
			if (function instanceof Function) {
				((Function) function).setRangeByBounds(locator.tox(0), locator.tox(locator.I_X),
						locator.toy(locator.I_Y), locator.toy(0));
				function.setPlotGap(getPlottingGapC());
			}
		}
		repaint();
	}
	//Allows for the locking of translation and scaling in the specified direction
	public boolean lockX = false;
	public boolean lockY = false;
	MouseMotionListener translate = new MouseMotionListener() {

		@Override
		public void mouseDragged(MouseEvent e) {
			if (isAutoScrolling) {
				double newx = locator.tox(e.getX(), e.getY());
				double newy = locator.toy(e.getX(), e.getY());
				if (tracking) {
					if(!lockX)
						setxTr(newx - x + locator.getxTr());
					if(!lockY)
						setyTr(newy - y + locator.getyTr());
				}
				repaint();
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {

		}

	};

	Dataset selected = null;

	/**
	 * For x, y location determination, dataset renewal, and dataset selection
	 */
	MouseListener register2D = new MouseListener() {

		@Override
		public void mouseClicked(MouseEvent e) {
			boolean selectionFound = false;
			for (Dataset dataset : functions) {
				if (!selectionFound && dataset.isSelected(25, e.getX(), e.getY(), locator)) {
					dataset.isSelected = true;
					selectionFound = true;
					selected = dataset;
				} else {
					dataset.isSelected = false;
				}
			}
			if(!selectionFound)
				selected=null;
		}

		@Override
		public void mousePressed(MouseEvent e) {
			x = locator.tox(e.getX(), e.getY());
			y = locator.toy(e.getX(), e.getY());
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			renewDatasets();
			repaint();
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			tracking = true;
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

	};

	double x, y;
	protected boolean tracking = true;

	public DataPane2D() {
		setBackground(Color.white);
		super.addMouseMotionListener(translate);
		super.addMouseListener(register2D);

		super.addMouseWheelListener(new MouseWheelListener() {
			boolean pending = false;
			final int threshold = 100;
			int timeleft;

			/*
			 * Designed to avoid successive call to renewFunctions().
			 */
			private void resize() {
				if (pending) {
					timeleft = threshold;
				} else {
					pending = true;
					timeleft = threshold;
					new Thread() {
						public void run() {
							try {
								while (timeleft > 0) {
									Thread.sleep(1);
									timeleft--;
								}
								renewDatasets();
								repaint();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							renewScheduled = true;
							pending = false;
						}
					}.start();
				}
			}

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				double scaling = Math.pow(Math.E, e.getPreciseWheelRotation() / 10);
				if (selected != null) {
					double x = selected.selectionCoords[0];
					double y = selected.selectionCoords[1];
					double z = (selected.selectionCoords.length > 2) ? selected.selectionCoords[2] : 0;
					zoom(x, y, z, scaling, scaling, scaling);
				} else {
					int pinX = e.getX(), pinY = e.getY();
					zoom(pinX, pinY, scaling, scaling, scaling);
				}
				resize();
				repaint();
			}

		});
		this.addComponentListener(new ComponentListener() {

			@Override
			public void componentResized(ComponentEvent e) {
				repaint();
			}

			@Override
			public void componentMoved(ComponentEvent e) {
			}

			@Override
			public void componentShown(ComponentEvent e) {
				repaint();
			}

			@Override
			public void componentHidden(ComponentEvent e) {
			}

		});
	}

	public void reset() {
		locator.setxTr(0);
		locator.setyTr(0);
		locator.setxScale(1);
		locator.setyScale(1);
		repaint();
	}

	/**
	 * Adds the new dataset into the list of datasets and repaint the graph
	 * 
	 * @param dataset
	 */
	public void addDataset(Dataset dataset) {
		functions.add(dataset);
		renewDatasets();
	}

	/**
	 * Removes the dataset from the list of datasets and repaint the graph
	 * 
	 * @param dataset
	 * @return wheter the dataset exists in the list of datasets
	 */
	public boolean removeDataset(Dataset dataset) {
		boolean val = functions.remove(dataset);
		repaint();
		return val;
	}

	public void setFPS(int FPS) {
		this.FPS = FPS;
		timing.setDelay(1000 / FPS);
	}

	public double getFPS() {
		return FPS;
	}

	public void zoom(int pinX, int pinY, double factorx, double factory) {
		zoom(pinX, pinY, factorx, factory, 0);
	}

	public void zoom(int pinX, int pinY, double factorx, double factory, double factorz) {
		double pinx = locator.tox(pinX, pinY);
		double piny = locator.toy(pinX, pinY);
		double pinz = locator.toz(pinX, pinY);
		zoom(pinx, piny, pinz, factorx, factory, factorz);
	}

	public void zoom(double pinx, double piny, double pinz, double factorx, double factory, double factorz) {
		if(!lockX)
			locator.xScale *= factorx;
		if(!lockY)
			locator.yScale *= factory;
		locator.zScale *= factorz;
		if(!lockX)
			locator.xTr = (pinx + locator.xTr) / factorx - pinx;
		if(!lockY)
			locator.yTr = (piny + locator.yTr) / factory - piny;
		locator.zTr = (pinz + locator.zTr) / factorz - pinz;
		locator.I_xyz[0] = locator.I_x();
		locator.I_xyz[1] = locator.I_y();
		locator.I_xyz[2] = locator.I_z();
		locator.resizeVectors();
	}

	public void setxScale(double xScale) {
		locator.setxScale(xScale);
	}

	public void setyScale(double yScale) {
		locator.setyScale(yScale);
	}

	public void setzScale(double zScale) {
		locator.setzScale(zScale);
	}

	public void setxTr(double xTr) {
		locator.setxTr(xTr);
	}

	public void setyTr(double yTr) {
		locator.setyTr(yTr);
	}

	boolean isAutoScrolling = true;

	/**
	 * When auto scrolling is on, the data panel is translated when the mouse is
	 * dragged
	 * 
	 * @param isAutoScrolling
	 */
	public void setScrolling(boolean isAutoScrolling) {
		this.isAutoScrolling = isAutoScrolling;
	}

	public Locator getLocator() {
		return locator;
	}

	/**
	 * 
	 * @param plotDensity suggests how densely the curve should be graphed, namely
	 *                    how many plots per pixel (usually one point per pixel.
	 */
	public void setPlotGapP(double plotGapP) {
		this.plotGapP = plotGapP;
	}

	public List<Dataset> getDatasets() {
		return functions;
	}

	/**
	 * Clears the data pane of all its datasets
	 */
	public void clear() {
		this.functions.clear();
		paint();
	}

	/**
	 * Once the content of the panel is changed, call paint to commit the changes
	 */
	public void paint() {
		renewDatasets();
		repaint();
	}

	public void terminate() {
		timing.stop();
	}
}
/*
 * Copyright 2017
 */