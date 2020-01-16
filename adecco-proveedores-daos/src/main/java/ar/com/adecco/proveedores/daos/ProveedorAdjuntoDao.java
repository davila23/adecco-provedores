// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.daos;

import java.util.List;
import ar.com.adecco.proveedores.model.Proveedor;
import ar.com.adecco.proveedores.daos.exception.DaoException;
import ar.com.adecco.proveedores.daos.helpers.ErrorMessage;
import javax.inject.Named;
import ar.com.adecco.proveedores.model.ProveedorAdjunto;

@Named
public class ProveedorAdjuntoDao extends AbstractDao<ProveedorAdjunto>
{
    private static final long serialVersionUID = 4159479691346802379L;
    
    @Override
    public Class<ProveedorAdjunto> getClaseEntidad() {
        return ProveedorAdjunto.class;
    }
    
    public void addAdjunto(final ProveedorAdjunto proveedorAdjunto) throws DaoException {
        try {
            this.agregarNoFlush(proveedorAdjunto);
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
    }
    
    public List<ProveedorAdjunto> getAdjuntos(final Proveedor proveedor) throws DaoException {
        List resultList;
        try {
            resultList = this.entityManager.createQuery("select pa from ProveedorAdjunto pa where pa.proveedor = :p").setParameter("p", (Object)proveedor).getResultList();
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
        return (List<ProveedorAdjunto>)resultList;
    }
}
