package es.uvigo.mei.pedidos.vista;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zkplus.jpa.JpaUtil;

import es.uvigo.mei.pedidos.entidades.Articulo;
import es.uvigo.mei.pedidos.entidades.ArticuloAlmacen;
import es.uvigo.mei.pedidos.entidades.Familia;
import es.uvigo.mei.pedidos.servicios.ArticuloDAO;
import es.uvigo.mei.pedidos.servicios.FamiliaDAO;

public class ArticuloVM {
	private List<Articulo> articulos;
	private Articulo articuloActual;
	private String textoBusqueda;
	private boolean edicionNuevoArticulo;
	private List<Familia> familias;
	private Familia familiaSeleccionada;
	
	private ArticuloDAO articuloDAO;
	private FamiliaDAO familiaDAO;
	
	@Init
	public void inicializar() {
		articuloDAO = new ArticuloDAO(JpaUtil.getEntityManager());
		
		articulos = articuloDAO.buscarTodos();
		articuloActual = null;
		edicionNuevoArticulo = false;
		
		familiaDAO = new FamiliaDAO(JpaUtil.getEntityManager());
		familias = familiaDAO.buscarTodas();
		familiaSeleccionada = null;
	}

	public List<Articulo> getArticulos() {
		return articulos;
	}

	public void setArticulos(List<Articulo> articulos) {
		this.articulos = articulos;
		articuloActual = null;
	}

	public Articulo getArticuloActual() {
		return articuloActual;
	}

	public void setArticuloActual(Articulo articuloActual) {
		this.articuloActual = articuloActual;
	}

	public String getTextoBusqueda() {
		return textoBusqueda;
	}

	public void setTextoBusqueda(String textoBusqueda) {
		this.textoBusqueda = textoBusqueda;
	}
	
	

	public boolean isEdicionNuevoArticulo() {
		return edicionNuevoArticulo;
	}

	public void setEdicionNuevoArticulo(boolean edicionNuevoArticulo) {
		this.edicionNuevoArticulo = edicionNuevoArticulo;
	}

	public Familia getFamiliaSeleccionada() {
		return familiaSeleccionada;
	}

	public void setFamiliaSeleccionada(Familia familiaSeleccionada) {
		this.familiaSeleccionada = familiaSeleccionada;
	}

	public List<Familia> getFamilias() {
		return familias;
	}

	public List<ArticuloAlmacen> recuperarStockArticuloActual() {
		if (articuloActual != null) {
			return articuloDAO.buscarStockArticulo(articuloActual);
		}		
		else {
			return Collections.emptyList();			
		}
	}
	
	@Command
	@NotifyChange("articulos")
	public void buscarID() {
		if ((textoBusqueda != null) && !textoBusqueda.isEmpty()) {
			articulos = new ArrayList<>();
			articulos.add(articuloDAO.buscarPorId(Long.parseLong(textoBusqueda)));
			articuloActual = null;
		}
	}

	@Command
	@NotifyChange("articulos")
	public void buscarNombre() {
		if ((textoBusqueda != null) && !textoBusqueda.isEmpty()) {
			articulos = articuloDAO.buscarPorNombre(textoBusqueda);
			articuloActual = null;
		}
	}

	@Command
	@NotifyChange("articulos")
	public void buscarFamilia() {
		if (familiaSeleccionada != null) {
			articulos = articuloDAO.buscarPorFamilia(familiaSeleccionada);
			articuloActual = null;
		}
	}

	@Command
	@NotifyChange("articulos")
	public void buscarTodos() {
		articulos = articuloDAO.buscarTodos();
		articuloActual = null;
	}

	@Command
	@NotifyChange("articuloActual")
	public void editar(@BindingParam("articuloEditar") Articulo articulo) {
		if (articulo != null) {
			articuloActual = articulo;
			edicionNuevoArticulo = false;
		}
	}

	@Command
	@NotifyChange("articulos")
	public void eliminar(@BindingParam("articuloEliminar") Articulo articulo) {
		if (articulo != null) {
			articuloDAO.eliminar(articulo);
			articulos.remove(articulo);
		}
	}

	@Command
	@NotifyChange("articuloActual")
	public void nuevoArticulo() {
		articuloActual = new Articulo();
		edicionNuevoArticulo = true;
	}

	@Command
	@NotifyChange("articuloActual")
	public void guardarArticulo() {
		if (edicionNuevoArticulo) {
			articuloDAO.crear(articuloActual);
		} else {
			articuloDAO.actualizar(articuloActual);
		}
		articuloActual = null;
		edicionNuevoArticulo = false;
	}

	@Command
	@NotifyChange("articuloActual")
	public void cancelarEdicion() {
		articuloActual = null;
		edicionNuevoArticulo = false;
	}

}
