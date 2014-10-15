allTests=`find ./src/test/java/com/ -name Test* | grep -v .svn | while read LINE; do echo $LINE | sed ' s/^.*\/Test/Test/ ' | sed ' s/.java// ' ; done`
if [ $# -eq 0 ];
then
    clear
    echo "PLEASE give the name of the test as PARAM"
    echo "============================================================================"
    echo "available tests are: "$allTests
    echo ""
    echo ""
    echo "for e.g. ./test TestWmosToFFMWInterfaces"
    exit 127
fi
testName=$1

svn co https://nessvn1.nespresso.com/svn/globemanufacturing/NesOATestSuite/ .
mvn -o clean -Dtest=$testName test

