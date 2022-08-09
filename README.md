# eStore
Emart: User is able to look at items that eStore has in stock. Can use various search parameters to look for items

Edepot: Used to fulfill orders and replenish orders once an item drops below the stock threshold. A manager is also able to: order,modify,and delete items. They can also modify a particular customer and their attributes. 

UserInterface: driver for the functions defined in Emart and Edepot

Connect: used to connect to the sqlite database.



# Setup:

Ubuntu/Linux: 

## Pre-reqs:

install sqlite3: sudo apt-get install sqlite3

install java: sudo apt install openjdk-11-jdk-headless 

## Database:

navigate to eStore/sqlite/db

then on the command line do: sqlite3 chinook.db
(change chinook to whatever you want just make sure to follow up with the necessary changes throughout the files)

if you need a different version put it in "/eStore/sqlite/java/connect/net/sqlitetutorial" with the java files. 

download sqlite-jdbc-driver: https://www.sqlitetutorial.net/sqlite-java/sqlite-jdbc-driver/

## Compiling:

Then do javac *.java to compile within the same folder.

After the java files are compiled do the following command inside the same folder

## Running program:

command: java -classpath ".:sqlite-jdbc-3.36.0.3.jar" Connect 




