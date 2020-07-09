package org.killbill.billing.plugin.api.payment;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.UUID;

import javax.annotation.Nullable;
import javax.xml.bind.JAXBException;

import org.bouncycastle.jcajce.provider.asymmetric.dsa.DSASigner.stdDSA;
import org.joda.time.DateTime;
import org.killbill.billing.account.api.Account;
import org.killbill.billing.catalog.api.Currency;
import org.killbill.billing.osgi.libs.killbill.OSGIConfigPropertiesService;
import org.killbill.billing.osgi.libs.killbill.OSGIKillbillAPI;
import org.killbill.billing.osgi.libs.killbill.OSGIKillbillLogService;
import org.killbill.billing.payment.api.PaymentMethodPlugin;
import org.killbill.billing.payment.api.PluginProperty;
import org.killbill.billing.payment.api.TransactionType;
import org.killbill.billing.payment.plugin.api.GatewayNotification;
import org.killbill.billing.payment.plugin.api.HostedPaymentPageFormDescriptor;
import org.killbill.billing.payment.plugin.api.PaymentMethodInfoPlugin;
import org.killbill.billing.payment.plugin.api.PaymentPluginApiException;
import org.killbill.billing.payment.plugin.api.PaymentTransactionInfoPlugin;
import org.killbill.billing.plugin.api.PluginProperties;
import org.killbill.billing.plugin.api.payment.*;
import org.killbill.billing.plugin.client.MpesaClientWrapper;
import org.killbill.billing.plugin.core.MpesaConfigurationHandler;

import org.killbill.billing.plugin.dao.MpesaDao;
import org.killbill.billing.plugin.dao.gen.tables.records.MpesaResponsesRecord;
import org.killbill.billing.plugin.dao.gen.tables.MpesaPaymentMethods;
import org.killbill.billing.plugin.dao.gen.tables.MpesaResponses;
import org.killbill.billing.plugin.dao.gen.tables.records.MpesaPaymentMethodsRecord;
import org.killbill.billing.plugin.util.KillBillMoney;
import org.killbill.billing.util.callcontext.CallContext;
import org.killbill.billing.util.callcontext.TenantContext;
import org.killbill.clock.Clock;
import org.osgi.service.log.LogService;

import com.google.common.base.MoreObjects;

public class MpesaPluginPaymentApi extends PluginPaymentPluginApi<MpesaResponsesRecord, MpesaResponses, MpesaPaymentMethodsRecord, MpesaPaymentMethods>{
   
    private final MpesaConfigurationHandler mpesaConfigurationHandler;
    private final MpesaDao dao;

    public MpesaPluginPaymentApi(final MpesaConfigurationHandler mpesaConfigurationHandler, 
                                final OSGIKillbillAPI killbillAPI,
                                final OSGIConfigPropertiesService configProperties,
                                final OSGIKillbillLogService logService, 
                                final Clock clock,
			                    final MpesaDao dao)throws JAXBException  {
                super(killbillAPI, configProperties, logService, clock, dao);
                this.mpesaConfigurationHandler = mpesaConfigurationHandler;
                this.dao = dao;
	}
	public static final String PROPERTY_EMAIL = "email";

    // MPESA
    public static final String PROPERTY_PHONENUMBER = "phone_number";
    public static final String PROPERTY_CITY = "city";
    public static final String PROPERTY_ZIP = "zip";
    public static final String PROPERTY_COUNTRY = "country";
    public static final String PROPERTY_RESULT_DESC = "result_desc";
    public static final String PROPERTY_MPESA_RECEIPT_NUMBER = "mpesa_receipt_number";
    public static final String PROPERTY_BALANCE = "balance";
    public static final String PROPERTY_FIRST_NAME = "first_name";
    public static final String PROPERTY_LAST_NAME = "last_name";

    @Override
    public PaymentTransactionInfoPlugin authorizePayment(UUID arg0, UUID arg1, UUID arg2, UUID arg3, BigDecimal arg4,
            Currency arg5, Iterable<PluginProperty> arg6, CallContext arg7) throws PaymentPluginApiException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HostedPaymentPageFormDescriptor buildFormDescriptor(UUID arg0, Iterable<PluginProperty> arg1,
            Iterable<PluginProperty> arg2, CallContext arg3) throws PaymentPluginApiException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("To be implemented");
    }

    @Override
    public PaymentTransactionInfoPlugin capturePayment(UUID arg0, UUID arg1, UUID arg2, UUID arg3, BigDecimal arg4,
            Currency arg5, Iterable<PluginProperty> arg6, CallContext arg7) throws PaymentPluginApiException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PaymentTransactionInfoPlugin creditPayment(UUID arg0, UUID arg1, UUID arg2, UUID arg3, BigDecimal arg4,
            Currency arg5, Iterable<PluginProperty> arg6, CallContext arg7) throws PaymentPluginApiException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GatewayNotification processNotification(String arg0, Iterable<PluginProperty> arg1, CallContext arg2)
            throws PaymentPluginApiException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PaymentTransactionInfoPlugin purchasePayment(UUID arg0, UUID arg1, UUID arg2, UUID arg3, BigDecimal arg4,
            Currency arg5, Iterable<PluginProperty> arg6, CallContext arg7) throws PaymentPluginApiException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PaymentTransactionInfoPlugin refundPayment(UUID arg0, UUID arg1, UUID arg2, UUID arg3, BigDecimal arg4,
            Currency arg5, Iterable<PluginProperty> arg6, CallContext arg7) throws PaymentPluginApiException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PaymentTransactionInfoPlugin voidPayment(UUID arg0, UUID arg1, UUID arg2, UUID arg3,
            Iterable<PluginProperty> arg4, CallContext arg5) throws PaymentPluginApiException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected PaymentTransactionInfoPlugin buildPaymentTransactionInfoPlugin(MpesaResponsesRecord record) {
        // TODO Auto-generated method stub
        return new MpesaPaymentTransactionInfo(record);
    }

    @Override
    protected PaymentMethodPlugin buildPaymentMethodPlugin(MpesaPaymentMethodsRecord record) {
        // TODO Auto-generated method stub
        return new MpesaPaymentMethodPlugin(record);
    }

    @Override
    protected PaymentMethodInfoPlugin buildPaymentMethodInfoPlugin(MpesaPaymentMethodsRecord record) {
        // TODO Auto-generated method stub
        return new MpesaPaymentMethodInfoPlugin(record);
    }

    @Override
    protected String getPaymentMethodId(MpesaPaymentMethodsRecord input) {
        // TODO Auto-generated method stub
        return input.getKbPaymentMethodId();
    }


   

}