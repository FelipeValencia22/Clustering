==================================================================================================================
Asignatura: Minería de datos.
Número de Práctica: 1.(Clustering:Kmeans)
Autores: Jose Ignacio Sánchez, Josu Rodríguez.
Fecha: 26 de octubre de 2014.
==================================================================================================================
Recursos utilizados:
	- Linux Mint 16, 17 con JDK 7 y eclipse Kepler.
	- Windows 7*, 8.1 con JDK 7 y eclipse Kepler.
	- Librería de JFreeChart 1.0.19
	- Librería de OpenCSV 2.3.

* Es posible que dé errores a la hora de ejecutar el ejecutable .jar en Windows 7.
==================================================================================================================
Objetivos:

El objetivo principal del sistema es implementar el algoritmo de clustering kmeans y la evalueación del algoritmo, utilizando 
Silhouette Coefficient como medida de desempeño.Como funcionalidad adicional cabe realizar la gráfica de la función de los resultados
obtenidos en un experimento de varias ejecuciones.

Tras agrupar las instancias, se analiza la cohesión de cada cluster y la disimilitud con el resto.


Funcionalidades adicionales implementadas (más información en la Documentación):
	- Carga de datos desde archivos con cualquier extensión.
	- Preproceso de los datos configurable.
	- Ejecución del algoritmo principal totalmente configurable, permitiendo adaptarse a los datos de la forma más adecuada o incluso realizar
	análisis paramétrico de diferentes ejecuciones

Nota: la normalización está desactivado por defecto para el algoritmo kmeans (filtro Normalize).
==================================================================================================================
Argumentos:
	-No son necesarios argumentos en la línea de comandos, no obstante se debe rellenar de forma correcta el fichero de 
	configuración dispuesto con el ejecutable, en el cual se permite ajustar la configuración de los distintos parámetros. 
	El fichero de configuración kmeans.conf	deberá encontrarse en el directorio config que se debe encontrar en el mismo 
	directorio que el ejecutable.
		file=data/ClusterData.atributos.txt;#path del archivo
		k=2;#número de clusters.
		iterations=10;#número de iteracines máximas fijo;
		difference=0.0;#ponderación de la comparación de las distancias con los centroides.
		distance=3;#Distancia a utilizar (exponente).
		initialize=0;#inicialización (Codebook aleatorio o matriz de pertenencia aleatoria).
		file_extension=txt;#extensión del archivo.
		data_line_start=0;#línea en la que empiezan los datos (para csv u otros formatos).
		delimiter=\t;#delimitador entre atributos(para csv u otros formatos).
		normalize=1#normalizar o no(0 no normaliza 1 si);
		ratio_max=1.0(disimilitud para actualizar codebook 0.0(distintos) a 1.0(iguales));
	- Opciones a considerar:
		-Se recomienda contener los ficheros con los datos en el directorio data creado en el mismo directorio que el ejecutable.
		-Conceder los permisos necesarios de ejecución al ejecutable.(sudo chmod 755 path/kmeans.jar)
		-La medida de desempeño utilizada no garantiza una evaluación óptima, aunque si ofrece una medida de cuanto de bien se han agrupado
		 las instancias en el cluster correspondiente.
	- Ejemplo: java -jar kmeans.jar 
==================================================================================================================
Precondiciones:
	- Todos los campos del fichero de configuración corectamente completados.
	- Al menos un atributo numérico.

Postcondiciones:
	- Se obtiene un gráfico de la matriz de pertenencia y el número de instancias cotenidas en cada cluster con sus correspondientes centroides, 
	además del resultado de la evaluación(Silhouette Coefficient).
==================================================================================================================
Ejemplo de ejecución en GNU/Linux:

Ejecución del programa mediante la línea de comandos:

1.Descomprimimos el archivo kmeans-1.0.zip,de donde obtenemos todos los recursos necesarios.

2. Nos situamos en el directorio donde hemos descomprimimos el archivo , lugar donde está situado el ejecutable en el
subdirectorio bin, kmeans.jar, supondremos está en nuestro directorio personal:

		$cd /home/usuario/Clustering/bin

2. Le damos los permisos necesarios de ejecución, en caso de que no los tenga ya, para asegurarnos
podemos introducir el siguiente comando:

		$ls -la

En caso de que no tenga los permisos suficientes para poder ejecutarlo se los asignamos:

		$sudo chmod 755 kmeans-1.0.jar

Nota: se necesita la contraseña de super usuario para realizar esta tarea.

3.Nos aseguramos que tenemos el fichero de configuración con los parámetros de ejecución que necesitamos.
	
		$cd ..
		$cd config
		$sudo gedit kmeans.conf #Editor de textos que tenemos en el sistema.

4. Volvemos al directorio Clustering e introducimos el comando para ejecutar kmeans-1.0.jar:

		$cd ~/Clustering
		$java -jar -Xmx2048m bin/kmeans-1.0.jar

Donde le asignaremos suficiente memoria para poder realizar el cómputo sin problemas con el parámetro -Xmx
de la máquina virtual de java.

En la carpeta data se encuentran varios archivos de diferentes extensiones con los que realizar el experimento, siendo uno de
ellos bank-data.csv, si queremos ejecutar con estos datos, no es necesario modificar el archivo de configuración, ya que biene
con una configuración por defecto:

		$java -jar -Xmx2048m bin/kmeans-1.0.jar 

==================================================================================================================
Ejemplo de ejecución en Windows:

0. Asegurarse de tener Java instalado en el ordenador.

1. Pulsamos el botón de Windows + la tecla R y escribimos cmd. También podemos ir al menú de inicio -> ejecutar
y escribir cmd. Se nos abrirá la terminal de Windows.

2. Nos situamos en el directorio descomprimido, lugar donde está situado el ejecutable en el
subdirectorio bin, kmeans.jar, supondremos que el directorio en está en nuestro directorio personal:

		XP: cd C:\
		    cd Documents and Settings\usuario\Clustering\bin
		7, 8.1: cd C:\
		    cd Users\usuario\Clustering\bin

3. Volvemos al directorio Clustering e introducimos el comando para ejecutar kmeans-1.0.jar:

		cd ..
		java -jar -Xmx2048m bin\kmeans-1.0.jar 

Donde le asignaremos suficiente memoria para poder realizar el cómputo sin problemas con el parámetro -Xmx
de la máquina virtual de java.

Donde le asignaremos suficiente memoria para poder realizar el cómputo sin problemas con el parámetro -Xmx
de la máquina virtual de java.

En la carpeta data se encuentran varios archivos de diferentes extensiones con los que realizar el experimento, siendo uno de
ellos bank-data.csv, si queremos ejecutar con estos datos, no es necesario modificar el archivo de configuración, ya que biene
con una configuración por defecto:


		java -jar -Xmx2048m bin\kmeans-1.0.jar

==================================================================================================================
Ejemplo de resultados:
utilizando algunos datos que s edisponen en el directorio data y con la siguiente configuración:

file=data/ClusterData.atributos.txt; 
k=3;
iterations=10;
difference=0.0;
distance=3;
initialize=0;
file_extension=txt;
data_line_start=0;
delimiter=\t;
normalize=1;
ratio_max=1.0;

		$java -jar -Xmx2048m bin\kmeans-1.0.jar
		
CLUSTER: 0
---------------------------------------
Número de instancias en el cluster: 666
Con el codeword: 
0.19383355958698473
0.5910097597597602
0.07768188356423648
0.0606106106106103

===========================================================================
CLUSTER: 1
---------------------------------------
Número de instancias en el cluster: 816
Con el codeword: 
0.4351497448294401
0.30757761437908654
0.5704728950403695
0.549019607843141

===========================================================================
CLUSTER: 2
---------------------------------------
Número de instancias en el cluster: 518
Con el codeword: 
0.6974612577352304
0.44973455598455686
0.7891178092858777
0.8231660231660218

===========================================================================
Average Silhouette coefficient: 0.5146198370277008						
===========================================================================


==================================================================================================================
No se normalizarán las instancias
CLUSTER: 0
---------------------------------------
Número de instancias en el cluster: 12
Con el codeword: 
146.66666666666666
18.666666666666664
6.666666666666667
12.833333333333332
1.616666666666667

===========================================================================
CLUSTER: 1
---------------------------------------
Número de instancias en el cluster: 9
Con el codeword: 
293.3333333333333
16.777777777777775
24.444444444444443
7.777777777777778
2.1777777777777776

===========================================================================
CLUSTER: 2
---------------------------------------
Número de instancias en el cluster: 6
Con el codeword: 
101.66666666666666
14.166666666666666
4.166666666666667
142.5
2.166666666666667

===========================================================================
Average Silhouette coefficient: 0.45858721545569253
===========================================================================
