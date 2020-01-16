// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.service;

import org.slf4j.LoggerFactory;
import ar.com.adecco.dominio.contacto.ConfiguracionModulo;
import ar.com.adecco.dominio.persona.PersonaRelacion;
import ar.com.adecco.dominio.contacto.Modulo;
import javax.inject.Inject;
import ar.com.adecco.proveedores.daos.ConfiguracionModuloDao;
import org.slf4j.Logger;
import javax.inject.Named;
import java.io.Serializable;

@Named
public class ConfiguracionModuloService implements Serializable
{
    private static final long serialVersionUID = 1730075414287229L;
    private static final Logger logger;
    @Inject
    private ConfiguracionModuloDao configuracionModuloDao;
    
    public void updateLastAccess(final Modulo.Codigo codigo, final Long n, final PersonaRelacion.TipoRelacion tipoRelacion, final Long n2) {
        this.configuracionModuloDao.updateLastAccess(codigo, n, tipoRelacion, n2);
    }
    
    public void updateLastAccess(final Modulo.Codigo codigo, final PersonaRelacion personaRelacion) {
        this.configuracionModuloDao.updateLastAccess(codigo, personaRelacion);
    }
    
    public void updateLastAccess(final ConfiguracionModulo configuracionModulo) {
        this.configuracionModuloDao.updateLastAccess(configuracionModulo);
    }
    
    static {
        logger = LoggerFactory.getLogger((Class)ConfiguracionModuloService.class);
    }
}
