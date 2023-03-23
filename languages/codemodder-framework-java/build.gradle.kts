@Suppress("DSL_SCOPE_VIOLATION") // https://github.com/gradle/gradle/issues/22797
plugins {
    id("io.openpixee.codetl.base")
    id("io.openpixee.codetl.java-library")
    id("io.openpixee.codetl.maven-publish")
    alias(libs.plugins.fileversioning)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

publishing {
    publications {
        register<MavenPublication>("maven") {
            from(components["java"])
            artifactId = "codemodder-framework-java"
        }
    }
}

dependencies {
    compileOnly(libs.jetbrains.annotations)

    api("io.github.pixee:codetf-java:0.0.2") // TODO bring codetf-java into the monorepo
    implementation(libs.dom4j)
    api(libs.guice)
    api(libs.contrast.sarif)
    api(libs.java.security.toolkit)
    api(libs.javaparser.core)
    api(libs.javaparser.symbolsolver.core)
    api(libs.javaparser.symbolsolver.logic)
    api(libs.javaparser.symbolsolver.model)
    implementation(libs.logback.classic)
    implementation(libs.maven.model)
    api(libs.slf4j.api)
    api(project(":languages:codemodder-common"))

    testImplementation(testlibs.bundles.junit.jupiter)
    testImplementation(testlibs.bundles.hamcrest)
    testImplementation(testlibs.assertj)
    testImplementation(testlibs.mockito)

    testRuntimeOnly(testlibs.junit.jupiter.engine)
}
