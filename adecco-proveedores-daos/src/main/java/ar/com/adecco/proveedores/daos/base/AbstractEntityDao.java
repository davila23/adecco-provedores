// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.daos.base;

import java.util.ArrayList;
import java.util.HashSet;
import ar.com.adecco.dominio.Entidad;
import java.util.Collection;
import java.util.Date;
import ar.com.adecco.dominio.EntidadAuditada;
import ar.com.adecco.proveedores.filters.commons.OrderDirection;
import ar.com.adecco.proveedores.filters.commons.OrderExpression;
import java.util.Iterator;
import javax.persistence.TypedQuery;
import javax.persistence.Query;
import java.util.Map;
import java.util.List;
import java.lang.reflect.ParameterizedType;
import javax.inject.Inject;
import java.security.Principal;
import javax.persistence.PersistenceContext;
import javax.persistence.EntityManager;
import java.io.Serializable;

public abstract class AbstractEntityDao<T> implements Serializable, SavableEntityDao<T>, UpdatableEntityDao<T>, RemovableEntityDao<T>
{
    private static final long serialVersionUID = -4993169756952347374L;
    private Class<T> entityClass;
    @PersistenceContext(unitName = "primary")
    protected EntityManager entityManager;
    @Inject
    Principal principal;
    
    public Class<T> getEntityClass() {
        if (this.entityClass == null) {
            this.entityClass = (Class<T>)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        }
        return this.entityClass;
    }
    
    public <E extends T> List<E> getByQuery(final CustomQuery customQuery) {
        System.out.println("### getByQuery - Inicio");
        final StringBuilder sb = new StringBuilder();
        if (customQuery.getSelect() != null) {
            sb.append("select distinct ").append(customQuery.getSelect());
        }
        sb.append(" from ");
        if (customQuery.getEagerLoadFrom() != null) {
            sb.append(customQuery.getEagerLoadFrom());
        }
        else {
            sb.append(customQuery.getFrom());
        }
        sb.append(" where 1 = 1 ").append(customQuery.getWhere());
        if (customQuery.getOrderBy() != null) {
            sb.append(" order by ").append(customQuery.getOrderBy());
        }
        final Query query = this.entityManager.createQuery(sb.toString());
        if (customQuery.getPaginator() != null) {
            System.out.println("### getByQuery - paginator != null");
            final StringBuilder sb2 = new StringBuilder();
            if (customQuery.getSelect() != null && customQuery.getEagerLoadFrom() != null) {
                sb2.append("select count(distinct ").append(customQuery.getSelect()).append(") ");
            }
            else {
                sb2.append("select count(*) ");
            }
            sb2.append(" from ").append(customQuery.getFrom());
            sb2.append(" where 1 = 1 ").append(customQuery.getWhere());
            final TypedQuery query2 = this.entityManager.createQuery(sb2.toString(), (Class)Long.class);
            for (final Map.Entry<String, Object> entry : customQuery.getParams().entrySet()) {
                query2.setParameter((String)entry.getKey(), entry.getValue());
                query.setParameter((String)entry.getKey(), entry.getValue());
            }
            final Long n = (Long)query2.getSingleResult();
            System.out.println("### getByQuery - rowCount: " + n);
            customQuery.getPaginator().setRowCount(n.intValue());
            if (customQuery.getPaginator().isOnlyCount()) {
                return null;
            }
            query.setFirstResult(customQuery.getPaginator().getFirst());
            query.setMaxResults(customQuery.getPaginator().getPageSize());
        }
        else {
            for (final Map.Entry<String, Object> entry2 : customQuery.getParams().entrySet()) {
                query.setParameter((String)entry2.getKey(), entry2.getValue());
            }
        }
        System.out.println("### getByQuery - Fin");
        return (List<E>)query.getResultList();
    }
    
    protected <E extends Enum<E>> String buildOrderBy(final List<OrderExpression<E>> list, final Map<E, String> map) {
        if (list == null) {
            return null;
        }
        StringBuilder sb = null;
        for (final OrderExpression<E> orderExpression : list) {
            final String s = map.get(orderExpression.getElement());
            if (s == null) {
                System.out.println("ERROR: Mapped value not found for enum OrderElement \"" + s + "\".");
                sb = null;
                break;
            }
            if (sb == null) {
                sb = new StringBuilder(s);
            }
            else {
                sb.append(", ").append(s);
            }
            if (orderExpression.getDirection() != OrderDirection.DESC) {
                continue;
            }
            sb.append(" desc");
        }
        if (sb == null) {
            return null;
        }
        return sb.toString();
    }
    
    protected EntidadAuditada addAuditData(final EntidadAuditada entidadAuditada) {
        entidadAuditada.setFechaModificacion(new Date());
        entidadAuditada.setUsuarioModificacion(this.principal.getName());
        if (entidadAuditada.getId() == 0L) {
            entidadAuditada.setFechaCreacion(new Date());
            entidadAuditada.setUsuarioCreacion(this.principal.getName());
        }
        return entidadAuditada;
    }
    
    protected ar.com.adecco.proveedores.model.base.EntidadAuditada addAuditData(final ar.com.adecco.proveedores.model.base.EntidadAuditada entidadAuditada) {
        entidadAuditada.setFechaModificacion(new Date());
        entidadAuditada.setUsuarioModificacion(this.principal.getName());
        if (entidadAuditada.getId() == 0L) {
            entidadAuditada.setFechaCreacion(new Date());
            entidadAuditada.setUsuarioCreacion(this.principal.getName());
        }
        return entidadAuditada;
    }
    
    @Override
    public void save(final T t) {
        this.save(t, true);
    }
    
    @Override
    public void save(final T t, final boolean b) {
        if (t instanceof EntidadAuditada) {
            this.addAuditData((EntidadAuditada)t);
        }
        else if (t instanceof ar.com.adecco.proveedores.model.base.EntidadAuditada) {
            this.addAuditData((ar.com.adecco.proveedores.model.base.EntidadAuditada)t);
        }
        this.entityManager.persist((Object)t);
        if (b) {
            this.entityManager.flush();
        }
    }
    
    @Override
    public T update(final T t) {
        return this.update(t, true);
    }
    
    @Override
    public T update(final T t, final boolean b) {
        if (t instanceof EntidadAuditada) {
            this.addAuditData((EntidadAuditada)t);
        }
        else if (t instanceof ar.com.adecco.proveedores.model.base.EntidadAuditada) {
            this.addAuditData((ar.com.adecco.proveedores.model.base.EntidadAuditada)t);
        }
        final Object merge = this.entityManager.merge((Object)t);
        if (b) {
            this.entityManager.flush();
        }
        return (T)merge;
    }
    
    @Override
    public void remove(final T t) {
        this.remove(t, true);
    }
    
    @Override
    public void remove(final T t, final boolean b) {
        this.entityManager.remove((Object)t);
        if (b) {
            this.entityManager.flush();
        }
    }
    
    protected <T extends Entidad> EntitiesToPersist<T> prepareToPersist(final Collection<T> collection, final Collection<T> collection2) {
        final EntitiesToPersist<Entidad> entitiesToPersist = (EntitiesToPersist<Entidad>) new EntitiesToPersist<T>();
        final HashSet<T> toRemove = new HashSet<T>((Collection<? extends T>)collection2);
        toRemove.removeAll(collection);
        entitiesToPersist.setToRemove((Collection<Entidad>)toRemove);
        entitiesToPersist.setToSave((Collection<Entidad>)new ArrayList<T>());
        entitiesToPersist.setToUpdate((Collection<Entidad>)new ArrayList<T>());
        for (final Entidad entidad : collection) {
            if (entidad.getId() > 0L) {
                entitiesToPersist.getToUpdate().add((T)entidad);
            }
            else {
                entitiesToPersist.getToSave().add((T)entidad);
            }
        }
        return (EntitiesToPersist<T>)entitiesToPersist;
    }
}
