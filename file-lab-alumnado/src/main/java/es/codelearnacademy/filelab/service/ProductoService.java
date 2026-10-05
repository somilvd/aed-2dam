package es.codelearnacademy.filelab.service;

import es.codelearnacademy.filelab.model.Producto;
import es.codelearnacademy.filelab.repository.IProductoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class ProductoService {

    private final IProductoRepository repository;

    public ProductoService(IProductoRepository repository) {
        this.repository = repository;
    }

    public Optional<Producto> maximoPrecio() {
        List<Producto> productos = repository.findAll();

        if (productos.isEmpty()) {
            return Optional.empty();
        }
        Producto maximo = productos.get(0);
        for (Producto producto : productos) {
            if (producto.precio() > maximo.precio()) {
                maximo = producto;
            }
        }
        return Optional.of(maximo);
    }

    public Optional<Producto> minimoPrecio() {
        List<Producto> productos = repository.findAll();

        if (productos.isEmpty()) {
            return Optional.empty();
        }
        Producto minimo = productos.get(0);
        for (Producto producto : productos) {
            if (producto.precio() < minimo.precio()) {
                minimo = producto;
            }
        }
        return Optional.of(minimo);
    }

    public Optional<Producto> maximoStock() {
        List<Producto> productos = repository.findAll();

        if (productos.isEmpty()) {
            return Optional.empty();
        }
        Producto maximo = productos.get(0);
        for (Producto producto : productos) {
            if (producto.stock() > maximo.stock()) {
                maximo = producto;
            }
        }
        return Optional.of(maximo);
    }

    public Optional<Producto> minimoStock() {
        List<Producto> productos = repository.findAll();

        if (productos.isEmpty()) {
            return Optional.empty();
        }
        Producto minimo = productos.get(0);
        for (Producto producto : productos) {
            if (producto.stock() < minimo.stock()) {
                minimo = producto;
            }
        }
        return Optional.of(minimo);
    }

    public int stockTotal() {
        int total = 0;

        for (Producto producto : repository.findAll()) {
            total += producto.stock();
        }

        return total;
    }

    public double valorInventario() {
        double total = 0.0;

        for (Producto producto : repository.findAll()) {
            total += producto.precio() * producto.stock();
        }

        return total;
    }

    public List<Producto> sinStock() {
        List<Producto> resultado = new ArrayList<>();

        for (Producto producto : repository.findAll()) {
            if (producto.stock() == 0){
                resultado.add(producto);
            }
        }
        return resultado;
    }

    public List<Producto> buscar(String texto) {
        List<Producto> resultado = new ArrayList<>();

        for (Producto producto : repository.findAll()) {
            if (producto.nombre().toLowerCase().contains(texto.toLowerCase().trim())) {
                resultado.add(producto);
            }
        }

        return resultado;
    }
}
