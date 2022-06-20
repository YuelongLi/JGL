package ui;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ui.Conversion;
import ui.JSizer;
import ui.Locator;

import java.awt.Color;
import javax.swing.JSlider;
import javax.swing.JButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.border.LineBorder;
import javax.swing.SwingConstants;
import javax.swing.JLabel;

public class DataFrame2D extends JFrame {
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	public DataPane2D dataPane = new DataPane2D();
	JLabel coordinates;
	ArrayList<DataFrame2D> linkedFrames = new ArrayList<DataFrame2D>();
	private DataFrame2D self = this;
	Locator locator;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DataFrame2D frame = new DataFrame2D("Sample");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public DataFrame2D(String name) {
		super(name);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 707, 567);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel mainPane = new JPanel();
		mainPane.setBackground(Color.WHITE);
		mainPane.setBounds(0, 0, 689, 520);
		contentPane.setLayout(null);
		contentPane.add(mainPane);
		mainPane.setLayout(null);
		
		JSlider slider = new JSlider();
		slider.setMaximum(1000);
		slider.setValue(500);
		slider.setOrientation(SwingConstants.VERTICAL);
		slider.setBounds(637, 13, 38, 454);
		mainPane.add(slider);
		slider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				if (slider.isFocusOwner()) {
					dataPane.setyScale(Math.exp((double) (slider.getValue() - 500) / 25));
					dataPane.paint();
				}
			}

		});
		dataPane.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
		dataPane.setBounds(14, 13, 609, 454);
		mainPane.add(dataPane);
		dataPane.coordinateMajor = new Color(0x6789ab);
//		dataPane2D.axis = dataPane2D.coordinateMajor;
		dataPane.coordinateMinor = new Color(0xefefef);
		locator = dataPane.getLocator();
		dataPane.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				double x = dataPane.getLocator().tox(arg0.getX(), arg0.getY()),
						y = dataPane.getLocator().toy(arg0.getX(), arg0.getY());
				x = (double) ((int) (x * 100)) / 100;
				y = (double) ((int) (y * 100)) / 100;
				coordinates.setText("x: " + String.valueOf(x) + ", y: " + String.valueOf(y));
				self.mouseClicked(x, y, arg0.getButton());
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
			}
			@Override
			public void mousePressed(MouseEvent arg0) {
			}
			@Override
			public void mouseReleased(MouseEvent arg0) {
			}

		});

		JButton btnProceed = new JButton("proceed");
		btnProceed.setBounds(586, 480, 89, 27);
		mainPane.add(btnProceed);

		coordinates = new JLabel("");
		coordinates.setBounds(24, 484, 152, 18);
		mainPane.add(coordinates);

		btnProceed.addActionListener((e) -> proceed());

		JSizer sizer = new JSizer(mainPane);
		sizer.addAllComponents();
		sizer.addListener(this);
	}
	/**
	 * Override this method to define response to mouse clicking on data pane
	 * @param x The x coordinate on data pane where the mouse has clicked
	 * @param y The y coordinate on data pane where the mouse has clicked
	 * @param mouseButton which button on the mouse clicked 
	 * @see java.awt.event.MouseEvent
	 */
	protected void mouseClicked(double x, double y, int mouseButton) {
		
	}
	/**
	 * Override this method to define extra graphics that shall be added to 
	 * the data pane
	 * @param g
	 */
	protected void paintDataPane(Graphics g) {
	}
	int getX0() {
		return dataPane.getWidth()/2;
	}
	/**
	 * Override this method to change the x label of coordinates
	 * @param x
	 * @return the string value of the label
	 */
	public String getXLabel(double x) {
		String label = String.valueOf(x);
		if(label.length()>4)label = label.substring(0, x>=0?4:5);
		return (x<10000)?label:Conversion.getNumberLabel(x, 3);
	}
	
	/**
	 * Override this method to change the y label of coordinates
	 * @param y
	 * @return the string value of the label
	 */
	public String getYLabel(double y) {
		String label = String.valueOf(y);
		if(label.length()>5)label = label.substring(0, y>=0?5:6);
		return (y<100000)?label:Conversion.getNumberLabel(y, 3);
	}
	
	/**
	 * Override this method to determine what to change when the range of data pane
	 * in another AnalysisFrame that this is linked to is changed
	 * 
	 * @param from
	 * @param to
	 * @param locator of the updated AnalysisFrame
	 */
	public void updateStockRange(double from, double to, Locator locator) {
		// Sets the locating parameters inside the locator of this.dataPane according to
		// the target AnalysisFrame
		dataPane.getLocator().setxTr(locator.getxTr());
		dataPane.getLocator().setyTr(locator.getyTr());
	}

	public void proceed() {
	}
}
