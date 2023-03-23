format:
	java -jar libs/formatter.jar -i src/main/java/org/hse/bse/*.java

lint:
	java -jar libs/formatter.jar --set-exit-if-changed src/main/java/org/hse/bse/*.java
