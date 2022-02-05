###### Currency converter

This API converts one currency values to another. It uses external webservice to get rates (https://exchangeratesapi.io/)

###### Solution

The solution checks that input currency codes are in ISO 4217 format. I decided to omit separate call to the API just for the
currency validation, instead, if we will get the error from the external API it error message will be displayed.

Another possible option to get a valid currency codes for this API is to parse them once and save into DB, 
but in this case it will introduce more stateful data into the app (which is not a bad thing though)

###### How to run

To run the project, please run `mvnw install`. Then, in the directory with the created `CurrencyConversionAPI-0.0.1-SNAPSHOT.jar` run `java -jar c:CurrencyConversionAPI-0.0.1-SNAPSHOT.jar` to launch the web service.

To create a docker image please run `docker build -t java -jar c:CurrencyConversionAPI-0.0.1-SNAPSHOT.jar` when in the project directory. Run `docker run -p 8080:8080 java -jar c:CurrencyConversionAPI-0.0.1-SNAPSHOT.jar` to launch container from the image.

###### How to use

Open API UI is available here -> http://localhost:8080/swagger-ui/index.html

Example of CURL request:

`curl -X 'GET' \
'http://localhost:8080/convert?from=EUR&to=UAH&quantity=10' \
-H 'accept: application/json'`

###### Exchangerates API

This solution uses Exchangerates API, the token in the application.properties uses basic subscription, which allows only EUR as a base currency.
If you purchased higher subscription level, please put your token into the properties and set `exchangerates.api.free-subscription` to false.


###### AWS

App is up and running:
http://16.170.253.13:8080/convert?from=EUR&to=UAH&quantity=123
Swagger: http://16.170.253.13:8080/swagger-ui/index.html