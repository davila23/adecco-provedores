// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.daos;

import ar.com.adecco.proveedores.model.IIBBMultilateral;
import java.util.Iterator;
import java.util.HashMap;
import javax.persistence.Query;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Collection;
import ar.com.adecco.proveedores.daos.exception.DaoException;
import ar.com.adecco.proveedores.daos.helpers.ErrorMessage;
import javax.inject.Named;
import ar.com.adecco.proveedores.model.Proveedor;

@Named
public class ProveedorDao extends AbstractDao<Proveedor>
{
    private static final long serialVersionUID = -2564541584357381062L;
    
    public void addProveedor(final Proveedor proveedor) throws DaoException {
        try {
            this.agregarNoFlush(proveedor);
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
    }
    
    public void modifyAndFlushProveedor(final Proveedor proveedor) throws DaoException {
        try {
            this.modificar(proveedor);
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
    }
    
    public void modifyProveedor(final Proveedor proveedor) throws DaoException {
        try {
            this.modificarNoFlush(proveedor);
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
    }
    
    public List<Proveedor> getByIds(final Collection<Long> collection) {
        if (collection == null || collection.isEmpty()) {
            return null;
        }
        return (List<Proveedor>)this.getEntityManager().createQuery("select p from " + this.getClaseEntidad().getName() + " p " + "where p.id in :ids", (Class)Proveedor.class).setParameter("ids", (Object)collection).getResultList();
    }
    
    public Proveedor getProveedorByName(final String s) throws DaoException {
        try {
            final Query query = this.getEntityManager().createQuery("from " + this.getClaseEntidad().getName() + " e where upper(e.razonSocial) like :nombre");
            query.setParameter("nombre", (Object)('%' + s.toUpperCase() + '%'));
            return (Proveedor)query.getSingleResult();
        }
        catch (NoResultException ex2) {
            return null;
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
    }
    
    public List<Proveedor> getActiveProvidersByUser(final String s) throws DaoException {
        List resultList;
        try {
            final Query query = this.getEntityManager().createQuery("select e from ar.com.adecco.proveedores.model.Proveedor e where e.usuarioCreacion = :nombre and e.proveedorEBS is not null order by e.razonSocial");
            query.setParameter("nombre", (Object)s);
            resultList = query.getResultList();
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
        return (List<Proveedor>)resultList;
    }
    
    public List<Proveedor> getProveedoresByFilter(final String s, final HashMap<String, Object> hashMap) throws DaoException {
        List resultList;
        try {
            final Query query = this.getEntityManager().createQuery("select e from " + this.getClaseEntidad().getName() + " e " + "where 1 = 1" + s);
            for (final String s2 : hashMap.keySet()) {
                System.out.println("getProveedoresByFilter - nombreParametro: " + s2 + " - " + hashMap.get(s2));
                query.setParameter(s2, hashMap.get(s2));
            }
            resultList = query.getResultList();
            System.out.println("getProveedoresByFilter - proveedores: " + resultList.size());
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
        return (List<Proveedor>)resultList;
    }
    
    public Long getIdProveedorByCuit(final String s) {
        try {
            return (Long)this.getEntityManager().createQuery("select p.id from " + this.getClaseEntidad().getName() + " p " + "where p.cuit = :cuit", (Class)Long.class).setParameter("cuit", (Object)s).getSingleResult();
        }
        catch (NoResultException ex) {
            return null;
        }
    }
    
    @Override
    public Class<Proveedor> getClaseEntidad() {
        return Proveedor.class;
    }
    
    public Proveedor getProveedorEagerLoadById(final long n) {
        try {
            final Proveedor proveedor = (Proveedor)this.getEntityManager().createQuery("select p from " + this.getClaseEntidad().getName() + " p " + "left join fetch p.moneda " + "where p.id = :id", (Class)Proveedor.class).setParameter("id", (Object)n).getSingleResult();
            proveedor.getProvincia().getNombre();
            proveedor.getJurisdiccionLocal().getNombre();
            final Iterator<IIBBMultilateral> iterator = proveedor.getIibbMultilaterales().iterator();
            while (iterator.hasNext()) {
                iterator.next().getJurisdiccion().getNombre();
            }
            return proveedor;
        }
        catch (NoResultException ex) {
            return null;
        }
    }
}
