package analytics;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import ctrl.CtrlUtil;

/**
 * Application Lifecycle Listener implementation class Monitor
 *
 */
@WebListener
public class Monitor implements ServletRequestListener, HttpSessionListener
{

	/**
	 * Default constructor.
	 */
	public Monitor()
	{
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpSessionListener#sessionCreated(HttpSessionEvent)
	 */
	public void sessionCreated(HttpSessionEvent se)
	{
		// TODO Auto-generated method stub
		log_total_users(se);
	}

	private synchronized void log_total_users(HttpSessionEvent se)
	{
		ServletContext servletContext = se.getSession().getServletContext();
		Integer curr_total = (Integer) servletContext.getAttribute(CtrlUtil.USERS);

		curr_total = curr_total == null ? 1 : curr_total + 1;
		servletContext.setAttribute(CtrlUtil.USERS, curr_total);
	}

	/**
	 * @see ServletRequestListener#requestDestroyed(ServletRequestEvent)
	 */
	public void requestDestroyed(ServletRequestEvent sre)
	{
		// TODO Auto-generated method stub
	}

	/**
	 * @see ServletRequestListener#requestInitialized(ServletRequestEvent)
	 */
	public void requestInitialized(ServletRequestEvent sre)
	{
		// TODO Auto-generated method stub
		synchronized (this)
		{
			log_service_request(sre);
			record_S3_S4_use(sre);
		}

	}

	private synchronized void record_S3_S4_use(ServletRequestEvent sre)
	{
		HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();
		HttpSession session = request.getSession();
		Map<String, Integer> counter = (Map) session.getAttribute(CtrlUtil.SERVICE_COUNTER);
		
		//User has not used either S3 or S4
		if (counter == null || counter != null && (!counter.containsKey("Drone") || !counter.containsKey("Ride"))) 
			return;
		
		//increment only if User used either S3 or S4 for the first time. 
		if ((request.getServletPath().contains("Drone") && counter.get("Drone").equals(new Integer(1))) ||
			request.getServletPath().contains("Ride") && counter.get("Ride").equals(new Integer(1)))
		{
			ServletContext servlet_context = request.getServletContext();
			Integer num = (Integer) servlet_context.getAttribute(CtrlUtil.S3_S4_USERS);
			num = num==null? 1 : num+1;
			servlet_context.setAttribute(CtrlUtil.S3_S4_USERS, new Integer(num));
		}
			
	}

	private synchronized void log_service_request(ServletRequestEvent sre)
	{
		HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();

		if (request.getParameter("calc") == null || request.getParameter("recalc") == null)
		{
			ServletContext servletContext = request.getServletContext();
			HttpSession session = request.getSession();

			String service = request.getServletPath().replaceAll("/", "");
			int idx = service.indexOf(".");
			service = service.substring(0, idx);

			// log into Server scope
			if (servletContext.getAttribute(CtrlUtil.SERVICE_COUNTER) == null)
			{
				Map<String, Integer> counter_temp = new HashMap<String, Integer>();
				servletContext.setAttribute(CtrlUtil.SERVICE_COUNTER, counter_temp);
			}
			Map<String, Integer> counter = (Map<String, Integer>) servletContext.getAttribute(CtrlUtil.SERVICE_COUNTER);
			Integer curr = (Integer) counter.get(service);
			curr = curr == null ? 0 : curr;
			counter.put(service, new Integer(++curr));

			System.out.println("New Request updated on Session: ");
			Set<String> keys = counter.keySet();
			for (String key : keys)
			{
				System.out.println(" " + key + "=" + counter.get(key));
			}

			// log into Session scope
			if (session.getAttribute(CtrlUtil.SERVICE_COUNTER) == null)
			{
				Map<String, Integer> counter_temp = new HashMap<String, Integer>();
				session.setAttribute(CtrlUtil.SERVICE_COUNTER, counter_temp);
			}
			counter = (Map) session.getAttribute(CtrlUtil.SERVICE_COUNTER);
			curr = (Integer) counter.get(service);
			curr = curr == null ? 0 : curr;
			counter.put(service, new Integer(++curr));
		}
	}

	/**
	 * @see HttpSessionListener#sessionDestroyed(HttpSessionEvent)
	 */
	public void sessionDestroyed(HttpSessionEvent se)
	{
		// TODO Auto-generated method stub
	}

}
