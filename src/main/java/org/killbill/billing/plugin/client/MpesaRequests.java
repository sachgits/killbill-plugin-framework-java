package org.killbill.billing.plugin.client;

import org.killbill.billing.plugin.models.*;

public class MpesaRequests {

    TransactionStatusRequest tStatusRequest = new TransactionStatusRequest("19195-3838049-2", APIResourceConstants.TEST_MSISDN,
    1,"https://192.168.100.2/kb/mpkillbill/callmeback","https://192.168.100.2/kb/mpkillbill/qtimeout",
    "keeping up with this payment", "100");

    RegisterURLRequest rUrlRequest = new RegisterURLRequest(600425, 
    "https://192.168.100.2/kb/mpkillbill/callmeback",                            
    "https://192.168.100.2/kb/mpkillbill/validateme");
  final SimulateTransactionRequest sTransactionRequest = new SimulateTransactionRequest(APIResourceConstants.SANDBOX_SHORTCODE,
    40.00,APIResourceConstants.TEST_MSISDN,
     "mpkb_transaction101");
  
}