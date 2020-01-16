// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.action;

import ar.com.adecco.proveedores.model.Factura;
import ar.com.adecco.dominio.ebs.po.OrdenCompra;
import com.google.common.base.Strings;
import ar.com.adecco.proveedores.model.NotaDebito;
import ar.com.adecco.proveedores.model.NotaCredito;
import ar.com.adecco.proveedores.model.Perfil;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import java.util.Iterator;

import ar.com.syntagma.adecco.login.cliente.MenuBean;
import ar.com.syntagma.adecco.login.servicio.UsuarioEbsDto;
import java.util.Arrays;
import ar.com.adecco.proveedores.filters.commons.OrderDirection;
import ar.com.adecco.proveedores.filters.commons.OrderExpression;
import ar.com.adecco.proveedores.daos.exception.ServiceException;
import ar.com.syntagma.helpers.PropertiesHelper;
import ar.com.syntagma.uiutils.Mensajes;
import javax.faces.application.FacesMessage;
import java.util.EnumSet;
import ar.com.adecco.proveedores.filters.ComprobanteFilter;
import ar.com.adecco.proveedores.model.EstadoFactura;
import org.primefaces.model.LazyDataModel;
import ar.com.adecco.dominio.ebs.hr.Compania;
import java.util.List;
import ar.com.adecco.proveedores.model.Comprobante;
import ar.com.adecco.proveedores.service.UsuarioService;
import ar.com.adecco.proveedores.service.CompaniaService;
import ar.com.adecco.proveedores.service.ComprobanteService;
import javax.inject.Inject;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class AceptacionFacturaAction implements Serializable
{
    private static final long serialVersionUID = 3009888147871284004L;
    @Inject
    private AdministracionFacturaAction administracionFacturaAction;
    @Inject
    private NotaCreditoProveedorAction notaCreditoProveedorAction;
    @Inject
    private NotaDebitoProveedorAction notaDebitoProveedorAction;
    @Inject
    private ComprobanteService comprobanteService;
    @Inject
    private CompaniaService companiaService;
    @Inject
    private UsuarioService usuarioService;
    @Inject
    private MenuBean menuBean;
    private Comprobante comprobante;
    private List<Compania> companiasAdecco;
    private LazyDataModel<Comprobante> comprobantes;
    private Comprobante[] selectedComprobantes;
    private List<EstadoFactura> estados;
    private EstadoFactura estadoEnviada;
    private String motivoRechazo;
    private ComprobanteFilter filter;
    protected EnumSet<Vista> vistaActual;
    
    public AceptacionFacturaAction() {
        this.estadoEnviada = EstadoFactura.ENVIADA;
    }
    
    @PostConstruct
    public void init() {
        //this.comprobantes = (LazyDataModel)new AceptacionFacturaAction$1(this);
        this.selectedComprobantes = new Comprobante[0];
        this.motivoRechazo = "";
        this.filter = new ComprobanteFilter();
        try {
            this.companiasAdecco = this.companiaService.getCompaniasAdecco();
            this.cargarEstados();
            final UsuarioEbsDto usuarioEbs = this.menuBean.getUsuarioEbs();
            if (usuarioEbs == null) {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, "No se pudo obtener su usuario EBS");
                this.setVistaActual(null);
                return;
            }
            final String property = new PropertiesHelper("proveedores.properties").getProperty("pagina.factura.aprobacion", "");
            if (!property.isEmpty() && this.usuarioService.isUsuarioLogeadoConAccesoAPagina(property)) {
                this.filter.setSolicitanteId((Long)null);
            }
            else {
                this.filter.setSolicitanteId(Long.valueOf(usuarioEbs.getId()));
            }
        }
        catch (ServiceException ex) {
            final Iterator<String> iterator = ex.getMessages().iterator();
            while (iterator.hasNext()) {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, (String)iterator.next());
            }
            this.setVistaActual(null);
            return;
        }
        this.filter.setEagerLoad(true);
        this.filter.setCompaniaId(this.companiasAdecco.get(0).getId());
        this.filter.setOrderBy((List)Arrays.asList(new OrderExpression((Enum)ComprobanteFilter.OrderElement.FECHA, OrderDirection.DESC)));
        this.filter.setEstado(EstadoFactura.ENVIADA);
        this.setVistaActual(Vista.LISTADO);
    }
    
    public void setVistaActual(final Vista vista) {
        if (vista == null) {
            this.vistaActual = EnumSet.noneOf(Vista.class);
            return;
        }
        // final int n = AceptacionFacturaAction$2.$SwitchMap$ar$com$adecco$proveedores$action$AceptacionFacturaAction$Vista[vista.ordinal()];
        this.vistaActual = EnumSet.of(vista);
    }
    
    public boolean vistaActual(final Vista vista) {
        return this.vistaActual.contains(vista);
    }
    
    public boolean vistaActual(final String s) {
        return this.vistaActual.contains(Vista.valueOf(s));
    }
    
    public void cargarEstados() {
        (this.estados = new ArrayList()).add(EstadoFactura.INGRESADA);
        this.estados.add(EstadoFactura.ENVIADA);
        this.estados.add(EstadoFactura.ACEPTADA);
        this.estados.add(EstadoFactura.RECHAZADA);
        this.estados.add(EstadoFactura.APROBADA);
        this.estados.add(EstadoFactura.PAGADA);
    }
    
    public void show(final Comprobante comprobante) {
        this.comprobante = comprobante;
        switch (comprobante.getTipoComprobante().ordinal()) {
            case 1: {
                this.administracionFacturaAction.initialize(comprobante.getProveedor());
                this.administracionFacturaAction.administracionFactura(Perfil.ACEPTADOR, comprobante.getId());
                this.setVistaActual(Vista.FACTURA);
                break;
            }
            case 2: {
                this.notaCreditoProveedorAction.initialize(comprobante.getProveedor());
                this.notaCreditoProveedorAction.administrar(Perfil.ACEPTADOR, (NotaCredito)comprobante);
                this.setVistaActual(Vista.NOTACREDITO);
                break;
            }
            case 3: {
                this.notaDebitoProveedorAction.initialize(comprobante.getProveedor());
                this.notaDebitoProveedorAction.administrar(Perfil.ACEPTADOR, (NotaDebito)comprobante);
                this.setVistaActual(Vista.NOTADEBITO);
                break;
            }
        }
    }
    
    public void buscar() {
        this.validateFilter();
    }
    
    public void aceptarFacturas() {
        final int length = this.selectedComprobantes.length;
        if (length == 0) {
            Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, "Debe seleccionar un comprobante del listado.");
            return;
        }
        try {
            final Comprobante[] selectedComprobantes = this.selectedComprobantes;
            for (int length2 = selectedComprobantes.length, i = 0; i < length2; ++i) {
                this.comprobanteService.aceptar(selectedComprobantes[i]);
            }
            this.selectedComprobantes = new Comprobante[0];
        }
        catch (ServiceException ex) {
            final Iterator<String> iterator = (Iterator<String>)ex.getMessages().iterator();
            while (iterator.hasNext()) {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, (String)iterator.next());
            }
            return;
        }
        if (length > 1) {
            Mensajes.displayFacesMessage(FacesMessage.SEVERITY_INFO, "Los comprobantes seleccionados fueron aceptados.");
        }
        else {
            Mensajes.displayFacesMessage(FacesMessage.SEVERITY_INFO, "El comprobante seleccionado fue aceptado.");
        }
        this.buscar();
    }
    
    public void aceptar() {
        try {
            this.comprobanteService.aceptar(this.comprobante);
        }
        catch (ServiceException ex) {
            final Iterator<String> iterator = ex.getMessages().iterator();
            while (iterator.hasNext()) {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, (String)iterator.next());
            }
            return;
        }
        this.setVistaActual(Vista.LISTADO);
        Mensajes.displayFacesMessage(FacesMessage.SEVERITY_INFO, "El comprobante fue aceptado.");
        this.buscar();
    }
    
    public void rechazar() {
        if (this.motivoRechazo.isEmpty()) {
            Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, "Debe ingresar un motivo de rechazo.");
            return;
        }
        this.setVistaActual(Vista.LISTADO);
        try {
            this.comprobanteService.rechazar(this.comprobante, this.motivoRechazo, true);
        }
        catch (ServiceException ex) {
            final Iterator<String> iterator = ex.getMessages().iterator();
            while (iterator.hasNext()) {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, (String)iterator.next());
            }
            return;
        }
        this.motivoRechazo = "";
        this.buscar();
        Mensajes.displayFacesMessage(FacesMessage.SEVERITY_INFO, "El comprobante fue rechazado.");
    }
    
    public void volver() {
        this.setVistaActual(Vista.LISTADO);
    }
    
    public boolean validateFilter() {
        boolean b = true;
        if (!Strings.isNullOrEmpty(this.filter.getNumeroDesde()) && !Strings.isNullOrEmpty(this.filter.getNumeroHasta()) && this.filter.getNumeroDesde().compareTo(this.filter.getNumeroHasta()) > 0) {
            Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, "El N\u00famero Desde no puede ser mayor al N\u00famero Hasta.");
            b = false;
        }
        if (this.filter.getFechaDesde() != null && this.filter.getFechaHasta() != null && this.filter.getFechaHasta().before(this.filter.getFechaDesde())) {
            Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, "La Fecha Desde no puede ser posterior a la Fecha Hasta.");
            b = false;
        }
        return b;
    }
    
    public OrdenCompra getOrdenCompra(final Comprobante comprobante) {
        switch (comprobante.getTipoComprobante().ordinal()) {
            case 1: {
                return ((Factura)comprobante).getOrdenCompra();
            }
            case 2: {
                return ((NotaCredito)comprobante).getFacturaAplicada().getOrdenCompra();
            }
            case 3: {
                return ((NotaDebito)comprobante).getFacturaAplicada().getOrdenCompra();
            }
            default: {
                return null;
            }
        }
    }
    
    public Factura getFacturaAplicada(final Comprobante comprobante) {
        switch (comprobante.getTipoComprobante().ordinal()) {
            case 2: {
                return ((NotaCredito)comprobante).getFacturaAplicada();
            }
            case 3: {
                return ((NotaDebito)comprobante).getFacturaAplicada();
            }
            default: {
                return null;
            }
        }
    }
    
    public EstadoFactura getEstadoEnviada() {
        return this.estadoEnviada;
    }
    
    public void setEstadoEnviada(final EstadoFactura estadoEnviada) {
        this.estadoEnviada = estadoEnviada;
    }
    
    public List<Compania> getCompaniasAdecco() {
        return (List<Compania>)this.companiasAdecco;
    }
    
    public void setCompaniasAdecco(final List<Compania> companiasAdecco) {
        this.companiasAdecco = companiasAdecco;
    }
    
    public LazyDataModel<Comprobante> getComprobantes() {
        return (LazyDataModel<Comprobante>)this.comprobantes;
    }
    
    public void setComprobantes(final LazyDataModel<Comprobante> comprobantes) {
        this.comprobantes = comprobantes;
    }
    
    public Comprobante[] getSelectedComprobantes() {
        return this.selectedComprobantes;
    }
    
    public void setSelectedComprobantes(final Comprobante[] selectedComprobantes) {
        this.selectedComprobantes = selectedComprobantes;
    }
    
    public String getMotivoRechazo() {
        return this.motivoRechazo;
    }
    
    public void setMotivoRechazo(final String motivoRechazo) {
        this.motivoRechazo = motivoRechazo;
    }
    
    public Comprobante getComprobante() {
        return this.comprobante;
    }
    
    public void setComprobante(final Comprobante comprobante) {
        this.comprobante = comprobante;
    }
    
    public List<EstadoFactura> getEstados() {
        return (List<EstadoFactura>)this.estados;
    }
    
    public void setEstados(final List<EstadoFactura> estados) {
        this.estados = estados;
    }
    
    public ComprobanteFilter getFilter() {
        return this.filter;
    }
    
    public void setFilter(final ComprobanteFilter filter) {
        this.filter = filter;
    }
    
        
    public static enum Vista {
        LISTADO,
        FACTURA,
        NOTACREDITO,
        NOTADEBITO
    }
}
