package org.killbill.billing.plugin.client;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Nullable;

import org.joda.time.Period;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

public class MpesaConfigProperties {
    
        public static final String PROPERTY_PREFIX = "org.killbill.billing.plugin.kbsafcom.";
        private static final String ENTRY_DELIMITER = "|";
        private static final String KEY_VALUE_DELIMITER = "#";
        private static final String DEFAULT_CONNECTION_TIMEOUT = "30000";
        private static final String DEFAULT_READ_TIMEOUT = "60000";

        private final Integer merchantAccounts;
        private final String apiKey;
        private final String apiSecret;
        private final String defaultPaymentUrl;
        private final String proxyServer;
        private final Integer proxyPort;
        private final String proxyType;
        private final Boolean trustAllCertificates;
        private final String paymentConnectionTimeout;
        private final String paymentReadTimeout;
        private final String mpesaCertificate;
        private final String passkey;
        private final String initiatorName;
        private final String securityCredential;
        private final String confirmationURL;
        private final String validationURL;

        public static final String REVERSAL_TRANSACTIONS = "/mpesa/reversal/v1/request";
        public static final String TRANSACTIONS_STATUS = "/mpesa/transactionstatus/v1/query";
        public static final String SIMULATE_C2B_TRANSACTION = "/mpesa/c2b/v1/simulate";
        public static final String ONLINE_STATUS_QUERY = "/mpesa/stkpushquery/v1/query";
        public static final String B2C_PAYMENTREQUEST = "/mpesa/b2c/v1/paymentrequest";
        public static final String INITIATE_ONLINE_PAYMENT = "/mpesa/stkpush/v1/processrequest";
        public static final String ACCOUNT_BALANCE = "/mpesa/accountbalance/v1/query";
        public static final String SECURE_TOKEN_URL = "/oauth/v1/generate?grant_type=client_credentials";
        public static final String REGISTER_URL = "/mpesa/c2b/v1/registerurl";
        public static final int    SANDBOX_SHORTCODE = 234480; //DELETE NOT APPLICABLE
        public static final int    SHORTCODE = 600425;         //DELETE NOT APPLICABLE

        public static final String MPESA_CERTIFICATE="mpesa.cer";
        public static final String TOKEN="Bearer ";
        public static final String TIMESTAMP="timestamp";
        public static final String AUTHORIZE="Authorization";
        
        public MpesaConfigProperties(final Properties properties){
            this.apiKey = properties.getProperty(PROPERTY_PREFIX+ "apiKey");
            this.apiSecret = properties.getProperty(PROPERTY_PREFIX + "apiSecret");
        this.proxyServer = properties.getProperty(PROPERTY_PREFIX + "proxyServer");
        this.proxyPort = Integer.valueOf(properties.getProperty(PROPERTY_PREFIX + "proxyPort"));
        this.proxyType = properties.getProperty(PROPERTY_PREFIX + "proxyType");
        this.trustAllCertificates = Boolean.valueOf(properties.getProperty(PROPERTY_PREFIX + "trustAllCertificates", "false"));
        this.defaultPaymentUrl = properties.getProperty(PROPERTY_PREFIX + "paymentUrl");
        this.paymentConnectionTimeout = properties.getProperty(PROPERTY_PREFIX + "paymentConnectionTimeout", DEFAULT_CONNECTION_TIMEOUT);
        this.paymentReadTimeout = properties.getProperty(PROPERTY_PREFIX + "paymentReadTimeout", DEFAULT_READ_TIMEOUT);
        //TODO: we should use parseInt instead of valueOf improvment
        this.merchantAccounts = Integer.valueOf(properties.getProperty(PROPERTY_PREFIX + "shortcode")); 
        this.passkey = properties.getProperty(PROPERTY_PREFIX + "passkey");
        this.mpesaCertificate = properties.getProperty(PROPERTY_PREFIX + "mpesaCertificate");
        this.initiatorName = properties.getProperty(PROPERTY_PREFIX + "initiatorName");
        this.securityCredential = properties.getProperty(PROPERTY_PREFIX + "securityCredential");
        this.confirmationURL = properties.getProperty(PROPERTY_PREFIX + "confimationUrl");
        this.validationURL = properties.getProperty(PROPERTY_PREFIX + "validationUrl");
        }



        public String getApiKey() {
            return apiKey;
        }
        public String getApiSecret() {
            return apiSecret;
        }
        public String getProxyServer() {
            return proxyServer;
        }
        public Integer getProxyPort() {
            return proxyPort;
        }
        public String getProxyType() {
            return proxyType;
        }
        public Boolean getTrustAllCertificates() {
            return trustAllCertificates;
        }
        public String getDefaultPaymentUrl() {
            return defaultPaymentUrl;
        }
        public String getPaymentConnectionTimeout() {
            return paymentConnectionTimeout;
        }
        public String getPaymentReadTimeout() {
            return paymentReadTimeout;
        }
        public Integer getMerchantAccounts() {
            return merchantAccounts;
        }
        public String getPasskey() {
            return passkey;
        }
        public String getMpesaCertificate() {
            return mpesaCertificate;
        }
        public String getInitiatorName() {
            return initiatorName;
        }
        public String getSecurityCredential() {
            return securityCredential;
        }
        public String getConfirmationURL() {
            return confirmationURL;
        }
        public String getValidationURL() {
            return validationURL;
        }

}