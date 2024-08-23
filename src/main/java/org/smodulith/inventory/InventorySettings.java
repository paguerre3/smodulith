package org.smodulith.inventory;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

/**
 * The @Value annotation in Spring is used to inject values
 * into fields from property files, system properties,
 * or other sources in the Spring Environment.
 * However, when using Lombok to generate boilerplate code such as
 * constructors or setters, you may need to be aware of
 * how @Value interacts with these generated methods.
 * E.g. @Value("${app.name}")
 * private final String appName;
 *
 * Lombok's @RequiredArgsConstructor generates a constructor
 * for all final fields and fields that are marked as @NonNull
 *
 * The @ConstructorBinding is a Spring Boot annotation
 * that marks the constructor to be used for binding
 * external configuration properties
 * (e.g., from application.properties or application.yml)
 * to the class fields.
 * Use Case: This is commonly used in classes annotated
 * with @ConfigurationProperties to create immutable
 * configuration objects, where the properties are bound
 * via the constructor.
 *
 */
@Value
@RequiredArgsConstructor(access = AccessLevel.PACKAGE, onConstructor = @__(@ConstructorBinding))
@ConfigurationProperties("smodulith.inventory")
class InventorySettings {

    /**
     * Some stock threshold.
     */
    int stockThreshold;
}
