# Chat
#### Chat web application powered by Spring framework and WebSocket.

## Features
* WebSocket chat
* Back-end REST API for bots purposes
* Automatic user avatar generation
* Login by Google OAuth2

## Technologies
Back-end:
* Spring framework
* Thymeleaf
* Assembly by Maven

Front-end:
* UIkit
* jQuery, sockJS, STOMP
* Assembly by WebPack

## System requirements

* Java 8
* Hibernate compatible relational database (_PostgreSQL_ recommended)
* Maven for building

## Getting started
1. `git clone https://github.com/D3lph1/Chat.git`

### Web
You should configure _/web/src/resources/application.properties_ file
by setting database connection options and google oauth2 credentials.

Optionally, if you do not want to create bot manually (it achieves by
registering user on _/signup_ page and set database field _bot_ for him
in _users_ table) you can import table dumps _/database/users.sql_ and
_/database/messages.sql_ into database. To ensure bot avatar representation
copy _/avatars_ folder to built application location.

Build "web" application:

`cd web`

`mvn clean install`

### Bot
Find _/bot/src/main/resources/bot.properties_ file and set required
settings.

Export dump file _/database/posts.sql_ into database.

Build "bot" application:

`cd bot`

`mvn clean install`

## Screenshots
![Login](https://i.ibb.co/zPSZ5Tf/login.png)

![Chat](https://i.ibb.co/9NCnxQZ/chat.png)


## License

The Chat is open-sourced software licensed under the MIT license.
