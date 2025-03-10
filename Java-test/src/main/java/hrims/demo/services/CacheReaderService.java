package hrims.demo.services;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import hrims.demo.data.Person;
import hrims.demo.support.Settings;

@Slf4j
@Service
public class CacheReaderService {
    
    @Autowired
    private Settings _settings;

    public List<Person> loadPeople() throws IOException {
        log.info("loadPeople entered");

        var file = new File(_settings.PeopleCache);
        if (!file.exists() || !file.isFile()) {
            log.warn("No cache found");
            return new ArrayList<>();
        }

        var mapper = new ObjectMapper();
        log.info("Reading cache..");
        return mapper.readValue(file, new TypeReference<List<Person>>() {}); 
    }

    public void savePeople(List<Person> people) throws IOException {
        log.info("savePeople entered");

        if (people == null) {
            log.warn("No people to save..");
            people = new ArrayList<>();
        }

        var mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT); 
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE);

        log.info("Writing cache..");
        mapper.writeValue(new File(_settings.PeopleCache), people);
    }
}
