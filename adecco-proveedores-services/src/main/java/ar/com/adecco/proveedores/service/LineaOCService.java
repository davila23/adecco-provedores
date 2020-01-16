// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.service;

import ar.com.adecco.proveedores.daos.helpers.ErrorMessage;
import ar.com.adecco.proveedores.daos.exception.DaoException;
import ar.com.adecco.proveedores.daos.exception.ServiceException;
import ar.com.adecco.dominio.ebs.po.LineaOrdenCompra;
import java.util.List;
import ar.com.adecco.dominio.ebs.po.OrdenCompra;
import javax.inject.Inject;
import ar.com.adecco.proveedores.daos.LineaOCDao;
import javax.inject.Named;
import java.io.Serializable;

@Named
public class LineaOCService implements Serializable
{
    private static final long serialVersionUID = 820733276480248324L;
    @Inject
    LineaOCDao lineaOCDao;
    
    public List<LineaOrdenCompra> getLineasByOC(final OrdenCompra ordenCompra) throws ServiceException {
        try {
            return (List<LineaOrdenCompra>)this.lineaOCDao.getLineasByOC(ordenCompra);
        }
        catch (DaoException ex) {
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, LineaOCService.class.getName());
        }
        catch (Exception ex2) {
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, LineaOCService.class.getName());
        }
    }
}
