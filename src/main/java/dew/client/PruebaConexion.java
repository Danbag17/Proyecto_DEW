package dew.client;

public class PruebaConexion {
    public static void main(String[] args) {
        try {
            CentroEducativoClient cliente = new CentroEducativoClient();

            // 1. Intentar Login con el usuario administrador predeterminado
            // El DNI es "111111111" y el password "654321" 
            System.out.println("Intentando login...");
            String key = cliente.login("111111111", "654321");

            if (key != null) {
                System.out.println("¡Conexión exitosa!");
                System.out.println("Tu KEY de sesión es: " + key); // [cite: 122]

                // 2. Probar a obtener las asignaturas usando la key obtenida [cite: 125, 247]
                System.out.println("Solicitando lista de asignaturas...");
                String jsonAsignaturas = cliente.getAsignaturas(key);
                System.out.println("Respuesta del servidor: " + jsonAsignaturas);
            } else {
                System.out.println("Error: No se pudo obtener la clave de sesión.");
            }
        } catch (Exception e) {
            System.err.println("Se produjo un error durante la prueba: " + e.getMessage());
            e.printStackTrace();
        }
    }
}