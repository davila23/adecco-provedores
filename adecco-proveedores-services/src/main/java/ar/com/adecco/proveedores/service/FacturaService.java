// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.service;

import ar.com.adecco.dominio.ebs.fnd.Usuario;
import java.util.Date;
import ar.com.adecco.proveedores.model.CodigoAutorizacion;
import ar.com.adecco.proveedores.model.FacturaEstado;
import ar.com.adecco.dominio.ebs.po.OrdenCompra;
import ar.com.syntagma.uiutils.Mensajes;
import java.util.Iterator;
import ar.com.adecco.dominio.ebs.po.DistribucionOrdenCompra;
import ar.com.adecco.dominio.ebs.po.LineaOrdenCompra;
import javax.persistence.PersistenceException;
import ar.com.adecco.proveedores.bean.MessageBean;
import ar.com.adecco.proveedores.model.Linea;
import java.util.Arrays;
import ar.com.adecco.proveedores.filters.commons.OrderDirection;
import ar.com.adecco.proveedores.filters.commons.OrderExpression;
import ar.com.adecco.proveedores.model.Comprobante;
import ar.com.adecco.proveedores.filters.ComprobanteFilter;
import ar.com.adecco.proveedores.filters.Paginator;
import ar.com.adecco.proveedores.model.Proveedor;
import ar.com.adecco.proveedores.daos.helpers.ErrorMessage;
import ar.com.adecco.proveedores.daos.exception.DaoException;
import ar.com.adecco.proveedores.model.Factura;
import javax.annotation.PostConstruct;
import ar.com.adecco.proveedores.daos.exception.ServiceException;
import ar.com.adecco.proveedores.model.EstadoFactura;
import java.util.List;
import java.util.HashMap;
import ar.com.adecco.proveedores.daos.DistribucionOCDao;
import ar.com.adecco.proveedores.daos.InterfazAPDao;
import ar.com.adecco.proveedores.daos.FacturaEbsDao;
import ar.com.adecco.proveedores.daos.FacturaEstadoDao;
import ar.com.adecco.proveedores.daos.ComprobanteDao;
import ar.com.adecco.proveedores.daos.ComprobanteAdjuntoDao;
import ar.com.adecco.proveedores.daos.CompaniaDao;
import ar.com.adecco.proveedores.daos.LineaOCDao;
import ar.com.adecco.proveedores.daos.LineaDao;
import ar.com.adecco.proveedores.daos.OrdenCompraDao;
import ar.com.adecco.proveedores.daos.ProveedorEBSDao;
import ar.com.adecco.proveedores.daos.ProveedorDao;
import ar.com.adecco.proveedores.daos.FacturaDao;
import javax.inject.Inject;
import ar.com.syntagma.adecco.login.cliente.MenuBean;
import org.jboss.solder.logging.Logger;
import javax.inject.Named;
import java.io.Serializable;

@Named
public class FacturaService implements Serializable
{
    private static final long serialVersionUID = 515772184150851800L;
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
    FacturaDao facturaDao;
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
    ComprobanteDao comprobanteDao;
    @Inject
    FacturaEstadoDao facturaEstadoDao;
    @Inject
    FacturaEbsDao facturaEbsDao;
    @Inject
    InterfazAPDao interfazApDao;
    @Inject
    DistribucionOCDao distribucionOCDao;
    @Inject
    ComprobanteService comprobanteService;
    private HashMap<String, Object> parametros;
    private static final List<EstadoFactura> estadosAFacturar;
    
    public FacturaService() {
        this.parametros = new HashMap<String, Object>();
    }
    
    @PostConstruct
    public void init() throws ServiceException {
        this.parametros.clear();
    }
    
    public Factura getById(final long n) throws ServiceException {
        return this.getById(n, false);
    }
    
    public Factura getById(final long n, final boolean b) throws ServiceException {
        try {
            return this.facturaDao.getById(n, b);
        }
        catch (DaoException ex) {
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, FacturaService.class.getName());
        }
        catch (Exception ex2) {
            ex2.printStackTrace();
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, FacturaService.class.getName());
        }
    }
    
    public Factura getEntByFacturaEbs(final ar.com.adecco.dominio.ebs.ap.Factura factura) throws ServiceException {
        try {
            return this.facturaDao.getFacturaByFacturaEbs(factura);
        }
        catch (DaoException ex) {
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, FacturaService.class.getName());
        }
        catch (Exception ex2) {
            ex2.printStackTrace();
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, FacturaService.class.getName());
        }
    }
    
    public List<Comprobante> getForPanelControl(final Proveedor proveedor, final Paginator paginator) {
        final ComprobanteFilter comprobanteFilter = new ComprobanteFilter();
        comprobanteFilter.setProveedorId(Long.valueOf(proveedor.getId()));
        comprobanteFilter.setEagerLoad(true);
        comprobanteFilter.setTipoComprobante(Comprobante.TipoComprobante.FC);
        comprobanteFilter.setOrderBy((List)Arrays.asList(new OrderExpression((Enum)ComprobanteFilter.OrderElement.FECHA, OrderDirection.DESC)));
        return this.comprobanteDao.getByFilter(comprobanteFilter, paginator);
    }
    
    public List<Comprobante> getForAplicarComprobante(final Proveedor proveedor, final Paginator paginator) {
        final ComprobanteFilter comprobanteFilter = new ComprobanteFilter();
        comprobanteFilter.setProveedorId(Long.valueOf(proveedor.getId()));
        comprobanteFilter.setEagerLoad(true);
        comprobanteFilter.setTipoComprobante(Comprobante.TipoComprobante.FC);
        comprobanteFilter.setOrderBy((List)Arrays.asList(new OrderExpression((Enum)ComprobanteFilter.OrderElement.FECHA, OrderDirection.DESC)));
        return this.comprobanteDao.getByFilter(comprobanteFilter, paginator);
    }
    
    public List<MessageBean> actualizarFactura(final Factura factura, final List<Linea> list) throws ServiceException {
        return this.comprobanteService.actualizar((Comprobante)factura, list);
    }
    
    public void flushEntities() throws ServiceException {
        try {
            this.facturaDao.flushEntities();
        }
        catch (PersistenceException ex) {
            this.facturaDao.clearEntities();
            throw new ServiceException(ex.getMessage(), (Exception)ex, FacturaService.class.getName());
        }
        catch (Exception ex2) {
            ex2.printStackTrace();
            this.facturaDao.clearEntities();
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, FacturaService.class.getName());
        }
    }
    
    public Double calcularImporteNoFacturado(final LineaOrdenCompra lineaOrdenCompra) throws ServiceException {
        final Double value = lineaOrdenCompra.getPrecioUnitario() * lineaOrdenCompra.getCantidad();
        List<DistribucionOrdenCompra> distribucionesByLineaOC;
        try {
            distribucionesByLineaOC = this.distribucionOCDao.getDistribucionesByLineaOC(lineaOrdenCompra);
            if (distribucionesByLineaOC == null) {
                throw new Exception("Lista null");
            }
        }
        catch (Exception ex) {
            throw new ServiceException("Error interno: no se pueden obtener las distribuciones de la l\u00ednea de OC asociada.", FacturaService.class.getName());
        }
        Double n = 0.0;
        for (final DistribucionOrdenCompra distribucionOrdenCompra : distribucionesByLineaOC) {
            if (distribucionOrdenCompra.getImporteFacturado() != null) {
                n += distribucionOrdenCompra.getImporteFacturado();
            }
        }
        return value - n;
    }
    
    public Double calcularImportePendienteDeFacturar(final LineaOrdenCompra lineaOrdenCompra) throws ServiceException {
        return this.calcularImportePendienteDeFacturar(lineaOrdenCompra, null);
    }
    
    public Double calcularImportePendienteDeFacturar(final LineaOrdenCompra lineaOrdenCompra, final Linea linea) throws ServiceException {
        Double n = 0.0;
        List<Linea> lineasPorLineaOC;
        try {
            lineasPorLineaOC = this.lineaDao.getLineasPorLineaOC(lineaOrdenCompra);
            if (lineasPorLineaOC == null) {
                throw new Exception("Lista null");
            }
        }
        catch (Exception ex) {
            throw new ServiceException("Error interno: no se pueden obtener las l\u00edneas asociadas a la l\u00ednea de OC", FacturaService.class.getName());
        }
        for (final Linea linea2 : lineasPorLineaOC) {
            if (linea2.equals((Object)linea)) {
                continue;
            }
            if (!FacturaService.estadosAFacturar.contains(linea2.getComprobante().getEstado())) {
                continue;
            }
            n += linea2.getImporte();
        }
        return n;
    }
    
    private void validarFacturaAEnviar(final Factura factura) throws ServiceException {
        if (factura == null) {
            throw new ServiceException("Error interno: Factura nula", FacturaService.class.getName());
        }
        if (factura.getLineas() == null || factura.getLineas().isEmpty()) {
            throw new ServiceException("Error interno: Factura sin l\u00edneas de factura", FacturaService.class.getName());
        }
        if (factura.getOrdenCompra() == null) {
            throw new ServiceException("Error interno: Factura sin OC asociado", FacturaService.class.getName());
        }
        final OrdenCompra ordenCompra = factura.getOrdenCompra();
        List lineasByOC;
        try {
            lineasByOC = this.lineaOCDao.getLineasByOC(ordenCompra);
        }
        catch (DaoException ex) {
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, FacturaService.class.getName());
        }
        if (lineasByOC == null || lineasByOC.isEmpty()) {
            throw new ServiceException("Error interno: OC asociado sin l\u00edneas de OC", FacturaService.class.getName());
        }
        for (final Linea linea : factura.getLineas()) {
            if (linea.getTipo() == Linea.TipoLinea.ITEM) {
                if (linea.getLineaOrdenCompra() == null) {
                    throw new ServiceException("Error interno: L\u00ednea de factura sin l\u00ednea de OC asociado", FacturaService.class.getName());
                }
                final LineaOrdenCompra lineaOrdenCompra = linea.getLineaOrdenCompra();
                if (!ordenCompra.equals(lineaOrdenCompra.getOrdenCompra())) {
                    throw new ServiceException("Error interno: L\u00ednea de factura con l\u00ednea de OC asociado distinto al OC asociado a la factura", FacturaService.class.getName());
                }
                final Double calcularImporteNoFacturado = this.calcularImporteNoFacturado(lineaOrdenCompra);
                final Double calcularImportePendienteDeFacturar = this.calcularImportePendienteDeFacturar(lineaOrdenCompra, linea);
                final Double value = calcularImporteNoFacturado - calcularImportePendienteDeFacturar;
                if (linea.getImporte() > value) {
                    throw new ServiceException("El importe de la l\u00ednea nro. " + linea.getNumero() + " supera el importe restante por facturar de la l\u00ednea de la OC asociada. " + "Importe de l\u00ednea: " + Mensajes.doubleToCurrency((double)linea.getImporte()) + ", " + "restante por facturar: " + Mensajes.doubleToCurrency((double)value) + " " + "(importe sin facturar: " + Mensajes.doubleToCurrency((double)calcularImporteNoFacturado) + ", " + "importe pendiente de facturar: " + Mensajes.doubleToCurrency((double)calcularImportePendienteDeFacturar) + ")", FacturaService.class.getName());
                }
                continue;
            }
        }
    }
    
    public void enviarFactura(final Factura factura) throws ServiceException {
        try {
            this.validarFacturaAEnviar(factura);
            final FacturaEstado facturaEstado = new FacturaEstado();
            facturaEstado.setEstadoAnterior(factura.getEstado());
            facturaEstado.setEstado(EstadoFactura.ENVIADA);
            factura.setEstado(EstadoFactura.ENVIADA);
            facturaEstado.setComentario("Env\u00edo de la factura");
            facturaEstado.setFactura((Comprobante)factura);
            this.facturaEstadoDao.addFacturaEstado(facturaEstado);
            List<Usuario> usuariosEbsSolicitantesByOrdenCompra = null;
            int n = (facturaEstado.getEstadoAnterior() == EstadoFactura.RECHAZADA && !factura.isVolverAAceptar()) ? 1 : 0;
            if (n == 0) {
                usuariosEbsSolicitantesByOrdenCompra = (List<Usuario>)this.ordenCompraDao.getUsuariosEbsSolicitantesByOrdenCompra(factura.getOrdenCompra());
                if (usuariosEbsSolicitantesByOrdenCompra == null || usuariosEbsSolicitantesByOrdenCompra.isEmpty()) {
                    n = 1;
                }
            }
            if (n != 0) {
                final FacturaEstado facturaEstado2 = new FacturaEstado();
                facturaEstado2.setEstadoAnterior(factura.getEstado());
                facturaEstado2.setEstado(EstadoFactura.ACEPTADA);
                factura.setEstado(EstadoFactura.ACEPTADA);
                facturaEstado2.setComentario("Factura aceptada autom\u00e1ticamente");
                facturaEstado2.setFactura((Comprobante)factura);
                this.facturaEstadoDao.addFacturaEstado(facturaEstado2);
            }
            if (factura.getCae() == CodigoAutorizacion.CAE) {
                factura.setFechaRecepcion(new Date());
            }
            this.facturaDao.modifyFactura(factura);
            this.facturaDao.flushEntities();
            if (factura.getEstado().equals((Object)EstadoFactura.ENVIADA)) {
                this.sendEmailEnviada(factura.getProveedor(), factura, usuariosEbsSolicitantesByOrdenCompra);
            }
            else if (factura.getEstado().equals((Object)EstadoFactura.ACEPTADA)) {
                this.sendEmailAceptada(factura.getProveedor(), factura);
            }
        }
        catch (DaoException ex) {
            this.facturaDao.clearEntities();
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, FacturaService.class.getName());
        }
        catch (Exception ex2) {
            ex2.printStackTrace();
            this.facturaDao.clearEntities();
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, FacturaService.class.getName());
        }
    }
    
    public void pagar(final Factura factura) throws ServiceException {
        try {
            final FacturaEstado facturaEstado = new FacturaEstado();
            facturaEstado.setEstado(EstadoFactura.PAGADA);
            facturaEstado.setEstadoAnterior(factura.getEstado());
            facturaEstado.setComentario("Factura Pagada");
            facturaEstado.setFactura((Comprobante)factura);
            this.facturaEstadoDao.addFacturaEstado(facturaEstado);
            factura.setEstado(EstadoFactura.PAGADA);
            this.facturaDao.modifyFactura(factura);
            this.facturaDao.flushEntities();
        }
        catch (DaoException ex) {
            this.facturaDao.clearEntities();
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, FacturaService.class.getName());
        }
        catch (Exception ex2) {
            ex2.printStackTrace();
            this.facturaDao.clearEntities();
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, FacturaService.class.getName());
        }
    }
    
    private void sendEmailEnviada(final Proveedor proveedor, final Factura factura, final List<Usuario> list) throws ServiceException {
        this.emailService.send(this.emailFactoryService.createEmailFacturaEnviada(proveedor, (Comprobante)factura, list));
    }
    
    private void sendEmailAceptada(final Proveedor proveedor, final Factura factura) throws ServiceException {
        this.emailService.send(this.emailFactoryService.createEmailFacturaAceptada(proveedor, (Comprobante)factura));
    }
    
    private void sendEmailAprobada(final Proveedor proveedor, final Factura factura) throws ServiceException {
        this.emailService.send(this.emailFactoryService.createEmailFacturaAprobada(proveedor, (Comprobante)factura));
    }
    
    private void sendEmailRechazada(final Proveedor proveedor, final Factura factura, final String s) throws ServiceException {
        this.emailService.send(this.emailFactoryService.createEmailFacturaRechazada(proveedor, (Comprobante)factura, s));
    }
    
    public Linea createLinea(final Linea.TipoLinea tipoLinea, final Factura factura, final LineaOrdenCompra lineaOrdenCompra) {
        return this.createLinea(tipoLinea, factura, lineaOrdenCompra, null);
    }
    
    public Linea createLinea(final Linea.TipoLinea tipo, final Factura comprobante, final LineaOrdenCompra lineaOrdenCompra, final Linea linea) {
        final Linea linea2 = new Linea();
        linea2.setTipo(tipo);
        linea2.setLineaOrdenCompra(lineaOrdenCompra);
        linea2.setDescripcion(lineaOrdenCompra.getDescripcionArticulo());
        linea2.setComprobante((Comprobante)comprobante);
        linea2.setCompania(comprobante.getOrdenCompra().getCompania());
        try {
            final Double calcularImporteNoFacturado = this.calcularImporteNoFacturado(lineaOrdenCompra);
            final Double calcularImportePendienteDeFacturar = this.calcularImportePendienteDeFacturar(lineaOrdenCompra, linea);
            final Double value = calcularImporteNoFacturado - calcularImportePendienteDeFacturar;
            linea2.setImporte(value);
            linea2.setCantidad(Double.valueOf(value / lineaOrdenCompra.getPrecioUnitario()));
            System.out.println("lineaOC: " + lineaOrdenCompra.getNumeroLinea() + " - importe: " + lineaOrdenCompra.getPrecioUnitario() * lineaOrdenCompra.getCantidad() + " - importeNoFacturado: " + calcularImporteNoFacturado + " - importePendienteDeFacturar: " + calcularImportePendienteDeFacturar + " - importePorFacturarConPendientes: " + value + " - linea.getCantidad(): " + linea2.getCantidad());
        }
        catch (ServiceException ex) {
            linea2.setImporte(Double.valueOf(lineaOrdenCompra.getPrecioUnitario() * lineaOrdenCompra.getCantidad()));
            linea2.setCantidad(lineaOrdenCompra.getCantidad());
            System.out.println("createLinea: Error al determinar saldo restante para linea OC " + lineaOrdenCompra.getId() + ": " + ex.getMessage());
            ex.printStackTrace();
        }
        return linea2;
    }
    
    public HashMap<String, Object> getParametros() {
        return this.parametros;
    }
    
    public void setParametros(final HashMap<String, Object> parametros) {
        this.parametros = parametros;
    }
    
    public static List<EstadoFactura> getEstadosAFacturar() {
        return FacturaService.estadosAFacturar;
    }
    
    static {
        log = Logger.getLogger((Class)FacturaService.class);
        estadosAFacturar = Arrays.asList(EstadoFactura.INGRESADA, EstadoFactura.ENVIADA, EstadoFactura.ACEPTADA);
    }
}
