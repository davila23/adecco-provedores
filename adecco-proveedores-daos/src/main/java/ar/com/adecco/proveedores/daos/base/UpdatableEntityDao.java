// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.daos.base;

public interface UpdatableEntityDao<T>
{
    T update(final T p0);
    
    T update(final T p0, final boolean p1);
}
