// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.model.base;

import java.util.Date;

public interface Auditable
{
    String getUsuarioCreacion();
    
    void setUsuarioCreacion(final String p0);
    
    String getUsuarioModificacion();
    
    void setUsuarioModificacion(final String p0);
    
    Date getFechaCreacion();
    
    void setFechaCreacion(final Date p0);
    
    Date getFechaModificacion();
    
    void setFechaModificacion(final Date p0);
}
