
# Api de publicação de vídeo em multiplatformas simultaneamente

MPVU API é uma aplicação desenvolvida em `Spring Boot (Java)` que permite a publicação de vídeos em múltiplas plataformas de forma simultânea. A API oferece integração com o `TikTok` e `Instagram`, utilizando autenticação segura via OAuth2 para garantir a privacidade e a confiabilidade do processo.

## Documentação da API

#### Login

```http
  POST /api/v2/user/login
```
#### Body

| Parâmetro   | Tipo       | Descrição                                   |
| :---------- | :--------- | :------------------------------------------ |
| `username`      | `string` | **Obrigatório**. Username do usuario |
| `password`      | `string` | **Obrigatório**. Senha do usuario |

#### Responstas

| Status      | Descrição                                          |
| :---------- | :------------------------------------------------- |
| `200`  | JWT user token |
| `400`  | Username and password are required |
| `404`  | Invalid username |
| `401`  | Invalid password |

###

#### Registro

```http
  POST /api/v2/user/register
```

#### Body

| Parâmetro   | Tipo       | Descrição                                   |
| :---------- | :--------- | :------------------------------------------ |
| `username`      | `string` | **Obrigatório**. Username do usuario |
| `email`      | `string` | **Obrigatório**. Email do usuario |
| `password`      | `string` | **Obrigatório**. Senha do usuario |

#### Responstas

| Status      | Descrição                                          |
| :---------- | :------------------------------------------------- |
| `200`  | JWT user token |
| `400`  | Username and password and email are required |
| `400`  | Invalid email |
| `400`  | Email is already in use |

###

#### TikTok oAuth2 callback

```http
  GET /api/v2/oauth2/tiktok/callback
```
#### Params

| Parâmetro   | Tipo       | Descrição                                   |
| :---------- | :--------- | :------------------------------------------ |
| `userToken`      | `string` | **Obrigatório**. Token do usuario |
| `code`      | `string` | **Obrigatório**. Code do login do usuario no tiktok |

#### Responstas

| Status      | Descrição                                          |
| :---------- | :------------------------------------------------- |
| `200`  | Connectado com sucesso |
| `400`  | User token and code are required |
| `400`  | Invalid code |
| `401`  | Invalid user token |
| `404`  | User not found |

###

#### Google callback

```http
  GET /api/v2/oauth2/google/callback
```

#### Params

| Parâmetro   | Tipo       | Descrição                                   |
| :---------- | :--------- | :------------------------------------------ |
| `code`      | `string` | **Obrigatório**. Code do login do usuario no google |

#### Responstas

| Status      | Descrição                                          |
| :---------- | :------------------------------------------------- |
| `200`  | Se o email tiver cadastrado retorna o JWT do usuario ou  retorna o Token do confirm-password do usuario |
| `400`  | Code are required |
| `400`  | Invalid code |
| `404`  | Invalid user |

###

#### Confirma senha com registro usando google

```http
  POST /api/v2/oauth2/google/confirm-password
```

#### Body

| Parâmetro   | Tipo       | Descrição                                   |
| :---------- | :--------- | :------------------------------------------ |
| `token`      | `string` | **Obrigatório**. Token do confirm-password do usuario |
| `password`      | `string` | **Obrigatório**. Senha do usuario |

#### Responstas

| Status      | Descrição                                          |
| :---------- | :------------------------------------------------- |
| `200`  | JWT do usuario |
| `400`  | Password and token are required |
| `401`  | Token is invalid" |
| `401`  | Token already used |
| `401`  | Token is invalid |

###