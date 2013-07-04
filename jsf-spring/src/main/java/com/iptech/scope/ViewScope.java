package com.iptech.scope;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.logging.Logger;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PreDestroyViewMapEvent;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

/**
 * @author m.nikolaev Date: 21.11.12 Time: 0:37
 */
public class ViewScope implements Scope, Serializable, HttpSessionBindingListener {
	private static final long serialVersionUID = 130059382711925363L;
	private static final Logger LOGGER = Logger.getLogger("ViewScope");
    private final WeakHashMap<HttpSession, Set<ViewScopeViewMapListener>> sessionToListeners = new WeakHashMap<HttpSession, Set<ViewScopeViewMapListener>>();

    @Override
    public Object get(String name, ObjectFactory objectFactory) {
        Map<String, Object> viewMap = FacesContext.getCurrentInstance().getViewRoot().getViewMap();
        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (viewMap) {
            if (viewMap.containsKey(name)) {
                return viewMap.get(name);
            } else {
                LOGGER.warning("Creating bean {}"+ name);
                Object object = objectFactory.getObject();
                viewMap.put(name, object);

                return object;
            }
        }
    }

    @Override
    public Object remove(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getConversationId() {
        return null;
    }

    @Override
    public void registerDestructionCallback(String name, Runnable callback) {
        LOGGER.warning("registerDestructionCallback for bean {}"+ name);
        UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
        ViewScopeViewMapListener listener =
                new ViewScopeViewMapListener(viewRoot, name, callback, this);

        viewRoot.subscribeToViewEvent(PreDestroyViewMapEvent.class, listener);

        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        final Set<ViewScopeViewMapListener> sessionListeners;
        synchronized (sessionToListeners) {
            if (!sessionToListeners.containsKey(httpSession)) {
                sessionToListeners.put(httpSession, new HashSet<ViewScopeViewMapListener>());
            }
            sessionListeners = sessionToListeners.get(httpSession);
        }
        //noinspection SynchronizationOnLocalVariableOrMethodParameter
        synchronized (sessionListeners) {
            Set<ViewScopeViewMapListener> toRemove = new HashSet<ViewScopeViewMapListener>();
            for (ViewScopeViewMapListener viewMapListener : sessionListeners) {
                if (viewMapListener.checkRoot()) {
                    toRemove.add(viewMapListener);
                }
            }
            sessionListeners.removeAll(toRemove);
            sessionListeners.add(listener);
        }
        if (!FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("sessionBindingListener")) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("sessionBindingListener", this);
        }

    }

    @Override
    public Object resolveContextualObject(String key) {
        return null;
    }

    @Override
    public void valueBound(HttpSessionBindingEvent event) {
        LOGGER.warning("Session event bound {}"+ event.getName());
    }

    @Override
    public void valueUnbound(HttpSessionBindingEvent event) {
        LOGGER.warning("Session event unbound {}"+ event.getName());
        final Set<ViewScopeViewMapListener> listeners;
        synchronized (sessionToListeners) {
            if (sessionToListeners.containsKey(event.getSession())) {
                listeners = sessionToListeners.get(event.getSession());
                sessionToListeners.remove(event.getSession());
            } else {
                listeners = null;
            }
        }
        if (listeners != null) {
            for (ViewScopeViewMapListener listener : listeners) {
                listener.doCallback();
            }
        }
    }

    public void clearFromListener(ViewScopeViewMapListener listener) {
        LOGGER.warning("Removing listener from map");
        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if (httpSession != null) {
            synchronized (sessionToListeners) {
                if (sessionToListeners.containsKey(httpSession)) {
                    sessionToListeners.get(httpSession).remove(listener);
                }
            }
        }
    }

}