package br.com.unicred.cards.proposal.behavior;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;

@RunWith(Cucumber.class)
@ActiveProfiles("integration-test")
@CucumberOptions(
    plugin = {
        "pretty",
        "json:target/cucumber-report/cucumber.json",
        "io.qameta.allure.cucumber4jvm.AllureCucumber4Jvm"
    },
    features = "src/test/resources/behavior/feature",
    snippets = CucumberOptions.SnippetType.CAMELCASE,
    tags = "not @development"
)
public class BehaviorTest {

}
