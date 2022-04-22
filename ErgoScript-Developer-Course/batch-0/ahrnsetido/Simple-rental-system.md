# Simple Rental System (for example bike rental)
##### _ErgoScript Batch-0 Homework 1_

## 1 Goals and purpose

 - In this simple rental system, real life assets can be rented out for a limited time period in exchange for a small fee.
 - Each real life asset is represented by a token (NFT), or the access to the real life asset can be enabled with that token (NFT).
 - In addition to the rental fee, the renter needs to deposit a certain amount of ERG  as insurance.
 - Various scenarios of returning the asset can be defined by the asset provider and an appropriate penalty on the deposit can be defined before a rental agreement.
 1.1. successful return: in time, and good condition
 1.2. late return, good condition
 1.3. timely return, bad condition
 1.4. late return, bad condition
 1.5. no return

## 2 Parties involved

  2.1. **Renter**: Wants to rent the real life asset for use
 
  2.2. **Provider**: Provides real life asset

## 3 Overview of transactions

3.1. **Renter provides deposit and rental fee** to the system.
3.2. **Renter is given the token** to access real life asset.
3.3. **Renter picks up real life asset**. 
3.4. **Provider is paid for the rental fee**.
3.5. **Renter returns real life asset**.
3.6. **Provider confirms return of the asset**.
3.7. **Deposit is returned to Renter**.

## 4 Details of Transactions
### 4.1 Renter provides deposit and fee transaction
|INPUTS|OUTPUTS|
|------|-------|
|**In(0): Renter Utility Box** | **Out(0): Renter Utility Box**|
|**Inputs: Renter eUTXOs**| **Out(1): Renter Proxy Contract Box (PCB)**|
| | **Out(2): Renter eUTXOs**|
| | **Out(3): Mining fee**|

|**In(0): Renter Utility Box** |
|----|
|- helps create all info/registers into Renter PCB|
|- Spend.Cond: if Renter requests a new job and Renter eUTXO has the correct amount of ERG|

|**Out(0): Renter Utility Box**|
|----|
|- Renter Utility Box is re-created|


 |**Inputs: Renter eUTXOs**|
|----|
|- eUTXOs in Renter's wallet|
|- has to cover rental fee, deposit amount and mining fee in ERG|
|- Spend. Cond: Renter's PK|

 |**Out(1): Renter PCB**|
|----|
|- is created with a rental job|
|- tracks rental info, rental job ID|
|- Registers: Renter ID, Job ID, Job Status = created|

| **Out(2): Renter eUTXO**|
|---|
|- Change returned to Renter|

| **Out(3) Mining fee**|
|---|
|- 0.001ERG |

### 4.2  Renter is given the token to access real life asset
|INPUTS|OUTPUTS|
|------|-------|
|**In(0): Provider Token Box (PTB)**| **Out(0): PTB**|
|**In(1): Renter PCB** |**Out(1): Renter PCB** | 
||**Out(2): Renter eUTXO**|
||**Out(3): Mining fee**|

|**In(0): Provider Token Box**|
|---|
|- contains rental / deposit return conditions|
|- Tokens: NFTs to access real life asset|
|- Registers: provider ID|
|- Spend. cond.: Renter PCB requests a token and can pay deposit+fees in ERG|

|**In(1): Renter PCB**|
|---|
|- Value: deposit + rental fee|
|- Registers: Renter ID, Job ID, Job Status = created|
|- Spend. Cond.: PTB in tx to provide a token, token is sent to Renter eUTXO|

|**Out(0): PTB**|
|---|
|- The same PTB as In(0)|
|- Value: increased by deposit and fee of the rental job|
|- Registers: provider ID|
|- Tokens: one less access NFTs|

|**Out(1): Renter PCB**|
|---|
|- same as In(1)|
|- Value: minimal|
|- Registers: Renter ID, Job ID, Job Status = NFT-sent|

|**Out(2): Renter eUTXO**|
|---|
|- Token: access NFT for real life asset |

|**Out(3): Mining fee**|
|---|
|- 0.001ERG |

### 4.3+4.4 **Renter picks up real life asset + Provider is paid**

|INPUTS|OUTPUTS|
|------|-------|
|**In(0): PTB**| **Out(0): PTB**|
|**In(1): Renter PCB**|**Out(1): Renter PCB**|
||**Out(2): Provider eUTXO**|
||**Out(3): Mining fee**|

|**In(0): PTB**|
|---|
|- Value: contains deposit, fee for rental job|
|- Spend. Cond.: fee is paid to Provider eUTXO and Out(1) Renter PCB will be in the status where the real life asset has been accessed |

|**In(1): Renter PCB**|
|----|
|- contains rental job info and status|
|- Value: minimal|
|- Registers: Renter ID, Job ID, Job Status = NFT-sent|
|- Spend. Cond.: System detecting that Renter accesses real life asset|

|**Out(0): PTB**|
|----|
|- same as In(0) PTB|
|- Value: contains deposit for rental job|

|**Out(1): Renter PCB**|
|----|
|- same as In(1) with updated status|
|- Value: minimal|
|- Registers: Renter ID, Job ID, Job Status = in-use|

|**Out(2): Provider eUTXO**|
|----|
|- Value: rental fee of job|

|**Out(3): Mining fee**|
|---|
|- 0.001ERG |


### 4.5 + 4.6 + 4.7 **Renter returns real life asset**, **Provider confirms return + Deposit is returned**

|INPUTS|OUTPUTS|
|------|-------|
|**In(0): PTB**|**Out(0): PTB**|
|**In(1): Renter PCB**|**Out(1): Renter PCB**|
|**In(2): Renter eUTXO**|**Out(2): Renter eUTXO**|
||**Out(3): Mining fee**|

|**In(0): PTB**|
|---|
|- Value: contains deposit for rental job|
|- Spend. Cond.: Provider confirms return of real life asset|

|**In(1): Renter PCB**|
|----|
|- Value: minimal|
|- Registers: Renter ID, Job ID, Job Status = in-use|
|- Spend. Cond.: In(2) Renter eUTXO has access NFT|

|**In(2): Renter eUTXO**|
|----|
|- Value: minimal|
|- Tokens: access NFT|

|**Out(0): PTB**|
|---|
|- Value: less deposit of rental job|
|- Tokens: increased access NFT by 1|

|**Out(1): Renter PCB**|
|----|
|- Value: minimal|
|- Registers: Renter ID, Job ID, Job Status = returned|

|**Out(2): Renter eUTXO**|
|----|
|- Value: deposit|

|**Out(3): Mining fee**|
|---|
|- 0.001ERG |
