// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.service;

import ar.com.adecco.proveedores.daos.helpers.ErrorMessage;
import ar.com.adecco.proveedores.daos.exception.DaoException;
import ar.com.adecco.proveedores.daos.exception.ServiceException;
import ar.com.adecco.dominio.ebs.hr.Compania;
import java.util.List;
import javax.inject.Inject;
import ar.com.adecco.proveedores.daos.CompaniaDao;
import javax.inject.Named;
import java.io.Serializable;

@Named
public class CompaniaService implements Serializable
{
    private static final long serialVersionUID = -7850999880147850319L;
    @Inject
    CompaniaDao companiaDao;
    
    public List<Compania> getCompaniasAdecco() throws ServiceException {
        try {
            return (List<Compania>)this.companiaDao.getCompaniasAdecco();
        }
        catch (DaoException ex) {
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, CompaniaService.class.getName());
        }
        catch (Exception ex2) {
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, CompaniaService.class.getName());
        }
    }
}
