## API Wallet Transfer - Desafio Backend

#### Esta API realiza as operações CRUD de usuários, bem como a transferência de dinheiro entre eles.

### :computer: **Tecnologias usadas**

- Apache Maven
- Java 11
- Spring Boot
- Spring Data JPA
- Hibernate
- Banco H2 e MySQL
- Junit / Mockito / Rest Assured

## :building_construction: **1 - Estrutura do projeto**

![Estrutura do projeto](https://github.com/Vandeilsonln/Wallet_Transfer/blob/master/_images/estrutura_projeto.PNG?raw=true)

### **1.1 - Model**

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
