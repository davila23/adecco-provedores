// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.filters;

import ar.com.adecco.proveedores.filters.commons.OrderExpression;
import java.util.List;
import java.io.Serializable;

public class OrdenCompraFilter implements Serializable
{
    private static final long serialVersionUID = -5171527757296305L;
    private Long proveedorEbsId;
    private Boolean abierta;
    private List<OrderExpression<OrderElement>> orderBy;
    
    public Long getProveedorEbsId() {
        return this.proveedorEbsId;
    }
    
    public void setProveedorEbsId(final Long proveedorEbsId) {
        this.proveedorEbsId = proveedorEbsId;
    }
    
    public Boolean getAbierta() {
        return this.abierta;
    }
    
    public void setAbierta(final Boolean abierta) {
        this.abierta = abierta;
    }
    
    public List<OrderExpression<OrderElement>> getOrderBy() {
        return this.orderBy;
    }
    
    public void setOrderBy(final List<OrderExpression<OrderElement>> orderBy) {
        this.orderBy = orderBy;
    }
    
    public enum OrderElement
    {
        NUMERO, 
        CREATIONDATE, 
        FECHAAPROBACION;
    }
}
