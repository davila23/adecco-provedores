// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.model;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;
import javax.persistence.Table;
import javax.persistence.Entity;
import java.io.Serializable;

@Entity
@Table(schema = "compras")
public class InterfazAP implements Serializable
{
    private static final long serialVersionUID = 1625867581556529940L;
    private long id;
    private Date fecha;
    private String error;
    private Comprobante factura;
    private Proveedor proveedor;
    private EstadoFactura estadoFactura;
    private EstadoProveedor estadoProveedor;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return this.id;
    }
    
    public void setId(final long id) {
        this.id = id;
    }
    
    public Date getFecha() {
        return this.fecha;
    }
    
    public void setFecha(final Date fecha) {
        this.fecha = fecha;
    }
    
    public String getError() {
        return this.error;
    }
    
    public void setError(final String error) {
        this.error = error;
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FACTURA_ID")
    public Comprobante getFactura() {
        return this.factura;
    }
    
    public void setFactura(final Comprobante factura) {
        this.factura = factura;
    }
    
    @Enumerated(EnumType.STRING)
    public EstadoFactura getEstadoFactura() {
        return this.estadoFactura;
    }
    
    public void setEstadoFactura(final EstadoFactura estadoFactura) {
        this.estadoFactura = estadoFactura;
    }
    
    @Enumerated(EnumType.STRING)
    public EstadoProveedor getEstadoProveedor() {
        return this.estadoProveedor;
    }
    
    public void setEstadoProveedor(final EstadoProveedor estadoProveedor) {
        this.estadoProveedor = estadoProveedor;
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROVEEDOR_ID", referencedColumnName = "ID")
    public Proveedor getProveedor() {
        return this.proveedor;
    }
    
    public void setProveedor(final Proveedor proveedor) {
        this.proveedor = proveedor;
    }
}
