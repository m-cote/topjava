REST
=========

##Meals

Getting a meal:

`curl -X GET \
  http://localhost:8080/topjava/rest/meals/100002`

Getting all user meals:

`curl -X GET \
  http://localhost:8080/topjava/rest/meals`

Getting all user meals filtered by date:

`curl -X GET \
  'http://localhost:8080/topjava/rest/meals/filter?startDate=2015-05-30&endDate=2015-05-30'`

Getting all user meals filtered by date and time:

`curl -X GET \
  'http://localhost:8080/topjava/rest/meals/filter?startDate=2015-05-30&startTime=13:00:00&endDate=2015-05-30&endTime=23:59:59.9999'`

Creating a meal:

`curl -X POST \
  http://localhost:8080/topjava/rest/meals \
  -H 'Content-Type: application/json' \
  -d '{
"dateTime": "2019-04-01T20:01:00",
"description": "created",
"calories":1100
}'`

Updating a meal:

`curl -X PUT \
  http://localhost:8080/topjava/rest/meals/100004 \
  -H 'Content-Type: application/json' \
  -d '{
"dateTime": "2015-05-30T20:01:00",
"description": "updated description",
"calories":1500
}'`

Deleting a meal:

`curl -X DELETE \
  http://localhost:8080/topjava/rest/meals/100003`

##Users

Getting user data:

`curl -X GET \
  http://localhost:8080/topjava/rest/admin/users/100000`

Getting all users data:

`curl -X GET \
  http://localhost:8080/topjava/rest/admin/users`

Creating new user:

`curl -X POST \
  http://localhost:8080/topjava/rest/admin/users \
  -H 'Content-Type: application/json' \
  -d '{"name": "New2",
 "email": "new2@yandex.ru",
 "password": "passwordNew",
 "roles": ["ROLE_USER"]
}'`

Updating a user:

`curl -X PUT \
  http://localhost:8080/topjava/rest/admin/users/100000 \
  -H 'Content-Type: application/json' \
  -d '{"name": "UserUpdated",
 "email": "user@yandex.ru",
 "password": "passwordNew",
 "roles": ["ROLE_USER"]
}'`