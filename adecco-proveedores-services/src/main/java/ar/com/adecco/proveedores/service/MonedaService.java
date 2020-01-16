// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.service;

import ar.com.adecco.proveedores.daos.helpers.ErrorMessage;
import ar.com.adecco.proveedores.daos.exception.DaoException;
import ar.com.adecco.proveedores.daos.exception.ServiceException;
import ar.com.adecco.dominio.ebs.fnd.Moneda;
import java.util.List;
import javax.inject.Inject;
import ar.com.adecco.proveedores.daos.MonedaDao;
import javax.inject.Named;
import java.io.Serializable;

@Named
public class MonedaService implements Serializable
{
    private static final long serialVersionUID = -7955945626590517512L;
    @Inject
    MonedaDao monedaDao;
    
    public List<Moneda> getMonedasActivas() throws ServiceException {
        try {
            return (List<Moneda>)this.monedaDao.getActiveCoins();
        }
        catch (DaoException ex) {
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, MonedaService.class.getName());
        }
        catch (Exception ex2) {
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, MonedaService.class.getName());
        }
    }
    
    public Moneda getMonedaById(final String s) throws ServiceException {
        try {
            return this.monedaDao.getMonedaById(s);
        }
        catch (DaoException ex) {
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, MonedaService.class.getName());
        }
        catch (Exception ex2) {
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, MonedaService.class.getName());
        }
    }
}
