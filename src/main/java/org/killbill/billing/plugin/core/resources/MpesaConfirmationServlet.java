package org.killbill.billing.plugin.core.resources;
import java.util.Optional;

import javax.inject.Named;
import javax.inject.Singleton;

import org.jooby.Result;
import org.jooby.Results;
import org.jooby.MediaType;
import org.jooby.Status;
import org.jooby.mvc.GET;
import org.jooby.mvc.Local;
import org.jooby.mvc.Path;
import org.killbill.billing.plugin.client.MpesaConfigProperties;
import org.killbill.billing.plugin.core.PluginServlet;

import org.killbill.billing.tenant.api.Tenant;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.inject.Inject;

@Singleton
@Path("/confirmation")
public class MpesaConfirmationServlet extends PluginServlet {

    private MpesaConfigProperties config;

    @Inject
    public MpesaConfirmationServlet(final MpesaConfigProperties props){
        super();
        this.config = props;
    }

    @GET
    public Result statusOk(){
        return Results.with(Status.OK)
                    .type(MediaType.json);
    }


}