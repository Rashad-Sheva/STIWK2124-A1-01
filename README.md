# STIWK2124-A1-01
Accessible Reading List (ARL) web app that is able to store and serve book entries via a REST API with validation, pagination, and basic search. 
#  Accessible Reading List (Backend)

## Team Members
- Rashad-Sheva
- ainfana01
- ainasafa
- Mel0205

## Features Completed
- Full CRUD REST API (`/api/books`)
- Input Validation (`@Valid`, `@NotBlank`, etc.)
- Pagination (`?page=0&size=10`)
- Search by title or author (`?q=keyword`)
- Proper HTTP Status Codes
- MySQL Database with JPA

## How to Run

1. **Database**: Run `schema.sql` in MySQL Workbench
2. **Start Backend**:
   ```bash
   ./mvnw spring-boot:run