package it.polito.ai;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class HibernateFilter implements Filter {

	private SessionFactory sf;
	public void doFilter(ServletRequest request,
			ServletResponse response,
			FilterChain chain)
					throws IOException, ServletException {
		Session s=sf.getCurrentSession();
		Transaction tx=null;
		try {
			tx=s.beginTransaction();
			chain.doFilter(request, response);
			tx.commit();
		} catch (Throwable ex) {
			if (tx!=null) tx.rollback();
			throw new ServletException(ex);
		} finally {
			if (s!=null && s.isOpen()) s.close(); 
			s=null;
		}
	}
	public void destroy() {
		// TODO Auto-generated method stub

	}
	public void init(FilterConfig arg0) throws ServletException {
		sf = HibernateUtil.getSessionFactory();
	}


}
