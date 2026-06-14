# VetCare
Sistema para gestionar una clínica veterinaria (clientes, mascotas y citas) hecho en Java con Swing, Proyecto de Programación II

# VetCare - Clínica Veterinaria Huellitas

Este es mi proyecto para la materia de Programación II. Es una aplicación de escritorio
hecha en Java con Swing para manejar una clínica veterinaria: registrar clientes, sus
mascotas y agendar/gestionar las citas.

La idea era practicar POO (herencia, encapsulamiento), el uso de colecciones de Java y
guardar la información en archivos para que no se pierda al cerrar el programa.

## Qué se puede hacer

**Clientes**
- Registrar, modificar y eliminar clientes.
- Validaciones: el documento y el teléfono solo aceptan números, y el nombre solo letras.
- Una columna muestra cuántas mascotas tiene cada cliente.
- No deja eliminar un cliente si todavía tiene mascotas registradas.

**Mascotas**
- Registrar, modificar y eliminar mascotas, cada una asociada a su dueño.
- Validaciones: nombre, especie y raza solo letras; la edad solo números y máximo 100 años.
- No deja eliminar una mascota si tiene citas agendadas.

**Agendar Cita**
- La fecha se elige con tres "ruletas" (día, mes, año) y la hora igual (hora, minutos y AM/PM).
- Se puede marcar la cita como Normal o Prioritaria.
- Valida que la fecha exista de verdad (no deja un 31 de febrero, por ejemplo).
- No deja agendar dos citas a la misma fecha y hora (la veterinaria no puede atender dos a la vez).
- No deja agendar en fechas u horas que ya pasaron.

**Citas e Historial**
- Cada cita tiene un estado: En espera, En proceso, Atendida o Cancelada.
- Botones para Atender, marcar como atendida, Modificar y Cancelar.
- Al marcar una cita como atendida, compara la hora real con la prevista y dice si fue
  a tiempo, antes o después.
- Al cancelar pide el motivo y lo guarda.
- Las citas prioritarias se resaltan en la tabla.
- Cuando una cita se atiende o se cancela, pasa automáticamente a la pestaña de Historial,
  donde se ve el resultado de la atención y el motivo de cancelación.

## Tecnologías que usé

- Java
- Swing / AWT para la interfaz gráfica
- Colecciones (ArrayList)
- Persistencia en archivos de texto (CSV)
- `java.time` para el manejo de fechas y horas (Este fue un extra que agregue porque el profesor me lo sugirio)

## Estructura del proyecto

Separé el código en paquetes para tenerlo más ordenado:

- `Model` -> las clases del dominio: `Persona` (clase abstracta), `Cliente` (que hereda de
  Persona), `Mascota`, `Cita`, y los enums `Sexo` y `EstadoCita`.
- `Data` -> la lógica principal en `Veterinaria` y el guardado/cargado de archivos en
  `PersistenciaArchivo`.
- `Gui` -> las pantallas: la ventana principal con pestañas y un panel para cada cosa
  (clientes, mascotas, agendar, citas, historial).

Los datos se guardan en `clientes.csv`, `mascotas.csv` y `citas.csv`.

## Cómo ejecutarlo

1. descargar el repositorio.
2. Abrir el proyecto en NetBeans.
3. Hacer Open File.
4. Ejecutar la clase `VetCareApp` (es la que tiene el `main`).

La primera vez los archivos CSV se crean solos cuando vas registrando información.

## Cosas que me gustaría mejorar más adelante

- Poder reasignar las mascotas de un cliente a otro en vez de tener que eliminarlas.
- Agregar un login para diferenciar entre recepcionista y veterinario.
