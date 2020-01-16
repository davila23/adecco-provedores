// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.action;

import ar.com.adecco.dominio.persona.PersonaRelacion;
import ar.com.adecco.dominio.contacto.Modulo;
import ar.com.syntagma.helpers.HolderHelper;
import java.util.Iterator;
import ar.com.adecco.proveedores.daos.exception.ServiceException;
import ar.com.syntagma.uiutils.Mensajes;
import javax.faces.application.FacesMessage;
import ar.com.adecco.dominio.persona.PersonaRelacionProveedor;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import ar.com.adecco.proveedores.model.Perfil;
import ar.com.adecco.proveedores.model.Proveedor;
import java.util.List;
import ar.com.adecco.proveedores.model.Vista;
import java.util.EnumSet;
import ar.com.adecco.proveedores.service.ConfiguracionModuloService;
import ar.com.adecco.proveedores.service.PersonaRelacionProveedorService;
import ar.com.adecco.proveedores.service.ProveedorService;
import javax.inject.Inject;
import ar.com.syntagma.adecco.login.cliente.MenuBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class AdministracionProveedorAction implements Serializable
{
    private static final long serialVersionUID = 1015322455161937254L;
    @Inject
    private MenuBean menuBean;
    @Inject
    private PanelProveedorAction panelProveedorAction;
    @Inject
    private ProveedorService proveedorService;
    @Inject
    private PersonaRelacionProveedorService personaRelacionProveedorService;
    @Inject
    private ConfiguracionModuloService configuracionModuloService;
    protected EnumSet<Vista> vistaActual;
    private List<Proveedor> proveedores;
    private Proveedor proveedor;
    
    @PostConstruct
    public void init() {
        System.out.println("AdministracionProveedorAction.init() - inicio");
        this.panelProveedorAction.setPerfil(Perfil.PROVEEDOR);
        this.vistaActual = EnumSet.of(Vista.PROVEEDOR_LISTADO);
        this.loadProveedores();
        if (this.proveedores != null && this.proveedores.size() == 1) {
            this.proveedor = this.proveedores.get(0);
            this.proveedorChanged();
        }
    }
    
    public void loadProveedores() {
        this.proveedores = null;
        try {
            final List relacionProveedoresByUsuarioAdeccoId = this.personaRelacionProveedorService.getRelacionProveedoresByUsuarioAdeccoId(Long.valueOf(this.menuBean.getUsuario().getId()));
            System.out.println("usuariosRelacion.size(): " + relacionProveedoresByUsuarioAdeccoId.size());
            final ArrayList<Long> list = new ArrayList<Long>();
            final Iterator<PersonaRelacionProveedor> iterator = relacionProveedoresByUsuarioAdeccoId.iterator();
            while (iterator.hasNext()) {
                list.add(iterator.next().getProveedor().getId());
            }
            this.proveedores = (List<Proveedor>)this.proveedorService.listar((List)list);
        }
        catch (ServiceException ex) {
            final Iterator<String> iterator2 = ex.getMessages().iterator();
            while (iterator2.hasNext()) {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, (String)iterator2.next());
            }
        }
    }
    
    public void guardarProveedor() {
        final boolean proveedorNuevo = this.panelProveedorAction.isProveedorNuevo();
        final HolderHelper holderHelper = new HolderHelper();
        this.panelProveedorAction.guardarProveedor(holderHelper);
        if (proveedorNuevo && (boolean)holderHelper.get()) {
            this.loadProveedores();
        }
    }
    
    public void proveedorChanged() {
        if (this.proveedor == null) {
            return;
        }
        this.panelProveedorAction.loadProveedor(this.proveedor);
        this.vistaActual.add(Vista.PROVEEDOR_DETALLE);
        this.configuracionModuloService.updateLastAccess(Modulo.Codigo.PROVEEDORES, Long.valueOf(this.menuBean.getUsuario().getPersona().getId()), PersonaRelacion.TipoRelacion.PROVEEDOR, Long.valueOf(this.proveedor.getId()));
    }
    
    public void initAltaProveedor() {
        this.proveedor = null;
        this.panelProveedorAction.initAltaProveedor();
        this.vistaActual.add(Vista.PROVEEDOR_DETALLE);
    }
    
    public void enviarProveedor() {
        this.panelProveedorAction.enviarProveedor();
    }
    
    public boolean isVariosProveedores() {
        return this.proveedores != null && this.proveedores.size() > 1;
    }
    
    public boolean vistaActual(final Vista vista) {
        return this.vistaActual.contains(vista);
    }
    
    public boolean vistaActual(final String s) {
        return this.vistaActual.contains(Vista.valueOf(s));
    }
    
    public List<Proveedor> getProveedores() {
        return this.proveedores;
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
}
