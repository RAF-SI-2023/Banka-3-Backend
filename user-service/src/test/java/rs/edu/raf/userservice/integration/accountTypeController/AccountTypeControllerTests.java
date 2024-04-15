package rs.edu.raf.userservice.integration.accountTypeController;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.core.options.Constants.GLUE_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features/integration/accountTypeController")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "rs.edu.raf.userservice.integration.accountTypeController")
public class AccountTypeControllerTests{
}
