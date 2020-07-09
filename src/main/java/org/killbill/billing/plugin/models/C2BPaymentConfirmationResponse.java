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

 /**
  *  // Validation Response
  {
    "TransactionType":"",
    "TransID":"LGR219G3EY",
    "TransTime":"20170727104247",
    "TransAmount":"10.00",
    "BusinessShortCode":"600134",
    "BillRefNumber":"xyz",
    "InvoiceNumber":"",
    "OrgAccountBalance":"",
    "ThirdPartyTransID":"",
    "MSISDN":"254708374149",
    "FirstName":"John",
    "MiddleName":"Doe",
    "LastName":""
  }
  */

package org.killbill.billing.plugin.models;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonAutoDetect(getterVisibility= Visibility.DEFAULT,setterVisibility= Visibility.DEFAULT,fieldVisibility= Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_EMPTY)
public class C2BPaymentConfirmationResponse {
    @JsonProperty("TransactionType")
    public String TransactionType;

    @JsonProperty("TransID")
    public String TransID;

    @JsonProperty("TransTime")
    public Integer TransTime;

    @JsonProperty("TransAmount")
    public Float TransAmount;

    @JsonProperty("BusinessShortCode")
    public int BusinessShortCode;

    @JsonProperty("BillRefNumber")
    public String BillRefNumber;

    @JsonProperty("InvoiceNumber")
    public String InvoiceNumber;

    @JsonProperty("OrgAccountBalance")
    public Float OrgAccountBalance;

    @JsonProperty("ThirdPartyTransID")
    public  String ThirdPartyTransID;

    @JsonProperty("MSISDN")
    public Integer MSISDN;

    @JsonProperty("FirstName")
    public String FirstName;

    @JsonProperty("MiddleName")
    public String MiddleName;

    @JsonProperty("LastName")
    public String LastName;
}
