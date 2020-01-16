// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.daos;

import javax.persistence.Query;
import ar.com.adecco.proveedores.daos.exception.DaoException;
import ar.com.adecco.proveedores.daos.helpers.ErrorMessage;
import javax.persistence.NoResultException;
import javax.inject.Named;
import ar.com.adecco.dominio.ebs.fnd.Usuario;

@Named
public class UsuarioEbsDao extends AbstractDao<Usuario>
{
    private static final long serialVersionUID = 4396331101301804850L;
    
    @Override
    public Class<Usuario> getClaseEntidad() {
        return Usuario.class;
    }
    
    public Usuario getUserByName(final String s) throws DaoException {
        final String string = "select u from " + this.getClaseEntidad().getName() + " u where upper(u.nombre) = :userName ";
        try {
            final Query query = this.getEntityManager().createQuery(string);
            query.setParameter("userName", (Object)s.toUpperCase());
            return (Usuario)query.getSingleResult();
        }
        catch (NoResultException ex2) {
            return null;
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
    }
}
