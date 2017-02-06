package com.f1x.mtcdtools.configuration;

/**
 * Created by f1x on 2017-02-04.
 */

public interface ConfigurationChangeListener {
    void onParameterChanged(String parameterName, Configuration configuration);
}
