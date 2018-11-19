package es.uvigo.mei.pedidos.vista;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zkplus.jpa.JpaUtil;

import es.uvigo.mei.pedidos.entidades.Cliente;
import es.uvigo.mei.pedidos.entidades.Direccion;
import es.uvigo.mei.pedidos.servicios.ClienteDAO;

public class ClienteVM {
	private List<Cliente> clientes;
	private Cliente clienteActual;
	private String textoBusqueda;
	private boolean edicionNuevoCliente;

	private ClienteDAO clienteDAO;

	@Init
	public void inicializar() {
		clienteDAO = new ClienteDAO(JpaUtil.getEntityManager());
		
		clientes = clienteDAO.buscarTodos();
		clienteActual = null;
		edicionNuevoCliente = false;
	}

	public List<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
		clienteActual = null;
	}

	public Cliente getClienteActual() {
		return clienteActual;
	}

	public void setClienteActual(Cliente clienteActual) {
		this.clienteActual = clienteActual;
	}

	public String getTextoBusqueda() {
		return textoBusqueda;
	}

	public void setTextoBusqueda(String textoBusqueda) {
		this.textoBusqueda = textoBusqueda;
	}

	@Command
	@NotifyChange("clientes")
	public void buscarDNI() {
		if ((textoBusqueda != null) && !textoBusqueda.isEmpty()) {
			clientes = new ArrayList<>();
			clientes.add(clienteDAO.buscarPorDNI(textoBusqueda));
			clienteActual = null;
		}
	}

	@Command
	@NotifyChange("clientes")
	public void buscarNombre() {
		if ((textoBusqueda != null) && !textoBusqueda.isEmpty()) {
			clientes = clienteDAO.buscarPorNombre(textoBusqueda);
			clienteActual = null;
		}
	}

	@Command
	@NotifyChange("clientes")
	public void buscarLocalidad() {
		if ((textoBusqueda != null) && !textoBusqueda.isEmpty()) {
			clientes = clienteDAO.buscarPorLocalidad(textoBusqueda);
			clienteActual = null;
		}
	}

	@Command
	@NotifyChange("clientes")
	public void buscarTodos() {
		clientes = clienteDAO.buscarTodos();
		clienteActual = null;
	}

	@Command
	@NotifyChange("clienteActual")
	public void editar(@BindingParam("clienteEditar") Cliente cliente) {
		if (cliente != null) {
			clienteActual = cliente;
		}
	}

	@Command
	@NotifyChange("clientes")
	public void eliminar(@BindingParam("clienteEliminar") Cliente cliente) {
		if (cliente != null) {
			clienteDAO.eliminar(cliente);
			clientes.remove(cliente);
		}
	}

	@Command
	@NotifyChange("clienteActual")
	public void nuevoCliente() {
		clienteActual = new Cliente();
		clienteActual.setDireccion(new Direccion());
		edicionNuevoCliente = true;
	}

	@Command
	@NotifyChange("clienteActual")
	public void guardarCliente() {
		if (edicionNuevoCliente) {
			clienteDAO.crear(clienteActual);
		} else {
			clienteDAO.actualizar(clienteActual);
		}
		clienteActual = null;
		edicionNuevoCliente = false;
	}

	@Command
	@NotifyChange("clienteActual")
	public void cancelarEdicion() {
		clienteActual = null;
		edicionNuevoCliente = false;
	}

}
