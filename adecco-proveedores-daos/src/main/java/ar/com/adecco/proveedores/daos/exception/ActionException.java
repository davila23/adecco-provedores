// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.daos.exception;

import java.util.ArrayList;
import java.util.List;

public class ActionException extends Exception
{
    private static final long serialVersionUID = 5435861991784095200L;
    private List<String> messages;
    private String action;
    
    public ActionException(final Exception ex) {
        super(ex);
    }
    
    public ActionException(final List<String> messages, final String action) {
        this.setMessages(messages);
        this.setAction(action);
    }
    
    public ActionException(final String s, final String action) {
        super(s);
        this.getMessages().add(s);
        this.setAction(action);
    }
    
    public ActionException(final Exception ex, final String action) {
        super(ex);
        this.getMessages().add(ex.getMessage());
        this.setAction(action);
    }
    
    public ActionException(final String s, final Exception ex, final String action) {
        super(ex);
        this.getMessages().add(s);
        this.getMessages().add(ex.getMessage());
        this.setAction(action);
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
    
    public String getAction() {
        return this.action;
    }
    
    public void setAction(final String action) {
        this.action = action;
    }
}
