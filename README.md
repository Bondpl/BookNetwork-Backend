## Project:
- Book Social Network 
Users can lend and borrow books from others in the community. Each book would have a rental history, and users could rate the books.

### User:

- **id**: `Long` (Primary Key)
- **name**: `String`
- **email**: `String`
- **password**: `String`
- **description**: `String`
- **profilePictureUrl**: `String`
- **role**: `Enum (User, Admin, Banned)`

**Relationships**:

- **One-to-Many with Books**: A user can lend multiple books.
- **Many-to-Many with Books**: A user can borrow multiple books, and each book can be borrowed by multiple users over time.

### Book:

- **id**: `Long` (Primary Key)
- **title**: `String`
- **author**: `String`
- **isbn**: `String`
- **status**: `Enum` (`AVAILABLE`, `BORROWED`)
- **averageRating**: `Double`

**Relationships**:
**Many-to-One with Borrower (User)**: Many books can be borrowed by one user.

### Transaction history:

- **id**: `Long` (Primary Key)
- **borrowerId**: `User`
- **lenderId**: `User`
- **bookId**: `Book`
- **status**: `Enum` (`ONGOING`, `COMPLETED`)

### Rating:

- **id**: `Long` (Primary Key)
- **userId**: `User`
- **bookId**: `Book`
- **lenderId / borrowerId**: `User` (Reference to the user being rated, for borrower/lender feedback)
- **ratingValue**: `Integer` (Numerical rating, e.g., 1 to 5)
### Query:

- **Find all books with rating above 4.0**.

### Business Logic Operation:

- **Borrowing a Book**: The described operation is well-defined and involves multiple steps:
    1. **Check Book Availability**: Ensuring the book can be borrowed.
    2. **Create Transaction**: Recording the transaction details.
    3. **Update Book Status**: Changing the status of the book to `BORROWED`.
    4. **Update User’s Borrowed Books**: Incrementing the count of books borrowed by the user.