// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.service;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import org.jboss.seam.mail.core.SendFailedException;
import ar.com.adecco.proveedores.daos.helpers.ErrorMessage;
import org.jboss.seam.mail.core.enumerations.MessagePriority;
import org.jboss.seam.mail.core.enumerations.ContentDisposition;
import java.util.Map;
import org.apache.velocity.tools.generic.DateTool;
import org.jboss.seam.mail.templating.TemplateProvider;
import org.jboss.seam.mail.templating.velocity.VelocityTemplate;
import javax.mail.internet.InternetAddress;
import ar.com.adecco.proveedores.daos.exception.ServiceException;
import ar.com.adecco.proveedores.bean.EmailBean;
import javax.annotation.PostConstruct;
import ar.com.syntagma.helpers.PropertiesHelper;
import org.jboss.solder.logging.Logger;
import org.jboss.seam.mail.api.MailMessage;
import org.jboss.seam.mail.templating.velocity.CDIVelocityContext;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.jboss.solder.resourceLoader.ResourceProvider;
import javax.inject.Named;
import java.io.Serializable;

@Named
public class EmailService implements Serializable
{
    private static final long serialVersionUID = 1727013591334016108L;
    @Inject
    private ResourceProvider resourceProvider;
    @Inject
    private Instance<CDIVelocityContext> cdiVelocityContext;
    @Inject
    private Instance<MailMessage> mailMessage;
    private static final Logger log;
    private PropertiesHelper propertiesHelper;
    
    @PostConstruct
    public void init() {
        this.propertiesHelper = new PropertiesHelper("proveedores.properties");
    }
    
    public void send(final EmailBean emailBean) throws ServiceException {
        try {
            final MailMessage mailMessage = (MailMessage)this.mailMessage.get();
            boolean b = false;
            EmailService.log.info((Object)"Env\u00edo de mail");
            EmailService.log.info((Object)("De: " + emailBean.getFrom().getAddress() + " - Asunto: " + emailBean.getSubject()));
            final String property = this.propertiesHelper.getProperty("mail.filter", "");
            final String property2 = this.propertiesHelper.getProperty("mail.filter2", "");
            if (emailBean.getTo().isEmpty()) {
                throw new ServiceException("No existe destinatario a qui\u00e9n enviar el mail", EmailService.class.getName());
            }
            for (final InternetAddress internetAddress : emailBean.getTo()) {
                EmailService.log.info((Object)("Para: " + internetAddress.getAddress()));
                if (internetAddress.getAddress() == null) {
                    EmailService.log.warn((Object)("Se ignora \"" + internetAddress.getPersonal() + "\" por tener mail nulo"));
                }
                else if (!property.isEmpty() && internetAddress.getAddress().contains(property)) {
                    mailMessage.to(internetAddress);
                    b = true;
                }
                else if (!property2.isEmpty() && internetAddress.getAddress().contains(property2)) {
                    mailMessage.to(internetAddress);
                    b = true;
                }
                else {
                    EmailService.log.warn((Object)("Se ignora \"" + internetAddress.getAddress() + "\" por no cumplir con los filtros"));
                }
            }
            if (!b) {
                EmailService.log.warn((Object)"Se ignora el env\u00edo de mail. No hay destinatario.");
                return;
            }
            for (final InternetAddress internetAddress2 : emailBean.getCc()) {
                EmailService.log.info((Object)("CC: " + internetAddress2.getAddress()));
                if (internetAddress2.getAddress() == null) {
                    EmailService.log.warn((Object)("Se ignora \"" + internetAddress2.getPersonal() + "\" por tener mail nulo"));
                }
                else if (!property.isEmpty() && internetAddress2.getAddress().contains(property)) {
                    mailMessage.cc(internetAddress2);
                }
                else if (!property2.isEmpty() && internetAddress2.getAddress().contains(property2)) {
                    mailMessage.cc(internetAddress2);
                }
                else {
                    EmailService.log.warn((Object)("Se ignora \"" + internetAddress2.getAddress() + "\" por no cumplir con los filtros"));
                }
            }
            EmailService.log.info((Object)("email.getTemplate(): " + emailBean.getTemplate()));
            if (emailBean.getTemplate() != null) {
                mailMessage.bodyHtml((TemplateProvider)new VelocityTemplate(this.resourceProvider.loadResourceStream(emailBean.getTemplate()), (CDIVelocityContext)this.cdiVelocityContext.get()));
                mailMessage.put("dateTool", (Object)new DateTool());
                mailMessage.put("subject", (Object)emailBean.getSubject());
                if (emailBean.getMessage() != null) {
                    EmailService.log.info((Object)("message: " + emailBean.getMessage()));
                    mailMessage.put("message", (Object)emailBean.getMessage());
                }
                if (emailBean.getTemplateParams() != null) {
                    for (final Map.Entry<String, Object> entry : emailBean.getTemplateParams().entrySet()) {
                        EmailService.log.info((Object)(entry.getKey() + ": " + entry.getValue()));
                    }
                    mailMessage.put((Map)emailBean.getTemplateParams());
                }
            }
            else {
                if (emailBean.getMessage() == null) {
                    throw new ServiceException("No hay mensaje para enviar", EmailService.class.getName());
                }
                mailMessage.bodyHtml("<html><body><p>" + emailBean.getMessage() + "</p></body></html>");
            }
            mailMessage.from(emailBean.getFrom()).subject(emailBean.getSubject()).addAttachment("adecco_logo.gif", "image/gif", ContentDisposition.INLINE, this.resourceProvider.loadResourceStream("/WEB-INF/mails/img/adecco_logo.gif")).addAttachment("header_lineaseparadora.jpg", "image/jpeg", ContentDisposition.INLINE, this.resourceProvider.loadResourceStream("/WEB-INF/mails/img/header_lineaseparadora.jpg")).importance(MessagePriority.HIGH).send();
        }
        catch (SendFailedException ex) {
            ex.printStackTrace();
            throw new ServiceException(ErrorMessage.ERROR_SA.getMessage(), (Exception)ex, EmailService.class.getName());
        }
        catch (Exception ex2) {
            ex2.printStackTrace();
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, EmailService.class.getName());
        }
    }
    
    public void sendSeparate(final EmailBean emailBean) throws ServiceException {
        final List<InternetAddress> to = emailBean.getTo();
        for (final InternetAddress internetAddress : to) {
            final ArrayList<InternetAddress> to2 = new ArrayList<InternetAddress>();
            to2.add(internetAddress);
            emailBean.setTo(to2);
        }
        emailBean.setTo(to);
    }
    
    static {
        log = Logger.getLogger((Class)EmailService.class);
    }
    
    public enum EmailTemplate
    {
        NORMAL("/WEB-INF/mails/normal.vm");
        
        private final String resourceName;
        
        private EmailTemplate(final String resourceName) {
            this.resourceName = resourceName;
        }
        
        public String getResourceName() {
            return this.resourceName;
        }
    }
}
