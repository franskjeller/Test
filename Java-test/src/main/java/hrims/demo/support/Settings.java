package hrims.demo.support;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.*;

@ConfigurationProperties(prefix = "settings")
@Component
@Getter
public class Settings {
    public String ApplicationName;
    public String LogFile;
}
