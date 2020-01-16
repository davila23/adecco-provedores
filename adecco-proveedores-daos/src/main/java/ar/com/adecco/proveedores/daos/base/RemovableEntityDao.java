// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.daos.base;

public interface RemovableEntityDao<T>
{
    void remove(final T p0);
    
    void remove(final T p0, final boolean p1);
}
