// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.model;

import javax.persistence.Transient;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.util.List;
import java.util.Date;
import ar.com.adecco.dominio.ebs.fnd.Provincia;
import ar.com.adecco.dominio.ebs.fnd.Moneda;
import javax.persistence.Table;
import javax.persistence.Entity;
import java.io.Serializable;
import ar.com.adecco.proveedores.model.base.EntidadAuditada;

@Entity(name = "ar.com.adecco.proveedores.model.Proveedor")
@Table(schema = "compras", name = "PROVEEDOR")
public class Proveedor extends EntidadAuditada implements Serializable
{
    private static final long serialVersionUID = 8916684696320099361L;
    private String razonSocial;
    private String cuit;
    private String nombreComercial;
    private Moneda moneda;
    private String bancoSucursal;
    private String cbu;
    private String domicilio;
    private String ciudad;
    private Provincia provincia;
    private String codigoPostal;
    private String telefono;
    private String fax;
    private ar.com.adecco.dominio.ebs.po.Proveedor proveedorEBS;
    private String apellidoNombreRL;
    private String dniRL;
    private String telefonoRL;
    private String faxRL;
    private String apellidoNombreContacto;
    private String telefonoContacto;
    private String emailContacto;
    private String motivo;
    private Integer cantidadCotizaciones;
    private boolean contrato;
    private boolean cartaOferta;
    private boolean propuestaComercial;
    private String otro;
    private String terminoPago;
    private String nroIIBB;
    private Provincia jurisdiccionLocal;
    private boolean exentoLocal;
    private String porcentajeExclusionLocal;
    private String condicionIVA;
    private String impuestoGanancias;
    private String porcentajeExclusion;
    private String descripcionActividad;
    private String actividadPrincipal;
    private String observaciones;
    private boolean sistemaGestionCalidad;
    private boolean requisitosAdecco;
    private String respuestaRequisitosAdecco;
    private boolean procedimientoCalidad;
    private String respuestaProcedimientoCalidad;
    private boolean seguimientoAviso;
    private String respuestaSeguimientoAviso;
    private boolean reclamo;
    private String respuestaReclamo;
    private EstadoProveedor estado;
    private boolean aprobadoImpuestos;
    private boolean aprobadoCompras;
    private boolean aprobadoTesoreria;
    private Date fechaVencimiento;
    private List<ProveedorEstado> proveedorEstados;
    private List<IIBBMultilateral> iibbMultilaterales;
    private boolean selected;
    
    public String getRazonSocial() {
        return this.razonSocial;
    }
    
    public void setRazonSocial(final String razonSocial) {
        this.razonSocial = razonSocial;
    }
    
    public String getCuit() {
        return this.cuit;
    }
    
    public void setCuit(final String cuit) {
        this.cuit = cuit;
    }
    
    public String getNombreComercial() {
        return this.nombreComercial;
    }
    
    public void setNombreComercial(final String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CURRENCY_CODE")
    public Moneda getMoneda() {
        return this.moneda;
    }
    
    public void setMoneda(final Moneda moneda) {
        this.moneda = moneda;
    }
    
    public String getBancoSucursal() {
        return this.bancoSucursal;
    }
    
    public void setBancoSucursal(final String bancoSucursal) {
        this.bancoSucursal = bancoSucursal;
    }
    
    public String getCbu() {
        return this.cbu;
    }
    
    public void setCbu(final String cbu) {
        this.cbu = cbu;
    }
    
    public String getDomicilio() {
        return this.domicilio;
    }
    
    public void setDomicilio(final String domicilio) {
        this.domicilio = domicilio;
    }
    
    public String getCiudad() {
        return this.ciudad;
    }
    
    public void setCiudad(final String ciudad) {
        this.ciudad = ciudad;
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LOOKUP_CODE")
    public Provincia getProvincia() {
        return this.provincia;
    }
    
    public void setProvincia(final Provincia provincia) {
        this.provincia = provincia;
    }
    
    public String getCodigoPostal() {
        return this.codigoPostal;
    }
    
    public void setCodigoPostal(final String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }
    
    public String getTelefono() {
        return this.telefono;
    }
    
    public void setTelefono(final String telefono) {
        this.telefono = telefono;
    }
    
    public String getFax() {
        return this.fax;
    }
    
    public void setFax(final String fax) {
        this.fax = fax;
    }
    
    public String getApellidoNombreRL() {
        return this.apellidoNombreRL;
    }
    
    public void setApellidoNombreRL(final String apellidoNombreRL) {
        this.apellidoNombreRL = apellidoNombreRL;
    }
    
    public String getDniRL() {
        return this.dniRL;
    }
    
    public void setDniRL(final String dniRL) {
        this.dniRL = dniRL;
    }
    
    public String getTelefonoRL() {
        return this.telefonoRL;
    }
    
    public void setTelefonoRL(final String telefonoRL) {
        this.telefonoRL = telefonoRL;
    }
    
    public String getFaxRL() {
        return this.faxRL;
    }
    
    public void setFaxRL(final String faxRL) {
        this.faxRL = faxRL;
    }
    
    public String getApellidoNombreContacto() {
        return this.apellidoNombreContacto;
    }
    
    public void setApellidoNombreContacto(final String apellidoNombreContacto) {
        this.apellidoNombreContacto = apellidoNombreContacto;
    }
    
    public String getTelefonoContacto() {
        return this.telefonoContacto;
    }
    
    public void setTelefonoContacto(final String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }
    
    public String getEmailContacto() {
        return this.emailContacto;
    }
    
    public void setEmailContacto(final String emailContacto) {
        this.emailContacto = emailContacto;
    }
    
    public String getMotivo() {
        return this.motivo;
    }
    
    public void setMotivo(final String motivo) {
        this.motivo = motivo;
    }
    
    public boolean isContrato() {
        return this.contrato;
    }
    
    public void setContrato(final boolean contrato) {
        this.contrato = contrato;
    }
    
    public Integer getCantidadCotizaciones() {
        return this.cantidadCotizaciones;
    }
    
    public void setCantidadCotizaciones(final Integer cantidadCotizaciones) {
        this.cantidadCotizaciones = cantidadCotizaciones;
    }
    
    public boolean isCartaOferta() {
        return this.cartaOferta;
    }
    
    public void setCartaOferta(final boolean cartaOferta) {
        this.cartaOferta = cartaOferta;
    }
    
    public boolean isPropuestaComercial() {
        return this.propuestaComercial;
    }
    
    public void setPropuestaComercial(final boolean propuestaComercial) {
        this.propuestaComercial = propuestaComercial;
    }
    
    public String getOtro() {
        return this.otro;
    }
    
    public void setOtro(final String otro) {
        this.otro = otro;
    }
    
    public String getTerminoPago() {
        return this.terminoPago;
    }
    
    public void setTerminoPago(final String terminoPago) {
        this.terminoPago = terminoPago;
    }
    
    public String getDescripcionActividad() {
        return this.descripcionActividad;
    }
    
    public void setDescripcionActividad(final String descripcionActividad) {
        this.descripcionActividad = descripcionActividad;
    }
    
    public String getActividadPrincipal() {
        return this.actividadPrincipal;
    }
    
    public void setActividadPrincipal(final String actividadPrincipal) {
        this.actividadPrincipal = actividadPrincipal;
    }
    
    public String getNroIIBB() {
        return this.nroIIBB;
    }
    
    public void setNroIIBB(final String nroIIBB) {
        this.nroIIBB = nroIIBB;
    }
    
    public String getCondicionIVA() {
        return this.condicionIVA;
    }
    
    public void setCondicionIVA(final String condicionIVA) {
        this.condicionIVA = condicionIVA;
    }
    
    public String getImpuestoGanancias() {
        return this.impuestoGanancias;
    }
    
    public void setImpuestoGanancias(final String impuestoGanancias) {
        this.impuestoGanancias = impuestoGanancias;
    }
    
    public boolean isSistemaGestionCalidad() {
        return this.sistemaGestionCalidad;
    }
    
    public void setSistemaGestionCalidad(final boolean sistemaGestionCalidad) {
        this.sistemaGestionCalidad = sistemaGestionCalidad;
    }
    
    public boolean isRequisitosAdecco() {
        return this.requisitosAdecco;
    }
    
    public void setRequisitosAdecco(final boolean requisitosAdecco) {
        this.requisitosAdecco = requisitosAdecco;
    }
    
    public boolean isProcedimientoCalidad() {
        return this.procedimientoCalidad;
    }
    
    public void setProcedimientoCalidad(final boolean procedimientoCalidad) {
        this.procedimientoCalidad = procedimientoCalidad;
    }
    
    public boolean isSeguimientoAviso() {
        return this.seguimientoAviso;
    }
    
    public void setSeguimientoAviso(final boolean seguimientoAviso) {
        this.seguimientoAviso = seguimientoAviso;
    }
    
    public boolean isReclamo() {
        return this.reclamo;
    }
    
    public void setReclamo(final boolean reclamo) {
        this.reclamo = reclamo;
    }
    
    public String getObservaciones() {
        return this.observaciones;
    }
    
    public void setObservaciones(final String observaciones) {
        this.observaciones = observaciones;
    }
    
    public String getRespuestaRequisitosAdecco() {
        return this.respuestaRequisitosAdecco;
    }
    
    public void setRespuestaRequisitosAdecco(final String respuestaRequisitosAdecco) {
        this.respuestaRequisitosAdecco = respuestaRequisitosAdecco;
    }
    
    public String getRespuestaProcedimientoCalidad() {
        return this.respuestaProcedimientoCalidad;
    }
    
    public void setRespuestaProcedimientoCalidad(final String respuestaProcedimientoCalidad) {
        this.respuestaProcedimientoCalidad = respuestaProcedimientoCalidad;
    }
    
    public String getRespuestaSeguimientoAviso() {
        return this.respuestaSeguimientoAviso;
    }
    
    public void setRespuestaSeguimientoAviso(final String respuestaSeguimientoAviso) {
        this.respuestaSeguimientoAviso = respuestaSeguimientoAviso;
    }
    
    public String getRespuestaReclamo() {
        return this.respuestaReclamo;
    }
    
    public void setRespuestaReclamo(final String respuestaReclamo) {
        this.respuestaReclamo = respuestaReclamo;
    }
    
    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "VENDOR_ID")
    public ar.com.adecco.dominio.ebs.po.Proveedor getProveedorEBS() {
        return this.proveedorEBS;
    }
    
    public void setProveedorEBS(final ar.com.adecco.dominio.ebs.po.Proveedor proveedorEBS) {
        this.proveedorEBS = proveedorEBS;
    }
    
    public boolean isAprobadoImpuestos() {
        return this.aprobadoImpuestos;
    }
    
    public void setAprobadoImpuestos(final boolean aprobadoImpuestos) {
        this.aprobadoImpuestos = aprobadoImpuestos;
    }
    
    public boolean isAprobadoCompras() {
        return this.aprobadoCompras;
    }
    
    public void setAprobadoCompras(final boolean aprobadoCompras) {
        this.aprobadoCompras = aprobadoCompras;
    }
    
    public boolean isAprobadoTesoreria() {
        return this.aprobadoTesoreria;
    }
    
    public void setAprobadoTesoreria(final boolean aprobadoTesoreria) {
        this.aprobadoTesoreria = aprobadoTesoreria;
    }
    
    @OneToMany(mappedBy = "proveedor", fetch = FetchType.LAZY)
    public List<ProveedorEstado> getProveedorEstados() {
        return this.proveedorEstados;
    }
    
    public void setProveedorEstados(final List<ProveedorEstado> proveedorEstados) {
        this.proveedorEstados = proveedorEstados;
    }
    
    public String getPorcentajeExclusion() {
        return this.porcentajeExclusion;
    }
    
    public void setPorcentajeExclusion(final String porcentajeExclusion) {
        this.porcentajeExclusion = porcentajeExclusion;
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "JURISDICCIONLOCAL_ID", referencedColumnName = "LOOKUP_CODE")
    public Provincia getJurisdiccionLocal() {
        return this.jurisdiccionLocal;
    }
    
    public void setJurisdiccionLocal(final Provincia jurisdiccionLocal) {
        this.jurisdiccionLocal = jurisdiccionLocal;
    }
    
    public boolean isExentoLocal() {
        return this.exentoLocal;
    }
    
    public void setExentoLocal(final boolean exentoLocal) {
        this.exentoLocal = exentoLocal;
    }
    
    public String getPorcentajeExclusionLocal() {
        return this.porcentajeExclusionLocal;
    }
    
    public void setPorcentajeExclusionLocal(final String porcentajeExclusionLocal) {
        this.porcentajeExclusionLocal = porcentajeExclusionLocal;
    }
    
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROVEEDOR_ID", referencedColumnName = "ID", updatable = false)
    public List<IIBBMultilateral> getIibbMultilaterales() {
        return this.iibbMultilaterales;
    }
    
    public void setIibbMultilaterales(final List<IIBBMultilateral> iibbMultilaterales) {
        this.iibbMultilaterales = iibbMultilaterales;
    }
    
    @Enumerated(EnumType.STRING)
    public EstadoProveedor getEstado() {
        return this.estado;
    }
    
    public void setEstado(final EstadoProveedor estado) {
        this.estado = estado;
    }
    
    public Date getFechaVencimiento() {
        return this.fechaVencimiento;
    }
    
    public void setFechaVencimiento(final Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
    
    @Transient
    public boolean isSelected() {
        return this.selected;
    }
    
    public void setSelected(final boolean selected) {
        this.selected = selected;
    }
}
