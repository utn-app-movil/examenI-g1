Nómina (Payroll)

En este examen me tocó trabajar el escenario 13 de Nómina, en la rama `sharon`, que saqué de `development`.

Qué hice

- Creé `PayEmployee` para manejar los datos y `PayViewModel` para guardar, consultar, actualizar y eliminar usando el administrador de memoria que ya traía el proyecto.
- Conecté el formulario de `PayCrudActivity` y el listado de `PayListActivity`.
- Agregué las validaciones de los campos, los duplicados, el salario, el mes y el IBAN.
- Dejé los botones y los íconos del menú como venían en el proyecto.

 Errores que encontré y cómo los corregí

**El listado salía vacío.** Revisé `PayListActivity` y vi que usaba `emptyList()`. Lo cambié para que cargara los registros desde `PayViewModel`.

**Al abrir un empleado no aparecía la información.** En `PayCrudActivity`, la función `loadExistingData()` estaba comentada. Le quité los comentarios y probé de nuevo. Después sí aparecieron los datos.

**Al eliminar decía que se había eliminado, pero el empleado seguía ahí.** La línea que llamaba a `PayViewModel.delete(id)` estaba comentada. La activé y comprobé que ahora sí se elimina.

**Guardar y actualizar estaban incompletos.** Conecté esas operaciones con el ViewModel. También usé el debugger de Android Studio para revisar los datos antes de guardar.

Las capturas del antes, las pruebas y el debugging están en la carpeta [evidencias](evidencias/).

## Pruebas que hice

Probé la aplicación en mi celular Xiaomi conectado a Android Studio. La aplicación compiló, se instaló y abrió bien. Entré a Payroll y comprobé que podía guardar, ver, editar y eliminar empleados.

También probé que no dejara guardar una identificación repetida, un mes posterior al actual ni un salario de cero. El código incluye la validación del IBAN de Costa Rica. Estas son las pruebas que tengo documentadas en las capturas; no estoy presentando como probadas otras que todavía no tienen evidencia.

## Archivos de mi parte

- `PayCrudActivity.kt`
- `PayListActivity.kt`
- `domain/model/PayEmployee.kt`
- `viewmodel/PayViewModel.kt`

El archivo `ListAdapter.kt` tiene una corrección que hicimos con el profesor por un problema del proyecto base.

No modifiqué `MemoryManager`, `Person`, `Identifier`, `IRepository`, `MainActivity`, el Manifest, el menú, los strings, los temas ni `util`. Dejé los nombres y textos que ya venían en el proyecto para no tocar otras partes. Sé que el enunciado pide inglés y el prefijo `pay_`; esto queda indicado porque algunos elementos originales no siguen ese formato.

## Entrega

Mi rama es `sharon` y el Pull Request va dirigido a `development`. En las evidencias se puede ver lo que encontré, lo que corregí y las pruebas que hice.
