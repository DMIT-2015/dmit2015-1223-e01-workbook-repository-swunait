package dmit2015.persistence;

import dmit2015.config.ApplicationConfig;
import dmit2015.entity.Country;
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
public class CountryArquillianIT { // The class must be declared as public

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
                .addClasses(Country.class, CountryRepository.class)
                // TODO Add any additional libraries, packages, classes or resource files required
                // .addClasses(ApplicationStartupListener.class)
                .addPackage("dmit2015.entity")
                .addAsResource("META-INF/persistence.xml")
                // .addAsResource(new File("src/test/resources/META-INF/persistence-entity.xml"),"META-INF/persistence.xml")
                .addAsResource("META-INF/beans.xml");
    }

    @Inject
    private CountryRepository _countryRepository;

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
            "25,AU,Australia,40,ZW,Zimbabwe,50"
    })
    void findAll_Size_BoundaryValues(int expectedSize,
                                     String expectedFirstCountryId,
                                     String expectedFirstCountryName,
                                     BigInteger expectedFirstRegionId,
                                     String expectedLastCountryId,
                                     String expectedLastCountryName,
                                     BigInteger expectedLastRegionId) {
        assertThat(_countryRepository).isNotNull();
        // Arrange and Act
        List<Country> countryList = _countryRepository.findAll();
        // Assert
        assertThat(countryList.size())
                .isEqualTo(expectedSize);

        // Get the first entity and compare with expected results
        var firstCountry = countryList.get(0);
        assertThat(firstCountry.getCountryId()).isEqualTo(expectedFirstCountryId);
        assertThat(firstCountry.getCountryName()).isEqualTo(expectedFirstCountryName);
        assertThat(firstCountry.getRegionId()).isEqualTo(expectedFirstRegionId);

        // Get the last entity and compare with expected results
        var lastCountry = countryList.get(countryList.size() - 1);
        assertThat(lastCountry.getCountryId()).isEqualTo(expectedLastCountryId);
        assertThat(lastCountry.getCountryName()).isEqualTo(expectedLastCountryName);
        assertThat(lastCountry.getRegionId()).isEqualTo(expectedLastRegionId);

    }


    @Order(2)
    @ParameterizedTest
    @CsvSource(value = {
            "AU,Australia,40",
            "ZW,Zimbabwe,50"
    })
    void findById_ExistingId_IsPresent(String countryId, String countryName, BigInteger regionId) {
        // Arrange and Act
        Optional<Country> optionalCountry = _countryRepository.findById(countryId);
        assertThat(optionalCountry.isPresent())
                .isTrue();
        Country existingCountry = optionalCountry.orElseThrow();

        // Assert
        assertThat(existingCountry)
                .isNotNull();
        assertThat(existingCountry.getCountryName())
             .isEqualTo(countryName);
        assertThat(existingCountry.getRegionId())
                .isEqualTo(regionId);

    }


    @Order(3)
    @ParameterizedTest
    @CsvSource(value = {
            "Alberta, 20",
            "Atlantis, 50",
    })
    void add_ValidData_Added(String countryName, BigInteger regionId) throws SystemException, NotSupportedException {
        // Arrange
        Country newCountry = new Country();
        newCountry.setCountryName(countryName);
        var optionalRegion = _regionRepository.findById(regionId);
        assertThat(optionalRegion.isPresent()).isTrue();
        Region currentRegion = optionalRegion.orElseThrow();
        newCountry.setRegionsByRegionId(currentRegion);

        _beanManagedTransaction.begin();

        try {
            // Act
            _countryRepository.add(newCountry);

            // Assert
            Optional<Country> optionalCountry = _countryRepository.findById(newCountry.getCountryId());
            assertThat(optionalCountry.isPresent())
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
            "AU,New Australia,40",
            "ZW,New Zimbabwe,50"
    })
    void update_ExistingId_UpdatedData(String countryId, String countryName, BigInteger regionId) throws SystemException, NotSupportedException {
        // Arrange
        Optional<Country> optionalCountry = _countryRepository.findById(countryId);
        assertThat(optionalCountry.isPresent()).isTrue();

        Country existingCountry = optionalCountry.orElseThrow();
        assertThat(existingCountry).isNotNull();

        // Act
        existingCountry.setCountryName(countryName);
        var optionalRegion = _regionRepository.findById(regionId);
        assertThat(optionalRegion.isPresent()).isTrue();
        Region currentRegion = optionalRegion.orElseThrow();
        existingCountry.setRegionsByRegionId(currentRegion);

        _beanManagedTransaction.begin();

        try {
            Country updatedCountry = _countryRepository.update(countryId, existingCountry);

            // Assert
            assertThat(existingCountry)
                    .usingDefaultComparator()
                    .isEqualTo(updatedCountry);
        } catch (Exception ex) {
            fail("Failed to update entity with exception", ex.getMessage());
        } finally {
            _beanManagedTransaction.rollback();
        }

    }


    @Order(5)
    @ParameterizedTest
    @CsvSource(value = {
            "ZM",
            "ZW",
    })
    void deleteById_ExistingId_DeletedData(String countryId) throws SystemException, NotSupportedException {
        _beanManagedTransaction.begin();

        try {
            // Arrange and Act
            _countryRepository.deleteById(countryId);

            // Assert
            assertThat(_countryRepository.findById(countryId))
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
            "AA",
            "ZZ"
    })
    void findById_NonExistingId_IsEmpty(String countryId) {
        // Arrange and Act
        Optional<Country> optionalCountry = _countryRepository.findById(countryId);

        // Assert
        assertThat(optionalCountry.isEmpty())
                .isTrue();

    }


    @Order(7)
    @ParameterizedTest
    @CsvSource(value = {
            ", 20, Country Name cannot be blank",
            "   , 50, Country Name cannot be blank",
    }, nullValues = {"null"})
    void create_beanValidation_shouldFail(String countryName, BigInteger regionId, String expectedExceptionMessage) throws SystemException, NotSupportedException {
        // Arrange
        Country newCountry = new Country();
        newCountry.setCountryName(countryName);

        Region currentRegion = _regionRepository.findById(regionId).orElseThrow();
        newCountry.setRegionsByRegionId(currentRegion);

        _beanManagedTransaction.begin();
        try {
            // Act
            _countryRepository.add(newCountry);
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