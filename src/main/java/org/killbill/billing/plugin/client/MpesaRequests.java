package org.killbill.billing.plugin.client;

import org.killbill.billing.plugin.models.*;

public class MpesaRequests {

  /*  TransactionStatusRequest tStatusRequest = new TransactionStatusRequest("19195-3838049-2", APIResourceConstants.TEST_MSISDN,
    1,"https://192.168.100.2/kb/mpkillbill/callmeback","https://192.168.100.2/kb/mpkillbill/qtimeout",
    "keeping up with this payment", "100");

    RegisterURLRequest rUrlRequest = new RegisterURLRequest(600425, 
    "https://192.168.100.2/kb/mpkillbill/callmeback",                            
    "https://192.168.100.2/kb/mpkillbill/validateme");
  final SimulateTransactionRequest sTransactionRequest = new SimulateTransactionRequest(APIResourceConstants.SANDBOX_SHORTCODE,
    40.00,APIResourceConstants.TEST_MSISDN,
     "mpkb_transaction101");

        public static void main(String[] args) {
        MpesaClientWrapper mpesaGateway = null;
        TransactionStatusRequest tStatusRequest = new TransactionStatusRequest("testapi",
              "19195-3838049-2", "254725085687", IdentifierTypes.SHORTCODE.ordinal(), 
              "https://192.168.100.2/kb/mpkillbill/callmeback" 
              ,"https://192.168.100.2/kb/mpkillbill/qtimeout" , "keeping up with this payment", "100");
        /** SimulateTransactionRequest sTransactionRequest = new SimulateTransactionRequest(600425,40.00,"254725085687", "mpkb_transaction101");
         RegisterURLRequest rUrlRequest = new RegisterURLRequest(600425, "https://192.168.100.2/kb/mpkillbill/callmeback", 
                                                                "https://192.168.100.2/kb/mpkillbill/validateme");
        MpesaCoreBase mCoreBase = new MpesaCoreBase(600425);
        System.out.println(String.format("shortCode = %d, passkey=%s,\\ntimestamp=%d\\npassword=%s",
         mCoreBase.BusinessShortCode, mCoreBase.Passkey,mCoreBase.timestamp,mCoreBase.Password));
         
        try {
          mpesaGateway = new MpesaClientWrapper(APIResourceConstants.SANDBOX_URL,
                  APIResourceConstants.SecurityConstants.APIKEY, APIResourceConstants.SecurityConstants.APISECRET,
                  null, null, false);
                  mpesaGateway.triggerSimulateTransaction(tStatusRequest);
                  logger.warn(String.format("api token from saf: %s", mpesaGateway.token));
      } catch (GeneralSecurityException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
      }
     
  }
    **/
  
}