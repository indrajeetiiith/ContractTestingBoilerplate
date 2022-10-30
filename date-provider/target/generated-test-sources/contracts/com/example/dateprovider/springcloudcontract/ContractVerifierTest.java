package com.example.dateprovider.springcloudcontract;

import com.example.dateprovider.springcloudcontract.BaseTestClass;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import io.restassured.module.mockmvc.specification.MockMvcRequestSpecification;
import io.restassured.response.ResponseOptions;

import static org.springframework.cloud.contract.verifier.assertion.SpringCloudContractAssertions.assertThat;
import static org.springframework.cloud.contract.verifier.util.ContractVerifierUtil.*;
import static com.toomuchcoding.jsonassert.JsonAssertion.assertThatJson;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.*;

@SuppressWarnings("rawtypes")
public class ContractVerifierTest extends BaseTestClass {

	@Test
	public void validate_shouldReturnValidDateWhenRequestParamHasValidDate() throws Exception {
		// given:
			MockMvcRequestSpecification request = given()
					.header("Content-Type", "application/json");

		// when:
			ResponseOptions response = given().spec(request)
					.queryParam("date","2001-02-03")
					.get("/provider/validDate");

		// then:
			assertThat(response.statusCode()).isEqualTo(200);
			assertThat(response.header("Content-Type")).matches("application/json.*");

		// and:
			DocumentContext parsedJson = JsonPath.parse(response.getBody().asString());
			assertThatJson(parsedJson).field("['givenDate']").matches("(\\d\\d\\d\\d)-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])");
			assertThatJson(parsedJson).field("['year']").matches("(190[0-9]|19[5-9]\\d|200\\d|2020)");
			assertThatJson(parsedJson).field("['month']").matches("(1[0-2]|[1-9])");
			assertThatJson(parsedJson).field("['day']").matches("(3[01]|[12][0-9]|[1-9])");
			assertThatJson(parsedJson).field("['isValidDate']").isEqualTo(true);
			assertThatJson(parsedJson).field("['message']").isEqualTo("date parsed successfully");
	}

}
