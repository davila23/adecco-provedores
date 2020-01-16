// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.daos;

import ar.com.adecco.proveedores.daos.exception.DaoException;
import ar.com.adecco.proveedores.daos.helpers.ErrorMessage;
import java.util.List;
import javax.inject.Inject;
import java.security.Principal;
import javax.persistence.PersistenceContext;
import javax.persistence.EntityManager;
import java.io.Serializable;

public abstract class AbstractDao<T> implements IDao<T>, Serializable
{
    private static final long serialVersionUID = -8698928056528237238L;
    @PersistenceContext(unitName = "primary")
    protected EntityManager entityManager;
    @Inject
    Principal principal;
    
    public abstract Class<T> getClaseEntidad();
    
    @Override
    public void agregar(final T t) {
        this.entityManager.persist((Object)t);
        this.entityManager.flush();
    }
    
    public void agregarNoFlush(final T t) {
        this.entityManager.persist((Object)t);
    }
    
    @Override
    public void modificar(final T t) {
        this.entityManager.merge((Object)t);
        this.entityManager.flush();
    }
    
    public void modificarNoFlush(final T t) {
        this.entityManager.merge((Object)t);
    }
    
    @Override
    public void eliminar(final T t) {
        this.entityManager.remove((Object)t);
        this.entityManager.flush();
    }
    
    public void eliminarNoFlush(final T t) {
        this.entityManager.remove((Object)t);
    }
    
    public void eliminarNoFlush(final List<T> list) throws DaoException {
        if (list == null || list.size() == 0) {
            return;
        }
        try {
            this.entityManager.createQuery("delete from " + this.getClaseEntidad().getName() + " a where a in (:lista)").setParameter("lista", (Object)list).executeUpdate();
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
    }
    
    public void flushEntities() {
        this.entityManager.flush();
    }
    
    public void clearEntities() {
        this.entityManager.clear();
    }
    
    @Override
    public List<T> listarTodos() throws DaoException {
        try {
            return (List<T>)this.entityManager.createQuery("from " + this.getClaseEntidad().getName() + " a").getResultList();
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
    }
    
    @Override
    public List<T> listar() throws DaoException {
        try {
            return (List<T>)this.entityManager.createQuery("from " + this.getClaseEntidad().getName() + " a where " + this.getQueryHabilitado()).getResultList();
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
    }
    
    public List<T> listar(final List<?> list) throws DaoException {
        if (list == null || list.size() == 0) {
            return null;
        }
        try {
            return (List<T>)this.entityManager.createQuery("from " + this.getClaseEntidad().getName() + " a where " + this.getQueryHabilitado() + " and a.id in (:ids)").setParameter("ids", (Object)list).getResultList();
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
    }
    
    public List<T> listar(final String s) throws DaoException {
        try {
            return (List<T>)this.entityManager.createQuery("from " + this.getClaseEntidad().getName() + " a where " + this.getQueryHabilitado() + " order by " + s).getResultList();
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
    }
    
    public List<T> listar(final List<?> list, final String s) throws DaoException {
        if (list == null || list.size() == 0) {
            return null;
        }
        try {
            return (List<T>)this.entityManager.createQuery("from " + this.getClaseEntidad().getName() + " a where " + this.getQueryHabilitado() + " and a.id in (:ids) order by " + s).setParameter("ids", (Object)list).getResultList();
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
    }
    
    protected String getQueryHabilitado() {
        return "a.habilitado = true";
    }
    
    public T obtener(final Object o) throws Exception {
        try {
            return (T)this.entityManager.find((Class)this.getClaseEntidad(), o);
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
    }
    
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }
}
