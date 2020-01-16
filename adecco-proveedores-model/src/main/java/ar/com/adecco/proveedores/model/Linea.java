// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.model;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import ar.com.adecco.dominio.ebs.po.LineaOrdenCompra;
import ar.com.adecco.dominio.ebs.ap.Impuesto;
import ar.com.adecco.dominio.ebs.hr.Compania;
import java.sql.Date;
import ar.com.adecco.dominio.ebs.gl.CuentaContable;
import javax.persistence.Table;
import javax.persistence.Entity;
import java.io.Serializable;
import ar.com.adecco.proveedores.model.base.EntidadAuditada;

@Entity(name = "ar.com.adecco.proveedores.model.Linea")
@Table(schema = "compras", name = "FACTURADETALLE")
public class Linea extends EntidadAuditada implements Serializable
{
    private static final long serialVersionUID = 5265214823410129729L;
    private CuentaContable cuentaContable;
    private Date fechaContable;
    private Long numero;
    private Compania compania;
    private Double importe;
    private Double cantidad;
    private TipoLinea tipo;
    private Impuesto impuesto;
    private LineaOrdenCompra lineaOrdenCompra;
    private Comprobante comprobante;
    private Linea lineaAplicada;
    
    public Long getNumero() {
        return this.numero;
    }
    
    public void setNumero(final Long numero) {
        this.numero = numero;
    }
    
    @Enumerated(EnumType.STRING)
    public TipoLinea getTipo() {
        return this.tipo;
    }
    
    public void setTipo(final TipoLinea tipo) {
        this.tipo = tipo;
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FACTURA_ID", updatable = false)
    public Comprobante getComprobante() {
        return this.comprobante;
    }
    
    public void setComprobante(final Comprobante comprobante) {
        this.comprobante = comprobante;
    }
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IMPUESTO_ID", referencedColumnName = "TAX_ID")
    public Impuesto getImpuesto() {
        return this.impuesto;
    }
    
    public void setImpuesto(final Impuesto impuesto) {
        this.impuesto = impuesto;
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CODE_COMBINATION_ID", referencedColumnName = "CODE_COMBINATION_ID")
    public CuentaContable getCuentaContable() {
        return this.cuentaContable;
    }
    
    public void setCuentaContable(final CuentaContable cuentaContable) {
        this.cuentaContable = cuentaContable;
    }
    
    public Date getFechaContable() {
        return this.fechaContable;
    }
    
    public void setFechaContable(final Date fechaContable) {
        this.fechaContable = fechaContable;
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORG_ID", referencedColumnName = "ORGANIZATION_ID")
    public Compania getCompania() {
        return this.compania;
    }
    
    public void setCompania(final Compania compania) {
        this.compania = compania;
    }
    
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "PO_LINE_ID")
    public LineaOrdenCompra getLineaOrdenCompra() {
        return this.lineaOrdenCompra;
    }
    
    public void setLineaOrdenCompra(final LineaOrdenCompra lineaOrdenCompra) {
        this.lineaOrdenCompra = lineaOrdenCompra;
    }
    
    @Column(name = "TOTAL")
    public Double getImporte() {
        return this.importe;
    }
    
    public void setImporte(final Double importe) {
        this.importe = importe;
    }
    
    public Double getCantidad() {
        return this.cantidad;
    }
    
    public void setCantidad(final Double cantidad) {
        this.cantidad = cantidad;
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FACTURAAPLICADADETALLE_ID")
    public Linea getLineaAplicada() {
        return this.lineaAplicada;
    }
    
    public void setLineaAplicada(final Linea lineaAplicada) {
        this.lineaAplicada = lineaAplicada;
    }
    
    public enum TipoLinea
    {
        ITEM, 
        TAX;
    }
}
