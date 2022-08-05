# eStore
Simple eStore that allows you to search,checkout, and manage items that are sold by an arbitrary store. There is also a warehouse that keeps track of the 
different items that are provided to the store. The warehouse inventory is updated dynamically with each purchase of a customer or manager modifications to 
the stock. The data held in the store and warehouse are stored with a SQL database. Java is used to drive the actions of a customer or manager that purchases
items or modifies the stock levels and item descriptions of the store or warehouse


# Setup:

Prereq: have openjdk-11-jdk to work with the ojdbc11.jar

create a folder to keep ojdbc11.jar

then do pwd in the folder to get the full path to ojdbc11.jar

If there is a name with spaces used on one of the folders leading to 
ojdbc11.jar, put the whole path in double quotes. 

example : 
export CLASSPATH="/student/first last/OracleTest/ojdbc11.jar":$CLASSPATH

to compile the java files do the following:
javac -classpath ojdbc11.jar src/*.java

then execute OracleCon by entering the src folder and doing:
ex: cd src
java OracleCon


# TODO:

show how to create local sql database and connect to it
