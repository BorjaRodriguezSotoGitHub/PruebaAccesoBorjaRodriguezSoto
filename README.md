**Prueba de acceso**

Una empresa textil que cuenta con dos almacenes en EEUU desea gestionar de manera eficiente los envíos de su producto a los clientes. Para ello, se deberá desarrollar un software que informe a los trabajadores de dicha empresa acerca de cómo gestionar cada pedido de la manera más eficiente posible. En primer lugar, se deberá elegir la caja más pequeña posible en la que se enviará el producto teniendo en cuenta el máximo peso que es capaz de soportar y que el producto tiene que caber en la caja. Por otro lado se tendrá que elegir el almacén de envío teniendo en cuenta el precio y hora del envío. El precio depende del volumen de la caja y del precio de experiencia y la hora de salida del almacén tiene que coincidir con las horas de recogida de los transportistas. En caso de que el precio del envío sea igual desde ambos almacenes se elegirá aquel que tenga más existencias del producto. Finalmente deberá quedar reflejado el coste total de todos los envíos.

**Pasos a seguir**


1. Implementar la función `findBestBoxType`: Encuentra la caja más pequeña en la que quepa el producto a enviar.
2. Implementar la funcion `findBestRoute`: Elige el almacen desde donde se va a enviar el producto en función del coste y la hora. Para mayor facilidad se ha implementado la funcion `getDeliveryDateTime` que calcula la hora de entrega del producto desde el almacen. 

El programa obtiene los datos desde `input.txt` y genera un `output.txt` donde se encuentran los diferentes envios que se van a realizar con su correspondiente coste. El primer valor que aparece en el fichero `output.txt` es el coste total de todos los envíos.
En el proyecto aparece ya generado el fichero `output.txt` resultante a partir del `input.txt` dado.