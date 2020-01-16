// 
// Decompiled by Procyon v0.5.36
// 

package ar.com.adecco.proveedores.utils;

import java.io.IOException;
import org.jasig.cas.client.util.CommonUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.FilterChain;
import javax.servlet.ServletResponse;
import javax.servlet.ServletRequest;
import org.jasig.cas.client.session.SessionMappingStorage;
import javax.servlet.ServletException;
import javax.servlet.FilterConfig;
import org.jasig.cas.client.session.SingleSignOutHandler;
import org.jasig.cas.client.util.AbstractConfigurationFilter;

public final class CustomSingleSignOutFilter extends AbstractConfigurationFilter
{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}}