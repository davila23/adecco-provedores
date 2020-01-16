// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.daos;

import ar.com.adecco.dominio.ebs.ap.Factura;
import java.util.List;
import java.sql.CallableStatement;
import java.sql.Connection;
import org.hibernate.service.jdbc.connections.spi.ConnectionProvider;
import ar.com.adecco.proveedores.daos.helpers.ErrorMessage;
import ar.com.adecco.proveedores.daos.exception.DaoException;
import java.sql.SQLException;
import org.hibernate.Session;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import ar.com.adecco.proveedores.daos.exception.ServiceException;
import ar.com.adecco.proveedores.daos.exception.InterfazException;
import ar.com.syntagma.helpers.HolderHelper;
import javax.inject.Named;
import ar.com.adecco.dominio.ebs.po.Proveedor;

@Named
public class ProveedorEBSDao extends AbstractDao<Proveedor>
{
    private static final long serialVersionUID = 1100012874450587549L;
    private static final String CREAR_PROVEEDOR = "crearProveedor";
    private static final String ACTUALIZAR_PROVEEDOR = "actualizarProveedor";
    
    @Override
    public Class<Proveedor> getClaseEntidad() {
        return Proveedor.class;
    }
    
    public Proveedor addProveedor(final ar.com.adecco.proveedores.model.Proveedor proveedor, final long n, final HolderHelper<String> holderHelper) throws InterfazException, ServiceException {
        return this.callInterfazProveedor("crearProveedor", proveedor, n, holderHelper);
    }
    
    public Proveedor actualizarProveedor(final ar.com.adecco.proveedores.model.Proveedor proveedor, final long n, final HolderHelper<String> holderHelper) throws InterfazException, ServiceException {
        return this.callInterfazProveedor("actualizarProveedor", proveedor, n, holderHelper);
    }
    
    private Proveedor callInterfazProveedor(final String s, final ar.com.adecco.proveedores.model.Proveedor proveedor, final long n, final HolderHelper<String> holderHelper) throws InterfazException, ServiceException {
        final ConnectionProvider connectionProvider = ((SessionFactoryImplementor)((Session)this.getEntityManager().getDelegate()).getSessionFactory()).getConnectionProvider();
        Connection connection;
        try {
            connection = connectionProvider.getConnection();
        }
        catch (SQLException ex) {
            ex.printStackTrace();
            final InterfazException ex2 = new InterfazException("Error al obtener conexi\u00f3n: " + ex.getMessage());
            ex2.setCodigoError(ex.getErrorCode());
            throw ex2;
        }
        CallableStatement prepareCall;
        try {
            prepareCall = connection.prepareCall("{ ? = call APPS.INTERFAZ_AP_PROVEEDORES." + s + "(?, ?, ?) }");
        }
        catch (SQLException ex3) {
            ex3.printStackTrace();
            final InterfazException ex4 = new InterfazException("Error al llamar la funci\u00f3n de creaci\u00f3n del proveedor: " + ex3.getMessage());
            ex4.setCodigoError(ex3.getErrorCode());
            throw ex4;
        }
        try {
            prepareCall.registerOutParameter(1, 4);
        }
        catch (SQLException ex5) {
            ex5.printStackTrace();
            final InterfazException ex6 = new InterfazException("Error al declarar el par\u00e1metro de salida: " + ex5.getMessage());
            ex6.setCodigoError(ex5.getErrorCode());
            throw ex6;
        }
        try {
            prepareCall.setLong(2, proveedor.getId());
        }
        catch (SQLException ex7) {
            ex7.printStackTrace();
            final InterfazException ex8 = new InterfazException("Error al declarar el par\u00e1metro de Id del proveedor: " + ex7.getMessage());
            ex8.setCodigoError(ex7.getErrorCode());
            throw ex8;
        }
        try {
            prepareCall.setLong(3, n);
        }
        catch (SQLException ex9) {
            ex9.printStackTrace();
            final InterfazException ex10 = new InterfazException("Error al declarar el par\u00e1metro de Id del usuario: " + ex9.getMessage());
            ex10.setCodigoError(ex9.getErrorCode());
            throw ex10;
        }
        try {
            prepareCall.registerOutParameter(4, 12);
        }
        catch (SQLException ex11) {
            ex11.printStackTrace();
            final InterfazException ex12 = new InterfazException("Error al declarar el par\u00e1metro de mensaje de error: " + ex11.getMessage());
            ex12.setCodigoError(ex11.getErrorCode());
            throw ex12;
        }
        try {
            prepareCall.execute();
        }
        catch (SQLException ex13) {
            ex13.printStackTrace();
            final InterfazException ex14 = new InterfazException("Error al ejecutar el package: " + ex13.getMessage());
            ex14.setCodigoError(ex13.getErrorCode());
            try {
                connection.close();
            }
            catch (SQLException ex16) {
                ex13.printStackTrace();
                final InterfazException ex15 = new InterfazException("Error al cerrar la conexi\u00f3n: " + ex16.getMessage());
                ex15.setCodigoError(ex16.getErrorCode());
                throw ex15;
            }
            throw ex14;
        }
        long long1;
        try {
            long1 = prepareCall.getLong(1);
        }
        catch (SQLException ex17) {
            ex17.printStackTrace();
            final InterfazException ex18 = new InterfazException("Error al obtener el Id del proveedor EBS generado: " + ex17.getMessage());
            ex18.setCodigoError(ex17.getErrorCode());
            try {
                connection.close();
            }
            catch (SQLException ex19) {
                ex19.printStackTrace();
                final InterfazException ex20 = new InterfazException("Error al cerrar la conexi\u00f3n: " + ex19.getMessage());
                ex20.setCodigoError(ex19.getErrorCode());
                throw ex20;
            }
            throw ex18;
        }
        String string;
        try {
            string = prepareCall.getString(4);
        }
        catch (SQLException ex21) {
            ex21.printStackTrace();
            final InterfazException ex22 = new InterfazException("Error al obtener el mensaje de error: " + ex21.getMessage());
            ex22.setCodigoError(ex21.getErrorCode());
            try {
                connection.close();
            }
            catch (SQLException ex24) {
                ex21.printStackTrace();
                final InterfazException ex23 = new InterfazException("Error al cerrar la conexi\u00f3n: " + ex24.getMessage());
                ex23.setCodigoError(ex24.getErrorCode());
                throw ex23;
            }
            throw ex22;
        }
        try {
            connection.close();
        }
        catch (SQLException ex25) {
            ex25.printStackTrace();
            final InterfazException ex26 = new InterfazException("Error al cerrar la conexi\u00f3n: " + ex25.getMessage());
            ex26.setCodigoError(ex25.getErrorCode());
            throw ex26;
        }
        if (long1 == 0L) {
            final InterfazException ex27 = new InterfazException(string);
            ex27.setCodigoError(long1);
            throw ex27;
        }
        holderHelper.set(string);
        try {
            return this.obtener(long1);
        }
        catch (DaoException ex28) {
            throw new ServiceException(ex28.getMessages().get(0), ex28, this.getClaseEntidad().getName());
        }
        catch (Exception ex29) {
            throw new ServiceException(ErrorMessage.ERROR_CRITICAL.getMessage(), ex29, this.getClaseEntidad().getName());
        }
    }
    
    public List<Object[]> getProveedoresPorFactura(final List<Factura> list) {
        return (List<Object[]>)this.getEntityManager().createQuery("select fac.id, fac.proveedor from ar.com.adecco.dominio.ebs.ap.Factura fac where fac in (:facturas)").setParameter("facturas", (Object)list).getResultList();
    }
}
