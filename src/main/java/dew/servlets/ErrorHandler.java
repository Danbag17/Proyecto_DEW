package dew.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ErrorHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        Integer statusCode = (Integer) request.getAttribute(
                RequestDispatcher.ERROR_STATUS_CODE);

        String title;
        String message;

        switch (statusCode != null ? statusCode : 404) {
            case 403:
                title = "Acceso denegado";
                message = "No tienes permisos para acceder a este recurso.";
                break;                
            
            case 500:
                title = "Error interno del servidor";
                message = "Ha ocurrido un error inesperado. Inténtalo más tarde.";
                break;

            default:
                statusCode = 404;
                title = "Página no encontrada";
                message = "La página que buscas no existe. Verifica la dirección y vuelve a intentarlo.";
        }

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        String html = """
        		<!doctype html>
        		<html lang="es">
        		<head>
        		  <meta charset="utf-8">
        		  <title>%d - %s</title>
        		  <meta name="viewport" content="width=device-width, initial-scale=1">
        		  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        		  <link rel="stylesheet" href="/DEW/css/nol.css">
        		</head>
        		<body class="bg-light">
        		  <main class="container py-5">
        		    <div class="card shadow-sm rounded-4">
        		      <div class="card-body p-5 text-center">
        		        <div class="display-1 fw-bold text-danger mb-3">%d</div>

        		        <h1 class="h2 mb-3">%s</h1>

        		        <p class="text-muted mb-4">%s</p>

        		        <div class="d-grid gap-3 mx-auto" style="max-width: 360px;">
        		          <a href="/DEW/index.html" class="btn btn-primary w-100">
        		            Ir al inicio
        		          </a>

        		        </div>
        		      </div>
        		    </div>
        		  </main>
        		</body>
        		</html>
        		""".formatted(statusCode, title, statusCode, title, message);

        	out.print(html);
    }
}