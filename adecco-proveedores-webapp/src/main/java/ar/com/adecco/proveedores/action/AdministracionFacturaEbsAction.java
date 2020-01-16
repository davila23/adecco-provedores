// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.action;

import java.util.Date;
import ar.com.adecco.proveedores.model.EstadoFactura;
import ar.com.adecco.proveedores.model.FacturaEstado;
import javax.servlet.ServletOutputStream;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import ar.com.adecco.proveedores.model.Comprobante;
import java.util.Collection;
import java.util.Iterator;
import ar.com.adecco.proveedores.daos.exception.ServiceException;
import ar.com.syntagma.uiutils.Mensajes;
import javax.faces.application.FacesMessage;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import ar.com.adecco.proveedores.model.Letra;
import ar.com.adecco.proveedores.model.Perfil;
import ar.com.adecco.proveedores.model.Vista;
import ar.com.adecco.proveedores.model.ComprobanteAdjunto;
import ar.com.adecco.dominio.ebs.hr.Compania;
import ar.com.adecco.dominio.ebs.ap.Impuesto;
import ar.com.adecco.dominio.ebs.po.DistribucionOrdenCompra;
import ar.com.adecco.dominio.ebs.ap.DistribucionFactura;
import ar.com.adecco.proveedores.model.IIBBMultilateral;
import java.util.List;
import ar.com.adecco.proveedores.model.Proveedor;
import ar.com.adecco.dominio.ebs.ap.Factura;
import ar.com.adecco.dominio.ebs.po.OrdenCompra;
import ar.com.adecco.proveedores.service.FacturaEstadoService;
import ar.com.adecco.proveedores.service.ComprobanteAdjuntoService;
import ar.com.adecco.proveedores.service.CompaniaService;
import ar.com.adecco.proveedores.service.ImpuestoService;
import ar.com.adecco.proveedores.service.LineaOCService;
import ar.com.adecco.proveedores.service.ProvinciaService;
import ar.com.adecco.proveedores.service.IIBBMultilateralService;
import ar.com.adecco.proveedores.service.ProveedorService;
import ar.com.adecco.proveedores.service.FacturaService;
import ar.com.adecco.proveedores.service.FacturaEbsService;
import javax.inject.Inject;
import ar.com.adecco.proveedores.service.OrdenCompraService;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class AdministracionFacturaEbsAction implements Serializable
{
    private static final long serialVersionUID = 5414229127514879083L;
    @Inject
    private OrdenCompraService ordenCompraService;
    @Inject
    private FacturaEbsService facturaEbsService;
    @Inject
    private FacturaService facturaService;
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
    private FacturaEstadoService facturaEstadoService;
    private OrdenCompra ordenCompraSeleccionada;
    private Factura facturaSeleccionada;
    private ar.com.adecco.proveedores.model.Factura facturaProveedores;
    private Proveedor proveedor;
    private List<String> provinciasPrestoServicio;
    private List<IIBBMultilateral> listaIIBB;
    private List<DistribucionFactura> listaLineas;
    private List<DistribucionFactura> listaImpuestos;
    private List<DistribucionOrdenCompra> listaLineasOC;
    private List<Impuesto> tipoImpuestos;
    private List<Compania> companiasAdecco;
    private List<ComprobanteAdjunto> adjuntos;
    private Vista vistaFactura;
    private Vista altaFac;
    private Vista edicionFactura;
    private Perfil perfilUsuario;
    private Perfil perfilProveedor;
    private List<Letra> comboLetras;
    private double importeTotalLineas;
    private double importeTotalImpuestos;
    private String nombreImpSeleccionado;
    private static final String tipoImpuestoIVA = "IVA";
    private static final String tipoImpuestoPercIIBB = "PERCIIBB";
    private static final String tipoImpuestoII = "OTHPERC";
    
    public AdministracionFacturaEbsAction() {
        this.altaFac = Vista.ALTA_FAC;
        this.edicionFactura = Vista.EDICION_FAC;
        this.perfilProveedor = Perfil.PROVEEDOR;
        this.importeTotalLineas = 0.0;
        this.importeTotalImpuestos = 0.0;
    }
    
    @PostConstruct
    public void init() {
        this.inicializarFormulario();
    }
    
    public void inicializarFormulario() {
        this.ordenCompraSeleccionada = null;
        this.facturaSeleccionada = null;
        this.proveedor = null;
        this.nombreImpSeleccionado = null;
        this.provinciasPrestoServicio = new ArrayList<String>();
        this.listaIIBB = null;
        this.listaLineas = new ArrayList<DistribucionFactura>();
        this.listaLineasOC = new ArrayList<DistribucionOrdenCompra>();
        this.listaImpuestos = new ArrayList<DistribucionFactura>();
        try {
            this.companiasAdecco = (List<Compania>)this.companiaService.getCompaniasAdecco();
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
        (this.comboLetras = new ArrayList<Letra>()).add(Letra.A);
        this.comboLetras.add(Letra.B);
        this.comboLetras.add(Letra.C);
        this.comboLetras.add(Letra.E);
        this.comboLetras.add(Letra.M);
    }
    
    public void administracionFactura(final Perfil perfilUsuario, final long n) {
        this.setPerfilUsuario(perfilUsuario);
        this.setVistaFactura(Vista.EDICION_FAC);
        System.out.println("administracionFactura: Id factura: " + n);
        try {
            this.obtenerFacturaSeleccionada(n);
            this.obtenerFacturaProveedores();
            this.obtenerItemsFactura();
            this.obtenerItemsImpuesto();
            this.obtenerListaImpuestos();
        }
        catch (ServiceException ex) {
            final Iterator<String> iterator = ex.getMessages().iterator();
            while (iterator.hasNext()) {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, (String)iterator.next());
            }
        }
    }
    
    public void obtenerListaImpuestos() throws ServiceException {
        this.tipoImpuestos = new ArrayList<Impuesto>();
        if (this.facturaSeleccionada != null) {
            this.tipoImpuestos.addAll(this.impuestoService.getImpuestosByType(this.facturaSeleccionada.getCompania(), "IVA"));
            this.tipoImpuestos.addAll(this.impuestoService.getImpuestosByType(this.facturaSeleccionada.getCompania(), "PERCIIBB"));
            this.tipoImpuestos.addAll(this.impuestoService.getImpuestosByType(this.facturaSeleccionada.getCompania(), "OTHPERC"));
        }
    }
    
    public void obtenerFacturaSeleccionada(final long n) throws ServiceException {
        this.facturaSeleccionada = this.facturaEbsService.getById(n, true);
    }
    
    public void obtenerFacturaProveedores() throws ServiceException {
        this.facturaProveedores = ((this.facturaSeleccionada != null) ? this.facturaService.getEntByFacturaEbs(this.facturaSeleccionada) : null);
    }
    
    public List<ComprobanteAdjunto> getAdjuntos() {
        if (this.adjuntos == null) {
            try {
                this.adjuntos = (List<ComprobanteAdjunto>)this.comprobanteAdjuntoService.getAdjuntos((Comprobante)this.facturaProveedores);
            }
            catch (ServiceException ex) {
                ex.printStackTrace();
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, "Error al obtener la lista de adjuntos");
            }
        }
        return this.adjuntos;
    }
    
    public void setAdjuntos(final List<ComprobanteAdjunto> adjuntos) {
        this.adjuntos = adjuntos;
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
    
    public FacturaEstado getEstadoActual() {
        try {
            return this.facturaEstadoService.getEstadoActual((Comprobante)this.facturaProveedores);
        }
        catch (ServiceException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public boolean isFacturaRechazada() {
        return EstadoFactura.RECHAZADA.equals((Object)this.facturaProveedores.getEstado());
    }
    
    public String getCantidadBytes(final long n) {
        return Mensajes.humanReadableByteCount(n);
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
        this.listaLineas = new ArrayList<DistribucionFactura>();
        this.listaLineasOC = new ArrayList<DistribucionOrdenCompra>();
        this.setImporteTotalLineas(0.0);
        for (final DistribucionFactura distribucionFactura : this.facturaSeleccionada.getDistribuciones()) {
            if (distribucionFactura.getDistribucionOrdenCompra() != null) {
                distribucionFactura.getDistribucionOrdenCompra().getLineaOrdenCompra();
            }
            if (distribucionFactura.getTipo().equals("ITEM")) {
                this.listaLineas.add(distribucionFactura);
                this.listaLineasOC.add(distribucionFactura.getDistribucionOrdenCompra());
                this.setImporteTotalLineas(this.getImporteTotalLineas() + distribucionFactura.getImporte());
            }
        }
    }
    
    public void obtenerItemsImpuesto() {
        this.listaImpuestos = new ArrayList<DistribucionFactura>();
        this.setImporteTotalImpuestos(0.0);
        for (final DistribucionFactura distribucionFactura : this.facturaSeleccionada.getDistribuciones()) {
            if (distribucionFactura.getTipo().equals("TAX")) {
                this.listaImpuestos.add(distribucionFactura);
                this.setImporteTotalImpuestos(this.getImporteTotalImpuestos() + distribucionFactura.getImporte());
            }
        }
    }
    
    public boolean proveedorHabilitado() {
        return new Date().before(this.proveedor.getFechaVencimiento());
    }
    
    public boolean isExisteEnProveedores() {
        return this.facturaProveedores != null;
    }
    
    public String formatAdeccoEbsDate(final String s) {
        final String formatAdeccoEbsDate = Mensajes.formatAdeccoEbsDate(s);
        if (formatAdeccoEbsDate == null) {
            System.out.println("No se pudo parsear la fecha \"" + s + "\"");
            return s;
        }
        return formatAdeccoEbsDate;
    }
    
    public OrdenCompraService getOrdenCompraService() {
        return this.ordenCompraService;
    }
    
    public void setOrdenCompraService(final OrdenCompraService ordenCompraService) {
        this.ordenCompraService = ordenCompraService;
    }
    
    public FacturaEbsService getFacturaEbsService() {
        return this.facturaEbsService;
    }
    
    public void setFacturaEbsService(final FacturaEbsService facturaEbsService) {
        this.facturaEbsService = facturaEbsService;
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
    
    public List<Letra> getComboLetras() {
        return this.comboLetras;
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
    
    public ar.com.adecco.proveedores.model.Factura getFacturaProveedores() {
        return this.facturaProveedores;
    }
    
    public void setFacturaProveedores(final ar.com.adecco.proveedores.model.Factura facturaProveedores) {
        this.facturaProveedores = facturaProveedores;
    }
    
    public List<String> getProvinciasPrestoServicio() {
        return this.provinciasPrestoServicio;
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
        return this.listaIIBB;
    }
    
    public void setListaIIBB(final List<IIBBMultilateral> listaIIBB) {
        this.listaIIBB = listaIIBB;
    }
    
    public List<DistribucionFactura> getListaLineas() {
        return this.listaLineas;
    }
    
    public void setListaLineas(final List<DistribucionFactura> listaLineas) {
        this.listaLineas = listaLineas;
    }
    
    public List<DistribucionOrdenCompra> getListaLineasOC() {
        return this.listaLineasOC;
    }
    
    public void setListaLineasOC(final List<DistribucionOrdenCompra> listaLineasOC) {
        this.listaLineasOC = listaLineasOC;
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
    
    public OrdenCompra getOrdenCompraSeleccionada() {
        return this.ordenCompraSeleccionada;
    }
    
    public void setOrdenCompraSeleccionada(final OrdenCompra ordenCompraSeleccionada) {
        this.ordenCompraSeleccionada = ordenCompraSeleccionada;
    }
    
    public List<Compania> getCompaniasAdecco() {
        return this.companiasAdecco;
    }
    
    public void setCompaniasAdecco(final List<Compania> companiasAdecco) {
        this.companiasAdecco = companiasAdecco;
    }
    
    public List<DistribucionFactura> getListaImpuestos() {
        return this.listaImpuestos;
    }
    
    public void setListaImpuestos(final List<DistribucionFactura> listaImpuestos) {
        this.listaImpuestos = listaImpuestos;
    }
    
    public List<Impuesto> getTipoImpuestos() {
        return this.tipoImpuestos;
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
}
