package es.uvigo.mei.pedidos.servicios;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.RollbackException;
import javax.persistence.TypedQuery;

import es.uvigo.mei.pedidos.entidades.Almacen;
import es.uvigo.mei.pedidos.entidades.Articulo;
import es.uvigo.mei.pedidos.entidades.ArticuloAlmacen;
import es.uvigo.mei.pedidos.entidades.ArticuloAlmacenId;
import net.bytebuddy.implementation.bytecode.Throw;

public class AlmacenDAO {
	private EntityManager em;

	public AlmacenDAO(EntityManager em) {
		super();
		this.em = em;
	}

	public Almacen crear(Almacen nuevoAlmacen) throws RollbackException {
		Almacen almacenCreado = null;
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			em.persist(nuevoAlmacen);
			almacenCreado = nuevoAlmacen;
			tx.commit();
		} catch (Exception ex) {
			if ((tx != null) && (tx.isActive())) {
				tx.rollback();
				throw new RollbackException(ex);
			}
		}
		return almacenCreado;
	}

	public Almacen actualizar(Almacen almacen) {
		Almacen almacenModificado = null;

		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			almacenModificado = em.merge(almacen);
			tx.commit();
		} catch (Exception ex) {
			if ((tx != null) && (tx.isActive())) {
				tx.rollback();
				throw new RollbackException(ex);
			}
		}
		return almacenModificado;
	}

	public void eliminar(Almacen almacen) {
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			em.remove(almacen);
			tx.commit();
		} catch (Exception ex) {
			if ((tx != null) && (tx.isActive())) {
				tx.rollback();
				throw new RollbackException(ex);
			}
		}
	}

	public Almacen buscarPorId(Long id) {
		return em.find(Almacen.class, id);
	}

	public List<Almacen> buscarTodos() {
		TypedQuery<Almacen> q = em.createQuery("SELECT a FROM Almacen AS a", Almacen.class);
		return q.getResultList();
	}

	public List<Almacen> buscarPorNombre(String patron) {
		TypedQuery<Almacen> q = em.createQuery("SELECT a FROM Almacen AS a WHERE a.nombre LIKE :patron", Almacen.class);
		q.setParameter("patron", "%" + patron + "%");
		return q.getResultList();
	}

	public List<Almacen> buscarPorLocalidad(String localidad) {
		TypedQuery<Almacen> q = em.createQuery("SELECT a FROM Almacen AS a WHERE a.direccion.localidad LIKE :localidad",
				Almacen.class);
		q.setParameter("localidad", "%" + localidad + "%");
		return q.getResultList();
	}

	public List<ArticuloAlmacen> buscarStockArticulos(Almacen almacen) {
		TypedQuery<ArticuloAlmacen> q = em.createQuery(
				"SELECT aa FROM ArticuloAlmacen AS aa WHERE aa.almacen.id = :idAlmacen", ArticuloAlmacen.class);
		q.setParameter("idAlmacen", almacen.getId());
		return q.getResultList();
	}

	public ArticuloAlmacen actualizarStockArticulo(Articulo articulo, Almacen almacen, int nuevoStock) {
		ArticuloAlmacen stockArticuloModificado = null;

		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			ArticuloAlmacen stockArticulo = em.find(ArticuloAlmacen.class,
					new ArticuloAlmacenId(articulo.getId(), almacen.getId()));
			if (stockArticulo != null) {
				stockArticulo.setStock(nuevoStock);
				stockArticuloModificado = em.merge(stockArticulo);
			}
			tx.commit();
		} catch (Exception ex) {
			if ((tx != null) && (tx.isActive())) {
				tx.rollback();
				throw new RollbackException(ex);
			}
		}
		return stockArticuloModificado;
	}

	public ArticuloAlmacen crearStockArticulo(Articulo articulo, Almacen almacen, int stock) {
		ArticuloAlmacen stockArticuloCreado = null;

		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			ArticuloAlmacen stockArticulo = em.find(ArticuloAlmacen.class,
					new ArticuloAlmacenId(articulo.getId(), almacen.getId()));
			if (stockArticulo == null) {
				// No exite stock del producto en el almacen
				stockArticulo = new ArticuloAlmacen(articulo, almacen, stock);
				em.persist(stockArticulo);
				stockArticuloCreado = stockArticulo;
			} else {
				actualizarStockArticulo(articulo, almacen, stock);
			}
			tx.commit();
		} catch (Exception ex) {
			if ((tx != null) && (tx.isActive())) {
				tx.rollback();
				throw new RollbackException(ex);
			}
		}
		return stockArticuloCreado;
	}

	public void eliminarStockArticulo(Articulo articulo, Almacen almacen) {
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			ArticuloAlmacen stockArticulo = em.find(ArticuloAlmacen.class,
					new ArticuloAlmacenId(articulo.getId(), almacen.getId()));
			if (stockArticulo != null) {
				em.remove(stockArticulo);
			}
			tx.commit();
		} catch (Exception ex) {
			if ((tx != null) && (tx.isActive())) {
				tx.rollback();
				throw new RollbackException(ex);
			}
		}
	}
}
