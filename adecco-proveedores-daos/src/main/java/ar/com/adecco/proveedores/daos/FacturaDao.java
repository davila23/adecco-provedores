// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.daos;

import ar.com.adecco.proveedores.model.Linea;
import ar.com.adecco.proveedores.model.EstadoFactura;
import javax.persistence.NoResultException;
import java.util.Iterator;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import java.util.HashMap;
import java.util.List;
import ar.com.adecco.dominio.ebs.po.OrdenCompra;
import ar.com.adecco.proveedores.daos.exception.DaoException;
import ar.com.adecco.proveedores.daos.helpers.ErrorMessage;
import javax.inject.Named;
import ar.com.adecco.proveedores.model.Factura;

@Named
public class FacturaDao extends AbstractDao<Factura>
{
    private static final long serialVersionUID = 5500601207829276101L;
    
    public void addFactura(final Factura factura) throws DaoException {
        try {
            this.agregarNoFlush(factura);
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
    }
    
    public void modifyFactura(final Factura factura) throws DaoException {
        try {
            this.modificarNoFlush(factura);
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
    }
    
    public void removeFactura(final Factura factura) throws DaoException {
        try {
            this.eliminarNoFlush(factura);
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
    }
    
    public Factura getById(final long n, final boolean b) throws DaoException {
        try {
            if (!b) {
                return this.obtener(n);
            }
            return (Factura)this.getEntityManager().createQuery("select e from ar.com.adecco.proveedores.model.Factura e left join fetch e.proveedor pr left join fetch e.ordenCompra oc left join fetch e.lineas l left join fetch l.lineaOrdenCompra loc where e.id = :id order by e.id desc", (Class)this.getClaseEntidad()).setParameter("id", (Object)n).getSingleResult();
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
    }
    
    public List<Factura> getFacturasByOC(final OrdenCompra ordenCompra) throws DaoException {
        List resultList;
        try {
            resultList = this.getEntityManager().createQuery("select e from ar.com.adecco.proveedores.model.Factura e where e.ordenCompra = :OC order by e.id desc").setParameter("OC", (Object)ordenCompra).getResultList();
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
        return (List<Factura>)resultList;
    }
    
    public List<Factura> getFacturasByFilter(final String s, final HashMap<String, Object> hashMap) throws DaoException {
        List resultList;
        try {
            final Query query = this.getEntityManager().createQuery("select e from ar.com.adecco.proveedores.model.Factura e left join fetch e.proveedor left join fetch e.ordenCompra where 1 = 1" + s);
            for (final String s2 : hashMap.keySet()) {
                query.setParameter(s2, hashMap.get(s2));
            }
            resultList = query.getResultList();
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
        return (List<Factura>)resultList;
    }
    
    public boolean existNroFacturaByProveedor(final Factura factura) throws DaoException {
        boolean b = false;
        try {
            final TypedQuery<Factura> query = (TypedQuery<Factura>) this.getEntityManager().createQuery(
                    "select e from ar.com.adecco.proveedores.model.Factura e where e.proveedor = :proveedor and e.numero = :nroFactura and e.letra = :letra");
            query.setParameter("proveedor", factura.getProveedor());
            query.setParameter("nroFactura", (Object)factura.getNumero().trim());
            query.setParameter("letra", (Object)factura.getLetra());
            final Iterator<Factura> iterator = query.getResultList().iterator();
            while (iterator.hasNext()) {
                if (iterator.next().getId() != factura.getId()) {
                    b = true;
                    break;
                }
            }
        }
        catch (NoResultException ex2) {
            return b;
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
        return b;
    }
    
    public List<Object[]> getImportesPorOC(final List<OrdenCompra> list, final List<EstadoFactura> list2) throws DaoException {
        List resultList;
        try {
            resultList = this.getEntityManager().createQuery("select oc.id, sum(li.importe) from ar.com.adecco.proveedores.model.Factura fac join fac.ordenCompra oc join fac.lineas li where oc in (:listaOC) and li.tipo = :tipo and fac.estado in (:ef) group by oc").setParameter("listaOC", (Object)list).setParameter("tipo", (Object)Linea.TipoLinea.ITEM).setParameter("ef", (Object)list2).getResultList();
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
        return (List<Object[]>)resultList;
    }
    
    public Factura getFacturaByFacturaEbs(final ar.com.adecco.dominio.ebs.ap.Factura factura) throws DaoException {
        Factura factura2;
        try {
            factura2 = (Factura)this.getEntityManager().createQuery("select fc from ar.com.adecco.proveedores.model.Factura fc where fc.comprobanteEbs = :fEbs", (Class)Factura.class).setParameter("fEbs", (Object)factura).getSingleResult();
        }
        catch (NoResultException ex2) {
            return null;
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
        return factura2;
    }
    
    @Override
    public Class<Factura> getClaseEntidad() {
        return Factura.class;
    }
}
