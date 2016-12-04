package pl.projectdcm.gui;

import java.sql.Statement;

import pl.projectdcm.data.DBManager;

public class Staff {

	
	void addSampleEntries() {
				
		sendQuery("CREATE TABLE pytania ( id INT (4) PRIMARY KEY , pytanie VARCHAR (200) ) ;");
		
		sendQuery("CREATE TABLE odpowiedzi ( id INT (4) PRIMARY KEY , odpowiedz VARCHAR (100),"
				+ " rank INT (4), idp INT (4) ) ;");
		
		sendQuery("INSERT INTO pytania ( id , pytanie ) VALUES ( 1 , 'Jaki smak kisielu smakuje wam najbardziej?' ) ;");
				
		sendQuery("INSERT INTO odpowiedzi ( id , odpowiedz , rank , idp ) VALUES ( 1 , 'Malinowy' , 2 , 1 ) ;");
		sendQuery("INSERT INTO odpowiedzi ( id , odpowiedz , rank , idp ) VALUES ( 2 , 'Truskawkowy' , 8 , 1 ) ;");
		sendQuery("INSERT INTO odpowiedzi ( id , odpowiedz , rank , idp ) VALUES ( 3 , 'Lubi� o smaku owoc�w le�nych :)' , 13 , 1 ) ;");
		
		sendQuery("INSERT INTO pytania ( id , pytanie ) VALUES ( 2 , 'Z czym kojarzy Ci sie srodek transportu?' ) ;");
		
		sendQuery("INSERT INTO odpowiedzi ( id , odpowiedz , rank , idp ) VALUES ( 4 , 'Podroze.' , 9 , 2 ) ;");
		sendQuery("INSERT INTO odpowiedzi ( id , odpowiedz , rank , idp ) VALUES ( 5 , 'Wakacje ?' , 11 , 2 ) ;");
		sendQuery("INSERT INTO odpowiedzi ( id , odpowiedz , rank , idp ) VALUES ( 6 , 'Nuda' , 2 , 2 ) ;");
		
		
		sendQuery("INSERT INTO pytania ( id , pytanie ) VALUES ( 3 , 'Co to znaczy wedlug ciebie, byc wolnym?' ) ;");
		
		sendQuery("INSERT INTO odpowiedzi ( id , odpowiedz , rank , idp ) VALUES ( 7 , 'Zyc w anarchokapitalizmie.' , 7 , 3 ) ;");
		sendQuery("INSERT INTO odpowiedzi ( id , odpowiedz , rank , idp ) VALUES ( 8 , 'Zycie bez obowiazkow' , 11 , 3 ) ;");
		sendQuery("INSERT INTO odpowiedzi ( id , odpowiedz , rank , idp ) VALUES ( 9 , 'Latawiec' , 6 , 3 ) ;");
		
	
		
	}

	protected void sendQuery(String query) {
		try {

			Statement stmt = DBManager.getConnection().createStatement();
			stmt.execute(query);
			stmt.close();

		} catch (Exception e) {

		}
	}
}
