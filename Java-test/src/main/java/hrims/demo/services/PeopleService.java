package hrims.demo.services;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hrims.demo.data.Person;
import hrims.demo.services.CacheReaderService;

@Service
public class PeopleService {
    
    @Autowired
    private CacheReaderService _cacheReader;

    public Person getPersonByNid(String nid) throws IOException {
        var people = _cacheReader.loadPeople();

        return people.stream()
            .filter(p -> p.NationalId.equals(nid))
            .findFirst().orElse(null);
    }

    public String addOrUpdatePerson(Person person) throws IOException {
        var people = _cacheReader.loadPeople();
        
        var matchingPerson = people.stream()
            .filter(p -> p.NationalId.equals(person.NationalId))
            .findFirst().orElse(null);

        String result = "";
        if (matchingPerson == null) {
            people.add(person);
            result = "Person with national id " + person.NationalId + " was added to cache. Currently contains " + people.size() + " people.";
        } else {
            matchingPerson.Name = person.Name;
            matchingPerson.Email = person.Email;
            result = "Person with national id " + person.NationalId + " was updated in cache. Currently contains " + people.size() + " people.";
        }

        _cacheReader.savePeople(people);

        return result;
    }
}
