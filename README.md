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
⚠️ <b>Docker</b> must be running before executing <b>KafkaContainer</b> under <code>TestSmodulithApplication</code> when running <code>Maven tests</code> (<code>EventPublishRegistryTests</code>).