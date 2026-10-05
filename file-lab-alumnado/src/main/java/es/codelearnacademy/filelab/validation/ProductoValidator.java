package es.codelearnacademy.filelab.validation;

import es.codelearnacademy.filelab.model.Producto;

public final class ProductoValidator {

    private ProductoValidator() {
    }

    public static void validar(Producto producto) {
        if (producto.id() <= 0 || producto == null
                || producto.nombre() == null || producto.nombre().trim().isBlank()
                || producto.precio() <= 0 || producto.stock() <= 0) {
            throw new IllegalArgumentException();
        }
    }
}
