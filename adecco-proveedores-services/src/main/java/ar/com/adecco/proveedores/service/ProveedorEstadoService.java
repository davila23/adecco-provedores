// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.service;

import ar.com.adecco.proveedores.daos.exception.DaoException;
import ar.com.adecco.proveedores.daos.exception.ServiceException;
import ar.com.adecco.proveedores.model.ProveedorEstado;
import ar.com.adecco.proveedores.model.Proveedor;
import javax.inject.Inject;
import ar.com.adecco.proveedores.daos.ProveedorEstadoDao;
import javax.inject.Named;
import java.io.Serializable;

@Named
public class ProveedorEstadoService implements Serializable
{
    private static final long serialVersionUID = -2588569859758598928L;
    @Inject
    ProveedorEstadoDao proveedorEstadoDao;
    
    public ProveedorEstado getEstadoActual(final Proveedor proveedor) throws ServiceException {
        try {
            return this.proveedorEstadoDao.getEstadoActualByProveedor(proveedor);
        }
        catch (DaoException ex) {
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, ProveedorEstadoService.class.getName());
        }
        catch (Exception ex2) {
            throw new ServiceException(ex2, ProveedorEstadoService.class.getName());
        }
    }
}
