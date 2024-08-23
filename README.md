# smodulith
Srping Modulith compendium

---
### MVC vs. DDD
Legacy organization compared the Clean approach when project grows in size and complexity, i.e. adding business rules and processes:
![Screenshot](https://github.com/paguerre3/smodulith/blob/main/img/01-cohesion-vs-coupling.png?raw=true)

---
### Modulith
<code>var modules = ApplicationModules.of(App.class);
<pr></pr>
System.out.println(modules);
<pr></pr>
modules.verify();</code>
Each module shows dependencies and visibility of beans (services, entities, record values, repos, aggregators, factories, etc). 
<b>"+"</b> sign shows public beans while <b>"0"</b> displays private ones not visible for other modules.

