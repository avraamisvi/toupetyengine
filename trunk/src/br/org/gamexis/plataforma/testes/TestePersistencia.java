package br.org.gamexis.plataforma.testes;

import java.util.ArrayList;
import java.util.List;

import org.neodatis.odb.main.ODB;
import org.neodatis.odb.main.ODBFactory;

public class TestePersistencia {	
	List<String> lst = new ArrayList<String>();
	public TestePersistencia() {
		lst.add("val1");
		lst.add("val2");
		lst.add("val3");
	}

	public static void main(String[] args) {
		TestePersistencia t = new TestePersistencia();

		try {
			ODB odb = ODBFactory.open("testeSave.salvo");
			odb.store(t);
			odb.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
}
