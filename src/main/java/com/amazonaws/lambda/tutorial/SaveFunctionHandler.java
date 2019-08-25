package com.amazonaws.lambda.tutorial;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class SaveFunctionHandler implements RequestHandler<PersonRequest, PersonResponse> {

	private DynamoDB dynamoDb;
	private String DYNAMODB_TABLE_NAME = "Person";
	private Regions REGION = Regions.CA_CENTRAL_1;

	@Override
	public PersonResponse handleRequest(PersonRequest personRequest, Context context) {
		this.initDynamoDbClient();
		persistData(personRequest);
		PersonResponse personResponse = new PersonResponse();
		personResponse.setMessage("Saved Successfully!!!");
		return personResponse;
	}

	private PutItemOutcome persistData(PersonRequest personRequest) throws ConditionalCheckFailedException{

		PutItemOutcome putItemOutcome = this.dynamoDb.getTable(DYNAMODB_TABLE_NAME)
				.putItem(
						new PutItemSpec().withItem(new Item()
								.withNumber("id", personRequest.getId())
								.withString("firstName", personRequest.getFirstName())
								.withString("lastName", personRequest.getLastName()))
						);

		return putItemOutcome;
	}

	private void initDynamoDbClient() {
		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(REGION).build();
		this.dynamoDb = new DynamoDB(client);
	}

}
// com.amazonaws.lambda.tutorial.SaveFunctionHandler