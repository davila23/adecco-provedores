// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.converter;

import ar.com.adecco.dominio.ebs.ap.Chequera;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.enterprise.context.RequestScoped;
import javax.faces.convert.FacesConverter;
import ar.com.adecco.proveedores.converter.selectitems.SelectItemsConverter;

@FacesConverter("chequeraConverter")
@RequestScoped
public class ChequeraConverter extends SelectItemsConverter
{
    public String getAsString(final FacesContext facesContext, final UIComponent uiComponent, final Object o) {
        if (o == null) {
            return "";
        }
        return String.valueOf(((Chequera)o).getId());
    }
}
