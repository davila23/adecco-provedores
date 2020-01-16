// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.daos.exception;

import java.util.ArrayList;
import java.util.List;

public class ServiceException extends Exception
{
    private static final long serialVersionUID = 8017375461720977712L;
    private List<String> messages;
    private String servicio;
    
    public ServiceException(final Exception ex) {
        super(ex);
    }
    
    public ServiceException(final List<String> messages, final String servicio) {
        this.setMessages(messages);
        this.setServicio(servicio);
    }
    
    public ServiceException(final String s, final String servicio) {
        super(s);
        this.getMessages().add(s);
        this.setServicio(servicio);
    }
    
    public ServiceException(final Exception ex, final String servicio) {
        super(ex);
        this.getMessages().add(ex.getMessage());
        this.setServicio(servicio);
    }
    
    public ServiceException(final String s, final Exception ex, final String servicio) {
        super(ex);
        this.getMessages().add(s);
        this.getMessages().add(ex.getMessage());
        this.setServicio(servicio);
    }
    
    public void addMessage(final String s) {
        this.getMessages().add(s);
    }
    
    public void removeMessage(final String s) {
        if (!this.getMessages().isEmpty()) {
            this.getMessages().remove(s);
        }
    }
    
    public List<String> getMessages() {
        if (this.messages == null) {
            this.messages = new ArrayList<String>();
        }
        return this.messages;
    }
    
    private void setMessages(final List<String> messages) {
        this.messages = messages;
    }
    
    public String getServicio() {
        return this.servicio;
    }
    
    public void setServicio(final String servicio) {
        this.servicio = servicio;
    }
}
