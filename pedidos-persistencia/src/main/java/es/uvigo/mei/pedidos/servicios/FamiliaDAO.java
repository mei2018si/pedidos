package es.uvigo.mei.pedidos.servicios;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.RollbackException;
import javax.persistence.TypedQuery;

import es.uvigo.mei.pedidos.entidades.Familia;

public class FamiliaDAO {
	private EntityManager em;

	public FamiliaDAO(EntityManager em) {
		super();
		this.em = em;
	}

	public Familia crear(Familia nuevoFamilia) throws RollbackException {
		Familia familiaCreado = null;
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			em.persist(nuevoFamilia);
			familiaCreado = nuevoFamilia;
			tx.commit();
		} catch (Exception ex) {
			if ((tx != null) && (tx.isActive())) {
				tx.rollback();
				throw new RollbackException(ex);
			}
		}
		return familiaCreado;
	}

	public Familia actualizar(Familia familia) {
		Familia familiaModificado = null;

		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			familiaModificado = em.merge(familia);
			tx.commit();
		} catch (Exception ex) {
			if ((tx != null) && (tx.isActive())) {
				tx.rollback();
				throw new RollbackException(ex);
			}
		}
		return familiaModificado;
	}

	public void eliminar(Familia familia) {
		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			em.remove(familia);
			tx.commit();
		} catch (Exception ex) {
			if ((tx != null) && (tx.isActive())) {
				tx.rollback();
				throw new RollbackException(ex);
			}
		}
	}

	public Familia buscarPorId(Long id) {
		return em.find(Familia.class, id);
	}

	public List<Familia> buscarTodas() {
		TypedQuery<Familia> q = em.createQuery("SELECT f FROM Familia AS f", Familia.class);
		return q.getResultList();
	}

	public List<Familia> buscarPorNombre(String patron) {
		TypedQuery<Familia> q = em.createQuery("SELECT f FROM Familia AS f WHERE f.nombre LIKE :patron", Familia.class);
		q.setParameter("patron", "%" + patron + "%");
		return q.getResultList();
	}


}
