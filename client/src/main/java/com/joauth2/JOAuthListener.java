package com.joauth2;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@WebListener
public class JOAuthListener implements HttpSessionListener, HttpSessionAttributeListener {

	private static Log log = LogFactory.get(JOAuthListener.class);
	
	@Override
	public void attributeAdded(HttpSessionBindingEvent event) {

	}

	@Override
	public void attributeRemoved(HttpSessionBindingEvent event) {
	
	}

	@Override
	public void attributeReplaced(HttpSessionBindingEvent arg0) {
	
	}

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		log.info("session created");
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		if (Attr.getTotalUser() > 0) {
			Attr.setTotalUser(Attr.getTotalUser() - 1);
		}

		HttpSession session = se.getSession();

		// 删除授权平台上保存的登录信息
		if (session.getAttribute(OAuth2Constants.SESSION_LOGIN_RECORD_ID) != null) {
			int id = (Integer)session.getAttribute(OAuth2Constants.SESSION_LOGIN_RECORD_ID);
			ClientLogin.saveLogoutInfo(id);
		}

		// 清空session
		ClientUser user = null;
		if (session.getAttribute(OAuth2Constants.SESSION_CLIENT_ATTR) != null) {
			user = (ClientUser) session.getAttribute(OAuth2Constants.SESSION_CLIENT_ATTR);
			ClientLogin.logout(user.getId(), session, OAuth2Constants.SESSION_CLIENT_ATTR);
		}
	}



}
