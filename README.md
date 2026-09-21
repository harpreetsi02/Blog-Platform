# Blog Platform

A RESTful blogging/content platform backend built with **Spring Boot**, featuring JWT + OAuth2 authentication, nested comments, tagging, likes, soft delete, full-text search, and query-performance optimizations.

## ✨ Features

- **Authentication & Authorization**
  - Email/password login with BCrypt password hashing
  - JWT-based stateless authentication
  - OAuth 2.0 Login with Google (issues the same JWT as normal login)
  - Role-based access control (RBAC)

- **Posts**
  - Full CRUD with pagination
  - Author association, tag association
  - Soft delete (`deletedAt`) instead of hard delete
  - Optimistic locking (`@Version`) on updates

- **Comments**
  - Self-referencing relationship for nested/threaded replies
  - Idempotent creation (via `Idempotency-Key` header) to prevent duplicate submissions

- **Tags**
  - Many-to-many relationship between Posts and Tags
  - Find-or-create resolution from tag names

- **Likes**
  - One like per user per post, enforced via a database unique constraint
  - Toggle-based like/unlike endpoint

- **Search**
  - Basic keyword search via JPQL `LIKE`
  - Production-grade search via MySQL `FULLTEXT` index (native query)

- **Performance**
  - N+1 query prevention via `JOIN FETCH` (single-object associations) and `@BatchSize` (collections)
  - Method-level caching (`@Cacheable` / `@CacheEvict`) for frequently accessed posts

## 🛠️ Tech Stack

- Java, Spring Boot
- Spring Security (JWT + OAuth2 Client)
- Spring Data JPA / Hibernate
- MySQL / MariaDB
- Maven

## 📁 Package Structure

```
com.backend.blogplatform
 ├── entity/
 ├── dto/
 │    ├── request/
 │    └── response/
 ├── mapper/
 ├── repository/
 ├── service/
 ├── controller/
 ├── exception/
 ├── config/
 └── security/
```

## 🚀 Getting Started

1. Clone the repository
2. Copy `application.properties.example` to `application.properties` and fill in:
   - MySQL database credentials
   - JWT secret and expiration
   - Google OAuth2 client ID/secret
3. Run `ALTER TABLE posts ADD FULLTEXT(title, content);` on your database (for full-text search)
4. Build and run: `mvn spring-boot:run`

## 📌 API Overview

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/login` | Login and receive a JWT |
| GET | `/oauth2/authorization/google` | Login with Google |
| GET | `/api/posts` | List posts (paginated) |
| POST | `/api/posts` | Create a post |
| PUT | `/api/posts/{id}` | Update a post |
| DELETE | `/api/posts/{id}` | Soft-delete a post |
| GET | `/api/posts/search?keyword=` | Keyword search (LIKE) |
| GET | `/api/posts/search-fulltext?keyword=` | Keyword search (FULLTEXT) |
| POST | `/api/posts/{postId}/like` | Toggle like on a post |
| POST | `/api/comments` | Add a comment (supports replies via `parentCommentId`) |
| GET | `/api/posts/{postId}/comments` | Get comments for a post |