package dmit2015.restclient.openweathermap;

import jakarta.annotation.Generated;

import java.util.LinkedHashMap;
import java.util.Map;

@Generated("jsonschema2pojo")
public class Wind {

    private Double speed;
    private Integer deg;
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Integer getDeg() {
        return deg;
    }

    public void setDeg(Integer deg) {
        this.deg = deg;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
