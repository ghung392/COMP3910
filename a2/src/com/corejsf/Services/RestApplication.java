package com.corejsf.Services;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("rest")
public class RestApplication extends Application {

	private final Set<Class<?>> classes;

	public RestApplication()
	{
		HashSet<Class<?>> c = new HashSet<>();
		c.add(EmployeeResource.class);
		classes = Collections.unmodifiableSet(c);
	}

	@Override
	public Set<Class<?>> getClasses()
	{
		return classes;
	}
}
