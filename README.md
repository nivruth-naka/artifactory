# artifactory

```mvn spring-boot:run``` (this command can be executed to build and run the project)

The application provides two APIs-  
1. ```GET /items``` to retrieve the current inventory  
```
curl --location --request GET 'localhost:8080/items' --header 'Authorization: Basic bWl3LXVzZXI6Y2hhbmdlbWUxMjM=' | json_pp
```
```json
[{"name":"belt","description":"unisex","price":99},{"name":"hat","description":"men's","price":45},{"name":"hat","description":"women's","price":75},{"name":"perfume","description":"Guess","price":99},{"name":"perfume","description":"Calvin Klein","price":69},{"name":"perfume","description":"Coach","price":129},{"name":"shoes","description":"9M","price":175},{"name":"shoes","description":"10","price":199},{"name":"shoes","description":"8W","price":222}]
```

2. ```POST /items``` to let a user buy an item  
```
curl --location --request POST 'localhost:8080/items' --header 'Authorization: Basic bWl3LXVzZXI6Y2hhbmdlbWUxMjM=' --header 'Content-Type: application/json' --data-raw '{
    "name": "hat",
    "description": "women'\''s",
    "price": 75
}'
```
No response with Status: 200 OK

Tech stack-  
Java 11  
Spring Boot  
Lombok  
H2  
Karate  

Surge model-  
There is an in-memory cache that saves item viewed timestamps in a queue, mapped to item ID. The cache uses sliding window principle to evict timestamp entries greater than an hour. Whenever there are more than 10 entries in the cache, the item's price is updated and the cache is refreshed.

Authentication was implemented through Spring Security
