// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.service;

import ar.com.adecco.proveedores.filters.Paginator;
import ar.com.adecco.proveedores.filters.OrdenCompraFilter;
import ar.com.adecco.dominio.ebs.po.LineaOrdenCompra;
import ar.com.adecco.proveedores.model.Factura;
import java.util.Iterator;
import java.util.HashMap;
import ar.com.adecco.proveedores.bean.ImportesBean;
import java.util.Map;
import ar.com.adecco.proveedores.daos.helpers.ErrorMessage;
import ar.com.adecco.proveedores.daos.exception.DaoException;
import ar.com.adecco.proveedores.daos.exception.ServiceException;
import ar.com.adecco.proveedores.model.Proveedor;
import ar.com.adecco.dominio.ebs.po.OrdenCompra;
import java.util.List;
import ar.com.adecco.proveedores.daos.ProveedorDao;
import ar.com.adecco.proveedores.daos.LineaOCDao;
import ar.com.adecco.proveedores.daos.FacturaDao;
import ar.com.adecco.proveedores.daos.OrdenCompraDao;
import javax.inject.Inject;
import java.security.Principal;
import javax.inject.Named;
import java.io.Serializable;

@Named
public class OrdenCompraService implements Serializable
{
    private static final long serialVersionUID = -1886111281115427347L;
    @Inject
    Principal principal;
    @Inject
    OrdenCompraDao ordenCompraDao;
    @Inject
    FacturaDao facturaDao;
    @Inject
    LineaOCDao lineaOCDao;
    @Inject
    ProveedorDao proveedorDao;
    
    public List<OrdenCompra> obtenerOrdenesCompraAprobadas(final long n) throws ServiceException {
        try {
            return (List<OrdenCompra>)this.ordenCompraDao.getOCAprobadasByProveedor(((Proveedor)this.proveedorDao.obtener((Object)n)).getProveedorEBS());
        }
        catch (DaoException ex) {
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, OrdenCompraService.class.getName());
        }
        catch (Exception ex2) {
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, OrdenCompraService.class.getName());
        }
    }
    
    public List<OrdenCompra> obtenerOrdenesCompraAbiertas(final long n) throws ServiceException {
        try {
            return (List<OrdenCompra>)this.ordenCompraDao.getOCAbiertasByProveedor(((Proveedor)this.proveedorDao.obtener((Object)n)).getProveedorEBS());
        }
        catch (DaoException ex) {
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, OrdenCompraService.class.getName());
        }
        catch (Exception ex2) {
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, OrdenCompraService.class.getName());
        }
    }
    
    public Map<Long, ImportesBean> obtenerImportesPorOC(final List<OrdenCompra> list) throws ServiceException {
        try {
            final HashMap<Long, ImportesBean> hashMap = new HashMap<Long, ImportesBean>();
            for (final Object[] array : this.ordenCompraDao.getImportesPorOC(list)) {
                final Long n = (Long)array[0];
                ImportesBean importesBean = (ImportesBean) hashMap.get(n);
                if (importesBean == null) {
                    System.out.println("ordenCompraDao.getImportesPorOC: nuevo importe: " + n);
                    importesBean = new ImportesBean();
                    hashMap.put(n, importesBean);
                }
                importesBean.setImporte((Double)array[1]);
                importesBean.setImporteFacturado((Double)array[2]);
            }
            for (final Object[] array2 : this.facturaDao.getImportesPorOC(list, FacturaService.getEstadosAFacturar())) {
                final Long n2 = (Long)array2[0];
                final ImportesBean importesBean2 = (ImportesBean) hashMap.get(n2);
                if (importesBean2 == null) {
                    System.out.println("Se detect\u00f3 una OC en Proveedores que no fue detectada en EBS. Id: " + n2 + ". Se ignora.");
                }
                else {
                    importesBean2.setImporteAFacturar((Double)array2[1]);
                }
            }
            for (final ImportesBean importesBean3 : hashMap.values()) {
                System.out.println("importes: " + importesBean3.getImporte() + " - " + importesBean3.getImporteAFacturar() + " - " + importesBean3.getImporteFacturado() + " - " + importesBean3.getImporteNoFacturado() + " - " + importesBean3.getSaldoRestante());
            }
            for (final ImportesBean importesBean4 : hashMap.values()) {
                if (importesBean4.getImporteFacturado() == null) {
                    importesBean4.setImporteFacturado(0.0);
                }
                if (importesBean4.getImporteAFacturar() == null) {
                    importesBean4.setImporteAFacturar(0.0);
                }
                importesBean4.setImporteNoFacturado(importesBean4.getImporte() - importesBean4.getImporteFacturado());
                importesBean4.setSaldoRestante(importesBean4.getImporteNoFacturado() - importesBean4.getImporteAFacturar());
            }
            return hashMap;
        }
        catch (DaoException ex) {
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, OrdenCompraService.class.getName());
        }
        catch (Exception ex2) {
            throw new ServiceException(ex2, OrdenCompraService.class.getName());
        }
    }
    
    public List<OrdenCompra> cargarOC(final long n) throws ServiceException {
        try {
            return (List<OrdenCompra>)this.ordenCompraDao.getOCByProveedorEBS(((Proveedor)this.proveedorDao.obtener((Object)n)).getProveedorEBS());
        }
        catch (DaoException ex) {
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, OrdenCompraService.class.getName());
        }
        catch (Exception ex2) {
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, OrdenCompraService.class.getName());
        }
    }
    
    public OrdenCompra getById(final long n) throws ServiceException {
        try {
            return this.ordenCompraDao.getById(n);
        }
        catch (DaoException ex) {
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, OrdenCompraService.class.getName());
        }
        catch (Exception ex2) {
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, OrdenCompraService.class.getName());
        }
    }
    
    public OrdenCompra getById(final long n, final boolean b) throws ServiceException {
        if (!b) {
            return this.getById(n);
        }
        try {
            return this.ordenCompraDao.getByIdEagerLoad(n);
        }
        catch (DaoException ex) {
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, OrdenCompraService.class.getName());
        }
        catch (Exception ex2) {
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, OrdenCompraService.class.getName());
        }
    }
    
    public List<Factura> cargarFacturasAsociadas(final OrdenCompra ordenCompra) throws ServiceException {
        try {
            return (List<Factura>)this.facturaDao.getFacturasByOC(ordenCompra);
        }
        catch (DaoException ex) {
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, OrdenCompraService.class.getName());
        }
        catch (Exception ex2) {
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, OrdenCompraService.class.getName());
        }
    }
    
    public List<LineaOrdenCompra> cargarLineasOC(final OrdenCompra ordenCompra) throws ServiceException {
        try {
            return (List<LineaOrdenCompra>)this.lineaOCDao.getLineasByOC(ordenCompra);
        }
        catch (DaoException ex) {
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, OrdenCompraService.class.getName());
        }
        catch (Exception ex2) {
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, OrdenCompraService.class.getName());
        }
    }
    
    public List<OrdenCompra> getByFilter(final OrdenCompraFilter ordenCompraFilter, final Paginator paginator) {
        return (List<OrdenCompra>)this.ordenCompraDao.getByFilter(ordenCompraFilter, paginator);
    }
}
