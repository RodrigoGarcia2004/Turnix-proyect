# 🏥 Turnix Pro: Sistema de Gestión Hospitalaria y Telemedicina en Tiempo Real

## 📖 INTRODUCCIÓN Y CONTEXTO
**Turnix Pro** nace como respuesta a la necesidad de digitalización en el sector sanitario. A diferencia de un sistema de turnos genérico, esta plataforma se enfoca en la **atención médica remota**, resolviendo la brecha entre la gestión de espera física y la consulta virtual. 

El proyecto ha evolucionado de una arquitectura básica de red a una **solución intermodular completa**, integrando persistencia de datos, comunicación full-duplex y una interfaz web profesional orientada a la experiencia de usuario (UX).

---

## 🛠️ STACK TECNOLÓGICO Y JUSTIFICACIÓN
Para elevar el proyecto al nivel intermodular exigido, se han seleccionado tecnologías que permiten escalabilidad y un entorno de ejecución real:

* **Backend (Java v17+):** Motor de alta concurrencia capaz de gestionar múltiples hilos de ejecución.
* **WebSockets (JSR 356):** Sustituimos los Sockets TCP convencionales por WebSockets para permitir una comunicación bidireccional en tiempo real compatible con navegadores modernos, eliminando la necesidad de aplicaciones de escritorio obsoletas.
* **Frontend (HTML5, CSS3, JavaScript ES6):** Implementación de una SPA (Single Page Application) que consume el servicio de sockets, simulando un entorno de producción web profesional.
* **Persistencia (MySQL / MariaDB):** Gestión de datos relacionales para auditoría, logs de chat y trazabilidad de turnos.
* **JSON:** Estándar de intercambio de datos para asegurar la interoperabilidad entre el servidor Java y el cliente web.

---

## 🏗️ ARQUITECTURA Y DISEÑO TÉCNICO

### **1. Gestión de Roles y Seguridad**
El sistema no es plano; implementa una jerarquía de acceso basada en roles:
* **Paciente:** Interfaz simplificada para solicitud de turno y chat de consulta.
* **Médico:** Panel de control para gestión de cola, métricas de reputación y atención privada.
* **Administrador (Backoffice):** (En desarrollo) Supervisión de logs y gestión de usuarios.

### **2. Gestión de la Concurrencia y Estado**
Uno de los mayores retos técnicos resueltos es la consistencia de datos ante accesos concurrentes:
* **Queue Management:** Uso de colecciones thread-safe en Java para garantizar que el orden de los turnos sea inalterable.
* **Heartbeat & Reconexión:** El sistema detecta desconexiones de clientes y gestiona la limpieza de la cola en tiempo real para evitar "turnos fantasma".

### **3. Protocolo de Comunicación Personalizado**
Se ha diseñado un protocolo sobre WebSockets que diferencia entre **Mensajes de Control** y **Mensajes de Datos**:
* `COMANDO:ABRIR_CHAT`: Dispara eventos en el DOM del cliente.
* `VALORACION_ACTUALIZADA`: Sincroniza el estado de la DB con la UI del médico sin refresco de página.

---

## 📊 MODELO DE DATOS Y PERSISTENCIA
Para superar la crítica de "ampliación superficial", se ha diseñado un esquema que garantiza la integridad referencial y permite auditorías post-consulta:
* **Tabla `usuarios`:** Almacena credenciales, roles y perfiles médicos (incluyendo reputación media).
* **Tabla `turnos`:** Registro vivo de la carga asistencial.
* **Tabla `logs_chat`:** (Fase Actual) Persistencia de las interacciones para cumplimiento legal y seguimiento médico.
* **Tabla `valoraciones`:** Almacena el feedback de los pacientes para el análisis de calidad del servicio.

---

## 🚀 ESPECIFICACIONES FUNCIONALES (Nivel Avanzado)
1.  **RF-1 Autenticación Robusta:** Acceso diferenciado con validación en servidor y persistencia de sesión.
2.  **RF-2 Gestión de Turnos Inteligente:** Algoritmo que calcula la posición real y notifica cambios al instante.
3.  **RF-3 Chat Médico-Paciente:** Comunicación cifrada en tránsito mediante el túnel del WebSocket.
4.  **RF-4 Feedback System:** Sistema de valoración de estrellas que impacta en la reputación del médico en tiempo real.
5.  **RF-5 Auditoría de Tiempos:** Registro automático de la hora de inicio y fin de cada consulta para métricas de eficiencia.

---

## 📅 PLANIFICACIÓN TÉCNICA (GANTT REVISADO)

| Fase | Descripción | Tecnologías | Duración |
| :--- | :--- | :--- | :--- |
| **I. Infraestructura** | Migración a WebSockets y refactorización de concurrencia. | Java, JSR 356 | 3 Semanas |
| **II. Interfaz Web** | Desarrollo de paneles SPA para Médico y Paciente. | JS ES6, CSS Grid | 3 Semanas |
| **III. Persistencia** | Diseño de DB avanzada y optimización de consultas JDBC. | MySQL, Pooling | 2 Semanas |
| **IV. Integración** | Implementación de envío de archivos y logs de actividad. | JSON, Filesystem API | 2 Semanas |

---

## 🧪 PRUEBAS Y CALIDAD
* **Test de Estrés:** Simulación de 100+ pacientes concurrentes solicitando turno.
* **Integridad de Datos:** Verificación de que ninguna desconexión abrupta deje registros inconsistentes en la DB.
* **Compatibilidad:** Pruebas en Chrome, Firefox y Safari para asegurar el estándar de WebSockets.

---
> **Justificación del Proyecto:** Turnix Pro no es solo una práctica de sockets; es una infraestructura de servicios integrados que demuestra competencia en redes (PSP), bases de datos (AD) y desarrollo de interfaces (DI).
