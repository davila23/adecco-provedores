// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.daos;

import javax.persistence.Query;
import ar.com.adecco.proveedores.daos.exception.DaoException;
import ar.com.adecco.proveedores.daos.helpers.ErrorMessage;
import javax.persistence.NoResultException;
import javax.inject.Named;
import ar.com.adecco.dominio.ebs.fnd.Provincia;

@Named
public class ProvinciaDao extends AbstractDao<Provincia>
{
    private static final long serialVersionUID = 8457175678142889529L;
    
    @Override
    public Class<Provincia> getClaseEntidad() {
        return Provincia.class;
    }
    
    @Override
    protected String getQueryHabilitado() {
        return "a.habilitado = 'Y'";
    }
    
    public Provincia getProvinciaByName(final String s) throws DaoException {
        try {
            final Query query = this.getEntityManager().createQuery("from " + this.getClaseEntidad().getName() + " e where upper(e.nombre) = :name");
            query.setParameter("name", (Object)s.toUpperCase());
            return (Provincia)query.getSingleResult();
        }
        catch (NoResultException ex2) {
            return null;
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
    }
}
