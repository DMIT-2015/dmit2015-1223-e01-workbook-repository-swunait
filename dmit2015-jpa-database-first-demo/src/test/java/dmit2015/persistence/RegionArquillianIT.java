package dmit2015.persistence;

import dmit2015.config.ApplicationConfig;
import dmit2015.entity.Region;
import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.PomEquippedResolveStage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(ArquillianExtension.class)
public class RegionArquillianIT { // The class must be declared as public

    static String mavenArtifactIdId;

    @Deployment
    public static WebArchive createDeployment() throws IOException, XmlPullParserException {
        PomEquippedResolveStage pomFile = Maven.resolver().loadPomFromFile("pom.xml");
        MavenXpp3Reader reader = new MavenXpp3Reader();
        Model model = reader.read(new FileReader("pom.xml"));
        mavenArtifactIdId = model.getArtifactId();
        final String archiveName = model.getArtifactId() + ".war";
        return ShrinkWrap.create(WebArchive.class, archiveName)
                .addAsLibraries(pomFile.resolve("org.codehaus.plexus:plexus-utils:3.4.2").withTransitivity().asFile())
                .addAsLibraries(pomFile.resolve("org.hamcrest:hamcrest:2.2").withTransitivity().asFile())
                .addAsLibraries(pomFile.resolve("org.assertj:assertj-core:3.24.2").withTransitivity().asFile())
                .addAsLibraries(pomFile.resolve("com.h2database:h2:2.1.214").withTransitivity().asFile())
//                .addAsLibraries(pomFile.resolve("com.microsoft.sqlserver:mssql-jdbc:11.2.3.jre17").withTransitivity().asFile())
                .addAsLibraries(pomFile.resolve("com.oracle.database.jdbc:ojdbc11:23.2.0.0").withTransitivity().asFile())
                .addAsLibraries(pomFile.resolve("org.hibernate.orm:hibernate-spatial:6.2.3.Final").withTransitivity().asFile())
                // .addAsLibraries(pomFile.resolve("org.eclipse:yasson:3.0.3").withTransitivity().asFile())
                .addClass(ApplicationConfig.class)
                .addClasses(RegionRepository.class)
                // TODO Add any additional libraries, packages, classes or resource files required
                // .addClasses(ApplicationStartupListener.class)
                .addPackage("dmit2015.entity")
                .addAsResource("META-INF/persistence.xml")
                // .addAsResource(new File("src/test/resources/META-INF/persistence-entity.xml"),"META-INF/persistence.xml")
                .addAsResource("META-INF/beans.xml");
    }

    @Inject
    private RegionRepository _regionRepository;

    @Resource
    private UserTransaction _beanManagedTransaction;

    @BeforeAll
    static void beforeAllTestMethod() {
        // code to execute before test methods are executed
    }

    @BeforeEach
    void beforeEachTestMethod() {
        // Code to execute before each method such as creating the test data
    }

    @AfterEach
    void afterEachTestMethod() {
        // code to execute after each test method such as deleteing the test data
    }


    @Order(1)
    @ParameterizedTest
    @CsvSource(value = {
            "7,10,Europe,50,Africa"
    })
    void findAll_Size_BoundaryValues(int expectedSize,
                                     BigInteger expectedFirstRegionId,
                                     String expectedFirstRegionName,
                                     BigInteger expectedLastRegionId,
                                     String expectedLastRegionName) {
        assertThat(_regionRepository).isNotNull();
        // Arrange and Act
        List<Region> regionList = _regionRepository.findAll();
        // Assert
        assertThat(regionList.size())
                .isEqualTo(expectedSize);

        // Get the first entity and compare with expected results
        var firstRegion = regionList.get(0);
        assertThat(firstRegion.getRegionId()).isEqualTo(expectedFirstRegionId);
        assertThat(firstRegion.getRegionName()).isEqualTo(expectedFirstRegionName);

        // Get the last entity and compare with expected results
        var lastRegion = regionList.get(regionList.size() - 1 - 2);
        assertThat(lastRegion.getRegionId()).isEqualTo(expectedLastRegionId);
        assertThat(lastRegion.getRegionName()).isEqualTo(expectedLastRegionName);

    }


    @Order(2)
    @ParameterizedTest
    @CsvSource(value = {
            "20,Americas",
            "30,Asia"
    })
    void findById_ExistingId_IsPresent(BigInteger regionId, String regionName) {
        // Arrange and Act
        Optional<Region> optionalRegion = _regionRepository.findById(regionId);
        assertThat(optionalRegion.isPresent())
                .isTrue();
        Region existingRegion = optionalRegion.orElseThrow();

        // Assert
        assertThat(existingRegion)
                .isNotNull();
        assertThat(existingRegion.getRegionName())
             .isEqualTo(regionName);

    }


    @Order(3)
    @ParameterizedTest
    @CsvSource(value = {
            "Canada",
            "Alberta",
    })
    void add_ValidData_Added(String regionName) throws SystemException, NotSupportedException {
        // Arrange
        Region newRegion = new Region();
        newRegion.setRegionName(regionName);

        _beanManagedTransaction.begin();

        try {
            // Act
            _regionRepository.add(newRegion);

            // Assert
            Optional<Region> optionalRegion = _regionRepository.findById(newRegion.getRegionId());
            assertThat(optionalRegion.isPresent())
                 .isTrue();

        } catch (Exception ex) {
            fail("Failed to add entity with exception", ex.getMessage());
        } finally {
            _beanManagedTransaction.rollback();
        }

    }


    @Order(4)
    @ParameterizedTest
    @CsvSource(value = {
            "10, European",
            "30, South Asia",
    })
    void update_ExistingId_UpdatedData(BigInteger regionId, String regionName) throws SystemException, NotSupportedException {
        // Arrange
        Optional<Region> optionalRegion = _regionRepository.findById(regionId);
        assertThat(optionalRegion.isPresent()).isTrue();

        Region existingRegion = optionalRegion.orElseThrow();
        assertThat(existingRegion).isNotNull();

        // Act
        existingRegion.setRegionName(regionName);

        _beanManagedTransaction.begin();

        try {
            Region updatedRegion = _regionRepository.update(regionId, existingRegion);

            // Assert
            assertThat(existingRegion)
                    .usingDefaultComparator()
                    .isEqualTo(updatedRegion);
        } catch (Exception ex) {
            fail("Failed to update entity with exception", ex.getMessage());
        } finally {
            _beanManagedTransaction.rollback();
        }

    }


    @Order(5)
    @ParameterizedTest
    @CsvSource(value = {
            "60",
            "70",
    })
    void deleteById_ExistingId_DeletedData(BigInteger regionId) throws SystemException, NotSupportedException {
        _beanManagedTransaction.begin();

        try {
            // Arrange and Act
            _regionRepository.deleteById(regionId);

            // Assert
            assertThat(_regionRepository.findById(regionId))
                    .isEmpty();

        } catch (Exception ex) {
            fail("Failed to delete entity with exception message %s", ex.getMessage());
        } finally {
            _beanManagedTransaction.rollback();
        }

    }


    @Order(6)
    @ParameterizedTest
    @CsvSource(value = {
            "0",
            "99"
    })
    void findById_NonExistingId_IsEmpty(BigInteger regionId) {
        // Arrange and Act
        Optional<Region> optionalRegion = _regionRepository.findById(regionId);

        // Assert
        assertThat(optionalRegion.isEmpty())
                .isTrue();

    }


    @Order(7)
    @ParameterizedTest
    @CsvSource(value = {
            "null,Region Name cannot be blank",
            "       ,cannot be blank",
    }, nullValues = {"null"})
    void create_beanValidation_shouldFail(String regionName, String expectedExceptionMessage) throws SystemException, NotSupportedException {
        // Arrange
        Region newRegion = new Region();
        newRegion.setRegionName(regionName);

        _beanManagedTransaction.begin();
        try {
            // Act
            _regionRepository.add(newRegion);
            fail("An bean validation constraint should have been thrown");
        } catch (Exception ex) {
            // Assert
            assertThat(ex)
                    .hasMessageContaining(expectedExceptionMessage);
        } finally {
            _beanManagedTransaction.rollback();
        }

    }

}