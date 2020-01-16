// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.daos;

import java.util.Collections;
import java.util.HashMap;
import ar.com.adecco.proveedores.daos.base.CustomQuery;
import ar.com.adecco.proveedores.filters.Paginator;
import ar.com.adecco.dominio.ebs.fnd.Usuario;
import java.util.List;
import ar.com.adecco.dominio.ebs.po.Proveedor;
import ar.com.adecco.proveedores.daos.exception.DaoException;
import ar.com.adecco.proveedores.daos.helpers.ErrorMessage;
import ar.com.adecco.proveedores.filters.OrdenCompraFilter;
import java.util.Map;
import javax.inject.Named;
import ar.com.adecco.dominio.ebs.po.OrdenCompra;
import ar.com.adecco.proveedores.daos.base.AbstractEntityDao;

@Named
public class OrdenCompraDao extends AbstractEntityDao<OrdenCompra>
{
    private static final long serialVersionUID = 3935401866069573921L;
    private static final Map<OrdenCompraFilter.OrderElement, String> filterOrderByMap;
    
    public OrdenCompra getById(final long n) throws DaoException {
        try {
            return (OrdenCompra)this.entityManager.find((Class)OrdenCompra.class, (Object)n);
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, OrdenCompra.class.getName());
        }
    }
    
    public OrdenCompra getByIdEagerLoad(final long n) throws DaoException {
        try {
            return (OrdenCompra)this.entityManager.createQuery("select oc from OrdenCompra oc join fetch oc.proveedor join fetch oc.moneda where oc.id = :id", (Class)OrdenCompra.class).setParameter("id", (Object)n).getSingleResult();
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, OrdenCompra.class.getName());
        }
    }
    
    public List<OrdenCompra> getOCByProveedorEBS(final Proveedor proveedor) throws DaoException {
        List resultList;
        try {
            resultList = this.entityManager.createQuery("from " + this.getEntityClass().getName() + " e where e.proveedor = :proveedor order by e.id desc").setParameter("proveedor", (Object)proveedor).getResultList();
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getEntityClass().getName());
        }
        return (List<OrdenCompra>)resultList;
    }
    
    public List<OrdenCompra> getOCAprobadasByProveedor(final Proveedor proveedor) throws DaoException {
        List resultList;
        try {
            resultList = this.entityManager.createQuery("from " + this.getEntityClass().getName() + " e where e.aprobado = 'Y' and e.proveedor = :proveedor order by e.id desc").setParameter("proveedor", (Object)proveedor).getResultList();
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getEntityClass().getName());
        }
        return (List<OrdenCompra>)resultList;
    }
    
    public List<OrdenCompra> getOCAbiertasByProveedor(final Proveedor proveedor) throws DaoException {
        List resultList;
        try {
            resultList = this.entityManager.createQuery("from " + this.getEntityClass().getName() + " e where e.aprobado = 'Y' and e.fechaCierre IS NULL and e.proveedor = :proveedor order by e.id desc").setParameter("proveedor", (Object)proveedor).getResultList();
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getEntityClass().getName());
        }
        return (List<OrdenCompra>)resultList;
    }
    
    public List<Object[]> getImportesPorOC(final List<OrdenCompra> list) throws DaoException {
        List resultList;
        try {
            resultList = this.entityManager.createQuery("select oc.id, (select sum(loc.cantidad * loc.precioUnitario) from LineaOrdenCompra loc where loc.ordenCompra = oc), (select sum(doc.importeFacturado) from DistribucionOrdenCompra doc where doc.ordenCompra = oc) from OrdenCompra oc where oc in (:listaOC) group by oc.id").setParameter("listaOC", (Object)list).getResultList();
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getEntityClass().getName());
        }
        return (List<Object[]>)resultList;
    }
    
    public List<Usuario> getUsuariosEbsSolicitantesByOrdenCompra(final OrdenCompra ordenCompra) throws DaoException {
        try {
            return (List<Usuario>)this.entityManager.createQuery("select u from Usuario u where u.empleado.id in (select doc.distribucionSolicitud.lineaSolicitud.solicitud.empleado.id from DistribucionOrdenCompra doc where doc.ordenCompra = :oc)", (Class)Usuario.class).setParameter("oc", (Object)ordenCompra).getResultList();
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getEntityClass().getName());
        }
    }
    
    public List<OrdenCompra> getByFilter(final OrdenCompraFilter ordenCompraFilter, final Paginator paginator) {
        final CustomQuery customQuery = new CustomQuery();
        customQuery.setSelect("oc");
        customQuery.setFrom("OrdenCompra oc");
        final StringBuilder sb = new StringBuilder();
        final HashMap<String, Object> params = new HashMap<String, Object>();
        this.buildWhereAndParams(ordenCompraFilter, sb, params);
        customQuery.setWhere(sb.toString());
        customQuery.setParams(params);
        customQuery.setOrderBy(this.buildOrderBy(ordenCompraFilter.getOrderBy(), OrdenCompraDao.filterOrderByMap));
        customQuery.setPaginator(paginator);
        return (List<OrdenCompra>) this.getByQuery(customQuery);
    }
    
    private void buildWhereAndParams(final OrdenCompraFilter ordenCompraFilter, final StringBuilder sb, final Map<String, Object> map) {
        sb.append(" and oc.aprobado = 'Y'");
        if (ordenCompraFilter.getProveedorEbsId() != null) {
            sb.append(" and oc.proveedor.id = :prId");
            map.put("prId", ordenCompraFilter.getProveedorEbsId());
        }
        if (ordenCompraFilter.getAbierta() != null) {
            if (ordenCompraFilter.getAbierta()) {
                sb.append(" and oc.fechaCierre is null");
            }
            else {
                sb.append(" and oc.fechaCierre is not null");
            }
        }
    }
    
    static {
        final HashMap<OrdenCompraFilter.OrderElement, String> hashMap = new HashMap<OrdenCompraFilter.OrderElement, String>();
        hashMap.put(OrdenCompraFilter.OrderElement.NUMERO, "oc.numero");
        hashMap.put(OrdenCompraFilter.OrderElement.CREATIONDATE, "oc.creationDate");
        hashMap.put(OrdenCompraFilter.OrderElement.FECHAAPROBACION, "oc.fechaAprobacion");
        filterOrderByMap = Collections.unmodifiableMap(hashMap);
    }
}
