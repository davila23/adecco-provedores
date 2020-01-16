// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.service;

import ar.com.syntagma.validators.CuitValidator;
import ar.com.syntagma.validators.CbuValidator;
import javax.faces.application.FacesMessage;
import java.util.GregorianCalendar;
import ar.com.adecco.proveedores.daos.exception.InterfazException;
import ar.com.syntagma.helpers.HolderHelper;
import java.util.Date;
import ar.com.syntagma.helpers.PropertiesHelper;
import ar.com.adecco.dominio.persona.PersonaRelacionProveedor;
import ar.com.adecco.proveedores.model.ProveedorEstado;
import ar.com.adecco.proveedores.model.EstadoProveedor;
import java.util.ArrayList;
import ar.com.adecco.proveedores.bean.MessageBean;
import java.util.Iterator;
import ar.com.adecco.proveedores.model.IIBBMultilateral;
import java.util.List;
import java.util.Collection;
import ar.com.adecco.proveedores.daos.helpers.ErrorMessage;
import ar.com.adecco.proveedores.daos.exception.DaoException;
import ar.com.adecco.proveedores.daos.exception.ServiceException;
import ar.com.adecco.proveedores.model.Proveedor;
import java.util.HashMap;
import ar.com.adecco.proveedores.daos.InterfazAPDao;
import ar.com.adecco.proveedores.daos.PersonaRelacionProveedorDao;
import ar.com.adecco.proveedores.daos.UsuarioAdeccoDao;
import ar.com.adecco.proveedores.daos.IIBBMultilateralDao;
import ar.com.adecco.proveedores.daos.ProveedorEstadoDao;
import ar.com.adecco.proveedores.daos.ProveedorAdjuntoDao;
import ar.com.adecco.proveedores.daos.ProvinciaDao;
import ar.com.adecco.proveedores.daos.ProveedorDao;
import ar.com.adecco.proveedores.daos.ProveedorEBSDao;
import javax.inject.Inject;
import ar.com.syntagma.adecco.login.cliente.MenuBean;
import org.jboss.solder.logging.Logger;
import javax.inject.Named;
import java.io.Serializable;

@Named
public class ProveedorService implements Serializable
{
    private static final long serialVersionUID = -661344451191328779L;
    private static final Logger log;
    @Inject
    MenuBean menuBean;
    @Inject
    EmailService emailService;
    @Inject
    EmailFactoryService emailFactoryService;
    @Inject
    PersonaRelacionProveedorService personaRelacionProveedorService;
    @Inject
    ProveedorEBSDao proveedorEBSDao;
    @Inject
    ProveedorDao proveedorDao;
    @Inject
    ProvinciaDao provinciaDao;
    @Inject
    ProveedorAdjuntoDao proveedorAdjuntoDao;
    @Inject
    ProveedorEstadoDao proveedorEstadoDao;
    @Inject
    IIBBMultilateralDao iibbMultilateralDao;
    @Inject
    UsuarioAdeccoDao usuarioAdeccoDao;
    @Inject
    PersonaRelacionProveedorDao personaRelacionProveedorDao;
    @Inject
    InterfazAPDao interfazAPDao;
    private HashMap<String, Object> parametros;
    
    public ProveedorService() {
        this.parametros = new HashMap<String, Object>();
    }
    
    public Proveedor getEntityById(final long n) throws ServiceException {
        try {
            return (Proveedor)this.proveedorDao.obtener((Object)n);
        }
        catch (DaoException ex) {
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, ProveedorService.class.getName());
        }
        catch (Exception ex2) {
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, ProveedorService.class.getName());
        }
    }
    
    public List<Proveedor> getByIds(final Collection<Long> collection) {
        return (List<Proveedor>)this.proveedorDao.getByIds((Collection)collection);
    }
    
    public Proveedor getProveedorEagerLoadById(final long n) throws ServiceException {
        try {
            return this.proveedorDao.getProveedorEagerLoadById(n);
        }
        catch (Exception ex) {
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex, ProveedorService.class.getName());
        }
    }
    
    public List<Proveedor> listar(final List<Long> list) throws ServiceException {
        try {
            return (List<Proveedor>)this.proveedorDao.listar((List)list, "a.razonSocial");
        }
        catch (DaoException ex) {
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, ProveedorService.class.getName());
        }
        catch (Exception ex2) {
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, ProveedorService.class.getName());
        }
    }
    
    public boolean isProvinciaIIBBDisponible(final Proveedor proveedor, final String s) {
        if (s.equals("Extranjero") || s.equals(proveedor.getJurisdiccionLocal())) {
            return false;
        }
        final Iterator<IIBBMultilateral> iterator = proveedor.getIibbMultilaterales().iterator();
        while (iterator.hasNext()) {
            if (s.equals(iterator.next().getJurisdiccion())) {
                return false;
            }
        }
        return true;
    }
    
    public List<MessageBean> guardarProveedor(final Proveedor proveedor, final List<IIBBMultilateral> list) throws ServiceException {
        final ArrayList<MessageBean> list2 = new ArrayList<MessageBean>();
        try {
            if (proveedor.getId() == 0L) {
                proveedor.setHabilitado(true);
                proveedor.setEstado(EstadoProveedor.INGRESADO);
                this.proveedorDao.addProveedor(proveedor);
                for (final IIBBMultilateral iibbMultilateral : proveedor.getIibbMultilaterales()) {
                    if (iibbMultilateral.getId() == 0L) {
                        this.iibbMultilateralDao.addIIBB(iibbMultilateral);
                    }
                }
                final ProveedorEstado proveedorEstado = new ProveedorEstado();
                proveedorEstado.setEstado(EstadoProveedor.INGRESADO);
                proveedorEstado.setProveedor(proveedor);
                proveedorEstado.setComentario("Creaci\u00f3n");
                this.proveedorEstadoDao.addProveedorEstado(proveedorEstado);
                final PersonaRelacionProveedor personaRelacionProveedor = new PersonaRelacionProveedor();
                personaRelacionProveedor.setProveedor(proveedor);
                personaRelacionProveedor.setPersona(this.usuarioAdeccoDao.getUserByName(this.menuBean.getUsuario().getLogin()).getPersona());
                this.personaRelacionProveedorDao.addUsuarioRelacion(personaRelacionProveedor);
            }
            else {
                this.iibbMultilateralDao.removeIIBB((List)list);
                for (final IIBBMultilateral iibbMultilateral2 : proveedor.getIibbMultilaterales()) {
                    if (iibbMultilateral2.getId() == 0L) {
                        this.iibbMultilateralDao.addIIBB(iibbMultilateral2);
                    }
                }
                this.proveedorDao.modifyProveedor(proveedor);
            }
            this.proveedorDao.flushEntities();
            this.proveedorDao.clearEntities();
            return list2;
        }
        catch (DaoException ex) {
            this.proveedorDao.clearEntities();
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, ProveedorService.class.getName());
        }
        catch (Exception ex2) {
            this.proveedorDao.clearEntities();
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, ProveedorService.class.getName());
        }
    }
    
    public List<MessageBean> enviarProveedor(final Proveedor proveedor, final int n, final List<IIBBMultilateral> list) throws ServiceException {
        final ArrayList<MessageBean> list2 = new ArrayList<MessageBean>();
        try {
            if (this.validarProveedor(proveedor, n, list2)) {
                for (final IIBBMultilateral iibbMultilateral : list) {
                    if (iibbMultilateral.getId() != 0L) {
                        this.iibbMultilateralDao.removeIIBB((IIBBMultilateral)this.iibbMultilateralDao.obtener((Object)iibbMultilateral.getId()));
                    }
                }
                for (final IIBBMultilateral iibbMultilateral2 : proveedor.getIibbMultilaterales()) {
                    if (iibbMultilateral2.getId() == 0L) {
                        this.iibbMultilateralDao.addIIBB(iibbMultilateral2);
                    }
                }
                final ProveedorEstado proveedorEstado = new ProveedorEstado();
                proveedorEstado.setEstado(EstadoProveedor.ENVIADO);
                proveedorEstado.setEstadoAnterior(proveedor.getEstado());
                proveedorEstado.setProveedor(proveedor);
                proveedorEstado.setComentario("Estado ENVIADO");
                this.proveedorEstadoDao.addProveedorEstado(proveedorEstado);
                proveedor.setEstado(EstadoProveedor.ENVIADO);
                proveedor.setAprobadoCompras(false);
                proveedor.setAprobadoImpuestos(false);
                proveedor.setAprobadoTesoreria(false);
                this.proveedorDao.modifyProveedor(proveedor);
                this.proveedorDao.flushEntities();
                this.sendEmailProveedorEnviado(proveedor);
            }
            return list2;
        }
        catch (DaoException ex) {
            this.proveedorDao.clearEntities();
            ex.printStackTrace();
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, ProveedorService.class.getName());
        }
        catch (Exception ex2) {
            this.proveedorDao.clearEntities();
            ex2.printStackTrace();
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, ProveedorService.class.getName());
        }
    }
    
    public boolean proveedorEditable(final Proveedor proveedor) {
        if (proveedor.getEstado() == null) {
            return true;
        }
        switch (proveedor.getEstado()) {
            case INGRESADO:
            case RECHAZADO: {
                return true;
            }
            case APROBADO: {
                if (proveedor.getFechaVencimiento() == null) {
                    break;
                }
                int int1 = 0;
                final String property = new PropertiesHelper("proveedores.properties").getProperty("diasAvisoVencimiento", "");
                if (!property.isEmpty()) {
                    int1 = Integer.parseInt(property);
                }
                if (new Date().after(restarDiasAFecha(proveedor.getFechaVencimiento(), int1))) {
                    return true;
                }
                break;
            }
        }
        return false;
    }
    
    public boolean editarProveedor(final Proveedor proveedor) {
        return EstadoProveedor.ENVIADO.equals((Object)proveedor.getEstado());
    }
    
    public boolean proveedorIngresado(final Proveedor proveedor) {
        return EstadoProveedor.INGRESADO.equals((Object)proveedor.getEstado());
    }
    
    public boolean proveedorEnviado(final Proveedor proveedor) {
        return EstadoProveedor.ENVIADO.equals((Object)proveedor.getEstado());
    }
    
    public boolean proveedorAprobado(final Proveedor proveedor) {
        return EstadoProveedor.APROBADO.equals((Object)proveedor.getEstado());
    }
    
    public boolean proveedorErrorAprobacion(final Proveedor proveedor) {
        return EstadoProveedor.ERROR_APROBACION.equals((Object)proveedor.getEstado());
    }
    
    public boolean proveedorRechazado(final Proveedor proveedor) {
        return EstadoProveedor.RECHAZADO.equals((Object)proveedor.getEstado());
    }
    
    private void aprobarProveedor(final Proveedor proveedor, final long n) throws ServiceException {
        try {
            final PropertiesHelper propertiesHelper = new PropertiesHelper("proveedores.properties");
            if (!propertiesHelper.getProperty("diasVigenciaProveedor", "").isEmpty()) {
                proveedor.setFechaVencimiento(agregarDiasAFecha(new Date(), Integer.parseInt(propertiesHelper.getProperty("diasVigenciaProveedor", ""))));
            }
            final HolderHelper holderHelper = new HolderHelper();
            if (proveedor.getProveedorEBS() == null) {
                final ar.com.adecco.dominio.ebs.po.Proveedor addProveedor = this.proveedorEBSDao.addProveedor(proveedor, n, holderHelper);
                proveedor.setProveedorEBS(addProveedor);
                this.interfazAPDao.addProveedor(proveedor, (String)holderHelper.get());
                if (holderHelper.get() != null && !((String)holderHelper.get()).isEmpty()) {
                    if (addProveedor == null) {
                        throw new ServiceException("Ha ocurrido el siguiente error: " + (String)holderHelper.get(), ProveedorService.class.getName());
                    }
                    ProveedorService.log.warn((Object)"El proveedor ya exist\u00eda en EBS, se procede a actualizar sus datos...");
                    this.proveedorDao.modifyAndFlushProveedor(proveedor);
                    this.proveedorEBSDao.actualizarProveedor(proveedor, n, holderHelper);
                    this.interfazAPDao.addProveedor(proveedor, (String)holderHelper.get());
                    ProveedorService.log.info((Object)"Datos del proveedor actualizados en EBS.");
                }
            }
            else {
                final ar.com.adecco.dominio.ebs.po.Proveedor actualizarProveedor = this.proveedorEBSDao.actualizarProveedor(proveedor, n, holderHelper);
                this.interfazAPDao.addProveedor(proveedor, (String)holderHelper.get());
                if (proveedor.getProveedorEBS().getId() != actualizarProveedor.getId()) {
                    ProveedorService.log.warn((Object)("El proveedor en EBS es distinto al guardado en Proveedores. Id en EBS: " + actualizarProveedor.getId() + ". " + "Id en Proveedores: " + proveedor.getProveedorEBS().getId() + "."));
                    ProveedorService.log.warn((Object)"Se prodece a actualizarlo.");
                    proveedor.setProveedorEBS(actualizarProveedor);
                }
            }
            final ProveedorEstado proveedorEstado = new ProveedorEstado();
            proveedorEstado.setEstado(EstadoProveedor.APROBADO);
            proveedorEstado.setEstadoAnterior(proveedor.getEstado());
            proveedorEstado.setProveedor(proveedor);
            proveedorEstado.setComentario("Estado APROBADO");
            this.proveedorEstadoDao.addProveedorEstado(proveedorEstado);
            proveedor.setEstado(EstadoProveedor.APROBADO);
            this.proveedorDao.modifyProveedor(proveedor);
            this.proveedorDao.flushEntities();
            this.sendEmailProveedorAprobado(proveedor);
        }
        catch (DaoException ex) {
            this.aprobarProveedorHandleException((Exception)ex, proveedor);
        }
        catch (InterfazException ex2) {
            this.aprobarProveedorHandleException((Exception)ex2, proveedor);
        }
    }
    
    private void aprobarProveedorHandleException(final Exception ex, final Proveedor proveedor) throws ServiceException {
        final ProveedorEstado proveedorEstado = new ProveedorEstado();
        proveedorEstado.setEstado(EstadoProveedor.ERROR_APROBACION);
        proveedorEstado.setEstadoAnterior(proveedor.getEstado());
        proveedorEstado.setProveedor(proveedor);
        if (ex.getMessage() != null) {
            proveedorEstado.setComentario((ex.getMessage().length() <= 255) ? ex.getMessage() : ex.getMessage().substring(0, 255));
        }
        else {
            proveedorEstado.setComentario("Error de Aprobaci\u00f3n");
        }
        final ServiceException ex2 = new ServiceException(ex);
        ex2.setServicio(ProveedorService.class.getName());
        ex2.addMessage(ErrorMessage.ERROR_IAP.getMessage());
        try {
            this.proveedorEstadoDao.addProveedorEstado(proveedorEstado);
            proveedor.setEstado(EstadoProveedor.ERROR_APROBACION);
            this.interfazAPDao.addProveedor(proveedor, ex.getMessage());
            this.interfazAPDao.flushEntities();
            this.proveedorDao.modifyProveedor(proveedor);
            this.proveedorDao.flushEntities();
        }
        catch (DaoException ex3) {
            this.proveedorDao.clearEntities();
            ex2.addMessage((String)ex3.getMessages().get(0));
        }
        throw ex2;
    }
    
    public boolean puedeAprobarseProveedor(final Proveedor proveedor) {
        return proveedor.isAprobadoCompras() && proveedor.isAprobadoImpuestos() && proveedor.isAprobadoTesoreria() && (proveedor.getEstado().equals((Object)EstadoProveedor.ENVIADO) || proveedor.getEstado().equals((Object)EstadoProveedor.ERROR_APROBACION));
    }
    
    public void reintentarAprobarProveedor(final Proveedor proveedor, final long n) throws ServiceException {
        try {
            this.aprobarProveedor(proveedor, n);
        }
        catch (ServiceException ex) {
            this.proveedorDao.clearEntities();
            throw ex;
        }
        catch (Exception ex2) {
            this.proveedorDao.clearEntities();
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, ProveedorService.class.getName());
        }
    }
    
    public void aprobarCompras(final Proveedor proveedor, final long n) throws ServiceException {
        try {
            proveedor.setAprobadoCompras(true);
            this.proveedorDao.modifyProveedor(proveedor);
            this.proveedorDao.flushEntities();
            if (this.puedeAprobarseProveedor(proveedor)) {
                this.aprobarProveedor(proveedor, n);
            }
        }
        catch (ServiceException ex) {
            this.proveedorDao.clearEntities();
            throw ex;
        }
        catch (Exception ex2) {
            this.proveedorDao.clearEntities();
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, ProveedorService.class.getName());
        }
    }
    
    public void aprobarImpuestos(final Proveedor proveedor, final long n) throws ServiceException {
        try {
            proveedor.setAprobadoImpuestos(true);
            this.proveedorDao.modifyProveedor(proveedor);
            this.proveedorDao.flushEntities();
            if (this.puedeAprobarseProveedor(proveedor)) {
                this.aprobarProveedor(proveedor, n);
            }
        }
        catch (ServiceException ex) {
            this.proveedorDao.clearEntities();
            throw ex;
        }
        catch (Exception ex2) {
            this.proveedorDao.clearEntities();
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, ProveedorService.class.getName());
        }
    }
    
    public void aprobarTesoreria(final Proveedor proveedor, final long n) throws ServiceException {
        try {
            proveedor.setAprobadoTesoreria(true);
            this.proveedorDao.modifyProveedor(proveedor);
            this.proveedorDao.flushEntities();
            if (this.puedeAprobarseProveedor(proveedor)) {
                this.aprobarProveedor(proveedor, n);
            }
        }
        catch (ServiceException ex) {
            this.proveedorDao.clearEntities();
            throw ex;
        }
        catch (Exception ex2) {
            this.proveedorDao.clearEntities();
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, ProveedorService.class.getName());
        }
    }
    
    public void rechazarCompras(final Proveedor proveedor, final String comentario) throws ServiceException {
        try {
            final ProveedorEstado proveedorEstado = new ProveedorEstado();
            proveedorEstado.setEstado(EstadoProveedor.RECHAZADO);
            proveedorEstado.setEstadoAnterior(proveedor.getEstado());
            proveedorEstado.setProveedor(proveedor);
            proveedorEstado.setComentario(comentario);
            this.proveedorEstadoDao.addProveedorEstado(proveedorEstado);
            proveedor.setAprobadoCompras(false);
            proveedor.setAprobadoImpuestos(false);
            proveedor.setAprobadoTesoreria(false);
            proveedor.setEstado(EstadoProveedor.RECHAZADO);
            this.proveedorDao.modifyProveedor(proveedor);
            this.proveedorDao.flushEntities();
            this.sendEmailProveedorRechazado(proveedor, comentario);
        }
        catch (DaoException ex) {
            this.proveedorDao.clearEntities();
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, ProveedorService.class.getName());
        }
        catch (Exception ex2) {
            this.proveedorDao.clearEntities();
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, ProveedorService.class.getName());
        }
    }
    
    public void rechazarImpuestos(final Proveedor proveedor, final String comentario) throws ServiceException {
        try {
            final ProveedorEstado proveedorEstado = new ProveedorEstado();
            proveedorEstado.setEstado(EstadoProveedor.RECHAZADO);
            proveedorEstado.setEstadoAnterior(proveedor.getEstado());
            proveedorEstado.setProveedor(proveedor);
            proveedorEstado.setComentario(comentario);
            this.proveedorEstadoDao.addProveedorEstado(proveedorEstado);
            proveedor.setAprobadoCompras(false);
            proveedor.setAprobadoImpuestos(false);
            proveedor.setAprobadoTesoreria(false);
            proveedor.setEstado(EstadoProveedor.RECHAZADO);
            this.proveedorDao.modifyProveedor(proveedor);
            this.proveedorDao.flushEntities();
            this.sendEmailProveedorRechazado(proveedor, comentario);
        }
        catch (DaoException ex) {
            this.proveedorDao.clearEntities();
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, ProveedorService.class.getName());
        }
        catch (Exception ex2) {
            this.proveedorDao.clearEntities();
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, ProveedorService.class.getName());
        }
    }
    
    public void rechazarTesoreria(final Proveedor proveedor, final String comentario) throws ServiceException {
        try {
            final ProveedorEstado proveedorEstado = new ProveedorEstado();
            proveedorEstado.setEstado(EstadoProveedor.RECHAZADO);
            proveedorEstado.setEstadoAnterior(proveedor.getEstado());
            proveedorEstado.setProveedor(proveedor);
            proveedorEstado.setComentario(comentario);
            this.proveedorEstadoDao.addProveedorEstado(proveedorEstado);
            proveedor.setAprobadoCompras(false);
            proveedor.setAprobadoImpuestos(false);
            proveedor.setAprobadoTesoreria(false);
            proveedor.setEstado(EstadoProveedor.RECHAZADO);
            this.proveedorDao.modifyProveedor(proveedor);
            this.proveedorDao.flushEntities();
            this.sendEmailProveedorRechazado(proveedor, comentario);
        }
        catch (DaoException ex) {
            this.proveedorDao.clearEntities();
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, ProveedorService.class.getName());
        }
        catch (Exception ex2) {
            this.proveedorDao.clearEntities();
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, ProveedorService.class.getName());
        }
    }
    
    private void sendEmailProveedorEnviado(final Proveedor proveedor) throws ServiceException {
        this.emailService.send(this.emailFactoryService.createEmailProveedorEnviadoCompras(proveedor));
        this.emailService.send(this.emailFactoryService.createEmailProveedorEnviadoTesoreria(proveedor));
        this.emailService.send(this.emailFactoryService.createEmailProveedorEnviadoImpuestos(proveedor));
    }
    
    public void sendEmailProveedorAprobado(final Proveedor proveedor) throws ServiceException {
        this.emailService.send(this.emailFactoryService.createEmailProveedorAprobado(proveedor));
    }
    
    public void sendEmailProveedorRechazado(final Proveedor proveedor, final String s) throws ServiceException {
        this.emailService.send(this.emailFactoryService.createEmailProveedorRechazado(proveedor, s));
    }
    
    public List<Proveedor> buscarProveedores(final String s, final String s2, final EstadoProveedor estadoProveedor) throws ServiceException {
        try {
            return (List<Proveedor>)this.proveedorDao.getProveedoresByFilter(this.setearFiltros(s, s2, estadoProveedor), (HashMap)this.parametros);
        }
        catch (DaoException ex) {
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, ProveedorService.class.getName());
        }
        catch (Exception ex2) {
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, ProveedorService.class.getName());
        }
    }
    
    public String setearFiltros(final String s, final String s2, final EstadoProveedor estadoProveedor) {
        String s3 = "";
        this.parametros.clear();
        if (s != null && !s.isEmpty()) {
            s3 += " and upper(razonSocial) like :razonSocial";
            this.parametros.put("razonSocial", '%' + s.toUpperCase() + '%');
        }
        if (s2 != null && !s2.isEmpty()) {
            s3 += " and upper(cuit) like :cuit";
            this.parametros.put("cuit", '%' + s2.toUpperCase() + '%');
        }
        if (estadoProveedor != null) {
            s3 += " and estado = :estado";
            this.parametros.put("estado", estadoProveedor);
        }
        return s3;
    }
    
    public static Date agregarDiasAFecha(final Date time, final int n) {
        final GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(time);
        gregorianCalendar.add(5, n);
        return gregorianCalendar.getTime();
    }
    
    public static Date restarDiasAFecha(final Date time, final int n) {
        final GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(time);
        gregorianCalendar.add(5, -n);
        return gregorianCalendar.getTime();
    }
    
    public boolean proveedorHabilitado(final Proveedor proveedor) {
        return new Date().before(proveedor.getFechaVencimiento()) && proveedor.getHabilitado();
    }
    
    public boolean validarProveedor(final Proveedor proveedor, final int n, final List<MessageBean> list) {
        boolean b = true;
        if (proveedor.getRazonSocial() == null || proveedor.getRazonSocial().isEmpty()) {
            list.add(new MessageBean("El campo Raz\u00f3n Social o Nombre del propietario es obligatorio, debe ingresar un valor.", FacesMessage.SEVERITY_ERROR));
            b = false;
        }
        if (proveedor.getCuit() == null || proveedor.getCuit().isEmpty()) {
            list.add(new MessageBean("El campo CUIT es obligatorio, debe ingresar un valor.", FacesMessage.SEVERITY_ERROR));
            b = false;
        }
        else {
            final String validarCuit = this.validarCuit(proveedor);
            if (!validarCuit.isEmpty()) {
                list.add(new MessageBean(validarCuit, FacesMessage.SEVERITY_ERROR));
                b = false;
            }
        }
        if (proveedor.getBancoSucursal() == null || proveedor.getBancoSucursal().isEmpty()) {
            list.add(new MessageBean("El campo Banco Sucursal es obligatorio, debe ingresar un valor.", FacesMessage.SEVERITY_ERROR));
            b = false;
        }
        if (proveedor.getCbu() == null || proveedor.getCbu().isEmpty()) {
            list.add(new MessageBean("El campo CBU es obligatorio, debe ingresar un valor.", FacesMessage.SEVERITY_ERROR));
            b = false;
        }
        else if (!CbuValidator.validate(proveedor.getCbu())) {
            list.add(new MessageBean("El campo CBU es inv\u00e1lido.", FacesMessage.SEVERITY_ERROR));
            b = false;
        }
        if (proveedor.getDomicilio() == null || proveedor.getDomicilio().isEmpty()) {
            list.add(new MessageBean("El campo Domicilio Fiscal es obligatorio, debe ingresar un valor.", FacesMessage.SEVERITY_ERROR));
            b = false;
        }
        if (proveedor.getCiudad() == null || proveedor.getCiudad().isEmpty()) {
            list.add(new MessageBean("El campo Ciudad es obligatorio, debe ingresar un valor.", FacesMessage.SEVERITY_ERROR));
            b = false;
        }
        if (proveedor.getProvincia() == null) {
            list.add(new MessageBean("El campo Provincia es obligatorio, debe ingresar un valor.", FacesMessage.SEVERITY_ERROR));
            b = false;
        }
        if (proveedor.getCodigoPostal() == null || proveedor.getCodigoPostal().isEmpty()) {
            list.add(new MessageBean("El campo C\u00f3digo Postal es obligatorio, debe ingresar un valor.", FacesMessage.SEVERITY_ERROR));
            b = false;
        }
        if (proveedor.getTelefono() == null || proveedor.getTelefono().isEmpty()) {
            list.add(new MessageBean("El campo Tel\u00e9fono es obligatorio, debe ingresar un valor.", FacesMessage.SEVERITY_ERROR));
            b = false;
        }
        if (proveedor.getApellidoNombreContacto() == null || proveedor.getApellidoNombreContacto().isEmpty()) {
            list.add(new MessageBean("El campo Nombre y Apellido del Contacto es obligatorio, debe ingresar un valor.", FacesMessage.SEVERITY_ERROR));
            b = false;
        }
        if (proveedor.getTelefonoContacto() == null || proveedor.getTelefonoContacto().isEmpty()) {
            list.add(new MessageBean("El campo Tel\u00e9fono del Contacto es obligatorio, debe ingresar un valor.", FacesMessage.SEVERITY_ERROR));
            b = false;
        }
        if (proveedor.getEmailContacto() == null || proveedor.getEmailContacto().isEmpty()) {
            list.add(new MessageBean("El campo E-mail del Contacto es obligatorio, debe ingresar un valor.", FacesMessage.SEVERITY_ERROR));
            b = false;
        }
        if (proveedor.getNroIIBB() == null || proveedor.getNroIIBB().isEmpty()) {
            list.add(new MessageBean("El campo Nro IIBB es obligatorio, debe ingresar un valor.", FacesMessage.SEVERITY_ERROR));
            b = false;
        }
        if (proveedor.getJurisdiccionLocal() == null) {
            list.add(new MessageBean("El campo IIBB Local es obligatorio, debe ingresar un valor.", FacesMessage.SEVERITY_ERROR));
            b = false;
        }
        if (proveedor.isExentoLocal() && (proveedor.getPorcentajeExclusionLocal() == null || proveedor.getPorcentajeExclusionLocal().isEmpty())) {
            list.add(new MessageBean("Debe ingresarse un valor en el campo % Exclusi\u00f3n si es exento.", FacesMessage.SEVERITY_ERROR));
            b = false;
        }
        if (proveedor.getPorcentajeExclusionLocal() != null && !proveedor.getPorcentajeExclusionLocal().isEmpty()) {
            try {
                Float.parseFloat(proveedor.getPorcentajeExclusionLocal());
            }
            catch (NumberFormatException ex) {
                list.add(new MessageBean("El porcentaje de exclusi\u00f3n local debe ser num\u00e9rico.", FacesMessage.SEVERITY_ERROR));
                b = false;
            }
        }
        if (proveedor.getCondicionIVA() == null || proveedor.getCondicionIVA().isEmpty()) {
            list.add(new MessageBean("El campo Condici\u00f3n IVA es obligatorio, debe ingresar un valor.", FacesMessage.SEVERITY_ERROR));
            b = false;
        }
        if (proveedor.getImpuestoGanancias() == null || proveedor.getImpuestoGanancias().isEmpty()) {
            list.add(new MessageBean("El campo Impuesto a las Ganancias es obligatorio, debe ingresar un valor.", FacesMessage.SEVERITY_ERROR));
            b = false;
        }
        if (proveedor.getActividadPrincipal() == null || proveedor.getActividadPrincipal().isEmpty()) {
            list.add(new MessageBean("El campo Actividad Principal es obligatorio, debe ingresar un valor.", FacesMessage.SEVERITY_ERROR));
            b = false;
        }
        if (!proveedor.isSistemaGestionCalidad()) {
            if (proveedor.getRespuestaRequisitosAdecco() == null || proveedor.getRespuestaRequisitosAdecco().isEmpty()) {
                list.add(new MessageBean("Debe fundamentar la respuesta de la pregunta nro 2.", FacesMessage.SEVERITY_ERROR));
                b = false;
            }
            if (proveedor.getRespuestaProcedimientoCalidad() == null || proveedor.getRespuestaProcedimientoCalidad().isEmpty()) {
                list.add(new MessageBean("Debe fundamentar la respuesta de la pregunta nro 3.", FacesMessage.SEVERITY_ERROR));
                b = false;
            }
            if (proveedor.getRespuestaSeguimientoAviso() == null || proveedor.getRespuestaSeguimientoAviso().isEmpty()) {
                list.add(new MessageBean("Debe fundamentar la respuesta de la pregunta nro 4.", FacesMessage.SEVERITY_ERROR));
                b = false;
            }
            if (proveedor.getRespuestaReclamo() == null || proveedor.getRespuestaReclamo().isEmpty()) {
                list.add(new MessageBean("Debe fundamentar la respuesta de la pregunta nro 5.", FacesMessage.SEVERITY_ERROR));
                b = false;
            }
        }
        if (n == 0) {
            list.add(new MessageBean("El apartado de Adjuntos es obligatorio, debe ingresar un adjunto para poder enviar.", FacesMessage.SEVERITY_ERROR));
            b = false;
        }
        return b;
    }
    
    public Long getIdProveedorByCuit(final String s) {
        return this.proveedorDao.getIdProveedorByCuit(s);
    }
    
    public String validarCuit(final Proveedor proveedor) {
        try {
            final String trim = proveedor.getCuit().trim();
            if (trim == null || trim.isEmpty()) {
                return null;
            }
            if (!CuitValidator.validate(trim, CuitValidator.Formato.CON_GUIONES)) {
                return "CUIT inv\u00e1lido";
            }
            final Long idProveedorByCuit = this.getIdProveedorByCuit(trim);
            if (idProveedorByCuit != null && proveedor.getId() != idProveedorByCuit) {
                return "CUIT ya existente en el sistema";
            }
            return "";
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return "Error al validar el CUIT";
        }
    }
    
    public HashMap<String, Object> getParametros() {
        return this.parametros;
    }
    
    public void setParametros(final HashMap<String, Object> parametros) {
        this.parametros = parametros;
    }
    
    static {
        log = Logger.getLogger((Class)ProveedorService.class);
    }
}
