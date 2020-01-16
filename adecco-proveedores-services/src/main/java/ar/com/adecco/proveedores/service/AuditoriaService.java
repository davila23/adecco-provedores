// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.service;

import javax.persistence.PreUpdate;
import java.util.Date;
import java.security.Principal;
import javax.persistence.PrePersist;
import ar.com.adecco.proveedores.model.base.Auditable;
import java.util.Set;
import javax.enterprise.context.spi.Contextual;
import javax.enterprise.inject.spi.Bean;
import java.lang.reflect.Type;
import java.lang.annotation.Annotation;
import javax.naming.NamingException;
import javax.naming.InitialContext;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Named;
import java.io.Serializable;

@Named
public class AuditoriaService implements Serializable
{
    private static final long serialVersionUID = -4311426640069246917L;
    private static final String usuarioPorDefecto = "SYSTEM";
    
    private static BeanManager getBeanManager() {
        try {
            return (BeanManager)new InitialContext().lookup("java:comp/BeanManager");
        }
        catch (NamingException ex) {
            System.err.println("AuditoriaService.getBeanManager(): javax.naming.NameNotFoundException: java:comp/BeanManager");
            return null;
        }
    }
    
    private <T> T getBeanByClass(final Class<T> clazz) {
        final BeanManager beanManager = getBeanManager();
        if (beanManager == null) {
            return null;
        }
        final Set<Bean<?>> beans = beanManager.getBeans((Type) clazz, new Annotation[0]);
        if (beans == null || beans.isEmpty()) {
            return null;
        }
        final Bean bean = beans.iterator().next();
        return (T)beanManager.getReference(bean, (Type)clazz, beanManager.createCreationalContext((Contextual)bean));
    }
    
    @PrePersist
    public void prePersist(final Object o) {
        if (o instanceof Auditable) {
            this.agregarDatosAuditoriaAlta((Auditable)o);
        }
    }
    
    public Auditable agregarDatosAuditoriaAlta(final Auditable auditable) {
        final Principal principal = this.getBeanByClass(Principal.class);
        final String s = (principal != null) ? principal.getName() : "SYSTEM";
        final Date date = new Date();
        auditable.setFechaCreacion(date);
        auditable.setFechaModificacion(date);
        auditable.setUsuarioCreacion(s);
        auditable.setUsuarioModificacion(s);
        return auditable;
    }
    
    @PreUpdate
    public void preUpdate(final Object o) {
        if (o instanceof Auditable) {
            this.agregarDatosAuditoriaModificacion((Auditable)o);
        }
    }
    
    public Auditable agregarDatosAuditoriaModificacion(final Auditable auditable) {
        final Principal principal = this.getBeanByClass(Principal.class);
        final String s = (principal != null) ? principal.getName() : "SYSTEM";
        final Date date = new Date();
        if (auditable.getFechaCreacion() == null) {
            auditable.setFechaCreacion(date);
        }
        if (auditable.getUsuarioCreacion() == null) {
            auditable.setUsuarioCreacion(s);
        }
        auditable.setFechaModificacion(date);
        auditable.setUsuarioModificacion(s);
        return auditable;
    }
}
