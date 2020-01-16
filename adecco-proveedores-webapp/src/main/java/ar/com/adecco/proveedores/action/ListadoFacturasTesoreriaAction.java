// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.action;

import com.google.common.base.Strings;
import java.util.Collection;
import java.util.ArrayList;
import java.util.HashMap;
import org.primefaces.context.RequestContext;
import ar.com.adecco.proveedores.model.Perfil;
import javax.annotation.PostConstruct;
import java.util.Iterator;
import java.util.Arrays;
import ar.com.adecco.proveedores.filters.commons.OrderDirection;
import ar.com.adecco.proveedores.filters.commons.OrderExpression;
import ar.com.adecco.proveedores.daos.exception.ServiceException;
import ar.com.syntagma.uiutils.Mensajes;
import javax.faces.application.FacesMessage;
import java.util.Calendar;
import org.apache.commons.lang.time.DateUtils;
import java.util.EnumSet;
import ar.com.adecco.proveedores.filters.ComprobanteFilter;
import java.util.Date;
import ar.com.adecco.proveedores.model.EstadoFactura;
import ar.com.adecco.dominio.ebs.ap.Chequera;
import ar.com.adecco.dominio.ebs.po.Proveedor;
import java.util.Map;
import ar.com.adecco.proveedores.datamodel.SimpleSelectableDataModel;
import ar.com.adecco.dominio.ebs.hr.Compania;
import java.util.List;
import ar.com.adecco.dominio.ebs.ap.Factura;
import ar.com.adecco.proveedores.service.ChequeraEbsService;
import ar.com.adecco.proveedores.service.CompaniaService;
import ar.com.adecco.proveedores.service.FacturaEbsService;
import javax.inject.Inject;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class ListadoFacturasTesoreriaAction implements Serializable
{
    private static final long serialVersionUID = 3585201866063154184L;
    @Inject
    private AdministracionFacturaEbsAction administracionFacturaEbsAction;
    @Inject
    private FacturaEbsService facturaEbsService;
    @Inject
    private CompaniaService companiaService;
    @Inject
    private ChequeraEbsService chequeraEbsService;
    private Factura factura;
    private List<Compania> companiasAdecco;
    private SimpleSelectableDataModel<Factura> facturas;
    private Factura[] selectedFacturas;
    private Map<Proveedor, List<Factura>> selectedComprobantesByProveedor;
    private List<Chequera> chequeras;
    private Chequera chequera;
    private EstadoFactura estadoAprobada;
    private Date fechaPago;
    private Date fechaActual;
    private boolean popupMessages;
    private ComprobanteFilter filter;
    protected EnumSet<Vista> vistaActual;
    
    public ListadoFacturasTesoreriaAction() {
        this.estadoAprobada = EstadoFactura.APROBADA;
    }
    
    @PostConstruct
    public void init() {
        // this.facturas = (SimpleSelectableDataModel)new ListadoFacturasTesoreriaAction(this);
        this.fechaActual = DateUtils.truncate(new Date(), 5);
        final Calendar instance = Calendar.getInstance();
        instance.setTime(this.fechaActual);
        instance.add(5, -100);
        this.selectedFacturas = new Factura[0];
        try {
            this.companiasAdecco = this.companiaService.getCompaniasAdecco();
        }
        catch (ServiceException ex) {
            final Iterator<String> iterator = ex.getMessages().iterator();
            while (iterator.hasNext()) {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, (String)iterator.next());
            }
            return;
        }
        (this.filter = new ComprobanteFilter()).setEagerLoad(true);
        this.filter.setCompaniaId(this.companiasAdecco.get(0).getId());
        this.filter.setOrderBy((List)Arrays.asList(new OrderExpression((Enum)ComprobanteFilter.OrderElement.FECHA, OrderDirection.DESC)));
        this.filter.setEstado(EstadoFactura.ACEPTADA);
        this.filter.setFechaDesde(instance.getTime());
        this.setVistaActual(Vista.LISTADO);
        this.buscar();
    }
    
    public void setVistaActual(final Vista vista) {
        if (vista == null) {
            this.vistaActual = EnumSet.noneOf(Vista.class);
            return;
        }
        //final int n = ListadoFacturasTesoreriaAction$2.$SwitchMap$ar$com$adecco$proveedores$action$ListadoFacturasTesoreriaAction$Vista[vista.ordinal()];
        this.vistaActual = EnumSet.of(vista);
    }
    
    public boolean vistaActual(final Vista vista) {
        return this.vistaActual.contains(vista);
    }
    
    public boolean vistaActual(final String s) {
        return this.vistaActual.contains(Vista.valueOf(s));
    }
    
    public void administracionFactura(final long n) {
        try {
            this.factura = this.facturaEbsService.getById(n);
        }
        catch (ServiceException ex) {
            final Iterator<String> iterator = ex.getMessages().iterator();
            while (iterator.hasNext()) {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, (String)iterator.next());
            }
            return;
        }
        this.setVistaActual(Vista.FACTURA);
        this.administracionFacturaEbsAction.administracionFactura(Perfil.PAGO, n);
    }
    
    public void buscar() {
        if (this.validateFilter()) {
            try {
                this.facturas.setWrappedData((Object)this.facturaEbsService.getByFilter(this.filter));
            }
            catch (ServiceException ex) {
                final Iterator<String> iterator = ex.getMessages().iterator();
                while (iterator.hasNext()) {
                    Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, (String)iterator.next());
                }
            }
        }
    }
    
    public void pagarFactura() {
        if (!this.validarPago()) {
            return;
        }
        try {
            this.facturaEbsService.pagar((List)Arrays.asList(this.administracionFacturaEbsAction.getFacturaSeleccionada()), this.chequera, this.fechaPago);
            this.setVistaActual(Vista.LISTADO);
            Mensajes.displayFacesMessage(FacesMessage.SEVERITY_INFO, "El comprobante fue pagado.");
        }
        catch (ServiceException ex) {
            final Iterator<String> iterator = ex.getMessages().iterator();
            while (iterator.hasNext()) {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, (String)iterator.next());
            }
        }
    }
    
    public void pagarComprobantes() {
        if (!this.validarPagos()) {
            return;
        }
        this.loadComprobantesByProveedor();
        final int length = this.selectedFacturas.length;
        boolean b = false;
        try {
            final Iterator<List<Factura>> iterator = this.selectedComprobantesByProveedor.values().iterator();
            while (iterator.hasNext()) {
                this.facturaEbsService.pagar(iterator.next(), this.chequera, this.fechaPago);
                b = true;
            }
            this.selectedFacturas = new Factura[0];
        }
        catch (ServiceException ex) {
            final Iterator<String> iterator2 = (Iterator<String>)ex.getMessages().iterator();
            while (iterator2.hasNext()) {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, (String)iterator2.next());
            }
            if (b) {
                this.buscar();
            }
            return;
        }
        this.setVistaActual(Vista.LISTADO);
        if (length > 1) {
            Mensajes.displayFacesMessage(FacesMessage.SEVERITY_INFO, "Los comprobantes seleccionados fueron pagados.");
        }
        else {
            Mensajes.displayFacesMessage(FacesMessage.SEVERITY_INFO, "El comprobante seleccionado fue pagado.");
        }
        this.buscar();
    }
    
    public void showPagarComprobantes() {
        if (!this.validarPagos()) {
            RequestContext.getCurrentInstance().addCallbackParam("failed", true);
            return;
        }
        this.loadComprobantesByProveedor();
    }
    
    private void loadComprobantesByProveedor() {
        this.selectedComprobantesByProveedor = new HashMap();
        for (final Factura factura : this.selectedFacturas) {
            List<Factura> list = this.selectedComprobantesByProveedor.get(factura.getProveedor());
            if (list == null) {
                list = new ArrayList<Factura>();
                this.selectedComprobantesByProveedor.put(factura.getProveedor(), list);
            }
            list.add(factura);
        }
    }
    
    public Double getSaldoAPagarByProveedor(final Proveedor proveedor) {
        if (this.selectedComprobantesByProveedor == null) {
            return null;
        }
        Double n = 0.0;
        final Iterator<Factura> iterator = this.selectedComprobantesByProveedor.get(proveedor).iterator();
        while (iterator.hasNext()) {
            n += iterator.next().getSaldo();
        }
        return n;
    }
    
    public List<Proveedor> getSelectedProveedores() {
        if (this.selectedComprobantesByProveedor == null) {
            return null;
        }
        return new ArrayList<Proveedor>(this.selectedComprobantesByProveedor.keySet());
    }
    
    public List<Factura> getSelectedComprobantesByProveedor(final Proveedor proveedor) {
        if (this.selectedComprobantesByProveedor == null) {
            return null;
        }
        return this.selectedComprobantesByProveedor.get(proveedor);
    }
    
    public void volver() {
        this.setVistaActual(Vista.LISTADO);
    }
    
    public boolean validarPago() {
        boolean b = true;
        if (this.chequera == null) {
            Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, "El campo Chequera es obligatorio, debe seleccionar una opci\u00f3n.");
            b = false;
        }
        if (this.fechaPago == null) {
            Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, "El campo Fecha de pago es obligatorio, debe ingresar una fecha.");
            b = false;
        }
        else if (this.fechaPago.before(this.fechaActual)) {
            Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, "La Fecha de pago debe ser mayor o igual a la fecha actual.");
            b = false;
        }
        return b;
    }
    
    public boolean validarPagos() {
        boolean b = true;
        if (this.selectedFacturas == null || this.selectedFacturas.length == 0) {
            Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, "Debe seleccionar una factura del listado.");
            b = false;
        }
        if (!this.validarPago()) {
            b = false;
        }
        return b;
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
    
    public List<Chequera> obtenerChequeras() throws ServiceException {
        return (List<Chequera>)this.chequeraEbsService.listarPorCompaniaId((long)this.filter.getCompaniaId());
    }
    
    public boolean setPopupMessagesFalse() {
        return this.popupMessages = false;
    }
    
    public FacturaEbsService getFacturaEbsService() {
        return this.facturaEbsService;
    }
    
    public void setFacturaEbsService(final FacturaEbsService facturaEbsService) {
        this.facturaEbsService = facturaEbsService;
    }
    
    public EstadoFactura getEstadoAprobada() {
        return this.estadoAprobada;
    }
    
    public void setEstadoAprobada(final EstadoFactura estadoAprobada) {
        this.estadoAprobada = estadoAprobada;
    }
    
    public AdministracionFacturaEbsAction getAdministracionFacturaEbsAction() {
        return this.administracionFacturaEbsAction;
    }
    
    public void setAdministracionFacturaEbsAction(final AdministracionFacturaEbsAction administracionFacturaEbsAction) {
        this.administracionFacturaEbsAction = administracionFacturaEbsAction;
    }
    
    public ComprobanteFilter getFilter() {
        return this.filter;
    }
    
    public void setFilter(final ComprobanteFilter filter) {
        this.filter = filter;
    }
    
    public Factura getFactura() {
        return this.factura;
    }
    
    public void setFactura(final Factura factura) {
        this.factura = factura;
    }
    
    public List<Compania> getCompaniasAdecco() {
        return (List<Compania>)this.companiasAdecco;
    }
    
    public void setCompaniasAdecco(final List<Compania> companiasAdecco) {
        this.companiasAdecco = companiasAdecco;
    }
    
    public SimpleSelectableDataModel<Factura> getFacturas() {
        return (SimpleSelectableDataModel<Factura>)this.facturas;
    }
    
    public void setFacturas(final SimpleSelectableDataModel<Factura> facturas) {
        this.facturas = facturas;
    }
    
    public Factura[] getSelectedFacturas() {
        return this.selectedFacturas;
    }
    
    public void setSelectedFacturas(final Factura[] selectedFacturas) {
        this.selectedFacturas = selectedFacturas;
    }
    
    public List<Chequera> getChequeras() {
        return (List<Chequera>)this.chequeras;
    }
    
    public void setChequeras(final List<Chequera> chequeras) {
        this.chequeras = chequeras;
    }
    
    public Chequera getChequera() {
        return this.chequera;
    }
    
    public void setChequera(final Chequera chequera) {
        this.chequera = chequera;
    }
    
    public Date getFechaPago() {
        return this.fechaPago;
    }
    
    public void setFechaPago(final Date fechaPago) {
        this.fechaPago = fechaPago;
    }
    
    public boolean isPopupMessages() {
        return this.popupMessages;
    }
    
    public static enum Vista {
        LISTADO,
        FACTURA
    }

}
