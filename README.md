# Bank-App-Part-I

A minimalistic banking system with features such as:

* Split transaction  
* Online Payment  
* Transfer  
* Savings account  

## Design Patterns
1) DAO (https://www.geeksforgeeks.org/data-access-object-pattern/)  

- Used DAO for managing a "database" for accounts and users.

2) Command Design Pattern
 - Implemented the Command design pattern to 
handle and execute each user command based on input.
 
3) Factory Design Pattern
- Used the Command design pattern in the getCommand method, 
which takes an enum of commands and
constructs the appropriate command using a switch statement.

4) Strategy Design Pattern
- Implemented the Strategy Design Pattern for creating each type of cashback.

## Project Structure
- The project is structured into 8 packages:
1) bank:
    - Contains the Account class and its subclasses (ClassicAccount
   and SavingsAccount).
    - Contains the Transaction class and its subclasses, 
   which represent every type of transaction.  
      (AccountCreation, CardCreation, CardDestruction, CardPayment,  
   DeleteError, FrozenPayment, InsufficientFunds, InterestChanged,  
   SendReceive, SplitPaymentTransaction)
    - Contains the User class.
    - Contains the Dao interface, implemented by DaoImpl class.
    - Contains DaoObject, implemented by Account and User classes.
    - Contains Card class and its subclass OneTimeCard.
2) graph:
    - Contains Node class, which represents a node in the currency graph.
    - Contains CurrencyGraph class, implemented to handle currency exchanges
3) command:
    - Contains Client class, implemented for Command Design Pattern.
    - Contains Inovker class of a Command Design Pattern.
    - Contains CommandType enum that lists  
   all the commands of the banking system.
    - Contains Command interface, with a specific class for each command.  
      (AddAccount, AddFunds, AddInterest, ChangeInterestRate,  
   CheckCardStatus, CreateCard, CreateOneTimeCard, DeleteAccount, 
   DeleteCard, PayOnline, PrintTransactions, PrintUsers,  
   Report, SendMoney, SetAlias, SetMinimumBalance,  
   SpendingsReport, SplitPayment)
4) start:
   - Contains a utility class, with a method that starts the application.
5) main:
    - Contains the main method
6) utils:
    - Contains a utility class that generates IBANs and card numbers.
7) fileio:
    - Contains input classes for handling commands.
8) checker:
    - Contains checker's classes.

9) commerciants:
	- Contains Cashback Strategy classes.
	- Contains Merchant class.
## Commands

##### 1. printUsers - Prints a list of all users in the system, along with their details.
##### 2. printTransactions - Prints all transactions of a user.
##### 3. addAccount - adds a new account to a user.
##### 4. addFunds - adds funds to an account.
##### 5. createCard - creates a new card for a user's account.
##### 6. createOneTimeCard - generates a new one-time card for a user's account.
##### 7. deleteCard - deletes a card from an account.
##### 8. deleteAccount - removes an account of the selected user.
##### 9. payOnline - pays an amount to a merchant using a card.
##### 10. setMinimumBalance - sets a minimum balance to an account.
##### 11. checkCardStatus - checks the balance of an account and sets the status of the selected card.
##### 12. sendMoney - transfers an amount of money from a sender account to a receiver account.
##### 13. setAlias - sets a global alias to an account.
##### 14. splitPayment - splits a payment into multiple accounts.
##### 15. addInterest - collects interest for a savings account.
##### 16. changeInterestRate - changes the interest rate for a savings account.
##### 17. report - prints all transactions of an *account.
##### 18. spendingsReport - prints all card payments of a classic account.
##### 19. upgradePlan - changes a user's current account plan to a higher-tier subscription. 

"*" - Prints all transactions for a classic account, and only interest rate transactions for a savings account.
## Disclaimer
	- This is not the final version of the banking system, so many tests may not pass.

