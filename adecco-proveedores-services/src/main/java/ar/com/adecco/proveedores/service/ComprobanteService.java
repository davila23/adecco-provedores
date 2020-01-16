// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.service;

import java.util.regex.Pattern;
import javax.faces.application.FacesMessage;
import java.util.ArrayList;
import ar.com.adecco.proveedores.bean.MessageBean;
import ar.com.adecco.proveedores.model.Proveedor;
import com.google.common.base.Strings;
import ar.com.adecco.proveedores.model.EstadoComprobanteEbs;
import ar.com.syntagma.helpers.HolderHelper;
import java.util.Iterator;
import ar.com.adecco.proveedores.model.ComprobanteAdjunto;
import ar.com.adecco.proveedores.model.Linea;
import ar.com.adecco.dominio.ebs.po.OrdenCompra;
import ar.com.adecco.dominio.ebs.fnd.Usuario;
import ar.com.adecco.proveedores.daos.helpers.ErrorMessage;
import ar.com.adecco.proveedores.daos.exception.DaoException;
import java.util.Date;
import ar.com.adecco.proveedores.model.CodigoAutorizacion;
import ar.com.adecco.proveedores.model.NotaDebito;
import ar.com.adecco.proveedores.model.NotaCredito;
import ar.com.adecco.proveedores.model.Factura;
import ar.com.adecco.proveedores.model.EstadoFactura;
import ar.com.adecco.proveedores.model.FacturaEstado;
import ar.com.adecco.proveedores.filters.Paginator;
import java.util.List;
import ar.com.adecco.proveedores.filters.ComprobanteFilter;
import ar.com.adecco.proveedores.daos.exception.ServiceException;
import ar.com.adecco.proveedores.model.Comprobante;
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
import javax.inject.Inject;
import ar.com.syntagma.adecco.login.cliente.MenuBean;
import org.jboss.solder.logging.Logger;
import javax.inject.Named;
import java.io.Serializable;

@Named
public class ComprobanteService implements Serializable
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
    
    public Comprobante getById(final long n) throws ServiceException {
        return this.comprobanteDao.getById(n);
    }
    
    public <T extends Comprobante> List<T> getByFilter(final ComprobanteFilter comprobanteFilter) {
        return (List<T>)this.comprobanteDao.getByFilter(comprobanteFilter, (Paginator)null);
    }
    
    public <T extends Comprobante> List<T> getByFilter(final ComprobanteFilter comprobanteFilter, final Paginator paginator) {
        return (List<T>)this.comprobanteDao.getByFilter(comprobanteFilter, paginator);
    }
    
    public void enviar(final Comprobante comprobante) throws ServiceException {
        try {
            final FacturaEstado facturaEstado = new FacturaEstado();
            facturaEstado.setEstadoAnterior(comprobante.getEstado());
            facturaEstado.setEstado(EstadoFactura.ENVIADA);
            comprobante.setEstado(EstadoFactura.ENVIADA);
            facturaEstado.setComentario("Env\u00edo del comprobante");
            facturaEstado.setFactura(comprobante);
            this.facturaEstadoDao.addFacturaEstado(facturaEstado);
            List<Usuario> usuariosEbsSolicitantesByOrdenCompra = null;
            int n = (facturaEstado.getEstadoAnterior() == EstadoFactura.RECHAZADA && !comprobante.isVolverAAceptar()) ? 1 : 0;
            if (n == 0) {
                OrdenCompra ordenCompra = null;
                switch (comprobante.getTipoComprobante()) {
                    case FC: {
                        ordenCompra = ((Factura)comprobante).getOrdenCompra();
                        break;
                    }
                    case NC: {
                        ordenCompra = ((NotaCredito)comprobante).getFacturaAplicada().getOrdenCompra();
                        break;
                    }
                    case ND: {
                        ordenCompra = ((NotaDebito)comprobante).getFacturaAplicada().getOrdenCompra();
                        break;
                    }
                }
                usuariosEbsSolicitantesByOrdenCompra = (List<Usuario>)this.ordenCompraDao.getUsuariosEbsSolicitantesByOrdenCompra(ordenCompra);
                if (usuariosEbsSolicitantesByOrdenCompra == null || usuariosEbsSolicitantesByOrdenCompra.isEmpty()) {
                    n = 1;
                }
            }
            if (n != 0) {
                final FacturaEstado facturaEstado2 = new FacturaEstado();
                facturaEstado2.setEstadoAnterior(comprobante.getEstado());
                facturaEstado2.setEstado(EstadoFactura.ACEPTADA);
                comprobante.setEstado(EstadoFactura.ACEPTADA);
                facturaEstado2.setComentario("Comprobante aceptado autom\u00e1ticamente");
                facturaEstado2.setFactura(comprobante);
                this.facturaEstadoDao.addFacturaEstado(facturaEstado2);
            }
            if (comprobante.getCae() == CodigoAutorizacion.CAE) {
                comprobante.setFechaRecepcion(new Date());
            }
            this.comprobanteDao.update(comprobante);
            if (comprobante.getEstado().equals((Object)EstadoFactura.ENVIADA)) {
                this.sendEmailEnviada(comprobante.getProveedor(), comprobante, usuariosEbsSolicitantesByOrdenCompra);
            }
            else if (comprobante.getEstado().equals((Object)EstadoFactura.ACEPTADA)) {
                this.sendEmailAceptada(comprobante.getProveedor(), comprobante);
            }
        }
        catch (DaoException ex) {
            this.facturaEstadoDao.clearEntities();
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, ComprobanteService.class.getName());
        }
        catch (Exception ex2) {
            ex2.printStackTrace();
            this.facturaEstadoDao.clearEntities();
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, ComprobanteService.class.getName());
        }
    }
    
    public void eliminar(Comprobante byId) throws ServiceException {
        try {
            byId = this.comprobanteDao.getById(byId.getId());
            final Iterator<Linea> iterator = byId.getLineas().iterator();
            while (iterator.hasNext()) {
                this.lineaDao.removeLinea((Linea)iterator.next());
            }
            final Iterator<FacturaEstado> iterator2 = byId.getEstados().iterator();
            while (iterator2.hasNext()) {
                this.facturaEstadoDao.removeFacturaEstado((FacturaEstado)iterator2.next());
            }
            final Iterator<ComprobanteAdjunto> iterator3 = this.comprobanteAdjuntoDao.getAdjuntos(byId).iterator();
            while (iterator3.hasNext()) {
                this.comprobanteAdjuntoDao.remove((ComprobanteAdjunto)iterator3.next());
            }
            this.facturaEstadoDao.flushEntities();
            this.comprobanteDao.remove(byId);
            this.facturaEstadoDao.flushEntities();
        }
        catch (DaoException ex) {
            this.facturaEstadoDao.clearEntities();
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, ComprobanteService.class.getName());
        }
        catch (Exception ex2) {
            ex2.printStackTrace();
            this.facturaEstadoDao.clearEntities();
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, ComprobanteService.class.getName());
        }
    }
    
    public void aprobar(final Comprobante factura, final long n) throws ServiceException {
        try {
            final HolderHelper holderHelper = new HolderHelper();
            final ar.com.adecco.dominio.ebs.ap.Factura addFactura = this.facturaEbsDao.addFactura(factura, n, holderHelper);
            final EstadoComprobanteEbs estadoEbs = (addFactura == null) ? EstadoComprobanteEbs.ERROR_FACTURACION : (Strings.isNullOrEmpty((String)holderHelper.get()) ? null : EstadoComprobanteEbs.RETENIDA);
            final EstadoFactura estadoFactura = (estadoEbs == null) ? EstadoFactura.APROBADA : factura.getEstado();
            final FacturaEstado facturaEstado = new FacturaEstado();
            facturaEstado.setFactura(factura);
            facturaEstado.setEstadoAnterior(factura.getEstado());
            facturaEstado.setEstado(estadoFactura);
            facturaEstado.setComentario((String)holderHelper.get());
            factura.setComprobanteEbs(addFactura);
            factura.setEstadoEbs(estadoEbs);
            factura.setEstado(estadoFactura);
            this.interfazApDao.addComprobante(factura, (String)holderHelper.get());
            this.facturaEstadoDao.addFacturaEstado(facturaEstado);
            this.comprobanteDao.update(factura);
            ComprobanteService.log.info((Object)("Comprobante EBS " + factura.getTipoLetraNumero() + " " + ((factura.getEstadoEbs() != null) ? factura.getEstadoEbs().getNombre() : "OK")));
            if (factura.getEstado() == EstadoFactura.APROBADA) {
                this.sendEmailAprobada(factura.getProveedor(), factura);
            }
        }
        catch (Exception ex) {
            this.facturaEstadoDao.clearEntities();
            ex.printStackTrace();
            throw new ServiceException("Error encontrado al aprobar el comprobante " + factura.getTipoLetraNumero() + ": " + ex.getMessage(), ComprobanteService.class.getName());
        }
    }
    
    public void liberar(final Comprobante factura, final long n) throws ServiceException {
        try {
            final HolderHelper holderHelper = new HolderHelper();
            final EstadoComprobanteEbs estadoEbs = ((boolean)this.facturaEbsDao.liberarFactura(factura, n, holderHelper)) ? null : EstadoComprobanteEbs.RETENIDA;
            final EstadoFactura estadoFactura = (estadoEbs == null) ? EstadoFactura.APROBADA : factura.getEstado();
            final FacturaEstado facturaEstado = new FacturaEstado();
            facturaEstado.setFactura(factura);
            facturaEstado.setEstadoAnterior(factura.getEstado());
            facturaEstado.setEstado(estadoFactura);
            facturaEstado.setComentario((String)holderHelper.get());
            factura.setEstadoEbs(estadoEbs);
            factura.setEstado(estadoFactura);
            this.interfazApDao.addComprobante(factura, (String)holderHelper.get());
            this.facturaEstadoDao.addFacturaEstado(facturaEstado);
            this.comprobanteDao.update(factura);
            ComprobanteService.log.info((Object)("Comprobante EBS " + factura.getTipoLetraNumero() + " " + ((factura.getEstadoEbs() != null) ? factura.getEstadoEbs().getNombre() : "OK")));
            if (factura.getEstado() == EstadoFactura.APROBADA) {
                this.sendEmailAprobada(factura.getProveedor(), factura);
            }
        }
        catch (Exception ex) {
            this.facturaEstadoDao.clearEntities();
            ex.printStackTrace();
            throw new ServiceException("Error encontrado al liberar el comprobante " + factura.getTipoLetraNumero() + ": " + ex.getMessage(), ComprobanteService.class.getName());
        }
    }
    
    public void aceptar(final Comprobante factura) throws ServiceException {
        try {
            final FacturaEstado facturaEstado = new FacturaEstado();
            facturaEstado.setEstado(EstadoFactura.ACEPTADA);
            facturaEstado.setEstadoAnterior(factura.getEstado());
            facturaEstado.setComentario((String)null);
            facturaEstado.setFactura(factura);
            this.facturaEstadoDao.addFacturaEstado(facturaEstado);
            factura.setEstado(EstadoFactura.ACEPTADA);
            this.comprobanteDao.update(factura);
            this.sendEmailAceptada(factura.getProveedor(), factura);
        }
        catch (DaoException ex) {
            this.facturaEstadoDao.clearEntities();
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, ComprobanteService.class.getName());
        }
        catch (Exception ex2) {
            ex2.printStackTrace();
            this.facturaEstadoDao.clearEntities();
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, ComprobanteService.class.getName());
        }
    }
    
    public void rechazar(final Comprobante factura, final String comentario, final boolean volverAAceptar) throws ServiceException {
        try {
            final FacturaEstado facturaEstado = new FacturaEstado();
            facturaEstado.setEstado(EstadoFactura.RECHAZADA);
            facturaEstado.setEstadoAnterior(factura.getEstado());
            facturaEstado.setComentario(comentario);
            facturaEstado.setFactura(factura);
            this.facturaEstadoDao.addFacturaEstado(facturaEstado);
            factura.setEstado(EstadoFactura.RECHAZADA);
            factura.setEstadoEbs((EstadoComprobanteEbs)null);
            factura.setVolverAAceptar(volverAAceptar);
            this.comprobanteDao.update(factura);
            this.sendEmailRechazada(factura.getProveedor(), factura, comentario);
        }
        catch (DaoException ex) {
            this.facturaEstadoDao.clearEntities();
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, ComprobanteService.class.getName());
        }
        catch (Exception ex2) {
            ex2.printStackTrace();
            this.facturaEstadoDao.clearEntities();
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, ComprobanteService.class.getName());
        }
    }
    
    public void pagar(final Comprobante factura) throws ServiceException {
        try {
            final FacturaEstado facturaEstado = new FacturaEstado();
            facturaEstado.setEstado(EstadoFactura.PAGADA);
            facturaEstado.setEstadoAnterior(factura.getEstado());
            facturaEstado.setComentario((String)null);
            facturaEstado.setFactura(factura);
            this.facturaEstadoDao.addFacturaEstado(facturaEstado);
            factura.setEstado(EstadoFactura.PAGADA);
            this.comprobanteDao.update(factura);
        }
        catch (DaoException ex) {
            this.facturaEstadoDao.clearEntities();
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, ComprobanteService.class.getName());
        }
        catch (Exception ex2) {
            ex2.printStackTrace();
            this.facturaEstadoDao.clearEntities();
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, ComprobanteService.class.getName());
        }
    }
    
    public boolean existNroComprobanteByProveedor(final Comprobante comprobante) throws ServiceException {
        try {
            return this.comprobanteDao.existNroByProveedor(comprobante);
        }
        catch (DaoException ex) {
            ex.printStackTrace();
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, ComprobanteService.class.getName());
        }
        catch (Exception ex2) {
            ex2.printStackTrace();
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, ComprobanteService.class.getName());
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
    
    public List<MessageBean> actualizar(final Comprobante comprobante, final List<Linea> list) throws ServiceException {
        final ArrayList<MessageBean> list2 = new ArrayList<MessageBean>();
        try {
            this.fixCantidades(comprobante);
            if (this.validar(list2, comprobante)) {
                if (comprobante.getId() == 0L) {
                    this.comprobanteDao.save(comprobante);
                    for (final Linea linea : comprobante.getLineas()) {
                        linea.setComprobante(comprobante);
                        this.lineaDao.addLinea(linea);
                    }
                    final FacturaEstado facturaEstado = new FacturaEstado();
                    facturaEstado.setEstado(EstadoFactura.INGRESADA);
                    facturaEstado.setComentario("Creaci\u00f3n de la Factura");
                    facturaEstado.setFactura(comprobante);
                    this.facturaEstadoDao.addFacturaEstado(facturaEstado);
                    comprobante.getEstados().add(facturaEstado);
                }
                else {
                    for (final Linea linea2 : comprobante.getLineas()) {
                        if (linea2.getId() == 0L) {
                            linea2.setComprobante(comprobante);
                            this.lineaDao.addLinea(linea2);
                        }
                        else {
                            this.lineaDao.modifyLinea(linea2);
                        }
                    }
                    for (final Linea linea3 : list) {
                        final Linea linea4 = (Linea)this.lineaDao.obtener((Object)linea3.getId());
                        comprobante.getLineas().remove(linea3);
                        this.lineaDao.removeLinea(linea4);
                    }
                    this.comprobanteDao.update(comprobante);
                }
                this.comprobanteDao.flushEntities();
            }
            return list2;
        }
        catch (DaoException ex) {
            this.comprobanteDao.clearEntities();
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, NotaCreditoService.class.getName());
        }
        catch (ServiceException ex2) {
            this.comprobanteDao.clearEntities();
            throw ex2;
        }
        catch (Exception ex3) {
            ex3.printStackTrace();
            this.comprobanteDao.clearEntities();
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex3, NotaCreditoService.class.getName());
        }
    }
    
    public boolean validar(final List<MessageBean> list, final Comprobante comprobante) throws ServiceException {
        boolean b = true;
        if (comprobante.getNumero() == null) {
            list.add(new MessageBean("El campo N\u00famero de comprobante es obligatorio, debe ingresar un valor.", FacesMessage.SEVERITY_ERROR));
            b = false;
        }
        if (!Pattern.compile("\\d{4}-\\d{8}").matcher(comprobante.getNumero().toUpperCase()).matches()) {
            list.add(new MessageBean("El campo N\u00famero es incorrecto, el formato debe ser [XXXX-XXXXXXXX].", FacesMessage.SEVERITY_ERROR));
            b = false;
        }
        if (this.existNroByProveedor(comprobante)) {
            list.add(new MessageBean("El n\u00famero de comprobante ingresado ya existe.", FacesMessage.SEVERITY_ERROR));
            b = false;
        }
        if (comprobante.getFecha() == null) {
            list.add(new MessageBean("El campo Fecha es obligatorio, debe ingresar un valor.", FacesMessage.SEVERITY_ERROR));
            b = false;
        }
        final HolderHelper holderHelper = new HolderHelper();
        if (!this.validaImporte(comprobante.getImporte(), (HolderHelper<String>)holderHelper)) {
            list.add(new MessageBean("Importe del comprobante inv\u00e1lido: " + (String)holderHelper.get(), FacesMessage.SEVERITY_ERROR));
            b = false;
        }
        if (comprobante.getFechaVencimientoCae() == null) {
            list.add(new MessageBean("El campo Fecha Venc. C\u00f3digo Autorizaci\u00f3n es obligatorio, debe ingresar un valor.", FacesMessage.SEVERITY_ERROR));
            b = false;
        }
        else if (comprobante.getFecha() != null && comprobante.getFechaVencimientoCae().before(comprobante.getFecha())) {
            list.add(new MessageBean("El campo Fecha de vencimiento debe ser mayor o igual a la Fecha del comprobante.", FacesMessage.SEVERITY_ERROR));
            b = false;
        }
        if (comprobante.getNumeroCae() != null && !Pattern.compile("\\d{14}").matcher(comprobante.getNumeroCae().toUpperCase()).matches()) {
            list.add(new MessageBean("El campo C\u00f3digo de Autorizaci\u00f3n es incorrecto, debe ser 14 d\u00edgitos num\u00e9ricos.", FacesMessage.SEVERITY_ERROR));
            b = false;
        }
        for (final Linea linea : comprobante.getLineas()) {
            if (linea.getDescripcion().isEmpty()) {
                String s = "Descripci\u00f3n";
                if (linea.getTipo() == Linea.TipoLinea.TAX) {
                    s += " del impuesto";
                }
                if (linea.getTipo() == Linea.TipoLinea.ITEM) {
                    s += " de la l\u00ednea";
                }
                list.add(new MessageBean(s + " nro " + linea.getNumero() + " es obligatorio", FacesMessage.SEVERITY_ERROR));
                b = false;
            }
            if (!this.validaImporte(linea.getImporte(), (HolderHelper<String>)holderHelper)) {
                String s2 = "Importe";
                if (linea.getTipo() == Linea.TipoLinea.TAX) {
                    s2 += " del impuesto";
                }
                if (linea.getTipo() == Linea.TipoLinea.ITEM) {
                    s2 += " de la l\u00ednea";
                }
                list.add(new MessageBean(s2 + " nro " + linea.getNumero() + " inv\u00e1lido: " + (String)holderHelper.get(), FacesMessage.SEVERITY_ERROR));
                b = false;
            }
        }
        return b;
    }
    
    public boolean existNroByProveedor(final Comprobante comprobante) throws ServiceException {
        try {
            return this.comprobanteDao.existNroByProveedor(comprobante);
        }
        catch (DaoException ex) {
            ex.printStackTrace();
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, FacturaService.class.getName());
        }
        catch (Exception ex2) {
            ex2.printStackTrace();
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, FacturaService.class.getName());
        }
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
    
    public void fixCantidades(final Comprobante comprobante) {
        for (final Linea linea : comprobante.getLineas()) {
            if (linea.getTipo() == Linea.TipoLinea.ITEM) {
                this.cambiarCantidad(linea);
            }
        }
    }
    
    public void cambiarImporte(final Linea linea) {
        Double value = (linea.getLineaOrdenCompra() != null) ? linea.getLineaOrdenCompra().getPrecioUnitario() : linea.getLineaAplicada().getLineaOrdenCompra().getPrecioUnitario();
        if (value == null) {
            value = 0.0;
        }
        final double n = Math.rint(linea.getCantidad() * value * 100.0) / 100.0;
        linea.setImporte(Double.valueOf(n));
        linea.setCantidad(Double.valueOf((value != 0.0) ? (n / value) : 0.0));
    }
    
    public void cambiarCantidad(final Linea linea) {
        Double value = (linea.getLineaOrdenCompra() != null) ? linea.getLineaOrdenCompra().getPrecioUnitario() : ((linea.getLineaAplicada() != null) ? linea.getLineaAplicada().getLineaOrdenCompra().getPrecioUnitario() : null);
        Double n = linea.getImporte();
        if (value == null) {
            value = 0.0;
        }
        if (n == null) {
            n = 0.0;
        }
        linea.setCantidad(Double.valueOf((value != 0.0) ? (n / value) : 0.0));
    }
    
    static {
        log = Logger.getLogger((Class)ComprobanteService.class);
    }
}
