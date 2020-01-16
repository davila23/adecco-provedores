// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.model.base;

public interface IEntidad
{
    long getId();
    
    void setId(final long p0);
    
    String getDescripcion();
    
    void setDescripcion(final String p0);
    
    boolean getHabilitado();
    
    void setHabilitado(final boolean p0);
}
