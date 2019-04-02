REST API
=========

##Данные о еде

Пример получения данных одной еды:

`curl -X GET \
  http://localhost:8080/topjava/rest/meals/100002`

Пример получения данных всей еды пользователя:

`curl -X GET \
  http://localhost:8080/topjava/rest/meals`

Пример получения данных всей еды пользователя за один день:

`curl -X GET \
  'http://localhost:8080/topjava/rest/meals/filter?startDate=2015-05-30&endDate=2015-05-30'`

Пример получения данных всей еды пользователя за период:

`curl -X GET \
  'http://localhost:8080/topjava/rest/meals/filter?startDate=2015-05-30&startTime=13:00:00&endDate=2015-05-30&endTime=23:59:59.9999'`

Пример создания еды:

`curl -X POST \
  http://localhost:8080/topjava/rest/meals \
  -H 'Content-Type: application/json' \
  -d '{
"dateTime": "2019-04-01T20:01:00",
"description": "created",
"calories":1100
}'`

Пример обновления данных еды:

`curl -X PUT \
  http://localhost:8080/topjava/rest/meals/100004 \
  -H 'Content-Type: application/json' \
  -d '{
"dateTime": "2015-05-30T20:01:00",
"description": "updated description",
"calories":1500
}'`

Пример удаления еды:

`curl -X DELETE \
  http://localhost:8080/topjava/rest/meals/100003`