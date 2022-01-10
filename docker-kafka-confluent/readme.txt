kafka-docker 

Download and install
https://hub.docker.com/editions/community/docker-ce-desktop-windows

Configure(add) memory -  I didn't do  that 

Register account  @ hub.docker.com -  create dockerId
369118
Olena(4)

Sign-in into  desktop  with  dockerId

Launch cmd and go to  the place with 
C:\GIT\kafka\avroKafkaProducer\kafka-docker>
docker-compose.yml

Issue cmd to start:
docker-compose up

docker-compose down

wait until  everything downloaded and configured


http://localhost:8081/subjects


to check the ports blocked by Windows to  address
ERROR: attempt was made to access a socket in a way forbidden by its access permissions

ERROR: Error response from daemon: Ports are not available: listen tcp 0.0.0.0:2181: bind: An attempt was made to access a socket in a way forbidden by its access permissions
Run

netsh interface ipv4 show excludedportrange protocol=tcp

to  check ports


Need to enable Hyper-V  on windows 
DISM /Online /Enable-Feature /All /FeatureName:Microsoft-Hyper-V
see - https://windowsreport.com/hyper-v-and-containers-not-enabled/



