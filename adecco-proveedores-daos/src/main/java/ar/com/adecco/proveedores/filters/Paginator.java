// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.filters;

import java.io.Serializable;

public class Paginator implements Serializable
{
    private static final long serialVersionUID = -5171527757290135L;
    private int first;
    private int pageSize;
    private int rowCount;
    private boolean onlyCount;
    
    public int getFirst() {
        return this.first;
    }
    
    public void setFirst(final int first) {
        this.first = first;
    }
    
    public int getPageSize() {
        return this.pageSize;
    }
    
    public void setPageSize(final int pageSize) {
        this.pageSize = pageSize;
    }
    
    public int getRowCount() {
        return this.rowCount;
    }
    
    public void setRowCount(final int rowCount) {
        this.rowCount = rowCount;
    }
    
    public boolean isOnlyCount() {
        return this.onlyCount;
    }
    
    public void setOnlyCount(final boolean onlyCount) {
        this.onlyCount = onlyCount;
    }
}
