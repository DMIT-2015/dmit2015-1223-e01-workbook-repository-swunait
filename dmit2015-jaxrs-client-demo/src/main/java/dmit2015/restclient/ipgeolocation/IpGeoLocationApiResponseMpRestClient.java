package dmit2015.restclient.ipgeolocation;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.QueryParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 * The baseUri for the web MpRestClient be set in either microprofile-config.properties (recommended)
 * or in this file using @RegisterRestClient(baseUri = "http://server/path").
 * <p>
 * To set the baseUri in microprofile-config.properties:
 * 1) Open src/main/resources/META-INF/microprofile-config.properties
 * 2) Add a key/value pair in the following format:
 * package-name.ClassName/mp-rest/url=baseUri
 * For example:
 * package-name:    dmit2015.restclient
 * ClassName:       IpGeoLocationApiResponseMpRestClient
 * baseUri:         http://localhost:8080/contextName
 * The key/value pair you need to add is:
 * <code>
 * dmit2015.restclient.IpGeoLocationApiResponseMpRestClient/mp-rest/url=http://localhost:8080/contextName
 * </code>
 * <p>
 * To use the client interface from an environment does support CDI, add @Inject and @RestClient before the field declaration such as:
 * <code>
 *
 * @Inject
 * @RestClient private IpGeoLocationApiResponseMpRestClient _ipgeolocationapiresponseMpRestClient;
 * </code>
 * <p>
 * To use the client interface from an environment that does not support CDI, you can use the RestClientBuilder class to programmatically build an instance as follows:
 * <code>
 * URI apiURI = new URI("http://sever/contextName");
 * IpGeoLocationApiResponseMpRestClient _ipgeolocationapiresponseMpRestClient = RestClientBuilder.newBuilder().baseUri(apiURi).build(IpGeoLocationApiResponseMpRestClient.class);
 * </code>
 */
@RequestScoped
@RegisterRestClient(baseUri = "https://api.ipgeolocation.io/ipgeo")
public interface IpGeoLocationApiResponseMpRestClient {

    @GET
    IpGeoLocationApiResponse getGeoLocation(
            @QueryParam("apiKey") String apiKey,
            @QueryParam("ip") String ipAddress);

}