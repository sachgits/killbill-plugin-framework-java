package org.killbill.billing.plugin.api.payment;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.killbill.billing.catalog.api.Currency;
import org.killbill.billing.payment.api.PluginProperty;
import org.killbill.billing.payment.api.TransactionType;
import org.killbill.billing.payment.plugin.api.PaymentPluginStatus;
import org.killbill.billing.plugin.dao.gen.tables.records.MpesaResponsesRecord;

public class MpesaPaymentTransactionInfo extends PluginPaymentTransactionInfoPlugin {

    public MpesaPaymentTransactionInfo(UUID kbPaymentId, UUID kbTransactionPaymentPaymentId,
            TransactionType transactionType, BigDecimal amount, Currency currency, PaymentPluginStatus pluginStatus,
            String gatewayError, String gatewayErrorCode, String firstPaymentReferenceId,
            String secondPaymentReferenceId, DateTime createdDate, DateTime effectiveDate,
            List<PluginProperty> properties) {
        super(kbPaymentId, kbTransactionPaymentPaymentId, transactionType, amount, currency, pluginStatus, gatewayError,
                gatewayErrorCode, firstPaymentReferenceId, secondPaymentReferenceId, createdDate, effectiveDate,
                properties);
        // TODO Auto-generated constructor stub
    }

    public MpesaPaymentTransactionInfo(final MpesaResponsesRecord record) {
        super(UUID.fromString(record.getKbPaymentId()),
              UUID.fromString(record.getKbPaymentTransactionId()),
              TransactionType.valueOf(record.getKbTransactionType()),
              record.getTransAmount(),
              Currency.valueOf("KES"),
              getPaymentPluginStatus("To Be Implemented", "To Be Implemented"),
              null,
              null,
              record.getTransId(),
              record.getTransactionType(),
              new DateTime(DateTimeZone.UTC),
              new DateTime(DateTimeZone.UTC),
              ImmutableList.<PluginProperty>of());
    }

    private static PaymentPluginStatus getPaymentPluginStatus(final String transactionStatus, final String validationStatus) {
        // TODO
        return "success".equals(validationStatus) ? PaymentPluginStatus.PROCESSED : PaymentPluginStatus.ERROR;
    }
    
}