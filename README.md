# mixpanel_push
Mixpanel push is an example in java of how to push events to Mixpanel.

Prerequisites: java 1.8

Instructions

Get the repository from github:
git clone https://github.com/asunwoo/mixpanel_push.git

Build the project

In the project root folder execute:
mvn clean package

From the project folder root execute:
java -classpath target/mixpanel_post-1.0.jar com.mixpanel.events.PushEvent <mixpanel token>

Note: The command line argument <mixpanel token> specifies your Mixpanel project token

example:
java -classpath target/mixpanel_post-1.0.jar com.mixpanel.events.PushEvent e9d8dc0d6d2fa8665d8f28643cd77cb2
