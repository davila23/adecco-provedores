// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.service;

import ar.com.adecco.proveedores.daos.exception.DaoException;
import ar.com.adecco.proveedores.daos.exception.ServiceException;
import ar.com.adecco.proveedores.model.FacturaEstado;
import ar.com.adecco.proveedores.model.Comprobante;
import javax.inject.Inject;
import ar.com.adecco.proveedores.daos.FacturaEstadoDao;
import javax.inject.Named;
import java.io.Serializable;

@Named
public class FacturaEstadoService implements Serializable
{
    private static final long serialVersionUID = -2188569859758598928L;
    @Inject
    FacturaEstadoDao facturaEstadoDao;
    
    public FacturaEstado getEstadoActual(final Comprobante comprobante) throws ServiceException {
        try {
            return this.facturaEstadoDao.getEstadoActualByComprobante(comprobante);
        }
        catch (DaoException ex) {
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, FacturaEstadoService.class.getName());
        }
        catch (Exception ex2) {
            throw new ServiceException(ex2, FacturaEstadoService.class.getName());
        }
    }
}
