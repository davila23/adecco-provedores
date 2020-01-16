// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.service;

import ar.com.adecco.proveedores.model.Comprobante;
import ar.com.adecco.dominio.persona.PersonaRelacionProveedor;
import ar.com.adecco.proveedores.model.Proveedor;
import ar.com.adecco.proveedores.daos.exception.ServiceException;
import ar.com.adecco.dominio.menues.UsuarioAdecco;
import ar.com.adecco.proveedores.bean.EmailBean;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import ar.com.adecco.dominio.ebs.fnd.Usuario;
import ar.com.adecco.dominio.persona.Persona;
import java.io.UnsupportedEncodingException;
import javax.mail.internet.InternetAddress;
import javax.annotation.PostConstruct;
import ar.com.syntagma.helpers.PropertiesHelper;
import ar.com.adecco.proveedores.daos.OrdenCompraDao;
import javax.inject.Inject;
import javax.inject.Named;

@Named
public class EmailFactoryService
{
    @Inject
    private UsuarioService usuarioService;
    @Inject
    private PersonaRelacionProveedorService usuarioAdeccoRelacionService;
    @Inject
    private OrdenCompraDao ordenCompraDao;
    private PropertiesHelper propertiesHelper;
    
    @PostConstruct
    public void init() {
        this.propertiesHelper = new PropertiesHelper("proveedores.properties");
    }
    
    private InternetAddress createInternetAddressNotificador() throws UnsupportedEncodingException {
        return new InternetAddress(this.propertiesHelper.getProperty("app.mail.notifierMailAddress", ""), new String(this.propertiesHelper.getProperty("app.mail.notifierName", "").getBytes("ISO-8859-1"), "UTF-8"));
    }
    
    private InternetAddress createInternetAddress(final Persona persona) throws UnsupportedEncodingException {
        return new InternetAddress(persona.getEmail(), persona.getApellidoNombre());
    }
    
    private InternetAddress createInternetAddress(final Usuario usuario) throws UnsupportedEncodingException {
        return new InternetAddress(usuario.getEmail(), usuario.getNombre());
    }
    
    private List<InternetAddress> createInternetAddress(final List<Persona> list) throws UnsupportedEncodingException {
        final ArrayList<InternetAddress> list2 = new ArrayList<InternetAddress>();
        for (final Persona persona : list) {
            list2.add(new InternetAddress(persona.getEmail(), persona.getApellidoNombre()));
        }
        return list2;
    }
    
    private EmailBean createEmail() {
        final EmailBean emailBean = new EmailBean();
        try {
            emailBean.setFrom(this.createInternetAddressNotificador());
        }
        catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        return emailBean;
    }
    
    private void addToStaticMail(final EmailBean emailBean, final String s, final String s2) {
        try {
            final String property = this.propertiesHelper.getProperty(s, "");
            System.out.println("addToStaticMail - " + s + ": " + property);
            if (!property.isEmpty()) {
                emailBean.addTo(property, s2);
            }
        }
        catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
    }
    
    private void addToMailsByPagina(final EmailBean emailBean, final String s) {
        try {
            final String property = this.propertiesHelper.getProperty(s, "");
            System.out.println("addToMailsByPagina - " + s + ": " + property);
            if (!property.isEmpty()) {
                final List<UsuarioAdecco> usuarioAdeccoPorPagina = this.usuarioService.getUsuarioAdeccoPorPagina(property);
                System.out.println("addToMailsByPagina - " + s + ": " + usuarioAdeccoPorPagina.size());
                final Iterator<UsuarioAdecco> iterator = usuarioAdeccoPorPagina.iterator();
                while (iterator.hasNext()) {
                    emailBean.getTo().add(this.createInternetAddress(iterator.next().getPersona()));
                }
            }
        }
        catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        catch (ServiceException ex2) {
            ex2.printStackTrace();
        }
    }
    
    private void addToMailsByProveedor(final EmailBean emailBean, final Proveedor proveedor) {
        try {
            final List<PersonaRelacionProveedor> byProveedor = this.usuarioAdeccoRelacionService.getByProveedor(proveedor);
            System.out.println("addToMailsByProveedor - " + proveedor.getId() + ": " + byProveedor.size());
            for (final PersonaRelacionProveedor personaRelacionProveedor : byProveedor) {
                System.out.println("addToMailsByProveedor - " + personaRelacionProveedor.getPersona().getApellidoNombre() + ": " + personaRelacionProveedor.getPersona().getEmail());
                emailBean.addTo(this.createInternetAddress(personaRelacionProveedor.getPersona()));
            }
        }
        catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        catch (ServiceException ex2) {
            ex2.printStackTrace();
        }
        catch (Exception ex3) {
            ex3.printStackTrace();
        }
    }
    
    private EmailBean createEmailProveedorEnviado(final Proveedor proveedor, final String s) {
        final EmailBean email = this.createEmail();
        email.setSubject("Solicitud de aprobaci\u00f3n de proveedor " + proveedor.getRazonSocial());
        email.setMessage("El proveedor " + proveedor.getRazonSocial() + " (CUIT " + proveedor.getCuit() + ") se encuentra esperando aprobaci\u00f3n." + "<br /><a href=\"" + s + "\">Acceder al sistema</a>");
        email.setTemplate(EmailService.EmailTemplate.NORMAL.getResourceName());
        return email;
    }
    
    public EmailBean createEmailProveedorEnviadoCompras(final Proveedor proveedor) {
        final EmailBean emailProveedorEnviado = this.createEmailProveedorEnviado(proveedor, String.format(this.propertiesHelper.getProperty("link.proveedor.aprobacion.compras", ""), proveedor.getId()));
        this.addToStaticMail(emailProveedorEnviado, "email.compras", "Compras");
        this.addToMailsByPagina(emailProveedorEnviado, "pagina.proveedor.aprobacion.compras");
        return emailProveedorEnviado;
    }
    
    public EmailBean createEmailProveedorEnviadoTesoreria(final Proveedor proveedor) {
        final EmailBean emailProveedorEnviado = this.createEmailProveedorEnviado(proveedor, String.format(this.propertiesHelper.getProperty("link.proveedor.aprobacion.tesoreria", ""), proveedor.getId()));
        this.addToStaticMail(emailProveedorEnviado, "email.tesoreria", "Tesorer\u00eda");
        this.addToMailsByPagina(emailProveedorEnviado, "pagina.proveedor.aprobacion.tesoreria");
        return emailProveedorEnviado;
    }
    
    public EmailBean createEmailProveedorEnviadoImpuestos(final Proveedor proveedor) {
        final EmailBean emailProveedorEnviado = this.createEmailProveedorEnviado(proveedor, String.format(this.propertiesHelper.getProperty("link.proveedor.aprobacion.impuestos", ""), proveedor.getId()));
        this.addToStaticMail(emailProveedorEnviado, "email.impuestos", "Impuestos");
        this.addToMailsByPagina(emailProveedorEnviado, "pagina.proveedor.aprobacion.impuestos");
        return emailProveedorEnviado;
    }
    
    public EmailBean createEmailProveedorAprobado(final Proveedor proveedor) {
        final EmailBean email = this.createEmail();
        email.setSubject("Solicitud de alta de proveedor " + proveedor.getRazonSocial());
        email.setMessage("La solicitud de alta del proveedor " + proveedor.getRazonSocial() + " (CUIT " + proveedor.getCuit() + ") fue aprobada, ya puede ingresar al sistema con su perfil." + "<br /><a href=\"" + this.propertiesHelper.getProperty("link.app", "") + "\">Acceder al sistema</a>");
        email.setTemplate(EmailService.EmailTemplate.NORMAL.getResourceName());
        this.addToStaticMail(email, "email.proveedor", "Proveedor");
        this.addToMailsByProveedor(email, proveedor);
        return email;
    }
    
    public EmailBean createEmailProveedorRechazado(final Proveedor proveedor, final String s) {
        final String format = String.format(this.propertiesHelper.getProperty("link.proveedor.administracion", ""), proveedor.getId());
        final EmailBean email = this.createEmail();
        email.setSubject("Solicitud de alta de proveedor " + proveedor.getRazonSocial());
        email.setMessage("La solicitud de alta del proveedor " + proveedor.getRazonSocial() + " (CUIT " + proveedor.getCuit() + ") fue rechazada." + "<br/>" + "Motivo del rechazo: " + s + "<br /><a href=\"" + format + "\">Acceder al sistema</a>");
        email.setTemplate(EmailService.EmailTemplate.NORMAL.getResourceName());
        this.addToStaticMail(email, "email.proveedor", "Proveedor");
        this.addToMailsByProveedor(email, proveedor);
        return email;
    }
    
    public EmailBean createEmailFacturaEnviada(final Proveedor proveedor, final Comprobante comprobante, final List<Usuario> list) {
        final String format = String.format(this.propertiesHelper.getProperty("link.factura.aceptacion", ""), comprobante.getId());
        final EmailBean email = this.createEmail();
        email.setSubject("Pendiente de aceptaci\u00f3n: comprobante " + comprobante.getTipoLetraNumero() + " del proveedor " + proveedor.getRazonSocial());
        email.setMessage("Se requiere de su aceptaci\u00f3n para el comprobante " + comprobante.getTipoLetraNumero() + " del proveedor " + proveedor.getRazonSocial() + " (CUIT " + proveedor.getCuit() + ")." + "<br /><a href=\"" + format + "\">Acceder al sistema</a>");
        email.setTemplate(EmailService.EmailTemplate.NORMAL.getResourceName());
        try {
            final Iterator<Usuario> iterator = list.iterator();
            while (iterator.hasNext()) {
                email.addTo(this.createInternetAddress(iterator.next()));
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return email;
    }
    
    public EmailBean createEmailFacturaAceptada(final Proveedor proveedor, final Comprobante comprobante) {
        final String format = String.format(this.propertiesHelper.getProperty("link.factura.aprobacion", ""), comprobante.getId());
        final EmailBean email = this.createEmail();
        email.setSubject("Pendiente de aprobaci\u00f3n: comprobante " + comprobante.getTipoLetraNumero() + " del proveedor " + proveedor.getRazonSocial());
        email.setMessage("Se requiere de su aprobaci\u00f3n para el comprobante " + comprobante.getTipoLetraNumero() + " del proveedor " + proveedor.getRazonSocial() + " (CUIT " + proveedor.getCuit() + ")." + "<br /><a href=\"" + format + "\">Acceder al sistema</a>");
        email.setTemplate(EmailService.EmailTemplate.NORMAL.getResourceName());
        this.addToStaticMail(email, "email.cuentasAPagar", "Cuentas a Pagar");
        this.addToMailsByPagina(email, "pagina.factura.aprobacion");
        return email;
    }
    
    public EmailBean createEmailFacturaAprobada(final Proveedor proveedor, final Comprobante comprobante) {
        final String format = String.format(this.propertiesHelper.getProperty("link.factura.pago", ""), comprobante.getId());
        final EmailBean email = this.createEmail();
        email.setSubject("Pendiente de pago: comprobante " + comprobante.getTipoLetraNumero() + " del proveedor " + proveedor.getRazonSocial());
        email.setMessage("El comprobante " + comprobante.getTipoLetraNumero() + " del proveedor " + proveedor.getRazonSocial() + " (CUIT " + proveedor.getCuit() + ") " + "fue aprobada. Puede realizar el pago de la misma accediendo al sistema." + "<br /><a href=\"" + format + "\">Acceder al sistema</a>");
        email.setTemplate(EmailService.EmailTemplate.NORMAL.getResourceName());
        this.addToStaticMail(email, "email.tesoreria", "Tesorer\u00eda");
        this.addToMailsByPagina(email, "pagina.factura.pago");
        return email;
    }
    
    public EmailBean createEmailFacturaRechazada(final Proveedor proveedor, final Comprobante comprobante, final String s) {
        final String format = String.format(this.propertiesHelper.getProperty("link.factura.administracion", ""), proveedor.getId(), comprobante.getId());
        final EmailBean email = this.createEmail();
        email.setSubject("Rechazo de comprobante " + comprobante.getTipoLetraNumero());
        email.setMessage("Se notifica al proveedor " + proveedor.getRazonSocial() + " (CUIT " + proveedor.getCuit() + ") " + "que el comprobante " + comprobante.getTipoLetraNumero() + " fue rechazado." + "<br/>" + "Motivo del rechazo: " + s + "<br /><a href=\"" + format + "\">Acceder al sistema</a>");
        email.setTemplate(EmailService.EmailTemplate.NORMAL.getResourceName());
        this.addToStaticMail(email, "email.proveedor", "Proveedor");
        this.addToMailsByProveedor(email, proveedor);
        return email;
    }
}
