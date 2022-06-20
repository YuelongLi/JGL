package ui;

import javax.swing.*;
import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;

/**
 * JSizer class provides autosizing mechanism for UIs deisigned with absolute layout.
 * <div> It is a subclass of JComponent and it can be utilized as a flexible layout</div>
 * @version 1.0
 * @author Yuelong Li 
 * @see JComponent
 */
public class JSizer{
	/**
	 * The objects of size adjustment
	 */ 
	protected Components components = new Components();
	/**
	 * stateListener detects any state changing in of the main JFrame
	 */
	protected WindowStateListener stateListener;
	/**
	 * Components in this list will be monitered, every change in their size will cause a collective resize
	 */ 
	protected HashMap<Integer,ComponentAdapter> listener = new HashMap<Integer,ComponentAdapter>(0);
	protected HashMap<Integer,ComponentAdapter> fontListener = new HashMap<Integer,ComponentAdapter>(0);
	/**
	 * List of indexes of the monitered components, every change in their size will cause a collective resize
	 */ 
	public List<Integer> listenerIndex = new ArrayList<Integer>(0);
	/**
	 * Standarlized structural configuration
	 * @param Dimension size
	 * @param Dimension position
	 */
	Configs orgConfigs = new Configs();
	/**
	 * Size of components currently
	 * @param Dimension size
	 * @param Dimension position
	 */
	Configs currentConfigs = new Configs();
	
	/**
	 * Ratio between current configuration and the original configuration, updated in every group resize
	 */
	double[] ratio=new double[2];
	/**
	 * Difference between current configuration and the original configuration, updated in every group resize
	 */
	int[] difference=new int[2];
	/**
	 * <div><b>Constructor:</b>
	 * <div> 
	 * Takes in the JComponents and adds them into the resize waiting list,
	 * <div>and initializes the stuctural configuration for all components
	 * <p>
	 * @param ComponentList
	 * <div> the first element  of ComponentList is the scaling standard by default
	 * </p>
	 * @see  codeLibrary.JSizer.orgConfig
	 */
	public JSizer(JComponent... ComponentList){
		for(JComponent Component:ComponentList){
			Dimension[] config = new Dimension[2];
			components.add(Component);
			config[0]=Component.getSize();
			config[1]=Dimension(Component.getLocation());
			orgConfigs.add(config);
			currentConfigs.add(config.clone());
		}
	}
	public JSizer(Component... ComponentList){
		for(Component Component:ComponentList){
			Dimension[] config = new Dimension[2];
			components.add(Component);
			config[0]=Component.getSize();
			config[1]=Dimension(Component.getLocation());
			orgConfigs.add(config);
			currentConfigs.add(config.clone());
		}
	}
	public void addAllComponents(){
		for(Component component: ((Container) components.get(0)).getComponents()){
			add((JComponent)component);
		}
		
	}
	public void setBounds(int x, int y, int width, int height){
		setLocation(x,y);
		setSize(width,height);
	}
	public void setLocation(Point p){
		reLocate(p,0);
	}
	public void setLocation(int x, int y){
		reLocate(x,y,0);
	}
	public Point getLocation(){
		return getLocation(0);
	}
	/**
	 * Returns the position of the component defined by index
	 * @param index defines the index of the component in Components
	 * @return The point at which the component is located
	 */
	public Point getLocation(int index){
		return components.get(index).getLocation();
	}
	/**
	 * relocates all the components according to the new position of the main component p
	 * @param p the new position of the component
	 */
	public void relocateAll(Point p){
		relocateAll(p,0);
	}
	/**
	 * relocates all the component according to the new position of the component located at index
	 * @param p the new position of the component
	 * @param index the index of component in Components
	 */
	public void relocateAll(Point p,int index){
		ratio = compare(orgConfigs.get(0)[0],currentConfigs.get(0)[0]);
		difference = subtract(p,Point(orgConfigs.get(index)[1]));
		for(int i = components.size()-1; i>=0;i--){
			changeLocation(i);
		}
	}
	/**
	 * relocates all the component according to the new position of the main component
	 * @param x the new x position of the component
	 * @param y the new y position of the component
	 */
	public void relocateAll(int x,int y){
		relocateAll(x,y,0);
	}
	/**
	 * relocates all the component according to the new position of the component located at index
	 * @param x the new x position of the component
	 * @param y the new y position of the component
	 * @param index the index of component in Components
	 */
	public void relocateAll(int x,int y,int index){
		relocateAll(Point(Dimension(x,y)),0);
	}
	/**
	 * relocates the component that locates at index in Components
	 * @param p specifies the new location of the component
	 * @param index specifies the component to be relocated
	 * @see setLocation(Point p)
	 */
	public void reLocate(Point p, int index){
		reLocate(p.x,p.y,index);
	}
	/**
	 * relocates the component that locates at index in Components to x, y
	 * @param x the new x coordinate of the component
	 * @param y the new y coordinate of the component
	 * @param index
	 * @see setLocation(int x, int y)
	 */
	public void reLocate(int x, int y, int index){
		ratio = compare(orgConfigs.get(0)[0],currentConfigs.get(0)[0]);
		difference=subtract(Point(x,y),Point(orgConfigs.get(index)[1]));
		changeLocation(index);
	}
	/**
	 * resizes all components in JSizer as a whole so that the size of main component in components will become "size"
	 * @param size the dimension specifying the new size of this component
	 */
	public void setSize(Dimension size){
		resizeAll(size);
	}
	/**
	 * Resizes all the components as a whole so that the main component has width width and height height. 
	 * @param width - the new width of this component in pixels
	 * @param height - the new height of this component in pixels
	 */
	public void setSize(int width,int height){
		resizeAll(width,height);
	}
	/**
	 * Returns the size of the main component
	 */
	public Dimension getSize(){
		return components.get(0).getSize();
	}
	/**
	 * Returns the size of the component at the index
	 * @param index The index at which the component is located at.
	 */
	public Dimension getSize(int index){
		return components.get(index).getSize();
	}
	/**
	 * Resizes all the components by an identical ratio defined by the ratio between the new size and the original size. 
	 * @param size The new size the first component will be changed to
	 */
	public void resizeAll(Dimension size){
		resizeAll(size,0);
	}
	/**
	 * Resizes all the components by and identical ratio.
	 * @param size The new size of the component
	 * @param index The index at which the comonent is located in Components
	 */
	public void resizeAll(Dimension size,int index){
		ratio = compare(size,orgConfigs.get(index)[0]);
		for(int i = components.size()-1; i>=0;i--){
			changeSize(i);
		}
	}
	/**
	 * Resizes all the components by and identical ratio.
	 * @param width The new width of the first component
	 * @param height The new height of the first component
	 */
	public void resizeAll(int width, int height){
		resizeAll(new Dimension(width,height),0);
	}
	/**
	 * Resizes all the components by and identical ratio.
	 * @param width The new width of the first component
	 * @param height The new height of the first component
	 * @param index  The index at which the comonent is located in Components
	 */
	public void resizeAll(int width, int height, int index){
		resizeAll(new Dimension(width,height),0);
	}
	/**
	 * Resize the first component in Components to the new size
	 * @param size the new size of the component
	 * @param index the index at which the component being resized is located
	 */
	public void resize(Dimension size, int index){
		double[] newConfigRatio = compare(size, currentConfigs.get(index)[0]);
		orgConfigs.get(index)[0] = multiply(newConfigRatio,orgConfigs.get(index)[0]);
		currentConfigs.get(index)[0] = size;
		components.get(index).setSize(size);
	}
	/**
	 * Resize the first component in Components to the new size
	 * @param width The new width of the first component
	 * @param height The new height of the first component
	 */
	public void resize(int width, int height, int index){
		resize(Dimension(width,height), index);
	}
	
	/**
	 * Takes in the component and adds it into Components, and stores all the configurations of it.
	 * @param component
	 */
	public void add(JComponent Component){
		components.add(Component);
		ratio = compare(orgConfigs.get(0)[0],currentConfigs.get(0)[0]);
		Dimension[] config = new Dimension[2];
		config[0]=Component.getSize();
		config[1]=Dimension(Component.getLocation());
		currentConfigs.add(config);
		Dimension[] orgConfig = new Dimension[2];
		orgConfig[0]=multiply(ratio,config[0]);
		orgConfig[1]=multiply(ratio,config[1]);
		orgConfigs.add(orgConfig);
	}
	/**
	 * Removes the component located at index in Components array
	 * @param index 
	 */
	public void remove(int index){
		removeListener(index);
		components.remove(index);
		orgConfigs.remove(index);
		currentConfigs.remove(index);
	}
	public void repaint(){
		for(Component component: components)component.repaint();
	}

	/**
	 * Adds a component to the Listener list
	 * @param index the index at which the component is located
	 */
	public void addListener(int index){
		listener.put(Integer.valueOf(index),new ComponentAdapter(){
			public void componentResized(ComponentEvent e){
				resizeAll(components.get(index).getSize(),index);
			}
		});
		listenerIndex.add(Integer.valueOf(index));
		components.get(index).addComponentListener(listener.get(index));
	}
	/**
	 * specifies the component within which the text are needed to be changed dynamically
	 * @param index
	 */
	public void addFontListener(int index){
		fontListener.put(Integer.valueOf(index),new ComponentAdapter(){
			public void componentResized(ComponentEvent e){
				JLabel label = (JLabel)components.get(0);
				Font labelFont = label.getFont();
				String labelText = label.getText();

				int stringWidth = label.getFontMetrics(labelFont).stringWidth(labelText);
				int componentWidth = label.getWidth();

				// Find out how much the font can grow in width.
				double widthRatio = (double)componentWidth / (double)stringWidth;

				int newFontSize = (int)(labelFont.getSize() * widthRatio);
				int componentHeight = label.getHeight();

				// Pick a new font size so it will not be larger than the height of label.
				int fontSizeToUse = Math.min(newFontSize, componentHeight);

				// Set the label's font size to the newly determined size.
				label.setFont(new Font(labelFont.getName(), Font.PLAIN, fontSizeToUse));
			}
		});
		components.get(index).addComponentListener(fontListener.get(index));
	}
	
	/**
	 * Adds a JFrame as the size listener. Whenever its size is changed, the size
	 * is passed into the resizeAll method along with the reference of the first component in components
	 * @param component
	 */
	public void addListener(JFrame frame){
		components.add(frame);
		Dimension orgSize[] = {frame.getSize()};
		orgConfigs.add(-1,orgSize);
		currentConfigs.add(-1,orgSize);
		listener.put(Integer.valueOf(-1),new ComponentAdapter(){
			public void componentResized(ComponentEvent e){
				Dimension actualSize=new Dimension(components.get(-1).getWidth()-18,components.get(-1).getHeight()-47);
				resizeAll(actualSize,-1);
			}
		});
		stateListener = new WindowStateListener(){
			public void windowStateChanged(WindowEvent e){
				Dimension actualSize=new Dimension(components.get(-1).getWidth(),components.get(-1).getHeight()-37);
				resizeAll(actualSize,-1);
			}
		};
		listenerIndex.add(Integer.valueOf(-1));
		components.get(-1).addComponentListener(listener.get(-1));
		((JFrame) components.get(-1)).addWindowStateListener(stateListener);
	}
	/**
	 * removes the size listener of the component locates at index
	 * @param index
	 */
	public void removeListener(int index){
		if(listenerIndex.contains(index)){
			components.get(index).removeComponentListener(listener.get(index));
			int i = 0;
			for(int pass:listenerIndex){
				if(pass==index){
					listenerIndex.remove(i);
					break;
				}
				i++;
			}
		}
		if(index==-1){
			((JFrame) components.get(-1)).removeWindowStateListener(stateListener);
		}
	}
	
	/**
	 * Takes in the component index to locate the component in components array and resize it according to the ratio
	 * @param index the index where the component is located in Components
	 * @param ratio the resizing ratio between the original component size and the new component size
	 */
	protected void changeSize(int index){
		Dimension[] orgConfig = orgConfigs.get(index);
		Dimension[] config = new Dimension[2];
		config[0]=multiply(ratio,orgConfig[0]);
		config[1]=multiply(ratio,orgConfig[1]);
		components.get(index).setBounds(config[1].width,config[1].height,config[0].width,config[0].height);
		components.get(index).revalidate();
		currentConfigs.get(index)[0]=config[0];
		currentConfigs.get(index)[1]=config[1];
		
	}
	/**
	 * Takes in the component index to locate the component in components array and relocate it according to the ratio
	 * @param index the index where the component is located in Components
	 * @param difference the relocating difference between the original component location and the new component location
	 */
	protected void changeLocation(int index){
		Dimension[] currentConfig = currentConfigs.get(index);
		Point[] config = new Point[2];
		config[0]=Point(currentConfig[0]);
		config[1]=Point(currentConfig[1].width+difference[0],currentConfig[1].height+difference[1]);
		components.get(index).setBounds(config[1].x,config[1].y,config[0].x,config[0].y);
		components.get(index).repaint();
		currentConfigs.get(index)[1]=Dimension(config[1]);
		orgConfigs.get(index)[1]=multiply(ratio,Dimension(config[1]));
	}

	/**
	 * returns the ratio between to components
	 * @param size1 the size of the first component
	 * @param size2 the size of the second component
	 * @return the ratio between to components
	 */
	protected double[] compare(Dimension size1, Dimension size2){
		double[] ratio = new double[2];
		ratio[0] = size1.getWidth()/size2.getWidth();
		ratio[1] = size1.getHeight()/size2.getHeight();
		return ratio;
	}
	private int[] subtract(Point a, Point b){
		int[] point=new int[2];
		point[0] = a.x-b.x;
		point[1] = a.y-b.y;
		return point;
	}
	private Dimension multiply(double[] ratio,Dimension size){
		return new Dimension((int)Math.round(ratio[0]*size.getWidth()),(int)Math.round(ratio[1]*size.getHeight()));
	}
	private Point Point(Dimension a){
		return new Point(a.width,a.height);
	}
	private Point Point(int... point){
		return new Point(point[0], point[1]);
	}
	private Dimension Dimension(Point a){
		return new Dimension((int)a.getX(), (int)a.getY());
	}
	private Dimension Dimension(int... dimension){
		return new Dimension(dimension[0], dimension[1]);
	}
	/**
	 * An Components is a customized ArrayList which contains all the components regarded
	 * @category ArrayList
	 * @author Yuelong
	 *
	 */
	public class Components extends ArrayList<Component>{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		/**
		 * this item is usually the main frame in which the rest of the components are rested in
		 */
		private Component item0;
		public boolean add(JComponent component){
			super.add(component);
			return true;
		}
		public boolean add(JFrame frame){
			item0=frame;
			return true;
		}
		public void add(int index,Component component){
			if(index>=0){
				super.add(index, component);
			}
			else item0=component;
		}
		public Component get(int i){
			if(i>=0){
				return super.get(i);
			}
			else return item0;
		}
		public Component remove(int index){
			Component instance = get(index);
			if(index>=0){
				super.remove(index);
				return instance;
			}
			else {
				item0=null;
				return instance;
			}
		}
		public Component set(int index,Component component){
			Component instance = get(index);
			if(index>=0){
				super.set(index,component);
				return instance;
			}
			else {
				item0 = (JFrame)component;
				return instance;
			}
		}
	}
	/**
	 * Configs is a customized arrayList<Dimension[]>
	 * @author yuelong
	 *
	 */
	class Configs extends ArrayList<Dimension[]>{
		/**
		 *  
		 */
		private static final long serialVersionUID = 1L;
		/**
		 * this item is usually the main frame in which the rest of the components are rested in
		 */
		private Dimension[] item0=new Dimension[1];
		public void add(int Index,Dimension[] dimension){
			if(Index>=0){
				super.add(Index, dimension);
			}
			else item0[0]=new Dimension((dimension[0].width-18), (dimension[0].height-47));
		}
		public boolean add(Dimension[] dimension){
			super.add(dimension);
			return true;
		}
		public Dimension[] get(int i){
			if(i>=0){
				return super.get(i);
			}
			else return item0;
		}
		public Dimension[] remove(int index){
			Dimension[] instance = get(index);
			if(index>=0){
				super.remove(index);
				return instance;
			}
			else {
				item0=null;
				return instance;
			}
		}
		public Dimension[] set(int index,Dimension[] component){
			Dimension[] instance = get(index);
			if(index>=0){
				super.set(index,component);
				return instance;
			}
			else {
				item0[0].setSize((component[0].width-18), (component[0].height-47));
				return instance;
			}
		}
	}
}
/*
 * © Copyright 2016
 * Cannot be used without authorization
 */