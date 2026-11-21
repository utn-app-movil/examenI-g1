# examenI

1ro revisamos que corra:
![alt text](image.png)

1er error el botón de Producción no funciona. Así que implementamos funcionalidad:
![alt text](image.png)

## Reporte de Errores y Soluciones Implementadas

1. **Verificación inicial y problema con el botón de Producción:**
   Al revisar el proyecto base y ejecutarlo por primera vez, detecté que al presionar el botón de Producción en el menú principal (`btnProduction_main`), la pantalla no cargaba ningún registro ni permitía gestionar datos, ya que las actividades (`ProdListActivity` y `ProdCrudActivity`) y el ViewModel estaban vacíos o sin conexión lógica con el repositorio en memoria. 
   **Solución:** Implementé la lógica completa de navegación, carga de datos con `ListAdapter`, y conecté el CRUD utilizando la clase `MemoryDataManager` a través de un `ProdViewModel` personalizado.

2. **Desalineación inicial con los campos del layout base:**
   Me encontré con que el layout genérico heredado (`activity_prod_crud.xml`) incluía componentes de texto orientados a personas (`txtName`, `txtPhone`, `txtEmail`, etc.) que no formaban parte directa del modelo de dominio de una orden de producción. 
   **Solución:** Como los lineamientos prohibían modificar los layouts base y archivos protegidos, adapté lógicamente la actividad para que conviviera armónicamente con dicha estructura, enfocando los datos de negocio en el número de orden, selección múltiple de productos, cantidad, fechas y empresa destino.

3. **Falta de retroalimentación e integración de datos:**
   Noté que al intentar registrar o interactuar con el módulo, no existía un flujo que informara al usuario sobre el estado de las operaciones o la existencia de registros previos. 
   **Solución:** Integré diálogos de confirmación (`util.showDialogCondition`), alertas informativas con `Toast` utilizando los mensajes estándar existentes en `strings.xml`, y aseguré que la entidad `ProdOrder` heredara correctamente de `Identifier` para su correcta visualización en el listado.

4. **Validación de Negocio e Integridad de Datos:**
   Para cumplir con los requerimientos del escenario y evitar inconsistencias, detecté la necesidad de blindar el formulario contra entradas erróneas o duplicadas. 
   **Solución implementada:**
   * Programé la validación estricta de que la cantidad sea mayor a cero (`qty > 0`).
   * Validé que la fecha de entrega sea estrictamente posterior a la fecha de inicio (`deliveryDt.isAfter(startDt)`).
   * Añadí una verificación de datos duplicados consultando el repositorio mediante `ProdViewModel.getById`, previniendo colisiones al registrar una orden con el mismo identificador (`TextDuplicateData`).

![alt text](image-1.png)
