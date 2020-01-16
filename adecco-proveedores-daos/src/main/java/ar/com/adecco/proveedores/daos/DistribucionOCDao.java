// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.daos;

import ar.com.adecco.dominio.ebs.po.LineaOrdenCompra;
import ar.com.adecco.proveedores.daos.exception.DaoException;
import ar.com.adecco.proveedores.daos.helpers.ErrorMessage;
import java.util.List;
import ar.com.adecco.dominio.ebs.po.OrdenCompra;
import javax.inject.Named;
import ar.com.adecco.dominio.ebs.po.DistribucionOrdenCompra;

@Named
public class DistribucionOCDao extends AbstractDao<DistribucionOrdenCompra>
{
    private static final long serialVersionUID = 2329191755163410249L;
    
    public List<DistribucionOrdenCompra> getDistribucionesByOC(final OrdenCompra ordenCompra) throws DaoException {
        List resultList;
        try {
            resultList = this.getEntityManager().createQuery("from " + this.getClaseEntidad().getName() + " e where e.ordenCompra = :ordenCompra order by e.id desc").setParameter("ordenCompra", (Object)ordenCompra).getResultList();
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
        return (List<DistribucionOrdenCompra>)resultList;
    }
    
    public List<DistribucionOrdenCompra> getDistribucionesByLineaOC(final LineaOrdenCompra lineaOrdenCompra) throws DaoException {
        List resultList;
        try {
            resultList = this.getEntityManager().createQuery("from " + this.getClaseEntidad().getName() + " e where e.lineaOrdenCompra = :loc order by e.id desc").setParameter("loc", (Object)lineaOrdenCompra).getResultList();
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getClaseEntidad().getName());
        }
        return (List<DistribucionOrdenCompra>)resultList;
    }
    
    @Override
    public Class<DistribucionOrdenCompra> getClaseEntidad() {
        return DistribucionOrdenCompra.class;
    }
}
