// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.model.base;

import javax.persistence.Column;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
public abstract class Entidad implements IEntidad, Serializable
{
    private static final long serialVersionUID = 6969807758778489394L;
    private long id;
    private String descripcion;
    private boolean habilitado;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Override
    public long getId() {
        return this.id;
    }
    
    @Override
    public void setId(final long id) {
        this.id = id;
    }
    
    @Override
    public String getDescripcion() {
        return this.descripcion;
    }
    
    @Override
    public void setDescripcion(final String descripcion) {
        this.descripcion = descripcion;
    }
    
    @Column(length = 1)
    @Override
    public boolean getHabilitado() {
        return this.habilitado;
    }
    
    @Override
    public void setHabilitado(final boolean habilitado) {
        this.habilitado = habilitado;
    }
    
    @Override
    public int hashCode() {
        return 31 * 1 + (int)(this.id ^ this.id >>> 32);
    }
    
    @Override
    public boolean equals(final Object o) {
        return this == o || (o != null && this.getClass().isAssignableFrom(o.getClass()) && this.getId() == Entidad.class.cast(o).getId() && this.getId() != 0L);
    }
}
