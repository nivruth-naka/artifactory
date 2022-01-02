Feature: Testing Artifactory REST APIs with Karate

  Scenario: Testing get items
    Given url 'http://localhost:8097/items'
    When method GET
    Then status 200

  Scenario: Testing buy item
    Given url 'http://localhost:8097/items'
    And request { name: 'wallet' , description: 'trifold' , price: 44 }
    When method POST
    Then status 200
