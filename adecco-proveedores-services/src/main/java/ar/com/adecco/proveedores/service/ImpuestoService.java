// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.service;

import ar.com.adecco.dominio.ebs.hr.Compania;
import ar.com.adecco.proveedores.daos.helpers.ErrorMessage;
import ar.com.adecco.proveedores.daos.exception.DaoException;
import ar.com.adecco.proveedores.daos.exception.ServiceException;
import ar.com.adecco.dominio.ebs.gl.JuegoLibros;
import ar.com.adecco.dominio.ebs.ap.Impuesto;
import java.util.List;
import ar.com.adecco.proveedores.daos.JuegoLibrosDao;
import javax.inject.Inject;
import ar.com.adecco.proveedores.daos.ImpuestoDao;
import javax.inject.Named;
import java.io.Serializable;

@Named
public class ImpuestoService implements Serializable
{
    private static final long serialVersionUID = -3353064809976889258L;
    @Inject
    ImpuestoDao impuestoDao;
    @Inject
    JuegoLibrosDao juegoLibrosDao;
    
    public List<Impuesto> getImpuestosByActividadPrincipal(final long n, final String s) throws ServiceException {
        try {
            return (List<Impuesto>)this.impuestoDao.getImpuestosByActPrincipal(s, (JuegoLibros)this.juegoLibrosDao.obtener((Object)n));
        }
        catch (DaoException ex) {
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, ImpuestoService.class.getName());
        }
        catch (Exception ex2) {
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, ImpuestoService.class.getName());
        }
    }
    
    public Impuesto getImpuestoByName(final Compania compania, final String s) throws ServiceException {
        try {
            return this.impuestoDao.getImpuestoByName(s, compania);
        }
        catch (DaoException ex) {
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, ImpuestoService.class.getName());
        }
        catch (Exception ex2) {
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, ImpuestoService.class.getName());
        }
    }
    
    public List<Impuesto> getImpuestosByType(final Compania compania, final String s) throws ServiceException {
        try {
            return (List<Impuesto>)this.impuestoDao.getImpuestosByType(compania, s);
        }
        catch (DaoException ex) {
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, ImpuestoService.class.getName());
        }
        catch (Exception ex2) {
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, ImpuestoService.class.getName());
        }
    }
}
