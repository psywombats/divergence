/**
 *  ClassWrapper.java
 *  Created on Apr 4, 2014 9:05:57 PM for project jgdbe
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgnse.tree;

import net.wombatrpgs.mgns.core.Annotations.Path;
import net.wombatrpgs.mgns.core.MainSchema;

/**
 * Wraps a MainSchema reference. For sorting purposes.
 */
public class ClassWrapper implements Comparable<ClassWrapper> {
	
	public Class<? extends MainSchema> clazz;
	
	public ClassWrapper(Class<? extends MainSchema> clazz) {
		this.clazz = clazz;
	}

	@Override public int compareTo(ClassWrapper other) {
		String[] path1 = path();
		String[] path2 = other.path();
		int at = 0;
		while (true) {
			if (path1.length <= at && path2.length <= at) {
				return clazz.getSimpleName().compareTo(other.clazz.getSimpleName());
			}
			if (path1.length <= at) return 1;
			if (path2.length <= at) return -1;
			String str1 = path1[at];
			String str2 = path2[at];
			int result = str1.compareTo(str2);
			if (result != 0) return result;
			at += 1;
		}
	}
	
	private String[] path() {
		if (clazz.isAnnotationPresent(Path.class)) {
			return clazz.getAnnotation(Path.class).value().split("/");
		} else {
			return new String[0];
		}
	}

}
