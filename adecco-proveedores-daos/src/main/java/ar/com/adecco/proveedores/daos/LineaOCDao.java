// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.daos;

import ar.com.adecco.proveedores.daos.exception.DaoException;
import ar.com.adecco.proveedores.daos.helpers.ErrorMessage;
import java.util.List;
import ar.com.adecco.dominio.ebs.po.OrdenCompra;
import javax.inject.Named;
import ar.com.adecco.dominio.ebs.po.LineaOrdenCompra;

@Named
public class LineaOCDao extends AbstractDao<LineaOrdenCompra>
{
    private static final long serialVersionUID = -8535541037433438278L;
    
    public List<LineaOrdenCompra> getLineasByOC(final OrdenCompra ordenCompra) throws DaoException {
        List resultList;
        try {
            resultList = this.getEntityManager().createQuery("from " + this.getClaseEntidad().getName() + " e where e.ordenCompra = :ordenCompra order by e.id desc").setParameter("ordenCompra", (Object)ordenCompra).getResultList();
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
        return (List<LineaOrdenCompra>)resultList;
    }
    
    @Override
    public Class<LineaOrdenCompra> getClaseEntidad() {
        return LineaOrdenCompra.class;
    }
}
