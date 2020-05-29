# Fabric interface simulation 

This demo files are meant to be used in order to test a retailer API to our way of communication,
We have here 2 utilities, one is to receive and display incoming messages, and the other is for sending messages back 


### Usage
#### Monitoring incoming messages 
```bash
ACCESS_ID=<aws access id> SECRET_KEY=<aws secret key id> ./gradlew monitorIncomingMessages
```
After you run it, every received message will be printed to the console 

#### Send a message back
```bash
ACCESS_ID=<aws access id> SECRET_KEY=<aws secret key id> ./gradlew sendMessage <file path>
```
