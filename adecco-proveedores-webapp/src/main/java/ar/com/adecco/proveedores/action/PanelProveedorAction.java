// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.action;

import ar.com.adecco.proveedores.model.ProveedorEstado;
import javax.servlet.ServletOutputStream;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.context.RequestContext;
import ar.com.adecco.proveedores.daos.helpers.ErrorMessage;
import ar.com.adecco.proveedores.bean.MessageBean;
import ar.com.syntagma.helpers.HolderHelper;
import java.util.Collection;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import java.util.Iterator;
import ar.com.adecco.proveedores.daos.exception.ServiceException;
import ar.com.syntagma.uiutils.Mensajes;
import javax.faces.application.FacesMessage;
import org.primefaces.model.UploadedFile;
import ar.com.adecco.dominio.ebs.fnd.Usuario;
import ar.com.adecco.dominio.ebs.fnd.Moneda;
import ar.com.adecco.dominio.ebs.ap.Impuesto;
import ar.com.adecco.proveedores.model.TipoAdjunto;
import ar.com.adecco.proveedores.model.ProveedorAdjunto;
import ar.com.adecco.proveedores.model.IIBBMultilateral;
import ar.com.adecco.dominio.ebs.fnd.Provincia;
import java.util.List;
import ar.com.adecco.proveedores.model.Proveedor;
import ar.com.adecco.proveedores.model.Perfil;
import ar.com.adecco.proveedores.model.CondicionGanancias;
import ar.com.adecco.proveedores.model.CondicionIVA;
import ar.com.adecco.proveedores.service.PersonaRelacionProveedorService;
import ar.com.adecco.proveedores.service.ProveedorAdjuntoService;
import ar.com.adecco.proveedores.service.ImpuestoService;
import ar.com.adecco.proveedores.service.MonedaService;
import ar.com.adecco.proveedores.service.ProvinciaService;
import ar.com.adecco.proveedores.service.UsuarioService;
import ar.com.adecco.proveedores.service.ProveedorEstadoService;
import javax.inject.Inject;
import ar.com.adecco.proveedores.service.ProveedorService;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ViewScoped
public class PanelProveedorAction implements Serializable
{
    private static final long serialVersionUID = 2326871850422128287L;
    @Inject
    private ProveedorService proveedorService;
    @Inject
    private ProveedorEstadoService proveedorEstadoService;
    @Inject
    private UsuarioService usuarioService;
    @Inject
    private ProvinciaService provinciaService;
    @Inject
    private MonedaService monedaService;
    @Inject
    private ImpuestoService impuestoService;
    @Inject
    private ProveedorAdjuntoService proveedorAdjuntoService;
    @Inject
    private PersonaRelacionProveedorService usuarioAdeccoRelacionService;
    private CondicionIVA[] comboCondicionIVA;
    private CondicionGanancias[] comboImpuestoGanancias;
    private Perfil perfil;
    private boolean disabled;
    private boolean disabledExclusion;
    private boolean disabledExclusionLocal;
    private boolean disabledCompras;
    private Proveedor proveedor;
    private List<Provincia> provincias;
    private List<Provincia> provinciasDisponibles;
    private IIBBMultilateral nuevoIIBB;
    private ProveedorAdjunto nuevoAdjunto;
    private TipoAdjunto[] tipoAdjuntos;
    private List<Impuesto> actividadesPrincipales;
    private List<Moneda> monedas;
    private List<IIBBMultilateral> listadoIIBBEliminar;
    private List<ProveedorAdjunto> adjuntos;
    private Usuario usuarioEBS;
    private String motivoRechazo;
    private String errorValidacionCuit;
    private UploadedFile file;
    private static final String tipoRetencion = "RET_GAN";
    private static final long juegos = 1001L;
    private static final String defaultProvivinciaId = "01";
    private String ultimoPorcentajeExclusion;
    private String ultimoPorcentajeExclusionLocal;
    private String ultimoPorcentajeExclusionIIBB;
    private boolean entidadModificada;
    private boolean entidadGuardada;
    private boolean operacionDeAdjunto;
    private boolean operacionDeAdjuntoDialog;
    private boolean operacionDeMultilateralDialog;
    
    public PanelProveedorAction() {
        this.entidadModificada = false;
        this.entidadGuardada = false;
    }
    
    @PostConstruct
    public void init() {
        this.tipoAdjuntos = TipoAdjunto.values();
        this.comboCondicionIVA = new CondicionIVA[] { CondicionIVA.INSCRIPTO, CondicionIVA.MONOTRIBUTISTA, CondicionIVA.NO_RESPONSABLE, CondicionIVA.EXENTO };
        this.comboImpuestoGanancias = CondicionGanancias.values();
        try {
            this.monedas = this.monedaService.getMonedasActivas();
            this.provincias = this.provinciaService.getProvincias(true);
            this.actividadesPrincipales = this.impuestoService.getImpuestosByActividadPrincipal(1001L, "RET_GAN");
        }
        catch (ServiceException ex) {
            final Iterator<String> iterator = ex.getMessages().iterator();
            while (iterator.hasNext()) {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, (String)iterator.next());
            }
        }
    }
    
    public void inicializarFormulario() {
        this.clearNuevoIIBB();
        this.clearNuevoAdjunto();
        this.listadoIIBBEliminar = new ArrayList();
        this.adjuntos = null;
    }
    
    public void loadProveedor(final Proveedor proveedor) {
        try {
            this.inicializarFormulario();
            this.proveedor = this.proveedorService.getProveedorEagerLoadById(proveedor.getId());
            this.actualizarProvinciasDisponibles();
            this.setFlags();
        }
        catch (ServiceException ex) {
            Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    public void initAltaProveedor() {
        try {
            this.inicializarFormulario();
            (this.proveedor = new Proveedor()).setProveedorEstados((List)new ArrayList());
            this.proveedor.setIibbMultilaterales((List)new ArrayList());
            this.proveedor.setTerminoPago("30 d\u00edas DFRF");
            this.proveedor.setMoneda(this.monedaService.getMonedaById("ARS"));
            for (final Provincia provincia : this.provincias) {
                if (provincia.getId().equals("01")) {
                    this.proveedor.setProvincia(provincia);
                    this.cambioDeProvincia();
                    break;
                }
            }
            if (this.proveedor.getProvincia() == null) {
                this.actualizarProvinciasDisponibles();
            }
            this.setFlags();
        }
        catch (ServiceException ex) {
            final Iterator<String> iterator2 = ex.getMessages().iterator();
            while (iterator2.hasNext()) {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, (String)iterator2.next());
            }
            ex.printStackTrace();
        }
    }
    
    private void setFlags() {
        if (this.perfil == null) {
            this.perfil = Perfil.SIN_PERFIL;
        }
        switch (this.perfil.ordinal()) {
            case 1: {
                if (this.proveedorService.proveedorEditable(this.proveedor)) {
                    this.setDisabled(false);
                    this.evaluarExclusion();
                    this.evaluarExclusionLocal();
                    return;
                }
                this.setDisabled(true);
                this.setDisabledExclusion(true);
                this.setDisabledExclusionLocal(true);
                return;
            }
            case 2: {
                this.setDisabledCompras(!this.isProveedorEnviado());
                break;
            }
        }
        this.setDisabled(true);
        this.setDisabledExclusion(true);
        this.setDisabledExclusionLocal(true);
    }
    
    public boolean isProveedorNuevo() {
        return this.proveedor.getId() == 0L;
    }
    
    public boolean isProveedorIngresado() {
        return this.proveedorService.proveedorIngresado(this.proveedor);
    }
    
    public boolean isProveedorEnviado() {
        return this.proveedorService.proveedorEnviado(this.proveedor);
    }
    
    public boolean isProveedorAprobado() {
        return this.proveedorService.proveedorAprobado(this.proveedor);
    }
    
    public boolean isProveedorErrorAprobacion() {
        return this.proveedorService.proveedorErrorAprobacion(this.proveedor);
    }
    
    public boolean isProveedorRechazado() {
        return this.proveedorService.proveedorRechazado(this.proveedor);
    }
    
    public boolean isPuedeAprobarseProveedor() {
        return this.proveedorService.puedeAprobarseProveedor(this.proveedor);
    }
    
    public void cambioDeProvincia() {
        this.proveedor.setJurisdiccionLocal(this.proveedor.getProvincia());
        IIBBMultilateral iibbMultilateral = null;
        for (final IIBBMultilateral iibbMultilateral2 : this.proveedor.getIibbMultilaterales()) {
            if (iibbMultilateral2.getJurisdiccion().equals((Object)this.proveedor.getProvincia())) {
                iibbMultilateral = iibbMultilateral2;
            }
        }
        if (iibbMultilateral != null) {
            this.doEliminarIIBBMultilateral(iibbMultilateral);
        }
        this.actualizarProvinciasDisponibles();
    }
    
    public void eliminarIIBBMultilateral(final IIBBMultilateral iibbMultilateral) {
        System.out.println("eliminarIIBBMultilateral - Inicio");
        this.doEliminarIIBBMultilateral(iibbMultilateral);
        this.actualizarProvinciasDisponibles();
        System.out.println("eliminarIIBBMultilateral - Fin");
    }
    
    public void hola() {
        System.out.println("hola() - this: " + this);
    }
    
    public void hola2(final IIBBMultilateral iibbMultilateral) {
        System.out.println("hola2() - Inicio: " + iibbMultilateral);
    }
    
    private void doEliminarIIBBMultilateral(final IIBBMultilateral iibbMultilateral) {
        final boolean remove = this.proveedor.getIibbMultilaterales().remove(iibbMultilateral);
        if (iibbMultilateral.getId() != 0L) {
            this.listadoIIBBEliminar.add(iibbMultilateral);
        }
        if (remove) {
            this.entidadModificada = true;
        }
    }
    
    private void actualizarProvinciasDisponibles() {
        System.out.println("actualizarProvinciasDisponibles - Inicio");
        this.provinciasDisponibles = new ArrayList(this.provincias);
        System.out.println("provinciasDisponibles.size(): " + this.provinciasDisponibles.size());
        if (this.proveedor == null) {
            return;
        }
        if (this.proveedor.getProvincia() != null) {
            this.provinciasDisponibles.remove(this.proveedor.getProvincia());
        }
        System.out.println("provinciasDisponibles.size(): " + this.provinciasDisponibles.size());
        final Iterator<IIBBMultilateral> iterator = this.proveedor.getIibbMultilaterales().iterator();
        while (iterator.hasNext()) {
            this.provinciasDisponibles.remove(iterator.next().getJurisdiccion());
        }
        System.out.println("provinciasDisponibles.size(): " + this.provinciasDisponibles.size());
        System.out.println("actualizarProvinciasDisponibles - Fin");
    }
    
    public void impuestosGananciasChanged() {
        this.evaluarExclusion();
    }
    
    private void evaluarExclusion() {
        if (CondicionGanancias.EXCLUIDO.getDescripcion().equals(this.proveedor.getImpuestoGanancias())) {
            if (this.isDisabledExclusion()) {
                this.proveedor.setPorcentajeExclusion(this.ultimoPorcentajeExclusion);
                this.setDisabledExclusion(false);
            }
        }
        else if (!this.isDisabledExclusion()) {
            this.ultimoPorcentajeExclusion = this.proveedor.getPorcentajeExclusion();
            this.proveedor.setPorcentajeExclusion((String)null);
            this.setDisabledExclusion(true);
        }
    }
    
    public void changeExentoLocal() {
        this.getProveedor().setExentoLocal(!this.proveedor.isExentoLocal());
        this.exentoLocalChanged();
    }
    
    private void exentoLocalChanged() {
        this.evaluarExclusionLocal();
    }
    
    private void evaluarExclusionLocal() {
        if (this.proveedor.isExentoLocal()) {
            this.proveedor.setPorcentajeExclusionLocal(this.ultimoPorcentajeExclusionLocal);
            this.setDisabledExclusionLocal(false);
        }
        else {
            this.ultimoPorcentajeExclusionLocal = this.proveedor.getPorcentajeExclusionLocal();
            this.proveedor.setPorcentajeExclusionLocal((String)null);
            this.setDisabledExclusionLocal(true);
        }
    }
    
    public void changeExentoIIBB() {
        this.nuevoIIBB.setExento(!this.nuevoIIBB.isExento());
        this.exentoIIBBChanged();
    }
    
    private void exentoIIBBChanged() {
        this.evaluarExclusionIIBB();
    }
    
    private void evaluarExclusionIIBB() {
        if (this.nuevoIIBB.isExento()) {
            this.nuevoIIBB.setPorcentajeExclusion(this.ultimoPorcentajeExclusionIIBB);
        }
        else {
            this.ultimoPorcentajeExclusionIIBB = this.nuevoIIBB.getPorcentajeExclusion();
            this.nuevoIIBB.setPorcentajeExclusion((String)null);
        }
    }
    
    public void guardarProveedor() {
        this.guardarProveedor(new HolderHelper());
    }
    
    public void guardarProveedor(final HolderHelper<Boolean> holderHelper) {
        if (this.proveedor.getRazonSocial() == null || this.proveedor.getRazonSocial().isEmpty()) {
            Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, "El campo Raz\u00f3n Social o Nombre del propietario es obligatorio, debe ingresar un valor.");
            holderHelper.set(false);
            return;
        }
        try {
            final List<MessageBean> guardarProveedor = this.proveedorService.guardarProveedor(this.proveedor, this.listadoIIBBEliminar);
            for (final MessageBean messageBean : guardarProveedor) {
                Mensajes.displayFacesMessage(messageBean.getSeverity(), messageBean.getMessage());
            }
            if (guardarProveedor.isEmpty()) {
                this.listadoIIBBEliminar = new ArrayList();
            }
            this.setFlags();
        }
        catch (ServiceException ex) {
            final Iterator<String> iterator2 = ex.getMessages().iterator();
            while (iterator2.hasNext()) {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, (String)iterator2.next());
            }
            holderHelper.set(false);
            return;
        }
        Mensajes.displayFacesMessage(FacesMessage.SEVERITY_INFO, "Los datos del proveedor se guardaron correctamente.");
        holderHelper.set(true);
        this.entidadGuardada = true;
    }
    
    public void enviarProveedor() {
        try {
            final List<MessageBean> enviarProveedor = this.proveedorService.enviarProveedor(this.proveedor, this.getAdjuntos().size(), this.listadoIIBBEliminar);
            for (final MessageBean messageBean : enviarProveedor) {
                Mensajes.displayFacesMessage(messageBean.getSeverity(), messageBean.getMessage());
            }
            if (enviarProveedor.isEmpty()) {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_INFO, "Los datos del proveedor se enviaron correctamente.");
                this.entidadGuardada = true;
                this.listadoIIBBEliminar = new ArrayList();
            }
            this.setFlags();
        }
        catch (ServiceException ex) {
            final Iterator<String> iterator2 = ex.getMessages().iterator();
            while (iterator2.hasNext()) {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, (String)iterator2.next());
            }
        }
    }
    
    public void aprobarCompras() {
        this.aprobarCompras(new HolderHelper());
    }
    
    public void aprobarCompras(final HolderHelper<Boolean> holderHelper) {
        if (!this.validarCompras()) {
            holderHelper.set(false);
            return;
        }
        try {
            if (this.usuarioEBS == null) {
                this.usuarioEBS = this.usuarioService.getUsuarioEbs();
                if (this.usuarioEBS == null) {
                    Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, ErrorMessage.DENIED_ACCESS.getMessage());
                    holderHelper.set(false);
                    return;
                }
            }
            this.proveedorService.aprobarCompras(this.proveedor, this.usuarioEBS.getId());
            this.setFlags();
        }
        catch (ServiceException ex) {
            Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
            final Iterator<String> iterator = ex.getMessages().iterator();
            while (iterator.hasNext()) {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, (String)iterator.next());
            }
            holderHelper.set(false);
            return;
        }
        Mensajes.displayFacesMessage(FacesMessage.SEVERITY_INFO, "El proveedor seleccionado fue aprobado correctamente.");
        this.entidadGuardada = true;
        holderHelper.set(true);
    }
    
    public void aprobarImpuestos() {
        this.aprobarImpuestos(new HolderHelper());
    }
    
    public void aprobarImpuestos(final HolderHelper<Boolean> holderHelper) {
        try {
            if (this.usuarioEBS == null) {
                this.usuarioEBS = this.usuarioService.getUsuarioEbs();
                if (this.usuarioEBS == null) {
                    Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, ErrorMessage.DENIED_ACCESS.getMessage());
                    holderHelper.set(false);
                    return;
                }
            }
            this.proveedorService.aprobarImpuestos(this.proveedor, this.usuarioEBS.getId());
            this.setFlags();
        }
        catch (ServiceException ex) {
            Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
            final Iterator<String> iterator = ex.getMessages().iterator();
            while (iterator.hasNext()) {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, (String)iterator.next());
            }
            holderHelper.set(false);
            return;
        }
        Mensajes.displayFacesMessage(FacesMessage.SEVERITY_INFO, "El proveedor seleccionado fue aprobado correctamente.");
        this.entidadGuardada = true;
        holderHelper.set(true);
    }
    
    public void aprobarTesoreria() {
        this.aprobarTesoreria(new HolderHelper());
    }
    
    public void aprobarTesoreria(final HolderHelper<Boolean> holderHelper) {
        try {
            if (this.usuarioEBS == null) {
                this.usuarioEBS = this.usuarioService.getUsuarioEbs();
                if (this.usuarioEBS == null) {
                    Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, ErrorMessage.DENIED_ACCESS.getMessage());
                    holderHelper.set(false);
                    return;
                }
            }
            this.proveedorService.aprobarTesoreria(this.proveedor, this.usuarioEBS.getId());
            this.setFlags();
        }
        catch (ServiceException ex) {
            Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
            final Iterator<String> iterator = ex.getMessages().iterator();
            while (iterator.hasNext()) {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, (String)iterator.next());
            }
            holderHelper.set(false);
            return;
        }
        Mensajes.displayFacesMessage(FacesMessage.SEVERITY_INFO, "El proveedor seleccionado fue aprobado correctamente.");
        this.entidadGuardada = true;
        holderHelper.set(true);
    }
    
    public boolean validarCompras() {
        boolean b = true;
        if (this.proveedor.getMotivo() == null || this.proveedor.getMotivo().isEmpty()) {
            Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, "El campo Motivo es obligatorio, debe ingresar un valor.");
            b = false;
        }
        return b;
    }
    
    public void rechazarCompras() {
        this.rechazarCompras(new HolderHelper());
    }
    
    public void rechazarCompras(final HolderHelper<Boolean> holderHelper) {
        if (this.motivoRechazo.isEmpty()) {
            Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, "Debe ingresar un motivo de rechazo.");
            holderHelper.set(false);
            return;
        }
        try {
            this.proveedorService.rechazarCompras(this.proveedor, this.motivoRechazo);
            this.setFlags();
        }
        catch (ServiceException ex) {
            Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
            final Iterator<String> iterator = ex.getMessages().iterator();
            while (iterator.hasNext()) {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, (String)iterator.next());
            }
            holderHelper.set(false);
            return;
        }
        Mensajes.displayFacesMessage(FacesMessage.SEVERITY_INFO, "El proveedor seleccionado fue rechazado.");
        this.entidadGuardada = true;
        holderHelper.set(true);
    }
    
    public void rechazarImpuestos() {
        this.rechazarImpuestos(new HolderHelper());
    }
    
    public void rechazarImpuestos(final HolderHelper<Boolean> holderHelper) {
        if (this.motivoRechazo.isEmpty()) {
            Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, "Debe ingresar un motivo de rechazo.");
            holderHelper.set(false);
            return;
        }
        try {
            this.proveedorService.rechazarImpuestos(this.proveedor, this.motivoRechazo);
            this.setFlags();
        }
        catch (ServiceException ex) {
            Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
            final Iterator<String> iterator = ex.getMessages().iterator();
            while (iterator.hasNext()) {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, (String)iterator.next());
            }
            holderHelper.set(false);
            return;
        }
        Mensajes.displayFacesMessage(FacesMessage.SEVERITY_INFO, "El proveedor seleccionado fue rechazado.");
        this.entidadGuardada = true;
        holderHelper.set(true);
    }
    
    public void rechazarTesoreria() {
        this.rechazarTesoreria(new HolderHelper());
    }
    
    public void rechazarTesoreria(final HolderHelper<Boolean> holderHelper) {
        if (this.motivoRechazo.isEmpty()) {
            Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, "Debe ingresar un motivo de rechazo.");
            holderHelper.set(false);
            return;
        }
        try {
            this.proveedorService.rechazarTesoreria(this.proveedor, this.motivoRechazo);
            this.setFlags();
        }
        catch (ServiceException ex) {
            Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
            final Iterator<String> iterator = ex.getMessages().iterator();
            while (iterator.hasNext()) {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, (String)iterator.next());
            }
            holderHelper.set(false);
            return;
        }
        Mensajes.displayFacesMessage(FacesMessage.SEVERITY_INFO, "El proveedor seleccionado fue rechazado.");
        this.entidadGuardada = true;
        holderHelper.set(true);
    }
    
    public void reintentarAprobar() {
        try {
            if (this.usuarioEBS == null) {
                this.usuarioEBS = this.usuarioService.getUsuarioEbs();
                if (this.usuarioEBS == null) {
                    Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, ErrorMessage.DENIED_ACCESS.getMessage());
                    return;
                }
            }
            this.proveedorService.reintentarAprobarProveedor(this.proveedor, this.usuarioEBS.getId());
            this.setFlags();
        }
        catch (ServiceException ex) {
            Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
            final Iterator<String> iterator = ex.getMessages().iterator();
            while (iterator.hasNext()) {
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, (String)iterator.next());
            }
            return;
        }
        Mensajes.displayFacesMessage(FacesMessage.SEVERITY_INFO, "El proveedor fue aprobado correctamente.");
        this.entidadGuardada = true;
    }
    
    public void guardarFechaVencimiento() {
        if (this.proveedor.getFechaVencimiento() == null) {
            Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, "Debe ingresar una fecha de vencimiento para el proveedor.");
            return;
        }
        final HolderHelper holderHelper = new HolderHelper();
        this.guardarProveedor(holderHelper);
    }
    
    public void clearNuevoIIBB() {
        this.setNuevoIIBB(new IIBBMultilateral());
    }
    
    public void agregarIIBBMultilateral() {
        if (!this.validarIIBBMultilateral()) {
            System.out.println("agregarIIBBMultilateral(): Error de validaci\u00f3n.");
            this.operacionDeMultilateralDialog = true;
            return;
        }
        this.nuevoIIBB.setProveedor(this.proveedor);
        this.proveedor.getIibbMultilaterales().add(this.nuevoIIBB);
        this.provinciasDisponibles.remove(this.nuevoIIBB.getJurisdiccion());
        this.clearNuevoIIBB();
        this.entidadModificada = true;
        RequestContext.getCurrentInstance().execute("dlgPopUpMultilateral.hide()");
    }
    
    public boolean validarIIBBMultilateral() {
        boolean b = true;
        if (this.nuevoIIBB.isExento() && (this.nuevoIIBB.getPorcentajeExclusion() == null || this.nuevoIIBB.getPorcentajeExclusion().isEmpty())) {
            Mensajes.displayClientFacesMessage("mensajesMultilateralDialog", FacesMessage.SEVERITY_ERROR, "Debe ingresar el porcentaje de exclusi\u00f3n si es exento.");
            b = false;
        }
        if (this.nuevoIIBB.getPorcentajeExclusion() != null && !this.nuevoIIBB.getPorcentajeExclusion().isEmpty()) {
            try {
                final float float1 = Float.parseFloat(this.nuevoIIBB.getPorcentajeExclusion());
                if (float1 < 0.0f || float1 > 100.0f) {
                    Mensajes.displayClientFacesMessage("mensajesMultilateralDialog", FacesMessage.SEVERITY_ERROR, "El porcentaje de exclusi\u00f3n debe ser un n\u00famero entre 0 y 100.");
                    b = false;
                }
            }
            catch (NumberFormatException ex) {
                Mensajes.displayClientFacesMessage("mensajesMultilateralDialog", FacesMessage.SEVERITY_ERROR, "El porcentaje de exclusi\u00f3n debe ser num\u00e9rico.");
                b = false;
            }
        }
        return b;
    }
    
    public void cancelarIIBBMultilateral() {
        this.clearNuevoIIBB();
    }
    
    public void clearNuevoAdjunto() {
        this.setNuevoAdjunto(new ProveedorAdjunto());
    }
    
    public void abrirPopUpAdjuntos() {
        this.clearNuevoAdjunto();
    }
    
    public void agregarAdjunto(final FileUploadEvent fileUploadEvent) {
        this.operacionDeAdjuntoDialog = true;
        try {
            if (fileUploadEvent.getFile().getSize() / 1024L < 5120L) {
                final byte[] data = new byte[(int)fileUploadEvent.getFile().getSize()];
                fileUploadEvent.getFile().getInputstream().read(data);
                this.nuevoAdjunto.setNombre(fileUploadEvent.getFile().getFileName());
                this.nuevoAdjunto.setSize(fileUploadEvent.getFile().getSize());
                this.nuevoAdjunto.setContentType(fileUploadEvent.getFile().getContentType());
                this.nuevoAdjunto.setData(data);
                fileUploadEvent.getFile().getInputstream().close();
            }
            else {
                Mensajes.displayClientFacesMessage("mensajesAdjuntoDialog", FacesMessage.SEVERITY_ERROR, "Tama\u00f1o m\u00e1ximo permitido 5MB.");
            }
        }
        catch (IOException ex) {
            Mensajes.displayClientFacesMessage("mensajesAdjuntoDialog", FacesMessage.SEVERITY_ERROR, ErrorMessage.ERROR_ADJUNTO.getMessage());
        }
    }
    
    public void agregarAdjunto() {
        this.operacionDeAdjuntoDialog = true;
        if (this.nuevoAdjunto.getDescripcion() != null) {
            this.nuevoAdjunto.setDescripcion(this.nuevoAdjunto.getDescripcion().trim());
        }
        boolean b = false;
        if (this.nuevoAdjunto.getData() == null) {
            Mensajes.displayClientFacesMessage("mensajesAdjuntoDialog", FacesMessage.SEVERITY_ERROR, "No se seleccion\u00f3 archivo a adjuntar.");
            b = true;
        }
        if (this.nuevoAdjunto.getTipoArchivo() == null || this.nuevoAdjunto.getTipoArchivo().isEmpty()) {
            Mensajes.displayClientFacesMessage("mensajesAdjuntoDialog", FacesMessage.SEVERITY_ERROR, "No se seleccion\u00f3 archivo a adjuntar.");
            b = true;
        }
        if (this.nuevoAdjunto.getDescripcion() == null || this.nuevoAdjunto.getDescripcion().isEmpty()) {
            Mensajes.displayClientFacesMessage("mensajesAdjuntoDialog", FacesMessage.SEVERITY_ERROR, "El campo Descripci\u00f3n es obligatorio.");
            b = true;
        }
        if (b) {
            return;
        }
        try {
            this.nuevoAdjunto.setProveedor(this.proveedor);
            this.proveedorAdjuntoService.agregarAdjunto(this.nuevoAdjunto);
            this.clearNuevoAdjunto();
            this.adjuntos = null;
            Mensajes.displayClientFacesMessage("mensajesAdjunto", FacesMessage.SEVERITY_INFO, "El archivo adjunto fue agregado correctamente.");
            RequestContext.getCurrentInstance().execute("dlgPopUpAdjuntos.hide()");
            this.entidadModificada = true;
            this.operacionDeAdjunto = true;
            this.operacionDeAdjuntoDialog = false;
        }
        catch (ServiceException ex) {
            final Iterator<String> iterator = ex.getMessages().iterator();
            while (iterator.hasNext()) {
                Mensajes.displayClientFacesMessage("mensajesAdjuntoDialog", FacesMessage.SEVERITY_ERROR, (String)iterator.next());
            }
        }
    }
    
    public void eliminarAdjunto(final ProveedorAdjunto proveedorAdjunto) {
        this.operacionDeAdjunto = true;
        try {
            this.proveedorAdjuntoService.eliminarAdjunto(proveedorAdjunto);
            this.adjuntos = null;
            Mensajes.displayClientFacesMessage("mensajesAdjunto", FacesMessage.SEVERITY_INFO, "El archivo adjunto fue eliminado correctamente.");
            this.entidadModificada = true;
        }
        catch (ServiceException ex) {
            final Iterator<String> iterator = ex.getMessages().iterator();
            while (iterator.hasNext()) {
                Mensajes.displayClientFacesMessage("mensajesAdjunto", FacesMessage.SEVERITY_ERROR, (String)iterator.next());
            }
        }
    }
    
    public void descargarAdjunto(final ProveedorAdjunto proveedorAdjunto) {
        try {
            final ProveedorAdjunto adjunto = this.proveedorAdjuntoService.getAdjunto(Long.valueOf(proveedorAdjunto.getId()));
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
    
    public ProveedorEstado getEstadoActual() {
        try {
            return this.proveedorEstadoService.getEstadoActual(this.proveedor);
        }
        catch (ServiceException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public String getCantidadBytes(final long n) {
        return Mensajes.humanReadableByteCount(n);
    }
    
    public void validarCuit() {
        this.setErrorValidacionCuit(this.proveedorService.validarCuit(this.proveedor));
    }
    
    public Proveedor getProveedor() {
        return this.proveedor;
    }
    
    public void setProveedor(final Proveedor proveedor) {
        this.proveedor = proveedor;
    }
    
    public ProveedorService getProveedorService() {
        return this.proveedorService;
    }
    
    public void setProveedorService(final ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }
    
    public CondicionIVA[] getComboCondicionIVA() {
        return this.comboCondicionIVA;
    }
    
    public void setComboCondicionIVA(final CondicionIVA[] comboCondicionIVA) {
        this.comboCondicionIVA = comboCondicionIVA;
    }
    
    public CondicionGanancias[] getComboImpuestoGanancias() {
        return this.comboImpuestoGanancias;
    }
    
    public void setComboImpuestoGanancias(final CondicionGanancias[] comboImpuestoGanancias) {
        this.comboImpuestoGanancias = comboImpuestoGanancias;
    }
    
    public UploadedFile getFile() {
        return this.file;
    }
    
    public void setFile(final UploadedFile file) {
        this.file = file;
    }
    
    public Perfil getPerfil() {
        return this.perfil;
    }
    
    public void setPerfil(final Perfil perfil) {
        this.perfil = perfil;
    }
    
    public boolean isDisabled() {
        return this.disabled;
    }
    
    public void setDisabled(final boolean disabled) {
        this.disabled = disabled;
    }
    
    public boolean isDisabledExclusion() {
        return this.disabledExclusion;
    }
    
    public void setDisabledExclusion(final boolean disabledExclusion) {
        this.disabledExclusion = disabledExclusion;
    }
    
    public boolean isDisabledExclusionLocal() {
        return this.disabledExclusionLocal;
    }
    
    public void setDisabledExclusionLocal(final boolean disabledExclusionLocal) {
        this.disabledExclusionLocal = disabledExclusionLocal;
    }
    
    public boolean isDisabledCompras() {
        return this.disabledCompras;
    }
    
    public void setDisabledCompras(final boolean disabledCompras) {
        this.disabledCompras = disabledCompras;
    }
    
    public UsuarioService getUsuarioService() {
        return this.usuarioService;
    }
    
    public void setUsuarioService(final UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }
    
    public IIBBMultilateral getNuevoIIBB() {
        return this.nuevoIIBB;
    }
    
    public void setNuevoIIBB(final IIBBMultilateral nuevoIIBB) {
        this.nuevoIIBB = nuevoIIBB;
    }
    
    public ProveedorAdjunto getNuevoAdjunto() {
        return this.nuevoAdjunto;
    }
    
    public void setNuevoAdjunto(final ProveedorAdjunto nuevoAdjunto) {
        this.nuevoAdjunto = nuevoAdjunto;
    }
    
    public TipoAdjunto[] getTipoAdjuntos() {
        return this.tipoAdjuntos;
    }
    
    public void setTipoAdjuntos(final TipoAdjunto[] tipoAdjuntos) {
        this.tipoAdjuntos = tipoAdjuntos;
    }
    
    public List<Impuesto> getActividadesPrincipales() {
        return (List<Impuesto>)this.actividadesPrincipales;
    }
    
    public void setActividadesPrincipales(final List<Impuesto> actividadesPrincipales) {
        this.actividadesPrincipales = actividadesPrincipales;
    }
    
    public List<Moneda> getMonedas() {
        return (List<Moneda>)this.monedas;
    }
    
    public void setMonedas(final List<Moneda> monedas) {
        this.monedas = monedas;
    }
    
    public List<Provincia> getProvincias() {
        return (List<Provincia>)this.provincias;
    }
    
    public void setProvincias(final List<Provincia> provincias) {
        this.provincias = provincias;
    }
    
    public List<Provincia> getProvinciasDisponibles() {
        return (List<Provincia>)this.provinciasDisponibles;
    }
    
    public void setProvinciasDisponibles(final List<Provincia> provinciasDisponibles) {
        this.provinciasDisponibles = provinciasDisponibles;
    }
    
    public List<IIBBMultilateral> getListadoIIBBEliminar() {
        return (List<IIBBMultilateral>)this.listadoIIBBEliminar;
    }
    
    public void setListadoIIBBEliminar(final List<IIBBMultilateral> listadoIIBBEliminar) {
        this.listadoIIBBEliminar = listadoIIBBEliminar;
    }
    
    public List<ProveedorAdjunto> getAdjuntos() {
        if (this.adjuntos == null) {
            try {
                this.adjuntos = this.proveedorAdjuntoService.getAdjuntos(this.proveedor);
            }
            catch (ServiceException ex) {
                ex.printStackTrace();
                Mensajes.displayFacesMessage(FacesMessage.SEVERITY_ERROR, "Error al obtener la lista de adjuntos");
            }
        }
        return (List<ProveedorAdjunto>)this.adjuntos;
    }
    
    public void setAdjuntos(final List<ProveedorAdjunto> adjuntos) {
        this.adjuntos = adjuntos;
    }
    
    public boolean isEntidadModificada() {
        return this.entidadModificada;
    }
    
    public boolean setEntidadModificadaFalse() {
        return this.entidadModificada = false;
    }
    
    public boolean isEntidadGuardada() {
        return this.entidadGuardada;
    }
    
    public boolean setEntidadGuardadaFalse() {
        return this.entidadGuardada = false;
    }
    
    public boolean isOperacionDeAdjunto() {
        return this.operacionDeAdjunto;
    }
    
    public boolean setOperacionDeAdjuntoFalse() {
        return this.operacionDeAdjunto = false;
    }
    
    public boolean isOperacionDeAdjuntoDialog() {
        return this.operacionDeAdjuntoDialog;
    }
    
    public boolean setOperacionDeAdjuntoDialogFalse() {
        return this.operacionDeAdjuntoDialog = false;
    }
    
    public boolean isOperacionDeMultilateralDialog() {
        return this.operacionDeMultilateralDialog;
    }
    
    public boolean setOperacionDeMultilateralDialogFalse() {
        return this.operacionDeMultilateralDialog = false;
    }
    
    public String getMotivoRechazo() {
        return this.motivoRechazo;
    }
    
    public void setMotivoRechazo(final String motivoRechazo) {
        this.motivoRechazo = motivoRechazo;
    }
    
    public String getErrorValidacionCuit() {
        return this.errorValidacionCuit;
    }
    
    public void setErrorValidacionCuit(final String errorValidacionCuit) {
        this.errorValidacionCuit = errorValidacionCuit;
    }
    
    public void emptyErrorValidacionCuit() {
        this.errorValidacionCuit = null;
    }
}
