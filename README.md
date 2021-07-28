# Introducción

**WeatherForecast** es un ejercicio similar a la [Weather kata](https://github.com/CodiumTeam/weather-kata) de [Codium Team](https://www.codium.team).

Se trata de una clase con un método público que devuelve la previsión del tiempo de una ciudad en una fecha concreta.

Para ello, esta clase utiliza una API externa (requiere conexión a internet): [www.metaweather.com](https://www.metaweather.com) 

Ejemplo:

```java
WeatherForecast weatherForecast = new WeatherForecast();
weatherForecast.getCityWeather("Madrid", new Date());
```


# Ejercicio

El ejercicio consiste en **refactorizar** el código para hacerlo más **mantenible**, ya que el código existente, aunque **funciona**, es muy difícil de entender. 
  
Para ello se pueden realizar múltiples modificaciones siempre que se mantenga el API público. Ejemplos de modificaciones: incluir tests automáticos, extraer métodos, renombrar variables, modificar tipos de datos, crear nuevas clases, añadir librerías, etc. 


# Documentación

La solución debería contener un fichero README donde se respondan estas preguntas:
- ¿Qué has empezado implementando y por qué?
	- He empezado pensando como podía estructurarlo de la mejor y mas óptima forma posible.
	He creado las interfaces para abstraerme de la implementación y luego he generado algunos tests unitarios.
	Luego he programado el resto, refactorizado, extraido metodos y pulido en general todo lo que he sido capaz.
- ¿Qué problemas te has encontrado al implementar los tests y cómo los has solventado?
	- No tengo mucha experiencia con mockups (aunque lo he intentado), así que he hecho tests básicos. 
	Creo que el mayor problema ha sido mockear la requestfactory...
- ¿Qué componentes has creado y por qué?
	- Lo primero de todo he usado una interfaz para implementar metodos que he visto que había que extraer
	del código original. He creado el servicio y he refactorizado y pulido métodos y variables.
	He creado constantes para no tener nada escrito a fuego en el programa.
	Luego he cambiado tipos o formas que veía que no estaba muy claro lo que hacían, como por ejemplo el tema de las
	fechas. Lo mismo para recorrer datos, aunque no estoy acostumbrado he intentado hacer el Stream por ser más óptimo.
	He pulido tests y comprobado que cubrian el programa. También se podría haber hecho una clase para enviar 
	mensajes a la hora de lanzar excepciones.
- Si has utilizado dependencias externas, ¿por qué has escogido esas dependencias?
	- No he usado ninguna dependencia extra... Solo he cambiado una importación porque veía que estaba bastante lioso el tema de fechas
	y Instant lo simplicifa bastante y hace más legible.
- ¿Has utilizado  streams, lambdas y optionals de Java 8? ¿Qué te parece la programación funcional?
	- He usado streams y lambdas a la hora de recorrer los datos que recibía. No estoy muy acotumbrado y posiblemente
	refactorizar esta parte del código ha sido lo que más tiempo me ha llevado. Me podría haber abstraido más con
	Optionals pero tampoco los manejo del todo aún así que he preferido no usarlos...
	La programación funcional es un poco más complicada de entender pero al final optimiza nuestro código
	y nos ahorra tiempo.
- ¿Qué piensas del rendimiento de la aplicación? 
	- Creo que he optimizado bastante el rendimientop ero que se podríar más. Haciendo un cliente
	que reciba cualquier petición de metodos alojados en la API de metaweather. En este ejercicio solo hemos
	abarcado dos métodos pero lo suyo sería que estuviese preparada para recibir cualquier tipo de petición.
- ¿Qué harías para mejorar el rendimiento si esta aplicación fuera a recibir al menos 100 peticiones por segundo?
	- Cachear la API o hacer algun servicio de multiprocesos...
- ¿Cuánto tiempo has invertido para implementar la solución? 
	- Unas 8 horas más o menos.
- ¿Crees que en un escenario real valdría la pena dedicar tiempo a realizar esta refactorización?
	- Siempre que el programa mejore y haya tiempo merece la pena.

# A tener en cuenta

- Se valorará positivamente el uso de TDD, se revisarán los commits para comprobar su uso.
- Se valorará positivamente la aplicación de los principios SOLID y "código limpio".
- La aplicación ya tiene un API público: un método público que devuelve el tiempo en un String. No es necesario crear nuevas interfaces: REST, HTML, GraphQL, ni nada por el estilo.


# Entrega

La solución se puede subir a un repositorio de código público como [github](https://github.com/). 

Otra opción sería comprimir la solución (incluyendo el directorio .git) en un fichero .zip/.tar.gz y enviarlo por email.
