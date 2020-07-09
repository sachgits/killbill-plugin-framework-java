/*! SET storage_engine=INNODB */;
/*! use killbill */;
/*
/**
  *payment method docs
 */
drop table if exists mpesa_payment_methods;
create table mpesa_payment_methods (
  record_id int(11) unsigned not null auto_increment
, kb_account_id char(36) not null
, kb_payment_method_id char(36) not null /**TODO phone number should be unique*/
, phone_number int(12) unsigned not null /** TODO not sure if to use smallint or char(12) since jooq smallint defaults to Short*/
, first_name varchar(255) default null
, last_name varchar(255) default null
, city varchar(255) default 'Nairobi'
, zip char(10) default '254'
, country varchar(255) default 'Kenya'
, is_default boolean not null default false
, is_deleted boolean not null default false
, additional_data longtext default null
, created_date datetime not null /* TODO: defaults to date.now() or something */
, updated_date datetime not null
, kb_tenant_id char(36) not null
, primary key(record_id)
, unique(phone_number)
) /*! CHARACTER SET utf8 COLLATE utf8_bin */;
create unique index mpesa_payment_methods_kb_payment_id on mpesa_payment_methods(kb_payment_method_id);

/**
  table to be used for killbill notifications from gateway
 */
drop table if exists mpesa_responses;
create table mpesa_responses(
record_id int(11) unsigned not null auto_increment
, kb_account_id char(36) not null
, kb_payment_id char(36) /* TODO: translates to mpesa BillRefNumber */
, kb_payment_transaction_id char(36)  /* TODO: translates to mpesa BillReNumber */
, kb_transaction_type char(36)
, transaction_type char(36) not null
, trans_id char(36)
, trans_time integer not null
, trans_amount numeric(15,9) not null
, business_shortcode int not null
, bill_ref_number varchar(255)
, invoice_number varchar(125)
, org_account_balance numeric(15,9)
, third_party_trans_id char(36)
, phone_number int(12) not null
, first_name varchar(125)
, middle_name varchar(125)
, last_name varchar(125)
, kb_tenant_id char(36) not null
, primary key (record_id)
) /*! CHARACTER SET utf8 COLLATE utf8_bin */;
create unique index mpesa_responses_kb_transaction_id on mpesa_responses(kb_payment_transaction_id);
