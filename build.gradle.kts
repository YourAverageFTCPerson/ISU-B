plugins {
    id("java")
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("application")
}

group = "average.ftc"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.openjfx:javafx-controls:23.0.1") // Update to the latest version if needed
    implementation("org.openjfx:javafx-fxml:23.0.1")    // Update to the latest version if needed
    implementation("org.openjfx:javafx-media:23.0.1")
//    implementation("com.fasterxml.jackson.core:jackson-core:2.18.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher") // https://stackoverflow.com/questions/77579350/how-do-i-get-rid-of-automatic-loading-of-test-framework-implementation-dependen
}

application {
    mainClass.set("average.ftc.Main") // Adjust to your main class
//    mainClass.set("average.ftc.AStarAlgoTest") // Adjust to your main class
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}

tasks.test {
    useJUnitPlatform()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(23))
    }
}

// If you plan to run the JavaFX application, add the necessary JavaFX configurations
javafx {
    version = "23.0.1" // Ensure the JavaFX version is compatible
    modules = listOf("javafx.controls", "javafx.fxml", "javafx.media") // Add necessary JavaFX modules
}
