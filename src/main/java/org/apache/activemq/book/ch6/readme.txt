JAAS认证
--${ACTIVEMQ_HOME}/bin/activemq console -Djava.security.auth.login.config=src/main/resources/org/apache/activemq/book/ch6/login.config  xbean:src/main/resources/org/apache/activemq/book/ch6/activemq-jaas.xml 

D:\work\resource\amq-in-action-example-src\src\main\resources\org\apache\activemq\book\ch6\activemq-simple.xml

-Djava.security.auth.login.config=D:\work\resource\amq-in-action-example-src\src\main\resources\org\apache\activemq\book\ch6\login.config  xbean:D:\work\resource\amq-in-action-example-src\src\main\resources\org\apache\activemq\book\ch6\activemq-jaas.xml 


For Simple authentication plugin example start ActiveMQ with:
${ACTIVEMQ_HOME}/bin/activemq xbean:src/main/resources/org/apache/activemq/book/ch5/activemq-simple.xml

For JAAS authentication example start ActiveMQ with:
${ACTIVEMQ_HOME}/bin/activemq -Djava.security.auth.login.config=src/main/resources/org/apache/activemq/book/ch5/login.config xbean:src/main/resources/org/apache/activemq/book/ch5/activemq-jaas.xml

For authorization example start ActiveMQ with:
${ACTIVEMQ_HOME}/bin/activemq -Djava.security.auth.login.config=src/main/resources/org/apache/activemq/book/ch5/login.config xbean:src/main/resources/org/apache/activemq/book/ch5/activemq-authorization.xml

For message authorization policy:

1. mvn install
2. cp target/book-1.0-SNAPSHOT.jar ${ACTIVEMQ_HOME}/lib/
3. ${ACTIVEMQ_HOME}/bin/activemq xbean:src/main/resources/org/apache/activemq/book/ch5/activemq-messagepolicy.xml

For developing customized plugin:
1. mvn install
2. cp target/book-1.0-SNAPSHOT.jar ${ACTIVEMQ_HOME}/lib/
3. ${ACTIVEMQ_HOME}/bin/activemq xbean:src/main/resources/org/apache/activemq/book/ch5/activemq-custom.xml
