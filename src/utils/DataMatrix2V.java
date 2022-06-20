package utils;

import java.util.ArrayList;
import java.util.List;

public class DataMatrix2V extends Surface {
	
	public void setSurface(List<ArrayList<double[]>> surface) {
		this.surface = surface;
	}
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
}
