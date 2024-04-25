package rs.edu.raf.userservice.integration.contactController;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.core.options.Constants.GLUE_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features/integration/contactController")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "rs.edu.raf.userservice.integration.contactController")
public class ContactControllerTests {
}
