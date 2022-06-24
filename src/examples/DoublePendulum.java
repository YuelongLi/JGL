package examples;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.JButton;

import ui.Locator;
import ui.JSizer;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class DoublePendulum extends JFrame {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField g;
	private JTextField θ1;
	private Timer autoUpdate;
	private JTextField r1;
	private JTextField θ2;
	private JTextField r2;
	private JTextField m1;
	private JTextField m2;
	
	double lengthFactor = 1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DoublePendulum frame = new DoublePendulum();
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
	public DoublePendulum() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1062, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		PresentPanel mainPane = new PresentPanel();
		mainPane.setBackground(new Color(255, 255, 204));
		mainPane.setBounds(0, 0, 1044, 653);
		contentPane.add(mainPane);
		mainPane.setLayout(null);
		JSizer sizer = new JSizer(mainPane);

		JLabel lblG = new JLabel("g");
		lblG.setHorizontalAlignment(SwingConstants.CENTER);
		lblG.setFont(new Font("ËÎÌå", Font.PLAIN, 20));
		lblG.setBounds(38, 13, 17, 24);
		mainPane.add(lblG);

		JLabel lblNkg = new JLabel("N/kg");
		lblNkg.setHorizontalAlignment(SwingConstants.CENTER);
		lblNkg.setFont(new Font("ËÎÌå", Font.PLAIN, 18));
		lblNkg.setBounds(141, 13, 61, 24);
		mainPane.add(lblNkg);

		JLabel lblDegrees = new JLabel("Degrees");
		lblDegrees.setBounds(141, 64, 72, 18);
		mainPane.add(lblDegrees);

		JButton btnRestart = new JButton("Restart");
		btnRestart.setBounds(14, 145, 113, 27);
		mainPane.add(btnRestart);
		btnRestart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					autoUpdate.stop();
					mainPane.θ1 = Double.valueOf(θ1.getText()) / 180 * Math.PI;
					mainPane.θ2 = Double.valueOf(θ2.getText()) / 180 * Math.PI;
					mainPane.g = Double.valueOf(g.getText());
					mainPane.r1 = Double.valueOf(r1.getText())*lengthFactor;
					mainPane.r2 = Double.valueOf(r2.getText())*lengthFactor;
					mainPane.m1 = Double.valueOf(m1.getText());
					mainPane.m2 = Double.valueOf(m2.getText());
					mainPane.w1 = 0;
					mainPane.w2 = 0;
					mainPane.t = 0;
					mainPane.initializedata();
				} catch (Exception f) {
				}
				autoUpdate.start();
			}
		});

		JToggleButton tglbtnPause = new JToggleButton("Pause");
		tglbtnPause.setBounds(168, 145, 142, 27);
		mainPane.add(tglbtnPause);
		tglbtnPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tglbtnPause.isSelected()) {
					autoUpdate.stop();
					tglbtnPause.setText("Resume");
				} else {
					autoUpdate.start();
					tglbtnPause.setText("Pause");
				}
			}
		});

		JSlider sldScale = new JSlider();
		sldScale.setBackground(new Color(255, 255, 204));
		sldScale.setOrientation(SwingConstants.VERTICAL);
		sldScale.setBounds(969, 402, 37, 227);
		mainPane.add(sldScale);
		sldScale.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				mainPane.loc.setScale(toScale(sldScale.getValue()));
				mainPane.repaint();
			}

		});

		θ2 = new JTextField();
		θ2.setText("5");
		θ2.setColumns(10);
		θ2.setBackground(new Color(255, 255, 204));
		θ2.setBounds(90, 98, 47, 24);
		mainPane.add(θ2);
				
		θ1 = new JTextField();
		θ1.setText("5");
		θ1.setColumns(10);
		θ1.setBackground(new Color(255, 255, 204));
		θ1.setBounds(90, 61, 47, 24);
		mainPane.add(θ1);
		
		r1 = new JTextField();
		r1.setText("5");
		r1.setColumns(10);
		r1.setBackground(new Color(255, 255, 204));
		r1.setBounds(292, 61, 47, 24);
		mainPane.add(r1);

		r2 = new JTextField();
		r2.setText("5");
		r2.setColumns(10);
		r2.setBackground(new Color(255, 255, 204));
		r2.setBounds(292, 98, 47, 24);
		mainPane.add(r2);
		
		m1 = new JTextField();
		m1.setText("5");
		m1.setColumns(10);
		m1.setBackground(new Color(255, 255, 204));
		m1.setBounds(504, 61, 47, 24);
		mainPane.add(m1);
		
		m2 = new JTextField();
		m2.setText("5");
		m2.setColumns(10);
		m2.setBackground(new Color(255, 255, 204));
		m2.setBounds(504, 98, 47, 24);
		mainPane.add(m2);
		
		g = new JTextField();
		g.setBounds(90, 15, 47, 24);
		mainPane.add(g);
		g.setBackground(new Color(255, 255, 204));
		g.setText("9.8");
		g.setColumns(10);

		JLabel label_1 = new JLabel("Degrees");
		label_1.setBounds(141, 101, 72, 18);
		mainPane.add(label_1);

		JLabel lblθ1 = new JLabel("\u03B81");
		lblθ1.setHorizontalAlignment(SwingConstants.CENTER);
		lblθ1.setBounds(14, 64, 72, 18);
		mainPane.add(lblθ1);

		JLabel lblθ2 = new JLabel("\u03B82");
		lblθ2.setHorizontalAlignment(SwingConstants.CENTER);
		lblθ2.setBounds(14, 101, 72, 18);
		mainPane.add(lblθ2);

		JLabel lblR1 = new JLabel("r1:");
		lblR1.setHorizontalAlignment(SwingConstants.CENTER);
		lblR1.setBounds(206, 64, 72, 18);
		mainPane.add(lblR1);

		JLabel lblR2 = new JLabel("r2:");
		lblR2.setHorizontalAlignment(SwingConstants.CENTER);
		lblR2.setBounds(206, 101, 72, 18);
		mainPane.add(lblR2);
		
		JLabel lblM1 = new JLabel("m1:");
		lblM1.setHorizontalAlignment(SwingConstants.CENTER);
		lblM1.setBounds(418, 64, 72, 18);
		mainPane.add(lblM1);
		
		JLabel lblM2 = new JLabel("m2:");
		lblM2.setHorizontalAlignment(SwingConstants.CENTER);
		lblM2.setBounds(418, 101, 72, 18);
		mainPane.add(lblM2);
		
		JLabel lblKg1 = new JLabel("Kg");
		lblKg1.setBounds(565, 64, 51, 18);
		mainPane.add(lblKg1);
		
		JLabel lblKg2 = new JLabel("Kg");
		lblKg2.setBounds(565, 101, 51, 18);
		mainPane.add(lblKg2);
		
		JLabel lblMeter = new JLabel("m");
		lblMeter.setBounds(357, 101, 41, 18);
		mainPane.add(lblMeter);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"m", "dm", "cm", "mm"}));
		comboBox.setBounds(353, 63, 51, 21);
		mainPane.add(comboBox);
		comboBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				lblMeter.setText((String)comboBox.getSelectedItem());
				lengthFactor = Math.pow(10,-comboBox.getSelectedIndex());
				mainPane.r1 = Double.valueOf(r1.getText())*lengthFactor;
				mainPane.r2 = Double.valueOf(r2.getText())*lengthFactor;
			}
		});

		sizer.addListener(this);
		sizer.addAllComponents();
		mainPane.initializedata();
		autoUpdate.start();
	}

	private double toScale(int val) {
		return  Math.pow(3, (double) (val - 50) / 50);
	}

	private class PresentPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private ActionListener update;
		private Locator loc = new Locator(this);

		private SimObject ball1, ball2;

		private double g = 9.8;// Constants

		private double // Variables
		t = 0, r1 = 5.0*lengthFactor, θ1 = (double) 0 / 180 * Math.PI, // in radians
				x1 = r1 * Math.sin(θ1), y1 = -r1 * Math.cos(θ1), r2 = 5.0*lengthFactor, θ2 = (double) 0 / 180 * Math.PI, // in radians
				x2 = x1 + r2 * Math.sin(θ2), y2 = y1 - r2 * Math.cos(θ2), m1 = 5, m2 = 5, w1 = 0, w2 = 0,
				PE = (r1+y1)*(m1)+(r1+r2+y2)*m2,KE =  0.5*m1*(w1*r1)*(w1*r1)+0.5*m2*(Math.pow(-Math.sin(θ1)*r1*w1-Math.sin(θ2)*r2*w2, 2)
						+Math.pow(Math.cos(θ1)*r1*w1+Math.cos(θ2)*r2*w2, 2)), E=PE+KE;// Angular speed by radians
		private ArrayList<Double[]> dataPck = new ArrayList<Double[]>();
		protected int millidt = 1;// time gap
		protected double dtime;// differential constant
		private double[] offset;
		private PresentPanel() {
			offset = new double[] { 0, r1/lengthFactor};
			loc.setyOffset(offset[1]);
			update = new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					moduleUpdate(t);
					t+=dtime;
					repaint();
				}
			};
			ball1 = new SimObject(loc);
			ball1.setLocation(x1, y1);
			ball1.setSize(1);
			ball1.setColor(new Color(255, 255, 22));
			ball2 = new SimObject(loc);
			ball2.setLocation(x2, y2);
			ball2.setSize(1);
			ball2.setColor(new Color(0, 163, 255));
			autoUpdate = new Timer(millidt, update);
		}
		
		protected void initializedata(){
			dataPck.clear();
			int i = 0;
			while(t<100){
				euler(0.0001);
				if(t>i*dtime){
					Double[] datas = new Double[]{r1, θ1, // in radians
							x1, y1, r2, θ2 , // in radians
							x2, y2, m1, m2, w1, w2,
							PE,KE,E};
					dataPck.add(datas);
					i++;
				}
			}
			t=0;
			dtime=(double) millidt / 1000;
		}
		protected void euler(double dt){
			offset[1]=r1/lengthFactor;
			double dfw1 = (-g*(2*m1 + m2)*Math.sin(θ1)-m2*g*Math.sin(θ1 - 2*θ2) - 2 *Math.sin(θ1 - θ2)* m2 *(w2*w2*r2 + w1*w1*r1*Math.cos(θ1 - θ2)))/
					(r1*(2*m1+m2-m2*Math.cos(2*θ1 - θ2)));
			double dfw2 = 2*Math.sin(θ1-θ2)*(w1*w1*r1*(m1 + m2) + g*(m1 + m2)*Math.cos (θ1) + w2*w2*r2*m2*Math.cos(θ1 - θ2))/
					(r2*(2*m1+m2-m2*Math.cos(2*θ1 - θ2)));
			w1+=dfw1*dt;
			w2+=dfw2*dt;
			θ1+=w1*dt;
			θ2+=w2*dt;
			t=t+dt;
			x1=r1*Math.sin(θ1);
			y1=-r1*Math.cos(θ1);
			x2=x1+r2*Math.sin(θ2);
			y2=y1-r2*Math.cos(θ2);
			PE = (r1+y1)*(m1)*g+(r1+r2+y2)*m2*g;
			KE = 0.5*m1*(w1*r1)*(w1*r1)+0.5*m2*(Math.pow(-Math.sin(θ1)*r1*w1-Math.sin(θ2)*r2*w2, 2)
					+Math.pow(Math.cos(θ1)*r1*w1+Math.cos(θ2)*r2*w2, 2));
			E=KE+PE;
			
		}

		protected void moduleUpdate(double t){
			int i = (int)(t/dtime);
			r1=dataPck.get(i)[0];
			θ1=dataPck.get(i)[1]; // in radians
			x1=dataPck.get(i)[2]; 
			y1=dataPck.get(i)[3];
			r2=dataPck.get(i)[4];
			θ2=dataPck.get(i)[5]; // in radians
			x2=dataPck.get(i)[6]; 
			y2=dataPck.get(i)[7]; 
			m1=dataPck.get(i)[8]; 
			m2=dataPck.get(i)[9];
			w1=dataPck.get(i)[10];  
			w2=dataPck.get(i)[11]; 
			PE=dataPck.get(i)[12];
			KE=dataPck.get(i)[13];
			E=dataPck.get(i)[14]; 
			ball2.setLocation(x2/lengthFactor, y2/lengthFactor);
			ball1.setLocation(x1/lengthFactor, y1/lengthFactor);
		}

		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setColor(Color.gray);
			g.drawOval(loc.toX(0 - 0.25), loc.toY(0 + 0.25), loc.toWidth(0.5), loc.toHeight(0.5));
			g.drawLine(loc.toX(x1/lengthFactor), loc.toY(y1/lengthFactor), loc.toX(x2/lengthFactor), loc.toY(y2/lengthFactor));
			g.drawLine(loc.toX(0), loc.toY(0), loc.toX(x1/lengthFactor), loc.toY(y1/lengthFactor));
			ball1.paintShape(g);
			ball2.paintShape(g);
			g.drawString("E= "+String.valueOf(E), loc.toX(1), loc.toY(0));
			g.drawString("t= "+String.valueOf(t), loc.toX(1), loc.toY(1));
			g.drawString("KE= "+String.valueOf(KE), loc.toX(1), loc.toY(-0.5));
			g.drawString("PE= " + String.valueOf(PE), loc.toX(1),loc.toY(-1));
		}
	}
}
