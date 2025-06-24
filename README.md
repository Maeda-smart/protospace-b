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
    INT createdBy FK
  }
```
