@api
Feature: Demo Pet Store

  @petstore
  Scenario Outline:  As a Pet Store Owner, I would like add new pet profile, upload pet image and update pet profile, view pet info by status or create new pet profile with form data and delete pet profile
    Given As a shop owner, I would add new pet to the store with the below data

        | url		| petId | categoryId | categoryName	| petName		| photoUrls									    | tagsId | tagsName	| status		|
        | /pet	    | 10	| 0		     | dog		    | Oreo   		| \\src\test\\resources\\image\\download.jpg	| 0		 | test		| available		|
    When I search with pet by ID "<petId>"
    Then I upload a pet image "<image>" by "<petId>"
    Then I can update the Pet info with below data and Validate

      | petId | categoryId | categoryName	| petName		| photoUrls									    | tagsId | tagsName	| status		|
      | 10	  | 0		   | dog		    | Milo   		| \\src\test\\resources\\image\\download.jpg	| 0		 | test		| available   	|

    And I can delete the pet profile by id "<petId>"

    Examples:
      | petId		|image		                                |
      | 10   		|\\src\test\\resources\\TestData\\test.png	|


