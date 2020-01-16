// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.action;

import javax.faces.context.FacesContext;
import java.util.Iterator;
import java.util.Map;
import ar.com.adecco.proveedores.daos.exception.ServiceException;
import javax.faces.application.FacesMessage;
import java.util.Arrays;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import ar.com.adecco.proveedores.bean.ImportesBean;
import ar.com.adecco.proveedores.model.Factura;
import ar.com.adecco.dominio.ebs.po.LineaOrdenCompra;
import java.util.List;
import ar.com.adecco.proveedores.model.Proveedor;
import ar.com.adecco.dominio.ebs.po.OrdenCompra;
import ar.com.adecco.proveedores.service.ProveedorService;
import ar.com.adecco.proveedores.service.OrdenCompraService;
import javax.inject.Inject;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class DetalleOCAction implements Serializable
{
    private static final long serialVersionUID = -6292414460556975673L;
    @Inject
    private AdministracionFacturaAction administracionFacturaAction;
    @Inject
    private PanelControlAction panelControlAction;
    @Inject
    private OrdenCompraService ordenCompraService;
    @Inject
    private ProveedorService proveedorService;
    private OrdenCompra ordenCompraSeleccionada;
    private Proveedor proveedor;
    private List<LineaOrdenCompra> lineasOC;
    private List<Factura> facturasAsociadas;
    private ImportesBean importes;
    
    @PostConstruct
    public void init() {
    }
    
    public void initialize(final Proveedor proveedor) {
        this.proveedor = proveedor;
        this.ordenCompraSeleccionada = null;
        this.lineasOC = new ArrayList<LineaOrdenCompra>();
        this.facturasAsociadas = new ArrayList<Factura>();
    }
    
    public void administracionOC(final long n) {
        try {
            this.ordenCompraSeleccionada = this.ordenCompraService.getById(n, true);
            this.lineasOC = (List<LineaOrdenCompra>)this.ordenCompraService.cargarLineasOC(this.ordenCompraSeleccionada);
            this.facturasAsociadas = (List<Factura>)this.ordenCompraService.cargarFacturasAsociadas(this.ordenCompraSeleccionada);
            final Map<Long, ImportesBean> obtenerImportesPorOC = this.ordenCompraService.obtenerImportesPorOC((List<OrdenCompra>)Arrays.asList(this.ordenCompraSeleccionada));
            this.importes = ((obtenerImportesPorOC != null) ? obtenerImportesPorOC.get(this.ordenCompraSeleccionada.getId()) : null);
        }
        catch (ServiceException ex) {
            final Iterator<String> iterator = ex.getMessages().iterator();
            while (iterator.hasNext()) {
                this.addMessage(iterator.next(), FacesMessage.SEVERITY_ERROR);
            }
        }
    }
    
    public void asociarFacturaOC(final long n) {
        this.panelControlAction.showNewFactura(n);
    }
    
    public void volver() {
        this.panelControlAction.showPanelControl();
    }
    
    public boolean proveedorHabilitado() {
        return this.proveedorService.proveedorHabilitado(this.proveedor);
    }
    
    public void addMessage(final String s, final FacesMessage.Severity severity) {
        final FacesMessage facesMessage = new FacesMessage();
        facesMessage.setDetail(s);
        facesMessage.setSummary(s);
        facesMessage.setSeverity(severity);
        FacesContext.getCurrentInstance().addMessage((String)null, facesMessage);
    }
    
    public OrdenCompra getOrdenCompraSeleccionada() {
        return this.ordenCompraSeleccionada;
    }
    
    public void setOrdenCompraSeleccionada(final OrdenCompra ordenCompraSeleccionada) {
        this.ordenCompraSeleccionada = ordenCompraSeleccionada;
    }
    
    public List<LineaOrdenCompra> getLineasOC() {
        return this.lineasOC;
    }
    
    public void setLineasOC(final List<LineaOrdenCompra> lineasOC) {
        this.lineasOC = lineasOC;
    }
    
    public List<Factura> getFacturasAsociadas() {
        return this.facturasAsociadas;
    }
    
    public void setFacturasAsociadas(final List<Factura> facturasAsociadas) {
        this.facturasAsociadas = facturasAsociadas;
    }
    
    public ImportesBean getImportes() {
        return this.importes;
    }
    
    public void setImportes(final ImportesBean importes) {
        this.importes = importes;
    }
    
    public boolean isAbiertaConPendientes() {
        return "Y".equals(this.ordenCompraSeleccionada.getAprobado()) && this.importes != null && this.importes.getSaldoRestante() > 0.0;
    }
}
