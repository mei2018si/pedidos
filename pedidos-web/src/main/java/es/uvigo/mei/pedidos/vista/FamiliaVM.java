package es.uvigo.mei.pedidos.vista;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zkplus.jpa.JpaUtil;

import es.uvigo.mei.pedidos.entidades.Familia;
import es.uvigo.mei.pedidos.servicios.FamiliaDAO;

public class FamiliaVM {
	private List<Familia> familias;
	private Familia familiaActual;
	private boolean edicionNuevaFamilia;

	private FamiliaDAO familiaDAO;

	
	
	public List<Familia> getFamilias() {
		return familias;
	}

	public void setFamilias(List<Familia> familias) {
		this.familias = familias;
	}

	public Familia getFamiliaActual() {
		return familiaActual;
	}

	public void setFamiliaActual(Familia familiaActual) {
		this.familiaActual = familiaActual;
	}

	public boolean isEdicionNuevaFamilia() {
		return edicionNuevaFamilia;
	}

	public void setEdicionNuevaFamilia(boolean edicionNuevaFamilia) {
		this.edicionNuevaFamilia = edicionNuevaFamilia;
	}

	@Init
	public void inicializar() {
		familiaDAO = new FamiliaDAO(JpaUtil.getEntityManager());

		recargarFamilias();
		familiaActual = null;
		edicionNuevaFamilia = false;
	}

	private void recargarFamilias() {
		familias = familiaDAO.buscarTodas();
	}

	@Command
	@NotifyChange("familias")
	public void eliminar(@BindingParam("familiaEliminar") Familia familia) {
		if (familia != null) {
			familiaDAO.eliminar(familia);
			recargarFamilias();
		}
	}

	@Command
	@NotifyChange("familiaActual")
	public void editar(@BindingParam("familiaEditar") Familia familia) {
		if (familia != null) {
			familiaActual = familia;
			edicionNuevaFamilia = false;
		}
	}
	
	@Command
	@NotifyChange("familiaActual")
	public void nuevaFamilia() {
		familiaActual = new Familia();
		edicionNuevaFamilia = true;
	}

	@Command
	@NotifyChange({ "familiaActual", "familias" })
	public void guardarFamilia() {
		if (edicionNuevaFamilia) {
			familiaDAO.crear(familiaActual);
		} else {
			familiaDAO.actualizar(familiaActual);
		}
		familiaActual = null;
		edicionNuevaFamilia = false;
		recargarFamilias();
	}

	@Command
	@NotifyChange("familiaActual")
	public void cancelarEdicion() {
		familiaActual = null;
		edicionNuevaFamilia = false;
	}

}
