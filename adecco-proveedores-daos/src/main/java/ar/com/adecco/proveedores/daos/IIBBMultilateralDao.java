// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.daos;

import ar.com.adecco.proveedores.model.Proveedor;
import java.util.List;
import ar.com.adecco.proveedores.daos.exception.DaoException;
import ar.com.adecco.proveedores.daos.helpers.ErrorMessage;
import javax.inject.Named;
import ar.com.adecco.proveedores.model.IIBBMultilateral;

@Named
public class IIBBMultilateralDao extends AbstractDao<IIBBMultilateral>
{
    private static final long serialVersionUID = -5956766386705029719L;
    
    @Override
    public Class<IIBBMultilateral> getClaseEntidad() {
        return IIBBMultilateral.class;
    }
    
    public void addIIBB(final IIBBMultilateral iibbMultilateral) throws DaoException {
        try {
            this.agregarNoFlush(iibbMultilateral);
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
    }
    
    public void removeIIBB(final IIBBMultilateral iibbMultilateral) throws DaoException {
        try {
            this.eliminarNoFlush(iibbMultilateral);
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
    }
    
    public void removeIIBB(final List<IIBBMultilateral> list) throws DaoException {
        try {
            this.eliminarNoFlush(list);
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
    }
    
    public List<IIBBMultilateral> getIIBBByProveedor(final Proveedor proveedor) throws DaoException {
        List resultList;
        try {
            resultList = this.getEntityManager().createQuery("from " + this.getClaseEntidad().getName() + " e where e.proveedor = :proveedor order by e.id desc").setParameter("proveedor", (Object)proveedor).getResultList();
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
        return (List<IIBBMultilateral>)resultList;
    }
}
