package hrims.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.anyList;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import hrims.demo.data.Person;
import hrims.demo.services.CacheReaderService;
import hrims.demo.services.PeopleService;

class PeopleServiceTests {

	@Mock
	private CacheReaderService _cacheReader;

	@InjectMocks
    private PeopleService _service;

	private List<Person> _testPeople;

	@BeforeEach
    public void setUp() {
		MockitoAnnotations.openMocks(this);
		
		var person1 = new Person();
		person1.Name = "Name1";
		person1.Email = "Email1";
		person1.NationalId = "Nid1";
		var person2 = new Person();
		person2.Name = "Name2";
		person2.Email = "Email2";
		person2.NationalId = "Nid2";
		var person3 = new Person();
		person3.Name = "Name3";
		person3.Email = "Email3";
		person3.NationalId = "Nid3";

		_testPeople = new ArrayList<>();
		_testPeople.add(person1);
		_testPeople.add(person2);
		_testPeople.add(person3);
    }

	@Test
	void getPersonByNidReturnPersonOnMatch() throws IOException {

		doReturn(_testPeople).when(_cacheReader).loadPeople();

		var match = _service.getPersonByNid("Nid1");
		
		assertThat(match).isNotNull();
		assertThat(match.NationalId).isNotNull().isEqualTo("Nid1");
		assertThat(match.Name).isNotNull().isEqualTo("Name1");
		assertThat(match.Email).isNotNull().isEqualTo("Email1");
	}

	@Test
	void getPersonByNidReturnsNullIfNoMatch() throws IOException {
		
		doReturn(_testPeople).when(_cacheReader).loadPeople();

		var match = _service.getPersonByNid("DoesNotExist");
		
		assertThat(match).isNull();
	}

	@Test
	void addOrUpdatePersonUpdatesPersonIfAlreadyExists() throws IOException {

		doReturn(_testPeople).when(_cacheReader).loadPeople();
		doNothing().when(_cacheReader).savePeople(anyList());

		var update = new Person();
		update.Name = "New Name";
		update.Email = "Email1";
		update.NationalId = "Nid1";

		var result = _service.addOrUpdatePerson(update);

		// Capture argument passed to the savePeople method
        ArgumentCaptor<List<Person>> captor = ArgumentCaptor.forClass(List.class);
		verify(_cacheReader).savePeople(captor.capture());
        List<Person> savedPeople = captor.getValue();

		assertThat(savedPeople).isNotNull();
		assertThat(savedPeople.size()).isEqualTo(3);
		assertThat(result).isEqualTo("Person with national id " + update.NationalId + " was updated in cache. Currently contains 3 people.");

		var updatedPerson = savedPeople.stream().filter(x -> x.NationalId.equals(update.NationalId)).findFirst().orElse(null);
		assertThat(updatedPerson).isNotNull();
		assertThat(updatedPerson.NationalId).isEqualTo(update.NationalId);
		assertThat(updatedPerson.Name).isEqualTo(update.Name);
		assertThat(updatedPerson.Email).isEqualTo(update.Email);
	}

	@Test
	void addOrUpdatePersonAddsPersonIfItDoesNotExist() throws IOException {
		
		doReturn(_testPeople).when(_cacheReader).loadPeople();
		doNothing().when(_cacheReader).savePeople(anyList());

		var newPerson = new Person();
		newPerson.Name = "NewName";
		newPerson.Email = "NewEmail";
		newPerson.NationalId = "NewNid";

		var result = _service.addOrUpdatePerson(newPerson);
		
		// Capture argument passed to the savePeople method
        ArgumentCaptor<List<Person>> captor = ArgumentCaptor.forClass(List.class);
		verify(_cacheReader).savePeople(captor.capture());
        List<Person> savedPeople = captor.getValue();

		assertThat(savedPeople).isNotNull();
		assertThat(savedPeople.size()).isEqualTo(4);
		assertThat(result).isEqualTo("Person with national id " + newPerson.NationalId + " was added to cache. Currently contains 4 people.");

		var insertedPerson = savedPeople.stream().filter(x -> x.NationalId.equals(newPerson.NationalId)).findFirst().orElse(null);
		assertThat(insertedPerson).isNotNull();
		assertThat(insertedPerson.NationalId).isEqualTo(newPerson.NationalId);
		assertThat(insertedPerson.Name).isEqualTo(newPerson.Name);
		assertThat(insertedPerson.Email).isEqualTo(newPerson.Email);
	}

}
