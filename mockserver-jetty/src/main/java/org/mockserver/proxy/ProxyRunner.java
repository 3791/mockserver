package org.mockserver.proxy;

import com.google.common.annotations.VisibleForTesting;
import org.eclipse.jetty.server.ForwardedRequestCustomizer;
import org.eclipse.jetty.server.HttpConfiguration;
import org.mockserver.model.HttpRequest;
import org.mockserver.proxy.filters.ProxyRequestFilter;
import org.mockserver.proxy.filters.ProxyResponseFilter;
import org.mockserver.runner.AbstractRunner;

import javax.servlet.http.HttpServlet;

/**
 * @author jamesdbloom
 */
public class ProxyRunner extends AbstractRunner {

    @VisibleForTesting
    ProxyServlet proxyServlet = new ProxyServlet();

    protected HttpServlet getServlet() {
        return proxyServlet;
    }

    @Override
    protected void extendHTTPConfig(HttpConfiguration https_config) {
        https_config.addCustomizer(new ForwardedRequestCustomizer());
    }

    /**
     * Add filter for HTTP requests, each filter get called before each request is proxied, if the filter return null then the request is not proxied
     *
     * @param httpRequest the request to match against for this filter
     * @param filter the filter to execute for this request, if the filter returns null the request will not be proxied
     */
    public ProxyRunner withFilter(HttpRequest httpRequest, ProxyRequestFilter filter) {
        proxyServlet.withFilter(httpRequest, filter);
        return this;
    }

    /**
     * Add filter for HTTP response, each filter get called after each request has been proxied
     *
     * @param httpRequest the request to match against for this filter
     * @param filter the filter that is executed after this request has been proxied
     */
    public ProxyRunner withFilter(HttpRequest httpRequest, ProxyResponseFilter filter) {
        proxyServlet.withFilter(httpRequest, filter);
        return this;
    }
}
