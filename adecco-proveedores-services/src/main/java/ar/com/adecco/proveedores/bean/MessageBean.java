// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.bean;

import javax.faces.application.FacesMessage;

public class MessageBean
{
    private String message;
    private FacesMessage.Severity severity;
    
    public MessageBean() {
        this.message = "";
        this.severity = null;
    }
    
    public MessageBean(final String message, final FacesMessage.Severity severity) {
        this.message = message;
        this.severity = severity;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public void setMessage(final String message) {
        this.message = message;
    }
    
    public FacesMessage.Severity getSeverity() {
        return this.severity;
    }
    
    public void setSeverity(final FacesMessage.Severity severity) {
        this.severity = severity;
    }
}
