So to understand eUTxO's first I had to first understand Account and uTxO models.

# The Account Model
The Account based model is probably the most simplest to understand. You can consider it in a way to resemble accounts for customer in a bank. So imagine Bob goes into a bank and deposits $100 into his account. The current balance of the account would be $100. Now if he transfer $20 to Alice, then the balance in his account is $80. So by just substracting the spent amount we can keep track of the balance of the account. As such it becomes easier to design smart contracts since it is computationally easier to maintain the balances. However, when running on scale with several parallel transactions it becomes much more difficult to maintain the state of the account leading to more transaction failures. Ethereum is an example of an account based model.

 **Advantages**
 - Easier to understand
 - Facilitates more expressive smart contracts

 **Disadvantages**
 - Difficult to scale and run parallel transactions

# The UTxO Model
As compared to the Account Model the UTxO model is a verification model. It does not compute the balance but instead uses inputs and outputs of a transaction. UTxO stands for "Unspent Transaction Output". So to understand this better lets consider again the transaction where Bob transfers $20 to Alice. Bob's initial balance in his account is $100. This would be consider as an input to the transaction. Once the transaction takes place it would generate two outputs. The first output is $20 which is allocated to Alice's account. The second output is $80 which allocated back to Bob. So after the transaction is completed we can say that the input of $100 is spent to produce unspent outputs of $20 and $80. Any following transactions can further use these unspent outputs as an input. Once an output is spent it can no longer be used.

This may seem a bit confusing at first but by only generating upsent outputs from transactions we can maintain the balance of an account. Also to run any transactions no particular state needs to be maintained. 

 **Advantages**
 - Stateless: Transactions depend only on the unspent outputs as inputs
 - Due to this several transactions can run in parallel which makes it more scalable
 - It becomes easier to determine the cost of running a transaction
 
 **Disadvantages**
 - There is definitely an upfront cost for determining which unspent outputs can be used 
 - I would also assume that since we end up creating unspent outputs for every transaction there will be more memory consumption
 - The stateless nature of the model here makes it much more difficult to create smart contracts

# The eUTxO Model
The eUTxO Model extends the UTxO model. It takes the benefits of the UTxO model and tries to solve the problem of creating smart contract more easily. To do this it associates a logical condition to an unspent output which decides when it can be used as input to a transaction. Say for example we can have a condition on Bob's account that ensures that he can transfer money to Alice only if the account maintains a minimum balance.

 **Advantages**
 - Gives all the benefits of that the UTxO model provides
 - Makes it a bit easier to create smart contracts
 
 **Disadvantages**
 - There are still so many unknowns here as this is so relatively new. Only thing I can think of is that it is more complex to understand and ultimately be much difficult to learn

# Boxes
Every transaction in the UTxO models generates unspent outputs. These unspent outputs are referred to as Boxes in ERGO. 

# Addresses
So we need a way to identify accounts on the blockchain. Addresses do just that. However since an account here can contain multiple unspent boxes we need to associate the address of that account with all of those boxes. To spend a box in a transaction, the transaction needs to be signed by the owner of that box.

