package es.uvigo.mei.pedidos.vista;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zkplus.jpa.JpaUtil;

import es.uvigo.mei.pedidos.entidades.Almacen;
import es.uvigo.mei.pedidos.entidades.Articulo;
import es.uvigo.mei.pedidos.entidades.ArticuloAlmacen;
import es.uvigo.mei.pedidos.entidades.Direccion;
import es.uvigo.mei.pedidos.servicios.AlmacenDAO;
import es.uvigo.mei.pedidos.servicios.ArticuloDAO;

public class AlmacenVM {
	private List<Almacen> almacenes;
	private Almacen almacenActual;
	private String textoBusqueda;
	private boolean edicionNuevoAlmacen;
	private ArticuloAlmacen stockActual;
	private boolean edicionNuevoStock;

	private AlmacenDAO almacenDAO;
	private ArticuloDAO articuloDAO;
	
	@Init
	public void inicializar() {
		almacenDAO = new AlmacenDAO(JpaUtil.getEntityManager());
		
		almacenes = almacenDAO.buscarTodos();
		almacenActual = null;
		edicionNuevoAlmacen = false;
		
		articuloDAO = new ArticuloDAO(JpaUtil.getEntityManager());
	}

	public List<Almacen> getAlmacenes() {
		return almacenes;
	}

	public void setAlmacenes(List<Almacen> almacenes) {
		this.almacenes = almacenes;
		almacenActual = null;
	}

	public Almacen getAlmacenActual() {
		return almacenActual;
	}

	public void setAlmacenActual(Almacen almacenActual) {
		this.almacenActual = almacenActual;
	}

	public String getTextoBusqueda() {
		return textoBusqueda;
	}

	public boolean isEdicionNuevoAlmacen() {
		return edicionNuevoAlmacen;
	}

	public void setEdicionNuevoAlmacen(boolean edicionNuevoAlmacen) {
		this.edicionNuevoAlmacen = edicionNuevoAlmacen;
	}

	public boolean isEdicionNuevoStock() {
		return edicionNuevoStock;
	}

	public void setEdicionNuevoStock(boolean edicionNuevoStock) {
		this.edicionNuevoStock = edicionNuevoStock;
	}

	public void setTextoBusqueda(String textoBusqueda) {
		this.textoBusqueda = textoBusqueda;
	}
	
	public ArticuloAlmacen getStockActual() {
		return stockActual;
	}

	public void setStockActual(ArticuloAlmacen stockActual) {
		this.stockActual = stockActual;
	}

	public List<ArticuloAlmacen> getStocksAlmacenActual() {
		if (almacenActual != null) {
			return almacenDAO.buscarStockArticulos(almacenActual);
		}		
		else {
			return Collections.emptyList();			
		}		
	}
	
	public List<Articulo> recuperarArticulos() {
		return articuloDAO.buscarTodos();		
	}
	
	@Command
	@NotifyChange("almacenes")
	public void buscarID() {
		if ((textoBusqueda != null) && !textoBusqueda.isEmpty()) {
			almacenes = new ArrayList<>();
			almacenes.add(almacenDAO.buscarPorId(Long.parseLong(textoBusqueda)));
			almacenActual = null;
		}
	}

	@Command
	@NotifyChange("almacenes")
	public void buscarNombre() {
		if ((textoBusqueda != null) && !textoBusqueda.isEmpty()) {
			almacenes = almacenDAO.buscarPorNombre(textoBusqueda);
			almacenActual = null;
		}
	}

	@Command
	@NotifyChange("almacenes")
	public void buscarLocalidad() {
		if ((textoBusqueda != null) && !textoBusqueda.isEmpty()) {
			almacenes = almacenDAO.buscarPorLocalidad(textoBusqueda);
			almacenActual = null;
		}
	}

	@Command
	@NotifyChange("almacenes")
	public void buscarTodos() {
		almacenes = almacenDAO.buscarTodos();
		almacenActual = null;
	}

	@Command
	@NotifyChange("almacenActual")
	public void editar(@BindingParam("almacenEditar") Almacen almacen) {
		if (almacen != null) {
			almacenActual = almacen;
			edicionNuevoAlmacen = false;
		}
	}

	@Command
	@NotifyChange("almacenes")
	public void eliminar(@BindingParam("almacenEliminar") Almacen almacen) {
		if (almacen != null) {
			almacenDAO.eliminar(almacen);
			almacenes.remove(almacen);
		}
	}

	@Command
	@NotifyChange("almacenActual")
	public void nuevoAlmacen() {
		almacenActual = new Almacen();
		almacenActual.setDireccion(new Direccion());
		edicionNuevoAlmacen = true;
	}

	@Command
	@NotifyChange("almacenActual")
	public void guardarAlmacen() {
		if (edicionNuevoAlmacen) {
			almacenDAO.crear(almacenActual);
		} else {
			almacenDAO.actualizar(almacenActual);
		}
		almacenActual = null;
		edicionNuevoAlmacen = false;
	}

	@Command
	@NotifyChange("almacenActual")
	public void cancelarEdicion() {
		almacenActual = null;
		edicionNuevoAlmacen = false;
	}

	@Command
	@NotifyChange({"stockActual","edicionNuevoStock"})
	public void nuevoStock() {
		stockActual = new ArticuloAlmacen();
		stockActual.setAlmacen(almacenActual);
		stockActual.setStock(0);
		
		edicionNuevoStock = true;
	}
	@Command
	@NotifyChange({"stockActual","edicionNuevoStock"})
	public void editarStock(@BindingParam("stockEditar") ArticuloAlmacen stock) {
		if (stock != null) {
			stockActual = stock;
			edicionNuevoStock = false;
		}
	}

	@Command
	@NotifyChange("stocksAlmacenActual")
	public void eliminarStock(@BindingParam("StockEliminar") ArticuloAlmacen stock) {
		if (stock != null) {
			almacenDAO.eliminarStockArticulo(stock.getArticulo(), stock.getAlmacen());
		}
	}
	
	@Command
	@NotifyChange({"stockActual","stocksAlmacenActual","edicionNuevoStock"})
	public void guardarStock() {
		if (edicionNuevoStock) {
			almacenDAO.crearStockArticulo(stockActual.getArticulo(), stockActual.getAlmacen(), stockActual.getStock());
		} else {
			almacenDAO.actualizarStockArticulo(stockActual.getArticulo(), stockActual.getAlmacen(), stockActual.getStock());
		}
		stockActual = null;
		edicionNuevoStock = false;
	}

	@Command
	@NotifyChange({"stockActual","edicionNuevoStock"})
	public void cancelarEdicionStock() {
		stockActual = null;
		edicionNuevoStock = false;
	}	
}
