// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.action;

import ar.com.adecco.proveedores.model.Perfil;
import ar.com.adecco.dominio.persona.PersonaRelacion;
import ar.com.adecco.dominio.contacto.Modulo;
import ar.com.adecco.proveedores.model.EstadoProveedor;
import javax.annotation.PostConstruct;
import java.util.Collection;
import ar.com.adecco.proveedores.model.Vista;
import ar.com.adecco.proveedores.model.Acceso;
import ar.com.adecco.proveedores.model.NotaDebito;
import ar.com.adecco.proveedores.model.NotaCredito;
import ar.com.adecco.proveedores.model.Factura;
import ar.com.adecco.dominio.ebs.po.OrdenCompra;
import org.primefaces.model.LazyDataModel;
import java.util.List;
import ar.com.adecco.proveedores.model.Proveedor;
import ar.com.adecco.proveedores.service.ConfiguracionModuloService;
import ar.com.adecco.proveedores.service.NotaDebitoService;
import ar.com.adecco.proveedores.service.NotaCreditoService;
import ar.com.adecco.proveedores.service.FacturaService;
import ar.com.adecco.proveedores.service.OrdenCompraService;
import ar.com.adecco.proveedores.service.PersonaRelacionProveedorService;
import ar.com.adecco.proveedores.service.ProveedorService;
import javax.inject.Inject;
import ar.com.syntagma.adecco.login.cliente.MenuBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class PanelControlAction implements Serializable
{
    private static final long serialVersionUID = -166986279621914825L;
    @Inject
    private MenuBean menuBean;
    @Inject
    private ProveedorService proveedorService;
    @Inject
    private PersonaRelacionProveedorService personaRelacionProveedorService;
    @Inject
    private OrdenCompraService ordenCompraService;
    @Inject
    private FacturaService facturaService;
    @Inject
    private NotaCreditoService notaCreditoService;
    @Inject
    private NotaDebitoService notaDebitoService;
    @Inject
    private ConfiguracionModuloService configuracionModuloService;
    @Inject
    private DetalleOCAction detalleOCAction;
    @Inject
    private AdministracionFacturaAction administracionFacturaAction;
    @Inject
    private NotaCreditoProveedorAction notaCreditoProveedorAction;
    @Inject
    private NotaDebitoProveedorAction notaDebitoProveedorAction;
    private Proveedor proveedor;
    private List<Proveedor> proveedores;
    private LazyDataModel<OrdenCompra> ordenesCompra;
    private LazyDataModel<Factura> facturas;
    private LazyDataModel<NotaCredito> notasCredito;
    private LazyDataModel<NotaDebito> notasDebito;
    private Acceso currentAccess;
    protected Vista currentView;
    
    @PostConstruct
    public void init() {
        this.proveedores = this.proveedorService.getByIds((Collection)this.menuBean.getRelacionProveedores());
        this.proveedor = ((this.proveedores != null && this.proveedores.size() == 1) ? this.proveedores.get(0) : null);
        this.onProveedorChanged();
        this.currentView = Vista.PRINCIPAL;
        //this.ordenesCompra = (LazyDataModel)new PanelControlAction$1(this);
        //this.facturas = (LazyDataModel)new PanelControlAction$2(this);
        //this.notasCredito = (LazyDataModel)new PanelControlAction$3(this);
        //this.notasDebito = (LazyDataModel)new PanelControlAction$4(this);
    }
    
    public boolean currentAccessIs(final Acceso acceso) {
        return this.currentAccess.equals((Object)acceso);
    }
    
    public boolean currentAccessIs(final String s) {
        return this.currentAccess.equals((Object)Acceso.valueOf(s));
    }
    
    public boolean currentViewIs(final Vista vista) {
        return this.currentView.equals((Object)vista);
    }
    
    public boolean currentViewIs(final String s) {
        return this.currentView.equals((Object)Vista.valueOf(s));
    }
    
    public void onProveedorChanged() {
        if (this.proveedor != null && this.proveedor.getProveedorEBS() != null && this.proveedor.getEstado().equals((Object)EstadoProveedor.APROBADO)) {
            this.currentAccess = Acceso.ACCESSO_DETALLE;
            this.configuracionModuloService.updateLastAccess(Modulo.Codigo.PROVEEDORES, Long.valueOf(this.menuBean.getUsuario().getPersona().getId()), PersonaRelacion.TipoRelacion.PROVEEDOR, Long.valueOf(this.proveedor.getId()));
        }
        else {
            this.currentAccess = Acceso.ACCESO_PENDIENTE;
        }
    }
    
    public boolean proveedorHabilitado() {
        return this.proveedorService.proveedorHabilitado(this.proveedor);
    }
    
    public void showOrdenCompra(final OrdenCompra ordenCompra) {
        this.detalleOCAction.initialize(this.proveedor);
        this.detalleOCAction.administracionOC(ordenCompra.getId());
        this.currentView = Vista.ADM_OC;
    }
    
    public void showFactura(final Factura factura) {
        this.administracionFacturaAction.initialize(this.proveedor);
        this.administracionFacturaAction.administracionFactura(Perfil.PROVEEDOR, factura.getId());
        this.currentView = Vista.ADM_FAC;
    }
    
    public void showNotaCredito(final NotaCredito notaCredito) {
        this.notaCreditoProveedorAction.initialize(this.proveedor);
        this.notaCreditoProveedorAction.administrar(Perfil.PROVEEDOR, notaCredito);
        this.currentView = Vista.ADM_NC;
    }
    
    public void showNotaDebito(final NotaDebito notaDebito) {
        this.notaDebitoProveedorAction.initialize(this.proveedor);
        this.notaDebitoProveedorAction.administrar(Perfil.PROVEEDOR, notaDebito);
        this.currentView = Vista.ADM_ND;
    }
    
    public void showNewFactura() {
        this.administracionFacturaAction.initialize(this.proveedor);
        this.administracionFacturaAction.altaFactura();
        this.currentView = Vista.ADM_FAC;
    }
    
    public void showNewFactura(final long n) {
        this.administracionFacturaAction.initialize(this.proveedor);
        this.administracionFacturaAction.asociarFacturaOC(Perfil.PROVEEDOR, n);
        this.currentView = Vista.ADM_FAC;
    }
    
    public void showNewNotaCredito() {
        this.notaCreditoProveedorAction.initialize(this.proveedor);
        this.notaCreditoProveedorAction.alta();
        this.currentView = Vista.ADM_NC;
    }
    
    public void showNewNotaDebito() {
        this.notaDebitoProveedorAction.initialize(this.proveedor);
        this.notaDebitoProveedorAction.alta();
        this.currentView = Vista.ADM_ND;
    }
    
    public void showPanelControl() {
        this.currentView = Vista.PRINCIPAL;
    }
    
    public List<Proveedor> getProveedores() {
        return (List<Proveedor>)this.proveedores;
    }
    
    public void setProveedores(final List<Proveedor> proveedores) {
        this.proveedores = proveedores;
    }
    
    public Proveedor getProveedor() {
        return this.proveedor;
    }
    
    public void setProveedor(final Proveedor proveedor) {
        this.proveedor = proveedor;
    }
    
    public LazyDataModel<OrdenCompra> getOrdenesCompra() {
        return (LazyDataModel<OrdenCompra>)this.ordenesCompra;
    }
    
    public void setOrdenesCompra(final LazyDataModel<OrdenCompra> ordenesCompra) {
        this.ordenesCompra = ordenesCompra;
    }
    
    public LazyDataModel<Factura> getFacturas() {
        return (LazyDataModel<Factura>)this.facturas;
    }
    
    public void setFacturas(final LazyDataModel<Factura> facturas) {
        this.facturas = facturas;
    }
    
    public LazyDataModel<NotaCredito> getNotasCredito() {
        return (LazyDataModel<NotaCredito>)this.notasCredito;
    }
    
    public void setNotasCredito(final LazyDataModel<NotaCredito> notasCredito) {
        this.notasCredito = notasCredito;
    }
    
    public LazyDataModel<NotaDebito> getNotasDebito() {
        return (LazyDataModel<NotaDebito>)this.notasDebito;
    }
    
    public void setNotasDebito(final LazyDataModel<NotaDebito> notasDebito) {
        this.notasDebito = notasDebito;
    }
}
