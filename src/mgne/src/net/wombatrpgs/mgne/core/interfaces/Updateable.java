package net.wombatrpgs.mgne.core.interfaces;

/**
 * Anything that's updated based on elapsed should implement this.
 */
public interface Updateable {
	
	/**
	 * Updates the object based on elapsed real time.
	 * @param 	elapsed				Time elapsed since last time update was
	 * 								called, in seconds
	 */
	public void update(float elapsed);

}
