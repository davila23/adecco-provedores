// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.daos;

import com.google.common.base.Strings;
import javax.persistence.Query;
import ar.com.adecco.proveedores.daos.exception.DaoException;
import ar.com.adecco.proveedores.daos.helpers.ErrorMessage;
import java.util.Map;
import java.util.HashMap;
import ar.com.adecco.proveedores.filters.ComprobanteFilter;
import java.util.Iterator;
import ar.com.adecco.dominio.ebs.ap.Cheque;
import java.util.Date;
import ar.com.adecco.dominio.ebs.ap.Chequera;
import java.util.List;
import javax.persistence.NoResultException;
import java.sql.CallableStatement;
import java.sql.Connection;
import org.hibernate.service.jdbc.connections.spi.ConnectionProvider;
import java.sql.SQLException;
import org.hibernate.Session;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import ar.com.adecco.proveedores.daos.exception.ServiceException;
import ar.com.adecco.proveedores.daos.exception.InterfazException;
import ar.com.syntagma.helpers.HolderHelper;
import ar.com.adecco.proveedores.model.Comprobante;
import javax.inject.Named;
import ar.com.adecco.dominio.ebs.ap.Factura;
import ar.com.adecco.proveedores.daos.base.AbstractEntityDao;

@Named
public class FacturaEbsDao extends AbstractEntityDao<Factura>
{
    private static final long serialVersionUID = -5574787820956791509L;
    
    public Factura addFactura(final Comprobante comprobante, final long n, final HolderHelper<String> holderHelper) throws InterfazException, ServiceException {
        switch (comprobante.getTipoComprobante()) {
            case FC: {
                return (Factura)this.callInterfazFactura(InterfaceFunction.CREAR_FACTURA, comprobante.getId(), n, holderHelper);
            }
            case NC: {
                return (Factura)this.callInterfazFactura(InterfaceFunction.CREAR_NOTACREDITO, comprobante.getId(), n, holderHelper);
            }
            case ND: {
                return (Factura)this.callInterfazFactura(InterfaceFunction.CREAR_NOTADEBITO, comprobante.getId(), n, holderHelper);
            }
            default: {
                return null;
            }
        }
    }
    
    public Boolean liberarFactura(final Comprobante comprobante, final long n, final HolderHelper<String> holderHelper) throws InterfazException, ServiceException {
        return (Boolean)this.callInterfazFactura(InterfaceFunction.LIBERAR_FACTURA, comprobante.getComprobanteEbs().getId(), n, holderHelper);
    }
    
    private Object callInterfazFactura(final InterfaceFunction interfaceFunction, final long n, final long n2, final HolderHelper<String> holderHelper) throws InterfazException, ServiceException {
        final ConnectionProvider connectionProvider = ((SessionFactoryImplementor)((Session)this.entityManager.getDelegate()).getSessionFactory()).getConnectionProvider();
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
            prepareCall = connection.prepareCall("{ ? = call APPS.INTERFAZ_AP_COMPRAS." + interfaceFunction.getFunctionName() + "(?, ?, ?) }");
        }
        catch (SQLException ex3) {
            ex3.printStackTrace();
            final InterfazException ex4 = new InterfazException("Error al llamar la funci\u00f3n de creaci\u00f3n de la factura: " + ex3.getMessage());
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
            prepareCall.setLong(2, n);
        }
        catch (SQLException ex7) {
            ex7.printStackTrace();
            final InterfazException ex8 = new InterfazException("Error al declarar el par\u00e1metro de Id de la factura: " + ex7.getMessage());
            ex8.setCodigoError(ex7.getErrorCode());
            throw ex8;
        }
        try {
            prepareCall.setLong(3, n2);
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
            System.out.println("Ejecutando llamada...");
            System.out.println("call APPS.INTERFAZ_AP_COMPRAS." + interfaceFunction.getFunctionName() + "(" + n + ", " + n2 + ", retorno)...");
            prepareCall.execute();
            System.out.println("Llamada ejecutada.");
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
            final InterfazException ex18 = new InterfazException("Error al obtener el id de la factura EBS generada: " + ex17.getMessage());
            ex18.setCodigoError(ex17.getErrorCode());
            try {
                connection.close();
            }
            catch (SQLException ex20) {
                ex17.printStackTrace();
                final InterfazException ex19 = new InterfazException("Error al cerrar la conexi\u00f3n: " + ex20.getMessage());
                ex19.setCodigoError(ex20.getErrorCode());
                throw ex19;
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
        switch (interfaceFunction) {
            case LIBERAR_FACTURA: {
                holderHelper.set(string);
                return long1 != 0L;
            }
            default: {
                holderHelper.set(string);
                return (long1 != 0L) ? this.getById(long1) : null;
            }
        }
    }
    
    public Factura getById(final long n) {
        try {
            return (Factura)this.entityManager.find((Class)this.getEntityClass(), (Object)n);
        }
        catch (NoResultException ex) {
            return null;
        }
    }
    
    public Factura getById(final long n, final boolean b) {
        if (!b) {
            return this.getById(n);
        }
        try {
            return (Factura)this.entityManager.createQuery("select fc from ar.com.adecco.dominio.ebs.ap.Factura fc join fetch fc.compania left join fetch fc.distribuciones fcd left join fetch fcd.impuesto where fc.id = :id", (Class)Factura.class).setParameter("id", (Object)n).getSingleResult();
        }
        catch (NoResultException ex) {
            return null;
        }
    }
    
    public Cheque pagar(final List<Factura> list, final Chequera chequera, final Date date, final long n) throws InterfazException {
        System.out.println("FacturaEbsDao.pagar() - Inicio");
        final ConnectionProvider connectionProvider = ((SessionFactoryImplementor)((Session)this.entityManager.getDelegate()).getSessionFactory()).getConnectionProvider();
        final StringBuilder sb = new StringBuilder();
        final Iterator<Factura> iterator = list.iterator();
        while (iterator.hasNext()) {
            sb.append(iterator.next().getId()).append(",");
        }
        sb.setLength(sb.length() - 1);
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
            prepareCall = connection.prepareCall("{ ? = call APPS.interfaz_ap_compras.crearPago (?, ?, ?, ?, ?) }");
        }
        catch (SQLException ex3) {
            ex3.printStackTrace();
            final InterfazException ex4 = new InterfazException("Error al llamar la funci\u00f3n de creaci\u00f3n de factura: " + ex3.getMessage());
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
            prepareCall.setString(2, sb.toString());
        }
        catch (SQLException ex7) {
            ex7.printStackTrace();
            final InterfazException ex8 = new InterfazException("Error al declarar el par\u00e1metro de Id de factura: " + ex7.getMessage());
            ex8.setCodigoError(ex7.getErrorCode());
            throw ex8;
        }
        try {
            prepareCall.setLong(3, chequera.getId());
        }
        catch (SQLException ex9) {
            ex9.printStackTrace();
            final InterfazException ex10 = new InterfazException("Error al declarar el par\u00e1metro de Id de chequera: " + ex9.getMessage());
            ex10.setCodigoError(ex9.getErrorCode());
            throw ex10;
        }
        try {
            prepareCall.setLong(4, n);
        }
        catch (SQLException ex11) {
            ex11.printStackTrace();
            final InterfazException ex12 = new InterfazException("Error al declarar el par\u00e1metro de Id de Usuario EBS: " + ex11.getMessage());
            ex12.setCodigoError(ex11.getErrorCode());
            throw ex12;
        }
        try {
            prepareCall.registerOutParameter(5, 12);
        }
        catch (SQLException ex13) {
            ex13.printStackTrace();
            final InterfazException ex14 = new InterfazException("Error al declarar el par\u00e1metro de mensaje de error: " + ex13.getMessage());
            ex14.setCodigoError(ex13.getErrorCode());
            throw ex14;
        }
        try {
            prepareCall.setDate(6, new java.sql.Date(date.getTime()));
        }
        catch (SQLException ex15) {
            ex15.printStackTrace();
            final InterfazException ex16 = new InterfazException("Error al declarar el par\u00e1metro de fecha de pago: " + ex15.getMessage());
            ex16.setCodigoError(ex15.getErrorCode());
            throw ex16;
        }
        try {
            System.out.println("Ejecutando llamada...");
            System.out.println("call APPS.interfaz_ap_compras.crearPago ('" + sb.toString() + "', " + chequera.getId() + ", " + n + ", retorno, to_date('" + new java.sql.Date(date.getTime()).toString() + "', 'rrrr-mm-dd'))...");
            prepareCall.execute();
            System.out.println("Llamada ejecutada.");
        }
        catch (SQLException ex17) {
            ex17.printStackTrace();
            final InterfazException ex18 = new InterfazException("Error al ejecutar el package: " + ex17.getMessage());
            ex18.setCodigoError(ex17.getErrorCode());
            try {
                connection.close();
            }
            catch (SQLException ex20) {
                ex17.printStackTrace();
                final InterfazException ex19 = new InterfazException("Error al cerrar la conexi\u00f3n: " + ex20.getMessage());
                ex19.setCodigoError(ex20.getErrorCode());
                throw ex19;
            }
            throw ex18;
        }
        long long1;
        try {
            long1 = prepareCall.getLong(1);
        }
        catch (SQLException ex21) {
            ex21.printStackTrace();
            final InterfazException ex22 = new InterfazException("Error al obtener el id del pago generada: " + ex21.getMessage());
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
        String string;
        try {
            string = prepareCall.getString(5);
        }
        catch (SQLException ex25) {
            ex25.printStackTrace();
            final InterfazException ex26 = new InterfazException("Error al obtener el mensaje de error: " + ex25.getMessage());
            ex26.setCodigoError(ex25.getErrorCode());
            try {
                connection.close();
            }
            catch (SQLException ex28) {
                ex25.printStackTrace();
                final InterfazException ex27 = new InterfazException("Error al cerrar la conexi\u00f3n: " + ex28.getMessage());
                ex27.setCodigoError(ex28.getErrorCode());
                throw ex27;
            }
            throw ex26;
        }
        try {
            connection.close();
        }
        catch (SQLException ex29) {
            ex29.printStackTrace();
            final InterfazException ex30 = new InterfazException("Error al cerrar la conexi\u00f3n: " + ex29.getMessage());
            ex30.setCodigoError(ex29.getErrorCode());
            throw ex30;
        }
        System.out.println("result: " + long1 + " - msgError: " + string);
        if (long1 == 0L) {
            final InterfazException ex31 = new InterfazException(string);
            ex31.setCodigoError(long1);
            throw ex31;
        }
        System.out.println("Factura pagada.");
        System.out.println("Obteniendo el cheque generado...");
        return (Cheque)this.entityManager.find((Class)Cheque.class, (Object)long1);
    }
    
    public List<Factura> getByFilter(final ComprobanteFilter comprobanteFilter, final int n) throws DaoException {
        final StringBuilder sb = new StringBuilder();
        final HashMap<String, Object> hashMap = new HashMap<String, Object>();
        this.buildWhereAndParams(comprobanteFilter, sb, hashMap);
        List resultList;
        try {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("select * from (select ai.* from apps.ap_invoices_all ai, apps.po_vendors pv,COMPRAS.AP_INVOICES_READY_TO_PAY_ALL_V i where ai.vendor_id = pv.vendor_id and ai.invoice_id = i.invoice_id and pv.vendor_type_lookup_code = 'VENDOR' ").append(sb.toString()).append(") ai_ where rownum <= :limite order by ai_.INVOICE_DATE desc");
            final Query nativeQuery = this.entityManager.createNativeQuery(sb2.toString(), (Class)Factura.class);
            hashMap.put("limite", n + 1);
            for (final Map.Entry<String, Object> entry : hashMap.entrySet()) {
                nativeQuery.setParameter((String)entry.getKey(), (Object)entry.getValue());
            }
            resultList = nativeQuery.getResultList();
        }
        catch (Exception ex) {
            throw new DaoException(ErrorMessage.ERROR_BD.getMessage(), ex, this.getEntityClass().getName());
        }
        return (List<Factura>)resultList;
    }
    
    private void buildWhereAndParams(final ComprobanteFilter comprobanteFilter, final StringBuilder sb, final Map<String, Object> map) {
        if (!Strings.isNullOrEmpty(comprobanteFilter.getNumeroDesde())) {
            sb.append(" and ai.invoice_num >= :numeroDesde");
            map.put("numeroDesde", comprobanteFilter.getNumeroDesde());
        }
        if (!Strings.isNullOrEmpty(comprobanteFilter.getNumeroHasta())) {
            sb.append(" and ai.invoice_num <= :numeroHasta");
            map.put("numeroHasta", comprobanteFilter.getNumeroHasta());
        }
        if (comprobanteFilter.getFechaDesde() != null) {
            sb.append(" and ai.invoice_date >= :fechaDesde");
            map.put("fechaDesde", comprobanteFilter.getFechaDesde());
        }
        if (comprobanteFilter.getFechaHasta() != null) {
            sb.append(" and ai.invoice_date <= :fechaHasta");
            map.put("fechaHasta", comprobanteFilter.getFechaHasta());
        }
        if (!Strings.isNullOrEmpty(comprobanteFilter.getProveedorNombre())) {
            sb.append(" and lower(pv.vendor_name) like :proveedor");
            map.put("proveedor", '%' + comprobanteFilter.getProveedorNombre().toLowerCase() + '%');
        }
        if (!Strings.isNullOrEmpty(comprobanteFilter.getProveedorCuit())) {
            sb.append(" and pv.segment1 like :cuit");
            map.put("cuit", '%' + comprobanteFilter.getProveedorCuit().replaceAll("-", "") + '%');
        }
        if (comprobanteFilter.getProveedorId() != null) {
            sb.append(" and pv.VENDOR_ID = :prId");
            map.put("prId", comprobanteFilter.getProveedorId());
        }
        if (comprobanteFilter.getCompaniaId() != null) {
            sb.append(" and ai.org_id = :companiaId");
            map.put("companiaId", comprobanteFilter.getCompaniaId());
        }
    }
    
    public enum InterfaceFunction
    {
        CREAR_FACTURA("crearFactura"), 
        LIBERAR_FACTURA("liberarFactura"), 
        CREAR_NOTACREDITO("crearNotaCredito"), 
        CREAR_NOTADEBITO("crearNotaDebito");
        
        String functionName;
        
        private InterfaceFunction(final String functionName) {
            this.functionName = functionName;
        }
        
        public String getFunctionName() {
            return this.functionName;
        }
    }
}
