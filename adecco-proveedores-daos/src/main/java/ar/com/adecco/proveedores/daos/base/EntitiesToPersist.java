// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.daos.base;

import java.util.Collection;

public class EntitiesToPersist<T>
{
    private Collection<T> toRemove;
    private Collection<T> toSave;
    private Collection<T> toUpdate;
    
    public Collection<T> getToRemove() {
        return this.toRemove;
    }
    
    public void setToRemove(final Collection<T> toRemove2) {
        this.toRemove = toRemove2;
    }
    
    public Collection<T> getToSave() {
        return this.toSave;
    }
    
    public void setToSave(final Collection<T> toSave) {
        this.toSave = toSave;
    }
    
    public Collection<T> getToUpdate() {
        return this.toUpdate;
    }
    
    public void setToUpdate(final Collection<T> toUpdate) {
        this.toUpdate = toUpdate;
    }
}
