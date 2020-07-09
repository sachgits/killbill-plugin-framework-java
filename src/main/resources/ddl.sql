/*! SET storage_engine=INNODB */;
/*
 *
 * //Confirmation Respose
  {
    "TransactionType":"",
    "TransID":"LGR219G3EY",
    "TransTime":"20170727104247",
    "TransAmount":"10.00",
    "BusinessShortCode":"600134",
    "BillRefNumber":"xyz",
    "InvoiceNumber":"",
    "OrgAccountBalance":"49197.00",
    "ThirdPartyTransID":"1234567890",
    "MSISDN":"254708374149",
    "FirstName":"John",
    "MiddleName":"",
    "LastName":""
  }
*/
drop table if exists mpesa_responses;
create table mpesa_responses (
  record_id int(11) unsigned not null auto_increment
, kb_account_id char(36) not null
, kb_payment_id char(36) not null
, kb_payment_transaction_id char(36) not null
, kb_transaction_type varchar(32) not null
, merchant_request_id varchar(32) not null
, checkout_request_id varchar(64) not null
, phone_number integer not null
, amount numeric(15,9)
, currency char(3)
, result_code smallint(4)
, result_desc varchar(255)
, gateway_resp_code varchar(64)
, gateway_message text DEFAULT NULL
, mpesa_receipt_number varchar(64)
, balance numeric(15,9)
, additional_data longtext default null
, transaction_date integer not null
, kb_tenant_id char(36) not null
, primary key(record_id)
) /*! CHARACTER SET utf8 COLLATE utf8_bin */;
create index mpesa_responses_kb_payment_id on mpesa_responses(kb_payment_id);
create index mpesa_responses_kb_payment_transaction_id on mpesa_responses(kb_payment_transaction_id);

/**
  *payment method docs
 */
drop table if exists mpesa_payment_methods;
create table mpesa_payment_methods (
  record_id int(11) unsigned not null auto_increment
, kb_account_id char(36) not null
, kb_payment_method_id char(36) not null
, phone_number smallint(12) not null
, first_name varchar(255) default null
, last_name varchar(255) default null
, city varchar(255) default 'Nairobi'
, state varchar(255) default 'kariobangi South'
, zip varchar(255) default null
, country varchar(255) default 'Kenya'
, is_default boolean not null default false
, is_deleted boolean not null default false
, additional_data longtext default null
, created_date datetime default null /* TODO: defaults to date.now() or something */
, updated_date datetime null
, kb_tenant_id char(36) not null
, primary key(record_id)
) /*! CHARACTER SET utf8 COLLATE utf8_bin */;
create unique index mpesa_payment_methods_kb_payment_id on mpesa_payment_methods(kb_payment_method_id);
/*
* account balance request docs
**/
drop table if exists mpesa_account_balance_req;
create table mpesa_account_balance_req(
record_id int(11) unsigned not null auto_increment
, initiator char(36) not null
, command_id char(36) not null default 'AccountBalance'
, partyA char(10) not null
, security_credential varchar(255)
, identifier_type  smallint(2) default 4
, remarks varchar(255)
, queue_timeout_url varchar(125) not null
, result_url varchar(125) not null
, primary key (record_id)
) /*! CHARACTER SET utf8 COLLATE utf8_bin */;

/**
  *account balance docs
**/
drop table if exists mpesa_account_balance_res;
create table mpesa_account_balance_res(
  record_id int(11) unsigned not null auto_increment
, result_code smallint(2) not null
, result_type smallint(2) not null
, result_desc char(125) default null
, transaction_id varchar(35) NOT NULL
, conversation_id varchar(125) default NULL
, originator_conversation_id varchar(125) default NULL
, account_balance  float(6) NOT NULL
, bo_completed_time integer NOT NULL
, primary key (record_id)
) /*! CHARACTER SET utf8 COLLATE utf8_bin */;

/**
  table to be used for killbill notifications from gateway
 */
drop table if exists mpesa_confirmation_req;
create table mpesa_confirmation_req(
record_id int(11) unsigned not null auto_increment
, kb_account_id char(36) not null
, kb_payment_id char(36) /* TODO: translates to mpesa BillRefNumber */
, kb_payment_transaction_id char(36)  /* TODO: translates to mpesa BillReNumber */
, transaction_type char(36) not null
, trans_id char(36)
, trans_time integer not null
, trans_amount numeric(15,9) not null
, business_shortcode smallint(6) not null
, bill_ref_number varchar(255)
, invoice_number varchar(125)
, org_account_balance numeric(15,9)
, third_party_trans_id varchar(125)
, phone_number integer not null
, first_name varchar(125)
, middle_name varchar(125)
, last_name varchar(125)
, primary key (record_id)
) /*! CHARACTER SET utf8 COLLATE utf8_bin */;
create unique index mpesa_confirmation_req_kb_transaction_id on mpesa_confirmation_req(kb_payment_transaction_id);

/**
 * documentation comes here
 */
drop table if exists mpesa_simulate_transaction_req;
create table mpesa_simulate_transaction_req(
  record_id int(11) unsigned not null auto_increment
, kb_account_id char(36) not null
, kb_payment_id char(36) not null /* translates to mpesa BillRefNumber */
, kb_payment_transaction_id char(36) not null /* where should we through this during simulate trans req */
, bill_ref_number varchar(255) not null
, phone_number smallint not null /* TODO: not sure where smallint is the correct datatype */
, amount numeric(15,9) not null
, command_id char(36) default 'CustomerPayBillOnline'
, short_code smallint not null
, primary key (record_id)
) /*! CHARACTER SET utf8 COLLATE utf8_bin */;
create unique index mpesa_simulate_transaction_req_kb_account_id on mpesa_simulate_transaction_req(kb_payment_transaction_id);

/**
more documentation comming soon
*/

drop table if exists mpesa_online_accepted_request;
create table mpesa_online_accepted_request(
 record_id int(11) unsigned not null auto_increment
, kb_account_id char(36) not null
, kb_payment_id char(36)
, kb_payment_transaction_id char(36)
, merchant_request_id char(36) not null
, checkout_request_id char(36) not null
, result_code smallint not null
, result_desc varchar(125)
, amount numeric(15,9) not null
, mpesa_receipt_number char(15)
, balance numeric(15,9)
, transaction_date char(15) not null
, phone_number smallint not null
, primary key (record_id)
) /*! CHARACTER SET utf8 COLLATE utf8_bin */;
create unique index mpesa_online_accepted_request_kb_account_id on mpesa_online_accepted_request(kb_payment_transaction_id);

/**
 * docs
 */
drop table if exists mpesa_online_cancelled_request;
create table mpesa_online_cancelled_request(
  record_id int(11) unsigned not null auto_increment
, kb_account_id char(36) not null
, kb_payment_id char(36)  /* TODO: check whethere there is a way to store id data when calling saf */
, kb_payment_transaction_id char(36) /* so that we can store transaction id for easier working */
, merchant_request_id char(36) not null
, checkout_request_id char(36) not null
, result_code smallint not null
, result_desc varchar(125)
, primary key (record_id)
) /*! CHARACTER SET utf8 COLLATE utf8_bin */;
create unique index mpesa_online_cancelled_request_kb_account_id on mpesa_online_cancelled_request(kb_payment_transaction_id);

/**
  *docks here
 */
drop table if exists mpesa_online_payment_req;
create table mpesa_online_payment_req(
  record_id int(11) unsigned not null auto_increment
 , kb_account_id char(36) not null
 , kb_payment_id char(36) not null /*TODO: how do we store this on mpesa */
 , kb_payment_transaction_id char(36) not null /*TODO: store this on mpesa */
 , business_short_code smallint not null
 , password varchar(255) not null
 , time_stamp timestamp not null
 , transaction_type char(36) not null default 'CustomerPayBillOnline'
 , amount numeric(15,9) not null
 , party_a smallint not null
 , party_b smallint not null /*what is this FOR */
 , phone_number smallint not null /* TODO: not sure if smallint is the best datatype for phone */
 , callback_url char(50) not null
 , account_reference varchar(125) not null /*TODO: is this the place to store data on mpesa */
 , transaction_desc char(36)
 , primary key (record_id)
) /*! CHARACTER SET utf8 COLLATE utf8_bin */;
create unique index mpesa_online_payment_req_kb_account_id on mpesa_online_payment_req(kb_payment_transaction_id);

/**
  *
 */
drop table if exists mpesa_online_query_request;
create table mpesa_online_query_request(
 record_id int(11) unsigned not null auto_increment
, kb_account_id char(36) not null
, kb_payment_id char(36) not null        /*TODO: this should have something to do with */
, kb_payment_transaction_id char(36) not null /*TODO: checkout_request_id  */
, business_short_code smallint not null
, password varchar(255) not null
, time_stamp timestamp not null
, checkout_request_id char(36)
, primary key (record_id)
) /*! CHARACTER SET utf8 COLLATE utf8_bin */;
create unique index mpesa_online_query_request_kb_account_id on mpesa_online_query_request(kb_payment_transaction_id);

/**
  *
 */
drop table if exists mpesa_online_query_response;
create table mpesa_online_query_response(
  record_id int(11) unsigned not null auto_increment
, kb_account_id char(36) not null
, kb_payment_id char(36) not null /*TODO: link this with checkoutRequestID */
, kb_payment_transaction_id char(36) not null /*TODO: and this too */
, response_description varchar(255)
, response_code smallint not null
, merchant_request_id char(36) not null
, checkout_request_id char(36) not null
, result_code smallint not null
, result_desc varchar(125)
, primary key (record_id)
) /*! CHARACTER SET utf8 COLLATE utf8_bin */;
create unique index mpesa_online_query_response_kb_account_id on mpesa_online_query_response(kb_payment_transaction_id);

/**
  *
*/
drop table if exists mpesa_register_url;
create table mpesa_register_url(
  record_id int(11) unsigned not null auto_increment
, short_code smallint not null
, response_type char(36)
, confirmation_url varchar(125) not null
, validation_url varchar(125) not null
, primary key (record_id)
) /*! CHARACTER SET utf8 COLLATE utf8_bin */;

/**
  *
*/
drop table if exists mpesa_register_url_resp;
create table mpesa_register_url_resp(
  record_id int(11) unsigned not null auto_increment
, conversation_id char(36) not null
, originator_conversation_id char(36) not null
, response_description varchar(125)
, primary key (record_id)
) /*! CHARACTER SET utf8 COLLATE utf8_bin */;

/**
  *
 */
drop table if exists mpesa_reversal_request;
create table mpesa_reversal_request(
  record_id int(11) unsigned not null auto_increment
, kb_account_id char(36) not null
, kb_payment_id char(36) /* TODO: greatest challenge is to hook-up this to mpesa actual request */
, kb_payment_transaction_id char(36) /* TODO: and back */
, initiator char(36) not null
, security_credential varchar(255) not null
, command_id char(35) default 'TransactionReversal'
, transaction_id char(35) not null
, amount numeric(15,9) not null
, receiver_party integer not null
, reciever_identifier_type smallint not null default 4
, result_url char(125) not null
, queue_timeout_url char(125) not null
, remarks varchar(255)
, occasion varchar(255)
, primary key (record_id)
) /*! CHARACTER SET utf8 COLLATE utf8_bin */;
create unique index mpesa_reversal_request_kb_account_id on mpesa_reversal_request(kb_payment_transaction_id);

/**
  *
*/
drop table if exists mpesa_reversal_response;
create table mpesa_reversal_response(
  record_id int(11) unsigned not null auto_increment
, kb_account_id char(36) not null
, kb_payment_id char(36)
, kb_payment_transaction_id char(36)
, result_desc varchar(255)
, result_type smallint not null
, result_code smallint not null
, originator_conversation_id char(36) not null
, conversation_id char(50) not null
, transaction_id char(36) not null
, queue_timeout_url char(125) not null
, primary key (record_id)
) /*! CHARACTER SET utf8 COLLATE utf8_bin */;
create unique index mpesa_reversal_response_kb_account_id on mpesa_reversal_response(kb_payment_transaction_id);

/**
  *
*/
drop table if exists mpesa_transaction_status_req;
create table mpesa_transaction_status_req(
  record_id int(11) unsigned not null auto_increment
, kb_account_id char(36) not null
, kb_payment_id char(36)
, kb_payment_transaction_id char(36)
, initiator char(36) not null
, security_credential varchar(255)
, command_id char(36) default 'TransactionStatusQuery'
, transaction_id char(36) not null
, party_a smallint not null
, identifier_type smallint not null
, result_url varchar(125) not null
, queue_timeout_url varchar(125) not null
, remarks varchar(255)
, occasion varchar(125)
, primary key (record_id)
) /*! CHARACTER SET utf8 COLLATE utf8_bin */;
create unique index mpesa_transaction_status_req_kb_account_id on mpesa_transaction_status_req(kb_payment_transaction_id);

/**
  *
 */
drop table if exists mpesa_transaction_status_res;
create table mpesa_transaction_status_res(
  record_id int(11) unsigned not null auto_increment
, kb_account_id char(36) not null
, kb_payment_id char(36)  /*TODO: how to hook this up */
, kb_payment_transaction_id char(36) /* TODO: hook me up please */
, result_type smallint not null
, result_code smallint not null
, result_desc varchar(255)
, transaction_id char(36)
, transaction_reason varchar(125)
, reason_type varchar(125)
, transaction_status char(20)
, amount numeric(15,9)
, finalised_time integer
, conversation_id char(36) not null
, receipt_no char(36)
, debit_party_charges numeric(15,9)
, debit_account_type char(36)
, initiated_time int
, originator_conversation_id char(36) not null
, credit_party_name varchar(255) not null
, debit_party_name varchar(125)
, occasion varchar(255)
, primary key (record_id)
) /*! CHARACTER SET utf8 COLLATE utf8_bin */;
create unique index mpesa_transaction_status_res_kb_account_id on mpesa_transaction_status_res(kb_payment_transaction_id);