// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.model;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Entity;
import java.io.Serializable;
import ar.com.adecco.proveedores.model.base.EntidadAuditada;

@Entity
@Table(schema = "compras", name = "PROVEEDORESTADO")
public class ProveedorEstado extends EntidadAuditada implements Serializable
{
    private static final long serialVersionUID = 7701029531009085912L;
    private Proveedor proveedor;
    private EstadoProveedor estadoAnterior;
    private EstadoProveedor estado;
    private String comentario;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROVEEDOR_ID")
    public Proveedor getProveedor() {
        return this.proveedor;
    }
    
    public void setProveedor(final Proveedor proveedor) {
        this.proveedor = proveedor;
    }
    
    @Enumerated(EnumType.STRING)
    public EstadoProveedor getEstado() {
        return this.estado;
    }
    
    public void setEstado(final EstadoProveedor estado) {
        this.estado = estado;
    }
    
    @Enumerated(EnumType.STRING)
    public EstadoProveedor getEstadoAnterior() {
        return this.estadoAnterior;
    }
    
    public void setEstadoAnterior(final EstadoProveedor estadoAnterior) {
        this.estadoAnterior = estadoAnterior;
    }
    
    public String getComentario() {
        return this.comentario;
    }
    
    public void setComentario(final String comentario) {
        this.comentario = comentario;
    }
}
