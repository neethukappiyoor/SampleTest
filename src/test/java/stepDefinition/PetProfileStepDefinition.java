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

public class PetProfileStepDefinition extends PetApiFunctions  {
    private Response res = null; // Response
    private static String petUrl = null;
    private PetDetails petInfo = null;

    @After
    public void tearDown() {
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

    @Then("I can search pet info by status  {string} exists")
    public void iCanSearchPetInfoByStatusExists(String status) {
        PetDetails[] petResponse = PetApiFunctions
                .findPetInfoByStatus(PetProfileStepDefinition.petUrl + "/findByStatus?status=" + status);
        for (PetDetails petProfile : petResponse) {
            if (petProfile.getId().equals(Integer.parseInt(status))) {
                Assert.assertEquals("Verify Pet Status!", petProfile.getStatus(), status);
            }
        }
    }

    @Then("Update a pet in the store with form data {string}{string}{string}")
    public void updateAPetInTheStoreWithFormData(String petId, String petName, String petStatus) {
        String param = "name=" + petName + "&status=" + petStatus;
        PetApiResponse expectedResponse = PetApiFunctions
                .updatePetDataWithFormData(PetProfileStepDefinition.petUrl + "/" + petId, param);
        Assert.assertEquals("Status Check Passed!", "200", expectedResponse.getCode().toString());
        Assert.assertNotNull("type field in response is not empty", expectedResponse.getType());
        Assert.assertEquals("Message return id", petId, expectedResponse.getMessage());
    }
}

