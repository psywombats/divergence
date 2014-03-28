/**
 *  InputEvent.java
 *  Created on Jan 22, 2014 12:52:22 AM for project saga
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgne.io;

import net.wombatrpgs.mgneschema.io.data.InputButton;

/**
 * This used to be an inputbutton-boolean pair, but now it's a slightly more
 * complicated struct. Command maps turn these into commands and keymaps produce
 * them from raw input.
 */
public class InputEvent {
	
	public InputButton button;
	public EventType type;
	
	public InputEvent(InputButton button, EventType type) {
		this.button = button;
		this.type = type;
	}
	
	/** Kryo constructor */
	protected InputEvent() { }
	
	public enum EventType {
		PRESS,
		RELEASE,
		HOLD,
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.button == null) ? 0 : this.button.hashCode());
		result = prime * result + ((this.type == null) ? 0 : this.type.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		InputEvent other = (InputEvent) obj;
		if (this.button != other.button) return false;
		if (this.type != other.type) return false;
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return button + " " + type;
	}

}
