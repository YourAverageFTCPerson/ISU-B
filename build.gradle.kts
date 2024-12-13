plugins {
    id("java")
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("application")
}

group = "average.ftc"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
//    maven("https://maven.openjfx.io")
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.openjfx:javafx-controls:23.0.1") // Update to the latest version if needed
    implementation("org.openjfx:javafx-fxml:23.0.1")    // Update to the latest version if needed
    implementation("org.openjfx:javafx-media:23.0.1");
}

application {
    mainClass.set("average.ftc.Main") // Adjust to your main class
}

tasks.test {
    useJUnitPlatform()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(23))
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(23)) // Set to Java 23
    }
}

// If you plan to run the JavaFX application, add the necessary JavaFX configurations
javafx {
    version = "23.0.1" // Ensure the JavaFX version is compatible
    modules = listOf("javafx.controls", "javafx.fxml", "javafx.media") // Add necessary JavaFX modules
}
