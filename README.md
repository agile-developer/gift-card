##### Description:
This program selects items to buy, by parsing a file consisting of two columns, separated by a comma. 
The first column is the name of the item and the second column is the price of the item in cents. 
The items are **sorted** by price. The program takes as input 3 command-line arguments:
- Location of a file containing items sorted by price
- Amount to spend, in cents
- Number of gifts to buy (must be 2 or more)

The output of the program is a selection of items to buy whose sum is minimally under or equal to the amount to spend. 
If the specified number of items cannot be bought with the amount provided, the program's output is `Not possible`.

##### Instructions:
* This project can be run with Java SE 8 or upwards, and Gradle 4.8 or upwards.
* Run `./gradlew clean shadowJar` to build the project. 
This will create an executable `gift-card.jar` file in the `build/libs` directory.
* To run the application issue the following command:  
`java -jar build/libs/gift-card.jar </path/to/items/file> <amount to spend> <number of gifts to buy>`  
* The file `src/main/resources/Items.txt` can be used to test the application as follows:  
`java -jar build/libs/gift-card.jar src/main/resources/Items.txt 2500 2`  
`java -jar build/libs/gift-card.jar src/main/resources/Items.txt 2300 2`  
`java -jar build/libs/gift-card.jar src/main/resources/Items.txt 10000 2`  
`java -jar build/libs/gift-card.jar src/main/resources/Items.txt 1100 2`  
`java -jar build/libs/gift-card.jar src/main/resources/Items.txt 2500 3`  
`java -jar build/libs/gift-card.jar src/main/resources/Items.txt 2300 3`  
`java -jar build/libs/gift-card.jar src/main/resources/Items.txt 10000 3`  
`java -jar build/libs/gift-card.jar src/main/resources/Items.txt 10000 4`  
`java -jar build/libs/gift-card.jar src/main/resources/Items.txt 10000 5`  
