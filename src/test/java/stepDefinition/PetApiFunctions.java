package stepDefinition;

import Helper.RequestBuilderHelper;
import pageObjectModel.pet.Category;
import pageObjectModel.pet.PetApiResponse;
import pageObjectModel.pet.PetDetails;
import pageObjectModel.pet.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.SoftAssertions;
import org.junit.Assert;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class PetApiFunctions {

    public static PetDetails createPetClass(Map<String, String> petData) {
        Category category = new Category(Integer.parseInt(petData.get("categoryId")),petData.get("categoryName"));
        List<String> photoUrls = new ArrayList<>();
        photoUrls.add(petData.get("photoUrls"));
        List<Tag> tags = new ArrayList<>();
        Tag tagData = new Tag(Integer.parseInt(petData.get("tagsId")),petData.get("tagsName"));
        tags.add(tagData);
        PetDetails petInfo = new PetDetails(Integer.parseInt(petData.get("petId")),category,petData.get("petName"),photoUrls,tags,petData.get("status"));
        return petInfo;
    }

    public static Response createPetRequest(String url, PetDetails petInfo) {
        RequestBuilderHelper apiRequestBuilder = new RequestBuilderHelper(url, "application/json", petInfo);
        RequestSpecification requestSpec = apiRequestBuilder.getRequestSpecification();
        requestSpec = RestAssured.given().spec(requestSpec);
        Response res = requestSpec.when().post();
        return res;

    }

    public static PetDetails validatePetInfoIsAdded(Response res) {
        System.out.println(res.getBody().asString());
        PetDetails petInfo = petResponseDeSerialization(res);
        Assert.assertEquals("Status Check Passed!", 200, res.getStatusCode());
        return petInfo;
    }

    private static PetDetails petResponseDeSerialization(Response res) {
        return res.as(PetDetails.class);
    }

    public static void comparePetInfo(SoftAssertions softAssertion, PetDetails expectedPetInfo, PetDetails actualPetResponse) {
        softAssertion.assertThat(expectedPetInfo.getId()).isEqualTo(actualPetResponse.getId());
        softAssertion.assertThat(expectedPetInfo.getName()).isEqualTo(actualPetResponse.getName());
        softAssertion.assertThat(expectedPetInfo.getStatus()).isEqualTo(actualPetResponse.getStatus());
        softAssertion.assertThat(expectedPetInfo.getCategory().getId()).isEqualTo(actualPetResponse.getCategory().getId());
        softAssertion.assertThat(expectedPetInfo.getCategory().getName()).isEqualTo(actualPetResponse.getCategory().getName());
        softAssertion.assertThat(expectedPetInfo.getTags().get(0).getId()).isEqualTo(actualPetResponse.getTags().get(0).getId());
        softAssertion.assertThat(expectedPetInfo.getTags().get(0).getName()).isEqualTo(actualPetResponse.getTags().get(0).getName());
        softAssertion.assertThat(expectedPetInfo.getPhotoUrls().get(0)).isEqualTo(actualPetResponse.getPhotoUrls().get(0));

    }

    public static PetDetails fetchPetInfoById(String url, String petId) {
        RequestBuilderHelper apiRequestBuilder = new RequestBuilderHelper(url + "/" + petId, "application/json", null);
        RequestSpecification requestSpec = apiRequestBuilder.getRequestSpecification();
        requestSpec = RestAssured.given().spec(requestSpec);
        Response res = requestSpec.when().get();
        PetDetails expectedResponse = res.as(PetDetails.class);
        Assert.assertEquals("Status Check Passed!", 200, res.getStatusCode());
        return expectedResponse;

    }

    public static PetApiResponse uploadImageOfPetById(String image, String petUrl, String petId) {
        RequestBuilderHelper apiRequestBuilder = new RequestBuilderHelper(petUrl + "/" + petId+"/uploadImage",image);
        RequestSpecification requestSpec = apiRequestBuilder.getRequestSpecification();
        requestSpec = RestAssured.given().spec(requestSpec);
        Response res = requestSpec.when().post();
        Assert.assertEquals("Status Check Passed!", 200, res.getStatusCode());
        return res.as(PetApiResponse.class);
    }

    public static PetApiResponse deletePetInfoById(String petUrl, String petId) {
        RequestBuilderHelper apiRequestBuilder = new RequestBuilderHelper(petUrl + "/" + petId, "application/json", null);
        RequestSpecification requestSpec = apiRequestBuilder.getRequestSpecification();
        requestSpec = RestAssured.given().spec(requestSpec);
        Response res = requestSpec.when().delete();
        PetApiResponse expectedResponse = res.as(PetApiResponse.class);
        Assert.assertEquals("Status Check Passed!", 200, res.getStatusCode());
        res = requestSpec.when().get();
        Assert.assertEquals("Verify pet record is deleted!", 404, res.getStatusCode());
        return expectedResponse;
    }

    public static PetDetails updatePetRequest(String petUrl, PetDetails toBeUpdated) {
        RequestBuilderHelper apiRequestBuilder = new RequestBuilderHelper(petUrl,"application/json",toBeUpdated);
        RequestSpecification requestSpec = apiRequestBuilder.getRequestSpecification();
        requestSpec = RestAssured.given().spec(requestSpec);
        Response res = requestSpec.when().put();
        PetDetails expectedResponse = res.as(PetDetails.class);
        Assert.assertEquals("Status Check Passed!", 200, res.getStatusCode());
        return expectedResponse;
    }

    public static PetDetails[] findPetInfoByStatus(String petUrl) {
        RequestBuilderHelper apiRequestBuilder = new RequestBuilderHelper(petUrl,"application/json",null);
        RequestSpecification requestSpec = apiRequestBuilder.getRequestSpecification();
        requestSpec = RestAssured.given().spec(requestSpec);
        Response res = requestSpec.when().get();
        PetDetails[] expectedResponse = res.as(PetDetails[].class);
        Assert.assertEquals("Status Check Passed!", 200, res.getStatusCode());
        return expectedResponse;
    }

    public static PetApiResponse updatePetDataWithFormData(String url, String formData) {
        RequestBuilderHelper apiRequestBuilder = new RequestBuilderHelper(url, "application/x-www-form-urlencoded", formData);
        RequestSpecification requestSpec = apiRequestBuilder.getRequestSpecification();
        requestSpec = RestAssured.given().spec(requestSpec);
        Response res = requestSpec.when().post();
        Assert.assertEquals("Status Check Passed!", 200, res.getStatusCode());
        return res.as(PetApiResponse.class);

    }
}
