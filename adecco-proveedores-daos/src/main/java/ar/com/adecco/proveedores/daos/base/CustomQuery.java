// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.daos.base;

import ar.com.adecco.proveedores.filters.Paginator;
import java.util.Map;

public class CustomQuery
{
    private String select;
    private String from;
    private String eagerLoadFrom;
    private String where;
    private Map<String, Object> params;
    private String orderBy;
    private Paginator paginator;
    
    public String getSelect() {
        return this.select;
    }
    
    public void setSelect(final String select) {
        this.select = select;
    }
    
    public String getFrom() {
        return this.from;
    }
    
    public void setFrom(final String from) {
        this.from = from;
    }
    
    public String getEagerLoadFrom() {
        return this.eagerLoadFrom;
    }
    
    public void setEagerLoadFrom(final String eagerLoadFrom) {
        this.eagerLoadFrom = eagerLoadFrom;
    }
    
    public String getWhere() {
        return this.where;
    }
    
    public void setWhere(final String where) {
        this.where = where;
    }
    
    public Map<String, Object> getParams() {
        return this.params;
    }
    
    public void setParams(final Map<String, Object> params) {
        this.params = params;
    }
    
    public String getOrderBy() {
        return this.orderBy;
    }
    
    public void setOrderBy(final String orderBy) {
        this.orderBy = orderBy;
    }
    
    public Paginator getPaginator() {
        return this.paginator;
    }
    
    public void setPaginator(final Paginator paginator) {
        this.paginator = paginator;
    }
}
