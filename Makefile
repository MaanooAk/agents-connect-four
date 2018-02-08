
.phony: main
main:
	mvn -Pjade-main exec:java
	
.phony: agents
agents:	
	mvn -Pjade-agent exec:java

.phony: clean
clean:
	mvn clean
	mvn package
