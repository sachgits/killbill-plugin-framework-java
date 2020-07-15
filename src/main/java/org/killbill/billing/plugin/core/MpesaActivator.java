package org.killbill.billing.plugin.core;

import java.util.Hashtable;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;

import org.killbill.billing.osgi.api.OSGIPluginProperties;
import org.killbill.billing.osgi.libs.killbill.KillbillActivatorBase;
import org.killbill.billing.payment.plugin.api.PaymentPluginApi;
import org.killbill.billing.plugin.api.notification.PluginConfigurationEventHandler;
import org.killbill.billing.plugin.api.payment.MpesaPluginPaymentApi;
import org.killbill.billing.plugin.client.MpesaClientWrapper;
import org.killbill.billing.plugin.client.MpesaConfigProperties;
import org.killbill.billing.plugin.core.resources.MpesaConfirmationServlet;
import org.killbill.billing.plugin.core.resources.jooby.PluginApp;
import org.killbill.billing.plugin.core.resources.jooby.PluginAppBuilder;
import org.killbill.billing.plugin.dao.MpesaDao;
import org.killbill.billing.plugin.models.RegisterURLRequest;
import org.killbill.billing.plugin.models.RegisterURLResponse;
import org.killbill.clock.Clock;
import org.killbill.clock.DefaultClock;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MpesaActivator extends KillbillActivatorBase{

    public static final String PLUGIN_NAME = "killbill-safcom";

    private MpesaConfigurationHandler mpesaConfigurationHandler;
    private MpesaConfigPropertiesConfigHandler mPropertiesConfigHandler;

    private static final Logger logger = LoggerFactory.getLogger(MpesaActivator.class);
    @Override
    public void start(final BundleContext context) throws Exception {
        super.start(context);

        final Clock clock = new DefaultClock();
        final MpesaDao dao = new MpesaDao(dataSource.getDataSource());

        // Register the servlet
        final PluginApp pluginApp = new PluginAppBuilder(PLUGIN_NAME,
                                                         killbillAPI,
                                                         logService,
                                                         dataSource,
                                                         super.clock,
                                                         configProperties).withRouteClass(MpesaServlet.class)
                                                                          .withRouteClass(MpesaConfirmationServlet.class)
                                                                          .build();
        final HttpServlet mpesaServlet= PluginApp.createServlet(pluginApp);
        registerServlet(context, mpesaServlet);



        mpesaConfigurationHandler = new MpesaConfigurationHandler(PLUGIN_NAME, killbillAPI, logService);
        mPropertiesConfigHandler = new MpesaConfigPropertiesConfigHandler(PLUGIN_NAME, killbillAPI, logService);

        final MpesaClientWrapper globalMpesaClient = mpesaConfigurationHandler.createConfigurable(configProperties.getProperties());
        mpesaConfigurationHandler.setDefaultConfigurable(globalMpesaClient);
        final MpesaConfigProperties configProps = mPropertiesConfigHandler.createConfigurable(configProperties.getProperties());
        mPropertiesConfigHandler.setDefaultConfigurable(configProps);

        //Register MpesaRegisterUrl
        RegisterURLRequest rUrlRequest = new RegisterURLRequest(configProps.getMerchantAccounts(), configProps.getConfirmationURL(),
         configProps.getValidationURL());
        RegisterURLResponse rUrlResponse = globalMpesaClient.registerMPesaPaybillRequest(rUrlRequest);
        logger.info("response from safcom RegisterURL '{}'", rUrlResponse);

        // Register the payment plugin
        final PaymentPluginApi paymentPluginApi = new MpesaPluginPaymentApi(mpesaConfigurationHandler, killbillAPI, configProperties, logService, clock, dao);
        registerPaymentPluginApi(context, paymentPluginApi);

        registerHandlers();
    }

    public void registerHandlers() {
        final PluginConfigurationEventHandler handler = new PluginConfigurationEventHandler(mpesaConfigurationHandler,mPropertiesConfigHandler);
        dispatcher.registerEventHandlers(handler);
    }

    private void registerServlet(final BundleContext context, final HttpServlet servlet) {
        final Hashtable<String, String> props = new Hashtable<String, String>();
        props.put(OSGIPluginProperties.PLUGIN_NAME_PROP, PLUGIN_NAME);
        registrar.registerService(context, Servlet.class, servlet, props);
    }

    private void registerPaymentPluginApi(final BundleContext context, final PaymentPluginApi api) {
        final Hashtable<String, String> props = new Hashtable<String, String>();
        props.put(OSGIPluginProperties.PLUGIN_NAME_PROP, PLUGIN_NAME);
        registrar.registerService(context, PaymentPluginApi.class, api, props);
    }

    
}