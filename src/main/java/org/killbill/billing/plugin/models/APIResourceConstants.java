/*
 * Copyright 2014-2020 Groupon, Inc
 * Copyright 2014-2020 The Billing Project, LLC
 *
 * The Billing Project licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.killbill.billing.plugin.models;

public class APIResourceConstants {
    public static final String SANDBOX_URL = "https://sandbox.safaricom.co.ke";
    public static final String LIVE_URL = "https://sandbox.safaricom.co.ke";
    public static final String REVERSAL_TRANSACTIONS = "/mpesa/reversal/v1/request";
    public static final String TRANSACTIONS_STATUS = "/mpesa/transactionstatus/v1/query";
    public static final String SIMULATE_C2B_TRANSACTION = "/mpesa/c2b/v1/simulate";
    public static final String ONLINE_STATUS_QUERY = "/mpesa/stkpushquery/v1/query";
    public static final String B2C_PAYMENTREQUEST = "/mpesa/b2c/v1/paymentrequest";
    public static final String INITIATE_ONLINE_PAYMENT = "/mpesa/stkpush/v1/processrequest";
    public static final String ACCOUNT_BALANCE = "/mpesa/accountbalance/v1/query";
    public static final String SECURE_TOKEN_URL = "/oauth/v1/generate?grant_type=client_credentials";
    public static final String REGISTER_URL = "/mpesa/c2b/v1/registerurl";
    public static final int    SANDBOX_SHORTCODE = 600425;
    public static final int    LIVE_SHORTCODE = 234480; 

    public class SecurityConstants{
        public static final String MPESA_CERTIFICATE="mpesa.cer";
        public static final String APIKEY="YPCP18GaH9gCZDTXKDpWYimoEwstTAqp";
        public static final String APISECRET="TyXcFZ3H4UozAuV2";
        public static final String TOKEN="Bearer ";
        public static final String TIMESTAMP="timestamp";
        public static final String AUTHORIZE="Authorization";
        public static final String PASSKEY="bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919";
        public static final String INITIATOR_NAME = "testapi";
        public static final String SECURITY_CREDENTIAL = "Apitest425#";
    }
}
