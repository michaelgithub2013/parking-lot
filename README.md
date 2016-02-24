# parking-lot
It demonstrates object oriented design, multithreading, testablility, extensibility, mantianablility. 

The required parking lot has a fixed number of parking spaces. The total vehicles in the parking lot should not be greater than the number. 

The parking lot has a fixed entries and exits. Each entry and exit can be set to open and closed. Vehicles are allowed to pass the open gates concurrently. 

When a vehicle entered the parking lot, it got a parking token with time stamp. 
The token can be used to get an unpaid parking bill. In this case, we assume each token is 10 dollars. 
A parking bill can be paid by giving a collection of payable. A payable could be any currency of coin, check and paper money, etc., as long as they can be exchanged to dollar.  In this case, currency exchange is not implemented.  
A vehicle should provide a paid parking bill before going out of the parking lot.

