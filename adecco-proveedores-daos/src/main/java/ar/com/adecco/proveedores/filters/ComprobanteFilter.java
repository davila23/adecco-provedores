// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.filters;

import ar.com.adecco.proveedores.filters.commons.OrderExpression;
import java.util.List;
import ar.com.adecco.proveedores.model.EstadoFactura;
import java.util.Date;
import ar.com.adecco.proveedores.model.Comprobante;
import java.util.Collection;
import java.io.Serializable;

public class ComprobanteFilter implements Serializable
{
    private static final long serialVersionUID = -5171527757296305L;
    private Collection<Comprobante> comprobantes;
    private Long proveedorId;
    private String proveedorNombre;
    private String proveedorCuit;
    private String numeroDesde;
    private String numeroHasta;
    private Date fechaDesde;
    private Date fechaHasta;
    private Comprobante.TipoComprobante tipoComprobante;
    private EstadoFactura estado;
    private Long companiaId;
    private Long solicitanteId;
    private boolean eagerLoad;
    private List<OrderExpression<OrderElement>> orderBy;
    
    public Collection<Comprobante> getComprobantes() {
        return this.comprobantes;
    }
    
    public void setComprobantes(final Collection<Comprobante> comprobantes) {
        this.comprobantes = comprobantes;
    }
    
    public Long getProveedorId() {
        return this.proveedorId;
    }
    
    public void setProveedorId(final Long proveedorId) {
        this.proveedorId = proveedorId;
    }
    
    public String getProveedorNombre() {
        return this.proveedorNombre;
    }
    
    public void setProveedorNombre(final String proveedorNombre) {
        this.proveedorNombre = proveedorNombre;
    }
    
    public String getProveedorCuit() {
        return this.proveedorCuit;
    }
    
    public void setProveedorCuit(final String proveedorCuit) {
        this.proveedorCuit = proveedorCuit;
    }
    
    public String getNumeroDesde() {
        return this.numeroDesde;
    }
    
    public void setNumeroDesde(final String numeroDesde) {
        this.numeroDesde = numeroDesde;
    }
    
    public String getNumeroHasta() {
        return this.numeroHasta;
    }
    
    public void setNumeroHasta(final String numeroHasta) {
        this.numeroHasta = numeroHasta;
    }
    
    public Date getFechaDesde() {
        return this.fechaDesde;
    }
    
    public void setFechaDesde(final Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }
    
    public Date getFechaHasta() {
        return this.fechaHasta;
    }
    
    public void setFechaHasta(final Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }
    
    public Comprobante.TipoComprobante getTipoComprobante() {
        return this.tipoComprobante;
    }
    
    public void setTipoComprobante(final Comprobante.TipoComprobante tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
    }
    
    public EstadoFactura getEstado() {
        return this.estado;
    }
    
    public void setEstado(final EstadoFactura estado) {
        this.estado = estado;
    }
    
    public Long getCompaniaId() {
        return this.companiaId;
    }
    
    public void setCompaniaId(final Long companiaId) {
        this.companiaId = companiaId;
    }
    
    public Long getSolicitanteId() {
        return this.solicitanteId;
    }
    
    public void setSolicitanteId(final Long solicitanteId) {
        this.solicitanteId = solicitanteId;
    }
    
    public boolean isEagerLoad() {
        return this.eagerLoad;
    }
    
    public void setEagerLoad(final boolean eagerLoad) {
        this.eagerLoad = eagerLoad;
    }
    
    public List<OrderExpression<OrderElement>> getOrderBy() {
        return this.orderBy;
    }
    
    public void setOrderBy(final List<OrderExpression<OrderElement>> orderBy) {
        this.orderBy = orderBy;
    }
    
    public enum OrderElement
    {
        FECHA;
    }
}
