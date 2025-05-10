val kotlinSdkVersion: String by project

dependencies {
    implementation(platform("software.amazon.awssdk:bom:2.24.0"))

    implementation("aws.sdk.kotlin:s3:$kotlinSdkVersion")
    implementation("aws.sdk.kotlin:s3control:$kotlinSdkVersion")
}
