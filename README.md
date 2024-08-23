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
<b>"+"</b> sign shows public beans while <b>"0"</b> displays private ones not visible for other modules. 
<pre><code>
var modules = ApplicationModules.of(App.class);
System.out.println(modules);
modules.verify();
</code></pre>


