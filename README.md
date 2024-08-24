# smodulith
Srping Modulith compendium

---
### MVC vs. DDD
Legacy organization compared the Clean approach when project grows in size and complexity, i.e. adding business rules and processes:
![Screenshot](https://github.com/paguerre3/smodulith/blob/main/img/01-cohesion-vs-coupling.png?raw=true)

---
### Modulith
Using Modulith each package that belongs to the root one is being treated as a different Module, e.g. <code>customer, inventory and order</code>. 
In the following example of Modulith testing code, each module shows dependencies and visibility of beans (services, entities, record values, repos, aggregators, factories, etc.).
<b>"+"</b> sign shows public beans while <b>"O"</b> displays private ones not visible for other modules. 
<pre><code>
var modules = ApplicationModules.of(App.class);
System.out.println(modules);
modules.verify();
</code></pre>
At the end, the validation checks that parts of one module that shouldn't be access to the other Modules are valid according to a Clean approach.
<pre><code>

# Inventory
> Logical name: inventory
> Base package: org.smodulith.inventory
> Direct module dependencies: none
> Spring beans:
  + ….Inventory
  o ….InventoryRepository
  o ….InventorySettings

# Customer
> Logical name: customer
> Base package: org.smodulith.customer
> Direct module dependencies: none
> Spring beans: none

# Order
> Logical name: order
> Base package: org.smodulith.order
> Direct module dependencies: none
> Spring beans:
  + ….OrderManagement
  + ….OrderRepository
</code></pre>

---
***Features and some implementation Notes***
- Whenever we "complete" an <b>Order</b> we want to update the <b>Inventory</b>, i.e. connecting <code>OrderManagement</code> with <code>Inventory</code> using Spring IoC -Injecting <code>Inventory</code> inside <code>OrderManagement</code> is shown as a Direct dependency of the Order Module.
- ⚠️ <b>Only the "Top Level" package of the Module is considered an API package where all it's components are Visible by Modulith for a DDD approach alignment</b> therefore if <code>InventoryRepository</code> is inside <code>Inventory</code> and has public access Then is visible inside <b>Order</b> Module so it's recommended to reduce visibility (class of package access) OR if we add a sub-package that belongs to a Module like <b>infrastructure</b> then all repositories inside that are considered Not Visible with "0" sign by Modulith, i.e. <b>all beans and value records inside Sub-Packages of a module like infrastructure, presentation, application and domain are "Not Visible" by default</b>.
- ⚠️ <code>@ApplicationModuleTest</code> inside <code>InventoryIntegrationTests of inventory package</code> is useful in <b>loading/bootstrapping only the components that belong to the Module</b>, i.e. it treats/runs Modules isolated like following the DDD approach (instead of loading the entire application). 
- When using <code>@ApplicationModuleTest</code> annotation if there are dependencies that belong to other Modules it's required to add <code>@MockBean</code>, e.g. Inventory inside Order module then we need to <code>@MockBean Inventory</code> so the dependency is present in the Modulith tests as required, OR simply add it as <code>@ApplicationModuleTest(mode=BootstrapMode=ALL_DEPENDENCIES)</code> so all required dependencies are being added by default without the need of adding them individually.  
- Instead of using a <code>@Transaction</code> block inside <code>Order.complete()</code> method that "conglomerates" all actions like Rewards and Sending e-mail doing everything in Sequence which will be a "blocking sequence" is proposed a <b>Domain Driven Events</b> technique which will be more performant and will have no dependencies, e.g. <b>we don't need to add Inventory code inside OrderManagement</b> and instead "register" the <code>OrderCompleted</code> event.
  ![Screenshot](https://github.com/paguerre3/smodulith/blob/main/img/02-transaction-as-a-blocking-piece.png?raw=true)
- ⚠️ Modulith provides an application <b>Event Publication Registry</b> so if in the middle of the execution of an Event the application gets shutdown then, once reloaded, the Event can be recovered.
- Documentation, Actuator and observability support.
- ⚠️ Automatic externalization of events into Kafka, AMQP, potentially Redis.
- Run <b>actuator profile</b>: <code>mvn clean install -Pactuator</code>.
- <b>Kafka Containers</b> inside Spring is being used instead of using docker-compose with Kafka setup.

---
### Requirement
1. ⚠️<b>Docker</b> must be running before executing <code>Maven tests</code>.
2. <b>Alt A </b>: <b>KafkaContainer</b> under <code>TestSmodulithApplication</code> needs docker for running <code>Maven tests</code> -<code>EventPublishRegistryTests</code> must uncomment @Import including the Test Configuration that loads Kafka container.
3. <b>Alt B (default)</b>: <code>docker-compose -f kafka.yml up -d</code> before running tests.

---
### Drawbacks of Microservices vs. Modular Monolith
***MSA drawback***: 
- Distributes components are more difficult to manage.
- It requires a lot of infrastructure.
- Refactor APIs is done ofter when boundary changes and it becomes hard to Maintain.
![Screenshot](https://github.com/paguerre3/smodulith/blob/main/img/03-modular-monolith.png?raw=true)
***Modular Monolith***: 
- <b>Fault tolerance</b> in the Monolith means that if the application goes down then the affectation isn't at a single process level/isolated module, i.e. affecting "all" modules included. 
- <b>Selective scaling</b> of only one Module affected isn't possible.
- Easier refactor of boundaries when APIs changes as it belongs to single repository.
- <b>Jmolecules</b> annotations help Modulith in terms of providing <b>Domain Driven Design Annotations</b> linked to the architecture instead of technical annotations.
- <b>DDD documentation renders</b> in PlantUML format generated dynamically in "target" directory -see test <code>ModularityTests.renderDocumentation</code>.
- <b>zipkin</b> for tracing asynchronous processes of a common transaction, similar to newrelic, taking the total time when the event is being registered and the time that it takes for all the "listeners" to complete the transaction process linked ("everything being associated to a single traceable ID"), e.g. OrderComplete event publish is listened by Inventory and Rewards updates (listeners) which are processes that run in parallel associated to the same transaction ID.  
![Screenshot](https://github.com/paguerre3/smodulith/blob/main/img/04-zipkin.png?raw=true)
- <b>Externalization</b> of events into Kafka, AMQP, potentially Redis using annotation <code>@Externalized</code> makes it easy to transition into an "external messaging" system and allows communication with external services.
- Legacy and Modularized can coexist using annotation <cod>@ApplicationModule(type=Type.OPEN)</code>.
![Screenshot](https://github.com/paguerre3/smodulith/blob/main/img/05-legacy-and-modulith.png?raw=true)
- Annotation ATM <code>@NamedInterface</code> is being used to <b>Expose</b> single java components or modules via package-info.java that are supposed to be internal by definition, but we want to pass Modulith verifications, e.g. <b>Domain Event of type Records</b> registered under an <b>AggregateRoot</b> of a Domain layer package of a Module (not top-level package) that are being listened by an <code>ApplicationModuleListener</code> located in another Module under the Application Layer a.k.a. <code>DomainEventListener</code> service. 
- Annotation <code>@Modulithic(sharedModules="core")</code> added into the Main Spring Boot application class means that every isolated module when is loaded will also load "core" module, i.e. core is a shared module among all existent modules.


---
### Further samples
***Forked Repository***
- [Branch with Monolith and internal Events in details with Zipkin (observability profile)](https://github.com/paguerre3/spring-restbucks/tree/demos/cora/server).
- [Branch with Monolith and messages externalized into Kafka](https://github.com/paguerre3/spring-restbucks/tree/demos/cora-externalized/server). 
- [Branch having a mix of Legacy and Monolith approach](https://github.com/paguerre3/spring-restbucks/tree/demos/cora-legacy/server).