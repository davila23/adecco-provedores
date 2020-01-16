// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.commons.transactions;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import javax.interceptor.InterceptorBinding;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.Annotation;

@Target({ ElementType.METHOD, ElementType.TYPE })
@InterceptorBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface Transactional {
}
