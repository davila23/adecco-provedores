// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.filters.commons;

public class OrderExpression<T extends Enum>
{
    T element;
    OrderDirection direction;
    
    public OrderExpression(final T element) {
        this.element = element;
        this.direction = OrderDirection.ASC;
    }
    
    public OrderExpression(final T element, final OrderDirection direction) {
        this.element = element;
        this.direction = direction;
    }
    
    public T getElement() {
        return this.element;
    }
    
    public void setElement(final T element) {
        this.element = element;
    }
    
    public OrderDirection getDirection() {
        return this.direction;
    }
    
    public void setDirection(final OrderDirection direction) {
        this.direction = direction;
    }
}
