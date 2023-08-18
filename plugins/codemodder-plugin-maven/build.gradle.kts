plugins {
    id("io.codemodder.java-library")
    id("io.codemodder.maven-publish")
}

description = "Plugin for providing Maven dependency management functions to codemods."

dependencies {
    compileOnly(libs.jetbrains.annotations)
    implementation(project(":framework:codemodder-base"))
    implementation("io.github.pixee.maven:pom-operator:0.0.21") // TODO bring into monorepo
    {
        exclude(group = "com.google.inject", module = "guice")
    }
    implementation("com.google.inject:guice:5.1.0")

    testImplementation(testlibs.bundles.junit.jupiter)
    testImplementation(testlibs.bundles.hamcrest)
    testImplementation(testlibs.assertj)
    testImplementation(testlibs.jgit)
    testImplementation(testlibs.mockito)
    testRuntimeOnly(testlibs.junit.jupiter.engine)
}
