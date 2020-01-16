// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.action;

import ar.com.adecco.proveedores.model.Factura;
import ar.com.adecco.dominio.ebs.po.OrdenCompra;
import com.google.common.base.Strings;
import ar.com.adecco.proveedores.model.EstadoComprobanteEbs;
import java.util.HashSet;
import ar.com.adecco.proveedores.model.NotaDebito;
import ar.com.adecco.proveedores.model.NotaCredito;
import ar.com.adecco.proveedores.model.Perfil;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import java.util.Iterator;
import java.util.Arrays;
import ar.com.adecco.proveedores.filters.commons.OrderDirection;
import ar.com.adecco.proveedores.filters.commons.OrderExpression;
import ar.com.adecco.proveedores.daos.exception.ServiceException;
import ar.com.syntagma.uiutils.Mensajes;
import javax.faces.application.FacesMessage;
import java.util.HashMap;
import java.util.EnumSet;
import ar.com.adecco.proveedores.filters.ComprobanteFilter;
import ar.com.adecco.dominio.ebs.fnd.Usuario;
import java.util.Map;
import ar.com.adecco.proveedores.model.EstadoFactura;
import org.primefaces.model.LazyDataModel;
import ar.com.adecco.dominio.ebs.hr.Compania;
import java.util.List;
import ar.com.adecco.proveedores.model.Comprobante;
import ar.com.adecco.proveedores.service.ComprobanteService;
import ar.com.adecco.proveedores.service.CompaniaService;
import ar.com.adecco.proveedores.service.UsuarioService;
import ar.com.adecco.proveedores.service.FacturaService;
import javax.inject.Inject;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class ListadoFacturasCtasAPagarAction implements Serializable
{
    private static final long serialVersionUID = -1018680827158327112L;
    @Inject
    private AdministracionFacturaAction administracionFacturaAction;
    @Inject
    private NotaCreditoProveedorAction notaCreditoProveedorAction;
    @Inject
    private NotaDebitoProveedorAction notaDebitoProveedorAction;
    @Inject
    private FacturaService facturaService;
    @Inject
    private UsuarioService usuarioService;
    @Inject
    private CompaniaService companiaService;
    @Inject
    private ComprobanteService comprobanteService;
    private Comprobante comprobante;
    private List<Compania> companiasAdecco;
    private LazyDataModel<Comprobante> comprobantes;
    private List<EstadoFactura> estados;
    private Map<Comprobante, Boolean> facturasAAprobar;
    private Map<Comprobante, Boolean> facturasALiberar;
    private Usuario usuarioEBS;
    private boolean existRegInList;
    private boolean existRegInListLiberar;
    private String motivoRechazo;
    private boolean rechazoVolverAAceptar;
    private ComprobanteFilter filter;
    protected EnumSet<Vista> vistaActual;
    
    @PostConstruct
    public void init() {
        // this.comprobantes = (LazyDataModel)new ListadoFacturasCtasAPagarAction$1(this);
        this.facturasAAprobar = new HashMap();
        this.facturasALiberar = new HashMap();
        this.usuarioEBS = null;
        this.motivoRechazo = "";
        this.existRegInList = false;
        this.existRegInListLiberar = false;
        try {
            this.companiasAdecco = this.companiaService.getCompaniasAdecco();
            this.cargarEstados();
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
        this.setVistaActual(Vista.LISTADO);
    }
    
    public void setVistaActual(final Vista vista) {
        if (vista == null) {
            this.vistaActual = EnumSet.noneOf(Vista.class);
            return;
        }
        //final int n = ListadoFacturasCtasAPagarAction$2.$SwitchMap$ar$com$adecco$proveedores$action$ListadoFacturasCtasAPagarAction$Vista[vista.ordinal()];
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
                this.administracionFacturaAction.administracionFactura(Perfil.APROBADOR, comprobante.getId());
                this.setVistaActual(Vista.FACTURA);
                break;
            }
            case 2: {
                this.notaCreditoProveedorAction.initialize(comprobante.getProveedor());
                this.notaCreditoProveedorAction.administrar(Perfil.APROBADOR, (NotaCredito)comprobante);
                this.setVistaActual(Vista.NOTACREDITO);
                break;
            }
            case 3: {
                this.notaDebitoProveedorAction.initialize(comprobante.getProveedor());
                this.notaDebitoProveedorAction.administrar(Perfil.APROBADOR, (NotaDebito)comprobante);
                this.setVistaActual(Vista.NOTADEBITO);
                break;
            }
        }
    }
    
    public boolean isAprobarEnabled() {
        return this.filter.getEstado() == EstadoFactura.ACEPTADA || this.existRegInList;
    }
    
    public boolean isLiberarEnabled() {
        return this.filter.getEstado() == EstadoFactura.ACEPTADA || this.existRegInListLiberar;
    }
    
    public void buscar() {
        this.facturasAAprobar = new HashMap();
        this.facturasALiberar = new HashMap();
        this.validateFilter();
    }
    
    private void buscarInner() {
        this.validateFilter();
    }
    
    public void aprobarFacturas() {
        try {
            this.usuarioEBS = this.usuarioService.getUsuarioEbs();
        }
        catch (ServiceException ex) {
            final Iterator<String> iterator = ex.getMessages().iterator();
            while (iterator.hasNext()) {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, (String)iterator.next());
            }
            return;
        }
        if (this.usuarioEBS == null) {
            Mensajes.displayFacesMessage(FacesMessage.SEVERITY_INFO, "Usted no puede realizar la operaci\u00f3n por falta de permisos.");
            return;
        }
        final HashSet<Comprobante> set = new HashSet<Comprobante>();
        for (final Map.Entry<Comprobante, Boolean> entry : this.facturasAAprobar.entrySet()) {
            if (entry.getValue()) {
                set.add((Comprobante)entry.getKey());
            }
        }
        if (set.isEmpty()) {
            Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, "Debe seleccionar un comprobante del listado.");
            return;
        }
        boolean b = false;
        int n = 0;
        int n2 = 0;
        int n3 = 0;
        try {
            for (final Comprobante comprobante : set) {
                this.comprobanteService.aprobar(comprobante, this.usuarioEBS.getId());
                b = true;
                if (comprobante.getEstado() == EstadoFactura.APROBADA) {
                    this.facturasAAprobar.put(comprobante, false);
                    ++n;
                }
                else if (comprobante.getEstadoEbs() == EstadoComprobanteEbs.ERROR_FACTURACION) {
                    ++n3;
                }
                else {
                    if (comprobante.getEstadoEbs() != EstadoComprobanteEbs.RETENIDA) {
                        continue;
                    }
                    ++n2;
                }
            }
        }
        catch (ServiceException ex2) {
            final Iterator<String> iterator4 = ex2.getMessages().iterator();
            while (iterator4.hasNext()) {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, (String)iterator4.next());
            }
        }
        if (n == set.size()) {
            if (n > 1) {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_INFO, "Los comprobantes seleccionados fueron aprobados.");
            }
            else {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_INFO, "El comprobante seleccionado fue aprobado.");
            }
        }
        else if (n3 == set.size()) {
            if (n3 > 1) {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_WARN, "Los comprobantes seleccionados no pudieron darse de alta en EBS.");
            }
            else {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_WARN, "El comprobante seleccionado no pudo darse de alta en EBS.");
            }
        }
        else if (n2 == set.size()) {
            if (n2 > 1) {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_WARN, "Los comprobantes seleccionados fueron retenidos.");
            }
            else {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_WARN, "El comprobante seleccionado fue retenido.");
            }
        }
        else {
            if (n > 1) {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_INFO, n + " comprobantes fueron aprobados.");
            }
            else {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_INFO, "Un comprobante fue aprobado.");
            }
            if (n2 > 1) {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_WARN, n2 + " comprobantes fueron retenidos.");
            }
            else {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_WARN, "Un comprobante fue retenido.");
            }
            if (n3 > 1) {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_WARN, n3 + " comprobantes no pudieron darse de alta en EBS.");
            }
            else {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_WARN, "Un comprobante no pudo darse de alta en EBS.");
            }
        }
        if (b) {
            this.buscarInner();
        }
    }
    
    public void liberarFacturas() {
        try {
            this.usuarioEBS = this.usuarioService.getUsuarioEbs();
        }
        catch (ServiceException ex) {
            final Iterator<String> iterator = ex.getMessages().iterator();
            while (iterator.hasNext()) {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, (String)iterator.next());
            }
            return;
        }
        if (this.usuarioEBS == null) {
            Mensajes.displayFacesMessage(FacesMessage.SEVERITY_INFO, "Usted no puede realizar la operaci\u00f3n por falta de permisos.");
            return;
        }
        final HashSet<Comprobante> set = new HashSet<Comprobante>();
        for (final Map.Entry<Comprobante, Boolean> entry : this.facturasALiberar.entrySet()) {
            if (entry.getValue()) {
                set.add((Comprobante)entry.getKey());
            }
        }
        if (set.isEmpty()) {
            Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, "Debe seleccionar un comprobante del listado.");
            return;
        }
        boolean b = false;
        int n = 0;
        int n2 = 0;
        try {
            for (final Comprobante comprobante : set) {
                this.comprobanteService.liberar(comprobante, this.usuarioEBS.getId());
                b = true;
                if (comprobante.getEstado() == EstadoFactura.APROBADA) {
                    this.facturasALiberar.put(comprobante, false);
                    ++n;
                }
                else {
                    if (comprobante.getEstadoEbs() != EstadoComprobanteEbs.RETENIDA) {
                        continue;
                    }
                    ++n2;
                }
            }
        }
        catch (ServiceException ex2) {
            final Iterator<String> iterator4 = ex2.getMessages().iterator();
            while (iterator4.hasNext()) {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, (String)iterator4.next());
            }
        }
        if (n == set.size()) {
            if (n > 1) {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_INFO, "Los comprobantes seleccionados fueron liberados.");
            }
            else {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_INFO, "El comprobante seleccionado fue liberado.");
            }
        }
        else if (n2 == set.size()) {
            if (n2 > 1) {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_WARN, "Los comprobantes seleccionados contin\u00faan retenidos.");
            }
            else {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_WARN, "El comprobante seleccionado contin\u00faa retenido.");
            }
        }
        else {
            if (n > 1) {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_INFO, n + " comprobantes fueron liberados.");
            }
            else {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_INFO, "Un comprobante fue liberado.");
            }
            if (n2 > 1) {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_WARN, n2 + " comprobantes contin\u00faan retenidos.");
            }
            else {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_WARN, "Un comprobante contin\u00faa retenido.");
            }
        }
        if (b) {
            this.buscarInner();
        }
    }
    
    public void aprobarFactura() {
        try {
            this.usuarioEBS = this.usuarioService.getUsuarioEbs();
        }
        catch (ServiceException ex) {
            final Iterator<String> iterator = ex.getMessages().iterator();
            while (iterator.hasNext()) {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, (String)iterator.next());
            }
            return;
        }
        if (this.usuarioEBS == null) {
            Mensajes.displayFacesMessage(FacesMessage.SEVERITY_INFO, "Usted no puede realizar la operaci\u00f3n por falta de permisos.");
            return;
        }
        try {
            this.comprobanteService.aprobar(this.comprobante, this.usuarioEBS.getId());
        }
        catch (ServiceException ex2) {
            final Iterator<String> iterator2 = ex2.getMessages().iterator();
            while (iterator2.hasNext()) {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, (String)iterator2.next());
            }
            return;
        }
        this.setVistaActual(Vista.LISTADO);
        if (this.comprobante.getEstado() == EstadoFactura.APROBADA) {
            Mensajes.displayFacesMessage(FacesMessage.SEVERITY_INFO, "El comprobante fue aprobado.");
        }
        else if (this.comprobante.getEstadoEbs() == EstadoComprobanteEbs.ERROR_FACTURACION) {
            Mensajes.displayFacesMessage(FacesMessage.SEVERITY_INFO, "El comprobante no pudo darse de alta en EBS.");
        }
        else if (this.comprobante.getEstadoEbs() == EstadoComprobanteEbs.RETENIDA) {
            Mensajes.displayFacesMessage(FacesMessage.SEVERITY_INFO, "El comprobante fue retenido.");
        }
        this.buscarInner();
    }
    
    public void liberar() {
        try {
            this.usuarioEBS = this.usuarioService.getUsuarioEbs();
        }
        catch (ServiceException ex) {
            final Iterator<String> iterator = ex.getMessages().iterator();
            while (iterator.hasNext()) {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, (String)iterator.next());
            }
            return;
        }
        if (this.usuarioEBS == null) {
            Mensajes.displayFacesMessage(FacesMessage.SEVERITY_INFO, "Usted no puede realizar la operaci\u00f3n por falta de permisos.");
            return;
        }
        try {
            this.comprobanteService.liberar(this.comprobante, this.usuarioEBS.getId());
        }
        catch (ServiceException ex2) {
            final Iterator<String> iterator2 = ex2.getMessages().iterator();
            while (iterator2.hasNext()) {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, (String)iterator2.next());
            }
            return;
        }
        this.setVistaActual(Vista.LISTADO);
        if (this.comprobante.getEstado() == EstadoFactura.APROBADA) {
            Mensajes.displayFacesMessage(FacesMessage.SEVERITY_INFO, "El comprobante fue liberado.");
        }
        else if (this.comprobante.getEstadoEbs() == EstadoComprobanteEbs.RETENIDA) {
            Mensajes.displayFacesMessage(FacesMessage.SEVERITY_INFO, "El comprobante contin\u00faa retenido.");
        }
        this.buscarInner();
    }
    
    public void rechazar() {
        if (this.motivoRechazo.isEmpty()) {
            Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, "Debe ingresar un motivo de rechazo necesariamente.");
            return;
        }
        this.setVistaActual(Vista.LISTADO);
        try {
            this.comprobanteService.rechazar(this.comprobante, this.motivoRechazo, this.rechazoVolverAAceptar);
        }
        catch (ServiceException ex) {
            final Iterator<String> iterator = ex.getMessages().iterator();
            while (iterator.hasNext()) {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, (String)iterator.next());
            }
            return;
        }
        this.motivoRechazo = "";
        this.buscarInner();
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
    
    public Comprobante getComprobante() {
        return this.comprobante;
    }
    
    public void setComprobante(final Comprobante comprobante) {
        this.comprobante = comprobante;
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
    
    public List<EstadoFactura> getEstados() {
        return (List<EstadoFactura>)this.estados;
    }
    
    public void setEstados(final List<EstadoFactura> estados) {
        this.estados = estados;
    }
    
    public Map<Comprobante, Boolean> getFacturasAAprobar() {
        return (Map<Comprobante, Boolean>)this.facturasAAprobar;
    }
    
    public void setFacturasAAprobar(final Map<Comprobante, Boolean> facturasAAprobar) {
        this.facturasAAprobar = facturasAAprobar;
    }
    
    public Map<Comprobante, Boolean> getFacturasALiberar() {
        return (Map<Comprobante, Boolean>)this.facturasALiberar;
    }
    
    public void setFacturasALiberar(final Map<Comprobante, Boolean> facturasALiberar) {
        this.facturasALiberar = facturasALiberar;
    }
    
    public ComprobanteFilter getFilter() {
        return this.filter;
    }
    
    public void setFilter(final ComprobanteFilter filter) {
        this.filter = filter;
    }
    
    public String getMotivoRechazo() {
        return this.motivoRechazo;
    }
    
    public void setMotivoRechazo(final String motivoRechazo) {
        this.motivoRechazo = motivoRechazo;
    }
    
    public boolean isRechazoVolverAAceptar() {
        return this.rechazoVolverAAceptar;
    }
    
    public void setRechazoVolverAAceptar(final boolean rechazoVolverAAceptar) {
        this.rechazoVolverAAceptar = rechazoVolverAAceptar;
    }
    
    public static enum Vista {
        LISTADO,
        FACTURA,
        NOTACREDITO,
        NOTADEBITO
    }

}
