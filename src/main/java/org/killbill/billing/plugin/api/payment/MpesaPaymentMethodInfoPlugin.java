package org.killbill.billing.plugin.api.payment;

import java.util.UUID;

import org.killbill.billing.plugin.dao.MpesaDao;
import org.killbill.billing.plugin.dao.gen.tables.records.MpesaPaymentMethodsRecord;

public class MpesaPaymentMethodInfoPlugin extends PluginPaymentMethodInfoPlugin {

    public MpesaPaymentMethodInfoPlugin(final MpesaPaymentMethodsRecord record) {
        super(UUID.fromString(record.getKbAccountId()),
              UUID.fromString(record.getKbPaymentMethodId()),
              record.getIsDefault() == MpesaDao.TRUE,
              record.getPhoneNumber().toString());
    }
    
}