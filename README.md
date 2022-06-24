# Java-UI
A light weight toolset that allows you to build Java UI and create plot visualizations instantly.

## Components
* ScaleLayout: an implementation of layout manager that takes care of all the sizing
* Locator: Graphics coordinatization
* DataPane2D: Function based high quality plotting
* DataPane3D: Plotting of 3-dimensional surfaces and data
* Dataset and its subclasses

## Basic Usage
### Code
```java
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
```
### Result
![basic-usage](img/basic-usage.png)

## Examples
- Gradient descent visualization
<img src="img/gradient-descent-example.png" alt="GradientDescent" width="75%"/>

- Double pendulum simulation
[<img src="img/double-pendulum.png" alt="DoublePendulum" width="75%"/>](src/examples/DoublePendulum.java)

- Functional plotting
<img src="img/analysis-frame.png" alt="AnalysisFrame" width="75%"/>

- Data visualization
<img src="img/data-vis.png" alt="DataVis" width="75%"/>


## Dependency
Miscelaneous algorithms from LDK library
