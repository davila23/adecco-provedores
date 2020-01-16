// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.model.base;

import javax.persistence.Column;
import java.util.Date;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
public abstract class EntidadAuditada extends Entidad implements Serializable, EntidadAuditable
{
    private static final long serialVersionUID = 7562681191782335576L;
    private String usuarioCreacion;
    private String usuarioModificacion;
    private Date fechaCreacion;
    private Date fechaModificacion;
    
    @Column(nullable = false)
    @Override
    public String getUsuarioCreacion() {
        return this.usuarioCreacion;
    }
    
    @Override
    public void setUsuarioCreacion(final String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }
    
    @Column(nullable = false)
    @Override
    public String getUsuarioModificacion() {
        return this.usuarioModificacion;
    }
    
    @Override
    public void setUsuarioModificacion(final String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }
    
    @Column(nullable = false)
    @Override
    public Date getFechaCreacion() {
        return this.fechaCreacion;
    }
    
    @Override
    public void setFechaCreacion(final Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    
    @Column(nullable = false)
    @Override
    public Date getFechaModificacion() {
        return this.fechaModificacion;
    }
    
    @Override
    public void setFechaModificacion(final Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }
}
