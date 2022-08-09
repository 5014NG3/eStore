# eStore
Emart: User is able to look at items that eStore has in stock. Can use various search parameters to look for items

Edepot: Used to fulfill orders and replenish orders once an item drops below the stock threshold. A manager is also able to: order,modify,and delete items. They can also modify a particular customer and their attributes. 

UserInterface: driver for the functions defined in Emart and Edepot

Connect: used to connect to the sqlite database.



# Setup:

Ubuntu/Linux: 

install sqlite3: sudo apt-get install sqlite3
install java: sudo apt install openjdk-11-jdk-headless 

navigate to eStore/sqlite/db

then on the command line do: sqlite3 chinook.db
(change chinook to whatever you want just make sure to follow up with the necessary changes throughout the files)

download sqlite-jdbc-driver: https://www.sqlitetutorial.net/sqlite-java/sqlite-jdbc-driver/

if you need a different version put in "/eStore/sqlite/java/connect/net/sqlitetutorial" with the java files. Then do javac *.java to compile

After the java files are compiled do: 

java -classpath ".:sqlite-jdbc-3.36.0.3.jar" Connect 


