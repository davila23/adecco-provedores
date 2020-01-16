// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.daos;

import java.util.Collections;
import com.google.common.base.Strings;
import java.util.HashMap;
import ar.com.adecco.proveedores.daos.base.CustomQuery;
import ar.com.adecco.proveedores.filters.Paginator;
import java.util.Iterator;
import javax.persistence.Query;
import java.util.Collection;
import ar.com.adecco.proveedores.daos.exception.DaoException;
import ar.com.adecco.proveedores.daos.helpers.ErrorMessage;
import ar.com.adecco.proveedores.model.Factura;
import java.util.List;
import ar.com.adecco.proveedores.model.Proveedor;
import javax.persistence.NoResultException;
import ar.com.adecco.proveedores.filters.ComprobanteFilter;
import java.util.Map;
import javax.inject.Named;
import ar.com.adecco.proveedores.model.Comprobante;
import ar.com.adecco.proveedores.daos.base.AbstractEntityDao;

@Named
public class ComprobanteDao extends AbstractEntityDao<Comprobante>
{
    private static final long serialVersionUID = 5500601207829276101L;
    private static final Map<ComprobanteFilter.OrderElement, String> filterOrderByMap;
    private List<Comprobante> resultList;
    
    public Comprobante getById(final long n) {
        try {
            return (Comprobante)this.entityManager.createQuery("select c from ar.com.adecco.proveedores.model.Comprobante c where c.id = :id", (Class)Comprobante.class).setParameter("id", (Object)n).getSingleResult();
        }
        catch (NoResultException ex) {
            return null;
        }
    }
    
    public <T extends Comprobante> List<T> getByProveedor(final Proveedor proveedor, final Class<T> clazz, final boolean b) throws DaoException {
        try {
            if (!b) {
                return (List<T>)this.entityManager.createQuery("from " + clazz.getName() + " c " + "where c.proveedor = :proveedor " + "order by c.fechaRecepcion desc", (Class)clazz).setParameter("proveedor", (Object)proveedor).getResultList();
            }
            if (clazz.equals(Factura.class)) {
                return (List<T>)this.entityManager.createQuery("from " + clazz.getName() + " c " + "left join fetch c.proveedor pr " + "left join fetch c.ordenCompra oc " + "where pr = :proveedor " + "order by e.fechaRecepcion desc", (Class)clazz).setParameter("proveedor", (Object)proveedor).getResultList();
            }
            return (List<T>)this.entityManager.createQuery("from " + clazz.getName() + " c " + "left join fetch c.proveedor pr " + "left join fetch c.facturaAplicada fc " + "where pr = :proveedor " + "order by c.fechaRecepcion desc", (Class)clazz).setParameter("proveedor", (Object)proveedor).getResultList();
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getEntityClass().getName());
        }
    }
    
    public Comprobante getByComprobanteEbs(final ar.com.adecco.dominio.ebs.ap.Factura factura) throws DaoException {
        try {
            return (Comprobante)this.entityManager.createQuery("select c from ar.com.adecco.proveedores.model.Comprobante c where c.comprobanteEbs = :cEbs", (Class)Comprobante.class).setParameter("cEbs", (Object)factura).getSingleResult();
        }
        catch (NoResultException ex2) {
            return null;
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getEntityClass().getName());
        }
    }
    
    public List<Comprobante> getByComprobantesEbs(final Collection<ar.com.adecco.dominio.ebs.ap.Factura> collection) {
        if (collection == null || collection.isEmpty()) {
            return null;
        }
        return (List<Comprobante>)this.entityManager.createQuery("select c from ar.com.adecco.proveedores.model.Comprobante c left join fetch c.comprobanteEbs where c.comprobanteEbs in :cEbs", (Class)Comprobante.class).setParameter("cEbs", (Object)collection).getResultList();
    }
    
    public boolean existNroByProveedor(final Comprobante comprobante) throws DaoException {
        boolean b = false;
        try {
            final Query query = this.entityManager.createQuery("select e from ar.com.adecco.proveedores.model.Comprobante e where e.proveedor = :proveedor and e.numero = :nroFactura and e.letra = :letra and TYPE(e) = :class");
            query.setParameter("proveedor", (Object)comprobante.getProveedor());
            query.setParameter("nroFactura", (Object)comprobante.getNumero().trim());
            query.setParameter("letra", (Object)comprobante.getLetra());
            query.setParameter("class", (Object)comprobante.getClass());
            resultList = (List<Comprobante>) query.getResultList();
            final Iterator<Comprobante> iterator = resultList.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().getId() != comprobante.getId()) {
                    b = true;
                    break;
                }
            }
        }
        catch (NoResultException ex2) {
            return b;
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getEntityClass().getName());
        }
        return b;
    }
    
    public List<Comprobante> getByFilter(final ComprobanteFilter comprobanteFilter, final Paginator paginator) {
        final CustomQuery customQuery = new CustomQuery();
        customQuery.setSelect("c");
        final String s = (comprobanteFilter.getTipoComprobante() != null) ? comprobanteFilter.getTipoComprobante().getReferencedClass().getName() : "ar.com.adecco.proveedores.model.Comprobante";
        customQuery.setFrom(s + " c " + "left join c.proveedor pr ");
        if (comprobanteFilter.isEagerLoad()) {
            final StringBuilder sb = new StringBuilder();
            sb.append(s).append(" c left join fetch c.proveedor pr ");
            if (comprobanteFilter.getTipoComprobante() != null) {
                switch (comprobanteFilter.getTipoComprobante()) {
                    case FC: {
                        sb.append("left join fetch c.ordenCompra oc ");
                        break;
                    }
                    case NC:
                    case ND: {
                        sb.append("left join fetch c.facturaAplicada fc");
                        break;
                    }
                }
            }
            else {
                sb.append("left join fetch c.ordenCompra oc left join fetch c.facturaAplicada fc");
            }
            customQuery.setEagerLoadFrom(sb.toString());
        }
        final StringBuilder sb2 = new StringBuilder();
        final HashMap<String, Object> params = new HashMap<String, Object>();
        this.buildWhereAndParams(comprobanteFilter, sb2, params);
        customQuery.setWhere(sb2.toString());
        customQuery.setParams(params);
        customQuery.setOrderBy(this.buildOrderBy(comprobanteFilter.getOrderBy(), ComprobanteDao.filterOrderByMap));
        customQuery.setPaginator(paginator);
        return (List<Comprobante>)this.getByQuery(customQuery);
    }
    
    private void buildWhereAndParams(final ComprobanteFilter comprobanteFilter, final StringBuilder sb, final Map<String, Object> map) {
        if (comprobanteFilter.getComprobantes() != null) {
            if (comprobanteFilter.getComprobantes().isEmpty()) {
                sb.append(" and 1 = 0");
            }
            else {
                sb.append(" and c in (:c)");
                map.put("c", comprobanteFilter.getComprobantes());
            }
        }
        if (!Strings.isNullOrEmpty(comprobanteFilter.getNumeroDesde())) {
            sb.append(" and c.numero >= :numeroDesde");
            map.put("numeroDesde", comprobanteFilter.getNumeroDesde());
        }
        if (!Strings.isNullOrEmpty(comprobanteFilter.getNumeroHasta())) {
            sb.append(" and c.numero <= :numeroHasta");
            map.put("numeroHasta", comprobanteFilter.getNumeroHasta());
        }
        if (comprobanteFilter.getFechaDesde() != null) {
            sb.append(" and c.fecha >= :fechaDesde");
            map.put("fechaDesde", comprobanteFilter.getFechaDesde());
        }
        if (comprobanteFilter.getFechaHasta() != null) {
            sb.append(" and c.fecha <= :fechaHasta");
            map.put("fechaHasta", comprobanteFilter.getFechaHasta());
        }
        if (!Strings.isNullOrEmpty(comprobanteFilter.getProveedorNombre())) {
            sb.append(" and lower(pr.razonSocial) like :proveedor");
            map.put("proveedor", '%' + comprobanteFilter.getProveedorNombre().toLowerCase() + '%');
        }
        if (!Strings.isNullOrEmpty(comprobanteFilter.getProveedorCuit())) {
            sb.append(" and lower(pr.cuit) like :cuit");
            map.put("cuit", '%' + comprobanteFilter.getProveedorCuit().toLowerCase() + '%');
        }
        if (comprobanteFilter.getProveedorId() != null) {
            sb.append(" and pr.id = :prId");
            map.put("prId", comprobanteFilter.getProveedorId());
        }
        if (comprobanteFilter.getEstado() != null) {
            sb.append(" and c.estado = :estado");
            map.put("estado", comprobanteFilter.getEstado());
        }
        if (comprobanteFilter.getCompaniaId() != null) {
            sb.append(" and c.compania.id = :companiaId");
            map.put("companiaId", comprobanteFilter.getCompaniaId());
        }
        if (comprobanteFilter.getSolicitanteId() != null) {
            sb.append(" and exists (select 1 from DistribucionOrdenCompra doc where doc.distribucionSolicitud.lineaSolicitud.solicitud.empleado.id = (select us.empleado.id from Usuario us where us.id = :solicitanteId) and doc.ordenCompra = c.ordenCompra)");
            map.put("solicitanteId", comprobanteFilter.getSolicitanteId());
        }
    }
    
    public void flushEntities() {
        this.entityManager.flush();
    }
    
    public void clearEntities() {
        this.entityManager.clear();
    }
    
    static {
        final HashMap<ComprobanteFilter.OrderElement, String> hashMap = new HashMap<ComprobanteFilter.OrderElement, String>();
        hashMap.put(ComprobanteFilter.OrderElement.FECHA, "c.fecha");
        filterOrderByMap = Collections.unmodifiableMap(hashMap);
    }
}
