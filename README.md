# BITP3123-Project-FoodDeliveryPlatform
## **Technical Documentation: Food Delivery Platform**

### **Introduction**

**Universiti Teknikal Malaysia Melaka**

**Faculty of Information and Communication Technology**

**Team Members**

**2 BITS S1G2**
| **Name**                             | **Matric Number** |
|--------------------------------------|-------------------|
| MUHAMMAD AMIN BIN ABD RAHMAN         | B032310892        |
| MUHAMMAD ZULHELMI BIN SAMSUL BAHARI  | B032310468        |
| MUHAMMAD IRFAN BIN KHASIM            | B032310730        |
| EIZHAN ARMANI BIN ROHAIMI            | B032310342        |
| MUHAMMAD DANIAL HAKIM BIN MOHD SAOFI | B032320048        |


### **Project Overview**

The Food Delivery Platform is a comprehensive solution designed to connect customers with local
restaurants and delivery partners. The system enables customers to browse menus, place orders,
track deliveries in real-time, and make secure payments. Restaurants can manage their menus,
receive orders, and update order statuses, while delivery partners can accept and fulfill delivery
requests.

The platform solves several key problems in the food delivery industry:

- Streamlines the ordering process for customers
- Provides restaurants with a digital storefront and order management system
- Optimizes delivery logistics for efficient order fulfillment
- Offers a secure payment processing system

Commercial Value & Third-Party Integration

This platform has significant commercial potential in the rapidly growing food delivery market, which
is projected to reach $192 billion globally by 2025. Key value propositions include:

1. **Revenue Streams** :

```
o Commission fees from restaurants
o Delivery fees from customers
```
```
o Premium listing options for restaurants
o Targeted advertising opportunities
```
2. **Third-Party Integrations** :
    o **Map Services** (Google Maps API): For delivery tracking and route optimization


### **System Architecture**

**High-Level Diagram**

<img width="439" height="281" alt="Image 808" src="https://github.com/user-attachments/assets/d5001f22-35d5-4b91-8f17-d58d194fe25b" />


Backend Application

Technology Stack

- **Programming Language** : Java
- **Framework** : Java HTTP Server (com.sun.net.httpserver)
- **Database** : MySQL
- **JSON Processing** : javax.json
- **Authentication** : Custom JWT implementation
- **Build Tool** : Maven


**Security Implementation**

The system implements the following security measures:

1. **JWT Authentication** :
    o All sensitive endpoints require a valid JWT token

```
o Tokens are signed with HS256 algorithm
o Token expiration: 24 hours
```
```
o Refresh token mechanism implemented
```
2. **Data Validation** :

```
o Input sanitization for all API endpoints
o Strict type checking for all request parameters
o JSON schema validation for request bodies
```
3. **Database Security** :
    o Prepared statements to prevent SQL injection

```
o Password hashing with BCrypt
o Principle of least privilege for database users
```
**Error Handling**

Standard error responses follow this format (JsonObject):

{

"error": "Error code",

"message": "Human-readable description",

"timestamp": "2023- 05 - 15T14:30:00Z"

}

Common error responses:

- 400 Bad Request: Invalid request parameters
- 401 Unauthorized: Missing or invalid authentication
- 403 Forbidden: Authenticated but not authorized
- 404 Not Found: Resource doesn't exist
- 500 Internal Server Error: Unexpected server error

Deployment

The system can be deployed using:

1. **Containerization** : Docker with compose for local development


2. **Cloud Deployment** : AWS ECS or Kubernetes cluster for production
3. **CI/CD** : GitHub Actions for automated testing and deployment

Future Enhancements

1. Real-time order tracking with WebSockets
2. Recommendation engine for personalized suggestions
3. Advanced analytics dashboard for restaurants
4. Integration with more payment providers
5. Multi-language support

# Food Delivery Platform Database Design

# Documentation

## Overview

This document describes the database schema for a Food Delivery Platform, including tables,

relationships, and field definitions. The database supports food ordering, delivery tracking,

payment processing, and user management.


<img width="673" height="1016" alt="Food_Delivery_Platform" src="https://github.com/user-attachments/assets/3be8d039-05a9-4a9c-94c9-6184e2c930d0" />


## Database Tables

### 1. food_items

Stores information about food items offered by restaurants.

Field Type Description

food_id BIGINT Primary key, unique identifier for the food item

category VARCHAR(255) Category of the food (e.g., Appetizer, Main Course)

description VARCHAR(255) Description of the food item

image_url VARCHAR(255) URL of the food item's image

is_available BIT(1) Whether the item is currently available (1) or not (0)

name VARCHAR(255) Name of the food item

price DOUBLE Price of the food item

restaurant_id BIGINT Foreign key referencing restaurants.restaurant_id


### 2. orders

Contains order header information.

Field Type Description

order_id BIGINT Primary key, unique identifier for the order

created_at DATETIME(6) Timestamp when the order was placed

delivery_address VARCHAR(255) Delivery address for the order

status ENUM Current status of the order (e.g., PENDING,
PROCESSING, DELIVERED)

total_amount DOUBLE Total amount of the order

customer_id BIGINT

```
Foreign key referencing users.user_id (customer
who placed the order)
```
delivery_partner_id BIGINT

```
Foreign key referencing users.user_id (delivery
partner assigned)
```
restaurant_id BIGINT Foreign key referencing restaurants.restaurant_id


### 3. order_items

Contains line items for each order (many-to-one relationship with orders).

Field Type Description

order_item_id BIGINT Primary key, unique identifier for the order item

price_per_unit DOUBLE Price of the food item at time of ordering

quantity INT Quantity ordered

special_instructions VARCHAR(255) Special instructions for this item

food_id BIGINT Foreign key referencing food_items.food_id

order_id BIGINT Foreign key referencing orders.order_id


### 4. delivery_tracking

Tracks the delivery status and progress of orders.

Field Type Description

tracking_id BIGINT

```
Primary key, unique identifier for the tracking
record
```
current_location VARCHAR(255) Current location of the delivery

estimated_delivery_time DATETIME Estimated time of delivery

last_updated DATETIME(6) Timestamp when the tracking was last updated

status ENUM

```
Current delivery status (e.g., DISPATCHED,
IN_TRANSIT)
```
delivery_partner_id BIGINT Foreign key referencing users.user_id

order_id BIGINT Foreign key referencing orders.order_id


### 5. payments

Records payment transactions for orders.

Field Type Description

payment_id BIGINT Primary key, unique identifier for the payment

amount DOUBLE Payment amount

created_at DATETIME(6) Timestamp when payment was processed

method ENUM Payment method (e.g., CREDIT_CARD, PAYPAL)

status ENUM

```
Payment status (e.g., PENDING, COMPLETED,
FAILED)
```
transaction_id VARCHAR(255) Unique transaction ID from payment processor

order_id BIGINT Foreign key referencing orders.order_id


### 6. restaurants

Contains information about restaurants on the platform.

Field Type Description

restaurant_id BIGINT Primary key, unique identifier for the restaurant

closing_time VARCHAR(255) Restaurant's closing time

delivery_radius INT Maximum delivery radius in meters/kilometers

description VARCHAR(255) Description of the restaurant

logo_url VARCHAR(255) URL of the restaurant's logo

name VARCHAR(255) Name of the restaurant

opening_time VARCHAR(255) Restaurant's opening time

user_id BIGINT Foreign key referencing users.user_id (restaurant
owner/admin)

is_active BIT(1) Whether the restaurant is active (1) or not (0)


### 7. users

Stores information about all system users (customers, restaurant staff, delivery partners).

Field Type Description

user_id BIGINT Primary key, unique identifier for the user

address VARCHAR(255) User's address

email VARCHAR(255) User's email address

full_name VARCHAR(255) User's full name

is_active BIT(1) Whether the user account is active (1) or not (0)

password VARCHAR(255) Hashed password

phone VARCHAR(255) User's phone number

role ENUM User role (e.g., CUSTOMER, RESTAURANT_ADMIN,
DELIVERY_PARTNER)

username VARCHAR(255) Unique username

## Relationships

1. Restaurants to Food Items: One-to-Many (One restaurant can have many food

```
items)
```
2. Users to Restaurants: One-to-Many (One user can own/manage many restaurants)
3. Orders to Order Items: One-to-Many (One order can have many order items)
4. Orders to Users: Many-to-One (Many orders can belong to one customer)
5. Orders to Restaurants: Many-to-One (Many orders can be placed at one restaurant)
6. Delivery Tracking to Orders: One-to-One (One tracking record per order)
7. Payments to Orders: One-to-One (One payment record per order)


## Indexes

Primary indexes are created on all primary key fields (food_id, order_id, etc.). Additional

indexes should be considered for:

- Frequently queried fields (user email, restaurant name)
- Foreign key fields for join performance
- Fields used in WHERE clauses (order status, delivery status)

## Enum Values

The documentation should specify possible values for ENUM fields:

- Order status: PENDING, ACCEPTED, PREPARING, READY_FOR_DELIVERY,

```
ON_THE_WAY, DELIVERED, CANCELLED
```
- Delivery status: PENDING, IN_TRANSIT, DELIVERED,
- Payment status: PENDING, COMPLETED, FAILED
- Payment method: CARD, PAYPAL, CASH_ON_DELIVERY
- User role: CUSTOMER, RESTAURANT, DELIVERY_PARTNER

## Business Rules

1. A food item must belong to exactly one restaurant
2. An order must have at least one order item
3. Each order must have exactly one payment record
4. Each order must have exactly one delivery tracking record
5. Restaurant staff users must be associated with at least one restaurant
6. Delivery partners cannot be associated with restaurants


## Data Dictionary

## 1. food_items Table

```
Field
Name
```
```
Data Type Constraint
s
```
```
Description Example Values
```
```
food_id BIGINT PRIMARY
KEY, NOT
NULL
```
```
Unique
identifier for
the food item
```
#### 1001, 1002

```
category VARCHAR(
5)
```
#### NOT

#### NULL

```
Category of
the food item
```
```
"Main Course", "Appetizer",
"Dessert"
description VARCHAR(
5)
```
#### NULLABL

#### E

```
Description
of the food
item
```
```
"Spicy chicken wings with
ranch dip"
```
```
image_url VARCHAR(
5)
```
#### NULLABL

#### E

```
URL of the
food item's
image
```
```
"https://example.com/wings.jp
g"
```
```
is_available BIT(1) NOT
NULL,
DEFAULT
1
```
```
Availability
status
(1=available,
0=unavailabl
e)
```
#### 1, 0

```
name VARCHAR(
5)
```
#### NOT

#### NULL

```
Name of the
food item
```
```
"Buffalo Wings", "Margherita
Pizza"
price DOUBLE NOT
NULL
```
```
Price of the
food item
```
#### 12.99, 8.

```
restaurant_i
d
```
#### BIGINT FOREIGN

#### KEY

```
(restaurants
), NOT
NULL
```
```
ID of the
restaurant
offering this
item
```
#### 501, 502


## 2. orders Table

```
Field Name Data Type Constraint
s
```
```
Descriptio
n
```
```
Example Values
```
```
order_id BIGINT PRIMARY
KEY, NOT
NULL
```
```
Unique
identifier
for the
order
```
#### 2001, 2002

```
created_at DATETIME(6) NOT
NULL
```
```
Timestamp
when order
was placed
```
#### "2023- 10 - 15

#### 14:30:22.123456"

```
delivery_address VARCHAR(
5)
```
#### NOT

#### NULL

```
Delivery
address for
the order
```
```
"123 Main St, Apt 4B,
New York"
```
```
status ENUM NOT
NULL
```
```
Current
status of
the order
```
#### 'PENDING',

#### 'ACCEPTED'

#### ,'PREPARING',

#### 'READY_FOR_DELIVER

#### Y',

#### 'ON_THE_WAY',

#### 'DELIVERED',

#### 'CANCELLED'

```
total_amount DOUBLE NOT
NULL
```
```
Total
amount of
the order
```
#### 45.75, 32.

```
customer_id BIGINT FOREIGN
KEY
(users),
NOT
NULL
```
```
ID of the
customer
who placed
order
```
#### 3001, 3002

```
delivery_partner_
id
```
#### BIGINT FOREIGN

#### KEY

```
(users),
NULLABL
E
```
```
ID of
assigned
delivery
partner
```
#### 4001, 4002

```
restaurant_id BIGINT FOREIGN
KEY
(restaurants
), NOT
NULL
```
```
ID of the
restaurant
receiving
order
```
#### 501, 502


## 3. order_items Table

```
Field Name Data Type Constraints Description Example
Values
order_item_id BIGINT PRIMARY KEY,
NOT NULL
```
```
Unique
identifier for
order item
```
#### 5001, 5002

```
price_per_unit DOUBLE NOT NULL Price of item at
time of ordering
```
#### 12.99, 8.

```
quantity INT NOT NULL Quantity
ordered
```
#### 2, 1

```
special_instructions VARCHAR(255) NULLABLE Special
preparation
requests
```
```
"No
onions",
"Extra
spicy"
food_id BIGINT FOREIGN KEY
(food_items), NOT
NULL
```
```
ID of the food
item ordered
```
#### 1001, 1002

```
order_id BIGINT FOREIGN KEY
(orders), NOT
NULL
```
```
ID of the parent
order
```
#### 2001, 2002


## 4. delivery_tracking Table

```
Field Name Data Type Constraints Description Example Values
tracking_id BIGINT PRIMARY
KEY, NOT
NULL
```
```
Unique
tracking
identifier
```
#### 6001, 6002

```
current_location VARCHAR(255) NULLABLE Current
delivery
location
```
```
"123 Main St",
"In transit - 5
mins away"
estimated_delivery_time DATETIME NULLABLE Estimated
delivery
completion
time
```
#### "2023- 10 - 15

#### 15:30:00"

```
last_updated DATETIME(6) NOT NULL Last status
update
timestamp
```
#### "2023- 10 - 15

#### 15:15:22.123456"

```
status ENUM NOT NULL Current
delivery
status
```
#### "PICKED_UP",

#### "IN_TRANSIT",

#### "DELIVERED"

```
delivery_partner_id BIGINT FOREIGN
KEY (users),
NULLABLE
```
```
ID of
delivery
partner
```
#### 4001, 4002

```
order_id BIGINT FOREIGN
KEY
(orders),
NOT NULL
```
```
ID of the
order being
tracked
```
#### 2001, 2002


## 5. payments Table

```
Field Name Data Type Constraints Description Example Values
payment_id BIGINT PRIMARY
KEY, NOT
NULL
```
```
Unique
payment
identifier
```
#### 7001, 7002

```
amount DOUBLE NOT NULL Payment
amount
```
#### 45.75, 32.

```
created_at DATETIME(6) NOT NULL Payment
processing
timestamp
```
#### "2023- 10 - 15

#### 14:31:05.123456"

```
method ENUM NOT NULL Payment
method used
```
#### "CARD", "PAYPAL",

#### "CASH_ON_DELIVERY"

```
status ENUM NOT NULL Payment
status
```
#### "PENDING",

#### "COMPLETED",

#### "FAILED"

```
transaction_id VARCHAR(255) NULLABLE Processor
transaction
ID
```
#### "TXN123456789",

#### "PYPL987654321"

```
order_id BIGINT FOREIGN
KEY
(orders),
NOT NULL
```
```
ID of the
associated
order
```
#### 2001, 2002


## 6. restaurants Table

```
Field Name Data Type Constraint
s
```
```
Description Example Values
```
```
restaurant_id BIGINT PRIMARY
KEY, NOT
NULL
```
```
Unique
restaurant
identifier
```
#### 501, 502

```
closing_time VARCHAR(
5)
```
#### NOT

#### NULL

```
Closing time
(format
flexible)
```
#### "22:00", "11:00 PM"

```
delivery_radi
us
```
#### INT NOT

#### NULL

```
Max
delivery
distance in
meters
```
#### 5000, 10000

```
description VARCHAR(
5)
```
#### NULLABL

#### E

```
Restaurant
description
```
```
"Authentic Italian cuisine
since 1985"
logo_url VARCHAR(
5)
```
#### NULLABL

#### E

```
URL of
restaurant
logo
```
```
"https://example.com/logo.jp
g"
```
```
name VARCHAR(
5)
```
#### NOT

#### NULL

```
Restaurant
name
```
```
"Mama Mia's", "Burger
Palace"
opening_time VARCHAR(
5)
```
#### NOT

#### NULL

```
Opening
time (format
flexible)
```
#### "08:00", "9:00 AM"

```
user_id BIGINT FOREIGN
KEY
(users),
NOT
NULL
```
```
Owner/admi
n user ID
```
#### 3005, 3006

```
is_active BIT(1) NOT
NULL,
DEFAULT
1
```
```
Active
status
(1=active,
0=inactive)
```
#### 1, 0


## 7. users Table

```
Field
Name
```
```
Data Type Constraints Description Example Values
```
```
user_id BIGINT PRIMARY
KEY, NOT
NULL
```
```
Unique user
identifier
```
#### 3001, 3002

```
address VARCHAR(255) NULLABLE User's
physical
address
```
```
"456 Oak Ave, Chicago"
```
```
email VARCHAR(255) NOT NULL,
UNIQUE
```
```
User's email
address
```
```
"user@example.com"
```
```
full_name VARCHAR(255) NOT NULL User's full
name
```
```
"John Smith", "Maria
Garcia"
is_active BIT(1) NOT NULL,
DEFAULT 1
```
```
Account
status
(1=active,
0=inactive)
```
#### 1, 0

```
password VARCHAR(255) NOT NULL Hashed
password
```
```
"hashed_value_abc123"
```
```
phone VARCHAR(255) NULLABLE Contact
phone
number
```
#### "+15551234567"

```
role ENUM NOT NULL User role in
system
```
#### "CUSTOMER",

#### "RESTAURANT ",

#### "DELIVERY_PARTNER"

```
username VARCHAR(255) NOT NULL,
UNIQUE
```
```
Login
username
```
```
"jsmith", "mariag"
```

## Enumeration Values

orders.status:

#### • PENDING

#### • ACCEPTED

#### • PREPARING

#### • READY_FOR_DELIVERY

#### • ON_THE_WAY

#### • DELIVERED

#### • CANCELLED

delivery_tracking.status:

#### • PICKED_UP

#### • IN_TRANSIT

#### • DELIVERED

payments.method:

#### • CARD

#### • PAYPAL

#### • CASH_ON_DELIVERY

payments.status:

#### • PENDING

#### • COMPLETED

#### • FAILED

users.role:

#### • CUSTOMER

#### • RESTAURANT

#### • DELIVERY_PARTNER




