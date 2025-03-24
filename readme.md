# 🎯 LABORATORIO   
**Escuela Colombiana de Ingeniería**  
📌 *Ciclos de Vida de Desarrollo de Software*  

---

## 👥 Integrantes del grupo **FENRIR**  
- 🏆 **Vicente Garzón Rios**  
- 🏆 **Daniel Alejandro Diaz Camelo**  
- 🏆 **Geronimo Martinez Nuñez**  
- 🏆 **Carlos David Barrero Velasquez**  

---

## 📌 Caso de Negocio: **Sistema de Reservas de Salones**  
**Escuela Colombiana de Ingeniería Julio Garavito**  

El proyecto consiste en una aplicación para la gestión de reservas de salones dentro de la **Escuela Colombiana de Ingeniería Julio Garavito**.  

✔️ Los usuarios podrán **consultar la disponibilidad de salones**, realizar reservas y cancelar sus reservas desde una interfaz web.  
✔️ La aplicación se conectará a un **API REST** desarrollado en **Spring Boot**.  
✔️ Se utilizará **MongoDB Cloud** o **archivo de texto plano** para la persistencia de datos.  

---

## ✅ Requerimientos  
🔹 El usuario debe poder **consultar la disponibilidad de laboratorios**.  
🔹 El usuario debe poder **reservar un laboratorio** especificando **fecha, hora y propósito**.  
🔹 El usuario debe poder **cancelar sus reservas**.  
🔹 La aplicación debe **validar** que un laboratorio no se pueda reservar si ya está ocupado.  

---

## 🏗️ Épicas  
📌 **Backend:** Implementación de un **API REST** para la lógica de negocio y persistencia de datos.  

---
## Scrum - DI/IOC

## 🚀 Sprints 
### 🏁 **Sprint 1: Configuración General del Proyecto**  

![Text](assets/1.png)

  🔹 Configuración de **ambientes** (backend y frontend).

  ![Text](assets/5.png)

  🔹 **Scaffolding** del proyecto.

  📌 Diseño Inicial - Mockups en Papel
Antes de la implementación digital, se realizaron bocetos a mano para definir la estructura general del sistema. Este paso permitió visualizar la disposición de los elementos clave en la interfaz y organizar mejor la experiencia de usuario antes de llevarlo a herramientas de diseño más avanzadas.

A continuación, se presentan algunos de los mockups iniciales realizados en papel:

<div align="center"> <img src="assets/7.png" alt="Mockup inicial 1" width="45%"> <img src="assets/8.png" alt="Mockup inicial 2" width="45%"> <br> <img src="assets/9.png" alt="Mockup inicial 3" width="45%"> <img src="assets/10.png" alt="Mockup inicial 4" width="45%"> </div>

  ![Text](assets/6.png)

  ![Text](assets/2.png)

  🔹 Configuración de la **base de datos** (MongoDB Cloud o archivo de texto plano).

  ![Text](assets/3.jpg)

  🔹 Definición del **modelo de datos** (salones y reservas).

  ![Text](assets/4.jpg)

---

### ⚡ **Sprint 2: Implementación del API REST**



![Text](assets/17.png)

  🔹 Crear los **endpoints** necesarios para consultar laboratorios, realizar reservas y cancelar reservas.

  ```yml
      @GetMapping("/allLabs")
      public List<Lab> getAllLabs() {
          return labService.getLabs();
      }
  ```

  ```yml
      /**
      * Creates a new reservation.
      * 
      * @param reservation Object containing the reservation details.
      * @return ResponseEntity with the operation result.
      * 
      * Possible responses:
      * - 200 OK: The reservation was successfully created.
      * - 400 BAD REQUEST: The reservation could not be created due to invalid data or availability conflicts.
      */
      @PostMapping
      public ResponseEntity<Object> createReservation(@RequestBody Reservation reservation) {
          try {
              Reservation createdReservation = reservationServiceImpl.createReservation(reservation);
              return ResponseEntity.status(HttpStatus.CREATED).body(createdReservation);
          } catch (IllegalArgumentException e) {
              return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
          }
  ```

  ```yml
  /**
     * Endpoint to cancel a reservation.
     *
     * This endpoint should:
     * - Receive the reservation ID from the request path.
     * - Call the service method to cancel the reservation.
     * - Return an appropriate HTTP response.
     *
     * @param reservationId ID of the reservation to cancel.
     * @return ResponseEntity with status 200 if successful, 404 if not found, or 400 if already canceled.
     */
    @PutMapping("/cancel/{reservationId}")
    public ResponseEntity<Object> cancelReserve(@PathVariable String reservationId) {
        try{
            Reservation reservation = reservationServiceImpl.cancelReservationByReservationId(reservationId);
            return ResponseEntity.status(HttpStatus.OK).body(reservation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
  ```

  🔹 Implementar la lógica de validación para evitar reservas conflictivas.

  ```yml

  /**
     * Creates a new reservation if the lab is available.
     * 
     * @param reservation Object containing the reservation details.
     * @return The created reservation stored in the database.
     * 
     *         Possible scenarios:
     *         - The reservation is successfully stored if the lab is available.
     *         - An exception is thrown if the lab is already booked for the
     *         requested time slot.
     */
    @Override
    public Reservation createReservation(Reservation reservation) {
        if (!labRepository.existsById(reservation.getLabId())) {
            throw new IllegalArgumentException("The lab does not exist");
        }

        if (!userRepository.existsById(reservation.getUserId())) {
            throw new IllegalArgumentException("The user does not exist");
        }

        if (!checkStartTime(reservation.getParsedStartTime(), reservation.getParsedDate())) {
            throw new IllegalArgumentException(
                    "The start time must be in the future. You cannot create a reservation with a past time");
        }

        if (!checkDate(reservation.getParsedDate())) {
            throw new IllegalArgumentException("You cannot select a past date for your reservation");
        }

        if (!isAvailable(reservation)) {
            throw new IllegalArgumentException(
                    "There is already a reservation in the lab selected in the time selected");
        }

        Reservation savedReservation = reservationRepository.save(reservation);

        addReservationToLab(reservation);
        addReservationToUser(reservation);

        return savedReservation;
    }

  ```

  🔹 Pruebas realizadas con PostMan a los endpoints.

  
  ## Autenticación y Rutas  

Las rutas para **login** y **signup** son **públicas**, por lo que **no requieren autorización**.  

Las demás rutas son **privadas** y requieren de un **token** para acceder.  

## Uso del Token en Postman  
Para autenticarte en Postman con el **token generado**, se siguen estos pasos:  

1. Ir a la pestaña **Authorization**.  
2. Seleccionar la opción **Bearer Token**.  
3. Pegar el **token generado** en el campo correspondiente. 


  ![alt text](assets/IMG-20250323-WA0011.jpg)
  ![alt text](assets/IMG-20250323-WA0012.jpg)
  ![alt text](assets/IMG-20250323-WA0013.jpg)
  ![alt text](assets/IMG-20250323-WA0014.jpg)
  ![alt text](assets/IMG-20250323-WA0015.jpg)
  ![alt text](assets/IMG-20250323-WA0016.jpg)
  ![alt text](assets/IMG-20250323-WA0017.jpg)
  ![alt text](assets/IMG-20250323-WA0018.jpg)
  ![alt text](assets/IMG-20250323-WA0019.jpg)
  ![alt text](assets/IMG-20250323-WA0020.jpg)

  ## Restricción de Acceso para Administradores  

En las siguientes tres rutas, **solo un administrador** tiene acceso. Esto significa que **únicamente un administrador** puede crear otro usuario con permisos de administrador.

![alt text](<assets/Imagen de WhatsApp 2025-03-23 a las 18.28.14_8c53553d.jpg>)
![alt text](<assets/Imagen de WhatsApp 2025-03-23 a las 18.28.14_8d883554.jpg>)
![alt text](<assets/Imagen de WhatsApp 2025-03-23 a las 18.28.14_5704ad2a.jpg>)


  🔹 Configuración de la persistencia de datos en MongoDB Cloud o archivo de texto plano.

  La **URL de la base de datos** y su **nombre** están almacenados en un archivo **.env**, el cual no se sube al repositorio por razones de seguridad.  
Cada desarrollador debe tener su propio archivo **.env** de forma local.

![alt text](assets/DB.jpg)

## DEVOPS / CI-CD

1.  Clonamos nuestro repositorio usado en el laboratorio 4

<p align="center">
  <img src="assets/repo_clone.png" alt="Clonando repositorio" width="600">
</p>

---

2. Luego de crear una cuenta gratuita en Azure, nos dirigimos a **App Services** > **Crear** > **Aplicación web** y creamos un "App Service"

<p align="center">
  <img src="assets/app_service1.png" alt="Creación de App Service" width="600">
</p>

<p align="center">
  <img src="assets/app_service2.png" alt="Configuración de App Service" width="600">
</p>

<p align="center">
  <img src="assets/app_service3.png" alt="Finalización de App Service" width="600">
</p>

---

3. Luego de haber creado nuestra app service, nos dirigimos a **Ir al recurso** > **Implementación** > **Centro de implementación** y agregamos el repositorio

<p align="center">
  <img src="assets/add_repo.png" alt="Agregando repositorio en Azure" width="600">
</p>

---

4. Cuando agregamos el repositorio, Azure automaticamente genera el **Workflow file** el cual se ejecuta automáticamente, pero saldra error ya que debemos configurar las variables de entorno

<p align="center">
  <img src="assets/workflow_error.png" alt="Error en Workflow por falta de variables de entorno" width="600">
</p>

---

5. En el repositorio, nos dirigimos a **Settings** > **Secrets and variables** > **Actions** > **Secrets** > **New repository secret** y agregamos las variables de entorno requeridas, en nuestro caso `DATA_BASE_NAME` y `DATA_BASE_NAME`

<p align="center">
  <img src="assets/var_env_git.png" alt="Configurando variables de entorno en GitHub" width="600">
</p>

---

6. En Azure, nos dirigimos a **Configuración** > **Variables de entorno** > **Agregar** y agregamos las variables de entorno requeridas, en nuestro caso `DATA_BASE_NAME` y `DATA_BASE_NAME`

<p align="center">
  <img src="assets/var_env_azure.png" alt="Configurando variables de entorno en Azure" width="600">
</p>

---

6. Luego de agregar las variables de entorno en git y Azure, configuramos nuestro "Workflow file" agregando las variables de entorno en **jobs** > **steps**

```shell
- name: Environment Variables
        run: |
          echo "DATA_BASE_URL=${{ secrets.DATA_BASE_URL }}" >> $GITHUB_ENV
          echo "DATA_BASE_NAME=${{ secrets.DATA_BASE_NAME }}" >> $GITHUB_ENV
```

---

7. Luego de modificar nuestro "Workflow file", ejecutamos de nuevo

<p align="center">
  <img src="assets/workflow_success.png" alt="Workflow ejecutado correctamente" width="600">
</p>

---

8. Para probar, nos dirigimos a **App Services** > **[nombre_de_tu_servicio]** y hacemos nuestras consultas al link que obtenemos en **Dominio predeterminado**, en nuestro caso `unireserva-haa2a4e3aueeeqes.brazilsouth-01.azurewebsites.net`

<p align="center">
  <img src="assets/test.png" alt="Probando el despliegue en Azure" width="600">
</p>


## 🚀 Tecnologías Utilizadas en el Backend  

El backend del proyecto fue desarrollado utilizando las siguientes tecnologías:  

- **☕ Java**: Lenguaje de programación principal.  
- **🛠️ Spring Boot**: Framework utilizado para agilizar el desarrollo del backend.  
- **🔒 Spring Security**: Implementado para la gestión de autenticación y autorización.  
- **🛡️ JWT (JSON Web Token)**: Utilizado para la autenticación de usuarios y la protección de rutas privadas.  
- **🔐 SSL (Secure Sockets Layer)**: Configurado para garantizar la seguridad en la comunicación entre cliente y servidor.  

### **☁️ Despliegue en Azure**  
El backend fue desplegado en **Azure**, aprovechando su infraestructura en la nube para garantizar **🌍 alta disponibilidad, 📈 escalabilidad y 🔒 seguridad** en la aplicación.  
