// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.daos;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;
import ar.com.adecco.proveedores.model.Proveedor;
import ar.com.adecco.proveedores.daos.exception.DaoException;
import ar.com.adecco.proveedores.daos.helpers.ErrorMessage;
import javax.inject.Named;
import ar.com.adecco.proveedores.model.ProveedorEstado;

@Named
public class ProveedorEstadoDao extends AbstractDao<ProveedorEstado>
{
    private static final long serialVersionUID = 1488798705453289198L;
    
    @Override
    public Class<ProveedorEstado> getClaseEntidad() {
        return ProveedorEstado.class;
    }
    
    public void addProveedorEstado(final ProveedorEstado proveedorEstado) throws DaoException {
        try {
            this.agregarNoFlush(proveedorEstado);
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
    }
    
    public List<ProveedorEstado> getEstadosByProveedor(final Proveedor proveedor) throws DaoException {
        List resultList;
        try {
            final Query query = this.getEntityManager().createQuery("from " + this.getClaseEntidad().getName() + " e where e.proveedor = :proveedor order by id desc");
            query.setParameter("proveedor", (Object)proveedor);
            resultList = query.getResultList();
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
        return (List<ProveedorEstado>)resultList;
    }
    
    public ProveedorEstado getEstadoActualByProveedor(final Proveedor proveedor) throws DaoException {
        try {
            return (ProveedorEstado)this.getEntityManager().createQuery("select pe from ProveedorEstado pe where (pe.proveedor.id, pe.fechaCreacion) = (select pe2.proveedor.id, max(pe2.fechaCreacion) from ProveedorEstado pe2 where pe2.proveedor = :proveedor group by pe2.proveedor.id)", (Class)ProveedorEstado.class).setParameter("proveedor", (Object)proveedor).getSingleResult();
        }
        catch (NoResultException ex) {
            return null;
        }
    }
}
