// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.action;

import javax.annotation.PostConstruct;
import ar.com.adecco.proveedores.daos.exception.ServiceException;
import java.io.Reader;
import java.io.IOException;
import java.io.Writer;
import org.apache.velocity.context.Context;
import org.apache.velocity.app.Velocity;
import java.io.StringWriter;
import java.io.InputStreamReader;
import java.net.URL;
import org.apache.velocity.VelocityContext;
import ar.com.syntagma.helpers.PropertiesHelper;
import org.jboss.solder.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@RequestScoped
public class HomeAction implements Serializable
{
    private static final long serialVersionUID = 3410554363117511710L;
    private static final Logger log;
    private String html;
    
    @PostConstruct
    public void init() throws ServiceException {
        final PropertiesHelper propertiesHelper = new PropertiesHelper("application.properties");
        final String property = propertiesHelper.getProperty("app.name", "");
        final String property2 = propertiesHelper.getProperty("theme.url", "");
        final String string = property2 + property + "-home.html";
        final VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("themeUrl", (Object)property2);
        try {
            Reader reader = null;
            StringWriter stringWriter = null;
            try {
                reader = new InputStreamReader(new URL(string).openStream());
                stringWriter = new StringWriter();
                Velocity.evaluate((Context)velocityContext, (Writer)stringWriter, HomeAction.class.getName(), reader);
                this.html = stringWriter.toString();
            }
            finally {
                if (reader != null) {
                    reader.close();
                }
                if (stringWriter != null) {
                    stringWriter.close();
                }
            }
        }
        catch (IOException ex) {
            HomeAction.log.error((Object)("Se detect\u00f3 un error al cargar la p\u00e1gina de inicio \"" + string + "\": " + ex.getMessage()));
        }
    }
    
    public String getHtml() {
        return this.html;
    }
    
    static {
        log = Logger.getLogger((Class)HomeAction.class);
    }
}
