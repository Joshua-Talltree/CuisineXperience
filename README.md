# CuisineXperience

CuisineXperience is not your typical social media website because it focuses on food and the users food experience. Visitors can create an account, create posts, add their favorite food photos, add friends to their network, create a food group, block other users, unblock other users, like posts, comment on posts, update posts, add their posts to categories, and receive an email when you receive a friend request. CuisineXperience is a fully functional, end-to-end CRUD full-stack website, built with Java, SpringBoot, Thymeleaf, jQuery, Bootstrap, MySQL, CSS, and Filestack API.

- Bootstrap and Custom CSS
- Spring Boot 2.1.x
- Hibernate ORM + JPA
- Thymeleaf
- MySQL
- Libraries and Utilities Applied
- jQuery link
- Filestack API

# How to get this app working
- Clone this repo locally.
- Create an application.properties file with valid credentials for a MySQL connection, and a personal Filestack API key because it requires a personal API key. use the application.properties.example as a template.

# Examples of CuisineXperience complexity

In this project you will find some examples for:

- Several complex JPA joinTable relationship where users can send requests to user in order to establish a friendship or be invited by another user to become friend, as well as adding posts that are part of a category.
- A small Enum class that contains a list of friendship statuses. 