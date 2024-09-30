plugins {
    id("java")
}

group = "dev.consti"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.java-websocket:Java-WebSocket:1.5.7")
    implementation("org.json:json:20240303")
}
