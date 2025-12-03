# Lambda Consumer Kafka - Módulo 3

Aplicação Spring Boot para consumo de mensagens Kafka implementada seguindo princípios de Clean Architecture e SOLID.

## Visão Geral

Esta aplicação atua como um consumidor Kafka que processa mensagens em tempo real, aplicando transformações e registrando eventos no console. O projeto demonstra a aplicação prática de padrões arquiteturais modernos em um contexto de processamento de eventos.

## Arquitetura

### Clean Architecture

O projeto está organizado em camadas bem definidas, respeitando o princípio de dependência unidirecional (de fora para dentro):

**1. Domain (Camada de Domínio)**
- Contém as entidades e regras de negócio fundamentais
- Independente de frameworks e tecnologias externas
- Classe: `EventMessage` - representa a estrutura de um evento processado

**2. Application (Camada de Aplicação)**
- Contém os casos de uso e lógica de aplicação
- Orquestra o fluxo de dados entre as camadas
- Classe: `EventProcessingService` - processa e transforma mensagens recebidas

**3. Infrastructure (Camada de Infraestrutura)**
- Implementa detalhes técnicos e integrações externas
- Configurações de frameworks e bibliotecas
- Classe: `KafkaConsumerConfig` - configuração do consumidor Kafka

**4. API (Camada de Interface)**
- Ponto de entrada para eventos externos
- Adaptadores que conectam o mundo externo à aplicação
- Classe: `KafkaEventListener` - listener que recebe mensagens do Kafka

### Princípios SOLID Aplicados

**Single Responsibility Principle (SRP)**
- Cada classe tem uma única responsabilidade bem definida
- `KafkaEventListener`: apenas escuta mensagens do Kafka
- `EventProcessingService`: apenas processa a lógica de negócio
- `KafkaConsumerConfig`: apenas configura o consumidor Kafka

**Open/Closed Principle (OCP)**
- O código está aberto para extensão mas fechado para modificação
- Novos tipos de processamento podem ser adicionados sem alterar código existente
- O método `detectType()` pode ser estendido para novos formatos

**Liskov Substitution Principle (LSP)**
- Uso de interfaces do Spring Framework garante substituibilidade
- Componentes podem ser substituídos por implementações alternativas

**Interface Segregation Principle (ISP)**
- Classes dependem apenas das interfaces que realmente utilizam
- Sem dependências desnecessárias entre componentes

**Dependency Inversion Principle (DIP)**
- Dependências apontam para abstrações, não implementações concretas
- Injeção de dependências via construtor com `@RequiredArgsConstructor`
- Facilita testes e manutenção

## Estrutura do Projeto
```
src/main/java/com/projetofinal/nair/lambda_consumer/
├── api/
│   └── listener/
│       └── KafkaEventListener.java          # Listener de mensagens Kafka
├── application/
│   └── service/
│       └── EventProcessingService.java      # Serviço de processamento
├── domain/
│   └── model/
│       └── EventMessage.java                # Entidade de domínio
├── infrastructure/
│   └── kafka/
│       └── KafkaConsumerConfig.java         # Configuração Kafka
└── LambdaConsumerApplication.java           # Classe principal
```

## Componentes Principais

### KafkaEventListener

Componente responsável por escutar mensagens de um tópico Kafka configurado.

**Configuração:**
- Tópico: definido em `app.kafka.topic`
- Grupo de consumidores: definido em `spring.kafka.consumer.group-id`

**Funcionalidade:**
- Recebe mensagens do Kafka de forma assíncrona
- Delega o processamento para `EventProcessingService`

### EventProcessingService

Serviço que contém a lógica de processamento das mensagens.

**Funcionalidades:**
- Cria objetos `EventMessage` a partir das mensagens brutas
- Detecta automaticamente o tipo da mensagem (JSON ou texto plano)
- Registra eventos processados no console

**Tipos de Mensagem Detectados:**
- `JSON_GUEST`: mensagens que começam com `{` e terminam com `}`
- `PLAIN_TEXT`: mensagens em formato texto simples

### EventMessage

Modelo de domínio que representa um evento processado.

**Atributos:**
- `message`: conteúdo da mensagem
- `source`: origem da mensagem (ex: "kafka-cli")
- `type`: tipo detectado da mensagem
- `timestamp`: momento do processamento

### KafkaConsumerConfig

Configuração do consumidor Kafka com as seguintes características:

**Configurações:**
- Deserialização de chave e valor como String
- Auto offset reset: `earliest` (consome desde o início do tópico)
- Bootstrap servers: configurável via properties

## Configuração

### application.properties
```properties
spring.application.name=lambda-consumer-nair
spring.kafka.bootstrap-servers=nair-mod3-kafka:9092
spring.kafka.consumer.group-id=nair-mod3-lambda-group
spring.kafka.consumer.auto-offset-reset=earliest
app.kafka.topic=nair-mod3-topic
```

### Docker Compose

O projeto inclui um ambiente completo com Zookeeper, Kafka e a aplicação consumidora:

**Serviços:**
- `nair-mod3-zookeeper`: coordenação do cluster Kafka (porta 22181)
- `nair-mod3-kafka`: broker Kafka (porta 29092)
- `nair-mod3-lambda-consumer`: aplicação consumidora

## Execução

### Pré-requisitos

- Java 21
- Maven 3.9+
- Docker e Docker Compose

### Executar com Docker Compose
```bash
docker-compose up -d
```

### Enviar Mensagem de Teste
```bash
docker exec -it nair_mod3_kafka kafka-console-producer \
  --broker-list localhost:9092 \
  --topic nair-mod3-topic
```

Após executar o comando, digite suas mensagens e pressione Enter.

### Visualizar Logs
```bash
docker logs -f nair_mod3_lambda_consumer
```

### Build Local
```bash
./mvnw clean package
```

## CI/CD

O projeto inclui um workflow GitHub Actions que:

1. Compila o JAR com Maven
2. Faz build da imagem Docker
3. Publica a imagem no Docker Hub

**Trigger:** push nas branches `main` ou `master`

## Tecnologias Utilizadas

- Java 21
- Spring Boot 3.3.5
- Spring Kafka
- Lombok
- Apache Kafka 7.7.0
- Maven
- Docker

## Benefícios da Arquitetura

**Manutenibilidade**
- Código organizado e fácil de entender
- Mudanças isoladas em camadas específicas

**Testabilidade**
- Componentes desacoplados facilitam testes unitários
- Dependências injetadas permitem uso de mocks

**Escalabilidade**
- Arquitetura preparada para crescimento
- Fácil adição de novos casos de uso

**Flexibilidade**
- Troca de tecnologias sem impacto no domínio
- Adaptação simples a novos requisitos
