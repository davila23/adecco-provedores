// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.service;

import java.util.List;
import ar.com.adecco.proveedores.model.Proveedor;
import ar.com.adecco.proveedores.daos.helpers.ErrorMessage;
import ar.com.adecco.proveedores.daos.exception.DaoException;
import ar.com.adecco.proveedores.daos.exception.ServiceException;
import ar.com.adecco.proveedores.model.ProveedorAdjunto;
import javax.inject.Inject;
import ar.com.adecco.proveedores.daos.ProveedorAdjuntoDao;
import javax.inject.Named;
import java.io.Serializable;

@Named
public class ProveedorAdjuntoService implements Serializable
{
    private static final long serialVersionUID = -2588569159758598988L;
    @Inject
    ProveedorAdjuntoDao proveedorAdjuntoDao;
    
    public void eliminarAdjunto(final ProveedorAdjunto proveedorAdjunto) throws ServiceException {
        try {
            this.proveedorAdjuntoDao.eliminar(this.proveedorAdjuntoDao.obtener((Object)proveedorAdjunto.getId()));
        }
        catch (DaoException ex) {
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, ProveedorAdjuntoService.class.getName());
        }
        catch (Exception ex2) {
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, ProveedorAdjuntoService.class.getName());
        }
    }
    
    public void agregarAdjunto(final ProveedorAdjunto proveedorAdjunto) throws ServiceException {
        try {
            this.proveedorAdjuntoDao.addAdjunto(proveedorAdjunto);
        }
        catch (DaoException ex) {
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, ProveedorAdjuntoService.class.getName());
        }
        catch (Exception ex2) {
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, ProveedorAdjuntoService.class.getName());
        }
    }
    
    public List<ProveedorAdjunto> getAdjuntos(final Proveedor proveedor) throws ServiceException {
        try {
            return (List<ProveedorAdjunto>)this.proveedorAdjuntoDao.getAdjuntos(proveedor);
        }
        catch (DaoException ex) {
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, ProveedorAdjuntoService.class.getName());
        }
        catch (Exception ex2) {
            throw new ServiceException(ex2, ProveedorAdjuntoService.class.getName());
        }
    }
    
    public ProveedorAdjunto getAdjunto(final Long n) throws ServiceException {
        try {
            return (ProveedorAdjunto)this.proveedorAdjuntoDao.obtener((Object)n);
        }
        catch (DaoException ex) {
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, ProveedorAdjuntoService.class.getName());
        }
        catch (Exception ex2) {
            throw new ServiceException(ex2, ProveedorAdjuntoService.class.getName());
        }
    }
}
