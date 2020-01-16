// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.service;

import ar.com.adecco.proveedores.model.Proveedor;
import ar.com.adecco.dominio.menues.UsuarioAdecco;
import ar.com.adecco.proveedores.daos.helpers.ErrorMessage;
import ar.com.adecco.proveedores.daos.exception.DaoException;
import ar.com.adecco.proveedores.daos.exception.ServiceException;
import ar.com.adecco.dominio.persona.PersonaRelacionProveedor;
import java.util.List;
import ar.com.adecco.proveedores.daos.PersonaRelacionProveedorDao;
import javax.inject.Inject;
import java.security.Principal;
import javax.inject.Named;
import java.io.Serializable;

@Named
public class PersonaRelacionProveedorService implements Serializable
{
    private static final long serialVersionUID = 2098022429146052857L;
    @Inject
    Principal principal;
    @Inject
    PersonaRelacionProveedorDao usuarioAdeccoRelacionDao;
    
    public List<PersonaRelacionProveedor> getRelacionProveedoresByUsuarioAdeccoId(final Long n) throws ServiceException {
        try {
            return (List<PersonaRelacionProveedor>)this.usuarioAdeccoRelacionDao.getByUsuarioAdeccoId(n);
        }
        catch (DaoException ex) {
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, PersonaRelacionProveedorService.class.getName());
        }
        catch (Exception ex2) {
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, PersonaRelacionProveedorService.class.getName());
        }
    }
    
    public List<PersonaRelacionProveedor> getRelacionProveedoresByUsuarioAdecco(final UsuarioAdecco usuarioAdecco) throws ServiceException {
        return this.getRelacionProveedoresByUsuarioAdeccoId(usuarioAdecco.getId());
    }
    
    public List<PersonaRelacionProveedor> getByProveedorId(final Long n) throws ServiceException {
        try {
            return (List<PersonaRelacionProveedor>)this.usuarioAdeccoRelacionDao.getByProveedorId(n);
        }
        catch (DaoException ex) {
            throw new ServiceException((String)ex.getMessages().get(0), (Exception)ex, PersonaRelacionProveedorService.class.getName());
        }
        catch (Exception ex2) {
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex2, PersonaRelacionProveedorService.class.getName());
        }
    }
    
    public List<PersonaRelacionProveedor> getByProveedor(final Proveedor proveedor) throws ServiceException {
        return this.getByProveedorId(proveedor.getId());
    }
}
