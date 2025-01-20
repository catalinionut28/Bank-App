# J. POO Morgan - Etapa 2


## Noi modificari:
* Am adaugat urmatoarele pachete:
****
* plan: unde se afla toate tipurile de service plan-uri
* visitor: visitor pattern pentru split payment
* commerciants: care contine cashback strategy-urile si clasa Merchant


****
* Am modificat urmatoarele:
****
* Am adaugat clase noi pentru tranzactii, cum ar fi: InterestRateIncome, SavingsWithdrawn,
CashTransaction etc.
* Am adaugat o clasa noua pentru split payment: MultiplePayment, care va fi folosita in PendingPayment
* Am adaugat clase pentru noile comenzi implementate
* Am modificat clasa StartApp pentru intr-o clasa care foloseste DP-ul Singleton
* Am modificat in Utils, adaugand o clasa noua care calculeaza varsta unui utilizator,
primind o data.
* La recomandarea corectorului etapei 1, am scos toate clasele care erau in fisierul command.Command
* Am modifcat metode precum payOnline si sendMoney din clasa abstracta Account
* Am modificat clasa User, care are mai multe functionalitati si campuri, precum: age si occupation, dar
si metode precum upgradePlan, care upgradeaza fiecarui cont planul asociat.
* Am modificat clasa SplitPayment din command, care este total diferita, si se foloseste de
DP-ul Visitor implementat.

****

Proiectul nu a fost in totalitate terminat, fiind nevoit sa implementez si
Business Account, si alte erori si feature-uri.

## Design Patterns

Pe langa design pattern-urile folosite la etapa anterioara (DAO, Command),
am implementat urmatoarele design pattern-uri:

#### Strategy:
 * folosit pentru strategia de cashback pe care a trebuit sa o implementez
 * se imparte in alte doua tipuri de strategii: NrOfTransactions si SpendingThreshold
 * fiecare tip de cashback are o clasa asociata 
 * contine si CashbackContext care primeste ca parametru un cashbackStrategy,
si il calculeaza.

#### Visitor:
* folosit pentru comanda split payment.
* visitorul viziteaza un utilizator, acesta fiind nevoit sa accepte sau sa refuze
primul payment din coada.
* contine interfata UserDecision, pe care o implementeaza clasa User, unde acesta accepta sau refuza
visitorul (prima plata).
* contine interfata PaymentVisitor, care defineste metoda visit(), precizata anterior.


#### Singleton:
* folosit pentru pornirea "aplicatiei".
* mi s-a parut mult mai elegant sa folosesc singleton decat o metoda statica.

