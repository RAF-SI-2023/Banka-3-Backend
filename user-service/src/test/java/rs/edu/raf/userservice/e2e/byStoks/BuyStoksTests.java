package rs.edu.raf.userservice.e2e.byStoks;


import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.core.options.Constants.GLUE_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features/e2e/buyStok")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "rs.edu.raf.userservice.e2e.byStoks")
public class BuyStoksTests {
}
