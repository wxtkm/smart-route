# SmartRoute Database Design


## User

Application user.

Fields:

- id
- email
- password
- firstName
- lastName
- createdAt
- updatedAt


## Role

User permissions.

Fields:

- id
- name


## Vehicle

User vehicle.

Fields:

- id
- brand
- model
- year
- fuelType
- tankCapacity
- averageConsumption


## Relationships


User

1 ---- *

Vehicle


User

* ---- *

Role