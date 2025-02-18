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

import hrims.demo.data.Person;
import hrims.demo.support.Settings;

@Service
public class PeopleService {
    
    @Autowired
    private Settings _settings;

    public Person getPersonByNid(String nid) throws IOException {
        var people = loadPeople();
        
        return people.stream()
            .filter(p -> p.NationalId.equals(nid))
            .findFirst().orElse(null);
    }

    public String addOrUpdatePerson(Person person) throws IOException {
        var people = loadPeople();
        
        var matchingPerson = people.stream()
            .filter(p -> p.NationalId.equals(person.NationalId))
            .findFirst().orElse(null);

        String result = "";
        if (matchingPerson == null) {
            people.add(person);
            result = "Person with national id " + person.NationalId + " was added to cache. Currently " + people.size() + " people.";
        } else {
            matchingPerson.Name = person.Name;
            matchingPerson.Email = person.Email;
            result = "Person with national id " + person.NationalId + " was updated in cache. Currently " + people.size() + " people.";
        }

        savePeople(people);

        return result;
    }

    private List<Person> loadPeople() throws IOException {
        var file = new File(_settings.PeopleCache);
        if (!file.exists() || !file.isFile()) {
            return new ArrayList<Person>();
        }

        var mapper = new ObjectMapper();
        return mapper.readValue(file, new TypeReference<List<Person>>() {}); 
    }

    private void savePeople(List<Person> people) throws IOException {

        if (people == null) {
            people = new ArrayList<Person>();
        }

        var mapper = new ObjectMapper();

        mapper.enable(SerializationFeature.INDENT_OUTPUT); 
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE);

        mapper.writeValue(new File(_settings.PeopleCache), people);
    }

}
