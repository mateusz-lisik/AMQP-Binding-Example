AMQP Binding Example
--------------------

In this little project I would like you to show how to operate on AMQP and how to notify application about events
that you received via it. For this purpose we will listen for certain events in given exchange and operate them
from single thread with single consumer code. Rest of the logic will be operated by appropriate event logic.


I decided to go with event driven approach since it is the clearest one for this purpose and I strongly recommend
using events in your applications as tight coupling means a lot of trouble.

## Build
This is gradle project, so you need to `gradle build` it.<br>
**Remember** to set RabbitMQ Connection details in `ApplicationSample.java`