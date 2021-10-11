package infinityshopping.online.app;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("infinityshopping.online.app");

        noClasses()
            .that()
            .resideInAnyPackage("infinityshopping.online.app.service..")
            .or()
            .resideInAnyPackage("infinityshopping.online.app.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..infinityshopping.online.app.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
