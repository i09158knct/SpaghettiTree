import org.jetbrains.compose.compose

plugins {
    kotlin("jvm") version "1.4.0"
    id("org.jetbrains.compose") version "0.1.0-m1-build62"
}

repositories {
    jcenter()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("org.eclipse.jgit", "org.eclipse.jgit", "5.9.0.202009080501-r")
//    implementation group: 'org.slf4j', name: 'slf4j-simple', version: '1.7.21'
}

compose.desktop {
    application {
        mainClass = "MainKt"
    }
}

