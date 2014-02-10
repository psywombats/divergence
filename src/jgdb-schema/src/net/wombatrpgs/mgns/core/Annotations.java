/**
 *  Annotations.java
 *  Created on Aug 4, 2012 7:28:44 PM for project MGNSchema
 *  Author: psy_wombats
 *  Contact: psy_wombats@wombatrpgs.net
 */
package net.wombatrpgs.mgns.core;

import java.lang.annotation.*;

/**
 * Contains all the annotations used in the schema.
 */
public class Annotations {
	
	/* GENERAL USE ************************************************************/
	
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	// if not present: FALSE
	public @interface EmptyAllowed {
		boolean value() default true;
	}
	
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	// if not present: UNUSED
	public @interface DefaultValue {
		String value();
	}
	
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	// if not present: FALSE
	public @interface Nullable {
		boolean value() default true;
	}
	
	/* LINKAGE ****************************************************************/
	
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface SchemaLink {
		Class<? extends MainSchema> value();
	}
	
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface InlineSchema {
		Class<? extends HeadlessSchema> value();
	}
	
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface EnumValue {
		String[] value();
	}
	
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface FileLink {
		// name of the subdirectory to look in (of project file), like "ui"
		String value();
	}
	
	/* SYSTEM *****************************************************************/
	
	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Path {
		String value() default "";
	}
	
	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface ExcludeFromTree {
		boolean value() default true;
	}
	
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Desc {
		String value();
	}
	
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface DisplayName {
		String value();
	}
	
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	// if not present: FALSE
	public @interface Header {
		boolean value() default true;
	}
	
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	// if not present: FALSE
	public @interface Immutable {
		boolean value() default true;
	}

}
