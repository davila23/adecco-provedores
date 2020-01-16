// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.daos;

import java.util.List;
import ar.com.adecco.dominio.ebs.gl.JuegoLibros;
import javax.persistence.Query;
import ar.com.adecco.proveedores.daos.exception.DaoException;
import ar.com.adecco.proveedores.daos.helpers.ErrorMessage;
import javax.persistence.NoResultException;
import ar.com.adecco.dominio.ebs.hr.Compania;
import javax.inject.Named;
import ar.com.adecco.dominio.ebs.ap.Impuesto;

@Named
public class ImpuestoDao extends AbstractDao<Impuesto>
{
    private static final long serialVersionUID = 2334794535305933003L;
    
    public Impuesto getImpuestoByName(final String s, final Compania compania) throws DaoException {
        try {
            final Query query = this.getEntityManager().createQuery("from " + this.getClaseEntidad().getName() + " e where e.codigo = :codigo and e.compania = :compania");
            query.setParameter("codigo", (Object)s);
            query.setParameter("compania", (Object)compania);
            return (Impuesto)query.getSingleResult();
        }
        catch (NoResultException ex2) {
            return null;
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
    }
    
    public List<Impuesto> getImpuestosByActPrincipal(final String s, final JuegoLibros juegoLibros) throws DaoException {
        List resultList;
        try {
            final Query query = this.getEntityManager().createQuery("from " + this.getClaseEntidad().getName() + " e where e.tipoRetencion = :tipo and e.juegoLibros = :juegos");
            query.setParameter("tipo", (Object)s);
            query.setParameter("juegos", (Object)juegoLibros);
            resultList = query.getResultList();
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
        return (List<Impuesto>)resultList;
    }
    
    public List<Impuesto> getImpuestosByType(final Compania compania, final String s) throws DaoException {
        List resultList;
        try {
            final Query query = this.getEntityManager().createQuery("from " + this.getClaseEntidad().getName() + " e where e.tipo = :tipo and e.compania = :compania");
            query.setParameter("tipo", (Object)s);
            query.setParameter("compania", (Object)compania);
            resultList = query.getResultList();
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
        return (List<Impuesto>)resultList;
    }
    
    @Override
    public Class<Impuesto> getClaseEntidad() {
        return Impuesto.class;
    }
}
