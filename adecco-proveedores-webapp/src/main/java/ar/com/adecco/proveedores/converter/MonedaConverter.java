// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.converter;

import ar.com.adecco.dominio.ebs.fnd.Moneda;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;
import ar.com.adecco.proveedores.converter.selectitems.SelectItemsConverter;

@FacesConverter("monedaConverter")
@RequestScoped
public class MonedaConverter extends SelectItemsConverter
{
    public String getAsString(final FacesContext facesContext, final UIComponent uiComponent, final Object o) {
        if (o == null) {
            return "";
        }
        return ((Moneda)o).getCodigo();
    }
}
