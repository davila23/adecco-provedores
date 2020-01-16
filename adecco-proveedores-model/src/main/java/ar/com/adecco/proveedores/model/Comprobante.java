// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.model;

import javax.persistence.Transient;
import javax.persistence.DiscriminatorValue;
import javax.persistence.OneToMany;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.util.List;
import ar.com.adecco.dominio.ebs.ap.Factura;
import ar.com.adecco.dominio.ebs.fnd.Provincia;
import ar.com.adecco.dominio.ebs.fnd.Moneda;
import ar.com.adecco.dominio.ebs.hr.Compania;
import java.util.Date;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.InheritanceType;
import javax.persistence.Inheritance;
import javax.persistence.Table;
import javax.persistence.Entity;
import java.io.Serializable;
import ar.com.adecco.proveedores.model.base.EntidadAuditada;

@Entity(name = "ar.com.adecco.proveedores.model.Comprobante")
@Table(schema = "compras", name = "FACTURA")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPOCOMPROBANTE")
public class Comprobante extends EntidadAuditada implements Serializable
{
    private static final long serialVersionUID = -6685818249699873457L;
    private Proveedor proveedor;
    private Letra letra;
    private String numero;
    private Date fecha;
    private Date fechaContable;
    private Date fechaRecepcion;
    private EstadoFactura estado;
    private EstadoComprobanteEbs estadoEbs;
    private Compania compania;
    private Moneda moneda;
    private Double tipoCambio;
    private Double importe;
    private Double importePagado;
    private CodigoAutorizacion cae;
    private String numeroCae;
    private Date fechaVencimientoCae;
    private Provincia provinciaPrestoServicio;
    private Factura comprobanteEbs;
    private boolean volverAAceptar;
    private List<FacturaEstado> estados;
    private List<Linea> lineas;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROVEEDOR_ID")
    public Proveedor getProveedor() {
        return this.proveedor;
    }
    
    public void setProveedor(final Proveedor proveedor) {
        this.proveedor = proveedor;
    }
    
    @Enumerated(EnumType.STRING)
    public Letra getLetra() {
        return this.letra;
    }
    
    public void setLetra(final Letra letra) {
        this.letra = letra;
    }
    
    public String getNumero() {
        return this.numero;
    }
    
    public void setNumero(final String numero) {
        this.numero = numero;
    }
    
    public Date getFecha() {
        return this.fecha;
    }
    
    public void setFecha(final Date fecha) {
        this.fecha = fecha;
    }
    
    public Date getFechaContable() {
        return this.fechaContable;
    }
    
    public void setFechaContable(final Date fechaContable) {
        this.fechaContable = fechaContable;
    }
    
    public Date getFechaRecepcion() {
        return this.fechaRecepcion;
    }
    
    public void setFechaRecepcion(final Date fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
    }
    
    @Enumerated(EnumType.STRING)
    public EstadoFactura getEstado() {
        return this.estado;
    }
    
    public void setEstado(final EstadoFactura estado) {
        this.estado = estado;
    }
    
    @Column(name = "ESTADO_EBS")
    @Enumerated(EnumType.STRING)
    public EstadoComprobanteEbs getEstadoEbs() {
        return this.estadoEbs;
    }
    
    public void setEstadoEbs(final EstadoComprobanteEbs estadoEbs) {
        this.estadoEbs = estadoEbs;
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORG_ID")
    public Compania getCompania() {
        return this.compania;
    }
    
    public void setCompania(final Compania compania) {
        this.compania = compania;
    }
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CURRENCY_CODE")
    public Moneda getMoneda() {
        return this.moneda;
    }
    
    public void setMoneda(final Moneda moneda) {
        this.moneda = moneda;
    }
    
    public Double getTipoCambio() {
        return this.tipoCambio;
    }
    
    public void setTipoCambio(final Double tipoCambio) {
        this.tipoCambio = tipoCambio;
    }
    
    public Double getImporte() {
        return this.importe;
    }
    
    public void setImporte(final Double importe) {
        this.importe = importe;
    }
    
    public Double getImportePagado() {
        return this.importePagado;
    }
    
    public void setImportePagado(final Double importePagado) {
        this.importePagado = importePagado;
    }
    
    @Enumerated(EnumType.STRING)
    public CodigoAutorizacion getCae() {
        return this.cae;
    }
    
    public void setCae(final CodigoAutorizacion cae) {
        this.cae = cae;
    }
    
    public String getNumeroCae() {
        return this.numeroCae;
    }
    
    public void setNumeroCae(final String numeroCae) {
        this.numeroCae = numeroCae;
    }
    
    public Date getFechaVencimientoCae() {
        return this.fechaVencimientoCae;
    }
    
    public void setFechaVencimientoCae(final Date fechaVencimientoCae) {
        this.fechaVencimientoCae = fechaVencimientoCae;
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LOOKUP_CODE")
    public Provincia getProvinciaPrestoServicio() {
        return this.provinciaPrestoServicio;
    }
    
    public void setProvinciaPrestoServicio(final Provincia provinciaPrestoServicio) {
        this.provinciaPrestoServicio = provinciaPrestoServicio;
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INVOICE_ID")
    public Factura getComprobanteEbs() {
        return this.comprobanteEbs;
    }
    
    public void setComprobanteEbs(final Factura comprobanteEbs) {
        this.comprobanteEbs = comprobanteEbs;
    }
    
    @Column(name = "VOLVER_ACEPTAR")
    public boolean isVolverAAceptar() {
        return this.volverAAceptar;
    }
    
    public void setVolverAAceptar(final boolean volverAAceptar) {
        this.volverAAceptar = volverAAceptar;
    }
    
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "FACTURA_ID")
    public List<FacturaEstado> getEstados() {
        return this.estados;
    }
    
    public void setEstados(final List<FacturaEstado> estados) {
        this.estados = estados;
    }
    
    @OneToMany(mappedBy = "comprobante", fetch = FetchType.LAZY)
    public List<Linea> getLineas() {
        return this.lineas;
    }
    
    public void setLineas(final List<Linea> lineas) {
        this.lineas = lineas;
    }
    
    @Transient
    public TipoComprobante getTipoComprobante() {
        final DiscriminatorValue discriminatorValue = this.getClass().getAnnotation(DiscriminatorValue.class);
        return (discriminatorValue == null) ? null : TipoComprobante.valueOf(discriminatorValue.value());
    }
    
    @Transient
    public String getLetraNumero() {
        if (this.letra == null || this.numero == null) {
            return this.numero;
        }
        return this.letra + "-" + this.numero;
    }
    
    @Transient
    public String getTipoLetraNumero() {
        return this.getTipoComprobante() + " " + ((this.letra != null) ? (this.letra + "-" + this.numero) : this.numero);
    }
    
    public enum TipoComprobante
    {
        FC("Factura", (Class<? extends Comprobante>)ar.com.adecco.proveedores.model.Factura.class), 
        NC("Nota de cr\u00e9dito", (Class<? extends Comprobante>)NotaCredito.class), 
        ND("Nota de d\u00e9bito", (Class<? extends Comprobante>)NotaDebito.class);
        
        private String label;
        private Class<? extends Comprobante> referencedClass;
        
        private TipoComprobante(final String label, final Class<? extends Comprobante> referencedClass) {
            this.label = label;
            this.referencedClass = referencedClass;
        }
        
        public String getLabel() {
            return this.label;
        }
        
        public Class<? extends Comprobante> getReferencedClass() {
            return this.referencedClass;
        }
    }
}
