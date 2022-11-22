plugins {
    id("java")
    id("application")
}

val javaMainClass = "ar.edu.uade.programacion3.tateti.CmdLineTateti"

application {
    mainClass.set(javaMainClass)
}

group = "ar.edu.uade.programacion3.tateti"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}