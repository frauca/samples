package clean.code.args;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Optional;
import java.util.Set;
import org.junit.Test;

public class ConfigurationParserTest {
  ConfigurationParser parserConfiguration = new ConfigurationParser();

  @Test
  public void givenString_whenOneArgumentConfiguration_thenConfigurationCouldBeGetted(){
    Set<Configuration> configurations = parserConfiguration.parse("l");
    Optional<Configuration> lArgument = configurations.stream().findFirst();
    assertThat(configurations.size(),is(1));
    assertTrue(lArgument.isPresent());
    assertThat(lArgument.get().getArgumentName(),is("l"));

  }

  @Test
  public void givenConfiguration_whenIntegerArgumen_thenIntegerConfig(){
    Set<Configuration> configurations = parserConfiguration.parse("l#");
    Optional<Configuration> lArgument = configurations.stream().findFirst();
    assertThat(configurations.size(),is(1));
    assertTrue(lArgument.isPresent());
    assertThat(lArgument.get().getArgumentName(),is("l"));
  }

}
