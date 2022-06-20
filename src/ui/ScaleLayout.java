package ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * A wrap around the idea of JSizer that implements LayoutManager
 * for smooth and reliable integration with JFrame visualizations
 * 
 * @author Yuelong Li
 *
 */
public class ScaleLayout implements LayoutManager, Serializable {
	/**
	 * Serialized id
	 */
	private static final long serialVersionUID = 838736935151200888L;
	
	/**
	 * The components that this layout are tracking
	 */
	ArrayList<Component> components = new ArrayList<Component>();
	/**
	 * Defines the bounds that containers should model their
	 * scales after
	 */
	ArrayList<Rectangle> layout = new ArrayList<Rectangle>();
	
	/**
	 * The object that gets listened for benchmarking size
	 * changes
	 */
	Component benchmarker;
	Dimension benchmark = new Dimension(0,0);
	
	Dimension preferredSize = new Dimension(15,15);
	
	@Override
	public void addLayoutComponent(String name, Component component) {
		components.add(component);
		Rectangle bounds = component.getBounds();
		layout.add(bounds);
	}

	@Override
	public void removeLayoutComponent(Component component) {
		int index = components.indexOf(component);
		if(index!=-1) {
			components.remove(component);
			layout.remove(index);
		}
	}

	
	/**
	 * Holder the current dimensions of the benchmarker,
	 * only used locally but preinstantiated for performance
	 */
	private Dimension dimensionHolder = new Dimension(0,0);
	@Override
	public void layoutContainer(Container container) {
		if(benchmarker==null)
			return;
		benchmarker.getSize(dimensionHolder);
		double xScale = dimensionHolder.getWidth()/benchmark.getWidth();
		double yScale = dimensionHolder.getHeight()/benchmark.getHeight();
		for(int i = 0; i<components.size(); i++) {
			Rectangle layoutBounds = layout.get(i);
			components.get(i).setBounds((int)(xScale*layoutBounds.x), (int)(yScale*layoutBounds.y), 
					(int)(xScale*layoutBounds.width), (int)(yScale*layoutBounds.height));
		}
	}

	@Override
	public Dimension minimumLayoutSize(Container container) {
		return new Dimension(0,0);
	}

	@Override
	public Dimension preferredLayoutSize(Container container) {
		return preferredSize;
	}

	public void setBenchmarker(Component benchmarker) {
		ScaleLayout.this.benchmarker = benchmarker;
		benchmarker.getSize(benchmark);
	}
	
	public void addAllComponents(Container container) {
		int ncomp = container.getComponentCount();
		for(int i = 0; i<ncomp; i++) {
			addLayoutComponent("",container.getComponent(i));
		}
	}
}
