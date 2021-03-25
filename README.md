# **API Wallet Transfer - Desafio Backend**

#### Esta API realiza as operações CRUD de usuários, bem como a transferência de dinheiro entre eles.
![REST http Verbs](https://www.codeproject.com/KB/webservices/826383/table.png)

### :computer: **Tecnologias usadas**

- Apache Maven
- Java 11
- Spring Boot
- Spring Data JPA
- Hibernate
- Banco H2 e MySQL
- Swagger
- Junit / Mockito / Rest Assured

---
## :building_construction: **1 - Estrutura do projeto**

![Estrutura do projeto](https://github.com/Vandeilsonln/Wallet_Transfer/blob/master/_images/estrutura_projeto.PNG?raw=true)

## **1.1 - Model**

#### Camada responsável por fazer o **ORM** *(Object Relational Mapper)*, fazendo uma ponte entre uma classe no Java com o modelo relacional do banco de dados.

### **1.1.1 - Users**

#### Entidade que representa os usuários da aplicação. Os mesmos podem optar por se cadastrarem como pessoa *física* (usuários comuns) ou *jurídica* (lojistas).


![Users entity](https://github.com/Vandeilsonln/Wallet_Transfer/blob/master/_images/user_entity.PNG?raw=true)

#### Foram usadas as seguintes anotações para auxiliar no desenvolvimento:
- **@Data**: Anotação do *Lombok* que se encarrega de criar os *getters* e *setters* em tempo de compilação.
- **@Accessors(chain=true)**: Anotação do *Lombok* que faz os *setters* retornarem *this* ao invés de *void*.
- **@GroupSequenceProvider**: Uma vez que temos um atributo onde pode ser validado tanto o CPF como o CNPJ, precisamos criar grupos para informar ao hibernate examente qual validação será aplicada, para que não haja conflitos ou que uma validação tenha preferência sobre a outra.
- **@AllArgsConstructor / @NoArgsConstructor**: Criam, respectivamente, construtores com todos os argumentos; e sem argumento algum.

### **1.1.2 - Transfer**

#### Entidade que representa as transferências da aplicação. Elas são instanciadas a partir do momento que uma transferência for bem sucedida e contém somente os IDs de quem enviou o dinheiro, de quem recebeu e do valor transferido.
![Transfer entity](https://github.com/Vandeilsonln/Wallet_Transfer/blob/master/_images/transferentity.PNG?raw=true)
---
## **DTOs - Data Transfer Object**

#### O *Design Pattern* DTO foi usado tanto nas classes *Users* como *Transfer*.
#### Na entidade **Users**, usamos um **DTO na Request** não só para que não seja necessário informar o campo *id* nas requisições, mas para realizar um tratamento na string do CPF/CNPJ.
```java
.setCpfCnpj(this.cpfCnpj.replaceAll("[^0-9]+", ""))
```
- Foi usado um Regex para remover todos os caracteres que não sejam dígitos. Dessa forma, nas requisições poderão ser fornecidas tanto um documento só com os números(76601728000130), como também com pontuação(76.601.728/0001-30).

- Além disso, foi usado o **DTO na Response** para que, por motivos de segurança, não sejam apresentados os campos **senha** e **walletAmount** ao realizar as operações *GET*.

#### Na entidade **Transfer**, foi usado somente um DTO na request para que não fosse necessário informar o campo *Id*.
---

## **1.2 - Controllers**

#### Camada responsável por orquestrar o fluxo da aplicação ao fazer o mapeamento e o direcionamento dos *requests* para a camada de serviços.

### **1.2.1 - UsersController**
#### Esse controller chama os métodos da camada de serviços **usersService**. É aqui que ocorrem as operações de cadastrar, buscar, alterar e deletar usuários; bem como E Para reduzirmos o acoplamento, utilizamos a anotação **@Autowired** para realizarmos a *injeção de dependência*.
![Users controllers](https://github.com/Vandeilsonln/Wallet_Transfer/blob/master/_images/user_controller.PNG?raw=true)

### **1.2.2 - TransferController**
#### Esse controller faz o registro de todas as transferências bem sucedidas, bem como a consulta das mesmas.
---

## **1.3 - Service**
#### Camada onde serão inseridas as regras de negócio, e onde é feita a injeção do repository para que seja possível a chamada dos métodos que farão as persistências no banco de dados.

### **1.3.1 - Regras de negócio - Criação de usuário**
#### Só é possível cadastrar um usuário, se as regras abaixo forem seguidas:
- Não pode haver dois usuários com o mesmo email ou CPF/CNPJ;
- O número do documento deverá ser válido, sendo essa função atribuída ao **Hibernate Validator**;
- Não será possível cadastrar um CPF com uma pessoa jurídica nem um CNPJ com pessoa física.
---

### **1.3.2 - Regras de negócio - Transferências**
#### O core da aplicação é a transferência de dinheiro entre usuários, e para garantir que isso ocorra da maneira correta, existem algumas regras que devem ser respeitadas.
- Não é possível enviar dinheiro para si mesmo.
- Pessoas físicas (comuns) podem transferir tanto para outras pessoas físicas como para jurídicas (lojistas).
- Lojistas não podem enviar dinheiro, somente receber.
- A transação deverá ser aprovada por um autorizador externo.
- Em caso de qualquer falha, a operação deverá ser revertida (conceito de transactions).
![regras de transferência](https://github.com/Vandeilsonln/Wallet_Transfer/blob/master/_images/transactions.PNG?raw=true)
---
## **1.4 - Repository**
#### Nesta camada criamos uma interface que extende a interface *JpaRepository* do *Spring Data JPA*. É através dela que iremos usar a camada de persistência para gravar e recuperar dados, fazendo uma ponte com o banco de dados.
### **1.4.1 - UsersRepository**
#### Foram criados mais dois métodos para buscar no banco de dados.

 ```java
Optional<Users> findByEmail(String email);
Optional<Users> findByCpfCnpj(String CpfCnpj);
```
---
### **1.4.2 - TransferRepository**
#### Foi criado um método que retorna uma lista de todas as transferências de um dado usuário, usando a anotação **@Query** para realizar uma query nativa.
```java
@Query(value = "SELECT * FROM transfers WHERE id_payer = ?1", nativeQuery = true)
    List<Transfer> getAllTransferById(Long id_payer);
```
---

## **:hammer_and_wrench: 2 - Configuração dos Profiles**

#### Foram configurados **2 profiles** para podermos testar a aplicação. 
![profiles](https://github.com/Vandeilsonln/Wallet_Transfer/blob/master/_images/profiles.PNG?raw=true)

 - O profile "**prod**" vai subir a aplicação com o banco de dados **MySQL**. O código para a criação de tudo será fornecido logo abaixo! :relaxed:
 - O profile "**dev**" vai subir a aplicação com o banco **H2 *in-memory***. Dessa forma, é possível testar os métodos sem inteferir no banco de produção, além de não ser necessário nenhum software adicional instalado. Além do mais, a aplicação poderá ser testada através da plataforma em nuvem **Heroku**.
---

#### A forma de escolher qual o profile será o ativo na hora de rodar a aplicação é através do aquivo **application.properties**. Por padrão, o profile ativo será o **dev**. Caso queira rodar a aplicação com o *MySQL*, basta trocar o valor para "**prod**".
```properties
spring.profiles.active=dev
```
### **2.1 - Rodando com o profile *"dev" (H2)***
<details>
<summary>Clique aqui para visualizar a configuração</summary>

#### Subindo a aplicação com esse profile, basta acessar o arquivo **application-dev.properties** e certificar que os atributos estão como abaixo abaixo:
```properties
# H2 Configuration (in-memory)
spring.h2.console.enabled=true
spring.h2.console.path=/h2

spring.datasource.url=jdbc:h2:mem:walletDb
spring.datasource.username=sa
spring.datasource.password=
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.hibernate.ddl-auto=update
```
</details>

---
### **2.2 - Rodando com o profile "*prod*" (MySQL)**
<details>

<summary>Clique para visualizar a configuração</summary>

#### É necessário a **construção das tabelas no MySQL**. A partir delas poderá ser feito o mapeamento com o Hibernate/JPA.
#### Deve-se, portanto, criar uma database com o nome "**walletdb**", e dentro dela criar as tabelas"**usuarios**" e "**transfers**"
![Tabelas MySQL](https://github.com/Vandeilsonln/Wallet_Transfer/blob/master/_images/tabelas_mysql.png?raw=true)


#### Segue abaixo o código SQL para criação da database e das tabelas:

```sql
CREATE database walletdb;
use walletdb;

CREATE TABLE IF NOT EXISTS usuarios (
id INT AUTO_INCREMENT,
nome_completo varchar(80) not null,
cpf_cnpj varchar(14) not null,
email varchar(150) not null,
senha varchar(24) not null,
wallet_amount decimal(8,2),
user_type varchar(8) not null,

CONSTRAINT usuarios_id_pk primary key(id),
CONSTRAINT usuarios_type CHECK(user_type in ('fisica', 'juridica'))
);

CREATE TABLE IF NOT EXISTS transfers (
id INT auto_increment,
id_payer int not null,
id_payee int not null,
amount decimal(8,2) not null,

CONSTRAINT transfers_id_pk primary key(id),
CONSTRAINT id_payer_fk FOREIGN KEY (id_payer) references usuarios(id),
CONSTRAINT id_payee_fk foreign key (id_payee) references usuarios(id)
);
```

<details>

 <summary>Opcionalmente, podemos popular a tabela "usuarios" com alguns registros, com o código abaixo: (Clique para Expandir)</summary>

```sql
INSERT INTO usuarios (nome_completo, cpf_cnpj, email, senha, wallet_amount, user_type) 
VALUES('Thiago Francisco Nunes', '71048762009', 'thiagonunes-75@yogoothies.com.br', 'PR1UusCYBE', 600, 'fisica');

INSERT INTO usuarios (nome_completo, cpf_cnpj, email, senha, wallet_amount, user_type) 
VALUES('Giovana e Joaquim Eletrônica', '57027737000115', 'posvenda@gioquimeletronica.com.br', '6GExHYnOk1', 2700, 'juridica');

INSERT INTO usuarios (nome_completo, cpf_cnpj, email, senha, wallet_amount, user_type) 
VALUES('Sandra Ana Jesus', '61371171629', 'sandra.jesus@email.com.br', 'n9B7wJEfWs', 1250, 'fisica');

INSERT INTO usuarios (nome_completo, cpf_cnpj, email, senha, wallet_amount, user_type) 
VALUES('Malu Entregas Expressas ME', '03801098000174', 'comunicacoes@maluexpress.com.br', 'JF11701fJyX', 5000, 'juridica');
```
</details>

---
#### Uma vez criado a database, devemos configurá-lo no arquivo "**application-prod.properties**", tomando os devidos cuidados com os atributos de ***url*, *username* e *password***, para que a conexão aconteça corretamente.

```properties
# MySQL configuration
spring.datasource.url=jdbc:mysql://localhost:3306/walletdb?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=admin
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.MySQL5Dialect
```
</details>

---
## :exclamation: **3 - Testes Unitários**
### **3.1 - Testando a camada Service**
#### Os testes na camada de serviço foram feitos utilizando as libs **Junit** e **Mockito**
#### Utilizamos a anotação **@ExtendWith(MockitoExtension.class)** uma vez que usamos o Junit 5.
![Testes do Service](https://github.com/Vandeilsonln/Wallet_Transfer/blob/master/_images/testeservice1.PNG?raw=true)

#### Para que os testes funcionem, é preciso usar as anotações **@Mock** e **@InjectMocks**. Dessa forma, cria-se um mock do *UserRepository*, o qual é injetado na instância criada de *UserService* pelo @InjectMocks.
---
#### O teste *"givenCpfWithPunctuationThenShouldFormatToOnlyNumbers()"* prevê que quando um usuário seja cadastrado com o CPF com pontos e traços, o documento seja formatado e salvo no banco com somente números.
```java
@Test
    public void givenCpfWithPunctuationThenShouldFormatToOnlyNumbers() throws ExecutionException {

        UsersRequestDTO user = new UsersRequestDTO("Nobre","111.619.170-98",
                "emailjuridica@email.com.br", "456def", 1000f, UsersTiposEnums.fisica);
        
        when(usersRepository.save(user.toModel())).thenReturn(new Users(1L, "Vandeilson",
                "11161917098", "email@email.com.br", "123abc",
                1000f, UsersTiposEnums.fisica));
        
        assertEquals("11161917098", usersService.registerNewUser(user.toModel()).getCpfCnpj());

    }
```
---
#### O teste *"whenGetAllUsersShouldReturnAListOfUsers()"* confere se ao realizar uma busca por todos os usuários (que na simulação são 2) é retornado uma lista cujo tamanho é de 2. 
```java
    @Test
    public void whenGetAllUsersShouldReturnAListOfUsers(){

        when(usersRepository.findAll()).thenReturn(Stream
                .of(new Users(1L, "Vandeilson","42183918829", "email@email.com.br", "123abc", 1000f, UsersTiposEnums.fisica),
                    new Users(2L, "Nobre","74343980000161", "emailjuridica@email.com.br", "456def", 1000f, UsersTiposEnums.juridica))
                .collect(Collectors.toList()));
        
        assertEquals(2,usersService.getAll().size());
```
---
### **3.2 - Testando a camada Controller**
#### Para execução dos testes nos controllers foram usados **Junit** e **Rest Assured**.
![Testes do Controller](https://github.com/Vandeilsonln/Wallet_Transfer/blob/master/_images/testecontroller1.PNG?raw=true)
#### Utilizamos a anotação **@WebMvcTest** em conjunto com o **@BeforeEach** para que, no momento da execução dos testes, seja subido somente os beans declarados. Isso resulta em um ganho de perfomance, já que não será carregado todo o contexto da aplicação.

#### O teste *shouldReturnSuccessWhenGetUserById()* testa se o Status Code retornado ao buscar um usuário por ID é o "200".

```java
@Test
    public void shouldReturnSuccessWhenGetUserById() throws ExecutionException {

        Mockito.when(this.usersService.getById(1L))
                .thenReturn(Optional.of(new Users(1L, "Vandeilson", "11161917098",
                        "email@email.com.br", "123abc", 1000f, UsersTiposEnums.fisica)));

        given()
                .accept(ContentType.JSON)
        .when()
                .get("api/v1/users/byId/{id}", 1L)
        .then()
                .statusCode(HttpStatus.OK.value())
                .log().all();
    }
```
---
#### O método *shouldReturnNoContentWhenDeleteUser()* testa se o Status code ao deletar um usuário é o "204".
```java
    @Test
    public void shouldReturnNoContentWhenDeleteUser(){
        given()
                .accept(ContentType.JSON)
        .when()
                .delete("api/v1/users/delete/{id}", 1L)
        .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .log().all();
    }
```
---

## :books: **4 - Documentação**
#### A API foi documentada com o Swagger através de anotações nos códigos dos Controllers.
##### Anotações da UsersControllers:
![Anotações da classe de controllers](https://github.com/Vandeilsonln/Wallet_Transfer/blob/master/_images/swagger1.PNG?raw=true)

##### Anotações da Transfercontrollers:
![Anotações da classe de controllers](https://github.com/Vandeilsonln/Wallet_Transfer/blob/master/_images/swagger2.PNG?raw=true)

#### E podemos ver todos os endpoints com suas respectivas anotações pela interface gráfica do Swagger.
![Swagger UI](https://github.com/Vandeilsonln/Wallet_Transfer/blob/master/_images/swagger3.PNG?raw=true)


## **AuthrizePayment - Rest Template**