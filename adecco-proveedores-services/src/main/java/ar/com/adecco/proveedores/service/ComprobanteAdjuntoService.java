// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.service;

import java.util.List;
import ar.com.adecco.proveedores.model.Comprobante;
import ar.com.adecco.proveedores.daos.helpers.ErrorMessage;
import ar.com.adecco.proveedores.daos.exception.DaoException;
import ar.com.adecco.proveedores.daos.exception.ServiceException;
import ar.com.adecco.proveedores.model.ComprobanteAdjunto;
import javax.inject.Inject;
import ar.com.adecco.proveedores.daos.ComprobanteAdjuntoDao;
import javax.inject.Named;
import java.io.Serializable;

@Named
public class ComprobanteAdjuntoService implements Serializable
{
    private static final long serialVersionUID = 2990204573784932084L;
    @Inject
    ComprobanteAdjuntoDao comprobanteAdjuntoDao;
    
    public void eliminarAdjunto(final ComprobanteAdjunto comprobanteAdjunto) throws ServiceException {
        try {
            this.comprobanteAdjuntoDao.eliminar(this.comprobanteAdjuntoDao.obtener((Object)comprobanteAdjunto.getId()));
        }
        catch (DaoException ex) {
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, ComprobanteAdjuntoService.class.getName());
        }
        catch (Exception ex2) {
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, ComprobanteAdjuntoService.class.getName());
        }
    }
    
    public void agregarAdjunto(final ComprobanteAdjunto comprobanteAdjunto) throws ServiceException {
        try {
            this.comprobanteAdjuntoDao.addAdjunto(comprobanteAdjunto);
        }
        catch (DaoException ex) {
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, ComprobanteAdjuntoService.class.getName());
        }
        catch (Exception ex2) {
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, ComprobanteAdjuntoService.class.getName());
        }
    }
    
    public List<ComprobanteAdjunto> getAdjuntos(final Comprobante comprobante) throws ServiceException {
        try {
            return (List<ComprobanteAdjunto>)this.comprobanteAdjuntoDao.getAdjuntos(comprobante);
        }
        catch (DaoException ex) {
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, ComprobanteAdjuntoService.class.getName());
        }
        catch (Exception ex2) {
            throw new ServiceException(ex2, ComprobanteAdjuntoService.class.getName());
        }
    }
    
    public ComprobanteAdjunto getAdjunto(final Long n) throws ServiceException {
        try {
            return (ComprobanteAdjunto)this.comprobanteAdjuntoDao.obtener((Object)n);
        }
        catch (DaoException ex) {
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, ComprobanteAdjuntoService.class.getName());
        }
        catch (Exception ex2) {
            throw new ServiceException(ex2, ComprobanteAdjuntoService.class.getName());
        }
    }
}
