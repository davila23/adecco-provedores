// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.daos;

import org.slf4j.LoggerFactory;
import java.util.Date;
import ar.com.adecco.dominio.persona.PersonaRelacion;
import ar.com.adecco.dominio.contacto.Modulo;
import org.slf4j.Logger;
import javax.inject.Named;
import ar.com.adecco.dominio.contacto.ConfiguracionModulo;

@Named
public class ConfiguracionModuloDao extends AbstractDao<ConfiguracionModulo>
{
    private static final long serialVersionUID = -3123595686734457L;
    private static final Logger log;
    
    @Override
    public Class<ConfiguracionModulo> getClaseEntidad() {
        return ConfiguracionModulo.class;
    }
    
    public void updateLastAccess(final Modulo.Codigo codigo, final Long n, final PersonaRelacion.TipoRelacion tipoRelacion, final Long n2) {
        if (tipoRelacion != PersonaRelacion.TipoRelacion.CLIENTE) {
            return;
        }
        this.entityManager.createQuery("update " + codigo.getReferencedClass().getName() + " cm " + "set cm.fechaUltimoAcceso = :ahora " + "where exists (" + "select 1 " + "from PersonaRelacionCliente pr " + "where pr.persona.id = :pId " + "and pr.cliente.id = :cId " + "and pr.habilitado = true " + "and cm.personaRelacion = pr" + ")").setParameter("ahora", (Object)new Date()).setParameter("pId", (Object)n).setParameter("cId", (Object)n2).executeUpdate();
    }
    
    public void updateLastAccess(final Modulo.Codigo codigo, final PersonaRelacion personaRelacion) {
        this.entityManager.createQuery("update " + codigo.getReferencedClass().getName() + " cm " + "set cm.fechaUltimoAcceso = :ahora " + "where cm.personaRelacion = :pr").setParameter("ahora", (Object)new Date()).setParameter("pr", (Object)personaRelacion).executeUpdate();
    }
    
    public void updateLastAccess(final ConfiguracionModulo configuracionModulo) {
        this.updateLastAccess(configuracionModulo.getModuloCodigo(), configuracionModulo.getPersonaRelacion());
    }
    
    static {
        log = LoggerFactory.getLogger((Class)ConfiguracionModuloDao.class);
    }
}
