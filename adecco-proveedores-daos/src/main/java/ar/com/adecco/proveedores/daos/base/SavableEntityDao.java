// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.daos.base;

public interface SavableEntityDao<T>
{
    void save(final T p0);
    
    void save(final T p0, final boolean p1);
}
