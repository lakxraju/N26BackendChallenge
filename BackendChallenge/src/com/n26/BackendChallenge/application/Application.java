package com.n26.BackendChallenge.application;

import org.glassfish.jersey.server.ResourceConfig;

public class Application extends ResourceConfig {
	
    public Application() {
        // Define the package which contains the service classes.
        packages("com.n26.BackendChallenge.services");
    }

}
