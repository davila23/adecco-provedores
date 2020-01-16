// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.service;

import ar.com.adecco.proveedores.daos.helpers.ErrorMessage;
import ar.com.adecco.proveedores.daos.exception.DaoException;
import ar.com.adecco.proveedores.daos.exception.ServiceException;
import ar.com.adecco.proveedores.model.IIBBMultilateral;
import java.util.List;
import ar.com.adecco.proveedores.model.Proveedor;
import javax.inject.Inject;
import ar.com.adecco.proveedores.daos.IIBBMultilateralDao;
import javax.inject.Named;
import java.io.Serializable;

@Named
public class IIBBMultilateralService implements Serializable
{
    private static final long serialVersionUID = 1907273234433805251L;
    @Inject
    IIBBMultilateralDao iibbMultilateralDao;
    
    public List<IIBBMultilateral> getIIBBByProveedor(final Proveedor proveedor) throws ServiceException {
        try {
            return (List<IIBBMultilateral>)this.iibbMultilateralDao.getIIBBByProveedor(proveedor);
        }
        catch (DaoException ex) {
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, IIBBMultilateralService.class.getName());
        }
        catch (Exception ex2) {
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, IIBBMultilateralService.class.getName());
        }
    }
}
