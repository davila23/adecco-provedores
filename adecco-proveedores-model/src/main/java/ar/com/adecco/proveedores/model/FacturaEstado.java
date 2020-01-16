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
@Table(schema = "compras", name = "FACTURAESTADO")
public class FacturaEstado extends EntidadAuditada implements Serializable
{
    private static final long serialVersionUID = 738167529110182825L;
    private Comprobante factura;
    private EstadoFactura estadoAnterior;
    private EstadoFactura estado;
    private String comentario;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FACTURA_ID")
    public Comprobante getFactura() {
        return this.factura;
    }
    
    public void setFactura(final Comprobante factura) {
        this.factura = factura;
    }
    
    @Enumerated(EnumType.STRING)
    public EstadoFactura getEstado() {
        return this.estado;
    }
    
    public void setEstado(final EstadoFactura estado) {
        this.estado = estado;
    }
    
    public String getComentario() {
        return this.comentario;
    }
    
    public void setComentario(final String comentario) {
        this.comentario = comentario;
    }
    
    @Enumerated(EnumType.STRING)
    public EstadoFactura getEstadoAnterior() {
        return this.estadoAnterior;
    }
    
    public void setEstadoAnterior(final EstadoFactura estadoAnterior) {
        this.estadoAnterior = estadoAnterior;
    }
}
