// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.action;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import ar.com.adecco.proveedores.daos.helpers.ErrorMessage;
import org.primefaces.event.FileUploadEvent;
import ar.com.syntagma.helpers.HolderHelper;
import org.primefaces.context.RequestContext;
import java.util.Date;
import ar.com.adecco.proveedores.model.EstadoFactura;
import ar.com.adecco.proveedores.bean.MessageBean;
import ar.com.adecco.proveedores.model.Comprobante;
import ar.com.adecco.proveedores.model.FacturaEstado;
import ar.com.adecco.dominio.ebs.fnd.Provincia;
import java.util.Collection;
import javax.faces.context.FacesContext;
import ar.com.adecco.proveedores.model.CondicionIVA;
import java.util.Iterator;
import ar.com.adecco.proveedores.daos.exception.ServiceException;
import ar.com.syntagma.uiutils.Mensajes;
import javax.faces.application.FacesMessage;
import java.util.HashMap;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import org.primefaces.component.datatable.DataTable;
import ar.com.adecco.proveedores.model.Letra;
import javax.faces.model.SelectItem;
import ar.com.adecco.proveedores.model.Perfil;
import ar.com.adecco.proveedores.model.Vista;
import ar.com.adecco.dominio.ebs.hr.Compania;
import ar.com.adecco.dominio.ebs.ap.Impuesto;
import ar.com.adecco.dominio.ebs.po.LineaOrdenCompra;
import ar.com.adecco.proveedores.model.IIBBMultilateral;
import java.util.List;
import ar.com.adecco.proveedores.bean.ImportesBean;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import ar.com.adecco.proveedores.model.ComprobanteAdjunto;
import ar.com.adecco.proveedores.model.Linea;
import ar.com.adecco.proveedores.model.Proveedor;
import ar.com.adecco.proveedores.model.Factura;
import ar.com.adecco.dominio.ebs.po.OrdenCompra;
import ar.com.adecco.proveedores.service.ComprobanteAdjuntoService;
import ar.com.adecco.proveedores.service.CompaniaService;
import ar.com.adecco.proveedores.service.ImpuestoService;
import ar.com.adecco.proveedores.service.LineaOCService;
import ar.com.adecco.proveedores.service.ProvinciaService;
import ar.com.adecco.proveedores.service.IIBBMultilateralService;
import ar.com.adecco.proveedores.service.ProveedorService;
import ar.com.adecco.proveedores.service.FacturaEstadoService;
import ar.com.adecco.proveedores.service.ComprobanteService;
import ar.com.adecco.proveedores.service.FacturaService;
import javax.inject.Inject;
import ar.com.adecco.proveedores.service.OrdenCompraService;
import org.jboss.solder.logging.Logger;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class AdministracionFacturaAction implements Serializable
{
    private static final long serialVersionUID = 5414229127614879083L;
    private static final Logger log;
    @Inject
    private OrdenCompraService ordenCompraService;
    @Inject
    private FacturaService facturaService;
    @Inject
    private ComprobanteService comprobanteService;
    @Inject
    private FacturaEstadoService facturaEstadoService;
    @Inject
    private ProveedorService proveedorService;
    @Inject
    private IIBBMultilateralService iibbMultilateralService;
    @Inject
    private ProvinciaService provinciaService;
    @Inject
    private LineaOCService lineaOCService;
    @Inject
    private ImpuestoService impuestoService;
    @Inject
    private CompaniaService companiaService;
    @Inject
    private ComprobanteAdjuntoService comprobanteAdjuntoService;
    @Inject
    private PanelControlAction panelControlAction;
    private OrdenCompra ordenCompraSeleccionada;
    private Factura facturaSeleccionada;
    private Proveedor proveedor;
    private Linea nuevoItemImpuesto;
    private ComprobanteAdjunto nuevoAdjunto;
    private LazyDataModel<OrdenCompra> ordenesCompraAbiertas;
    private Map<Long, ImportesBean> importesPorOCId;
    private List<String> provinciasPrestoServicio;
    private List<IIBBMultilateral> listaIIBB;
    private List<Linea> listaLineas;
    private List<Linea> lineasFacturaEliminar;
    private List<Linea> listaImpuestos;
    private List<Linea> lineasImpuestoEliminar;
    private List<LineaOrdenCompra> listaLineasOC;
    private List<Linea> lineasDisponibles;
    private Map<Linea, Boolean> lineasDisponiblesSeleccionadas;
    private List<Impuesto> tipoImpuestos;
    private List<Compania> companiasAdecco;
    private List<ComprobanteAdjunto> adjuntos;
    private Vista vistaFactura;
    private Vista altaFac;
    private Vista edicionFactura;
    private Perfil perfilUsuario;
    private Perfil perfilProveedor;
    private List<SelectItem> comboCodigoAutorizacion;
    private List<Letra> comboLetras;
    private String provinciaPrestoServicio;
    private double importeTotalLineas;
    private double importeTotalImpuestos;
    private String nombreImpSeleccionado;
    private DataTable ordenesCompraAbiertasDataTable;
    private static final String tipoImpuestoIVA = "IVA";
    private static final String tipoImpuestoPercIIBB = "PERCIIBB";
    private static final String tipoImpuestoII = "OTHPERC";
    private boolean operacionDeImpuestoDialog;
    
    public AdministracionFacturaAction() {
        this.altaFac = Vista.ALTA_FAC;
        this.edicionFactura = Vista.EDICION_FAC;
        this.perfilProveedor = Perfil.PROVEEDOR;
        this.importeTotalLineas = 0.0;
        this.importeTotalImpuestos = 0.0;
    }
    
    @PostConstruct
    public void init() {
        // this.ordenesCompraAbiertas = (LazyDataModel<OrdenCompra>) new LazyDataModel<OrdenCompra>().getWrappedData();
    }
    
    public void initialize(final Proveedor proveedor) {
        this.ordenCompraSeleccionada = null;
        this.proveedor = proveedor;
        this.nombreImpSeleccionado = null;
        this.provinciasPrestoServicio = new ArrayList();
        this.listaIIBB = null;
        this.nuevoItemImpuesto = new Linea();
        this.listaLineasOC = new ArrayList();
        this.importesPorOCId = new HashMap();
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
        this.setImporteTotalLineas(0.0);
        this.setImporteTotalImpuestos(0.0);
        (this.comboCodigoAutorizacion = new ArrayList()).add(new SelectItem((Object)"CAE", "CAE"));
        this.comboCodigoAutorizacion.add(new SelectItem((Object)"CAI", "CAI"));
        this.actualizarComboLetra(proveedor);
        this.clearFormFactura();
    }
    
    public void clearFormFactura() {
        this.facturaSeleccionada = null;
        this.listaLineas = new ArrayList();
        this.listaImpuestos = new ArrayList();
        this.lineasFacturaEliminar = new ArrayList();
        this.lineasImpuestoEliminar = new ArrayList();
        this.lineasDisponibles = new ArrayList();
        this.lineasDisponiblesSeleccionadas = new HashMap();
        this.adjuntos = null;
    }
    
    private void actualizarComboLetra(final Proveedor proveedor) {
        this.comboLetras = new ArrayList();
        if (proveedor != null) {
            if (proveedor.getCondicionIVA().equals(CondicionIVA.INSCRIPTO.getDescripcion())) {
                this.comboLetras.add(Letra.A);
                this.comboLetras.add(Letra.M);
            }
            else if (proveedor.getCondicionIVA().equals(CondicionIVA.MONOTRIBUTISTA.getDescripcion())) {
                this.comboLetras.add(Letra.C);
            }
            else if (proveedor.getCondicionIVA().equals(CondicionIVA.NO_RESPONSABLE.getDescripcion())) {
                this.comboLetras.add(Letra.C);
            }
            else if (proveedor.getCondicionIVA().equals(CondicionIVA.EXENTO.getDescripcion())) {
                this.comboLetras.add(Letra.C);
            }
            else {
                this.comboLetras.add(Letra.A);
                this.comboLetras.add(Letra.C);
                this.comboLetras.add(Letra.M);
            }
        }
    }
    
    public void volver() {
        this.panelControlAction.showPanelControl();
    }
    
    public boolean isNew() {
        return this.facturaSeleccionada.getId() == 0L;
    }
    
    public void altaFactura() {
        this.setVistaFactura(Vista.ALTA_FAC);
        this.ordenesCompraAbiertasDataTable = (DataTable)FacesContext.getCurrentInstance().getViewRoot().findComponent("administracionFacturaForm:ordenesCompraDataTable");
        if (this.ordenesCompraAbiertasDataTable != null) {
            this.ordenesCompraAbiertasDataTable.reset();
        }
    }
    
    public void guardarFactura() {
        try {
            if (this.actualizarFactura()) {
                this.loadFactura(this.facturaSeleccionada.getId());
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_INFO, "La factura se guardo correctamente.");
            }
        }
        catch (ServiceException ex) {
            final Iterator<String> iterator = ex.getMessages().iterator();
            while (iterator.hasNext()) {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, (String)iterator.next());
            }
        }
    }
    
    public void administracionFactura(final Perfil perfilUsuario, final long n) {
        this.setPerfilUsuario(perfilUsuario);
        this.setVistaFactura(Vista.EDICION_FAC);
        System.out.println("administracionFactura: Id factura: " + n);
        try {
            this.loadFactura(n);
        }
        catch (ServiceException ex) {
            final Iterator<String> iterator = ex.getMessages().iterator();
            while (iterator.hasNext()) {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, (String)iterator.next());
            }
        }
    }
    
    private void createAndLoadFactura(final long n, final OrdenCompra ordenCompra) throws ServiceException {
        this.clearFormFactura();
        this.createFactura(n, ordenCompra);
        this.obtenerItemsFactura();
        this.obtenerItemsImpuesto();
        this.obtenerLineasDisponibles();
        this.obtenerListaImpuestos();
    }
    
    private void loadFactura(final long n) throws ServiceException {
        this.clearFormFactura();
        this.obtenerFacturaSeleccionada(n);
        this.obtenerItemsFactura();
        this.obtenerItemsImpuesto();
        this.obtenerLineasDisponibles();
        this.obtenerListaImpuestos();
    }
    
    public void obtenerListaImpuestos() throws ServiceException {
        this.tipoImpuestos = new ArrayList();
        if (this.facturaSeleccionada != null) {
            this.tipoImpuestos.addAll(this.impuestoService.getImpuestosByType(this.facturaSeleccionada.getCompania(), "IVA"));
            this.tipoImpuestos.addAll(this.impuestoService.getImpuestosByType(this.facturaSeleccionada.getCompania(), "PERCIIBB"));
            this.tipoImpuestos.addAll(this.impuestoService.getImpuestosByType(this.facturaSeleccionada.getCompania(), "OTHPERC"));
        }
    }
    
    public void obtenerFacturaSeleccionada(final long n) throws ServiceException {
        this.facturaSeleccionada = this.facturaService.getById(n);
        if (this.proveedor == null) {
            this.proveedor = this.facturaSeleccionada.getProveedor();
        }
        this.obtenerProvinciasPrestoServicio();
    }
    
    public void obtenerProvinciasPrestoServicio() throws ServiceException {
        this.provinciasPrestoServicio = new ArrayList();
        this.listaIIBB = this.iibbMultilateralService.getIIBBByProveedor(this.proveedor);
        for (final Provincia provincia : this.provinciaService.getProvincias(true)) {
            if (!this.tieneConvenioMultilateral(provincia.getDescripcion())) {
                this.provinciasPrestoServicio.add(provincia.getDescripcion() + " (*)");
            }
            else {
                this.provinciasPrestoServicio.add(provincia.getDescripcion());
            }
        }
        if (this.facturaSeleccionada.getProvinciaPrestoServicio() != null && !this.tieneConvenioMultilateral(this.facturaSeleccionada.getProvinciaPrestoServicio().getNombre())) {
            this.setProvinciaPrestoServicio(this.facturaSeleccionada.getProvinciaPrestoServicio().getNombre() + " (*)");
        }
    }
    
    public boolean tieneConvenioMultilateral(final String s) throws ServiceException {
        if (this.provinciaService.getEntityById(this.proveedor.getJurisdiccionLocal().getId()).getNombre().equals(s)) {
            return true;
        }
        final Iterator<IIBBMultilateral> iterator = this.listaIIBB.iterator();
        while (iterator.hasNext()) {
            if (this.provinciaService.getEntityById(iterator.next().getJurisdiccion().getId()).getNombre().equals(s)) {
                return true;
            }
        }
        return false;
    }
    
    public void obtenerItemsFactura() {
        this.listaLineas = new ArrayList();
        this.listaLineasOC = new ArrayList();
        this.setLineasFacturaEliminar((List)new ArrayList());
        Double n = 0.0;
        for (final Linea linea : this.facturaSeleccionada.getLineas()) {
            if (linea.getTipo() == Linea.TipoLinea.ITEM) {
                this.listaLineas.add(linea);
                this.listaLineasOC.add(linea.getLineaOrdenCompra());
                if (linea.getImporte() != null) {
                    n += linea.getImporte();
                }
                else {
                    AdministracionFacturaAction.log.warn((Object)("Importe de l\u00ednea Id " + linea.getId() + " nulo. Se omite."));
                }
            }
        }
        this.setImporteTotalLineas(n);
    }
    
    public void obtenerItemsImpuesto() {
        this.listaImpuestos = new ArrayList();
        this.setLineasImpuestoEliminar((List)new ArrayList());
        this.setImporteTotalImpuestos(0.0);
        Double n = 0.0;
        for (final Linea linea : this.facturaSeleccionada.getLineas()) {
            if (linea.getTipo() == Linea.TipoLinea.TAX) {
                this.listaImpuestos.add(linea);
                if (linea.getImporte() != null) {
                    n += linea.getImporte();
                }
                else {
                    AdministracionFacturaAction.log.warn((Object)("Importe de l\u00ednea Id " + linea.getId() + " nulo. Se omite."));
                }
            }
        }
        this.setImporteTotalImpuestos(n);
    }
    
    public void obtenerLineasDisponibles() throws ServiceException {
        this.lineasDisponibles = new ArrayList();
        this.lineasDisponiblesSeleccionadas = new HashMap();
        for (final LineaOrdenCompra lineaOrdenCompra : this.lineaOCService.getLineasByOC(this.facturaSeleccionada.getOrdenCompra())) {
            if (!this.listaLineasOC.contains(lineaOrdenCompra)) {
                final Linea linea = this.facturaService.createLinea(Linea.TipoLinea.ITEM, this.facturaSeleccionada, lineaOrdenCompra);
                if (linea.getImporte() <= 0.0) {
                    continue;
                }
                this.lineasDisponibles.add(linea);
            }
        }
    }
    
    public boolean proveedorHabilitado() {
        return this.proveedorService.proveedorHabilitado(this.proveedor);
    }
    
    public FacturaEstado getEstadoActual() {
        try {
            return this.facturaEstadoService.getEstadoActual((Comprobante)this.facturaSeleccionada);
        }
        catch (ServiceException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public boolean actualizarFactura() throws ServiceException {
        long n = 1L;
        if (!this.provinciaPrestoServicio.isEmpty()) {
            String s = this.provinciaPrestoServicio;
            if (this.provinciaPrestoServicio.indexOf("*") != -1) {
                s = this.provinciaPrestoServicio.substring(0, this.provinciaPrestoServicio.length() - 4);
            }
            this.facturaSeleccionada.setProvinciaPrestoServicio(this.provinciaService.getProvinciaByName(s));
        }
        if (this.facturaSeleccionada.getId() == 0L) {
            this.facturaSeleccionada.setLineas((List)new ArrayList());
            this.facturaSeleccionada.setEstados((List)new ArrayList());
            for (final Linea linea : this.listaLineas) {
                linea.setNumero(Long.valueOf(n));
                this.facturaSeleccionada.getLineas().add(linea);
                ++n;
            }
            for (final Linea linea2 : this.listaImpuestos) {
                linea2.setNumero(Long.valueOf(n));
                this.facturaSeleccionada.getLineas().add(linea2);
                ++n;
            }
            final List<MessageBean> actualizarFactura = this.facturaService.actualizarFactura(this.facturaSeleccionada, (List)new ArrayList());
            for (final MessageBean messageBean : actualizarFactura) {
                Mensajes.displayFacesMessage(messageBean.getSeverity(), messageBean.getMessage());
            }
            if (!actualizarFactura.isEmpty()) {
                return false;
            }
        }
        else {
            final ArrayList<Linea> list = new ArrayList<Linea>();
            final ArrayList<Linea> list2 = new ArrayList<Linea>();
            for (final Linea linea3 : this.facturaSeleccionada.getLineas()) {
                if (!this.listaLineas.contains(linea3) && !this.listaImpuestos.contains(linea3)) {
                    list2.add(linea3);
                    if (linea3.getId() <= 0L) {
                        continue;
                    }
                    list.add(linea3);
                }
            }
            this.facturaSeleccionada.getLineas().removeAll(list2);
            for (final Linea linea4 : this.listaLineas) {
                linea4.setNumero(Long.valueOf(n));
                ++n;
                if (!this.facturaSeleccionada.getLineas().contains(linea4)) {
                    this.facturaSeleccionada.getLineas().add(linea4);
                }
            }
            for (final Linea linea5 : this.listaImpuestos) {
                linea5.setNumero(Long.valueOf(n));
                ++n;
                if (!this.facturaSeleccionada.getLineas().contains(linea5)) {
                    this.facturaSeleccionada.getLineas().add(linea5);
                }
            }
            list.addAll(this.lineasFacturaEliminar);
            list.addAll(this.lineasImpuestoEliminar);
            final List<MessageBean> actualizarFactura2 = this.facturaService.actualizarFactura(this.facturaSeleccionada, (List)list);
            for (final MessageBean messageBean2 : actualizarFactura2) {
                Mensajes.displayFacesMessage(messageBean2.getSeverity(), messageBean2.getMessage());
            }
            if (!actualizarFactura2.isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    private void createFactura(final long n, final OrdenCompra ordenCompra) throws ServiceException {
        this.setImporteTotalLineas(0.0);
        this.setImporteTotalImpuestos(0.0);
        (this.facturaSeleccionada = new Factura()).setHabilitado(true);
        this.facturaSeleccionada.setEstado(EstadoFactura.INGRESADA);
        this.facturaSeleccionada.setFecha(new Date());
        this.facturaSeleccionada.setOrdenCompra(ordenCompra);
        this.facturaSeleccionada.setMoneda(ordenCompra.getMoneda());
        this.facturaSeleccionada.setProveedor(this.proveedor);
        this.facturaSeleccionada.setCompania(ordenCompra.getCompania());
        this.facturaSeleccionada.setLineas((List)new ArrayList());
        this.actualizarComboLetra(this.proveedor);
        final Iterator<LineaOrdenCompra> iterator = this.lineaOCService.getLineasByOC(ordenCompra).iterator();
        while (iterator.hasNext()) {
            final Linea linea = this.facturaService.createLinea(Linea.TipoLinea.ITEM, this.facturaSeleccionada, (LineaOrdenCompra)iterator.next());
            if (linea.getImporte() > 0.0) {
                this.facturaSeleccionada.getLineas().add(linea);
            }
        }
        this.obtenerProvinciasPrestoServicio();
    }
    
    public void eliminarLinea(final Linea linea) {
        if (!this.listaLineas.contains(linea)) {
            return;
        }
        this.listaLineas.remove(linea);
        if (linea.getId() != 0L) {
            this.lineasFacturaEliminar.add(linea);
        }
        this.lineasDisponibles.add(this.facturaService.createLinea(Linea.TipoLinea.ITEM, this.facturaSeleccionada, linea.getLineaOrdenCompra(), linea));
        this.actualizarImporteTotal();
    }
    
    public void eliminarImpuesto(final Linea linea) {
        if (!this.listaImpuestos.contains(linea)) {
            return;
        }
        this.listaImpuestos.remove(linea);
        if (linea.getId() != 0L) {
            this.lineasImpuestoEliminar.add(linea);
        }
        this.actualizarImporteTotalImpuesto();
    }
    
    public EstadoFactura getEstadoFactura() {
        if (this.facturaSeleccionada == null) {
            return null;
        }
        return this.facturaSeleccionada.getEstado();
    }
    
    public void asociarFacturaOC(final Perfil perfilUsuario, final long n) {
        this.setPerfilUsuario(perfilUsuario);
        this.setVistaFactura(Vista.EDICION_FAC);
        try {
            this.ordenCompraSeleccionada = this.ordenCompraService.getById(n);
            this.createAndLoadFactura(this.proveedor.getId(), this.ordenCompraSeleccionada);
        }
        catch (ServiceException ex) {
            final Iterator<String> iterator = ex.getMessages().iterator();
            while (iterator.hasNext()) {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, (String)iterator.next());
            }
        }
    }
    
    public void agregarItemsFactura() {
        for (final Map.Entry<Linea, Boolean> entry : this.lineasDisponiblesSeleccionadas.entrySet()) {
            if (entry.getValue()) {
                this.listaLineas.add(entry.getKey());
                this.lineasDisponibles.remove(entry.getKey());
            }
        }
        this.lineasDisponiblesSeleccionadas = new HashMap();
        this.actualizarImporteTotal();
    }
    
    public void agregarItemImpuesto() {
        if (!this.validarNuevoItemImpuesto()) {
            System.out.println("agregarItemImpuesto(): Error de validaci\u00f3n.");
            this.operacionDeImpuestoDialog = true;
            return;
        }
        try {
            this.nuevoItemImpuesto.setImpuesto(this.impuestoService.getImpuestoByName(this.facturaSeleccionada.getCompania(), this.nombreImpSeleccionado));
            this.listaImpuestos.add(this.nuevoItemImpuesto);
            this.importeTotalImpuestos += this.nuevoItemImpuesto.getImporte();
        }
        catch (ServiceException ex) {
            this.operacionDeImpuestoDialog = true;
            final Iterator<String> iterator = ex.getMessages().iterator();
            while (iterator.hasNext()) {
                Mensajes.displayClientFacesMessage("mensajesImpuestoDialog", FacesMessage.SEVERITY_ERROR, (String)iterator.next());
            }
            return;
        }
        RequestContext.getCurrentInstance().execute("dlgPopUpImpuesto.hide()");
    }
    
    public void enviarFactura() {
        try {
            if (this.validarImportes() && this.validarAdjuntos() && this.actualizarFactura()) {
                this.loadFactura(this.facturaSeleccionada.getId());
                this.facturaService.enviarFactura(this.facturaSeleccionada);
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_INFO, "La factura se envi\u00f3 correctamente");
                this.volver();
            }
        }
        catch (ServiceException ex) {
            final Iterator<String> iterator = ex.getMessages().iterator();
            while (iterator.hasNext()) {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, (String)iterator.next());
            }
        }
    }
    
    public void eliminarFactura() {
        try {
            this.comprobanteService.eliminar((Comprobante)this.facturaSeleccionada);
        }
        catch (ServiceException ex) {
            final Iterator<String> iterator = ex.getMessages().iterator();
            while (iterator.hasNext()) {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, (String)iterator.next());
            }
            return;
        }
        Mensajes.displayFacesMessage(FacesMessage.SEVERITY_INFO, "La factura se elimin\u00f3 correctamente");
        this.volver();
    }
    
    public void abrirPopUpImpuesto() {
        this.setNombreImpSeleccionado(null);
        (this.nuevoItemImpuesto = new Linea()).setTipo(Linea.TipoLinea.TAX);
        this.nuevoItemImpuesto.setComprobante((Comprobante)this.facturaSeleccionada);
        this.nuevoItemImpuesto.setImpuesto(new Impuesto());
    }
    
    public String getCantidadBytes(final long n) {
        return Mensajes.humanReadableByteCount(n);
    }
    
    public boolean validarAdjuntos() {
        boolean b = true;
        if (this.getAdjuntos().size() == 0) {
            Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, "El apartado de Adjuntos es obligatorio, debe ingresar un adjunto para poder enviar la factura");
            b = false;
        }
        return b;
    }
    
    private boolean importesValidos() throws ServiceException {
        double n = 0.0;
        final double doubleValue = this.facturaSeleccionada.getImporte();
        for (final Linea linea : this.listaLineas) {
            if (linea.getImporte() != null) {
                n += linea.getImporte();
            }
            else {
                AdministracionFacturaAction.log.warn(("Importe de l\u00ednea Id " + linea.getId() + " nulo. Se omite."));
            }
        }
        return Math.rint(doubleValue * 100.0) / 100.0 == Math.rint(n * 100.0) / 100.0;
    }
    
    public boolean validarImportes() {
        boolean b = true;
        try {
            if (!this.importesValidos()) {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, "El importe de la factura debe ser igual a la suma de los importes de los \u00edtems");
                b = false;
            }
        }
        catch (ServiceException ex) {
            final Iterator<String> iterator = (Iterator<String>)ex.getMessages().iterator();
            while (iterator.hasNext()) {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, (String)iterator.next());
            }
            b = false;
        }
        return b;
    }
    
    private boolean validarNuevoItemImpuesto() {
        boolean b = true;
        if (this.nuevoItemImpuesto.getDescripcion().isEmpty()) {
            Mensajes.displayClientFacesMessage("mensajesImpuestoDialog", FacesMessage.SEVERITY_ERROR, "El campo Descripci\u00f3n es obligatorio, debe ingresar un valor");
            b = false;
        }
        final HolderHelper holderHelper = new HolderHelper();
        if (!this.validaImporte(this.nuevoItemImpuesto.getImporte(), holderHelper)) {
            Mensajes.displayClientFacesMessage("mensajesImpuestoDialog", FacesMessage.SEVERITY_ERROR, (String)holderHelper.get());
            b = false;
        }
        return b;
    }
    
    private boolean validaImporte(final Double n, final HolderHelper<String> holderHelper) {
        if (n == null || n <= 0.0) {
            holderHelper.set("El importe debe ser mayor a 0");
            return false;
        }
        if (n != Math.rint(n * 100.0) / 100.0) {
            holderHelper.set("El importe no puede tener m\u00e1s de 2 decimales");
            return false;
        }
        return true;
    }
    
    public void onImpuestoImporteChanged(final Linea linea) {
        this.actualizarImporteTotalImpuesto();
    }
    
    private void actualizarImporteTotal() {
        double importeTotalLineas = 0.0;
        for (final Linea linea : this.listaLineas) {
            if (linea.getImporte() != null) {
                importeTotalLineas += linea.getImporte();
            }
        }
        this.importeTotalLineas = importeTotalLineas;
    }
    
    public void actualizarImporteTotalImpuesto() {
        double importeTotalImpuestos = 0.0;
        for (final Linea linea : this.listaImpuestos) {
            if (linea.getImporte() != null) {
                importeTotalImpuestos += linea.getImporte();
            }
        }
        this.importeTotalImpuestos = importeTotalImpuestos;
    }
    
    public void onLineaImporteChanged(final Linea linea) {
        this.comprobanteService.cambiarCantidad(linea);
        this.actualizarImporteTotal();
    }
    
    public void onLineaCantidadChanged(final Linea linea) {
        this.comprobanteService.cambiarImporte(linea);
        this.actualizarImporteTotal();
    }
    
    public void abrirPopUpAdjuntos() {
        this.setNuevoAdjunto(new ComprobanteAdjunto());
    }
    
    public void agregarAdjunto(final FileUploadEvent fileUploadEvent) {
        try {
            if (fileUploadEvent.getFile().getSize() / 1024L < 5120L) {
                final byte[] data = new byte[(int)fileUploadEvent.getFile().getSize()];
                fileUploadEvent.getFile().getInputstream().read(data);
                (this.nuevoAdjunto = new ComprobanteAdjunto()).setHabilitado(true);
                this.nuevoAdjunto.setComprobante((Comprobante)this.facturaSeleccionada);
                this.nuevoAdjunto.setNombre(fileUploadEvent.getFile().getFileName());
                this.nuevoAdjunto.setSize(fileUploadEvent.getFile().getSize());
                this.nuevoAdjunto.setContentType(fileUploadEvent.getFile().getContentType());
                this.nuevoAdjunto.setData(data);
                this.nuevoAdjunto.setDescripcion("");
                fileUploadEvent.getFile().getInputstream().close();
                this.comprobanteAdjuntoService.agregarAdjunto(this.nuevoAdjunto);
                this.adjuntos = null;
                FacesContext.getCurrentInstance().addMessage("mensajesAdjunto", new FacesMessage(FacesMessage.SEVERITY_INFO, "El archivo adjunto fue agregado correctamente.", ""));
            }
            else {
                FacesContext.getCurrentInstance().addMessage("mensajesAdjunto", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Tama\u00f1o m\u00e1ximo permitido 5MB.", ""));
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
            FacesContext.getCurrentInstance().addMessage("mensajesAdjunto", new FacesMessage(FacesMessage.SEVERITY_ERROR, ErrorMessage.ERROR_ADJUNTO.getMessage(), ""));
        }
        catch (ServiceException ex2) {
            final Iterator<String> iterator = ex2.getMessages().iterator();
            while (iterator.hasNext()) {
                FacesContext.getCurrentInstance().addMessage("mensajesAdjunto", new FacesMessage(FacesMessage.SEVERITY_ERROR, (String)iterator.next(), ""));
            }
        }
    }
    
    public void descargarAdjunto(final ComprobanteAdjunto comprobanteAdjunto) {
        try {
            final ComprobanteAdjunto adjunto = this.comprobanteAdjuntoService.getAdjunto(Long.valueOf(comprobanteAdjunto.getId()));
            final HttpServletResponse httpServletResponse = (HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
            final byte[] data = adjunto.getData();
            httpServletResponse.setContentType(adjunto.getContentType());
            httpServletResponse.setContentLength(data.length);
            httpServletResponse.setContentType(adjunto.getContentType());
            httpServletResponse.setHeader("Content-Disposition", "attachment; filename=\"" + adjunto.getNombre() + "\"");
            final ServletOutputStream outputStream = httpServletResponse.getOutputStream();
            outputStream.write(data, 0, data.length);
            outputStream.flush();
            outputStream.close();
        }
        catch (Exception ex) {}
        finally {
            FacesContext.getCurrentInstance().responseComplete();
        }
    }
    
    public void eliminarAdjunto(final ComprobanteAdjunto comprobanteAdjunto) {
        try {
            this.comprobanteAdjuntoService.eliminarAdjunto(comprobanteAdjunto);
            this.adjuntos = null;
            FacesContext.getCurrentInstance().addMessage("mensajesAdjunto", new FacesMessage(FacesMessage.SEVERITY_INFO, "El archivo adjunto fue eliminado correctamente.", ""));
        }
        catch (ServiceException ex) {
            final Iterator<String> iterator = ex.getMessages().iterator();
            while (iterator.hasNext()) {
                FacesContext.getCurrentInstance().addMessage("mensajesAdjunto", new FacesMessage(FacesMessage.SEVERITY_ERROR, (String)iterator.next(), ""));
            }
        }
    }
    
    public boolean canShowEstadoMotivo() {
        if (this.facturaSeleccionada.getEstado() == null) {
            return false;
        }
        switch (this.facturaSeleccionada.getEstado().ordinal()) {
            case 1: {
                return true;
            }
            case 2:
            case 3: {
                return this.facturaSeleccionada.getEstadoEbs() != null && this.perfilUsuario != null && this.perfilUsuario != Perfil.PROVEEDOR;
            }
            default: {
                return false;
            }
        }
    }
    
    public Vista getVistaFactura() {
        return this.vistaFactura;
    }
    
    public void setVistaFactura(final Vista vistaFactura) {
        this.vistaFactura = vistaFactura;
    }
    
    public Perfil getPerfilUsuario() {
        return this.perfilUsuario;
    }
    
    public void setPerfilUsuario(final Perfil perfilUsuario) {
        this.perfilUsuario = perfilUsuario;
    }
    
    public List<SelectItem> getComboCodigoAutorizacion() {
        return (List<SelectItem>)this.comboCodigoAutorizacion;
    }
    
    public void setComboCodigoAutorizacion(final List<SelectItem> comboCodigoAutorizacion) {
        this.comboCodigoAutorizacion = comboCodigoAutorizacion;
    }
    
    public List<Letra> getComboLetras() {
        return (List<Letra>)this.comboLetras;
    }
    
    public void setComboLetras(final List<Letra> comboLetras) {
        this.comboLetras = comboLetras;
    }
    
    public Vista getEdicionFactura() {
        return this.edicionFactura;
    }
    
    public void setEdicionFactura(final Vista edicionFactura) {
        this.edicionFactura = edicionFactura;
    }
    
    public Vista getAltaFac() {
        return this.altaFac;
    }
    
    public void setAltaFac(final Vista altaFac) {
        this.altaFac = altaFac;
    }
    
    public ProveedorService getProveedorService() {
        return this.proveedorService;
    }
    
    public void setProveedorService(final ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }
    
    public Factura getFacturaSeleccionada() {
        return this.facturaSeleccionada;
    }
    
    public void setFacturaSeleccionada(final Factura facturaSeleccionada) {
        this.facturaSeleccionada = facturaSeleccionada;
    }
    
    public List<String> getProvinciasPrestoServicio() {
        return (List<String>)this.provinciasPrestoServicio;
    }
    
    public void setProvinciasPrestoServicio(final List<String> provinciasPrestoServicio) {
        this.provinciasPrestoServicio = provinciasPrestoServicio;
    }
    
    public Proveedor getProveedor() {
        return this.proveedor;
    }
    
    public void setProveedor(final Proveedor proveedor) {
        this.proveedor = proveedor;
    }
    
    public List<IIBBMultilateral> getListaIIBB() {
        return (List<IIBBMultilateral>)this.listaIIBB;
    }
    
    public void setListaIIBB(final List<IIBBMultilateral> listaIIBB) {
        this.listaIIBB = listaIIBB;
    }
    
    public String getProvinciaPrestoServicio() {
        return this.provinciaPrestoServicio;
    }
    
    public void setProvinciaPrestoServicio(final String provinciaPrestoServicio) {
        this.provinciaPrestoServicio = provinciaPrestoServicio;
    }
    
    public List<Linea> getListaLineas() {
        return (List<Linea>)this.listaLineas;
    }
    
    public void setListaLineas(final List<Linea> listaLineas) {
        this.listaLineas = listaLineas;
    }
    
    public List<LineaOrdenCompra> getListaLineasOC() {
        return (List<LineaOrdenCompra>)this.listaLineasOC;
    }
    
    public void setListaLineasOC(final List<LineaOrdenCompra> listaLineasOC) {
        this.listaLineasOC = listaLineasOC;
    }
    
    public List<Linea> getLineasFacturaEliminar() {
        return (List<Linea>)this.lineasFacturaEliminar;
    }
    
    public void setLineasFacturaEliminar(final List<Linea> lineasFacturaEliminar) {
        this.lineasFacturaEliminar = lineasFacturaEliminar;
    }
    
    public List<Linea> getLineasImpuestoEliminar() {
        return (List<Linea>)this.lineasImpuestoEliminar;
    }
    
    public void setLineasImpuestoEliminar(final List<Linea> lineasImpuestoEliminar) {
        this.lineasImpuestoEliminar = lineasImpuestoEliminar;
    }
    
    public double getImporteTotalLineas() {
        return this.importeTotalLineas;
    }
    
    public void setImporteTotalLineas(final double importeTotalLineas) {
        this.importeTotalLineas = importeTotalLineas;
    }
    
    public double getImporteTotalImpuestos() {
        return this.importeTotalImpuestos;
    }
    
    public void setImporteTotalImpuestos(final double importeTotalImpuestos) {
        this.importeTotalImpuestos = importeTotalImpuestos;
    }
    
    public String getNombreImpSeleccionado() {
        return this.nombreImpSeleccionado;
    }
    
    public void setNombreImpSeleccionado(final String nombreImpSeleccionado) {
        this.nombreImpSeleccionado = nombreImpSeleccionado;
    }
    
    public List<Linea> getLineasDisponibles() {
        return (List<Linea>)this.lineasDisponibles;
    }
    
    public void setLineasDisponibles(final List<Linea> lineasDisponibles) {
        this.lineasDisponibles = lineasDisponibles;
    }
    
    public Map<Linea, Boolean> getLineasDisponiblesSeleccionadas() {
        return (Map<Linea, Boolean>)this.lineasDisponiblesSeleccionadas;
    }
    
    public void setLineasDisponiblesSeleccionadas(final Map<Linea, Boolean> lineasDisponiblesSeleccionadas) {
        this.lineasDisponiblesSeleccionadas = lineasDisponiblesSeleccionadas;
    }
    
    public OrdenCompra getOrdenCompraSeleccionada() {
        return this.ordenCompraSeleccionada;
    }
    
    public void setOrdenCompraSeleccionada(final OrdenCompra ordenCompraSeleccionada) {
        this.ordenCompraSeleccionada = ordenCompraSeleccionada;
    }
    
    public LazyDataModel<OrdenCompra> getOrdenesCompraAbiertas() {
        return (LazyDataModel<OrdenCompra>)this.ordenesCompraAbiertas;
    }
    
    public void setOrdenesCompraAbiertas(final LazyDataModel<OrdenCompra> ordenesCompraAbiertas) {
        this.ordenesCompraAbiertas = ordenesCompraAbiertas;
    }
    
    public Linea getNuevoItemImpuesto() {
        return this.nuevoItemImpuesto;
    }
    
    public void setNuevoItemImpuesto(final Linea nuevoItemImpuesto) {
        this.nuevoItemImpuesto = nuevoItemImpuesto;
    }
    
    public List<Compania> getCompaniasAdecco() {
        return (List<Compania>)this.companiasAdecco;
    }
    
    public void setCompaniasAdecco(final List<Compania> companiasAdecco) {
        this.companiasAdecco = companiasAdecco;
    }
    
    public List<ComprobanteAdjunto> getAdjuntos() {
        if (this.adjuntos == null && this.facturaSeleccionada != null) {
            try {
                if (this.facturaSeleccionada.getId() > 0L) {
                    this.adjuntos = this.comprobanteAdjuntoService.getAdjuntos((Comprobante)this.facturaSeleccionada);
                }
            }
            catch (ServiceException ex) {
                ex.printStackTrace();
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, "Error al obtener la lista de adjuntos");
            }
        }
        return (List<ComprobanteAdjunto>)this.adjuntos;
    }
    
    public void setAdjuntos(final List<ComprobanteAdjunto> adjuntos) {
        this.adjuntos = adjuntos;
    }
    
    public ComprobanteAdjunto getNuevoAdjunto() {
        return this.nuevoAdjunto;
    }
    
    public void setNuevoAdjunto(final ComprobanteAdjunto nuevoAdjunto) {
        this.nuevoAdjunto = nuevoAdjunto;
    }
    
    public List<Linea> getListaImpuestos() {
        return (List<Linea>)this.listaImpuestos;
    }
    
    public void setListaImpuestos(final List<Linea> listaImpuestos) {
        this.listaImpuestos = listaImpuestos;
    }
    
    public List<Impuesto> getTipoImpuestos() {
        return (List<Impuesto>)this.tipoImpuestos;
    }
    
    public void setTipoImpuestos(final List<Impuesto> tipoImpuestos) {
        this.tipoImpuestos = tipoImpuestos;
    }
    
    public Perfil getPerfilProveedor() {
        return this.perfilProveedor;
    }
    
    public void setPerfilProveedor(final Perfil perfilProveedor) {
        this.perfilProveedor = perfilProveedor;
    }
    
    public boolean isAbiertaConPendientes(final OrdenCompra ordenCompra) {
        if (!"Y".equals(ordenCompra.getAprobado())) {
            return false;
        }
        final ImportesBean importesBean = this.importesPorOCId.get(ordenCompra.getId());
        return importesBean != null && importesBean.getSaldoRestante() > 0.0;
    }
    
    public ImportesBean getImportesBean(final OrdenCompra ordenCompra) {
        return this.importesPorOCId.get(ordenCompra.getId());
    }
    
    public DataTable getOrdenesCompraAbiertasDataTable() {
        return this.ordenesCompraAbiertasDataTable;
    }
    
    public void setOrdenesCompraAbiertasDataTable(final DataTable ordenesCompraAbiertasDataTable) {
        this.ordenesCompraAbiertasDataTable = ordenesCompraAbiertasDataTable;
    }
    
    public boolean isOperacionDeImpuestoDialog() {
        return this.operacionDeImpuestoDialog;
    }
    
    public boolean setOperacionDeImpuestoDialogFalse() {
        return this.operacionDeImpuestoDialog = false;
    }
    
    static {
        log = Logger.getLogger(AdministracionFacturaAction.class);
    }
}
