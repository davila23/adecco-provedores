// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.service;

import ar.com.syntagma.adecco.login.servicio.UsuarioEbsDto;
import ar.com.adecco.proveedores.daos.exception.InterfazException;
import ar.com.adecco.proveedores.model.Comprobante;
import java.util.Collection;
import java.util.Date;
import ar.com.adecco.dominio.ebs.ap.Chequera;
import java.util.Iterator;
import ar.com.adecco.proveedores.daos.exception.DaoException;
import ar.com.adecco.dominio.ebs.po.Proveedor;
import java.util.HashMap;
import java.util.List;
import ar.com.adecco.proveedores.filters.ComprobanteFilter;
import ar.com.adecco.proveedores.daos.exception.ServiceException;
import ar.com.adecco.proveedores.daos.helpers.ErrorMessage;
import ar.com.adecco.dominio.ebs.ap.Factura;
import ar.com.syntagma.adecco.login.cliente.MenuBean;
import ar.com.adecco.proveedores.daos.FacturaEbsDao;
import ar.com.adecco.proveedores.daos.ProveedorEBSDao;
import javax.inject.Inject;
import ar.com.adecco.proveedores.daos.ComprobanteDao;
import javax.inject.Named;
import java.io.Serializable;

@Named
public class FacturaEbsService implements Serializable
{
    private static final long serialVersionUID = 515772184150851800L;
    @Inject
    ComprobanteDao comprobanteDao;
    @Inject
    ComprobanteService comprobanteService;
    @Inject
    ProveedorEBSDao proveedorEBSDao;
    @Inject
    FacturaEbsDao facturaEbsDao;
    @Inject
    MenuBean menuBean;
    
    public Factura getById(final long n) throws ServiceException {
        try {
            return this.facturaEbsDao.getById(n);
        }
        catch (Exception ex) {
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex, FacturaEbsService.class.getName());
        }
    }
    
    public Factura getById(final long n, final boolean b) throws ServiceException {
        try {
            return this.facturaEbsDao.getById(n, b);
        }
        catch (Exception ex) {
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex, FacturaEbsService.class.getName());
        }
    }
    
    public List<Factura> getByFilter(final ComprobanteFilter comprobanteFilter) throws ServiceException {
        try {
            final List<Factura> byFilter = this.facturaEbsDao.getByFilter(comprobanteFilter, 300);
            if (byFilter == null) {
                throw new Exception("Ocurri\u00f3 un error interno al consultar las facturas a pagar.");
            }
            if (byFilter.size() > 300) {
                throw new Exception("Cantidad de registros encontrados supera el m\u00e1ximo permitido (300 regs.).");
            }
            if (!byFilter.isEmpty()) {
                final List<Object[]> proveedoresPorFactura = this.proveedorEBSDao.getProveedoresPorFactura(byFilter);
                if (proveedoresPorFactura == null) {
                    throw new Exception("Ocurri\u00f3 un error interno al consultar los proveedores de las facturas a pagar.");
                }
                final HashMap<Object, Proveedor> hashMap = new HashMap<Object, Proveedor>();
                for (final Object[] array : proveedoresPorFactura) {
                    hashMap.put(array[0], (Proveedor)array[1]);
                }
                for (final Factura factura : byFilter) {
                    factura.setProveedor((Proveedor)hashMap.get(factura.getId()));
                }
            }
            return (List<Factura>)byFilter;
        }
        catch (DaoException ex) {
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, FacturaEbsService.class.getName());
        }
        catch (Exception ex2) {
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, FacturaEbsService.class.getName());
        }
    }
    
    public void pagar(final List<Factura> list, final Chequera chequera, final Date date) throws ServiceException {
        if (list == null || list.isEmpty()) {
            throw new ServiceException("No hay comprobantes para pagar", ComprobanteService.class.getName());
        }
        Object proveedor = null;
        for (final Factura factura : list) {
            if (!factura.getProveedor().equals(proveedor)) {
                if (proveedor != null) {
                    throw new ServiceException("Los comprobantes deben corresponder a un mismo proveedor", ComprobanteService.class.getName());
                }
                proveedor = factura.getProveedor();
            }
        }
        try {
            final UsuarioEbsDto usuarioEbs = this.menuBean.getUsuarioEbs();
            if (usuarioEbs == null) {
                throw new Exception("No se pudo obtener el usuario de EBS");
            }
            this.facturaEbsDao.pagar((List)list, chequera, date, usuarioEbs.getId());
            final Iterator<Comprobante> iterator2 = this.comprobanteDao.getByComprobantesEbs((Collection)list).iterator();
            while (iterator2.hasNext()) {
                this.comprobanteService.pagar(iterator2.next());
            }
        }
        catch (InterfazException ex) {
            ex.printStackTrace();
            throw new ServiceException("Error encontrado al pagar " + ((list.size() > 1) ? "los comprobantes" : "el comprobante") + ": " + ex.getMessage(), FacturaEbsService.class.getName());
        }
        catch (Exception ex2) {
            ex2.printStackTrace();
            throw new ServiceException("Error encontrado al pagar " + ((list.size() > 1) ? "los comprobantes" : "el comprobante") + ": " + ex2.getMessage(), FacturaEbsService.class.getName());
        }
    }
}
