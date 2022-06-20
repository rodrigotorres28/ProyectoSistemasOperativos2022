El formato y la explicación de como modificar entradas se encuentra detallado en el informe, el siguiente es un extracto del mismo:

El formato de “Comercios.csv”, es el siguiente, en la segunda columna de la primera fila, se especifica el número de repartidores que tendremos en nuestra simulación. En el
resto de las filas, contendrán en la columna 1 el nombre de los comercios de nuestra simulación, no hay mayor restricciones para estos, pero aquellos nombres que se
especifiquen aquí, deberán coincidir con los nombres que se ingresen en la siguiente entrada.
 
En el archivo de entrada “Pedidos.csv”, el formato será el siguiente: en la primera columna, se ingresará el ID del pedido, de manera decreciente, en la segunda columna, un
nombre de comercio que coincida con alguno de los comercios especificados en la entrada anterior (“Comercios.csv”). En la tercera columna irá el tipo de comercio, el cual debe 
ser uno de los siguientes tres valores: “restaurante”, “farmacia”o “almacen”. En la cuarta columna irá el tiempo de elaboración, el cual será “0”, si el tipo de comercio es 
“farmacia” o “almacen”, o un valor entre 10 y 45, si el tipo de comercio es “restaurante. En la quinta columna irá la distancia al cliente, la cual tendrá un valor entre 5 
y 20. Por último, en la sexta columna irá el minuto de ingreso, cuyos valores pueden repetirse, pero deben estar ordenados decrecientemente. Es por esto que, para crear pedidos 
manualmente, se recomienda empezar desde abajo hacia arriba en el archivo de entrada. Otra opción es generarlo automáticamente utilizando el script provisto de python el cual 
genera 1000 pedidos aleatorios, en los que 8 de cada 10 son restaurantes, la elaboración y la distancia son aleatorias y el minuto de ingreso es entre 0 a 5 minutos mayor al 
anterior de manera aleatoria. El archivo se llama “GeneradorDePedidosRandom.py” 