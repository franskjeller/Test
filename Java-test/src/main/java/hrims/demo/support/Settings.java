package hrims.demo.support;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@ConfigurationProperties(prefix = "settings")
@Component
@Getter
@Setter
public class Settings {
    @JsonProperty("applicationName")
    public String ApplicationName;

    @JsonProperty("peopleCache")
    public String PeopleCache;

    @JsonProperty("logFile")
    public String LogFile;
}
