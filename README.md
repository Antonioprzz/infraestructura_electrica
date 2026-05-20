# ⚡ Infraestructura Eléctrica

Sistema de gestión de infraestructura eléctrica desarrollado con Java y ObjectDB como base de datos orientada a objetos, en el marco de la asignatura de Programación de 1º DAM.

---

## Descripción del proyecto

Aplicación de consola en Java que permite gestionar la red de infraestructura eléctrica de una compañía distribuidora. El sistema cubre el ciclo completo desde las subestaciones y líneas de transporte hasta los contratos de suministro, contadores y lecturas de consumo.

Las operaciones disponibles son altas, bajas, modificaciones y consultas (CRUD completo) sobre las seis entidades del dominio:

- **Subestacion** — subestaciones eléctricas con nombre, provincia, coordenadas y capacidad en MW.
- **LineaTransporte** — líneas de transporte entre dos subestaciones con código, longitud y voltaje.
- **Titular** — personas físicas o empresas titulares de contratos de suministro.
- **ContratoSuministro** — contratos con tarifa, fecha de alta y potencia contratada.
- **Contador** — contador físico vinculado 1:1 a un contrato.
- **LecturaConsumo** — lecturas de consumo en kWh con origen AUTOMATICO o MANUAL.

La persistencia se gestiona con JPA sobre ObjectDB (base de datos orientada a objetos), eliminando la impedancia objeto-relacional y permitiendo trabajar directamente con el modelo de clases Java.

---

## Requisitos previos

| Software | Versión mínima recomendada |
|---|---|
| Java JDK | 21 (el `pom.xml` declara source/target 25; ajustar si el JDK disponible es 21) |
| Apache Maven | 3.8+ |
| ObjectDB | 2.8.8 (se descarga automáticamente vía Maven) |
| IDE recomendado | IntelliJ IDEA Community Edition 2023+ |

> **Nota sobre la versión de Java:** el `pom.xml` del repositorio declara `maven.compiler.source` y `maven.compiler.target` en 25. Si tu JDK es el 21 (versión LTS habitual en clase), cambia ambos valores a `21` antes de compilar.

---

## Configuración de la base de datos

### 1. ObjectDB en el proyecto

ObjectDB se descarga automáticamente desde su repositorio Maven al ejecutar `mvn install` por primera vez, gracias a la entrada en `pom.xml`:

```xml
<repository>
    <id>objectdb</id>
    <url>https://m2.objectdb.com</url>
</repository>
```

No es necesario instalar ObjectDB de forma independiente.

### 2. Ruta del fichero de base de datos

El fichero de base de datos es `infraestructura.odb` y se ubica en la **raíz del proyecto** (al mismo nivel que `pom.xml`). La ruta está configurada en `persistence.xml` como una ruta relativa:

```xml
<property name="javax.persistence.jdbc.url" value="infraestructura.odb"/>
```

Esto significa que la aplicación debe **ejecutarse siempre desde la raíz del proyecto** para que ObjectDB encuentre (o cree) el fichero correctamente.

### 3. Datos iniciales de prueba

El repositorio incluye el fichero `infraestructura.odb` con datos de prueba ya cargados. Al clonar el repositorio y ejecutar la aplicación, los datos estarán disponibles de inmediato.

Si prefieres empezar con la base de datos vacía, basta con **eliminar o renombrar** el fichero `infraestructura.odb` antes de la primera ejecución. ObjectDB creará uno nuevo automáticamente.

### 4. Configurar `persistence.xml`

El fichero se encuentra en:

```
src/main/resources/META-INF/persistence.xml
```

Contenido relevante:

```xml
<persistence-unit name="redElectricaPU">
    <provider>com.objectdb.jpa.Provider</provider>

    <class>model.Contador</class>
    <class>model.ContratoSuministro</class>
    <class>model.LecturaConsumo</class>
    <class>model.LineaTransporte</class>
    <class>model.Subestacion</class>
    <class>model.Titular</class>

    <properties>
        <property name="javax.persistence.jdbc.url"   value="infraestructura.odb"/>
        <property name="javax.persistence.jdbc.user"     value="admin"/>
        <property name="javax.persistence.jdbc.password" value="admin"/>
    </properties>
</persistence-unit>
```

No es necesario modificarlo salvo que quieras cambiar la ruta o el nombre del fichero `.odb`.

---

## Clonar el repositorio

```bash
git clone https://github.com/Antonioprzz/infraestructura_electrica.git
cd infraestructura_electrica
```

---

## Compilación y ejecución

### Desde línea de comandos (Maven)

```bash
# Compilar el proyecto
mvn clean compile

# Ejecutar la aplicación (desde la raíz del proyecto)
mvn exec:java -Dexec.mainClass="app.Main"
```

> Si el plugin `exec-maven-plugin` no está declarado en el `pom.xml`, compila con `mvn clean package` y ejecuta el JAR resultante:
>
> ```bash
> mvn clean package
> java -cp target/infraestructura_electrica-1.0-SNAPSHOT.jar app.Main
> ```

### Desde IntelliJ IDEA

1. `File → Open` y selecciona la carpeta raíz del proyecto.
2. IntelliJ detectará el `pom.xml` e importará las dependencias automáticamente.
3. Si aparece el error *"SDK not found"*, ve a `File → Project Structure → SDK` y selecciona tu JDK 21.
4. Marca `src/main/java` como *Sources Root* si IntelliJ no lo reconoce.
5. Abre `src/main/java/app/Main.java` y pulsa el botón ▶ junto al método `main`.

---

## Estructura del proyecto

```
infraestructura_electrica/
│
├── src/
│   └── main/
│       ├── java/
│       │   ├── app/
│       │   │   └── Main.java                  ← Punto de entrada; menús de consola y CRUD
│       │   ├── model/
│       │   │   ├── Subestacion.java            ← Entidad: subestación eléctrica
│       │   │   ├── LineaTransporte.java        ← Entidad: línea de transporte entre subestaciones
│       │   │   ├── Titular.java                ← Entidad: titular de contratos
│       │   │   ├── ContratoSuministro.java     ← Entidad: contrato de suministro eléctrico
│       │   │   ├── Contador.java               ← Entidad: contador físico (1:1 con contrato)
│       │   │   └── LecturaConsumo.java         ← Entidad: lectura de consumo en kWh + enum OrigenLectura
│       │   ├── dao/
│       │   │   ├── SubestacionDAO.java         ← CRUD + consultas JPQL de subestaciones
│       │   │   ├── LineaTransporteDAO.java     ← CRUD + consultas por subestación
│       │   │   ├── TitularDAO.java             ← CRUD + búsqueda por NIF
│       │   │   ├── ContratoSuministroDAO.java  ← CRUD + listado por titular
│       │   │   ├── ContadorDAO.java            ← CRUD + búsqueda por contrato y número de serie
│       │   │   └── LecturaConsumoDAO.java      ← CRUD + listado por contador ordenado por fecha
│       │   ├── service/
│       │   │   ├── SubestacionService.java     ← Validaciones RS-001, RS-002
│       │   │   ├── LineaTransporteService.java ← Validaciones RS-006, unicidad de código, origen ≠ destino
│       │   │   ├── TitularService.java         ← Validación RS-004 (NIF único)
│       │   │   ├── ContratoSuministroService.java ← Validaciones RS-005, unicidad de código
│       │   │   ├── ContadorService.java        ← Validación número de serie único y restricción 1:1
│       │   │   └── LecturaConsumoService.java  ← Validación RS-003 (valor ≥ 0)
│       │   └── util/
│       │       ├── JPAUtil.java               ← Singleton del EntityManagerFactory
│       │       └── ValidationException.java   ← RuntimeException personalizada para reglas de negocio
│       └── resources/
│           └── META-INF/
│               └── persistence.xml            ← Configuración JPA/ObjectDB
│
├── doc/                                       ← Javadoc generado automáticamente
├── infraestructura.odb                        ← Fichero de base de datos ObjectDB (datos de prueba)
├── pom.xml                                    ← Gestión de dependencias Maven (ObjectDB 2.8.8, JPA 2.2)
└── README.md
```

---

## Equipo

| Nombre | Rol | Responsabilidades principales |
|---|---|---|
| **Antonio Pérez Díaz** | Jefe de Proyecto | Arquitectura en capas, `JPAUtil`, configuración de persistencia, coordinación Git y resolución de conflictos de merge |
| **Juan María Alanís** | Diseñador de Base de Datos | Modelo E-R, entidades JPA `Titular` y `ContratoSuministro`, restricciones de integridad y documentación del diseño |
| **Antonio Beltrán** | Desarrollador Backend | `ContadorDAO`, `LecturaConsumoDAO`, servicios asociados, enum `OrigenLectura` y validaciones RS-003/RS-005 |
| **Daniel Del Toro** | Desarrollador de Interfaz | Diseño e implementación de la interfaz de consola, menús de navegación, integración CRUD y validación de entradas |
| **Sergio Ojeda** | Responsable de Calidad y Documentación | Redacción de la memoria, pruebas funcionales manuales, revisión de validaciones y configuración del `pom.xml` |

---

> Proyecto desarrollado para la asignatura **Programación** — 1º DAM · Curso académico 2025/2026.
