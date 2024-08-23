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


---
***Implementation Notes***
- Whenever we "complete" an <b>Order</b> we want to update the <b>Inventory</b>, i.e. connecting <code>OrderManagement</code> with <code>Inventory</code> using Spring IoC -Injecting <code>Inventory</code> inside <code>OrderManagement</code> is shown as a Direct dependency of the Order Module.
- <b>Only the "Top Level" package of the Module is considered an API package where all it's components are Visible by Modulith</b> therefore if <code>InventoryRepository</code> is inside <code>Inventory</code> and has public access Then is visible inside <b>Order</b> Module so it's recommended to reduce visibility (class of package access) OR if we add a sub-package that belongs to a Module like <b>infrastructure</b> then all repositories inside that are considered Not Visible with "0" sign by Modulith, i.e. <b>all beans and value records inside Sub-Packages of a module like infrastructure, presentation, application and domain are "Not Visible" by default</b>.
- 