/**
 *  ShadowResult.java
 *  Created on Jan 16, 2015 1:34:46 AM for project mgne
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.physics;

import com.badlogic.gdx.math.Vector2;

/**
 * Struct for shadowy results from casting.
 */
public class ShadowResult {
	
	public float[] umbraVertices;
	public Fin fin1, fin2;
	
	public class Fin {
		public Vector2 root;
		public Vector2 umbra;
		public Vector2 penumbra;
		
		public float[] toVertices() {
			return new float[] {
					root.x, root.y,
					umbra.x, umbra.y,
					penumbra.x, penumbra.y
			};
		}
	}

}
