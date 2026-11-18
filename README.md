# Lab 3 - Spring Data JPA, Hibernate et MySQL

API REST de gestion de produits, catégories et commandes avec persistance dans MySQL. Le projet utilise Spring Boot 3.2, Java 21, Spring Data JPA, Hibernate et Lombok.

## Contenu

- `src/main/java` : entités (Product, Category, Order, OrderItem), repositories, DTOs, services et controllers
- `src/main/resources/application.properties` : connexion MySQL
- `src/main/resources/db` : scripts SQL (création de la base et données de test)
- `postman/` : collection Postman exportée

## Prérequis

- Java 21
- MySQL avec une base `lab3_db`

## Configuration

Dans `src/main/resources/application.properties`, adapter l'URL, l'utilisateur et le mot de passe MySQL :

```
spring.datasource.url=jdbc:mysql://localhost:3307/lab3_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=root
```

Les tables sont créées automatiquement par Hibernate au démarrage (`ddl-auto=update`).

## Lancer le projet

1. Créer la base avec le script `src/main/resources/db/schema.sql`.
2. Ouvrir un terminal dans le dossier du projet et lancer :
   ```
   .\mvnw spring-boot:run
   ```
3. L'API démarre sur `http://localhost:8080`.

On peut aussi lancer `Lab3JpaApplication` depuis IntelliJ.

Pour avoir des données de test, exécuter `src/main/resources/db/data.sql` dans MySQL Workbench après le premier démarrage.

## Endpoints

| Méthode | URL | Description |
|---|---|---|
| POST | `/api/categories` | Créer une catégorie |
| GET | `/api/categories` | Lister les catégories |
| GET | `/api/categories/{id}` | Afficher une catégorie |
| GET | `/api/categories/{id}/products` | Produits d'une catégorie |
| POST | `/api/products` | Créer un produit |
| GET | `/api/products?page=0&size=10&sort=id` | Lister les produits avec pagination et tri |
| GET | `/api/products/search?keyword=...` | Chercher un produit par nom |
| GET | `/api/products/{id}` | Afficher un produit |
| PUT | `/api/products/{id}` | Modifier un produit |
| DELETE | `/api/products/{id}` | Supprimer un produit |
| POST | `/api/orders` | Créer une commande avec ses items |
| GET | `/api/orders/{id}` | Afficher une commande |

Exemples de body :

```json
{ "name": "Électronique" }
```

```json
{ "name": "Smartphone", "price": 699.99, "categoryId": 1 }
```

```json
{ "items": [ { "productId": 1, "quantity": 2 } ] }
```

Codes de réponse : 201 (création), 200, 204 (suppression), 400 (validation), 404 (introuvable), 409 (nom en double ou produit utilisé dans une commande).

## Tester avec Postman

1. Lancer l'API.
2. Dans Postman, cliquer sur **Import** et choisir le fichier du dossier `postman/`.
3. Lancer les requêtes dans cet ordre : Create category, Create product, Create order, puis les autres.
4. Vérifier les données dans MySQL Workbench avec `SELECT * FROM products;`.
