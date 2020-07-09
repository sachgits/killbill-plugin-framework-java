package org.killbill.billing.plugin.api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.killbill.billing.payment.api.PluginProperty;
import org.killbill.billing.plugin.api.PluginProperties;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;

public abstract class MpesaModelPluginBase {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static List<PluginProperty> buildPluginProperties(@Nullable final String additionalData) {
        if (additionalData == null) {
            return ImmutableList.<PluginProperty>of();
        }

        final Map additionalDataMap;
        try {
            additionalDataMap = objectMapper.readValue(additionalData, Map.class);
        } catch (final IOException e) {
            return ImmutableList.<PluginProperty>of();
        }

        return PluginProperties.buildPluginProperties(additionalDataMap);
    }
}