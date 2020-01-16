// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.service;

import ar.com.adecco.dominio.menues.UsuarioAdecco;
import java.util.List;
import ar.com.syntagma.adecco.login.servicio.UsuarioDto;
import ar.com.adecco.proveedores.daos.helpers.ErrorMessage;
import ar.com.adecco.proveedores.daos.exception.DaoException;
import ar.com.adecco.proveedores.daos.exception.ServiceException;
import ar.com.adecco.dominio.persona.Persona;
import ar.com.adecco.dominio.ebs.fnd.Usuario;
import ar.com.adecco.proveedores.daos.UsuarioEbsDao;
import ar.com.adecco.proveedores.daos.UsuarioAdeccoDao;
import javax.inject.Inject;
import ar.com.syntagma.adecco.login.cliente.MenuBean;
import javax.inject.Named;
import java.io.Serializable;

@Named
public class UsuarioService implements Serializable
{
    private static final long serialVersionUID = -2887059456817784554L;
    @Inject
    MenuBean menuBean;
    @Inject
    UsuarioAdeccoDao usuarioAdeccoDao;
    @Inject
    UsuarioEbsDao usuarioEbsDao;
    
    public Usuario getUsuarioEbs() throws ServiceException {
        Usuario userByName = null;
        try {
            final UsuarioDto usuario = this.menuBean.getUsuario();
            final String login = usuario.getLogin();
            if (usuario.getPersona().getTipoPersona().equals(Persona.TipoPersona.INTERNA.toString())) {
                userByName = this.usuarioEbsDao.getUserByName(login.substring(0, login.indexOf("@")));
            }
            return userByName;
        }
        catch (DaoException ex) {
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, UsuarioService.class.getName());
        }
        catch (Exception ex2) {
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, UsuarioService.class.getName());
        }
    }
    
    public List<UsuarioAdecco> getUsuarioAdeccoPorPagina(final String s) throws ServiceException {
        try {
            return (List<UsuarioAdecco>)this.usuarioAdeccoDao.getByPagina(s);
        }
        catch (DaoException ex) {
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, UsuarioService.class.getName());
        }
        catch (Exception ex2) {
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, UsuarioService.class.getName());
        }
    }
    
    public List<UsuarioAdecco> getUsuarioAdeccoPorPagina(final List<String> list) throws ServiceException {
        try {
            return (List<UsuarioAdecco>)this.usuarioAdeccoDao.getByPagina((List)list);
        }
        catch (DaoException ex) {
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, UsuarioService.class.getName());
        }
        catch (Exception ex2) {
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, UsuarioService.class.getName());
        }
    }
    
    public boolean isUsuarioLogeadoConAccesoAPagina(final String s) throws ServiceException {
        try {
            return this.usuarioAdeccoDao.isUsuarioConAccesoAPagina(this.menuBean.getUsuario().getLogin(), s);
        }
        catch (DaoException ex) {
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, UsuarioService.class.getName());
        }
        catch (Exception ex2) {
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, UsuarioService.class.getName());
        }
    }
}
