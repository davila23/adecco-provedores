// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.commons.producers;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.DefaultConfigurationBuilder;
import org.apache.commons.configuration.Configuration;

public class ConfigProducer
{
    @Produces
    @Config
    @ApplicationScoped
    public Configuration getConfiguration() {
        Configuration configuration;
        try {
            configuration = new DefaultConfigurationBuilder("configuration.xml").getConfiguration();
        }
        catch (ConfigurationException ex) {
            return null;
        }
        return configuration;
    }
}
