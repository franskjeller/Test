package hrims.demo.support;

// import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

// @ConfigurationProperties(prefix = "settings")
@Component
@Data
public class Settings {
    @JsonProperty("peopleCache")
    public String PeopleCache;

    @JsonProperty("logFile")
    public String LogFile;
}
