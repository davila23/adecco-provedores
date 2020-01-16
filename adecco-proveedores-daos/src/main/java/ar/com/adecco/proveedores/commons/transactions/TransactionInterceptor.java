// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.commons.transactions;

import org.apache.commons.lang.exception.ExceptionUtils;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import org.slf4j.LoggerFactory;
import javax.inject.Inject;
import javax.transaction.UserTransaction;
import org.slf4j.Logger;
import javax.interceptor.Interceptor;

@Transactional
@Interceptor
public class TransactionInterceptor
{
    private Logger logger;
    @Inject
    private UserTransaction userTransaction;
    
    public TransactionInterceptor() {
        this.logger = LoggerFactory.getLogger((Class)TransactionInterceptor.class);
    }
    
    @AroundInvoke
    public Object runInTransaction(final InvocationContext invocationContext) throws Exception {
        boolean beginTransactionAndMarkLevelDeep = true;
        System.out.println("Corre dentro de la transaccion");
        Object proceed;
        try {
            this.logger.debug("Transaction begin attempt at " + invocationContext.getMethod());
            beginTransactionAndMarkLevelDeep = this.beginTransactionAndMarkLevelDeep(beginTransactionAndMarkLevelDeep);
            proceed = invocationContext.proceed();
            this.logger.debug("Transaction commit attempt at " + invocationContext.getMethod());
            this.commitIfOuterTransactionAndIsActive(beginTransactionAndMarkLevelDeep);
        }
        catch (Exception ex) {
            this.logger.warn("Transaction rolled back at " + invocationContext.getMethod());
            this.rollbackIfOuterTransactionAndIsValid(beginTransactionAndMarkLevelDeep);
            throw this.getRootCause(ex);
        }
        return proceed;
    }
    
    private boolean beginTransactionAndMarkLevelDeep(boolean b) throws Exception {
        synchronized (this.userTransaction) {
            if (this.userTransaction.getStatus() == 6) {
                this.userTransaction.begin();
            }
            else {
                b = false;
            }
        }
        return b;
    }
    
    private void commitIfOuterTransactionAndIsActive(final boolean b) throws Exception {
        synchronized (this.userTransaction) {
            if (b && this.userTransaction.getStatus() == 0) {
                this.userTransaction.commit();
            }
        }
    }
    
    private void rollbackIfOuterTransactionAndIsValid(final boolean b) throws Exception {
        synchronized (this.userTransaction) {
            if (b && this.isTransactionValidToRollback()) {
                this.userTransaction.rollback();
            }
        }
    }
    
    private boolean isTransactionValidToRollback() throws Exception {
        final int status = this.userTransaction.getStatus();
        return status != 4 || status != 9 || status != 6;
    }
    
    private Exception getRootCause(final Exception ex) {
        final Throwable rootCause = ExceptionUtils.getRootCause((Throwable)ex);
        final Exception ex2 = (Exception)((rootCause != null) ? rootCause : ex);
        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Root cause of " + ex.getClass() + " resolved to " + ex2);
        }
        return ex2;
    }
}
