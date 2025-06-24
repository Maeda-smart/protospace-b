# protospace-b

## 個々人で対応するべきこと

- `protospace-b/env`を編集する。
- DBを作成する。

`protospace-b/env`
```sh
#!/usr/bin/sh
export DATABASE_URL=jdbc:postgresql://localhost:5432/protospace
export DATABASE_PASSWORD=
export DATABASE_ROLE=
```

```mermaid
erDiagram
  users ||--o{ prototype: ""
  users ||--o{ comments: ""
  prototype ||--o{ comments: ""
  prototype ||--o{ prototype_tags: ""
  tags ||--o{ prototype_tags: ""
  users ||--o{ like: ""
  prototype ||--o{ like: ""
  users ||--o| pin: ""
  prototype ||--o| pin: ""
  users{
    SERIAL id PK
    VARCHAR(16) nickname
    VARCHAR(128) email UK
    VARCHAR(128) password
    VARCHAR(512) profile
    VARCHAR(128) affiliation
    VARCHAR(128) position
  }
  prototype{
    SERIAL id PK
    VARCHAR(128) prototypeName
    VARCHAR(128) catchCopy
    VARCHAR(128) concept
    VARCHAR(512) image
    INT user_id FK "users.id"
  }
  comments{
    SERIAL id PK
    VARCHAR(512) text
    INT user_id FK "users.id"
    INT prototype_id FK "prototype.id"
  }
  tags{
    SERIAL id PK
    VARCHAR(128) tag_name
  }
  prototype_tags{
    INT prototype_id FK "prototype.id"
    INT tag_id FK "tags.id"
  }
  like{
    INT prototype_id FK "prototype.id"
    INT user_id FK "user.id"
  }
  pin{
    INT user_id FK "user.id"
    INT prototype_id FK "prototype.id"
  }
```
