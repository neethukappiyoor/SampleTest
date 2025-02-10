package Helper;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
public class RequestBuilderHelper {
    private RequestSpecification requestSpec = null;

    public RequestBuilderHelper(String urlPath,String contentType,Object jsonObject) {
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.setBasePath(urlPath);
        builder.setContentType(contentType);
        builder.setBody(jsonObject);
        System.out.println(urlPath);
        this.requestSpec = builder.build();
        this.requestSpec = RestAssured.given().spec(this.requestSpec);
        this.requestSpec.log().all();

    }

    public RequestBuilderHelper(String urlPath,String filePath) {
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.setBasePath(urlPath);
        builder.setContentType("multipart/form-data");
        builder.addMultiPart("file",filePath,"image/jpeg");
        this.requestSpec = builder.build();
        this.requestSpec = RestAssured.given().spec(this.requestSpec);
        this.requestSpec.log().all();
    }

    public  RequestSpecification getRequestSpecification() {
        return this.requestSpec;
    }
}
