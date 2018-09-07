package de.zalando.zally.rule.zalando

import de.zalando.zally.getOpenApiContextFromContent
import org.assertj.core.api.Assertions.assertThat
import org.intellij.lang.annotations.Language
import org.junit.Test

class NestedPathsMayBeRootPathsRuleTest {

    private val rule = NestedPathsMayBeRootPathsRule()

    @Test
    fun `checkNestedPaths should return violations for paths containing nested sub resources`() {
        @Language("YAML")
        val spec = """
            openapi: 3.0.1
            paths:
              "/countries/{country-id}/cities/{city-id}": {}
        """.trimIndent()
        val context = getOpenApiContextFromContent(spec)

        val violations = rule.checkNestedPaths(context)

        assertThat(violations).isNotEmpty
        assertThat(violations[0].description).contains("may be top-level resource")
        assertThat(violations[0].pointer.toString())
            .isEqualTo("/paths/~1countries~1{country-id}~1cities~1{city-id}")
    }

    @Test
    fun `checkNestedPaths should return no violations if there are no paths containing nested sub resources`() {
        @Language("YAML")
        val spec = """
            openapi: 3.0.1
            paths:
              pets/: {}
        """.trimIndent()
        val context = getOpenApiContextFromContent(spec)

        val violations = rule.checkNestedPaths(context)

        assertThat(violations).isEmpty()
    }
}
