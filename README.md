# membership-system-project
Bows Formula One High Performance Cars.


To run the memebership project you will need an instance of mongo running.

I recommend the use of Postman as an interface to make requests to the database.

You will also need to run this project locally on your machine.

To run the end points use http://localhost:portNumber 
The port number will be visible when you run this project locally.

POST      /registerEmployee                 This is what you use to register an employee
GET       /presentCard/:card                This is what you use to present a card.
POST      /addBalance/:card/:amountToAdd    This is how to add to your card balance.