# Proyecto Dpresik2 (App Android con Firebase)

Esta es una aplicación nativa de Android desarrollada en Kotlin que demuestra una implementación completa de **Firebase Authentication**. Permite a los usuarios registrarse e iniciar sesión usando múltiples proveedores (Google y Email/Contraseña) y navegar a un área de menú segura después de la autenticación.

## Características

* **Autenticación de Firebase:** Backend robusto para la gestión de usuarios.
* **Inicio de Sesión con Google:** Integración de un clic con el SDK de Google Sign-In.
* **Login con Email/Contraseña:** Registro e inicio de sesión tradicional.
* **Navegación Segura:**
    * Los usuarios no autenticados permanecen en la pantalla de Login (`MainActivity`).
    * Los usuarios autenticados son dirigidos automáticamente al Menú Principal (`MenuActivity`).
* **Menú Principal:**
    * Pantalla de **Información de Cuenta** (`AccountInfoActivity`) que muestra los datos del usuario logueado.
    * Pantalla de **Activador ESP32** (`Esp32Activity`) como un placeholder visual para futuras integraciones de IoT.
    * Botón de **Cerrar Sesión** que limpia la sesión de Firebase/Google y regresa al Login.

## Stack Tecnológico

* **Lenguaje:** Kotlin
* **UI:** Android XML Layouts
* **Binding:** ViewBinding (para vincular vistas de XML a código Kotlin)
* **Backend:** Firebase
    * Firebase Authentication
* **SDKs:** Google Sign-In

## Configuración del Proyecto (¡Importante!)

Para ejecutar este proyecto, es **obligatorio** conectarlo a tu propio proyecto de Firebase. La app no funcionará sin los siguientes pasos.

### 1. Crear Proyecto en Firebase

1.  Ve a la [Consola de Firebase](https://console.firebase.google.com/).
2.  Crea un nuevo proyecto (ej. "Dpresik2").
3.  Dentro del proyecto, añade una nueva aplicación de Android.
4.  Usa el nombre de paquete exacto: `com.example.dpresik2`.

### 2. Habilitar Métodos de Autenticación

1.  En la consola de Firebase, ve a la sección **Authentication**.
2.  En la pestaña **Sign-in method**, habilita los siguientes proveedores:
    * **Google** (asegúrate de proveer un email de soporte).
    * **Email/Contraseña**.

### 3. Generar y Añadir Huella SHA-1 (Paso Crítico)

Firebase necesita la "huella digital" (SHA-1) de tu máquina de desarrollo para permitir el inicio de sesión con Google.

1.  En Android Studio, abre la pestaña **Terminal** (en la barra inferior).
2.  Ejecuta el siguiente comando para generar el reporte de firmas:
    ```bash
    .\gradlew signingReport
    ```
3.  Busca en la salida el bloque correspondiente a `Variant: debug`.
4.  Copia el valor de la huella **SHA1** (ej. `BB:EF:D3:...:81:85`).
5.  Ve a la **Configuración de tu proyecto** en Firebase (icono de engranaje ⚙️).
6.  Baja a la sección "Tus apps" y selecciona `com.example.dpresik2`.
7.  Baja a **"Huellas digitales de certificado SHA"** y haz clic en **"Añadir huella"**.
8.  Pega la huella `SHA1` que copiaste y guarda.

### 4. Obtener el Archivo de Configuración

1.  **DESPUÉS** de añadir la huella SHA-1, en la misma página de configuración, descarga la versión más reciente del archivo `google-services.json`.
2.  Coloca este archivo dentro de la carpeta `app/` de tu proyecto en Android Studio (al mismo nivel que `build.gradle.kts`).

### 5. Configurar el Client ID

1.  Abre el `google-services.json` que acabas de descargar.
2.  Busca el `client_id` que tenga un `client_type` de `3`.
3.  Copia ese valor.
4.  Ve a `app/src/main/res/values/strings.xml` y pega ese ID en el string `default_web_client_id`.

## Ejecutar la App

1.  Abre el proyecto en Android Studio.
2.  Asegúrate de haber colocado el `google-services.json` (Paso 4).
3.  Presiona **"Sync Now"** para que Gradle descargue todas las dependencias de Firebase.
4.  Ejecuta la app (▶️) en un emulador o dispositivo físico.

¡El inicio de sesión de Google y Email debería funcionar!
