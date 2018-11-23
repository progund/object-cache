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



FILE_SEP=:
ifeq ($(OS),Windows_NT)
# who took the decision to use ";" to separate directories in Windows?
# Shame on you!
FILE_SEP=;
endif


# Add android-stubs to CLASSPATH (even if not used)
CLASSPATH=.$(FILE_SEP)test$(FILE_SEP)bin$(FILE_SEP)libs/android-stubs-master/

OC_CLASSES=$(OC_SRC:%.java=%.class)
ANDROID_OC_CLASSES=$(ANDROID_OC_SRC:%.java=%.class)
OC_TEST_CLASSES=$(OC_TEST_SRC:%.java=%.class)

RELEASE_DIR=release


#
# Rules
#
%.class: %.java
	javac -cp $(CLASSPATH) $<

%.pdf: %.md
	pandoc $< -o $@

jar: $(OC_CLASSES)
	@echo "Creating jar file"
	jar cvf object-cache-$(VERSION).jar $(OC_CLASSES)
	@echo "Created jar file: object-cache-$(VERSION).jar"

android-jar: $(OC_CLASSES) $(ANDROID_OC_CLASSES) 
	@echo "Creating jar file"
	jar cvf object-cache-android-$(VERSION).jar $(OC_CLASSES) $(ANDROID_OC_CLASSES) 
	@echo "Created jar file: object-cache-android-$(VERSION).jar"

$(DEST_DIR):
	mkdir -p $(DEST_DIR) 

$(RELEASE_DIR):
	mkdir -p $(RELEASE_DIR) 

test: $(OC_TEST_CLASSES)
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

download-dependencies:
	@-if [ ! -f libs/android-stubs-master/android/content/Context.java ]; then echo "Downloading android-stubs"; mkdir -p libs ; cd libs && curl -LJ -o android-stubs.zip https://github.com/progund/android-stubs/archive/master.zip  && unzip android-stubs.zip; else echo android-stubs already downloaded; fi

clean:
	-rm $(OC_CLASSES) 
	-find . -name "*.class" | xargs rm
	-find . -name "*~" | xargs rm
	-find . -name "*.data" | xargs rm
	-rm -fr doc 
	-rm README.pdf
	@echo "all cleaned up"

really-clean: clean
	-rm -fr doc bin libs release
	@echo "all cleaned up"

doc: README.pdf
    javadoc -d doc -link "https://docs.oracle.com/javase/8/docs/api/" se.juneday


.PHONY: libs
.PHONY: test

