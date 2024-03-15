# Ecommerce Product Service

Welcome to the Product Service of my E-Commerce Project!
Ecommerce Product Service is developed for managing the product listing and offering users a fast and seamless product search experience.


## Tech Stack

**Programming Language:** Java

**Framework:** Spring Boot

**Databases:** MySQL, Elasticsearch

**Caching:** Redis (with LRU window=10)



## Problem Statement

The intial design of the product service was very basic and stored only some common charactersitics like product id, name,description, price and quantityAvailable in a MySQL Database.

But, soon I realised that this is an inpractical design as in common e-comerce websites like Amazon and Flipkart there are thousands of products and each has unique set of features like a shirt will have attributes like color, size, etc while a laptop will have RAM, HDD, Width of Display, etc.

Also, sellers are allowed to list their products so we also needed that certain basic charactersitics like color and size for a shirt and RAM, HDD and Display are for laptops should be provided by the sellers inorder to be able to add their products to the listing.

The third problem was to manage inventory for all the varities of products. 


## Solution

To tackle the different set of attributes for different products while keeping our search functionality fast, I came up with an idea to use Elasticsearch.

Elasticsearch is a No-SQL Database that uses inverted indexing to support faster search on object as well as nested objects.

I designed my class to store all the basic infiormation like product id, name, brand, description, price, and quantity in the main Elasticsearch object and created a nested map object to store the product-specific properties as key-value pairs.

For tackling the second problem, I wrote a validation code on application layer. I listed all the products in an Enum which required a list of mandatory properties in its constructor. Now, whenever we get an API call to create a product, I extracted the type of product, parsed the json received in object properties to a HashMap, and matched if my hashmap has all the required keys!

Now, we are left with the last but not the least inventory management. In tehe scenario of a high-traffic website like Amazon and Flipkart, we are required to handle concurrency with speed. Thus, I decided to let the database handle this and not write any application code for this. Asthe solution, I deployed a MySQL database and wrote the query to update the available quantity of the product only if it is greater than 0. 

As SQL operations are atomic and serialized by default, this will ensure concurrency with a great speed of MySQL.

## Links to other services:

Order Service: https://github.com/Manasmalhotra/Ecom-OrderService</br>
User Service: https://github.com/Manasmalhotra/Ecom-UserService</br>
Payment Service: https://github.com/Manasmalhotra/Ecom-PaymentService</br>
Notification Service:https://github.com/Manasmalhotra/Ecom-NotificationService</br>
Service Registry: https://github.com/Manasmalhotra/Ecom-ServiceRegistry
