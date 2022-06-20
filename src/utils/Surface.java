package utils;

import java.util.ArrayList;
import java.util.List;

import ui.Locator;

public class Surface extends Dataset{

	protected List<ArrayList<double[]>>surface;
	
	public List<ArrayList<double[]>> getSurface() {
		return surface;
	}
	
	@Override
	public void initialize(double... params) {
		
	}
	
	protected Texture texture = new Texture();
	
	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	
	public Texture getTexture() {
		return texture;
	}

	@Override
	public boolean isSelected(int selectionRange, int X, int Y, Locator locator) {
		// TODO Implement this
		return false;
	}
}