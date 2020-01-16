// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.daos.exception;

import java.util.ArrayList;
import java.util.List;

public class DaoException extends Exception
{
    private static final long serialVersionUID = 6781112204661254495L;
    private List<String> messages;
    private String dao;
    
    public DaoException(final Exception ex) {
        super(ex);
    }
    
    public DaoException(final List<String> messages, final String dao) {
        this.setMessages(messages);
        this.setDao(dao);
    }
    
    public DaoException(final String s, final String dao) {
        super(s);
        this.getMessages().add(s);
        this.setDao(dao);
    }
    
    public DaoException(final Exception ex, final String dao) {
        super(ex);
        this.getMessages().add(ex.getMessage());
        this.setDao(dao);
    }
    
    public DaoException(final String s, final Exception ex, final String dao) {
        super(ex);
        this.getMessages().add(s);
        this.getMessages().add(ex.getMessage());
        this.setDao(dao);
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
    
    public String getDao() {
        return this.dao;
    }
    
    public void setDao(final String dao) {
        this.dao = dao;
    }
}
