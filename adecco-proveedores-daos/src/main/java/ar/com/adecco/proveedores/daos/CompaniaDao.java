// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.daos;

import ar.com.adecco.proveedores.daos.exception.DaoException;
import ar.com.adecco.proveedores.daos.helpers.ErrorMessage;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import ar.com.adecco.dominio.ebs.hr.Compania;

@Named
public class CompaniaDao extends AbstractDao<Compania>
{
    private static final long serialVersionUID = 7154871312835085348L;
    
    @Override
    public Class<Compania> getClaseEntidad() {
        return Compania.class;
    }
    
    public List<Compania> getCompaniasAdecco() throws DaoException {
        final ArrayList list = new ArrayList();
        List resultList;
        try {
            resultList = this.getEntityManager().createQuery("from " + this.getClaseEntidad().getName() + " c where c.codigo is not null order by c.nombre").getResultList();
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
        return (List<Compania>)resultList;
    }
}
