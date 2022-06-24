package examples;

import javax.swing.JFrame;
import ui.DataPane2D;
import utils.Function1V;

public class SimpleGrapher {
	public static void main(String args[]) {
		JFrame frame = new JFrame("grapher");
		frame.setSize(700, 500);
		DataPane2D dataPane = new DataPane2D();
		frame.add(dataPane);
		
		Function1V func = new Function1V((x)->Math.sin(x));
		dataPane.addDataset(func);
		frame.setVisible(true);
	}
}
