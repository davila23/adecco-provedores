// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.utils;

import javax.transaction.NotSupportedException;
import javax.transaction.HeuristicMixedException;
import javax.transaction.RollbackException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.SystemException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletRequestEvent;
import org.jboss.seam.transaction.DefaultTransaction;
import org.jboss.seam.transaction.SeamTransaction;
import javax.inject.Inject;
import org.jboss.solder.exception.control.ExceptionToCatch;
import javax.enterprise.event.Event;
import org.jboss.solder.logging.Logger;
import javax.servlet.annotation.WebListener;
import javax.servlet.ServletRequestListener;

@WebListener
public class TransactionServletListener implements ServletRequestListener
{

	@Override
	public void requestDestroyed(ServletRequestEvent sre) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestInitialized(ServletRequestEvent sre) {
		// TODO Auto-generated method stub
		
	}
  /*  private static final Logger LOG;
    @Inject
    Event<ExceptionToCatch> txException;
    @Inject
    @DefaultTransaction
    private SeamTransaction tx;
    
    public void requestDestroyed(final ServletRequestEvent servletRequestEvent) {
        final String servletPath = ((HttpServletRequest)servletRequestEvent.getServletRequest()).getServletPath();
        if (servletPath.startsWith("/javax.faces.resource/") || !servletPath.endsWith(".jsf")) {
            return;
        }
        try {
            switch (this.tx.getStatus()) {
                case 0: {
                    this.tx.commit();
                    break;
                }
                case 1:
                case 2:
                case 7: {
                    this.tx.rollback();
                    break;
                }
            }
        }
        catch (SystemException ex) {
        	  //            TransactionServletListener.LOG.warn((Object)"Error rolling back the transaction", (Throwable)ex);
            this.txException.fire(new ExceptionToCatch((Throwable)ex));
        }
        catch (HeuristicRollbackException ex2) {
        	  //           TransactionServletListener.LOG.warn((Object)"Error committing the transaction", (Throwable)ex2);
            this.txException.fire(new ExceptionToCatch((Throwable)ex2));
        }
        catch (RollbackException ex3) {
        	  //            TransactionServletListener.LOG.warn((Object)"Error committing the transaction", (Throwable)ex3);
            this.txException.fire(new ExceptionToCatch((Throwable)ex3));
        }
        catch (HeuristicMixedException ex4) {
        	  //            TransactionServletListener.LOG.warn((Object)"Error committing the transaction", (Throwable)ex4);
            this.txException.fire(new ExceptionToCatch((Throwable)ex4));
        }
    }
    
    public void requestInitialized(final ServletRequestEvent servletRequestEvent) {
        final String servletPath = ((HttpServletRequest)servletRequestEvent.getServletRequest()).getServletPath();
        if (servletPath.startsWith("/javax.faces.resource/") || !servletPath.endsWith(".jsf")) {
            return;
        }
        try {
            final int status = this.tx.getStatus();
            if (status == 1 || status == 4) {
            	  //         TransactionServletListener.LOG.warn("Transaction was already started before the listener and is marked for rollback or rolled back from other thread, so doing rollback to disassociate it with current thread");
                this.tx.rollback();
            }
            else if (status != 6) {
            	  //         TransactionServletListener.LOG.warnv("Transaction was already started before the listener. Transaction status: {0}", (Object)status);
            }
            if (this.tx.getStatus() == 0) {
            	  //         TransactionServletListener.LOG.warn("Transaction was already started before the listener");
            }
            else {
                this.tx.begin();
            }
        }
        catch (SystemException ex) {
        	  //      TransactionServletListener.LOG.warn("Error starting the transaction, or checking status", (Throwable)ex);
            this.txException.fire(new ExceptionToCatch((Throwable)ex));
        }
        catch (NotSupportedException ex2) {
        	  //       TransactionServletListener.LOG.warn("Error starting the transaction", ex2);
            this.txException.fire(new ExceptionToCatch(ex2));
        }
    }
    
    static {
   //     LOG = Logger.getLogger(TransactionServletListener.class);
    }*/
}
