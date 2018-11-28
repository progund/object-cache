VERSION=0.1

#
# 
#
OC_SRC= \
  se/juneday/ObjectCacheReader.java \
  se/juneday/ObjectCache.java    

ANDROID_OC_SRC= \
   se/juneday/android/AndroidObjectCacheHelper.java 

OC_TEST_SRC= \
  test/se/juneday/test/ObjectCacheReadSingleTest.java \
  test/se/juneday/test/ObjectCacheReadManyTest.java \
  test/se/juneday/test/ObjectCacheStoreSingleTest.java \
  test/se/juneday/test/ObjectCacheStoreManyTest.java \
  test/se/juneday/test/OCReaderSingleTest.java \
  test/se/juneday/test/OCReaderManyTest.java \
  test/se/juneday/test/AndroidObjectCacheHelperTest.java



ANDROID_STUBS_VERSION=01
ANDROID_STUBS_JAR_FILE=android-stubs-$(ANDROID_STUBS_VERSION).jar
ANDROID_STUBS_JAR=libs/$(ANDROID_STUBS_JAR_FILE)
ANDROID_STUBS_JAR_URL=https://github.com/progund/android-stubs/releases/download/$(ANDROID_STUBS_VERSION)/$(ANDROID_STUBS_JAR_FILE)

FILE_SEP=:
ifeq ($(OS),Windows_NT)
# who took the decision to use ";" to separate directories in Windows?
# Shame on you!
FILE_SEP=;
endif

RELEASE_DIR=release/$(VERSION)

# Add android-stubs to CLASSPATH (even if not used)
CLASSPATH=.$(FILE_SEP)test$(FILE_SEP)bin$(FILE_SEP)$(ANDROID_STUBS_JAR)

OC_CLASSES=$(OC_SRC:%.java=%.class)
ANDROID_OC_CLASSES=$(ANDROID_OC_SRC:%.java=%.class)
OC_TEST_CLASSES=$(OC_TEST_SRC:%.java=%.class)

JAR_FILE=object-cache-$(VERSION).jar
ANDROID_JAR_FILE=object-cache-android-$(VERSION).jar

#
# Rules
#
%.class: %.java
	javac -cp $(CLASSPATH) $<

%.pdf: %.md
	pandoc $< -o $@


$(JAR_FILE): $(ANDROID_STUBS_JAR) $(OC_CLASSES) $(ANDROID_OC_CLASSES) 
	@echo "Creating jar file"
	jar cvf $(JAR_FILE) `find se -name "*.class"` README.md
	@echo "Created jar file: object-cache-$(VERSION).jar"
	@zipinfo object-cache-$(VERSION).jar

jar: test $(JAR_FILE)

$(DEST_DIR):
	mkdir -p $(DEST_DIR) 

$(RELEASE_DIR):
	echo "Making dir: $(RELEASE_DIR) "
	mkdir -p $(RELEASE_DIR) 

test: $(ANDROID_STUBS_JAR) $(OC_TEST_CLASSES)
	@echo "  ********** Testing many **********"
	@echo "  --== Test store (many) ==--"
	@java -cp $(CLASSPATH) se.juneday.test.ObjectCacheStoreManyTest --store
	@echo "  --== Test ObjectCache Reader (many) ==--"
	@java -cp $(CLASSPATH) se.juneday.ObjectCacheReader User
	@echo "  --== Test read (many) ==--"
	@java -cp $(CLASSPATH) se.juneday.test.ObjectCacheReadManyTest 
	@echo "  --== Test oc reader (many) ==--"
	@java -cp $(CLASSPATH) se.juneday.test.OCReaderManyTest

	@echo "  ********** Testing single **********"
	@echo "  --== Test store (single) ==--"
	@java -cp $(CLASSPATH) se.juneday.test.ObjectCacheStoreSingleTest --store
	@echo "  --== Test ObjectCache Reader (single) ==--"
	@java -cp $(CLASSPATH) se.juneday.ObjectCacheReader User
	@echo "  --== Test read (single) ==--"
	@java -cp $(CLASSPATH) se.juneday.test.ObjectCacheReadSingleTest 
	@echo "  --== Test oc reader (many) ==--"
	@java -cp $(CLASSPATH) se.juneday.test.OCReaderSingleTest

	@echo "  ********** Misc **********"
	@echo "  --== Test Android helper ==--"
	@java -cp $(CLASSPATH) se.juneday.test.AndroidObjectCacheHelperTest 

download-dependencies: $(ANDROID_STUBS_JAR) 

clean:
	-rm $(OC_CLASSES) 
	-find . -name "*.class" | xargs rm
	-find . -name "*~" | xargs rm
	-find . -name "*.data" | xargs rm
	-rm -fr doc 
	-rm -fr libs
	-rm README.pdf
	@echo "all cleaned up"

really-clean: clean
	-rm -fr doc bin libs release
	@echo "all cleaned up"

release: $(ANDROID_JAR_FILE) $(JAR_FILE) $(RELEASE_DIR) doc/index.html
	mv $(JAR_FILE) $(ANDROID_JAR_FILE) doc/ $(RELEASE_DIR)/ 


doc/index.html: 
	javadoc -d doc -link "https://docs.oracle.com/javase/8/docs/api/" se.juneday

doc: README.pdf doc/index.html

$(ANDROID_STUBS_JAR):
	mkdir -p libs
	curl -o $(ANDROID_STUBS_JAR) -LJ $(ANDROID_STUBS_JAR_URL) 


.PHONY: libs
.PHONY: test
.PHONY: release
