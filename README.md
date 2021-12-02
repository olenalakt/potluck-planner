# potluck-planner

Diagram: Potluck Planner.png

Potluck Planner (Min Viable Product) requirements:
1) user registration/authorization;
2) guest confirmation/authorization;
3) Registered user can:
  - create/modify/delete a potluck event: eventname is unique ;
  - add/remove guests: required guest info - guest name and guest email(unique);
  - review the event, guests status and menu for the event;
4) Guest can:
 - confirm availibility;
 - add/modify/delete dishes he plan to bring;
 - add/modify/delete drinks he plan to bring;
 - review the event, guests status and menu for the event;

Potluck Planner components:
- client SPA;
- API gateway (Springboot app);
- user microservice (Springboot app);
- event microservice (Springboot app);
- guest microservice (Springboot app);
- dish microservice (Springboot app);
- drink microservice (Springboot app);
- query(CQRS) microservice (Springboot app);

