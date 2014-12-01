package com.corejsf.Services;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Rest application class that houses all the rest resources.
 * @author Gabriel
 *
 */
@ApplicationPath("rest")
public class RestApplication extends Application {
    /** Set of resource classes. */
	private final Set<Class<?>> classes;
    /**
     * Constructor that adds all resource classes to a hashset.
     */
	public RestApplication() {
		HashSet<Class<?>> c = new HashSet<>();
		c.add(EmployeeResource.class);
		c.add(AuthenticationResource.class);
		classes = Collections.unmodifiableSet(c);
	}

	@Override
	public Set<Class<?>> getClasses() {
		return classes;
	}
}
