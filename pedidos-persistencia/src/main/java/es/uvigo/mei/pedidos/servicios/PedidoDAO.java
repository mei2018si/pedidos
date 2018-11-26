package es.uvigo.mei.pedidos.servicios;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.RollbackException;
import javax.persistence.TypedQuery;

import es.uvigo.mei.pedidos.entidades.Articulo;
import es.uvigo.mei.pedidos.entidades.Cliente;
import es.uvigo.mei.pedidos.entidades.Pedido;

public class PedidoDAO {
	private EntityManager em;

	public PedidoDAO(EntityManager em) {
		super();
		this.em = em;
	}

	public Pedido crear(Pedido nuevoPedido) throws RollbackException {
		Pedido pedidoCreado = null;
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			em.persist(nuevoPedido);
			pedidoCreado = nuevoPedido;
			tx.commit();
		} catch (Exception ex) {
			if ((tx != null) && (tx.isActive())) {
				tx.rollback();
				throw new RollbackException(ex);
			}
		}
		return pedidoCreado;
	}

	public Pedido actualizar(Pedido pedido) {
		Pedido pedidoModificado = null;

		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			pedidoModificado = em.merge(pedido);
			tx.commit();
		} catch (Exception ex) {
			if ((tx != null) && (tx.isActive())) {
				tx.rollback();
				throw new RollbackException(ex);
			}
		}
		return pedidoModificado;
	}

	public void eliminar(Pedido pedido) {
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			em.remove(pedido);
			tx.commit();
		} catch (Exception ex) {
			if ((tx != null) && (tx.isActive())) {
				tx.rollback();
				throw new RollbackException(ex);
			}
		}
	}

	public Pedido buscarPorId(Long id) {
		return em.find(Pedido.class, id);
	}

	public List<Pedido> buscarTodos() {
		TypedQuery<Pedido> q = em.createQuery("SELECT p FROM Pedido AS p", Pedido.class);
		return q.getResultList();
	}

	public List<Pedido> buscarPorCliente(Cliente cliente) {
		TypedQuery<Pedido> q = em.createQuery("SELECT p FROM Pedido AS p WHERE p.cliente.dni = :dniCliente", Pedido.class);
		q.setParameter("dniCliente", cliente.getDNI());
		return q.getResultList();
	}
	
	public List<Pedido> buscarPorArticulo(Articulo articulo) {
		TypedQuery<Pedido> q = em.createQuery("SELECT p FROM Pedido AS p JOIN p.lineas AS l WHERE l.articulo.id = :idArticulo", Pedido.class);
		q.setParameter("idArticulo", articulo.getId());
		return q.getResultList();
	}
}
