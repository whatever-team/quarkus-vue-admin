package io.lighting.handler;

import jakarta.annotation.Priority;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;
import io.lighting.common.logging.CorrelationContext;

/**
 * Inject a correlation id for each request and expose to responses/MD C.
 */
@Provider
@Priority(5)
public class RequestCorrelationFilter implements ContainerRequestFilter, ContainerResponseFilter {
    private static final Logger LOG = Logger.getLogger(RequestCorrelationFilter.class);

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String incoming = requestContext.getHeaderString(CorrelationContext.HEADER);
        String id = CorrelationContext.init(incoming);
        LOG.debugf("correlation id = %s, path=%s", id, requestContext.getUriInfo().getPath());
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        String id = CorrelationContext.current();
        if (id != null) {
            responseContext.getHeaders().putSingle(CorrelationContext.HEADER, id);
        }
        CorrelationContext.clear();
    }
}
