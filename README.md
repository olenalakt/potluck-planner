# potluck-planner

Diagram: Potluck Planner.png

Potluck Planner (Min Viable Product) requirements:
1) user registration/authorization;
2) guest authorization (TODO);
3) registered user can:
  - create/modify/delete a potluck event: eventname is unique ;
  - add/remove guests: required guest info - guest name and guest email(unique);
  - review the event, guests status and menu for the event;
4) guest can:
 - add/modify/delete dishes he plans to bring;
 - add/modify/delete drinks he plans to bring;
 - review the event and menu;
5) all services can be performed/verified via Postman scripts;

Potluck Planner components:
- client SPA - sample, only allows to authenticate the user and list events;
- user microservice - (Springboot app) (TODO -  implement security);
- oauth-server -  allows to  authenticate the user and issue JWT token, all services configured to use the same token 
- API gateway (Springboot app);
- event microservice (Springboot app);
- guest microservice (Springboot app);
- dish microservice (Springboot app);
- drink microservice (Springboot app);
- menu(CQRS) microservice (Springboot app);
- event cleanup microservice (Springboot app - kafka consumer, msg produced by user service when user is deleted);
- guest cleanup microservice (Springboot app - kafka consumer, msg produced by user service when user is deleted);
- dish cleanup microservice (Springboot app - kafka consumer, msg produced by user service when user is deleted);
- drink cleanup microservice (Springboot app - kafka consumer, msg produced by user service when user is deleted);
- menu cleanup microservice (Springboot app - kafka consumer, msg produced by user service when user is deleted);


