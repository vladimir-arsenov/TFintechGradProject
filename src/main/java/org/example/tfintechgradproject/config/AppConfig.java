package org.example.tfintechgradproject.config;

import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.io.WKTReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public WKTReader wktReader() {
        return new WKTReader(new GeometryFactory(new PrecisionModel(), 4326));
    }

}
