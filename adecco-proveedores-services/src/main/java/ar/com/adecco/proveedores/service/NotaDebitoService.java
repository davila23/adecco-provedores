// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.service;

import ar.com.adecco.dominio.ebs.fnd.Usuario;
import java.util.Date;
import ar.com.adecco.proveedores.model.CodigoAutorizacion;
import ar.com.adecco.proveedores.model.FacturaEstado;
import java.util.Iterator;
import java.util.Arrays;
import ar.com.adecco.proveedores.filters.commons.OrderDirection;
import ar.com.adecco.proveedores.filters.commons.OrderExpression;
import ar.com.adecco.proveedores.filters.ComprobanteFilter;
import ar.com.adecco.proveedores.filters.Paginator;
import ar.com.adecco.proveedores.model.Comprobante;
import java.util.ArrayList;
import ar.com.adecco.proveedores.bean.MessageBean;
import ar.com.adecco.proveedores.model.Linea;
import ar.com.adecco.proveedores.model.Proveedor;
import ar.com.adecco.dominio.ebs.ap.Factura;
import ar.com.adecco.proveedores.daos.helpers.ErrorMessage;
import ar.com.adecco.proveedores.daos.exception.DaoException;
import ar.com.adecco.proveedores.model.NotaDebito;
import javax.annotation.PostConstruct;
import ar.com.adecco.proveedores.daos.exception.ServiceException;
import ar.com.adecco.proveedores.model.EstadoFactura;
import java.util.List;
import ar.com.adecco.proveedores.daos.DistribucionOCDao;
import ar.com.adecco.proveedores.daos.InterfazAPDao;
import ar.com.adecco.proveedores.daos.FacturaEbsDao;
import ar.com.adecco.proveedores.daos.FacturaEstadoDao;
import ar.com.adecco.proveedores.daos.ComprobanteAdjuntoDao;
import ar.com.adecco.proveedores.daos.CompaniaDao;
import ar.com.adecco.proveedores.daos.LineaOCDao;
import ar.com.adecco.proveedores.daos.LineaDao;
import ar.com.adecco.proveedores.daos.OrdenCompraDao;
import ar.com.adecco.proveedores.daos.ProveedorEBSDao;
import ar.com.adecco.proveedores.daos.ProveedorDao;
import ar.com.adecco.proveedores.daos.ComprobanteDao;
import ar.com.adecco.proveedores.daos.NotaDebitoDao;
import javax.inject.Inject;
import ar.com.syntagma.adecco.login.cliente.MenuBean;
import org.jboss.solder.logging.Logger;
import javax.inject.Named;
import java.io.Serializable;

@Named
public class NotaDebitoService implements Serializable
{
    private static final long serialVersionUID = 125772184150851800L;
    private static final Logger log;
    @Inject
    MenuBean menuBean;
    @Inject
    EmailService emailService;
    @Inject
    EmailFactoryService emailFactoryService;
    @Inject
    PersonaRelacionProveedorService usuarioAdeccoRelacionService;
    @Inject
    ComprobanteService comprobanteService;
    @Inject
    NotaDebitoDao notaDebitoDao;
    @Inject
    ComprobanteDao comprobanteDao;
    @Inject
    ProveedorDao proveedorDao;
    @Inject
    ProveedorEBSDao proveedorEBSDao;
    @Inject
    OrdenCompraDao ordenCompraDao;
    @Inject
    LineaDao lineaDao;
    @Inject
    LineaOCDao lineaOCDao;
    @Inject
    CompaniaDao companiaDao;
    @Inject
    ComprobanteAdjuntoDao comprobanteAdjuntoDao;
    @Inject
    FacturaEstadoDao facturaEstadoDao;
    @Inject
    FacturaEbsDao facturaEbsDao;
    @Inject
    InterfazAPDao interfazApDao;
    @Inject
    DistribucionOCDao distribucionOCDao;
    private static final List<EstadoFactura> estadosAFacturar;
    
    @PostConstruct
    public void init() throws ServiceException {
    }
    
    public NotaDebito getById(final long n) throws ServiceException {
        try {
            return (NotaDebito)this.notaDebitoDao.obtener((Object)n);
        }
        catch (DaoException ex) {
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, NotaDebitoService.class.getName());
        }
        catch (Exception ex2) {
            ex2.printStackTrace();
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, NotaDebitoService.class.getName());
        }
    }
    
    public NotaDebito getById(final long n, final boolean b) throws ServiceException {
        try {
            return this.notaDebitoDao.getById(n, b);
        }
        catch (DaoException ex) {
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, NotaDebitoService.class.getName());
        }
        catch (Exception ex2) {
            ex2.printStackTrace();
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, NotaDebitoService.class.getName());
        }
    }
    
    public NotaDebito getByComprobanteEbs(final Factura factura) throws ServiceException {
        try {
            return (NotaDebito)this.comprobanteDao.getByComprobanteEbs(factura);
        }
        catch (DaoException ex) {
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, NotaDebitoService.class.getName());
        }
        catch (Exception ex2) {
            ex2.printStackTrace();
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, NotaDebitoService.class.getName());
        }
    }
    
    public List<NotaDebito> cargar(final Proveedor proveedor) throws ServiceException {
        return this.cargar(proveedor, false);
    }
    
    public List<NotaDebito> cargar(final Proveedor proveedor, final boolean b) throws ServiceException {
        try {
            return (List<NotaDebito>)this.comprobanteDao.getByProveedor(proveedor, (Class)NotaDebito.class, b);
        }
        catch (DaoException ex) {
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, NotaDebitoService.class.getName());
        }
        catch (Exception ex2) {
            ex2.printStackTrace();
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, NotaDebitoService.class.getName());
        }
    }
    
    public List<MessageBean> actualizar(final NotaDebito notaDebito, final List<Linea> list) throws ServiceException {
        final ArrayList list2 = new ArrayList();
        return this.comprobanteService.actualizar((Comprobante)notaDebito, list);
    }
    
    public List<Comprobante> getForPanelControl(final Proveedor proveedor, final Paginator paginator) {
        final ComprobanteFilter comprobanteFilter = new ComprobanteFilter();
        comprobanteFilter.setProveedorId(Long.valueOf(proveedor.getId()));
        comprobanteFilter.setEagerLoad(true);
        comprobanteFilter.setTipoComprobante(Comprobante.TipoComprobante.ND);
        comprobanteFilter.setOrderBy((List)Arrays.asList(new OrderExpression((Enum)ComprobanteFilter.OrderElement.FECHA, OrderDirection.DESC)));
        return (List<Comprobante>)this.comprobanteDao.getByFilter(comprobanteFilter, paginator);
    }
    
    private void validarFacturaAEnviar(final NotaDebito notaDebito) throws ServiceException {
        if (notaDebito == null) {
            throw new ServiceException("Error interno: Comprobante nulo", NotaDebitoService.class.getName());
        }
        if (notaDebito.getLineas() == null || notaDebito.getLineas().isEmpty()) {
            throw new ServiceException("Error interno: Comprobante sin detalle", NotaDebitoService.class.getName());
        }
        if (notaDebito.getFacturaAplicada() == null) {
            throw new ServiceException("Error interno: Comprobante sin factura aplicada", NotaDebitoService.class.getName());
        }
        final ar.com.adecco.proveedores.model.Factura facturaAplicada = notaDebito.getFacturaAplicada();
        for (final Linea linea : notaDebito.getLineas()) {
            if (linea.getTipo() == Linea.TipoLinea.ITEM) {
                if (linea.getLineaAplicada() == null) {
                    throw new ServiceException("Error interno: L\u00ednea de comprobante sin l\u00ednea de factura aplicada", NotaDebitoService.class.getName());
                }
                if (!facturaAplicada.equals((Object)linea.getLineaAplicada().getComprobante())) {
                    throw new ServiceException("Error interno: L\u00ednea de comprobante con l\u00ednea de factura aplicada distinto a la factura aplicada al comprobante", NotaDebitoService.class.getName());
                }
                continue;
            }
        }
    }
    
    public void enviar(final NotaDebito notaDebito) throws ServiceException {
        try {
            this.validarFacturaAEnviar(notaDebito);
            final FacturaEstado facturaEstado = new FacturaEstado();
            facturaEstado.setEstadoAnterior(notaDebito.getEstado());
            facturaEstado.setEstado(EstadoFactura.ENVIADA);
            notaDebito.setEstado(EstadoFactura.ENVIADA);
            facturaEstado.setComentario("Env\u00edo del comprobante");
            facturaEstado.setFactura((Comprobante)notaDebito);
            this.facturaEstadoDao.addFacturaEstado(facturaEstado);
            List<Usuario> usuariosEbsSolicitantesByOrdenCompra = null;
            int n = (facturaEstado.getEstadoAnterior() == EstadoFactura.RECHAZADA && !notaDebito.isVolverAAceptar()) ? 1 : 0;
            if (n == 0) {
                usuariosEbsSolicitantesByOrdenCompra = (List<Usuario>)this.ordenCompraDao.getUsuariosEbsSolicitantesByOrdenCompra(notaDebito.getFacturaAplicada().getOrdenCompra());
                if (usuariosEbsSolicitantesByOrdenCompra == null || usuariosEbsSolicitantesByOrdenCompra.isEmpty()) {
                    n = 1;
                }
            }
            if (n != 0) {
                final FacturaEstado facturaEstado2 = new FacturaEstado();
                facturaEstado2.setEstadoAnterior(notaDebito.getEstado());
                facturaEstado2.setEstado(EstadoFactura.ACEPTADA);
                notaDebito.setEstado(EstadoFactura.ACEPTADA);
                facturaEstado2.setComentario("Comprobante aceptado autom\u00e1ticamente");
                facturaEstado2.setFactura((Comprobante)notaDebito);
                this.facturaEstadoDao.addFacturaEstado(facturaEstado2);
            }
            if (notaDebito.getCae() == CodigoAutorizacion.CAE) {
                notaDebito.setFechaRecepcion(new Date());
            }
            this.comprobanteDao.update(notaDebito);
            if (notaDebito.getEstado().equals((Object)EstadoFactura.ENVIADA)) {
                this.sendEmailEnviada(notaDebito.getProveedor(), (Comprobante)notaDebito, usuariosEbsSolicitantesByOrdenCompra);
            }
            else if (notaDebito.getEstado().equals((Object)EstadoFactura.ACEPTADA)) {
                this.sendEmailAceptada(notaDebito.getProveedor(), (Comprobante)notaDebito);
            }
        }
        catch (DaoException ex) {
            this.notaDebitoDao.clearEntities();
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, NotaDebitoService.class.getName());
        }
        catch (Exception ex2) {
            ex2.printStackTrace();
            this.notaDebitoDao.clearEntities();
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, NotaDebitoService.class.getName());
        }
    }
    
    private void sendEmailEnviada(final Proveedor proveedor, final Comprobante comprobante, final List<Usuario> list) throws ServiceException {
        this.emailService.send(this.emailFactoryService.createEmailFacturaEnviada(proveedor, comprobante, list));
    }
    
    private void sendEmailAceptada(final Proveedor proveedor, final Comprobante comprobante) throws ServiceException {
        this.emailService.send(this.emailFactoryService.createEmailFacturaAceptada(proveedor, comprobante));
    }
    
    private void sendEmailAprobada(final Proveedor proveedor, final Comprobante comprobante) throws ServiceException {
        this.emailService.send(this.emailFactoryService.createEmailFacturaAprobada(proveedor, comprobante));
    }
    
    private void sendEmailRechazada(final Proveedor proveedor, final Comprobante comprobante, final String s) throws ServiceException {
        this.emailService.send(this.emailFactoryService.createEmailFacturaRechazada(proveedor, comprobante, s));
    }
    
    public static List<EstadoFactura> getEstadosAFacturar() {
        return NotaDebitoService.estadosAFacturar;
    }
    
    static {
        log = Logger.getLogger((Class)NotaDebitoService.class);
        estadosAFacturar = Arrays.asList(EstadoFactura.INGRESADA, EstadoFactura.ENVIADA, EstadoFactura.ACEPTADA);
    }
}
