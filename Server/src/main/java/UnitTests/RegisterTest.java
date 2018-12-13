package UnitTests;

import org.junit.Test;

import Request.EventsRequest;
import Request.PersonsRequest;
import Request.RegisterRequest;
import Result.EventsResult;
import Result.PersonsResult;
import Result.RegisterResult;
import Service.ClearService;
import Service.EventService;
import Service.PersonService;
import Service.RegisterService;

import static org.junit.Assert.assertEquals;

public class RegisterTest {

    @Test
    public void testRegister()
    {
        System.out.println("Testing registering a new user");
        ClearService clearService = new ClearService();
        clearService.clear();

        RegisterService registerService = new RegisterService();
        RegisterRequest request = new RegisterRequest("Jeremy","Jeremy","jeremy@byu.edu","Jeremy","Van Patten", "m");
        RegisterResult result = registerService.register(request);

        PersonService personService = new PersonService();
        PersonsRequest pRequest = new PersonsRequest(result.getAuthToken());
        PersonsResult pResult = personService.getPersons(pRequest);
        assertEquals(31,pResult.getData().size());

        EventService eventService = new EventService();
        EventsRequest eRequest = new EventsRequest(result.getAuthToken());
        EventsResult eResult = eventService.getEvents(eRequest);
        assertEquals(116,eResult.getData().size());

        System.out.println("Passed");

        System.out.println("Testing registering a user with an existing username");
        result = registerService.register(request);
        assertEquals("Username is already taken",result.getMessage());

        System.out.println("Passed");

    }
}
