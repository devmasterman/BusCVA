# BusCVA

  Configuración Sugar ORM

    Es necesario desactivar la opción instant run de android studio ya que está opción no es 
    compatible con el framework, para esto debemos de realizar los siguiente:
  
      1.- Abrir Android Studio
      2.- File -> Settings
      3.- Buscar "Intsant Run"
      4.- Desmarcar la opción "Enable Instant run to hot swap ... " 
      
  Configuración  API Goole Maps
  
    En ocaciones es necesario re-configurar la clave API de google maps, 
    ya sea para configurar un nuevo proyecto o bien, para inicarlo en un ambiente nuevo de desarrollo, 
    para esto debemos de realizar lo siguiente:
    
      1.- Crear un nuevo proyecto de Android Studio, seleccionando una plantilla de mapas
      2.- Abrir el archivo google_maps_api.xml y copiar la key SHA1 que aparece en los comentarios
      3.- Se puede obtener la key por consola/terminal, entrando en el home de android studio
      4.- $ cd  ~/.android/
      5.- $keytool -exportcert -alias androiddebugkey -keystore ~/.android/debug.keystore -list -v
      6.- $Enter keystore password:  (Ingresar "android").
      7.- $SHA1: 26:E5:9A:97:D4:9C:6C:CF:B3:20:C9:70:B5:83:68:F3:D4:E4:B7:2D
      8.- Ingresar a la consola de google API y crear la llave con SHA1 y el nombre del 
          paquete del proyecto.
          https://console.developers.google.com/apis/credentials?project=disco-bedrock-162117  
    
    NOTA: Existen en ocaciones en que aunque se agrega la key al console de google api, 
          la apicación sigue sin funcionar, para esto debemos regenerar una nueva google key 
          para esto podemos ingresar lo siguiente:
          https://console.developers.google.com/flows/enableapi?apiid=maps_android_backend&keyType=CLIENT_SIDE_ANDROID&r=BC:28:44:41:14:10:16:C3:E5:D7:4F:DC:FB:D4:D7:95:3B:B7:E2:FD%3Bnet.rutas.morelos.app
          

  Ejecutar apps en un dispositivo de hardware (Linux)
  
    Para ambientes Linux es necesario crear un arhivo de reglas para configurar nuestro dispositivo, 
    para esto debemos:
     
     1.- $cd /etc/udev/rules.d
     2.- $sudo vim 51-android.rules
     3.- Agregar SUBSYSTEM=="usb", ATTR{idVendor}=="0bb4", MODE="0666", GROUP="plugdev" 
     4.- La linea anterior es para la marca HTC
    
    NOTA: Para mas información y obtener los códigos de los modelos acceder a: 
          https://developer.android.com/studio/run/device.html?hl=es-419#VendorIds
