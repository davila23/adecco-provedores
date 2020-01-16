// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.daos;

import java.util.List;
import ar.com.adecco.proveedores.daos.exception.DaoException;
import ar.com.adecco.proveedores.daos.helpers.ErrorMessage;
import javax.inject.Named;
import ar.com.adecco.dominio.persona.PersonaRelacionProveedor;

@Named
public class PersonaRelacionProveedorDao extends AbstractDao<PersonaRelacionProveedor>
{
    private static final long serialVersionUID = -6451870322428607355L;
    
    @Override
    public Class<PersonaRelacionProveedor> getClaseEntidad() {
        return PersonaRelacionProveedor.class;
    }
    
    public void addUsuarioRelacion(final PersonaRelacionProveedor personaRelacionProveedor) throws DaoException {
        try {
            this.agregarNoFlush(personaRelacionProveedor);
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
    }
    
    public void modifyUsuarioRelacion(final PersonaRelacionProveedor personaRelacionProveedor) throws DaoException {
        try {
            this.modificarNoFlush(personaRelacionProveedor);
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
    }
    
    public List<PersonaRelacionProveedor> getByUsuarioAdeccoId(final Long n) throws DaoException {
        try {
            return (List<PersonaRelacionProveedor>)this.getEntityManager().createQuery("select prp from PersonaRelacionProveedor prp join fetch prp.proveedor where prp.persona.usuarioAdecco.id = :uaId", (Class)PersonaRelacionProveedor.class).setParameter("uaId", (Object)n).getResultList();
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
    }
    
    public List<PersonaRelacionProveedor> getByProveedorId(final Long n) throws DaoException {
        try {
            return (List<PersonaRelacionProveedor>)this.getEntityManager().createQuery("select prp from PersonaRelacionProveedor prp join fetch prp.persona where prp.proveedor.id = :provId", (Class)PersonaRelacionProveedor.class).setParameter("provId", (Object)n).getResultList();
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
    }
}
