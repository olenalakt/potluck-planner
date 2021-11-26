#docker run -it --mount src="$(pwd)",target=/export,type=bind prabath/openssl

#sh /export/gen-key.sh

##### CERTIFICATE AUTHORITY #####

#Generates a private key for the certificate authority
openssl genrsa -aes256 -passout pass:"olena123" -out /export/ca/ca_key.pem 4096

#Generates a public key for the certificate authority
openssl  req -new -passin pass:"olena123" -key /export/ca/ca_key.pem -x509 -days 3650 -out /export/ca/ca_cert.pem -subj "/CN=ca.elakt.ca"

##### User registration microservice #####

#Generates a private key for the User registration microservice
openssl genrsa -aes256 -passout pass:"olena123" -out /export/user-service/app_key.pem 4096

#Generates certificate signing request (CSR) for the User registration microservice 
openssl req -passin pass:"olena123" -new -key /export/user-service/app_key.pem -out /export/user-service/csr-for-app -subj "/CN=users.elakt.ca"

#Generates User registration microservice’s certificate, which is signed by the CA	
openssl x509 -req -passin pass:"olena123" -days 3650 -in /export/user-service/csr-for-app -CA /export/ca/ca_cert.pem -CAkey /export/ca/ca_key.pem -set_serial 01 -out /export/user-service/app_cert.pem

#Creates a Java keystore with User registration microservice’s private/public keys
openssl rsa -passin pass:"olena123" -in /export/user-service/app_key.pem -out /export/user-service/app_key.pem

cat /export/user-service/app_key.pem /export/user-service/app_cert.pem >> /export/user-service/application_keys.pem

openssl pkcs12  -export -passout pass:"olena123" -in /export/user-service/application_keys.pem -out /export/user-service/app.p12

keytool -importkeystore -srcstorepass olena123 -srckeystore /export/user-service/app.p12 -srcstoretype pkcs12 -deststorepass olena123 -destkeystore /export/user-service/userservice.jks -deststoretype JKS

keytool -changealias -alias "1" -destalias "userservice" -keystore /export/user-service/userservice.jks -storepass olena123

keytool -import -file /export/ca/ca_cert.pem -alias ca -noprompt  -keystore /export/user-service/userservice.jks -storepass olena123

keytool -importkeystore -srckeystore /export/user-service/userservice.jks -destkeystore /export/user-service/userservice.jks -deststoretype pkcs12 -storepass olena12

#Cleans up
rm /export/user-service/app.p12 
rm /export/user-service/application_keys.pem 
rm /export/user-service/app_cert.pem 
rm /export/user-service/app_key.pem
rm /export/user-service/csr-for-app

##### Event microservice #####

#Generates a private key for the Event microservice
openssl genrsa -aes256 -passout pass:"olena123" -out /export/event-service/app_key.pem 4096

#Generates certificate signing request (CSR) for the Event microservice 
openssl req -passin pass:"olena123" -new -key /export/event-service/app_key.pem -out /export/event-service/csr-for-app -subj "/CN=events.elakt.ca"

#Generates Event microservice’s certificate, which is signed by the CA	
openssl x509 -req -passin pass:"olena123" -days 3650 -in /export/event-service/csr-for-app -CA /export/ca/ca_cert.pem -CAkey /export/ca/ca_key.pem -set_serial 01 -out /export/event-service/app_cert.pem

#Creates a Java keystore with Event microservice’s private/public keys
openssl rsa -passin pass:"olena123" -in /export/event-service/app_key.pem -out /export/event-service/app_key.pem

cat /export/event-service/app_key.pem /export/event-service/app_cert.pem >> /export/event-service/application_keys.pem

openssl pkcs12  -export -passout pass:"olena123" -in /export/event-service/application_keys.pem -out /export/event-service/app.p12

keytool -importkeystore -srcstorepass olena123 -srckeystore /export/event-service/app.p12 -srcstoretype pkcs12 -deststorepass olena123 -destkeystore /export/event-service/eventservice.jks -deststoretype JKS

keytool -changealias -alias "1" -destalias "eventservice" -keystore /export/event-service/eventservice.jks -storepass olena123

keytool -import -file /export/ca/ca_cert.pem -alias ca  -noprompt -keystore /export/event-service/eventservice.jks -storepass olena123

keytool -importkeystore -srckeystore /export/event-service/eventservice.jks -destkeystore /export/event-service/eventservice.jks -deststoretype pkcs12 -storepass olena123

#Cleans up
rm /export/event-service/app.p12 
rm /export/event-service/application_keys.pem 
rm /export/event-service/app_cert.pem 
rm /export/event-service/app_key.pem
rm /export/event-service/csr-for-app


##### Guest microservice #####

#Generates a private key for the Guest microservice
openssl genrsa -aes256 -passout pass:"olena123" -out /export/guest-service/app_key.pem 4096

#Generates certificate signing request (CSR) for the Guest microservice 
openssl req -passin pass:"olena123" -new -key /export/guest-service/app_key.pem -out /export/guest-service/csr-for-app -subj "/CN=guests.elakt.ca"

#Generates Guest microservice’s certificate, which is signed by the CA	
openssl x509 -req -passin pass:"olena123" -days 3650 -in /export/guest-service/csr-for-app -CA /export/ca/ca_cert.pem -CAkey /export/ca/ca_key.pem -set_serial 01 -out /export/guest-service/app_cert.pem

#Creates a Java keystore with Guest microservice’s private/public keys
openssl rsa -passin pass:"olena123" -in /export/guest-service/app_key.pem -out /export/guest-service/app_key.pem

cat /export/guest-service/app_key.pem /export/guest-service/app_cert.pem >> /export/guest-service/application_keys.pem

openssl pkcs12  -export -passout pass:"olena123" -in /export/guest-service/application_keys.pem -out /export/guest-service/app.p12

keytool -importkeystore -srcstorepass olena123 -srckeystore /export/guest-service/app.p12 -srcstoretype pkcs12 -deststorepass olena123 -destkeystore /export/guest-service/guestservice.jks -deststoretype JKS

keytool -changealias -alias "1" -destalias "guestservice" -keystore /export/guest-service/guestservice.jks -storepass olena123

keytool -import -file /export/ca/ca_cert.pem -alias ca  -noprompt -keystore /export/guest-service/guestservice.jks -storepass olena123

keytool -importkeystore -srckeystore /export/guest-service/guestservice.jks -destkeystore /export/guest-service/guestservice.jks -deststoretype pkcs12 -storepass olena123

#Cleans up
rm /export/guest-service/app.p12 
rm /export/guest-service/application_keys.pem 
rm /export/guest-service/app_cert.pem 
rm /export/guest-service/app_key.pem
rm /export/guest-service/csr-for-app

##### Dish microservice #####

#Generates a private key for the Dish microservice
openssl genrsa -aes256 -passout pass:"olena123" -out /export/dish-service/app_key.pem 4096

#Generates certificate signing request (CSR) for the Dish microservice 
openssl req -passin pass:"olena123" -new -key /export/dish-service/app_key.pem -out /export/dish-service/csr-for-app -subj "/CN=dishes.elakt.ca"

#Generates Dish microservice’s certificate, which is signed by the CA	
openssl x509 -req -passin pass:"olena123" -days 3650 -in /export/dish-service/csr-for-app -CA /export/ca/ca_cert.pem -CAkey /export/ca/ca_key.pem -set_serial 01 -out /export/dish-service/app_cert.pem

#Creates a Java keystore with Dish microservice’s private/public keys
openssl rsa -passin pass:"olena123" -in /export/dish-service/app_key.pem -out /export/dish-service/app_key.pem

cat /export/dish-service/app_key.pem /export/dish-service/app_cert.pem >> /export/dish-service/application_keys.pem

openssl pkcs12  -export -passout pass:"olena123" -in /export/dish-service/application_keys.pem -out /export/dish-service/app.p12

keytool -importkeystore -srcstorepass olena123 -srckeystore /export/dish-service/app.p12 -srcstoretype pkcs12 -deststorepass olena123 -destkeystore /export/dish-service/dishservice.jks -deststoretype JKS

keytool -changealias -alias "1" -destalias "dishservice" -keystore /export/dish-service/dishservice.jks -storepass olena123

keytool -import -file /export/ca/ca_cert.pem -alias ca  -noprompt -keystore /export/dish-service/dishservice.jks -storepass olena123

keytool -importkeystore -srckeystore /export/dish-service/dishservice.jks -destkeystore /export/dish-service/dishservice.jks -deststoretype pkcs12 -storepass olena123

#Cleans up
rm /export/dish-service/app.p12 
rm /export/dish-service/application_keys.pem 
rm /export/dish-service/app_cert.pem 
rm /export/dish-service/app_key.pem
rm /export/dish-service/csr-for-app

##### Drink microservice #####

#Generates a private key for the Drink microservice
openssl genrsa -aes256 -passout pass:"olena123" -out /export/drink-service/app_key.pem 4096

#Generates certificate signing request (CSR) for the Drink microservice 
openssl req -passin pass:"olena123" -new -key /export/drink-service/app_key.pem -out /export/drink-service/csr-for-app -subj "/CN=drinks.elakt.ca"

#Generates Drink microservice’s certificate, which is signed by the CA	
openssl x509 -req -passin pass:"olena123" -days 3650 -in /export/drink-service/csr-for-app -CA /export/ca/ca_cert.pem -CAkey /export/ca/ca_key.pem -set_serial 01 -out /export/drink-service/app_cert.pem

#Creates a Java keystore with Drink microservice’s private/public keys
openssl rsa -passin pass:"olena123" -in /export/drink-service/app_key.pem -out /export/drink-service/app_key.pem

cat /export/drink-service/app_key.pem /export/drink-service/app_cert.pem >> /export/drink-service/application_keys.pem

openssl pkcs12  -export -passout pass:"olena123" -in /export/drink-service/application_keys.pem -out /export/drink-service/app.p12

keytool -importkeystore -srcstorepass olena123 -srckeystore /export/drink-service/app.p12 -srcstoretype pkcs12 -deststorepass olena123 -destkeystore /export/drink-service/drinkservice.jks -deststoretype JKS

keytool -changealias -alias "1" -destalias "drinkservice" -keystore /export/drink-service/drinkservice.jks -storepass olena123

keytool -import -file /export/ca/ca_cert.pem -alias ca  -noprompt -keystore /export/drink-service/drinkservice.jks -storepass olena123

keytool -importkeystore -srckeystore /export/drink-service/drinkservice.jks -destkeystore /export/drink-service/drinkservice.jks -deststoretype pkcs12 -storepass olena123

#Cleans up
rm /export/drink-service/app.p12 
rm /export/drink-service/application_keys.pem 
rm /export/drink-service/app_cert.pem 
rm /export/drink-service/app_key.pem
rm /export/drink-service/csr-for-app


##### Oath-server microservice #####

#Generates a private key for the Oath-server microservice
openssl genrsa -aes256 -passout pass:"olena123" -out /export/oath-server/app_key.pem 4096

#Generates certificate signing request (CSR) for the Oath-server microservice 
openssl req -passin pass:"olena123" -new -key /export/oath-server/app_key.pem -out /export/oath-server/csr-for-app -subj "/CN=oath.elakt.ca"

#Generates Oath-server microservice’s certificate, which is signed by the CA	
openssl x509 -req -passin pass:"olena123" -days 3650 -in /export/oath-server/csr-for-app -CA /export/ca/ca_cert.pem -CAkey /export/ca/ca_key.pem -set_serial 01 -out /export/oath-server/app_cert.pem

#Creates a Java keystore with Oath-server microservice’s private/public keys
openssl rsa -passin pass:"olena123" -in /export/oath-server/app_key.pem -out /export/oath-server/app_key.pem

cat /export/oath-server/app_key.pem /export/oath-server/app_cert.pem >> /export/oath-server/application_keys.pem

openssl pkcs12  -export -passout pass:"olena123" -in /export/oath-server/application_keys.pem -out /export/oath-server/app.p12

keytool -importkeystore -srcstorepass olena123 -srckeystore /export/oath-server/app.p12 -srcstoretype pkcs12 -deststorepass olena123 -destkeystore /export/oath-server/oathserver.jks -deststoretype JKS

keytool -changealias -alias "1" -destalias "oathserver" -keystore /export/oath-server/oathserver.jks -storepass olena123

keytool -import -file /export/ca/ca_cert.pem -alias ca  -noprompt -keystore /export/oath-server/oathserver.jks -storepass olena123

keytool -importkeystore -srckeystore /export/oath-server/oathserver.jks -destkeystore /export/oath-server/oathserver.jks -deststoretype pkcs12 -storepass olena123

#Cleans up
rm /export/oath-server/app.p12 
rm /export/oath-server/application_keys.pem 
rm /export/oath-server/app_cert.pem 
rm /export/oath-server/app_key.pem
rm /export/oath-server/csr-for-app


##### Gateway microservice #####

#Generates a private key for the Gateway microservice
openssl genrsa -aes256 -passout pass:"olena123" -out /export/gateway-service/app_key.pem 4096

#Generates certificate signing request (CSR) for the Gateway microservice 
openssl req -passin pass:"olena123" -new -key /export/gateway-service/app_key.pem -out /export/gateway-service/csr-for-app -subj "/CN=planner.elakt.ca"

#Generates Inventory microservice’s certificate, which is signed by the CA	
openssl x509 -req -passin pass:"olena123" -days 3650 -in /export/gateway-service/csr-for-app -CA /export/ca/ca_cert.pem -CAkey /export/ca/ca_key.pem -set_serial 01 -out /export/gateway-service/app_cert.pem

#Creates a Java keystore with Inventory microservice’s private/public keys
openssl rsa -passin pass:"olena123" -in /export/gateway-service/app_key.pem -out /export/gateway-service/app_key.pem

cat /export/gateway-service/app_key.pem /export/gateway-service/app_cert.pem >> /export/gateway-service/application_keys.pem

openssl pkcs12  -export -passout pass:"olena123" -in /export/gateway-service/application_keys.pem -out /export/gateway-service/app.p12

keytool -importkeystore -srcstorepass olena123 -srckeystore /export/gateway-service/app.p12 -srcstoretype pkcs12 -deststorepass olena123 -destkeystore /export/gateway-service/gatewayservice.jks -deststoretype JKS

keytool -changealias -alias "1" -destalias "gatewayservice" -keystore /export/gateway-service/gatewayservice.jks -storepass olena123

keytool -import -file /export/ca/ca_cert.pem -alias ca  -noprompt -keystore /export/gateway-service/gatewayservice.jks -storepass olena123

keytool -importkeystore -srckeystore /export/gateway-service/gatewayservice.jks -destkeystore /export/gateway-service/gatewayservice.jks -deststoretype pkcs12 -storepass olena123

#Cleans up
rm /export/gateway-service/app.p12 
rm /export/gateway-service/application_keys.pem 
rm /export/gateway-service/app_cert.pem 
rm /export/gateway-service/app_key.pem
rm /export/gateway-service/csr-for-app
