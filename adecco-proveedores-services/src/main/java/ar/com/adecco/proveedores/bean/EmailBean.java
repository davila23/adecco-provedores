// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.bean;

import java.util.HashMap;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import javax.mail.internet.InternetAddress;

public class EmailBean
{
    private InternetAddress from;
    private List<InternetAddress> to;
    private List<InternetAddress> cc;
    private String subject;
    private String message;
    private String template;
    private Map<String, Object> templateParams;
    
    public EmailBean() {
        this.to = new ArrayList<InternetAddress>();
        this.cc = new ArrayList<InternetAddress>();
    }
    
    public void addTo(final InternetAddress internetAddress) {
        this.to.add(internetAddress);
    }
    
    public void addTo(final String s, final String s2) throws UnsupportedEncodingException {
        this.to.add(new InternetAddress(s, s2));
    }
    
    public void addCc(final InternetAddress internetAddress) {
        this.cc.add(internetAddress);
    }
    
    public void addCc(final String s, final String s2) throws UnsupportedEncodingException {
        this.cc.add(new InternetAddress(s, s2));
    }
    
    public void setFrom(final String s, final String s2) throws UnsupportedEncodingException {
        this.from = new InternetAddress(s, s2);
    }
    
    public void addTemplateParam(final String s, final Object o) {
        if (this.templateParams == null) {
            this.templateParams = new HashMap<String, Object>();
        }
        this.templateParams.put(s, o);
    }
    
    public InternetAddress getFrom() {
        return this.from;
    }
    
    public void setFrom(final InternetAddress from) {
        this.from = from;
    }
    
    public List<InternetAddress> getTo() {
        return this.to;
    }
    
    public void setTo(final List<InternetAddress> to) {
        this.to = to;
    }
    
    public List<InternetAddress> getCc() {
        return this.cc;
    }
    
    public void setCc(final List<InternetAddress> cc) {
        this.cc = cc;
    }
    
    public String getSubject() {
        return this.subject;
    }
    
    public void setSubject(final String subject) {
        this.subject = subject;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public void setMessage(final String message) {
        this.message = message;
    }
    
    public String getTemplate() {
        return this.template;
    }
    
    public void setTemplate(final String template) {
        this.template = template;
    }
    
    public Map<String, Object> getTemplateParams() {
        return this.templateParams;
    }
    
    public void setTemplateParams(final Map<String, Object> templateParams) {
        this.templateParams = templateParams;
    }
}
