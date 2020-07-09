package org.killbill.billing.plugin.dao;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.sql.DataSource;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.jooq.impl.DSL;
import org.jooq.types.UInteger;
import org.killbill.billing.catalog.api.Currency;
import org.killbill.billing.payment.api.TransactionType;
import org.killbill.billing.plugin.api.payment.PluginPaymentPluginApi;
import org.killbill.billing.plugin.api.payment.MpesaPluginPaymentApi;
import org.killbill.billing.plugin.dao.payment.PluginPaymentDao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.typesafe.config.ConfigException.Null;

import org.killbill.billing.plugin.models.C2BPaymentConfirmationResponse;

import org.killbill.billing.plugin.dao.gen.tables.MpesaPaymentMethods;
import org.killbill.billing.plugin.dao.gen.tables.records.MpesaPaymentMethodsRecord;

import org.killbill.billing.plugin.dao.gen.tables.MpesaResponses;
import org.killbill.billing.plugin.dao.gen.tables.records.MpesaResponsesRecord;


import static org.killbill.billing.plugin.dao.gen.tables.MpesaPaymentMethods.MPESA_PAYMENT_METHODS;
import static org.killbill.billing.plugin.dao.gen.tables.MpesaResponses.MPESA_RESPONSES;;

public class MpesaDao extends PluginPaymentDao<MpesaResponsesRecord, MpesaResponses,
    MpesaPaymentMethodsRecord, MpesaPaymentMethods>{
        public MpesaDao(final DataSource dataSource)throws SQLException{
            super(MpesaResponses.MPESA_RESPONSES,MpesaPaymentMethods.MPESA_PAYMENT_METHODS, dataSource);
        }
    


 public static Map fromAdditionalData(final String additionalData) {
    if (additionalData == null) {
        return ImmutableMap.of();
    }

    try {
        return objectMapper.readValue(additionalData, Map.class);
    } catch (final IOException e) {
        throw new RuntimeException(e);
    }
 }

 public static String toAdditionalData(final Object additionalData) {
    if (additionalData == null) {
        return null;
    }

    try {
        return objectMapper.writeValueAsString(additionalData);
    } catch (final IOException e) {
        throw new RuntimeException(e);
    }
 }

 // Payment method
 @Override
 public void addPaymentMethod(final UUID kbAccountId, final UUID kbPaymentMethodId,
    final boolean isDefault, final Map<String,String> properties,
    final DateTime utcNow, final UUID kbTenantId)throws SQLException{
     
     /* Clone our properties, what we have been given might be unmodifiable */
     final Map<String, String> clonedProperties = new HashMap<String, String>(properties);
     final String phoneNumber = clonedProperties.remove(MpesaPluginPaymentApi.PROPERTY_PHONENUMBER);
     final String firstname = clonedProperties.remove(MpesaPluginPaymentApi.PROPERTY_FIRST_NAME);
     final String city = clonedProperties.remove(MpesaPluginPaymentApi.PROPERTY_CITY);
     final String zip = clonedProperties.remove(MpesaPluginPaymentApi.PROPERTY_ZIP);
     final String country = clonedProperties.remove(MpesaPluginPaymentApi.PROPERTY_COUNTRY);
     final String lastname = clonedProperties.remove(MpesaPluginPaymentApi.PROPERTY_LAST_NAME);
        
     /* Store computed data */
     execute(dataSource.getConnection(),
     new WithConnectionCallback<Void>() {
         @Override
         public Void withConnection(final Connection conn) throws SQLException {
             DSL.using(conn, dialect, settings)
             .insertInto(paymentMethodsTable,
             MPESA_PAYMENT_METHODS.KB_ACCOUNT_ID,
             MPESA_PAYMENT_METHODS.KB_PAYMENT_METHOD_ID,
             MPESA_PAYMENT_METHODS.PHONE_NUMBER,
             MPESA_PAYMENT_METHODS.FIRST_NAME,
             MPESA_PAYMENT_METHODS.LAST_NAME,
             MPESA_PAYMENT_METHODS.CITY,
             MPESA_PAYMENT_METHODS.ZIP,
             MPESA_PAYMENT_METHODS.COUNTRY,
             MPESA_PAYMENT_METHODS.IS_DEFAULT,
             MPESA_PAYMENT_METHODS.IS_DELETED,
             MPESA_PAYMENT_METHODS.ADDITIONAL_DATA,
             MPESA_PAYMENT_METHODS.CREATED_DATE,
             MPESA_PAYMENT_METHODS.UPDATED_DATE,
             MPESA_PAYMENT_METHODS.KB_TENANT_ID)
             .values(kbAccountId.toString(),
             kbPaymentMethodId.toString(),
             UInteger.valueOf(phoneNumber), //VERY VERY DANGEROUS!
             firstname,
             lastname,
             city,
             zip,
             country,
             fromBoolean(isDefault),
             FALSE,
             asString(clonedProperties),
             toTimestamp(utcNow).toLocalDateTime(),
             toTimestamp(utcNow).toLocalDateTime(),
             kbTenantId.toString())
             .execute();
             return null;
            }
        });
    }

    // Responses

    public MpesaResponsesRecord addResponse(final UUID kbAccountId,
                                              final UUID kbPaymentId,
                                              final UUID kbPaymentTransactionId,
                                              final TransactionType transactionType,
                                              final BigDecimal amount,
                                              final Currency currency,
                                              final C2BPaymentConfirmationResponse response,
                                              final DateTime utcNow,
                                              final UUID kbTenantId) throws SQLException {
        final String additionalData = toAdditionalData(response);

        return execute(dataSource.getConnection(),
                       new WithConnectionCallback<MpesaResponsesRecord>() {
                           @Override
                           public MpesaResponsesRecord withConnection(final Connection conn) throws SQLException {
                               DSL.using(conn, dialect, settings)
                                  .insertInto(MPESA_RESPONSES,
                                              MPESA_RESPONSES.KB_ACCOUNT_ID,
                                              MPESA_RESPONSES.KB_PAYMENT_ID,
                                              MPESA_RESPONSES.KB_PAYMENT_TRANSACTION_ID,
                                              MPESA_RESPONSES.KB_TRANSACTION_TYPE,
                                              MPESA_RESPONSES.TRANSACTION_TYPE,
                                              MPESA_RESPONSES.TRANS_ID,
                                              MPESA_RESPONSES.TRANS_TIME,
                                              MPESA_RESPONSES.TRANS_AMOUNT,
                                              MPESA_RESPONSES.BUSINESS_SHORTCODE,
                                              MPESA_RESPONSES.BILL_REF_NUMBER,
                                              MPESA_RESPONSES.INVOICE_NUMBER,
                                              MPESA_RESPONSES.ORG_ACCOUNT_BALANCE,
                                              MPESA_RESPONSES.THIRD_PARTY_TRANS_ID,
                                              MPESA_RESPONSES.PHONE_NUMBER,
                                              MPESA_RESPONSES.FIRST_NAME,
                                              MPESA_RESPONSES.MIDDLE_NAME,
                                              MPESA_RESPONSES.LAST_NAME,
                                              MPESA_RESPONSES.KB_TENANT_ID)
                                  .values(kbAccountId.toString(),
                                          kbPaymentId.toString(),
                                          kbPaymentTransactionId.toString(),
                                          transactionType.toString(),
                                          response.TransactionType,
                                          response.TransID,
                                          response.TransTime,
                                          BigDecimal.valueOf(response.TransAmount),
                                          response.BusinessShortCode,
                                          response.BillRefNumber,
                                          response.InvoiceNumber,
                                          BigDecimal.valueOf(response.OrgAccountBalance),
                                          response.ThirdPartyTransID,
                                          response.MSISDN,
                                          response.FirstName,
                                          response.MiddleName,
                                          response.LastName,
                                          kbTenantId.toString())
                                  .execute();

                               return DSL.using(conn, dialect, settings)
                                         .selectFrom(MPESA_RESPONSES)
                                         .where(MPESA_RESPONSES.KB_PAYMENT_TRANSACTION_ID.equal(kbPaymentTransactionId.toString()))
                                         .and(MPESA_RESPONSES.KB_TENANT_ID.equal(kbTenantId.toString()))
                                         .orderBy(MPESA_RESPONSES.RECORD_ID.desc())
                                         .limit(1)
                                         .fetchOne();
                           }
                       });
    }

        // this might be useful where from notification and we want to save response hence first get the ACCOUNTID
        public MpesaPaymentMethodsRecord getMpesaPaymentMethodByPhoneNumber(final UInteger PhoneNumber, final UUID kbTenantId) throws SQLException {
            return execute(dataSource.getConnection(),
                           new WithConnectionCallback<MpesaPaymentMethodsRecord>() {
                               @Override
                               public MpesaPaymentMethodsRecord withConnection(final Connection conn) throws SQLException {
                                   return DSL.using(conn, dialect, settings)
                                             .selectFrom(MPESA_PAYMENT_METHODS)
                                             .where(MPESA_PAYMENT_METHODS.PHONE_NUMBER.equal(PhoneNumber))
                                             .and(MPESA_PAYMENT_METHODS.KB_TENANT_ID.equal(kbTenantId.toString()))
                                             .orderBy(MPESA_PAYMENT_METHODS.RECORD_ID.desc())
                                             .limit(1)
                                             .fetchOne();
                               }
                           });
        }

        //function to return MpesaPaymentRecord that does not have firstname and lastname registered
        public MpesaPaymentMethodsRecord getPaymentMethodsByPhoneAndAccountNames(final UInteger phoneNumber, final String firstname, 
       final String lastName,final UUID kbTenantId)throws SQLException{
           return execute(dataSource.getConnection(),
                           new WithConnectionCallback<MpesaPaymentMethodsRecord>() {
                               @Override
                               public MpesaPaymentMethodsRecord withConnection(final Connection conn) throws SQLException {
                                   return DSL.using(conn, dialect, settings)
                                             .selectFrom(MPESA_PAYMENT_METHODS)
                                             .where(MPESA_PAYMENT_METHODS.PHONE_NUMBER.equal(phoneNumber))
                                             .and(MPESA_PAYMENT_METHODS.FIRST_NAME.equal(firstname))
                                             .and(MPESA_PAYMENT_METHODS.LAST_NAME.equal(lastName))
                                             .and(MPESA_PAYMENT_METHODS.KB_TENANT_ID.equal(kbTenantId.toString()))
                                             .orderBy(MPESA_PAYMENT_METHODS.RECORD_ID.desc())
                                             .limit(1)
                                             .fetchOne();
                               }
                           });
       }


}