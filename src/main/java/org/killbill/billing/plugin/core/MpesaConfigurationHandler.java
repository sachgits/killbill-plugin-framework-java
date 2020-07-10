package org.killbill.billing.plugin.core;

import java.security.GeneralSecurityException;
import java.util.Properties;

import org.killbill.billing.osgi.libs.killbill.OSGIKillbillAPI;
import org.killbill.billing.osgi.libs.killbill.OSGIKillbillLogService;
import org.killbill.billing.plugin.api.notification.PluginTenantConfigurableConfigurationHandler;
import org.killbill.billing.plugin.client.MpesaClientWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.annotations.VisibleForTesting;

public class MpesaConfigurationHandler extends PluginTenantConfigurableConfigurationHandler<MpesaClientWrapper>{

    private static final String PROPERTY_PREFIX = "org.killbill.billing.plugin.kbsafcom.";

    private static final Logger logger = LoggerFactory.getLogger(MpesaConfigurationHandler.class);
    

    public MpesaConfigurationHandler(final String pluginName,
                                       final OSGIKillbillAPI osgiKillbillAPI,
                                       final OSGIKillbillLogService osgiKillbillLogService) {
        super(pluginName, osgiKillbillAPI, osgiKillbillLogService);
    }

    @Override
    @VisibleForTesting
    public MpesaClientWrapper createConfigurable(final Properties properties) {
        final String proxyPort = properties.getProperty(PROPERTY_PREFIX + "proxyPort");
        try {
            return new MpesaClientWrapper(properties.getProperty(PROPERTY_PREFIX + "paymentUrl"),
                                            properties.getProperty(PROPERTY_PREFIX + "apiKey"),
                                            properties.getProperty(PROPERTY_PREFIX + "apiSecret"),
                                            properties.getProperty(PROPERTY_PREFIX + "proxyHost"),
                                            proxyPort == null ? null : Integer.valueOf(proxyPort),
                                            Boolean.valueOf(properties.getProperty(PROPERTY_PREFIX + "trustAllCertificates", "true")));
        } catch (final GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

}