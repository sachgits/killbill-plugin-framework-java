package org.killbill.billing.plugin.core;

import java.security.GeneralSecurityException;
import java.util.Properties;

import org.killbill.billing.osgi.libs.killbill.OSGIKillbillAPI;
import org.killbill.billing.osgi.libs.killbill.OSGIKillbillLogService;
import org.killbill.billing.plugin.api.notification.PluginTenantConfigurableConfigurationHandler;
import org.killbill.billing.plugin.client.MpesaClientWrapper;
import org.killbill.billing.plugin.client.MpesaConfigProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MpesaConfigPropertiesConfigHandler  extends PluginTenantConfigurableConfigurationHandler<MpesaConfigProperties>{
    
    private static final Logger logger = LoggerFactory.getLogger(MpesaConfigPropertiesConfigHandler.class);

    public MpesaConfigPropertiesConfigHandler(final String pluginName,
    final OSGIKillbillAPI osgiKillbillAPI,
    final OSGIKillbillLogService osgiKillbillLogService) {
        super(pluginName,osgiKillbillAPI,osgiKillbillLogService);
    }

    @Override
    protected MpesaConfigProperties createConfigurable(Properties properties) {
        // TODO Auto-generated method stub
        return new MpesaConfigProperties(properties);
    }

    
}
