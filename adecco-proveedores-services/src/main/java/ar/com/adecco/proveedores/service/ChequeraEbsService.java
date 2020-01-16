// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.service;

import ar.com.adecco.proveedores.daos.helpers.ErrorMessage;
import ar.com.adecco.proveedores.daos.exception.DaoException;
import ar.com.adecco.proveedores.daos.exception.ServiceException;
import ar.com.adecco.dominio.ebs.ap.Chequera;
import java.util.List;
import javax.inject.Inject;
import ar.com.adecco.proveedores.daos.ChequeraEbsDao;
import javax.inject.Named;
import java.io.Serializable;

@Named
public class ChequeraEbsService implements Serializable
{
    private static final long serialVersionUID = 515772184150851800L;
    @Inject
    ChequeraEbsDao chequeraEBSDao;
    
    public List<Chequera> listarPorCompaniaId(final long n) throws ServiceException {
        try {
            return (List<Chequera>)this.chequeraEBSDao.listarPorCompaniaId(n);
        }
        catch (DaoException ex) {
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, ChequeraEbsService.class.getName());
        }
        catch (Exception ex2) {
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, ChequeraEbsService.class.getName());
        }
    }
}
