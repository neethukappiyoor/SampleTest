package stepDefinition;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import cucumber.api.DataTable.*;

import org.assertj.core.api.SoftAssertions;
import org.junit.Assert;


import cucumber.api.java.After;
import cucumber.api.java.Before;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import pageObjectModel.pet.PetApiResponse;
import pageObjectModel.pet.PetDetails;

public class PetProfileStepDefinition {
    private Response res = null; // Response
    private SoftAssertions softAssertion = null;
    private static String petUrl = null;
    private PetDetails petInfo = null;

    @Before
    public void setup() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2";
        this.softAssertion = new SoftAssertions();
    }

    @After
    public void tearDown() {
        this.softAssertion.assertAll();
        RestAssured.reset();
    }
    @Given("As a shop owner, I would add new pet to the store with the below data")
    public void asAShopOwnerIWouldAddNewPetToTheStoreWithTheBelowData(DataTable dataTable)
    {

        List<Map<String,String>> data = dataTable.asMaps(String.class,String.class);
        Map<String, String> petData = data.get(0);
        PetProfileStepDefinition.petUrl = petData.get("url");
        this.petInfo = PetApiFunctions.createPetClass(petData);
        this.res = PetApiFunctions.createPetRequest(PetProfileStepDefinition.petUrl, this.petInfo);
        PetDetails petResponse = PetApiFunctions.validatePetInfoIsAdded(this.res);
        PetApiFunctions.comparePetInfo(softAssertion, this.petInfo, petResponse);
    }

    @When("I search with pet by ID {string}")
    public void iSearchWithPetByID(String petId) {
        PetDetails petResponse = PetApiFunctions.fetchPetInfoById(PetProfileStepDefinition.petUrl, petId);
        PetApiFunctions.comparePetInfo(softAssertion, this.petInfo, petResponse);
    }

    @Then("I upload a pet image {string} by {string}")
    public void iUploadAPetImageBy(String image, String petId) {
        String dir = System.getProperty("user.dir");
        PetApiResponse expectedResponse = PetApiFunctions.uploadImageOfPetById(dir + image, PetProfileStepDefinition.petUrl,
                petId);
        Assert.assertEquals("Status Check Passed!", "200", expectedResponse.getCode().toString());
        Assert.assertNotNull("type field in response is not empty", expectedResponse.getType());
        Assert.assertNotNull("Message field in response is not empty", expectedResponse.getMessage());

    }

    @Then("I can update the Pet info with below data and Validate")
    public void iCanUpdateThePetInfoWithBelowDataAndValidate(DataTable dataTable) {
        List<Map<String,String>> data = dataTable.asMaps(String.class,String.class);
        Map<String, String> petDataToBeUpdated = data.get(0);
        PetDetails toBeUpdated = PetApiFunctions.createPetClass(petDataToBeUpdated);
        PetDetails actualResponse = PetApiFunctions.updatePetRequest(PetProfileStepDefinition.petUrl, toBeUpdated);
        PetApiFunctions.comparePetInfo(softAssertion, toBeUpdated, actualResponse);


    }


    @And("I can delete the pet profile by id {string}")
    public void iCanDeleteThePetProfileById(String petId) {
        PetApiResponse expectedResponse = PetApiFunctions.deletePetInfoById(PetProfileStepDefinition.petUrl, petId);
        Assert.assertEquals("Status Check Passed!", "200", expectedResponse.getCode().toString());
        Assert.assertNotNull("type field in response is not empty", expectedResponse.getType());
        Assert.assertEquals("Message return id", petId, expectedResponse.getMessage());

    }
}

