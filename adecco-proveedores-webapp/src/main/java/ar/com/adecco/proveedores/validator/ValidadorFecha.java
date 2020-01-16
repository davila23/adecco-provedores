// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.validator;

import javax.faces.validator.ValidatorException;
import javax.faces.application.FacesMessage;
import java.util.regex.Pattern;
import java.text.SimpleDateFormat;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;

@FacesValidator("validadorFecha")
public class ValidadorFecha implements Validator
{
    public void validate(final FacesContext facesContext, final UIComponent uiComponent, final Object o) throws ValidatorException {
        if (o != null) {
            final String format = new SimpleDateFormat("dd/MM/yyyy").format(o);
            if (!Pattern.compile("^[0-9]{1,2}/{1}[0-9]{1,2}/{1}[12]{1}[0-9]{3}$").matcher(format).find()) {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "El formato de fecha debe ser dd/mm/aaaa.", format));
            }
        }
    }
}
