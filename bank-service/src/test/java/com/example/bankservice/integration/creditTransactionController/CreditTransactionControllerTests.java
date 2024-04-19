package com.example.bankservice.integration.creditTransactionController;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.core.options.Constants.GLUE_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features/integration/creditTransactionController")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "com.example.bankservice.integration.creditTransactionController")
public class CreditTransactionControllerTests {
}
