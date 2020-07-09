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
import org.killbill.billing.plugin.dao.MpesaDao;
import org.killbill.clock.Clock;
import org.killbill.clock.DefaultClock;
import org.osgi.framework.BundleContext;

public class MpesaActivator extends KillbillActivatorBase{

    public static final String PLUGIN_NAME = "killbill-safcom";

    private MpesaConfigurationHandler mpesaConfigurationHandler;

    @Override
    public void start(final BundleContext context) throws Exception {
        super.start(context);

        final Clock clock = new DefaultClock();
        final MpesaDao dao = new MpesaDao(dataSource.getDataSource());

        // Register the servlet
        final MpesaServlet mpesaServlet = new MpesaServlet();
        registerServlet(context, mpesaServlet);

        mpesaConfigurationHandler = new MpesaConfigurationHandler(PLUGIN_NAME, killbillAPI, logService);

        final MpesaClientWrapper globalMpesaClient = mpesaConfigurationHandler.createConfigurable(configProperties.getProperties());
        mpesaConfigurationHandler.setDefaultConfigurable(globalMpesaClient);

        // Register the payment plugin
        final PaymentPluginApi paymentPluginApi = new MpesaPluginPaymentApi(mpesaConfigurationHandler, killbillAPI, configProperties, logService, clock, dao);
        registerPaymentPluginApi(context, paymentPluginApi);

        registerHandlers();
    }

    public void registerHandlers() {
        final PluginConfigurationEventHandler handler = new PluginConfigurationEventHandler(mpesaConfigurationHandler);
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