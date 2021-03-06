package de.zalando.zally.apireview

import com.fasterxml.jackson.core.JsonPointer
import com.fasterxml.jackson.databind.JsonNode
import de.zalando.zally.rule.TestRuleSet
import de.zalando.zally.rule.api.Check
import de.zalando.zally.rule.api.Context
import de.zalando.zally.rule.api.Rule
import de.zalando.zally.rule.api.Severity
import de.zalando.zally.rule.api.Violation
import de.zalando.zally.rule.zalando.UseOpenApiRule
import de.zalando.zally.util.ast.JsonPointers
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile

@Configuration
open class RestApiTestConfiguration {

    @Bean
    @Primary
    @Profile("test")
    open fun rules(): Collection<Any> {
        return listOf(
            TestCheckIsOpenApi3(),
            TestCheckAlwaysReport3MustViolations(),
            TestUseOpenApiRule()
        )
    }

    /** Rule used for testing  */
    @Rule(
        ruleSet = TestRuleSet::class,
        id = "TestCheckIsOpenApi3",
        severity = Severity.MUST,
        title = "TestCheckIsOpenApi3"
    )
    class TestCheckIsOpenApi3 {

        @Check(severity = Severity.MUST)
        fun validate(json: JsonNode): Violation? {
            return if ("3.0.0" != json.path("openapi").textValue()) {
                Violation("TestCheckIsOpenApi3", JsonPointer.compile("/openapi"))
            } else null
        }
    }

    /** Rule used for testing  */
    @Rule(
        ruleSet = TestRuleSet::class,
        id = "TestCheckAlwaysReport3MustViolations",
        severity = Severity.MUST,
        title = "TestCheckAlwaysReport3MustViolations"
    )
    class TestCheckAlwaysReport3MustViolations {

        @Check(severity = Severity.MUST)
        fun validate(json: JsonNode): Iterable<Violation> {
            return listOf(
                Violation("TestCheckAlwaysReport3MustViolations #1", JsonPointers.EMPTY),
                Violation("TestCheckAlwaysReport3MustViolations #2", JsonPointers.EMPTY),
                Violation("TestCheckAlwaysReport3MustViolations #3", JsonPointers.EMPTY)
            )
        }
    }

    /** Rule used for testing  */
    @Rule(ruleSet = TestRuleSet::class, id = UseOpenApiRule.id, severity = Severity.MUST, title = "TestUseOpenApiRule")
    class TestUseOpenApiRule {

        @Check(severity = Severity.HINT)
        fun validate(context: Context): Iterable<Violation> {
            return emptyList()
        }
    }
}
