// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.commons.producers;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import java.util.List;
import java.util.Arrays;
import org.dozer.DozerBeanMapper;

public class BeanMapperProducer
{
    @Produces
    @BeanMapper
    @ApplicationScoped
    public DozerBeanMapper getBeanMapper() {
        final DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();
        dozerBeanMapper.setMappingFiles((List)Arrays.asList("dozer.xml"));
        return dozerBeanMapper;
    }
}
