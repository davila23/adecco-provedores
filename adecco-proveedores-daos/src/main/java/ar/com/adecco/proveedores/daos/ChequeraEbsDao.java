// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.daos;

import ar.com.adecco.proveedores.daos.exception.DaoException;
import ar.com.adecco.proveedores.daos.helpers.ErrorMessage;
import java.util.List;
import javax.inject.Named;
import ar.com.adecco.dominio.ebs.ap.Chequera;

@Named
public class ChequeraEbsDao extends AbstractDao<Chequera>
{
    private static final long serialVersionUID = -6696447331088015933L;
    
    @Override
    public Class<Chequera> getClaseEntidad() {
        return Chequera.class;
    }
    
    public List<Chequera> listarPorCompaniaId(final long n) throws DaoException {
        try {
            return (List<Chequera>)this.getEntityManager().createQuery("select c from Chequera c where c.compania.id = :companiaId and c.habilitadoViaticos = 'Y' order by c.nombre", (Class)Chequera.class).setParameter("companiaId", (Object)n).getResultList();
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
    }
}
