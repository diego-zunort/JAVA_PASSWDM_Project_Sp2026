# Java Password Manager

A Java desktop application for securely storing and managing passwords, built as a final project for Spring 2026.

## Features

- User authentication (Login / Sign Up with security PIN)
- Encrypted password vault per user (AES encryption)
- Add, view, modify, and remove saved credentials
- Built-in password generator
- Admin user role with elevated permissions (e.g., reset user passwords)

## UI Screens

| Screen | Description |
|---|---|
| Log In | Authenticate with username and master password |
| Sign Up | Create account with username, master password, and security PIN |
| Password Manager (main) | Table view of all saved entries; access all actions from here |
| Add Password | Save a new site/username/password entry |
| Generate Password | Auto-generate a password for a site |
| Modify Password | Update the password for an existing entry |
| Remove Password | Delete an existing entry after confirming current password |
| View Password | Reveal a stored password after entering security PIN |

## Architecture

The project demonstrates the four pillars of OOP:

- **Abstraction** — `User` abstract class and `IEncryptable` interface define structure without full implementation
- **Encapsulation** — private/protected fields accessed only through methods
- **Inheritance** — `AdminUser` and `StandardUser` both extend `User`
- **Polymorphism** — both subclasses override `hasPermission()` differently

### Class Diagram

```mermaid
classDiagram
    class User {
        <<abstract>>
        #username: String
        #masterPassword: String
        +getUsername()
        +verifyPassword(input)
        +hasPermission()*
    }

    class AdminUser {
        +hasPermission()
        +resetUserPassword()
    }

    class StandardUser {
        -vault: Vault
        +hasPermission()
        +getVault()
    }

    class Vault {
        -entries: ArrayList
        +addEntry(entry)
        +removeEntry(id)
        +searchEntries(keyword)
        +lock()
        +unlock(password)
    }

    class PasswordEntry {
        -siteName: String
        -username: String
        -password: String
        -category: String
        +getPassword()
        +setPassword(newPwd)
    }

    class PasswordGenerator {
        +generate(length)
        +checkStrength(password)
    }

    class IEncryptable {
        <<interface>>
        +encrypt(data)*
        +decrypt(data)*
    }

    class AESEncryption {
        -secretKey: String
        +encrypt(data)
        +decrypt(data)
    }

    note for User "ABSTRACTION<br/>Abstract class + interface<br/>define structure without<br/>full implementation"
    note for AdminUser "POLYMORPHISM<br/>Both subclasses override<br/>hasPermission() differently"
    note for StandardUser "ENCAPSULATION<br/>- private fields hidden<br/># protected fields<br/>accessed via methods"
    note for IEncryptable "ABSTRACTION<br/>Interface enforces<br/>a contract for encryption"

    User <|-- AdminUser : INHERITANCE
    User <|-- StandardUser : INHERITANCE
    IEncryptable <|.. AESEncryption : Realization
    StandardUser *-- Vault : Composition
    Vault *-- PasswordEntry : Composition
    PasswordEntry ..> IEncryptable : Dependency
    StandardUser ..> PasswordGenerator : Dependency
```

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17+ |
| UI | Java Swing / AWT |
| Database | SQLite (via `sqlite-jdbc` — xerial) |
| Encryption | AES (custom `AESEncryption` class implementing `IEncryptable`) |

> Passwords are encrypted via `AESEncryption` before being written to the database — the SQLite file itself is not relied upon for security.


