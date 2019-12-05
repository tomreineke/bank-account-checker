# General

This program uses an HTML input from the Commerzbank financial transaction overview to create a typescript file that contains all the transactions in Json format. This file can then be processed by the related project ..., which will group the transactions by its source, so that the user sees how much money in total he received from / sent to a client.

# Usage

* Login to your Commerzbank account with Chrome or Firefox and list your financial transactions for a certain time interval. 
* Scroll down to the very bottom of the page to make sure all the transactions for the relevant interval are loaded. Then go up again and right-click on the very first entry as shown in the following picture:
<img src="C:\Users\Tom Reineke\Pictures\2017-06-03\002.jpg" style="zoom:50%;" />
  The click on "Inspect".
* In the developer tools section, which will then open, click on the enclosing table for this element, then right-click again and select "copy element". 
* Open the input.xml file in this project and replace the sample content by pasting the HTML from the clipboard. Save input.xml and run the main method in XMLTransformer.kt. 
* This will update the transactions.ts file, which you will need in the project "bank-account" to evaluate / aggregate the transactions. 

