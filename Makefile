google-formater = find . -type f -name '*.java' | xargs java -jar libs/formatter.jar --aosp

format:
	${google-formater} -i

lint:
	${google-formater} --set-exit-if-changed --dry-run

build:
	./gradlew :jar
	cp build/libs/mas-restaurant-v0.0.1.jar restaurant.jar

run:
	java -jar restaurant.jar

update:
	make -B build
	make run
