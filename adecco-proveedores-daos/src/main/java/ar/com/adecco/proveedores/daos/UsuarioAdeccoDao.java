// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.daos;

import java.util.List;
import javax.persistence.Query;
import ar.com.adecco.proveedores.daos.exception.DaoException;
import ar.com.adecco.proveedores.daos.helpers.ErrorMessage;
import javax.persistence.NoResultException;
import javax.inject.Named;
import ar.com.adecco.dominio.menues.UsuarioAdecco;

@Named
public class UsuarioAdeccoDao extends AbstractDao<UsuarioAdecco>
{
    private static final long serialVersionUID = 1985826731835941194L;
    
    @Override
    public Class<UsuarioAdecco> getClaseEntidad() {
        return UsuarioAdecco.class;
    }
    
    public UsuarioAdecco getUserByName(final String s) throws DaoException {
        final String string = "select ua from " + this.getClaseEntidad().getName() + " ua join fetch ua.persona where upper(ua.login) = :usuario ";
        try {
            final Query query = this.getEntityManager().createQuery(string);
            query.setParameter("usuario", (Object)s.toUpperCase());
            return (UsuarioAdecco)query.getSingleResult();
        }
        catch (NoResultException ex2) {
            return null;
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
    }
    
    public List<UsuarioAdecco> getByPagina(final String s) throws DaoException {
        try {
            return (List<UsuarioAdecco>)this.getEntityManager().createQuery("select ua from Perfil p join p.menus m join p.usuariosAdecco ua join fetch ua.persona where m.pagina = :pagina and m.habilitado = 1 and p.habilitado = 1 and ua.habilitado = 1", (Class)UsuarioAdecco.class).setParameter("pagina", (Object)s).getResultList();
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
    }
    
    public List<UsuarioAdecco> getByPagina(final List<String> list) throws DaoException {
        try {
            return (List<UsuarioAdecco>)this.getEntityManager().createQuery("select ua from Perfil p join p.menus m join p.usuariosAdecco ua join fetch ua.persona where m.pagina in (:paginas) and m.habilitado = 1 and p.habilitado = 1 and ua.habilitado = 1", (Class)UsuarioAdecco.class).setParameter("paginas", (Object)list).getResultList();
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
    }
    
    public boolean isUsuarioConAccesoAPagina(final String s, final String s2) throws DaoException {
        System.out.println("isUsuarioConAccesoAPagina - login: [" + s + "]");
        System.out.println("isUsuarioConAccesoAPagina - pagina: [" + s2 + "]");
        try {
            final Number n = (Number)this.getEntityManager().createQuery("select count(ua) from Perfil p join p.menus m join p.usuariosAdecco ua where ua.login = :login and m.pagina = :pagina and m.habilitado = 1 and p.habilitado = 1 and ua.habilitado = 1").setParameter("login", (Object)s).setParameter("pagina", (Object)s2).getSingleResult();
            System.out.println("isUsuarioConAccesoAPagina: " + n.longValue() + ": " + (n.longValue() > 0L));
            return n.longValue() > 0L;
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
    }
}
