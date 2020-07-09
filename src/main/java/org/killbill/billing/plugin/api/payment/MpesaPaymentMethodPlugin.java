package org.killbill.billing.plugin.api.payment;

import java.util.List;
import java.util.UUID;

import org.killbill.billing.payment.api.PluginProperty;
import org.killbill.billing.plugin.api.MpesaModelPluginBase;
import org.killbill.billing.plugin.dao.MpesaDao;
import org.killbill.billing.plugin.dao.gen.tables.records.MpesaPaymentMethodsRecord;

public class MpesaPaymentMethodPlugin extends PluginPaymentMethodPlugin {

    public MpesaPaymentMethodPlugin(UUID kbPaymentMethodId, String externalPaymentMethodId,
            boolean isDefaultPaymentMethod, List<PluginProperty> properties) {
        super(kbPaymentMethodId, externalPaymentMethodId, isDefaultPaymentMethod, properties);
        // TODO Auto-generated constructor stub
    }

    public MpesaPaymentMethodPlugin(final MpesaPaymentMethodsRecord record) {
        super(record.getKbPaymentMethodId() == null ? null : UUID.fromString(record.getKbPaymentMethodId()),
              record.getPhoneNumber().toString(),
              (record.getIsDefault() != null) && MpesaDao.TRUE == record.getIsDefault(),
              MpesaModelPluginBase.buildPluginProperties(record.getAdditionalData()));
    }
    
}