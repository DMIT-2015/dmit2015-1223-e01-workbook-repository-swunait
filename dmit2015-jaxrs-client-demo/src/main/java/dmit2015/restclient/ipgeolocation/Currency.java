
package dmit2015.restclient.ipgeolocation;

import jakarta.annotation.Generated;

import java.util.LinkedHashMap;
import java.util.Map;

@Generated("jsonschema2pojo")
public class Currency {

    private String code;
    private String name;
    private String symbol;
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
