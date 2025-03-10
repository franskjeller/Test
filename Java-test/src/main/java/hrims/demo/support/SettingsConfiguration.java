package hrims.demo.support;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import hrims.demo.support.Settings;

@Configuration
public class SettingsConfiguration  {

    @Bean
    public Settings settings() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        
        // Locate and read the JSON file from resources
        ClassPathResource resource = new ClassPathResource("settings.json");
        
        // Convert JSON string from file to Settings object
        return objectMapper.readValue(resource.getInputStream(), Settings.class);
    }
}
