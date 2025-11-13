Assignment 4
------------

# Team Members
- Arthur Van Petegem
- Benjamin Hikaru Pfister

# GitHub link to your (forked) repository (if submitting through GitHub)

...

# Task 2

1. Why did message D have to be buffered and can we now always guarantee that all clients
   display the same message order?
- Message D arrived before the messages it depended on. The vector clock showed that the sender saw events that the receiver hadn't processed yet.
So D was buffered until these missing messages arrived, which maintained causal order.
- Vector clocks can only guarantee causal ordering (if A causes B, everyone can see A before B). Concurrent messages may arrive in different orders
at different clients, so we can't always guarantee it.

2. Note that the chat application uses UDP. What could be an issue with this design choice—and
   how would you fix it?
- UDP doesn't guarantee message delivery, order, or integrity. Messages can be lost, duplicated, or arrive out of order.
- To fix this, we switch to TCP which guarantees reliable, and ordered delivery of messages.

# Task 3

1. What is potential causality in Distributed Systems, and how can you model it? Why
   “potential causality” and not just “causality”?
>

2. If you look at your implementation of Task 2.1, can you think of one limitation of Vector Clocks? How would you overcome the limitation?
>

3. Figure 4 shows an example of enforcing causal communication using Vector Clocks. You can find a detailed explanation of this example and the broadcast algorithm being used in
   the Distributed Systems book by van Steen and Tannenbaum (see Chapter 5.2.2, page 270). Would you achieve the same result if you used the same broadcast algorithm but replaced
   Vector Clocks with Lamport Clocks? If not, why not? Explain briefly. 
>
