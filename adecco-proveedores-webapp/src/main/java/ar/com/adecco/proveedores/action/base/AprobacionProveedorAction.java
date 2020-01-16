// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.action.base;

import ar.com.syntagma.helpers.HolderHelper;
import java.util.Iterator;
import ar.com.adecco.proveedores.daos.exception.ServiceException;
import ar.com.syntagma.uiutils.Mensajes;
import javax.faces.application.FacesMessage;
import java.util.ArrayList;
import ar.com.adecco.proveedores.model.EstadoProveedor;
import ar.com.adecco.proveedores.model.Vista;
import ar.com.adecco.proveedores.model.Proveedor;
import java.util.List;
import ar.com.adecco.proveedores.service.ProveedorService;
import javax.inject.Inject;
import ar.com.adecco.proveedores.action.PanelProveedorAction;
import java.io.Serializable;

public abstract class AprobacionProveedorAction implements Serializable
{
    private static final long serialVersionUID = -786793629429454966L;
    @Inject
    protected PanelProveedorAction panelProveedorAction;
    @Inject
    protected ProveedorService proveedorService;
    protected List<Proveedor> proveedores;
    protected Vista vista;
    protected String razonSocial;
    protected String cuit;
    protected EstadoProveedor estado;
    protected List<EstadoProveedor> estados;
    
    public void initialize() {
        this.razonSocial = null;
        this.cuit = null;
        this.estado = EstadoProveedor.ENVIADO;
        (this.estados = new ArrayList<EstadoProveedor>()).add(EstadoProveedor.RECHAZADO);
        this.estados.add(EstadoProveedor.INGRESADO);
        this.estados.add(EstadoProveedor.ENVIADO);
        this.estados.add(EstadoProveedor.APROBADO);
        this.estados.add(EstadoProveedor.ERROR_APROBACION);
        this.proveedores = null;
        this.setVista(Vista.PROVEEDOR_LISTADO);
    }
    
    public void buscar() {
        this.proveedores = new ArrayList<Proveedor>();
        try {
            this.proveedores = (List<Proveedor>)this.proveedorService.buscarProveedores(this.razonSocial, this.cuit, this.estado);
        }
        catch (ServiceException ex) {
            final Iterator<String> iterator = ex.getMessages().iterator();
            while (iterator.hasNext()) {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, (String)iterator.next());
            }
        }
    }
    
    public void loadProveedor(final Proveedor proveedor) {
        this.panelProveedorAction.loadProveedor(proveedor);
        this.setVista(Vista.PROVEEDOR_DETALLE);
    }
    
    public void back() {
        this.buscar();
        this.setVista(Vista.PROVEEDOR_LISTADO);
    }
    
    public void aprobar() {
        final HolderHelper holderHelper = new HolderHelper();
        this.doAprobar((HolderHelper<Boolean>)holderHelper);
        if ((boolean) holderHelper.get()) {
            this.back();
        }
    }
    
    protected abstract void doAprobar(final HolderHelper<Boolean> p0);
    
    public void rechazar() {
        final HolderHelper holderHelper = new HolderHelper();
        this.doRechazar((HolderHelper<Boolean>)holderHelper);
        if ((boolean) holderHelper.get()) {
            this.back();
        }
    }
    
    protected abstract void doRechazar(final HolderHelper<Boolean> p0);
    
    public Vista getVista() {
        return this.vista;
    }
    
    public void setVista(final Vista vista) {
        this.vista = vista;
    }
    
    public boolean vistaActual(final Vista vista) {
        return this.vista == vista;
    }
    
    public boolean vistaActual(final String s) {
        return this.vista == Vista.valueOf(s);
    }
    
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
    
    public EstadoProveedor getEstado() {
        return this.estado;
    }
    
    public void setEstado(final EstadoProveedor estado) {
        this.estado = estado;
    }
    
    public List<EstadoProveedor> getEstados() {
        return this.estados;
    }
    
    public void setEstados(final List<EstadoProveedor> estados) {
        this.estados = estados;
    }
    
    public List<Proveedor> getProveedores() {
        return this.proveedores;
    }
    
    public void setProveedores(final List<Proveedor> proveedores) {
        this.proveedores = proveedores;
    }
}
