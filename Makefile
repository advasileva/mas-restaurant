google-formater = find . -type f -name '*.java' | xargs java -jar libs/formatter.jar

format:
	${google-formater} -i

lint:
	${google-formater} --set-exit-if-changed --dry-run
