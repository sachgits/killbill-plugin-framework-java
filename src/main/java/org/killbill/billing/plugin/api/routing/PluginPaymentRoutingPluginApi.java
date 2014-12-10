/*
 * Copyright 2014 Groupon, Inc
 * Copyright 2014 The Billing Project, LLC
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

package org.killbill.billing.plugin.api.routing;

import org.killbill.billing.payment.api.PluginProperty;
import org.killbill.billing.plugin.api.PluginApi;
import org.killbill.billing.routing.plugin.api.OnFailurePaymentRoutingResult;
import org.killbill.billing.routing.plugin.api.OnSuccessPaymentRoutingResult;
import org.killbill.billing.routing.plugin.api.PaymentRoutingApiException;
import org.killbill.billing.routing.plugin.api.PaymentRoutingContext;
import org.killbill.billing.routing.plugin.api.PaymentRoutingPluginApi;
import org.killbill.billing.routing.plugin.api.PriorPaymentRoutingResult;
import org.killbill.clock.Clock;
import org.killbill.killbill.osgi.libs.killbill.OSGIConfigPropertiesService;
import org.killbill.killbill.osgi.libs.killbill.OSGIKillbillAPI;
import org.killbill.killbill.osgi.libs.killbill.OSGIKillbillLogService;

public class PluginPaymentRoutingPluginApi extends PluginApi implements PaymentRoutingPluginApi {

    public PluginPaymentRoutingPluginApi(final OSGIKillbillAPI killbillAPI, final OSGIConfigPropertiesService configProperties, final OSGIKillbillLogService logService, final Clock clock) {
        super(killbillAPI, configProperties, logService, clock);
    }

    @Override
    public PriorPaymentRoutingResult priorCall(final PaymentRoutingContext context, final Iterable<PluginProperty> properties) throws PaymentRoutingApiException {
        return new PluginPriorPaymentRoutingResult(context);
    }

    @Override
    public OnSuccessPaymentRoutingResult onSuccessCall(final PaymentRoutingContext context, final Iterable<PluginProperty> properties) throws PaymentRoutingApiException {
        return new PluginOnSuccessPaymentRoutingResult();
    }

    @Override
    public OnFailurePaymentRoutingResult onFailureCall(final PaymentRoutingContext context, final Iterable<PluginProperty> properties) throws PaymentRoutingApiException {
        return new PluginOnFailurePaymentRoutingResult();
    }
}
