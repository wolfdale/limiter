package com.api.limiter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.properties")
public class ApplicationConfiguration {
    @Autowired
    private Environment environment;

    public String getApiRateLimit() {
        return environment.getProperty("api.rate.limit");
    }

}

