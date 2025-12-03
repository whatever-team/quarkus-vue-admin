package xyz.easyboot.common.context;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.UriInfo;
import lombok.Data;

@Data
@RequestScoped
public class HttpContext {

    @Inject
    @Context
    UriInfo uriInfo;

    @Inject
    @Context
    HttpHeaders httpHeaders;

}
