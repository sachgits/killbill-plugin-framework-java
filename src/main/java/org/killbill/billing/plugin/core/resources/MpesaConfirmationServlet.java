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
import org.killbill.billing.plugin.client.MpesaClientWrapper;
import org.killbill.billing.plugin.client.MpesaConfigProperties;
import org.killbill.billing.plugin.core.MpesaActivator;
import org.killbill.billing.plugin.core.MpesaConfigurationHandler;
import org.killbill.billing.plugin.core.PluginServlet;

import org.killbill.billing.tenant.api.Tenant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.inject.Inject;

@Singleton
@Path("/confirmation")
public class MpesaConfirmationServlet extends PluginServlet {

    private static final Logger logger = LoggerFactory.getLogger(MpesaConfirmationServlet.class);

    @Inject
    public MpesaConfirmationServlet(){
        super();
        
    }

    @GET
    public Result statusOk(){
        logger.info("Confirmation Message arrived");
        return Results.with(Status.OK)
                    .type(MediaType.json);
    }


}