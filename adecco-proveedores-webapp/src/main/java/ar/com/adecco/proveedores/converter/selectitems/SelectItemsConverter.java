// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.converter.selectitems;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;
import javax.faces.convert.Converter;

@FacesConverter("omnifaces.SelectItemsConverter")
@RequestScoped
public class SelectItemsConverter implements Converter
{
    public Object getAsObject(final FacesContext facesContext, final UIComponent uiComponent, final String s) {
        return SelectItemsUtils.findValueByStringConversion(facesContext, uiComponent, s, (Converter)this);
    }
    
    public String getAsString(final FacesContext facesContext, final UIComponent uiComponent, final Object o) {
        if (o == null) {
            return null;
        }
        return o.toString();
    }
}
