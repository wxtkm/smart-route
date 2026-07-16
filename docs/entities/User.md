# User

## Purpose

Represents an application user.

## Fields

- id
- email
- password
- firstName
- lastName
- createdAt
- updatedAt

## Relationships

User -> Vehicles (OneToMany)

User -> Roles (ManyToMany)