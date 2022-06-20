package utils;

import static codeLibrary.Algorithms.*;

public class Texture {
	/**
	 * Finds the brightness factor resulting from the reflection off the surface
	 * @param v incident vector
	 * @param n normal vector of the relection plane
	 * @param o directional vector of observing angle
	 * @return reflected vector
	 */
	public float BRF(double[]v, double[]n, double[] o) {
		return (float) (dot(o,add(multiply(-2*dot(v,n),n),v))/magnitude(n)/magnitude(n)/magnitude(v)/7.5+0.5);
	}
}
