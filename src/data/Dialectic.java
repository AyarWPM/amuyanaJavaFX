package data;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Dialectic{
	private IntegerProperty idDialectic;
	private IntegerProperty orientation;
	private IntegerProperty level;

	public Dialectic(int idDialectic, int orientation, int level) { 
		this.idDialectic = new SimpleIntegerProperty(idDialectic);
		this.orientation = new SimpleIntegerProperty(orientation);
		this.level = new SimpleIntegerProperty(level);
	}

	//Metodos atributo: idDialectic
	public int getIdDialectic() {
		return idDialectic.get();
	}
	public void setIdDialectic(int idDialectic) {
		this.idDialectic = new SimpleIntegerProperty(idDialectic);
	}
	public IntegerProperty IdDialecticProperty() {
		return idDialectic;
	}
	//Metodos atributo: orientation
	public int getOrientation() {
		return orientation.get();
	}
	public void setOrientation(int orientation) {
		this.orientation = new SimpleIntegerProperty(orientation);
	}
	public IntegerProperty OrientationProperty() {
		return orientation;
	}
	//Metodos atributo: level
	public int getLevel() {
		return level.get();
	}
	public void setLevel(int level) {
		this.level = new SimpleIntegerProperty(level);
	}
	public IntegerProperty LevelProperty() {
		return level;
	}
}