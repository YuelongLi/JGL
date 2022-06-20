package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import static codeLibrary.Algorithms.*;
import utils.*;

/**
 * The datapane 3d class serves the function of presenting datas by working with
 * the abstract data type {@link}DataSet. It is a flexible tool for graphics
 * design, function presentations and data presentations.
 * <p>
 * {@link}Locator is a key component of this class that powers through all the
 * coordinate transformations.
 * </p>
 * 
 * @see Locator
 * @version 0.3
 * @author Yuelong Li
 *
 */
public class DataPane3D extends DataPane2D {
	/**
	 * Identification Information
	 */
	private static final long serialVersionUID = 3930295700215629112L;

	public static final int TOTAL_PLOT_3D = 10000;

	public static final int GRIDX = 4, GRIDY = 1;

	/**
	 * Incident light source for determining reflection
	 */
	private double[] incidentLight = new double[] { 0, 0, -1 };

	/**
	 * Observing angle for determining reflection
	 */
	private double[] observeVector = new double[] { 0, 0, 1 };

	int mousebutton = 0;

	private double z;

	MouseListener register3D = new MouseListener() {

		@Override
		public void mouseClicked(MouseEvent e) {
			boolean selectionFound = false;
			for (Dataset dataset : functions) {
				if (!selectionFound && dataset.isSelected(100, e.getX(), e.getY(), locator)) {
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
			switch ((mousebutton = e.getButton())) {
			case MouseEvent.BUTTON3:
				x = locator.tox(e.getX(), e.getY());
				y = locator.toy(e.getX(), e.getY());
				z = locator.toz(e.getX(), e.getY());
				break;
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			renewDatasets();
			x = Double.NaN;
			y = Double.NaN;
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

	MouseMotionListener rotate = new MouseMotionListener() {

		@Override
		public void mouseDragged(MouseEvent e) {
			switch (mousebutton) {
			case MouseEvent.BUTTON1:
				double newx = (double) e.getX() / locator.dpiSize;
				double newy = (double) e.getY() / locator.dpiSize;
				if (!Double.isNaN(x) && !Double.isNaN(y)) {
					locator.rotateY(x - newx);
					locator.rotateX(y - newy);
				}
				x = newx;
				y = newy;
				repaint();
				break;
			case MouseEvent.BUTTON3:
				double newx1 = locator.tox(e.getX(), e.getY());
				double newy1 = locator.toy(e.getX(), e.getY());
				double newz1 = locator.toz(e.getX(), e.getY());
				locator.setxTr(newx1 - x + locator.getxTr());
				locator.setyTr(newy1 - y + locator.getyTr());
				locator.setzTr(newz1 - z + locator.getzTr());
				repaint();
				break;
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
		}

	};

	public DataPane3D() {
		super();
		locator.setUnitVectors(new double[] { 1, 0, 0 }, new double[] { 0, 0, -1 }, new double[] { 0, 1, 0 });
		super.removeMouseMotionListener(translate);
		super.addMouseMotionListener(rotate);
		super.removeMouseListener(register2D);
		super.addMouseListener(register3D);
		x = Double.NaN;
		y = Double.NaN;
		timing.start();
		locator.rotateX(0.01);
		locator.rotateY(0.01);
	}

	/**
	 * @return plotting gap estimation based on I_xy for the window
	 */
	protected double getPlottingGapS() {
		return Math.sqrt(locator.I_x() * locator.I_y() / TOTAL_PLOT_3D);
	}

	int sidePlots = (int) Math.sqrt((double) TOTAL_PLOT_3D);

	RenderMatrix matrix = new RenderMatrix();

	private ArrayList<double[]> getLine(double[] start, double[] end) {
		ArrayList<double[]> line = new ArrayList<double[]>();
		double[] increment = divideV(subtract(end, start), 100);
		for (double i = 0; i < 1; i += 0.01) {
			line.add(start.clone());
			addV(start, increment);
		}
		return line;
	}

	private double[] addV(double[] a, double[] b) {
		for (int i = 0; i < Math.min(a.length, b.length); i++)
			a[i] += b[i];
		return a;
	}

//	private double[] subtractV(double[] a, double[] b) {
//		for (int i = 0; i < Math.min(a.length, b.length); i++)
//			a[i] -= b[i];
//		return a;
//	}

	private double[] divideV(double[] a, double b) {
		for (int i = 0; i < a.length; i++)
			a[i] /= b;
		return a;
	}

	/**
	 * Quickly loads a curve into the render matrix
	 * 
	 * @param function
	 */
	private void drawCurve(Dataset function) {
		List<double[]> curve = ((Curve) function).getCurve();
		Color functionColor = function.getColor();
		for (int i = 1; i < curve.size(); i++) {
			try {
				if (curve.get(i - 1) != null && curve.get(i) != null
						&& !(Double.isNaN(curve.get(i)[0]) || Double.isNaN(curve.get(i)[1])
								|| Double.isNaN(curve.get(i - 1)[0]) || Double.isNaN(curve.get(i - 1)[1]))) {
					int X1 = locator.toX(curve.get(i - 1));
					int Y1 = locator.toY(curve.get(i - 1));
					int X2 = locator.toX(curve.get(i));
					int Y2 = locator.toY(curve.get(i));
					double Z1 = locator.toDoubleZ(curve.get(i - 1));
					double Z2 = locator.toDoubleZ(curve.get(i));
					int[] xy = new int[] { (X1 + X2) / 2, (Y1 + Y2) / 2 };
					int[][] position = new int[][] { new int[] { X1, Y1 }, new int[] { X2, Y2 } };
					double depth = (Z1 + Z2) / 2;
					RenderHint hint = new RenderHint();
					// Set rendering hints for the object
					hint.put(RenderMatrix.COLOR, functionColor);
					hint.put(RenderMatrix.DEPTH, depth);
					hint.put(RenderMatrix.XY, xy);
					hint.put(RenderMatrix.RENDERTYPE, RenderTypes.Line);
					hint.put(RenderMatrix.POSITION, position);
					// Add the hint to the rendering matrix
					matrix.addHint(hint);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			;
		}
	}

	/**
	 * Quickly loads a particle cloud into the render matrix
	 * 
	 * @param function
	 */
	private void drawParticle(double[] point, Color functionColor, double size) {

		int X = locator.toX(point);
		int Y = locator.toY(point);
		double Z = locator.toDoubleZ(point);
		int[] xy = new int[] { X, Y };
		RenderHint hint = new RenderHint();
		// Set rendering hints for the object
		hint.put(RenderMatrix.COLOR, functionColor);
		hint.put(RenderMatrix.DEPTH, Z);
		hint.put(RenderMatrix.XY, xy);
		hint.put(RenderMatrix.RENDERTYPE, RenderTypes.Particle);
		hint.put(RenderMatrix.POSITION, point);
		hint.put(RenderMatrix.SIZE, size);
		// Add the hint to the rendering matrix
		matrix.addHint(hint);

	}

	double index = 0;

	/**
	 * The paint component method renders the data and paints them onto the canvas.
	 * For 3d surfaces, matrix rendering for each would be ideal for graphing, but
	 * java doesn't allow users to quickly modify single pixels, so a strategy of
	 * sorting small surfaces (facelets) by their depth is adopted when rendering
	 * the surface.
	 */
	protected void paintComponent(Graphics g) {
		paintSuper(g);

		incidentLight = new double[] { 0.5 * Math.sin(2 * index), 0.5 * Math.cos(2 * index), -1 };

		index += 0.1;

		if (size != functions.size()) {
			renewDatasets();
			size = functions.size();
		}

		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setStroke(regular);

		double halfAxisLength = axisLength() / 2;
		if (drawAxis) {
			double[] xAxis0 = new double[] { -halfAxisLength, 0, 0 }, xAxis1 = new double[] { halfAxisLength, 0, 0 },
					yAxis0 = new double[] { 0, -halfAxisLength, 0 }, yAxis1 = new double[] { 0, halfAxisLength, 0 },
					zAxis0 = new double[] { 0, 0, -halfAxisLength }, zAxis1 = new double[] { 0, 0, halfAxisLength };
			// Axis datas
			ArrayList<double[]> xAxisData = getLine(xAxis0, xAxis1), yAxisData = getLine(yAxis0, yAxis1),
					zAxisData = getLine(zAxis0, zAxis1);
			// Axis initialization
			DataMatrix1V xAxis = new DataMatrix1V();
			xAxis.setColor(Color.RED);
			xAxis.setCurve(xAxisData);
			DataMatrix1V yAxis = new DataMatrix1V();
			yAxis.setColor(Color.GREEN);
			yAxis.setCurve(yAxisData);
			DataMatrix1V zAxis = new DataMatrix1V();
			zAxis.setColor(Color.BLUE);
			zAxis.setCurve(zAxisData);
			// Draw Axis
			drawCurve(xAxis);
			drawCurve(yAxis);
			drawCurve(zAxis);
			drawParticle(xAxis1, Color.RED, 0.5);
			drawParticle(yAxis1, Color.GREEN, 0.5);
			drawParticle(zAxis1, Color.BLUE, 0.5); 
		}

		for (Dataset function : functions) {

			if (function instanceof Curve) {
				drawCurve(function);
			}

			if (function instanceof Surface) {
				Color functionColor = function.getColor();
				List<ArrayList<double[]>> surface = ((Surface) function).getSurface();
				float[] HSB = Color.RGBtoHSB(functionColor.getRed(), functionColor.getGreen(), functionColor.getBlue(),
						null);
				Texture texture = ((Surface) function).getTexture();
				try {
					for (int i = 1; i < surface.size(); i++) {
						List<double[]> curve0 = surface.get(i - 1);
						List<double[]> curve1 = surface.get(i);
						for (int b = 1; b < Math.min(curve0.size(), curve1.size()); b++) {
							double[] p0 = curve0.get(b - 1), p1 = curve0.get(b), p2 = curve1.get(b),
									p3 = curve1.get(b - 1);
							double[] P0 = locator.toXYZ(p0), P1 = locator.toXYZ(p1), P2 = locator.toXYZ(p2),
									P3 = locator.toXYZ(p3);
							double[][] points = new double[][] { p0, p1, p2, p3 };
							int[] xy = new int[] { (int) ((P0[0] + P1[0] + P2[0] + P3[0]) / 4),
									(int) ((P0[1] + P1[1] + P2[1] + P3[1]) / 4) };
							double depth = (P0[2] + P1[2] + P2[2] + P3[2]) / 4;
							Color color = Color.getHSBColor(HSB[0], HSB[1], texture.BRF(incidentLight,
									cross(subtract(P1, P0), subtract(P3, P0)), observeVector));
							RenderHint hint = new RenderHint();
							// Set rendering hints for the object
							hint.put(RenderMatrix.COLOR, color);
							hint.put(RenderMatrix.DEPTH, depth);
							hint.put(RenderMatrix.POSITION, points);
							hint.put(RenderMatrix.XY, xy);
							hint.put(RenderMatrix.RENDERTYPE, RenderTypes.Surface);
							// Add the hint to the rendering matrix
							matrix.addHint(hint);
						}
						;
					}
				} catch (IndexOutOfBoundsException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				;
			}

			if (function instanceof Particles) {
				Color functionColor = function.getColor();
				Particles particles = (Particles) function;
				List<double[]> points = particles.getParticles(t);
				double size = particles.radius;
				try {
					for (double[] point : points) {
						drawParticle(point, functionColor, size);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			if (function instanceof Particle) {
				Color functionColor = function.getColor();
				Particle particle = (Particle) function;
				double[] position = particle.getParticle(t);
				double size = (particle.isSelected)?particle.radius*2:particle.radius;
				if(particle.isSelected) {
					locator.xTr = -position[0];
					locator.yTr = -position[1];
					locator.zTr = -position[2];
				}
				try {
					drawParticle(position, functionColor, size);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		matrix.render(g2);
	}

	private Polygon getPolygon(double[]... coords) {
		Polygon tri = new Polygon();
		for (double[] coord : coords) {
			tri.addPoint(locator.toX(coord), locator.toY(coord));
		}
		return tri;
	}

	public void reset() {
		super.reset();
		locator.setUnitVectors(new double[] { 1, 0, 0 }, new double[] { 0, 0, -1 }, new double[] { 0, 1, 0 });
		locator.setScale(1);
		locator.setzTr(0);
		t= 0;
	}

	/**
	 * This method manages to redefine bounds of each function, redefine its
	 * plotting gap and reintialize it. Frame update is paused when this method is
	 * called.
	 */
	protected void renewDatasets() {
		double halfRange = range() / 2;
		for (Dataset function : functions) {
			if (function instanceof Function1V) {
				((Function1V) function).setRangeByBounds(-halfRange, halfRange, -halfRange, halfRange, -halfRange,
						halfRange);
				function.setPlotGap(getPlottingGapC());
			}
			if (function instanceof Function2V) {
				((Function2V) function).setRangeByBounds(-halfRange, halfRange, -halfRange, halfRange, -halfRange,
						halfRange);
				function.setPlotGap(getPlottingGapS());
			}
			function.initialize();
		}
		repaint();
	}

	/**
	 * Finds the optimal axis length for the current window
	 * 
	 * @return axis length
	 */
	protected double axisLength() {
		return Math.min(locator.I_x(), Math.min(locator.I_y(), locator.I_z())) * 3 / 4;
	}

	/**
	 * Finds the xy range for the current window
	 * 
	 * @return axis length
	 */
	protected double range() {
		return axisLength() * 8 / 9;
	}

	// Provides basic information and helps initialize the render matrix. The render
	// matrix consists of
	// a set amount of grids according to the size of the screen and
	// expected resolution.
	protected class RenderMatrix extends ArrayList<TreeSet<HashMap<Integer, Object>>> {
		/**
		 * Because eclipse asked for this
		 */
		private static final long serialVersionUID = 1L;
		public static final int DEPTH = 0, POSITION = 1, COLOR = 2, RENDERTYPE = 3, XY = 4, SIZE = 5;

		// Initialize the rendering matrix
		public RenderMatrix() {
			super(GRIDX * GRIDY);
			for (int i = 0; i < GRIDX * GRIDY; i++)
				add(new TreeSet<HashMap<Integer, Object>>());
		}

		/**
		 * Add an render hint (a facelet or a line segment for example) to the render
		 * matrix
		 */
		public void addHint(RenderHint renderingHints) {
			int xGap = locator.I_X / GRIDX;
			int yGap = locator.I_Y / GRIDY;
			int[] xy = (int[]) renderingHints.get(XY);
			int index = xy[1] / yGap * GRIDX + xy[0] / xGap;
//			double depth = (double) renderingHints.get(DEPTH);
			// Color of original data set can be easily modified here
			// renderingHints.put(COLOR, Color.getHSBColor((float)depth*0.01f, 0.5f, 0.75f)
			// );
			if (GRIDX * GRIDY > index && index >= 0)
				get(index).add(renderingHints);
		}

		private int[] gridSizes;

		public void render(Graphics2D g) {
			/*
			 * In order to avoid overlapse of front surface, all grid size are recorded in a
			 * 2D array to make sure that all the facelets in the front are tesselated
			 * during the last round all together
			 */
			int maxSize = getMaxGrid();
			gridSizes = new int[GRIDX * GRIDY];
			for (int i = 0; i < gridSizes.length; i++)
				gridSizes[i] = maxSize;
			while (!lay(g))
				;
		}

		private int getMaxGrid() {
			int maxSize = 0;
			for (TreeSet<HashMap<Integer, Object>> grid : this)
				if (grid.size() > maxSize) {
					maxSize = grid.size();
				}

			return maxSize;
		}

		/**
		 * 
		 * @param g
		 * @return is the matrix empty
		 */
		private boolean lay(Graphics2D g) {
			if (gridSizes[0] == 0)
				return true;
			for (int i = 0; i < GRIDX * GRIDY; i++) {
				TreeSet<HashMap<Integer, Object>> grid = get(i);
				if (gridSizes[i] == grid.size()) {
					HashMap<Integer, Object> hints = grid.first();
					grid.remove(hints);
					RenderTypes type = (RenderTypes) hints.get(RENDERTYPE);
					switch (type) {
					case Line:
						g.setColor((Color) hints.get(COLOR));
						int[][] position = (int[][]) hints.get(POSITION);
						g.drawLine(position[0][0], position[0][1], position[1][0], position[1][1]);
						break;
					case Surface:
						Polygon poly = getPolygon((double[][]) hints.get(POSITION));
						g.setColor((Color) hints.get(COLOR));
						g.fillPolygon(poly);
						g.drawPolygon(poly);
						break;
					case Particle:
						double[] positionP = (double[]) hints.get(POSITION);
						double size = (double) hints.get(SIZE);
						g.setColor((Color) hints.get(COLOR));
						g.fillOval(locator.toX(positionP) - (int) (locator.dpiSize * size / 2),
								locator.toY(positionP) - (int) (locator.dpiSize * size / 2),
								(int) (locator.dpiSize * size), (int) (locator.dpiSize * size));
						break;
					}
				}
				gridSizes[i] -= 1;
			}
			return gridSizes[0] == 0;
		}

	}

	protected enum RenderTypes {
		Line, Surface, Particle
	}

	class RenderHint extends HashMap<Integer, Object> implements Comparable<HashMap<Integer, Object>> {

		private static final long serialVersionUID = 1L;

		@Override
		public int compareTo(HashMap<Integer, Object> arg0) {
			Double depth1 = (Double) get(RenderMatrix.DEPTH);
			Double depth2 = (Double) arg0.get(RenderMatrix.DEPTH);
			return depth1.compareTo(depth2);
		}

	}
}

/*
 * © Copyright 2017 Cannot be used without authorization
 */