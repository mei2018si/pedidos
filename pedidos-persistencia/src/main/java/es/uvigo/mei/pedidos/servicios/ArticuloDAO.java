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
import es.uvigo.mei.pedidos.entidades.Familia;

public class ArticuloDAO {
	private EntityManager em;

	public ArticuloDAO(EntityManager em) {
		super();
		this.em = em;
	}

	public Articulo crear(Articulo nuevoArticulo) throws RollbackException {
		Articulo articuloCreado = null;
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			em.persist(nuevoArticulo);
			articuloCreado = nuevoArticulo;
			tx.commit();
		} catch (Exception ex) {
			if ((tx != null) && (tx.isActive())) {
				tx.rollback();
				throw new RollbackException(ex);
			}
		}
		return articuloCreado;
	}

	public Articulo actualizar(Articulo articulo) {
		Articulo articuloModificado = null;

		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			articuloModificado = em.merge(articulo);
			tx.commit();
		} catch (Exception ex) {
			if ((tx != null) && (tx.isActive())) {
				tx.rollback();
				throw new RollbackException(ex);
			}
		}
		return articuloModificado;
	}

	public void eliminar(Articulo articulo) {
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			em.remove(articulo);
			tx.commit();
		} catch (Exception ex) {
			if ((tx != null) && (tx.isActive())) {
				tx.rollback();
				throw new RollbackException(ex);
			}
		}
	}

	public Articulo buscarPorId(Long id) {
		return em.find(Articulo.class, id);
	}

	public List<Articulo> buscarTodos() {
		TypedQuery<Articulo> q = em.createQuery("SELECT a FROM Articulo AS a", Articulo.class);
		return q.getResultList();
	}

	public List<Articulo> buscarPorNombre(String patron) {
		TypedQuery<Articulo> q = em.createQuery("SELECT a FROM Articulo AS a WHERE a.nombre LIKE :patron",
				Articulo.class);
		q.setParameter("patron", "%" + patron + "%");
		return q.getResultList();
	}

	public List<Articulo> buscarPorNombreFamilia(String patron) {
		TypedQuery<Articulo> q = em.createQuery("SELECT a FROM Articulo AS a WHERE a.familia.nombre LIKE :patron",
				Articulo.class);
		q.setParameter("patron", "%" + patron + "%");
		return q.getResultList();
	}

	public List<Articulo> buscarPorFamilia(Familia familia) {
		TypedQuery<Articulo> q = em.createQuery("SELECT a FROM Articulo AS a WHERE a.familia.id = :idFamilia",
				Articulo.class);
		q.setParameter("idFamilia", familia.getId());
		return q.getResultList();
	}
	
	
	public List<ArticuloAlmacen> buscarStockArticulo(Articulo articulo) {
		TypedQuery<ArticuloAlmacen> q = em.createQuery(
				"SELECT aa FROM ArticuloAlmacen AS aa WHERE aa.articulo.id = :idArticulo", ArticuloAlmacen.class);
		q.setParameter("idArticulo", articulo.getId());
		return q.getResultList();
	}

}
