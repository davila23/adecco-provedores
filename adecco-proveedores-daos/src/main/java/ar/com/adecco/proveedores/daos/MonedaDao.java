// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.daos;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import ar.com.adecco.proveedores.daos.exception.DaoException;
import ar.com.adecco.proveedores.daos.helpers.ErrorMessage;
import java.util.List;
import javax.inject.Named;
import ar.com.adecco.dominio.ebs.fnd.Moneda;

@Named
public class MonedaDao extends AbstractDao<Moneda>
{
    private static final long serialVersionUID = 807071895746011685L;
    
    @Override
    public Class<Moneda> getClaseEntidad() {
        return Moneda.class;
    }
    
    public List<Moneda> getActiveCoins() throws DaoException {
        List resultList;
        try {
            final Query query = this.getEntityManager().createQuery("from " + this.getClaseEntidad().getName() + " e where e.activo = :activo order by codigo");
            query.setParameter("activo", (Object)"Y");
            resultList = query.getResultList();
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
        return (List<Moneda>)resultList;
    }
    
    public Moneda getMonedaById(final String s) throws DaoException {
        try {
            final Query query = this.getEntityManager().createQuery("from " + this.getClaseEntidad().getName() + " e where e.codigo = :codigo");
            query.setParameter("codigo", (Object)s);
            return (Moneda)query.getSingleResult();
        }
        catch (NoResultException ex2) {
            return null;
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
    }
}
