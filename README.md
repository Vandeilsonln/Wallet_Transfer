# **API Wallet Transfer - Desafio Backend**

#### Esta API realiza as operações CRUD de usuários, bem como a transferência de dinheiro entre eles.

### :computer: **Tecnologias usadas**

- Apache Maven
- Java 11
- Spring Boot
- Spring Data JPA
- Hibernate
- Banco H2 e MySQL
- Junit / Mockito / Rest Assured

---
## :building_construction: **1 - Estrutura do projeto**

![Estrutura do projeto](https://github.com/Vandeilsonln/Wallet_Transfer/blob/master/_images/estrutura_projeto.PNG?raw=true)

## **1.1 - Model**

#### Camada responsável por fazer o **ORM** *(Object Relational Mapper)*, fazendo uma ponte entre uma classe no Java com o modelo relacional do banco de dados.

### **1.1.1 - Users**

#### Entidade que representa os usuários da aplicação. Os mesmos podem optar por se cadastrarem como pessoa *física* ou *jurídica*, desde que obedecidos os critérios de validação, que são:
- Não pode haver dois usuários com o mesmo email ou CPF/CNPJ;
- O número do documento deverá ser válido;
- Não será possível cadastrar um CPF com uma pessoa jurídica e vice-versa, mesmo que o documento esteja com número validado.

![Estrutura do projeto](https://github.com/Vandeilsonln/Wallet_Transfer/blob/master/_images/user_entity.PNG?raw=true)

#### Foram usadas as seguintes anotações para auxiliar no desenvolvimento:
- **@Data**: Anotação do *Lombok* que se encarrega de criar os *getters* e *setters* em tempo de compilação.
- **@Accessors(chain=true)**: Anotação do *Lombok* que faz os *setters* retornarem *this* ao invés de *void*.
- **@GroupSequenceProvider**: Uma vez que temos um atributo onde pode ser validado tanto o CPF como o CNPJ, precisamos criar grupos para informar ao hibernate examente qual validação será aplicada, para que não haja conflitos ou que uma validação tenha preferência sobre a outra.
- **@AllArgsConstructor / @NoArgsConstructor**: Criam, respectivamente, construtores com todos os argumentos; e sem argumento algum.

### **1.1.2 - Transfer**

#### Entidade que representa as transferências da aplicação. Elas são instanciadas a partir do momento que uma transferência for bem sucedida e contém somente os IDs de quem enviou o dinheiro, de quem recebeu e do valor transferido.
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
![Estrutura do projeto](https://github.com/Vandeilsonln/Wallet_Transfer/blob/master/_images/user_controller.PNG?raw=true)

### **1.2.2 - TransferController**
#### Esse controller faz o registro de todas as transferências bem sucedidas, bem como a consulta das mesmas.
---
