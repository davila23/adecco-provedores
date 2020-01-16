// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.service;

import java.util.Iterator;
import ar.com.adecco.proveedores.daos.helpers.ErrorMessage;
import ar.com.adecco.proveedores.daos.exception.DaoException;
import java.util.ArrayList;
import ar.com.adecco.proveedores.daos.exception.ServiceException;
import ar.com.adecco.dominio.ebs.fnd.Provincia;
import java.util.List;
import javax.inject.Inject;
import ar.com.adecco.proveedores.daos.ProvinciaDao;
import javax.inject.Named;
import java.io.Serializable;

@Named
public class ProvinciaService implements Serializable
{
    private static final long serialVersionUID = -6225842822378963945L;
    @Inject
    ProvinciaDao provinciaDao;
    
    public List<Provincia> getProvincias() throws ServiceException {
        return this.getProvincias(false);
    }
    
    public List<Provincia> getProvincias(final boolean b) throws ServiceException {
        final ArrayList list = new ArrayList();
        List<Provincia> listar;
        try {
            listar = this.provinciaDao.listar("a.nombre");
            if (b) {
                for (final Provincia provincia : listar) {
                    if (provincia.getNombre().equalsIgnoreCase("Extranjero")) {
                        listar.remove(provincia);
                        break;
                    }
                }
            }
        }
        catch (DaoException ex) {
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, ProvinciaService.class.getName());
        }
        catch (Exception ex2) {
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, ProvinciaService.class.getName());
        }
        return (List<Provincia>)listar;
    }
    
    public Provincia getEntityById(final String s) throws ServiceException {
        try {
            return (Provincia)this.provinciaDao.obtener((Object)s);
        }
        catch (DaoException ex) {
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, ProvinciaService.class.getName());
        }
        catch (Exception ex2) {
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, ProvinciaService.class.getName());
        }
    }
    
    public Provincia getProvinciaByName(final String s) throws ServiceException {
        try {
            return this.provinciaDao.getProvinciaByName(s);
        }
        catch (DaoException ex) {
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, ProvinciaService.class.getName());
        }
        catch (Exception ex2) {
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, ProvinciaService.class.getName());
        }
    }
}
